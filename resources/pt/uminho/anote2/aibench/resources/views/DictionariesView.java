package pt.uminho.anote2.aibench.resources.views;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Observable;
import java.util.Observer;
import java.util.SortedMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.resources.datatypes.Dictionaries;
import pt.uminho.anote2.aibench.resources.datatypes.DictionaryAibench;
import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.aibench.utils.gui.Help;
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
public class DictionariesView extends JPanel implements Observer{

	private static final long serialVersionUID = 1075013594569256121L;
	
	private Dictionaries dictionaries;
	private JTable dicsTable;
	private JScrollPane dicsScrollPane;
	private Connection connection;
	private JPanel jPanelDicStats;
	private JButton jButtonHelp;
	private JPanel jPanelDictionaries;
	private JPanel jPanelDicionaryOpenarions;
	private JButton jButtonAddDic;
	private JTable jTableDicContent;
	private JScrollPane jScrollPaneDicContentStats;
	private JTextField jTextFieldNumberOfClasses;
	private JLabel jLabelClassesContent;
	private JTextField jTextFieldAverageTermsForSyn;
	private JTextField jTextFieldSyn;
	private JTextField jTextFieldTerms;
	private JLabel jLabelSyn;
	private JLabel jLabelTerms;
	private JLabel jLabelAveregeSynForTerm;
	
	private DecimalFormat dec = new DecimalFormat("0.00");
	
	private DictionaryAibench dictionary;

	public DictionariesView(Dictionaries dictionaries) throws SQLException{
		this.dictionaries = dictionaries;
		this.dictionaries.addObserver(this);
		initGUI();
		populateGUI();
	}


	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			this.setPreferredSize(new java.awt.Dimension(560, 267));
			thisLayout.rowWeights = new double[] {0.2, 0.1, 0.0};
			thisLayout.rowHeights = new int[] {85, 102, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			this.setLayout(thisLayout);
			this.add(getJPanelDicStats(), new GridBagConstraints(0, 1, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			this.add(getJPanelDicionaryOpenarions(), new GridBagConstraints(0, 2, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			this.add(getJPanelDictionaries(), new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}

	}
	
	protected void changeDic() {
		if(dicsTable.getModel().getRowCount()>0&&dicsTable.getSelectedRow()!=-1)
		{
			int id = (Integer) dicsTable.getValueAt(dicsTable.getSelectedRow(), 0);
			String note = (String) dicsTable.getValueAt(dicsTable.getSelectedRow(), 1);
			String info = (String) dicsTable.getValueAt(dicsTable.getSelectedRow(), 2);
			dictionary = new DictionaryAibench(this.dictionaries.getResources().getDb(),id,note,info);	
			populateStats();
		}
	}
	
	
	private void populateStats() {
		if(dictionary!=null)
		{
			jTextFieldTerms.setText(String.valueOf(dictionary.getnumberTerms()));
			jTextFieldSyn.setText(String.valueOf(dictionary.getNumberSynonyms()));
			float averageTermsSyn = (float)dictionary.getNumberSynonyms() / (float)dictionary.getnumberTerms() ;
			jTextFieldAverageTermsForSyn.setText(dec.format(averageTermsSyn));	
			jTextFieldNumberOfClasses.setText(String.valueOf(dictionary.getClassContent().size()));
			jTableDicContent.setModel(getTableContenStatsModel());
			jPanelDicStats.updateUI();
		}
		
	}

	private TableModel getTableModel(){
		
		String[] columns = new String[] {"Id", "Name","Information"};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);
		
		Object[][] data;
		int i = 0;
		
		ResultSet res = this.dictionaries.getResources().getResourceFielsByType("dictionary");
		
		ArrayList<Values> values = new ArrayList<Values>();
				
		try {
			while(res.next())
				values.add(new Values(res.getInt(1), res.getString(2),res.getString(3)));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		data = new Object[values.size()][3];

		for(Values val : values)
		{
			Object[] objs = val.getObjects();	
			data[i][0] = objs[0];
			data[i][1] = objs[1];
			data[i][2] = objs[2];				
			tableModel.addRow(data[i++]);
		}
		return tableModel;
	}
	
	private void populateGUI() throws SQLException{		
		dicsTable.setModel(getTableModel());	
		completeTable();
	}
	
	class Values{
		private Object[] objs;
		
		public Values(Integer a, String b, String c) {
			this.objs = new Object[4];
			this.objs[0] = a;
			this.objs[1] = b;
			if(c==null)
			{
				this.objs[2] = "";
			}
			else
			{
				this.objs[2] = c;
			}
		}
		public Object[] getObjects() {
			return this.objs;
		}
	}

	public void update(Observable arg0, Object arg1) {
		try {
			populateGUI();
			populateStats();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	private void completeTable()
	{
		TableColumn viewColumn = new TableColumn();
		viewColumn.setHeaderValue("Load");
		viewColumn.setMinWidth(45);
		viewColumn.setMaxWidth(45);
		viewColumn.setPreferredWidth(45);
		viewColumn.setCellRenderer(new TableCellRenderer(){

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JButton viewButton = new JButton();
				viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/bottom16.png")));
				viewButton.setBackground(Color.WHITE);
				viewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						addDictionary();
					}
				});
				return viewButton;
			}		
		});
		viewColumn.setCellEditor(new TableCellEditor(){

			public Component getTableCellEditorComponent(JTable table, Object value, boolean flag, int row, int column) {
				JButton viewButton = new JButton();
				viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/bottom16.png")));
				viewButton.setBackground(Color.WHITE);
				viewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						addDictionary();
					}
				});
				return viewButton;
			}

			public void addCellEditorListener(CellEditorListener arg0) {}

			public void cancelCellEditing() {}

			public Object getCellEditorValue() { return null;}

			public boolean isCellEditable(EventObject arg0) { return true; }

			public void removeCellEditorListener(CellEditorListener arg0) {}

			public boolean shouldSelectCell(EventObject arg0) {return true;}

			public boolean stopCellEditing() { return true;}
		});
		
