package pt.uminho.anote2.aibench.resources.views;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
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
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.resources.datatypes.Resources;
import pt.uminho.anote2.aibench.resources.datatypes.RulesAibench;
import pt.uminho.anote2.aibench.resources.gui.EditRuleGUI;
import pt.uminho.anote2.aibench.utils.exceptions.MissingDatatypesException;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextInfoSmall;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.generic.genericpanel.buttons.ButtonTableCellEditor;
import pt.uminho.generic.genericpanel.buttons.ButtonTableRenderer;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;

public class RulesView extends JPanel implements Observer{


	private static final long serialVersionUID = 3739180981313329259L;
	private JButton jButtonHelp;
	private JTable jTableRulesContent;
	private JScrollPane jScrollPaneContent;
	private JButton jButton;
	private JTextPane jTextPaneNote;
	private JTextPane jTextPaneName;
	private JLabel jLabelInfo;
	private JLabel jLabelDicName;
	private JPanel jPanelOperations;
	private JPanel jPanelContent;
	private RulesAibench rules;
	private List<IResourceElement> elems;
	private JTabbedPane jTabbedPane1;
	private JPanel jPanelRSInfo;
	private JLabel jLabel1ID;
	private JTextField jTextFieldID;
	private JPanel jPanelRSStatsInfo;
	private JPanel jPanelRSStatsInfoStats;
	private JScrollPane jScrollPaneRSContentStats;
	private JTable jtableStats;
	private SortedMap<Integer, String> classes; // classeID,classe
	private JLabel jLabelTerms;
	private JLabel jLabelClassesContent;
	private JTextField jTextFieldTerms;
	private JTextField jTextFieldNumberOfClasses;
	private int numberTerms;

	
	public RulesView(RulesAibench rules)
	{
		this.rules=rules;
		this.rules.addObserver(this);
		elems = new ArrayList<IResourceElement>();
		try {
			classes = Resources.getResourceContentClasses(rules.getID());
			findRules();
			initGUI();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}

	}
	
	private void findRules() throws SQLException, DatabaseLoadDriverException {
		elems = rules.getRules();
	}

