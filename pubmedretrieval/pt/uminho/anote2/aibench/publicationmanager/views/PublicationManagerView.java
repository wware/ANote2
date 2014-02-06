package pt.uminho.anote2.aibench.publicationmanager.views;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.aibench.utils.exceptions.NonExistingConnection;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.TextAreaEditor;
import pt.uminho.anote2.aibench.utils.gui.TextAreaRenderer;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesIRProcess;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesProcess;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;



/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
/**
* Aibench View - For Publication Manager
* 
* @author Hugo Costa
* @author Rafael Carreira
*/
public class PublicationManagerView extends JPanel implements Observer{

	private static final long serialVersionUID = 7315096041623533479L;
	private PublicationManager pubManager;
	private JComboBox jComboSort;
	private JLabel jLabelSort;
	private JTextField jTextFieldSearch;
	private JLabel jLabelSearch;
	private JPanel jPanelSearch;
	private JButton jButtonHelp;
	private JScrollPane jScrollPane1;
	private JButton newQueryButton;
	private JTable querysTable;
	private List<QueryInformationRetrievalExtension> queries;

	public PublicationManagerView(PublicationManager pubManager) throws NonExistingConnection{
		super();	
		this.pubManager = pubManager;
		initGUI();
		this.pubManager.addObserver(this);	
	}
				
	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.15, 0.1, 0.4, 0.2, 0.1};
			thisLayout.rowHeights = new int[] {113, 7, 139, 7, 20};
			thisLayout.columnWeights = new double[] {0.2, 0.2, 0.2, 0.2, 0.2};
			thisLayout.columnWidths = new int[] {159, 184, 96, 161, 7};
			this.setLayout(thisLayout);
			{
				jScrollPane1 = new JScrollPane();
				this.add(jScrollPane1, new GridBagConstraints(0, 0, 5, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(7, 4, 0, 0), 0, 0));
				jScrollPane1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Load Queries for ClipBoard", TitledBorder.LEADING, TitledBorder.TOP));
				this.add(getNewQueryButton(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				this.add(getJButtonHelp(), new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				TableModel querysTableModel = fillModelQuerysTable("");
				querysTable = new JTable(){
					private static final long serialVersionUID = -6947631910938867860L;

					public boolean isCellEditable(int x,int y){
						if(y==5)
							return true;
						else
							return false;
					}		
				};
				jScrollPane1.setViewportView(querysTable);
				querysTable.setModel(querysTableModel);
				querysTable.setColumnSelectionAllowed(false);
				querysTable.setRowSelectionAllowed(true);
				constructQueryTable();
			}
			this.add(getJPanelSearch(), new GridBagConstraints(1, 4, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private TableModel fillModelQuerysTable(String order) throws SQLException{

		DefaultTableModel tableModel = new DefaultTableModel();
		Properties prop;
		ResultSet rs2 = null;
		String queryDB = QueriesProcess.selectQueriesPubmed;
		queryDB = queryDB+order;	
		Statement statement = this.pubManager.getDb().getConnection().createStatement();
		ResultSet rs = statement.executeQuery(queryDB);
		queries = new ArrayList<QueryInformationRetrievalExtension>();
		while(rs.next())
		{	
			Connection conn = this.pubManager.getDb().getConnection();
			PreparedStatement ps = conn.prepareStatement(QueriesIRProcess.selectQueryProperties);
			ps.setInt(1,rs.getInt(1));
			prop = new Properties();
			rs2 = ps.executeQuery();		
			while(rs2.next())
			{
				prop.put(rs2.getString(1),rs2.getString(2));
			}
		
			QueryInformationRetrievalExtension query = new QueryInformationRetrievalExtension(
					rs.getInt(1),
					rs.getDate(2),
					rs.getString(3),
					rs.getString(4),
					rs.getInt(5),
					rs.getInt(6),
					0,
					prop,
					pubManager);
			
			queries.add(query);
			rs2.close();
		}
		if(rs!=null)
		{
			rs.close();
		}
		
		String[] columns = new String[] {"Date","Query Details","Publications","Abstracts","ID"};
		tableModel.setColumnIdentifiers(columns);
		Object data[][];
		data = new Object[queries.size()][6];	
		int i=0;
		for(QueryInformationRetrievalExtension res: queries)
		{
			data[i][0] = res.getDate();
			data[i][1] = res.toString();
			data[i][2] = res.getDocumentMathing();
			data[i][3] = res.getAvailable_abstracts();
			data[i][4] = res.getID();
			tableModel.addRow(data[i]);
			i++;
		}
		return tableModel;		
	}

	private JButton getNewQueryButton() {
		if (newQueryButton == null) {
			newQueryButton = new JButton();
			newQueryButton.setText("New Query");
			newQueryButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/pubmed.png")));
			newQueryButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
						if (def.getID().equals("operations.pubmedsearch")){			
							Workbench.getInstance().executeOperation(def);
							return;
						}
					}
				}
			});
		}
		return newQueryButton;
	}
	
	public void update(Observable arg0, Object arg1) {

		try {
			TableModel querysTableModel = fillModelQuerysTable(getSelectedComboSortItem());
			querysTable.setModel(querysTableModel);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		constructQueryTable();
		//querysTable.updateUI();
	}

	private void constructQueryTable()
	{
		querysTable.getColumnModel().getColumn(0).setMaxWidth(80);
		querysTable.getColumnModel().getColumn(0).setMinWidth(80);
		querysTable.getColumnModel().getColumn(0).setPreferredWidth(80);
		
		querysTable.getColumnModel().getColumn(2).setMaxWidth(100);
		querysTable.getColumnModel().getColumn(2).setMinWidth(100);
		querysTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		
		querysTable.getColumnModel().getColumn(3).setMaxWidth(100);
		querysTable.getColumnModel().getColumn(3).setMinWidth(100);
		querysTable.getColumnModel().getColumn(3).setPreferredWidth(100);
		
		querysTable.getColumnModel().getColumn(4).setMaxWidth(30);
		querysTable.getColumnModel().getColumn(4).setMinWidth(30);
		querysTable.getColumnModel().getColumn(4).setPreferredWidth(30);
		
		TextAreaRenderer renderer = new TextAreaRenderer();
		TextAreaEditor editor = new TextAreaEditor();
		
		// mudar numeros de colunas
		for (int j = 0; j < 5; j++) {
			querysTable.getColumnModel().getColumn(j).setCellRenderer(renderer);
			querysTable.getColumnModel().getColumn(j).setCellEditor(editor);
		}
	
		TableColumn viewColumn = new TableColumn();
		viewColumn.setHeaderValue("Load");
		viewColumn.setMinWidth(40);
		viewColumn.setMaxWidth(40);
		viewColumn.setPreferredWidth(40);
		viewColumn.setCellRenderer(new TableCellRenderer(){
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JButton viewButton = new JButton();
				viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Add.png")));
				viewButton.setBackground(Color.WHITE);
				viewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						loadQuery();
					}
				});
				return viewButton;
			}
				
		});
		
		viewColumn.setCellEditor(new TableCellEditor(){

			public Component getTableCellEditorComponent(JTable table, Object value, boolean flag, int row, int column) {
				JButton viewButton = new JButton();
				viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Add.png")));
				viewButton.setBackground(Color.WHITE);
				viewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						loadQuery();
					}
				});
				return viewButton;
			}

			public void addCellEditorListener(CellEditorListener arg0) {
			}

			public void cancelCellEditing() {
			}

			public Object getCellEditorValue() {
				return null;
			}

			public boolean isCellEditable(EventObject arg0) {
				return true;
			}

			public void removeCellEditorListener(CellEditorListener arg0) {
			}

			public boolean shouldSelectCell(EventObject arg0) {
				return true;
			}

			public boolean stopCellEditing() {
				return true;
			}
				
		});		
		querysTable.addColumn(viewColumn);
	}
	
	public void loadQuery(){
		if(this.querysTable.getSelectedRow()==-1)
		{
			
		}
		else
		{
			pubManager.addQueryInformationRetrievalExtension(queries.get(this.querysTable.getSelectedRow()));
		}
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Help.internetAcess(GlobalOptions.wikiGeneralLink+"Publication_Manager_View");
				}
			});
		}
		return jButtonHelp;
	}
	
	private JPanel getJPanelSearch() {
		if(jPanelSearch == null) {
			jPanelSearch = new JPanel();
			GridBagLayout jPanelSearchLayout = new GridBagLayout();
			jPanelSearchLayout.rowWeights = new double[] {0.1};
			jPanelSearchLayout.rowHeights = new int[] {7};
			jPanelSearchLayout.columnWeights = new double[] {0.0, 0.1, 0.1, 0.0,0.0};
			jPanelSearchLayout.columnWidths = new int[] {7,7, 7, 7,7};
			jPanelSearch.setLayout(jPanelSearchLayout);
			jPanelSearch.add(getJLabelSearch(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelSearch.add(getJTextFieldSearch(), new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelSearch.add(getJLabelSort(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelSearch.add(getJComboSort(), new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelSearch;
	}
	
	private JLabel getJLabelSearch() {
		if(jLabelSearch == null) {
			jLabelSearch = new JLabel();
			jLabelSearch.setText("Search :");
		}
		return jLabelSearch;
	}
	
	private JTextField getJTextFieldSearch() {
		if(jTextFieldSearch == null) {
			jTextFieldSearch = new JTextField();
			jTextFieldSearch
				.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED),
					null));
			jTextFieldSearch.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent evt) {
					searchInTable(evt);
				}
			});
		}
		return jTextFieldSearch;
	}
	
	@SuppressWarnings("static-access")
	public void searchInTable(KeyEvent evt){
		
		String text;
		ArrayList<Integer> rows = new ArrayList<Integer>();
		DefaultListSelectionModel selectionModel = new DefaultListSelectionModel();
		
		if(jTextFieldSearch.getText().compareTo("")!=0 && evt.getKeyChar() == KeyEvent.VK_BACK_SPACE)
			text = jTextFieldSearch.getText();
		else
			text = jTextFieldSearch.getText()+evt.getKeyChar();
		
		int i=0;
		
		for(QueryInformationRetrievalExtension query: queries)
		{
			if(query.toString().contains(text))
			{
				rows.add(new Integer(i));
			}
			i++;
		}
		

		int row = 0;
		for(Integer r: rows)
		{
			row = r.intValue();
			selectionModel.addSelectionInterval(row, row);
		}
		
		this.querysTable.setSelectionMode(selectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.querysTable.setSelectionModel(selectionModel);
		if(selectionModel.isSelectionEmpty()&& (this.jTextFieldSearch.getText().compareTo("")!=0))
		{
			this.jTextFieldSearch.setForeground(new java.awt.Color(255,0,0));
			jTextFieldSearch.setBackground(new java.awt.Color(174,174,174));
		}
		else
		{
			this.jTextFieldSearch.setForeground(Color.BLACK);
			this.jTextFieldSearch.setBackground(Color.WHITE);
		}
		
		this.querysTable.scrollRectToVisible(this.querysTable.getCellRect(row, 0, true));	

	}
	
	private JLabel getJLabelSort() {
		if(jLabelSort == null) {
			jLabelSort = new JLabel();
			jLabelSort.setText("Sorted By :");
		}
		return jLabelSort;
	}
	
	private JComboBox getJComboSort() {
		if(jComboSort == null) {
			jComboSort = new JComboBox();
			ComboBoxModel jComboSortModel = 
				new DefaultComboBoxModel(
						new String[] { "Date ASC", "Date DESC","Keywords","Organism" });
			jComboSort.setModel(jComboSortModel);
			jComboSort.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					String order = getSelectedComboSortItem();
					refreshTableElements(order);
				}

				private void refreshTableElements(String order) {
					try {
						TableModel pubTableModel = fillModelQuerysTable(order);	
						querysTable.setModel(pubTableModel);
						constructQueryTable();
						querysTable.updateUI();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					catch (Exception e) {
					}
				}	
			});
	
		}
		return jComboSort;
	}
	
	private String getSelectedComboSortItem() {
		int index = jComboSort.getSelectedIndex();
		
		String order = " ORDER BY ";

		switch (index){
			case 0 : {
				order += "date ASC";
				break;
			}
			case 1 : {
				order += "date DESC";
				break;
			}
			case 2 : {
				order += "keywords ASC";
				break;
			}
			case 3 : {
				order += "organism ASC";
				break;
			}
			default: {
				order += "date ASC";
				break;
			}
		}
		return order;
	}
}
