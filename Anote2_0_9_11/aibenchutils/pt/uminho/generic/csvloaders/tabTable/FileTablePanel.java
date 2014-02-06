package pt.uminho.generic.csvloaders.tabTable;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.session.SessionPropertykeys;
import pt.uminho.anote2.aibench.utils.session.SessionSettings;
import pt.uminho.anote2.process.IE.io.export.Delimiter;
import pt.uminho.generic.csvloaders.tabTable.table.celleditor.ComboEditor;
import pt.uminho.generic.csvloaders.tabTable.table.cellrender.ComboRenderer;
import pt.uminho.generic.csvloaders.tabTable.table.headertable.MultiHeaderTableModel;


public class FileTablePanel extends javax.swing.JPanel implements
		ActionListener, TableModelListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JCheckBox hasHeaders;
	private JScrollPane tablePanel;
	private MultiHeaderTableModel multiHeaderModel;
	private JTable FileTable;
	private List<String> lines;
	private Set<String> columnValues;
	private Delimiter separator;
	/**
	 * Auto-generated main method to display this JPanel inside a new JFrame.
	 */



	public FileTablePanel(List<String> lines, Set<String> columnValues) {
		super();
		this.lines = lines;
		this.separator = getSessionDefault();
		if(separator==null)
			separator = Delimiter.COMMA;
		this.columnValues = columnValues;
		initGUI();
	}

	private Delimiter getSessionDefault() {
		String settingsDefaultGeneralDelimiterValue = SessionSettings.getSessionSettings().getSessionProperty(SessionPropertykeys.generalDelimiter);
		if(settingsDefaultGeneralDelimiterValue!=null)
		{
			Delimiter delimiter = Delimiter.getDelimiterByString(settingsDefaultGeneralDelimiterValue);
			return delimiter;
		}
		return null;
	}

	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			this.setPreferredSize(new java.awt.Dimension(549, 300));
			thisLayout.rowWeights = new double[] { 0.1, 0.0 };
			thisLayout.rowHeights = new int[] { 7, 7 };
			thisLayout.columnWeights = new double[] { 0.1, 0.0 };
			thisLayout.columnWidths = new int[] { 7, 7 };
			this.setLayout(thisLayout);

			hasHeaders = new JCheckBox();
			this.add(hasHeaders, new GridBagConstraints(1, 1, 1, 2, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
					new Insets(0, 0, 0, 0), 0, 0));
			hasHeaders.setText("File has headers");
			hasHeaders.addActionListener(this);
			createTable();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Delimiter getSeparator() {
		return separator;
	}

	public void setSeparator(Delimiter separator) {
		this.separator = separator;
	}


	/**
	 * This method will get the headers if the headers aren't for extra info
	 * 
	 * @return The headers
	 */
	public Map<String, Integer> getHeaders() {
		Map<String, Integer> headers = new HashMap<String, Integer>();

		String value;

		for (int j = 0; j < multiHeaderModel.getColumnCount(); j++) {
			value = (String) multiHeaderModel.getHeadersModel()
					.getValueAt(1, j);
			if (!value.equalsIgnoreCase("---") && !value.equalsIgnoreCase(""))
				headers.put(value, j);
		}
		return headers;
	}

	public void createTable() throws Exception {
		if (tablePanel != null)
			this.remove(tablePanel);
		multiHeaderModel = new MultiHeaderTableModel();

		int aux = hasHeaders.isSelected() ? 1 : 0;
		
		if (separator.getValue().length() > 0 && lines.size() > 0) {
			// number of columns in 1st row
//			int numColumns = this.lines.get(0).split(separator.getValue()).length;
			for (int i = aux; i < this.lines.size(); i++) {
				String str = this.lines.get(i);
				// to consider the last field when it's empty
				str = (str.substring(str.length()
						- separator.getValue().length()).equals(separator
						.getValue())) ? str + " " : str;
				String[] strArr = str.split(separator.getValue());
				// if any row has a diferent number of columns throw Exception
//				if (numColumns != strArr.length)
//					throw new Exception("The number of fields in line "
//							+ (i + 1) + " is diferente from the first line!");
				multiHeaderModel.addRow(strArr);
			}

		} else {
			for (int i = aux; i < this.lines.size(); i++) {
				String str = this.lines.get(i);
				String[] data = new String[1];
				data[0] = str;
				multiHeaderModel.addRow(data);
			}

		}

		String[] header = null;
		// SGC - allows a empty table && this.lines.size()>0
		if (hasHeaders.isSelected() && this.lines.size() > 0) {
			String h = (String) this.lines.get(0);
			header = h.split(separator.getValue());
		} else {
			char c = 'A';
			int nCol = multiHeaderModel.getColumnCount();
			header = new String[nCol];
			int j = 0;
			char caux = c;
			for (int i = 0; i < nCol; i++) {
				j = i;
				caux = c;
				while (j > 0) {
					caux++;
					j--;
				}
				header[i] = "" + caux;
			}
		}
		multiHeaderModel.addHeader(header);
		multiHeaderModel
				.addHeader(new String[multiHeaderModel.getColumnCount()]);

		for (int i = 0; i < multiHeaderModel.getColumnCount(); i++) {
			multiHeaderModel.setCellEditor(1, i, new ComboEditor(columnValues.toArray()));
			multiHeaderModel.setCellRenders(1, i, new ComboRenderer(columnValues.toArray()));
			if(columnValues.size()>0)
			{
				multiHeaderModel.getHeadersModel().setValueAt(columnValues.toArray()[0], 1, i);
			}
			else
				multiHeaderModel.getHeadersModel().setValueAt("---", 1, i);
		}

		tablePanel = multiHeaderModel.getMultiHeaderTable(this);
		tablePanel
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(tablePanel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						5, 5, 5, 5), 0, 0));
		this.updateUI();
	}

	public boolean validateValuesChosen() {

		List<String> values = new ArrayList<String>();
		String value;

		for (int j = 0; j < multiHeaderModel.getColumnCount(); j++) {
			value = (String) multiHeaderModel.getHeadersModel()
					.getValueAt(1, j);
			if (!value.equalsIgnoreCase("---")) {
				if (value != null && !value.equals(""))
					if (!values.contains(value))
						values.add(value);
					else
						return false;
			}
		}
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			createTable();
		} catch (Exception ex) {
			TreatExceptionForAIbench.treatExcepion(ex);
		}
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		int firstRow = e.getFirstRow();
		int column = e.getColumn();

		if (firstRow == 1) {
			multiHeaderModel.setEditable(0, column, true);;
		}
		updateUI();
	}
	
	public boolean getHasHeaders() {
		return hasHeaders.isSelected();
	}

	public void setHasHeaders(JCheckBox hasHeaders) {
		this.hasHeaders = hasHeaders;
	}

	public void setHasHeaders(boolean selected) {
		this.hasHeaders.setSelected(selected);
	}

	public JScrollPane getTablePanel() {
		return tablePanel;
	}

	public void setTablePanel(JScrollPane tablePanel) {
		this.tablePanel = tablePanel;
	}

	public JTable getFileTable() {
		return FileTable;
	}

	public void setFileTable(JTable fileTable) {
		FileTable = fileTable;
	}

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	public Set<String> getColumnValues() {
		return columnValues;
	}

	public void setColumnValues(Set<String> columnValues) {
		this.columnValues = columnValues;
	}

	public Delimiter getGeneralSeparator() {
		return separator;
	}

	public void setGeneralSeparator(Delimiter separator) {
		this.separator = separator;
	}
}