	private void initGUI() throws SQLException, DatabaseLoadDriverException {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.0, 0.5, 0.05};
			thisLayout.rowHeights = new int[] {7, 7, 7};
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};	
			this.setLayout(thisLayout);
			{
				this.add(getJPanelInfo(), new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jPanelContent = new JPanel();
				GridBagLayout jPanelContentLayout = new GridBagLayout();
				jPanelContent.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Elements", TitledBorder.LEADING, TitledBorder.TOP));
				this.add(jPanelContent, new GridBagConstraints(0, 1, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelContentLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelContentLayout.rowHeights = new int[] {7, 7, 7, 7};
				jPanelContentLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelContentLayout.columnWidths = new int[] {7, 7, 7, 7};
				jPanelContent.setLayout(jPanelContentLayout);
				{
					jScrollPaneContent = new JScrollPane();
					jPanelContent.add(jScrollPaneContent, new GridBagConstraints(0, 0, 4, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jScrollPaneContent.setViewportView(getJTableDicContent());
				}
			}
			{
				this.add(getJTabbedPane1(), new GridBagConstraints(0, 2, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			constructTable();
		}
	}

	private JPanel getOperationsPAne() {
		if(jPanelOperations == null)
		{
			jPanelOperations = new JPanel();
			GridBagLayout jPanelOperationsLayout = new GridBagLayout();
			jPanelOperations.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Operations", TitledBorder.LEADING, TitledBorder.TOP));
			jPanelOperationsLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanelOperationsLayout.rowHeights = new int[] {7, 7, 7};
			jPanelOperationsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelOperationsLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelOperations.setLayout(jPanelOperationsLayout);
			{
				jButton = new JButton();
				jPanelOperations.add(jButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelOperations.add(getJButtonHelp(), new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jButton.setText("Add Rule");
				jButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Refresh.png")));
				jButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
							if (def.getID().equals("operations.createrule")){			
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
	
	private TableModel fillModelPubTable(){
		String[] columns = new String[] {"Term","Class","Edit","Up","Down","DB"};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);	
		Object[][] data;
		data = new Object[elems.size()][6];
		int i=0;
		for(IResourceElement elem:elems)
		{
			data[i][0] = elem.getTerm();
			data[i][1] = ClassProperties.getClassIDClass().get(elem.getTermClassID());
			data[i][2] = "";
			data[i][3] = "";
			data[i][4] = "";
			data[i][5] = "";
			tableModel.addRow(data[i]);
		}	
		return tableModel;		
	}
	
	private JPanel getJPanelInfo() {
		if(jPanelRSInfo==null)
		{
			jPanelRSInfo = new JPanel();
			GridBagLayout jPanelDicInfoGB = new GridBagLayout();
			jPanelRSInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Information", TitledBorder.LEADING, TitledBorder.TOP));
			jPanelDicInfoGB.rowWeights = new double[] { 0.1};
			jPanelDicInfoGB.rowHeights = new int[] {7};
			jPanelDicInfoGB.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelDicInfoGB.columnWidths = new int[] {7, 7, 7, 7, 20, 7};
			jPanelRSInfo.setLayout(jPanelDicInfoGB);
			{
				jLabelDicName = new JLabel();
				jPanelRSInfo.add(jLabelDicName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelDicName.setText("Name :");
			}
			{
				jLabelInfo = new JLabel();
				jPanelRSInfo.add(jLabelInfo, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelInfo.setText("Description	 :");
			}
			{
				jTextPaneName = new JTextPane();
				jTextPaneName.setEditable(false);
				jTextPaneName.setText(rules.getName());
				jPanelRSInfo.add(jTextPaneName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jTextPaneNote = new JTextPane();
				jTextPaneNote.setEditable(false);
				jPanelRSInfo.add(jTextPaneNote, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jPanelRSInfo.add(getJLabel1ID(), new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelRSInfo.add(getJTextFieldID(), new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jTextPaneNote.setText(rules.getInfo());
			}
		}
		return jPanelRSInfo;
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
			jTextFieldID.setText(String.valueOf(rules.getID()));
			jTextFieldID.setEditable(false);
		}
		return jTextFieldID;
	}
	
	private void constructTable()
	{
		jTableRulesContent.getColumnModel().getColumn(1).setMaxWidth(250);
		jTableRulesContent.getColumnModel().getColumn(1).setMinWidth(150);
		jTableRulesContent.getColumnModel().getColumn(1).setPreferredWidth(150);	
		jTableRulesContent.getColumnModel().getColumn(2).setMaxWidth(35);
		jTableRulesContent.getColumnModel().getColumn(2).setMinWidth(35);
		jTableRulesContent.getColumnModel().getColumn(2).setPreferredWidth(35);	
		jTableRulesContent.getColumnModel().getColumn(3).setMaxWidth(50);
		jTableRulesContent.getColumnModel().getColumn(3).setMinWidth(50);
		jTableRulesContent.getColumnModel().getColumn(3).setPreferredWidth(50);
		jTableRulesContent.getColumnModel().getColumn(4).setMaxWidth(50);
		jTableRulesContent.getColumnModel().getColumn(4).setMinWidth(50);
		jTableRulesContent.getColumnModel().getColumn(4).setPreferredWidth(50);
		jTableRulesContent.getColumnModel().getColumn(5).setMaxWidth(50);
		jTableRulesContent.getColumnModel().getColumn(5).setMinWidth(50);
		jTableRulesContent.getColumnModel().getColumn(5).setPreferredWidth(50);
		jTableRulesContent.setRowHeight(25);
		ImageIcon editICon = new ImageIcon(getClass().getClassLoader().getResource("icons/rule26.png"));
		jTableRulesContent.getColumn("Edit").setCellRenderer(new ButtonTableEditRender(editICon));
		jTableRulesContent.getColumn("Edit").setCellEditor(new ButtonTableEditCellEditor());
		ImageIcon upIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/high.png"));
		jTableRulesContent.getColumn("Up").setCellRenderer(new ButtonTableUpRender(upIcon));
		jTableRulesContent.getColumn("Up").setCellEditor(new ButtonTableUpCellEditor());
		ImageIcon downIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/low.png"));
		jTableRulesContent.getColumn("Down").setCellRenderer(new ButtonTableDownRender(downIcon));
		jTableRulesContent.getColumn("Down").setCellEditor(new ButtonTableDownCellEditor());
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/Remove_from_database.png"));
		jTableRulesContent.getColumn("DB").setCellRenderer(new ButtonTableDBRenderer(icon));
		jTableRulesContent.getColumn("DB").setCellEditor(new ButtonTableDBCellEditor());
	}
	
	private void editRuleGUI() {
		int line = jTableRulesContent.getSelectedRow();
		if(line!=-1)
		{
			try {
				new EditRuleGUI(rules,elems.get(line));
			} catch (MissingDatatypesException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void changePrioretyUP() throws DatabaseLoadDriverException, SQLException {
		int selectLine = jTableRulesContent.getSelectedRow();
		if(selectLine==0)
		{
			Workbench.getInstance().warn("This rule has the highest priorety");
		}
		else if(selectLine!=-1&&jTableRulesContent.getModel().getRowCount()>1)
		{
			IResourceElement elemSelect = elems.get(selectLine);
			IResourceElement changeElem = elems.get(selectLine-1);
			rules.changePriorety(elemSelect, changeElem);
			rules.notifyViewObservers();
		}
	}
	
	private void changePrioretyDown() throws DatabaseLoadDriverException, SQLException {
		int selectLine = jTableRulesContent.getSelectedRow();	
		if(selectLine==jTableRulesContent.getRowCount()-1)
		{
			Workbench.getInstance().warn("This rule has the lowest priorety");
		}
		else if(jTableRulesContent.getRowCount()>1)
		{
			IResourceElement elemSelect = elems.get(selectLine);
			IResourceElement changeElem = elems.get(selectLine+1);
			rules.changePriorety(changeElem, elemSelect);
			rules.notifyViewObservers();
		}
	}
	
	
	public void update(Observable arg0, Object arg1) 
	{
		try {
			classes = Resources.getResourceContentClasses(rules.getID());
			findRules();
			TableModel jTableDicContentModel = fillModelPubTable();
			jTableRulesContent.setModel(jTableDicContentModel );
			constructTable();
			jTableRulesContent.updateUI();
			numberTerms = Resources.getnumberTerms(rules.getID());
			jTextFieldTerms.setText(String.valueOf(numberTerms));
			jTextFieldNumberOfClasses.setText(String.valueOf(classes.size()));
			jtableStats.setModel(getTableContenStatsModel());
			jTextPaneName.setText(rules.getName());
			jtableStats.updateUI();	
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}

	}
	
	private JTable getJTableDicContent() {
		if(jTableRulesContent==null)
		{
			jTableRulesContent = new JTable();
			TableModel jTableDicContentModel = fillModelPubTable();
			jTableRulesContent.setModel(jTableDicContentModel );
		}
		return jTableRulesContent;
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try {
						Help.internetAccess(GlobalOptions.wikiGeneralLink+"RulesSet_Add_Rule");
					} catch (IOException e1) {
						TreatExceptionForAIbench.treatExcepion(e1);
					}
				}
			});
		}
		return jButtonHelp;
	}
	
	private JTabbedPane getJTabbedPane1() throws SQLException, DatabaseLoadDriverException {
		if(jTabbedPane1 == null) {
			jTabbedPane1 = new JTabbedPane();
			jTabbedPane1.addTab("Operations",getOperationsPAne());
			jTabbedPane1.addTab(GlobalTextInfoSmall.statistics,getJPanelRSStats());
		}
		return jTabbedPane1;
	}
	
	private JPanel getJPanelRSStats() throws SQLException, DatabaseLoadDriverException {
		if(jPanelRSStatsInfo==null)
		{
			jPanelRSStatsInfo = new JPanel();
			GridBagLayout jPanelDicInfoLayout = new GridBagLayout();
			jPanelRSStatsInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""),GlobalTextInfoSmall.statistics, TitledBorder.LEADING,TitledBorder.TOP));
			jPanelDicInfoLayout.rowWeights = new double[] {0.1};
			jPanelDicInfoLayout.rowHeights = new int[] {7};
			jPanelDicInfoLayout.columnWeights = new double[] {0.05, 0.1};
			jPanelDicInfoLayout.columnWidths = new int[] {7, 7};
			jPanelRSStatsInfo.setLayout(jPanelDicInfoLayout);
			jPanelRSStatsInfo.add(getJPanelDicStats(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelRSStatsInfo.add(getJScrollPaneDicContentStats(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelRSStatsInfo;
	}
	
	private JPanel getJPanelDicStats() throws SQLException, DatabaseLoadDriverException {
		if(jPanelRSStatsInfoStats == null) {
			jPanelRSStatsInfoStats = new JPanel();
			GridBagLayout jPanelDicStatsLayout = new GridBagLayout();
			jPanelDicStatsLayout.rowWeights = new double[] {0.1, 0.1};
			jPanelDicStatsLayout.rowHeights = new int[] {7, 7};
			jPanelDicStatsLayout.columnWeights = new double[] {0.0, 0.05};
			jPanelDicStatsLayout.columnWidths = new int[] {7, 7};
			jPanelRSStatsInfoStats.setLayout(jPanelDicStatsLayout);
			jPanelRSStatsInfoStats.add(getJLabelTerms(), new GridBagConstraints(0, -1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelRSStatsInfoStats.add(getJTextFieldTerms(), new GridBagConstraints(1, -1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelRSStatsInfoStats.add(getJLabelClassesContent(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelRSStatsInfoStats.add(getJTextFieldNumberOfClasses(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelRSStatsInfoStats;
	}
	
	private JScrollPane getJScrollPaneDicContentStats() throws SQLException, DatabaseLoadDriverException {
		if(jScrollPaneRSContentStats == null) {
			jScrollPaneRSContentStats = new JScrollPane();
			jScrollPaneRSContentStats.setViewportView(getJTableRSContentStats());
		}
		return jScrollPaneRSContentStats;
	}

	private JTable getJTableRSContentStats() throws SQLException, DatabaseLoadDriverException {
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
			
			data = Resources.getClassContentStats(rules.getID(),classeID);
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
			numberTerms = Resources.getnumberTerms(rules.getID());
			jTextFieldTerms.setText(String.valueOf(numberTerms));
			jTextFieldTerms.setEditable(false);
		}
		return jTextFieldTerms;
	}
	
	
	private  JLabel getJLabelClassesContent() {
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
			editRuleGUI();
		}
		
	}
	
	class ButtonTableEditCellEditor extends ButtonTableCellEditor
	{
		private static final long serialVersionUID = 1L;

		public void whenClick() {
			editRuleGUI();	
		}
		
	}
	
	class ButtonTableUpRender extends ButtonTableRenderer{

		private static final long serialVersionUID = 6770443805813243771L;

		public ButtonTableUpRender(ImageIcon editICon) {
			super(editICon);
		}

		@Override
		public void whenClick() throws DatabaseLoadDriverException, SQLException {
			changePrioretyUP();
		}
		
	}
	
	class ButtonTableUpCellEditor extends ButtonTableCellEditor
	{
		private static final long serialVersionUID = 1L;

		public void whenClick() throws DatabaseLoadDriverException, SQLException {
			changePrioretyUP();	
		}	
	}
	
	class ButtonTableDownRender extends ButtonTableRenderer{

		private static final long serialVersionUID = 6770443805813243771L;

		public ButtonTableDownRender(ImageIcon editICon) {
			super(editICon);
		}

		@Override
		public void whenClick() throws DatabaseLoadDriverException, SQLException {
			changePrioretyDown();
		}
		
	}
	
	class ButtonTableDownCellEditor extends ButtonTableCellEditor
	{
		private static final long serialVersionUID = 1L;

		public void whenClick() throws DatabaseLoadDriverException, SQLException {
			changePrioretyDown();	
		}	
	}
	
	class ButtonTableDBRenderer extends ButtonTableRenderer{

		private static final long serialVersionUID = 6770443805813243771L;

		public ButtonTableDBRenderer(ImageIcon icon) {
			super(icon);
		}

		@Override
		public void whenClick() throws SQLException, DatabaseLoadDriverException {
			removeFromDB();
		}

	}
	
	class ButtonTableDBCellEditor extends ButtonTableCellEditor
	{
		private static final long serialVersionUID = 1L;

		public void whenClick() throws SQLException, DatabaseLoadDriverException {
			removeFromDB();	
		}
	}

	public void removeFromDB() throws SQLException, DatabaseLoadDriverException {
		int lineNumber = jTableRulesContent.getSelectedRow();
		IResourceElement elem = elems.get(lineNumber);
		int priorety = elem.getPriority();
		rules.inactiveElement(elem);
		rules.reoderpriorities(priorety);
		rules.notifyViewObservers();
		new ShowMessagePopup("Rule Removed");
	}

}