		dicsTable.addColumn(viewColumn);		
		dicsTable.getColumnModel().getColumn(0).setMaxWidth(50);
		dicsTable.getColumnModel().getColumn(0).setMinWidth(30);
		dicsTable.getColumnModel().getColumn(0).setPreferredWidth(40);
		dicsTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		dicsTable.getColumnModel().getColumn(3).setMaxWidth(45);
		dicsTable.getColumnModel().getColumn(3).setMinWidth(45);
		dicsTable.getColumnModel().getColumn(3).setPreferredWidth(45);
	}
	
	private void addDictionary() {		
		int id = (Integer) dicsTable.getValueAt(dicsTable.getSelectedRow(), 0);
		String note = (String) dicsTable.getValueAt(dicsTable.getSelectedRow(), 1);
		String info = (String) dicsTable.getValueAt(dicsTable.getSelectedRow(), 2);
		this.dictionaries.addDictionary(new DictionaryAibench(this.dictionaries.getResources().getDb(),id,note,info));
	}

	
	private JButton getJButtonAddDic() {
		if(jButtonAddDic == null) {
			jButtonAddDic = new JButton();
			jButtonAddDic.setText("Create Dictionary");
			jButtonAddDic.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Download.png")));
			jButtonAddDic.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
						if (def.getID().equals("operations.createdicionary")){			
							Workbench.getInstance().executeOperation(def);
							return;
						}
					}
				}
			});
		}
		return jButtonAddDic;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Connection getConnection() {
		return connection;
	}
	
	
	private JPanel getJPanelDicStats() {
		if(jPanelDicStats == null) {
			jPanelDicStats = new JPanel();
			jPanelDicStats.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Dictionary Selected Stats", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelDicStatsLayout = new GridBagLayout();
			jPanelDicStatsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelDicStatsLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelDicStatsLayout.columnWeights = new double[] {0.0, 0.1, 0.1, 0.1};
			jPanelDicStatsLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelDicStats.setLayout(jPanelDicStatsLayout);
			jPanelDicStats.add(getJLabelTerms(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicStats.add(getJLabelSyn(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicStats.add(getJTextFieldTerms(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicStats.add(getJTextFieldSyn(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicStats.add(getJLabelAveregeSynForTerm(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicStats.add(getJTextFieldAverageTermsForSyn(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicStats.add(getJLabelClassesContent(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicStats.add(getJTextFieldNumberOfClasses(), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicStats.add(getJScrollPaneDicContentStats(), new GridBagConstraints(2, 0, 2, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelDicStats;
	}
	
	private JLabel getJLabelTerms() {
		if(jLabelTerms == null) {
			jLabelTerms = new JLabel();
			jLabelTerms.setText("Terms");
		}
		return jLabelTerms;
	}
	
	private JLabel getJLabelSyn() {
		if(jLabelSyn == null) {
			jLabelSyn = new JLabel();
			jLabelSyn.setText("Synonyms");
		}
		return jLabelSyn;
	}
	
	private JTextField getJTextFieldTerms() {
		if(jTextFieldTerms == null) {
			jTextFieldTerms = new JTextField();
			jTextFieldTerms.setEditable(false);
		}
		return jTextFieldTerms;
	}
	
	private JTextField getJTextFieldSyn() {
		if(jTextFieldSyn == null) {
			jTextFieldSyn = new JTextField();
			jTextFieldSyn.setEditable(false);
		}
		return jTextFieldSyn;
	}
	
	private JLabel getJLabelAveregeSynForTerm() {
		if(jLabelAveregeSynForTerm == null) {
			jLabelAveregeSynForTerm = new JLabel();
			jLabelAveregeSynForTerm.setText("Average Term/Syn");
		}
		return jLabelAveregeSynForTerm;
	}
	
	private JTextField getJTextFieldAverageTermsForSyn() {
		if(jTextFieldAverageTermsForSyn == null) {
			jTextFieldAverageTermsForSyn = new JTextField();
			jTextFieldAverageTermsForSyn.setEditable(false);
		}
		return jTextFieldAverageTermsForSyn;
	}
	
	private JLabel getJLabelClassesContent() {
		if(jLabelClassesContent == null) {
			jLabelClassesContent = new JLabel();
			jLabelClassesContent.setText("Number of classes :");
		}
		return jLabelClassesContent;
	}
	
	private JTextField getJTextFieldNumberOfClasses() {
		if(jTextFieldNumberOfClasses == null) {
			jTextFieldNumberOfClasses = new JTextField();
			jTextFieldNumberOfClasses.setEditable(false);
		}
		return jTextFieldNumberOfClasses;
	}
	
	private JScrollPane getJScrollPaneDicContentStats() {
		if(jScrollPaneDicContentStats == null) {
			jScrollPaneDicContentStats = new JScrollPane();
			jScrollPaneDicContentStats.setViewportView(getJTableDicContent());
		}
		return jScrollPaneDicContentStats;
	}
	
	private JTable getJTableDicContent() {
		if(jTableDicContent == null) {
			TableModel jTableDicContentModel = new DefaultTableModel();
			jTableDicContent = new JTable(){
				private static final long serialVersionUID = -4090662450740771673L;

				public boolean isCellEditable(int x,int y)
				{
					return false;
				}
			};
			jTableDicContent.setModel(jTableDicContentModel);
		}
		return jTableDicContent;
	}
	
	private TableModel getTableContenStatsModel()
	{
		String[] columns = new String[] {"Class", "Terms","Synonyms"};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);	
		Object[] data;
		SortedMap<Integer, String> classes = dictionary.getDicionaryContentClasses();
		for(int classeID:dictionary.getClassContent() )
		{		
			data = dictionary.getClassContentStats(classeID);
			data[0] = classes.get(classeID);
			tableModel.addRow(data);
		}
		
		return tableModel;
	}
	
	private JPanel getJPanelDicionaryOpenarions() {
		if(jPanelDicionaryOpenarions == null) {
			jPanelDicionaryOpenarions = new JPanel();
			GridBagLayout jPanelDicionaryOpenarionsLayout = new GridBagLayout();
			jPanelDicionaryOpenarions.setLayout(jPanelDicionaryOpenarionsLayout);
			jPanelDicionaryOpenarions.add(getJButtonAddDic(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicionaryOpenarions.add(getJButtonHelp(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDicionaryOpenarions.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Dictionary Operations", TitledBorder.LEADING, TitledBorder.TOP));
			jPanelDicionaryOpenarionsLayout.rowWeights = new double[] {0.1};
			jPanelDicionaryOpenarionsLayout.rowHeights = new int[] {7};
			jPanelDicionaryOpenarionsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelDicionaryOpenarionsLayout.columnWidths = new int[] {7, 7, 7, 7};

		}
		return jPanelDicionaryOpenarions;
	}
	
	private JPanel getJPanelDictionaries() {
		if(jPanelDictionaries == null) {
			jPanelDictionaries = new JPanel();
			jPanelDictionaries.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Load Dictionaries for ClipBoard", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelDictionariesLayout = new GridBagLayout();
			jPanelDictionariesLayout.rowWeights = new double[] {0.1};
			jPanelDictionariesLayout.rowHeights = new int[] {7};
			jPanelDictionariesLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelDictionariesLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelDictionaries.setLayout(jPanelDictionariesLayout);
			{
				dicsScrollPane = new JScrollPane();
				jPanelDictionaries.add(dicsScrollPane, new GridBagConstraints(0, 0, 4, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));	
				{
					dicsTable = new JTable(){
						
						private static final long serialVersionUID = -4090662450740771673L;
						
						@Override
						public boolean isCellEditable(int x,int y){
							if(y==3)
								return true;
							return false;
						}
					};	
					dicsScrollPane.setViewportView(dicsTable);		
					dicsTable.setRowHeight(20);
					dicsTable.addMouseListener(new MouseListener() {
						
						public void mouseClicked(MouseEvent arg0) {
							changeDic();
						}
						
						public void mouseEntered(MouseEvent arg0) {
						}
						
						public void mouseExited(MouseEvent arg0) {
						}
						
						public void mousePressed(MouseEvent arg0) {
						}
						
						public void mouseReleased(MouseEvent arg0) {
						}
					});
					
				}
			}
		}
		return jPanelDictionaries;
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Help.internetAcess(GlobalOptions.wikiGeneralLink+"Dictionary_Create");
				}
			});
		}
		return jButtonHelp;
	}

}

