package pt.uminho.anote2.aibench.resources.views;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.SortedMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.resources.datatypes.LookupTableAibench;
import pt.uminho.anote2.aibench.resources.datatypes.Resources;
import pt.uminho.anote2.aibench.resources.gui.lookuptable.EditLookupTermGUI;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextInfoSmall;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;
import pt.uminho.generic.genericpanel.buttons.ButtonTableCellEditor;
import pt.uminho.generic.genericpanel.buttons.ButtonTableRenderer;
import pt.uminho.generic.genericpanel.tablesearcher.TableSearchPanel;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;

public class LookupTableView extends JPanel implements Observer{


	private static final long serialVersionUID = 3739180981313329259L;
	private JPanel jPanelLTInfo;
	private JButton jButtonHelp;
	private JButton jButtonExport;
	private JButton Import;
	private JButton jButton;
	private JTextPane jTextPaneNote;
	private JTextPane jTextPaneName;
	private JLabel jLabelInfo;
	private JLabel jLabelDicName;
	private JPanel jPanelOperations;
	private JPanel jPanelContent;
	private LookupTableAibench lookup;
	private JLabel jLabel1ID;
	private JTextField jTextFieldID;
	private JPanel jPanelSearchandInfoPanel;
	private JTabbedPane jTabbedPane1;
	private JPanel jPanelLTStats;
	private JScrollPane jScrollPaneDicContentStats;
	private JLabel jLabelTerms;
	private JLabel jLabelClassesContent;
	private JTextField jTextFieldTerms;
	private JTextField jTextFieldNumberOfClasses;
	private JPanel jPanelLTStatsInfo;
	private JTable jtableStats;
	private TableSearchPanel jTableSearch;
	
	DecimalFormat dec = new DecimalFormat("0.00");
	private SortedMap<Integer, String> classes; // classeID,classe
	private List<IResourceElement> elements;
	private int numberTerms;
	
	public LookupTableView(LookupTableAibench lookup)
	{
		this.lookup=lookup;
		try {
			this.lookup.addObserver(this);
			classes = Resources.getResourceContentClasses(lookup.getID());
			initGUI();
			constructTable(jTableSearch.getMainTable());
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}

	}
	
