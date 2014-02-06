package pt.uminho.anote2.aibench.resources.views;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.resources.datatypes.Dictionaries;
import pt.uminho.anote2.aibench.resources.datatypes.DictionaryAibench;
import pt.uminho.anote2.aibench.resources.datatypes.Resources;
import pt.uminho.anote2.aibench.resources.gui.RemoveResourceGUI;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.options.PreferencesSizeComponents;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextInfoSmall;
import pt.uminho.generic.genericpanel.buttons.ButtonTableCellEditor;
import pt.uminho.generic.genericpanel.buttons.ButtonTableRenderer;
import pt.uminho.generic.genericpanel.tablesearcher.TableSearchPanel;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;

public class DictionariesView extends JPanel implements Observer{

	private static final long serialVersionUID = 1075013594569256121L;
	
	private Dictionaries dictionaries;
	private TableSearchPanel jTableSearch;
	private Connection connection;
	private JPanel jPanelDicStats;
	private JButton jButtonHelp;
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

	public DictionariesView(Dictionaries dictionaries){
		this.dictionaries = dictionaries;
		this.dictionaries.addObserver(this);
		initGUI();
		try {
			populateGUI();
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
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
	
	protected void changeDic() throws SQLException, DatabaseLoadDriverException {
		if(jTableSearch.getModel().getRowCount()>0 && jTableSearch.getSelectedRowsInOriginalModel().length!=0)
		{
			int id = (Integer) jTableSearch.getValueAt(jTableSearch.getMainTable().getSelectedRows()[0], 0);
			String note = (String) jTableSearch.getValueAt(jTableSearch.getMainTable().getSelectedRows()[0], 1);
			String info = (String) jTableSearch.getValueAt(jTableSearch.getMainTable().getSelectedRows()[0], 2);
			dictionary = new DictionaryAibench(id,note,info);	
			populateStats();
		}
	}
	
	
	private void populateStats() throws SQLException, DatabaseLoadDriverException {
		if(dictionary!=null)
		{
			int terms = Resources.getnumberTerms(dictionary.getID());
			int synN = Resources.getNumberSynonyms(dictionary.getID());
			jTextFieldTerms.setText(String.valueOf(terms));
			jTextFieldSyn.setText(String.valueOf(synN));
			float averageTermsSyn = (float) synN / (float)terms;
			jTextFieldAverageTermsForSyn.setText(dec.format(averageTermsSyn));	
			jTextFieldNumberOfClasses.setText(String.valueOf(dictionary.getClassContent().size()));
			jTableDicContent.setModel(getTableContenStatsModel());
			jPanelDicStats.updateUI();
		}
		
	}

	private TableModel getTableModel() throws SQLException, DatabaseLoadDriverException{

		String[] columns = new String[] {"ID", "Name","Information","DB","Load"};
		DefaultTableModel tableModel = new DefaultTableModel()		{
			private static final long serialVersionUID = 1L;

			public Class<?> getColumnClass(int columnIndex){
				return (columnIndex == 0 )?(Integer.class):String.class;
			}

		};
		tableModel.setColumnIdentifiers(columns);

		Object[][] data;
		int i = 0;	
		ResultSet res = this.dictionaries.getResources().getResourceFielsByType(GlobalOptions.resourcesDictionaryName);
		ArrayList<Values> values = new ArrayList<Values>();

		while(res.next())
			values.add(new Values(res.getInt(1), res.getString(2),res.getString(3)));


		data = new Object[values.size()][5];

		for(Values val : values)
		{
			Object[] objs = val.getObjects();	
			data[i][0] = objs[0];
			data[i][1] = objs[1];
			data[i][2] = objs[2];
			data[i][3] = "";
			data[i][4] = "";
			tableModel.addRow(data[i++]);
		}
		return tableModel;
	}
	
	private void populateGUI() throws SQLException, DatabaseLoadDriverException{		
		jTableSearch.setModel(getTableModel());	
		completeTable(jTableSearch.getMainTable());
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
				TreatExceptionForAIbench.treatExcepion(e);
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			}
	}
	

	private void completeTable(JTable jtable)
	{
		jtable.setRowHeight(PreferencesSizeComponents.generalRowHeight);
		jtable.getColumnModel().getColumn(0).setMaxWidth(PreferencesSizeComponents.idfieldmaxWith);
		jtable.getColumnModel().getColumn(0).setMinWidth(PreferencesSizeComponents.idfieldminWidth);
		jtable.getColumnModel().getColumn(0).setPreferredWidth(PreferencesSizeComponents.idfieldpreferredWidth);
		jtable.getColumnModel().getColumn(1).setMinWidth(100);
		jtable.getColumnModel().getColumn(1).setPreferredWidth(200);
		jtable.getColumnModel().getColumn(2).setMaxWidth(750);
		jtable.getColumnModel().getColumn(2).setMinWidth(100);
		jtable.getColumnModel().getColumn(2).setPreferredWidth(300);
		jtable.getColumnModel().getColumn(3).setMaxWidth(PreferencesSizeComponents.removefieldmaxWith);
		jtable.getColumnModel().getColumn(3).setMinWidth(PreferencesSizeComponents.removefieldminWith);
		jtable.getColumnModel().getColumn(3).setPreferredWidth(PreferencesSizeComponents.removefieldpreferredWith);
		jtable.getColumnModel().getColumn(4).setMaxWidth(PreferencesSizeComponents.loadfieldmaxWith);
		jtable.getColumnModel().getColumn(4).setMinWidth(PreferencesSizeComponents.loadfieldminWith);
		jtable.getColumnModel().getColumn(4).setPreferredWidth(PreferencesSizeComponents.loadfieldpreferredWith);
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/Remove_from_database.png"));
		jtable.getColumn("DB").setCellRenderer(new ButtonTableDBRenderer(icon));
		jtable.getColumn("DB").setCellEditor(new ButtonTableDBCellEditor());
		ImageIcon icon2 = new ImageIcon(getClass().getClassLoader().getResource("icons/bottom16.png"));
		jtable.getColumn("Load").setCellRenderer(new ButtonLoadResourceRenderer(icon2));
		jtable.getColumn("Load").setCellEditor(new ButtonLoadResourceCellEditor());

	}

	
	private void removeFromDB() {
		if(this.jTableSearch.getMainTable().getSelectedRows()[0]==-1)
		{
			
		}
		else
		{
			int id = (Integer) jTableSearch.getValueAt(jTableSearch.getMainTable().getSelectedRows()[0], 0);
			String note = (String) jTableSearch.getValueAt(jTableSearch.getMainTable().getSelectedRows()[0], 1);
			String info = (String) jTableSearch.getValueAt(jTableSearch.getMainTable().getSelectedRows()[0], 2);
			DictionaryAibench lex = new DictionaryAibench(id,note,info);
			new RemoveResourceGUI(lex);
		}
	}
	
	private void addDictionary() {		
		int id = (Integer) jTableSearch.getValueAt(jTableSearch.getMainTable().getSelectedRows()[0], 0);
		String note = (String) jTableSearch.getValueAt(jTableSearch.getMainTable().getSelectedRows()[0], 1);
		String info = (String) jTableSearch.getValueAt(jTableSearch.getMainTable().getSelectedRows()[0], 2);;
		this.dictionaries.addDictionary(new DictionaryAibench(id,note,info));
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
			jPanelDicStats.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), GlobalTextInfoSmall.statistics, TitledBorder.LEADING, TitledBorder.TOP));
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
			jTableDicContent = new JTable();
			jTableDicContent.setModel(jTableDicContentModel);
		}
		return jTableDicContent;
	}
	
	private TableModel getTableContenStatsModel() throws SQLException, DatabaseLoadDriverException
	{
		String[] columns = new String[] {"Class", "Terms","Synonyms"};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);	
		Object[] data;
		SortedMap<Integer, String> classes = Resources.getResourceContentClasses(dictionary.getID());
		for(int classeID:dictionary.getClassContent() )
		{		
			data = Resources.getClassContentStats(dictionary.getID(),classeID);
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
			jPanelDicionaryOpenarions.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Operations", TitledBorder.LEADING, TitledBorder.TOP));
			jPanelDicionaryOpenarionsLayout.rowWeights = new double[] {0.1};
			jPanelDicionaryOpenarionsLayout.rowHeights = new int[] {7};
			jPanelDicionaryOpenarionsLayout.columnWidths = new int[] {7, 7, 7, 7};

		}
		return jPanelDicionaryOpenarions;
	}

	private JPanel getJPanelDictionaries() {
		if(jTableSearch == null) {
			jTableSearch = new TableSearchPanel();
			jTableSearch.getMainTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableSearch.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Dictionaries", TitledBorder.LEADING, TitledBorder.TOP));
			jTableSearch.getMainTable().addMouseListener(new MouseListener() {

				public void mouseClicked(MouseEvent arg0) {try {
					changeDic();
				} catch (SQLException e) {
					TreatExceptionForAIbench.treatExcepion(e);
				} catch (DatabaseLoadDriverException e) {
					TreatExceptionForAIbench.treatExcepion(e);
				}}
				public void mouseEntered(MouseEvent arg0) {}
				public void mouseExited(MouseEvent arg0) {}
				public void mousePressed(MouseEvent arg0) {}
				public void mouseReleased(MouseEvent arg0) {
				}
			});
			jTableSearch.getMainTable().addKeyListener(new KeyListener() {

				public void keyTyped(KeyEvent arg0) {}
				public void keyReleased(KeyEvent arg0) {}
				public void keyPressed(KeyEvent arg0) {try {
					changeDic();
				} catch (SQLException e) {
					TreatExceptionForAIbench.treatExcepion(e);
				} catch (DatabaseLoadDriverException e) {
					TreatExceptionForAIbench.treatExcepion(e);
				}}
			});				
		}
		return jTableSearch;

	}

	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try {
						Help.internetAccess(GlobalOptions.wikiGeneralLink+"Dictionary_Create");
					} catch (IOException e1) {
						TreatExceptionForAIbench.treatExcepion(e1);
					}
				}
			});
		}
		return jButtonHelp;
	}
	
	class ButtonTableDBRenderer extends ButtonTableRenderer{

		private static final long serialVersionUID = 1L;

		public ButtonTableDBRenderer(ImageIcon icon) {
			super(icon);
		}

		@Override
		public void whenClick() {
			removeFromDB();
		}

	}
	
	class ButtonTableDBCellEditor extends ButtonTableCellEditor
	{
		private static final long serialVersionUID = 1L;

		public void whenClick() {
			removeFromDB();	
		}
		
	}
	
	class ButtonLoadResourceRenderer extends ButtonTableRenderer {

		private static final long serialVersionUID = 1L;

		public ButtonLoadResourceRenderer(ImageIcon icon)
		{
			super(icon);
		}
		
		public void whenClick() {
			addDictionary();
		}

	}

	class ButtonLoadResourceCellEditor extends ButtonTableCellEditor {

		private static final long serialVersionUID = 1L;

		public void whenClick() {
			addDictionary();
		}

	}

}