	private void initGUI() throws SQLException, DatabaseLoadDriverException {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.0, 0.5, 0.0, 0.05};
			thisLayout.rowHeights = new int[] {7, 7, 20, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};	
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			this.setLayout(thisLayout);
			{
				jPanelLTInfo = getJPanelInfo();
				this.add(jPanelLTInfo, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jPanelContent = new JPanel();
				GridBagLayout jPanelContentLayout = new GridBagLayout();
				this.add(jPanelContent, new GridBagConstraints(0, 1, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelContentLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelContentLayout.rowHeights = new int[] {7, 7, 7, 7};
				jPanelContentLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelContentLayout.columnWidths = new int[] {7, 7, 7, 7};
				jPanelContent.setLayout(jPanelContentLayout);
				{
					jPanelContent.add(getJTableLTContent(), new GridBagConstraints(0, 0, 4, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
			}
			{
				this.add(getJTabbedPane1(), new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				this.add(getJPanelSearchandInfoPanel(), new GridBagConstraints(0, 2, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}

	}

	private JPanel getOperationPane() {
		if(jPanelOperations==null)
		{
			jPanelOperations = new JPanel();
			GridBagLayout jPanelOperationsLayout = new GridBagLayout();
			jPanelOperations.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Operations", TitledBorder.LEADING, TitledBorder.TOP));
			jPanelOperationsLayout.rowWeights = new double[] {0.1};
			jPanelOperationsLayout.rowHeights = new int[] {7};
			jPanelOperationsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelOperationsLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelOperations.setLayout(jPanelOperationsLayout);
			{
				jButton = new JButton();
				jPanelOperations.add(jButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelOperations.add(getImport(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelOperations.add(getJButtonExport(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jButton.setText("Add Term");
				jButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Refresh.png")));
				jButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
							if (def.getID().equals("operations.addelementtolookup")){			
								Workbench.getInstance().executeOperation(def);
								return;
							}
						}
					}
				});
			}
		}
		return jPanelOperations;
	}
	
	private TableModel fillModelPubTable() throws SQLException, DatabaseLoadDriverException{
		elements = new ArrayList<IResourceElement>();
		String[] columns = new String[] {"Term","Class","Edit","DB"};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);
		
		Object[][] data;	
		IResourceElementSet<IResourceElement> elems =  lookup.getResourceElements();
		data = new Object[elems.size()][4];
		int i=0;
		for(IResourceElement elem:elems.getElements())
		{
			data[i][0] = elem.getTerm();
			data[i][1] = ClassProperties.getClassIDClass().get(elem.getTermClassID());	
			data[i][2] = "";
			data[i][3] = "";
			elements.add(elem);
			tableModel.addRow(data[i]);
		}
			
		return tableModel;		
	}
	
	private void constructTable(JTable jtable)
	{
		jtable.getColumnModel().getColumn(1).setMaxWidth(150);
		jtable.getColumnModel().getColumn(1).setMinWidth(150);
		jtable.getColumnModel().getColumn(1).setPreferredWidth(150);
		jtable.getColumnModel().getColumn(2).setMaxWidth(50);
		jtable.getColumnModel().getColumn(2).setMinWidth(50);
		jtable.getColumnModel().getColumn(2).setPreferredWidth(50);
		jtable.getColumnModel().getColumn(3).setMaxWidth(50);
		jtable.getColumnModel().getColumn(3).setMinWidth(50);
		jtable.getColumnModel().getColumn(3).setPreferredWidth(50);
		ImageIcon editICon = new ImageIcon(getClass().getClassLoader().getResource("icons/rule26.png"));
		jtable.getColumn("Edit").setCellRenderer(new ButtonTableEditRender(editICon));
		jtable.getColumn("Edit").setCellEditor(new ButtonTableEditCellEditor());
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/Remove_from_database.png"));
		jtable.getColumn("DB").setCellRenderer(new ButtonTableDBRenderer(icon));
		jtable.getColumn("DB").setCellEditor(new ButtonTableDBCellEditor());
		jtable.setRowHeight(20);
	}

	



	public void update(Observable arg0, Object arg1) {
		try {
			classes = Resources.getResourceContentClasses(lookup.getID());
			jTableSearch.setModel(fillModelPubTable());
			constructTable(jTableSearch.getMainTable());
			numberTerms = Resources.getnumberTerms(lookup.getID());
			jTextFieldTerms.setText(String.valueOf(numberTerms));
			jTextFieldNumberOfClasses.setText(String.valueOf(classes.size()));
			jtableStats.setModel(getTableContenStatsModel());
			jTextPaneName.setText(lookup.getName());
			jtableStats.updateUI();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}

	}
	
	private JPanel getJTableLTContent() throws SQLException, DatabaseLoadDriverException {
		if(jTableSearch==null)
		{
			jTableSearch = new TableSearchPanel();
			jTableSearch.setModel(fillModelPubTable());
			jTableSearch.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Terms", TitledBorder.LEADING, TitledBorder.TOP));	
			jTableSearch.getMainTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return jTableSearch;
	}
	
	private JButton getImport() {
		if(Import == null) {
			Import = new JButton();
			Import.setText("Import");
			Import.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Download.png")));
			Import.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
						if (def.getID().equals("operations.loadcsvlookup")){			
							Workbench.getInstance().executeOperation(def);
							return;
						}
					}
				}
			});
			
		}
		return Import;
	}
	
	private JButton getJButtonExport() {
		if(jButtonExport == null) {
			jButtonExport = new JButton();
			jButtonExport.setText("Export");
			jButtonExport.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Upload22.png")));
			jButtonExport.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
						if (def.getID().equals("operations.savecsvlookup")){			
							Workbench.getInstance().executeOperation(def);
							return;
						}
					}
				}
			});
		}
		return jButtonExport;
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try {
						Help.internetAccess(GlobalOptions.wikiGeneralLink+"LookupTable_Add_Element");
					} catch (IOException e1) {
						TreatExceptionForAIbench.treatExcepion(e1);
					}
				}
			});
		}
		return jButtonHelp;
	}
	
	private JPanel getJPanelInfo() {
		if(jPanelLTInfo==null)
		{
			jPanelLTInfo = new JPanel();
			GridBagLayout jPanelDicInfoGB = new GridBagLayout();
			jPanelLTInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Information", TitledBorder.LEADING, TitledBorder.TOP));
			jPanelDicInfoGB.rowWeights = new double[] { 0.1};
			jPanelDicInfoGB.rowHeights = new int[] {7};
			jPanelDicInfoGB.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelDicInfoGB.columnWidths = new int[] {7, 7, 7, 7, 20, 7};
			jPanelLTInfo.setLayout(jPanelDicInfoGB);
			{
				jLabelDicName = new JLabel();
				jPanelLTInfo.add(jLabelDicName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelDicName.setText("Name :");
			}
			{
				jLabelInfo = new JLabel();
				jPanelLTInfo.add(jLabelInfo, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelInfo.setText("Description :");
			}
			{
				jTextPaneName = new JTextPane();
				jTextPaneName.setEditable(false);
				jTextPaneName.setText(lookup.getName());
				jPanelLTInfo.add(jTextPaneName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jTextPaneNote = new JTextPane();
				jTextPaneNote.setEditable(false);
				jPanelLTInfo.add(jTextPaneNote, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jPanelLTInfo.add(getJLabel1ID(), new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelLTInfo.add(getJTextFieldID(), new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jTextPaneNote.setText(lookup.getInfo());
			}
		}
		return jPanelLTInfo;
	}
	
	private JLabel getJLabel1ID() {
		if(jLabel1ID == null) {
			jLabel1ID = new JLabel();
			jLabel1ID.setText("ID :");
		}
		return jLabel1ID;
	}
	
	private JTextField getJTextFieldID() {
		if(jTextFieldID == null) {
			jTextFieldID = new JTextField();
			jTextFieldID.setText(String.valueOf(lookup.getID()));
			jTextFieldID.setEditable(false);
		}
		return jTextFieldID;
	}
	
	/**
	 * Search
	 */
	
	private JPanel getJPanelSearchandInfoPanel() {
		if(jPanelSearchandInfoPanel == null) {
			jPanelSearchandInfoPanel = new JPanel();
			GridBagLayout jPanelSearchandInfoPanelLayout = new GridBagLayout();
			jPanelSearchandInfoPanelLayout.rowWeights = new double[] {0.0};
			jPanelSearchandInfoPanelLayout.rowHeights = new int[] {7};
			jPanelSearchandInfoPanelLayout.columnWeights = new double[] {0.1, 0.05, 0.1, 0.05};
			jPanelSearchandInfoPanelLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelSearchandInfoPanel.setLayout(jPanelSearchandInfoPanelLayout);
			jPanelSearchandInfoPanel.add(getJButtonHelp(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelSearchandInfoPanel.add(getJButtonHelp(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelSearchandInfoPanel;
	}
	



	
	private JTabbedPane getJTabbedPane1() throws SQLException, DatabaseLoadDriverException {
		if(jTabbedPane1 == null) {
			jTabbedPane1 = new JTabbedPane();
			jTabbedPane1.addTab("Operations",getOperationPane());
			jTabbedPane1.addTab(GlobalTextInfoSmall.statistics,getJPanelLWStats());
		}
		return jTabbedPane1;
	}
	
	private JPanel getJPanelLWStats() throws SQLException, DatabaseLoadDriverException {
		if(jPanelLTStatsInfo==null)
		{
			jPanelLTStatsInfo = new JPanel();
			GridBagLayout jPanelDicInfoLayout = new GridBagLayout();
			jPanelLTStatsInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""),GlobalTextInfoSmall.statistics, TitledBorder.LEADING,TitledBorder.TOP));
			jPanelDicInfoLayout.rowWeights = new double[] {0.1};
			jPanelDicInfoLayout.rowHeights = new int[] {7};
			jPanelDicInfoLayout.columnWeights = new double[] {0.05, 0.1};
			jPanelDicInfoLayout.columnWidths = new int[] {7, 7};
			jPanelLTStatsInfo.setLayout(jPanelDicInfoLayout);
			jPanelLTStatsInfo.add(getJPanelDicStats(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelLTStatsInfo.add(getJScrollPaneDicContentStats(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelLTStatsInfo;
	}
	
	private JPanel getJPanelDicStats() throws SQLException, DatabaseLoadDriverException {
		if(jPanelLTStats == null) {
			jPanelLTStats = new JPanel();
			GridBagLayout jPanelDicStatsLayout = new GridBagLayout();
			jPanelDicStatsLayout.rowWeights = new double[] {0.1, 0.1};
			jPanelDicStatsLayout.rowHeights = new int[] {7, 7};
			jPanelDicStatsLayout.columnWeights = new double[] {0.0, 0.05};
			jPanelDicStatsLayout.columnWidths = new int[] {7, 7};
			jPanelLTStats.setLayout(jPanelDicStatsLayout);
			jPanelLTStats.add(getJLabelTerms(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelLTStats.add(getJTextFieldTerms(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelLTStats.add(getJLabelClassesContent(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelLTStats.add(getJTextFieldNumberOfClasses(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelLTStats;
	}
	
	private JScrollPane getJScrollPaneDicContentStats() throws SQLException, DatabaseLoadDriverException {
		if(jScrollPaneDicContentStats == null) {
			jScrollPaneDicContentStats = new JScrollPane();
			jScrollPaneDicContentStats.setViewportView(getJTableLTContentStats());
		}
		return jScrollPaneDicContentStats;
	}
	


	private JTable getJTableLTContentStats() throws SQLException, DatabaseLoadDriverException {
		if(jtableStats==null)
		{
			jtableStats = new JTable(getTableContenStatsModel());
		}
		return jtableStats;
	}

	private TableModel getTableContenStatsModel() throws SQLException, DatabaseLoadDriverException
	{
		String[] columns = new String[] {"Class", "Terms"};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);	
		Object[] data;
		for(int classeID:classes.keySet() )
		{
			
			data = Resources.getClassContentStats(lookup.getID(),classeID);
			data[0] = classes.get(classeID);
			tableModel.addRow(data);
		}
		
		return tableModel;
	}
	
	private JLabel getJLabelTerms() {
		if(jLabelTerms == null) {
			jLabelTerms = new JLabel();
			jLabelTerms.setText("Terms");
		}
		return jLabelTerms;
	}
	

	
	private JTextField getJTextFieldTerms() throws SQLException, DatabaseLoadDriverException {
		if(jTextFieldTerms == null) {
			jTextFieldTerms = new JTextField();
			jTextFieldTerms.setMinimumSize(new Dimension(60,25));
			numberTerms = Resources.getnumberTerms(lookup.getID());
			jTextFieldTerms.setText(String.valueOf(numberTerms));
			jTextFieldTerms.setEditable(false);
		}
		return jTextFieldTerms;
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
			jTextFieldNumberOfClasses.setMinimumSize(new Dimension(60,25));
			jTextFieldNumberOfClasses.setEditable(false);
			jTextFieldNumberOfClasses.setText(String.valueOf(classes.size()));
		}
		return jTextFieldNumberOfClasses;
	}
	
	class ButtonTableEditRender extends ButtonTableRenderer{

		private static final long serialVersionUID = 6770443805813243771L;

		public ButtonTableEditRender(ImageIcon editICon) {
			super(editICon);
		}

		@Override
		public void whenClick() {
			editLookupTableTermGUI();
		}
		
	}
	
	class ButtonTableEditCellEditor extends ButtonTableCellEditor
	{
		private static final long serialVersionUID = 1L;

		public void whenClick() {
			editLookupTableTermGUI();	
		}
		
	}

	public void editLookupTableTermGUI() {
		int lineNumber = jTableSearch.getSelectedRowsInOriginalModel()[0];
		IResourceElement elem = elements.get(lineNumber);
		new EditLookupTermGUI(this.lookup, elem);
	}
	
	class ButtonTableDBRenderer extends ButtonTableRenderer{

		private static final long serialVersionUID = 6770443805813243771L;

		public ButtonTableDBRenderer(ImageIcon icon) {
			super(icon);
		}

		@Override
		public void whenClick() {
			try {
				removeFromDB();
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			}		
		}

	}
	
	class ButtonTableDBCellEditor extends ButtonTableCellEditor
	{
		private static final long serialVersionUID = 1L;

		public void whenClick() {
			try {
				removeFromDB();
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			}	
		}
		
	}

	public void removeFromDB() throws SQLException, DatabaseLoadDriverException {
		int lineNumber = jTableSearch.getSelectedRowsInOriginalModel()[0];
		IResourceElement elem = elements.get(lineNumber);
		lookup.inactiveElement(elem);
		lookup.notifyViewObservers();
		new ShowMessagePopup("Term Removed");
	}

}
