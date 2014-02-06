package pt.uminho.anote2.relation.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.process.Stats.NERQueriesStats;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.process.re.RelationType;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalGUIOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.process.IE.re.IRelationsType;
import pt.uminho.anote2.relation.configuration.IRERelationAdvancedConfiguration;
import pt.uminho.anote2.relation.configuration.RERelationAdvancedConfiguration;
import pt.uminho.anote2.relation.datastructures.RelationsModelEnem;
import pt.uminho.anote2.relation.settings.RERelationDefaultSettings;
import pt.uminho.generic.genericpanel.tablesearcher.TableSearchPanel;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class REOperationWizard4 extends WizardStandard{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTabbedPane jtabbedPaneOptions;
	private JScrollPane jScrollPane1;
	private JCheckBox jCheckBoxrelationConstrainsWithout;
	private JCheckBox jCheckBoxOnlyUseEntitiesNearestVerb;
	private JPanel jPanelAdvenacedOptionPane;
	private JPanel jPanelRelationContrains;
	private JCheckBox jCheckBoxUsingMaxDistanceVerbEntities;
	private JSpinner jSpinnerMaxDistance;
	private JCheckBox jCheckBoxOnlyUseNearestEntitiesToVerb;
	private JScrollPane jScrollPaneRelationTypes;
	private JLabel jLabelMaxDistance;
	private JPanel jPanelRelationTypesMainPAnel;
	private JButton jButtonSelectAllOrNot;
	private TableSearchPanel jtableSearchRelationTypes;
	private boolean jButtonAllSelected = true;
	private ButtonGroup selectContrains;

	
	public REOperationWizard4(List<Object> param) {
		super(param);
		initGUI();
		if(param.size() == 4)
		{
			fillWithPreviousSettings(param.get(3));
			param.remove(3);
		}
		else
		{
			defaultSettings();
		}
		this.setTitle("Relation Extraction - Relation Constrains");
		Utilities.centerOnOwner(this);
		this.setModal(true);	
		this.setVisible(true);	
	}
	
	private void fillWithPreviousSettings(Object object) {
		IRERelationAdvancedConfiguration reAdvancedConfiguration = (IRERelationAdvancedConfiguration) object;
		jCheckBoxOnlyUseNearestEntitiesToVerb.setSelected(reAdvancedConfiguration.usingOnlyVerbNearestEntities());
		jCheckBoxOnlyUseEntitiesNearestVerb.setSelected(reAdvancedConfiguration.usingOnlyEntitiesNearestVerb());
		if(reAdvancedConfiguration.usingVerbEntitiesDistance())
		{
			jCheckBoxUsingMaxDistanceVerbEntities.setSelected(true);
			Integer value = reAdvancedConfiguration.getVerbEntitieMaxDistance();
			jSpinnerMaxDistance.setValue(value);
		}		
		SortedSet<IRelationsType> rt = reAdvancedConfiguration.getRelationsType();
		if(rt==null)
			rt = ( SortedSet<IRelationsType>) PropertiesManager.getPManager().getProperty((RERelationDefaultSettings.ADVANCED_RELATIONS_TYPE));
		fillRelationTypeSettings(rt);
	}

	private void initGUI()
	{
		setEnableDoneButton(false);
	}

	private void defaultSettings() {
		jCheckBoxOnlyUseNearestEntitiesToVerb.setSelected(Boolean.parseBoolean(PropertiesManager.getPManager().getProperty(RERelationDefaultSettings.ADVANCED_ONLY_NEAREST_VERB_ENTITIES).toString()));
		jCheckBoxOnlyUseEntitiesNearestVerb.setSelected(Boolean.parseBoolean(PropertiesManager.getPManager().getProperty(RERelationDefaultSettings.ADVANCED_ONLY_USE_ENTITY_TO_NEAREST_VERB).toString()));
		Integer value = Integer.valueOf((PropertiesManager.getPManager().getProperty(RERelationDefaultSettings.ADVANCED_VERB_ENTITES_MAX_DISTANCE).toString()));
		if(value!=0)
		{
			jCheckBoxUsingMaxDistanceVerbEntities.setSelected(true);
			jSpinnerMaxDistance.setValue(value);
		}
		SortedSet<IRelationsType> rt = ( SortedSet<IRelationsType>) PropertiesManager.getPManager().getProperty((RERelationDefaultSettings.ADVANCED_RELATIONS_TYPE));
		fillRelationTypeSettings(rt);
	}
	
	protected void fillRelationTypeSettings(SortedSet<IRelationsType> rt) {	
		if(!rt.isEmpty())
		{
			for(int i=0;i<jtableSearchRelationTypes.getMainTable().getRowCount();i++)
			{
				IRelationsType rowType = (IRelationsType) jtableSearchRelationTypes.getMainTable().getValueAt(i, 0);
				if(!rt.contains(rowType))
				{
					 jtableSearchRelationTypes.getMainTable().setValueAt(Boolean.FALSE, i, 1);
				}
			}
		}
	}


	@Override
	public JComponent getMainComponent() {
		if(jtabbedPaneOptions == null)
		{
			jtabbedPaneOptions = new JTabbedPane();
			try {
				jtabbedPaneOptions.addTab("Relation Types",getJPanelRelationTypesMainPAnel());
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();;
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			}	
			jtabbedPaneOptions.addTab("Advanced Model Options", getJPanelAdvenacedOptionPane());
		}
		return jtabbedPaneOptions;
	}
	
	private JPanel getJPanelAdvenacedOptionPane() {
		if(jPanelAdvenacedOptionPane == null) {
			jPanelAdvenacedOptionPane = new JPanel();
			selectContrains = new ButtonGroup();
			GridBagLayout jPanelAdvenacedOptionPaneLayout = new GridBagLayout();
			jPanelAdvenacedOptionPane.setPreferredSize(new java.awt.Dimension(219, 202));
			jPanelAdvenacedOptionPaneLayout.rowWeights = new double[] {0.1, 0.1};
			jPanelAdvenacedOptionPaneLayout.rowHeights = new int[] {7, 7};
			jPanelAdvenacedOptionPaneLayout.columnWeights = new double[] {0.1};
			jPanelAdvenacedOptionPaneLayout.columnWidths = new int[] {7};
			jPanelAdvenacedOptionPane.setLayout(jPanelAdvenacedOptionPaneLayout);
			jPanelAdvenacedOptionPane.add(getJPanelRelationverbEntitiesDistance(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelAdvenacedOptionPane;
	}
	
	private JPanel getJPanelRelationverbEntitiesDistance() {
		if(jPanelRelationContrains == null) {
			jPanelRelationContrains = new JPanel();
			GridBagLayout jPanelRelationverbEntitiesDistanceLayout = new GridBagLayout();
			jPanelRelationContrains.setBorder(BorderFactory.createTitledBorder("Relation Constrains"));
			jPanelRelationverbEntitiesDistanceLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelRelationverbEntitiesDistanceLayout.rowHeights = new int[] {7, 7, 20, 7, 7};
			jPanelRelationverbEntitiesDistanceLayout.columnWeights = new double[] {0.1, 0.1, 0.025};
			jPanelRelationverbEntitiesDistanceLayout.columnWidths = new int[] {7, 20, 7};
			jPanelRelationContrains.setLayout(jPanelRelationverbEntitiesDistanceLayout);
			jPanelRelationContrains.add(getJCheckBoxUsingMaxDistanceVerbEntities(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
			jPanelRelationContrains.add(getJSpinnerMaxDistance(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelRelationContrains.add(getJCheckBoxOnlyUseMearestEntitiesToVerb(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
			jPanelRelationContrains.add(getJLabelMaxDistance(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelRelationContrains.add(getJCheckBoxOnlyUseEntitiesNearestVerb(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
			jPanelRelationContrains.add(getJCheckBoxrelationConstrainsWithout(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
		}
		return jPanelRelationContrains;
	}
	
	private JCheckBox getJCheckBoxUsingMaxDistanceVerbEntities() {
		if(jCheckBoxUsingMaxDistanceVerbEntities == null) {
			jCheckBoxUsingMaxDistanceVerbEntities = new JCheckBox();
			jCheckBoxUsingMaxDistanceVerbEntities.setText("Using Max Distance Verb-Entities (In Words)");
			selectContrains.add(jCheckBoxUsingMaxDistanceVerbEntities);
		}
		return jCheckBoxUsingMaxDistanceVerbEntities;
	}
	
	private JLabel getJLabelMaxDistance() {
		if(jLabelMaxDistance == null) {
			jLabelMaxDistance = new JLabel();
			jLabelMaxDistance.setText("Max Words Distance :");
		}
		return jLabelMaxDistance;
	}
	
	private JSpinner getJSpinnerMaxDistance() {
		if(jSpinnerMaxDistance == null) {
			SpinnerNumberModel jSpinnerMaxDistanceModel = new SpinnerNumberModel(100, 1, 500, 1);
			jSpinnerMaxDistance = new JSpinner();
			jSpinnerMaxDistance.setModel(jSpinnerMaxDistanceModel);
		}
		return jSpinnerMaxDistance;
	}
	
	private JCheckBox getJCheckBoxOnlyUseMearestEntitiesToVerb() {
		if(jCheckBoxOnlyUseNearestEntitiesToVerb == null) {
			jCheckBoxOnlyUseNearestEntitiesToVerb = new JCheckBox();
			jCheckBoxOnlyUseNearestEntitiesToVerb.setText("Keep only relations where verbs are associated only with the nearest entities");
			selectContrains.add(jCheckBoxOnlyUseNearestEntitiesToVerb);
		}
		return jCheckBoxOnlyUseNearestEntitiesToVerb;
	}
	
	private JPanel getJPanelRelationTypesMainPAnel() throws SQLException, DatabaseLoadDriverException {
		if(jPanelRelationTypesMainPAnel == null) {
			jPanelRelationTypesMainPAnel = new JPanel();
			GridBagLayout jPanelRelationTypesMainPAnelLayout = new GridBagLayout();
			jPanelRelationTypesMainPAnelLayout.rowWeights = new double[] {0.1, 0.0};
			jPanelRelationTypesMainPAnelLayout.rowHeights = new int[] {7, 7};
			jPanelRelationTypesMainPAnelLayout.columnWeights = new double[] {0.1};
			jPanelRelationTypesMainPAnelLayout.columnWidths = new int[] {7};
			jPanelRelationTypesMainPAnel.setLayout(jPanelRelationTypesMainPAnelLayout);
			jPanelRelationTypesMainPAnel.add(getJButtonSelectAllOrNot(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelRelationTypesMainPAnel.add(getJPanel1(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelRelationTypesMainPAnel;
	}
	
	private JButton getJButtonSelectAllOrNot() {
		if(jButtonSelectAllOrNot == null) {
			jButtonSelectAllOrNot = new JButton();
			jButtonSelectAllOrNot.setText("Unselect All");
			jButtonSelectAllOrNot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					changeOriginalTableModel();
				}
			});
			
		}
		return jButtonSelectAllOrNot;
	}
	
	protected void changeOriginalTableModel() {
		boolean selectedAll = !jButtonAllSelected  ;
		for(int i=0;i<this.jtableSearchRelationTypes.getOriginalTableModel().getRowCount();i++)
		{
			this.jtableSearchRelationTypes.getOriginalTableModel().setValueAt(selectedAll, i, 1);
		}			
		if(selectedAll)
			jButtonSelectAllOrNot.setText("Unselect");
		else
			jButtonSelectAllOrNot.setText("Select All");
		jButtonAllSelected = selectedAll;
		jPanelRelationTypesMainPAnel.updateUI();
	}
	
	private TableSearchPanel getJPanel1() throws SQLException, DatabaseLoadDriverException {
		if(jtableSearchRelationTypes == null) {
			jtableSearchRelationTypes = new TableSearchPanel(getRelationTypeModal(),false);
			completeTable(jtableSearchRelationTypes.getMainTable());
		}
		return jtableSearchRelationTypes;
	}
	
	private void completeTable(JTable mainTable) {
		mainTable.setRowHeight(20);
		mainTable.getColumnModel().getColumn(1).setMaxWidth(GlobalGUIOptions.tableSelectionSize);
		mainTable.getColumnModel().getColumn(1).setMinWidth(GlobalGUIOptions.tableSelectionSize);
		mainTable.getColumnModel().getColumn(1).setPreferredWidth(GlobalGUIOptions.tableSelectionSize);
	}
	
	
	private Set<Integer> getIEProcessClassesID(IIEProcess ieprocess) throws SQLException, DatabaseLoadDriverException{
		Corpus corpus = (Corpus) HelpAibench.getSelectedItem(Corpus.class);
		Set<Integer> result = new TreeSet<Integer>();
		PreparedStatement ps;
		ps = Configuration.getDatabase().getConnection().prepareStatement(NERQueriesStats.classNumberAnnotationsCorpus);
		ps.setInt(1, ieprocess.getID());
		ps.setInt(2,corpus.getID());
		ResultSet rs = ps.executeQuery();
		while(rs.next())
		{
			result.add(ClassProperties.getClassClassID().get(rs.getString(1)));
		}
		rs.close();
		ps.close();
		return result;

	}
	
	private TableModel getRelationTypeModal() throws SQLException, DatabaseLoadDriverException {
		IEProcess ieProcess = (IEProcess) getParam().get(0);
		
		DefaultTableModel tableModel = new DefaultTableModel()		{
			private static final long serialVersionUID = 1L;

			public Class<?> getColumnClass(int columnIndex){
				if(columnIndex == 0)
					return IRelationsType.class;
				else			
					return Boolean.class;
			}
		};
		String[] columns = new String[] {"RelationType",""};
		tableModel.setColumnIdentifiers(columns);	
		Set<Integer> classIDS = getIEProcessClassesID(ieProcess);
		List<Integer> listClasses = new ArrayList<Integer>(classIDS);
		Object[] data = new Object[2];
		for(int i=0;i<listClasses.size();i++)
		{
			for(int j=i;j<listClasses.size();j++)
			{
				if(j==i)
				{
					data[0] = new RelationType(listClasses.get(i), listClasses.get(i));
					data[1] =  Boolean.TRUE;
					tableModel.addRow(data);
				}
				else
				{
					data[0] = new RelationType(listClasses.get(i), listClasses.get(j));
					data[1] =  Boolean.TRUE;
					tableModel.addRow(data);
					data[0] = new RelationType(listClasses.get(j), listClasses.get(i));
					data[1] =  Boolean.TRUE;
					tableModel.addRow(data);
				}
			}
		}
		return tableModel;
	}
	

	@Override
	public void goNext() {
		RelationsModelEnem model = (RelationsModelEnem) getParam().get(2);
		RERelationAdvancedConfiguration advancedConfiguration = getRERelationAdvancedOptions() ;
		if(advancedConfiguration.getRelationsType()!=null && advancedConfiguration.getRelationsType().size() == 0)
		{
			Workbench.getInstance().warn("Please select at least one Relation Type");
		}
		else
		{
			List<Object> parameters = getParam();
			parameters.add(advancedConfiguration); // 4
			if((model).equals(RelationsModelEnem.Binary_Biomedical_Verbs))
			{
				closeView();
				new REOperationWizard6a(parameters);
			}
			else
			{
				closeView();
				new REOperationWizard5(parameters);
			}
		}
	}

	private RERelationAdvancedConfiguration getRERelationAdvancedOptions() {
		SortedSet<IRelationsType> rt = getRelationsTypes();
		if(rt.size() == jtableSearchRelationTypes.getOriginalTableModel().getRowCount())
		{
			rt = null;
		}
		if(jCheckBoxOnlyUseNearestEntitiesToVerb.isSelected())
		{
			return new RERelationAdvancedConfiguration(true,false,-1,rt);
		}
		else if(jCheckBoxOnlyUseEntitiesNearestVerb.isSelected())
		{
			return new RERelationAdvancedConfiguration(false,true,-1,rt);
		}
		else if(jCheckBoxUsingMaxDistanceVerbEntities.isSelected())
		{
			return new RERelationAdvancedConfiguration(false,false,(Integer)jSpinnerMaxDistance.getValue(),rt);
		}
		else
		{
			return new RERelationAdvancedConfiguration(false,false,-1,rt);
		}
	}
	
	private SortedSet<IRelationsType> getRelationsTypes() {
		SortedSet<IRelationsType> rtselected = new TreeSet<IRelationsType>();
		for(int i=0;i<jtableSearchRelationTypes.getOriginalTableModel().getRowCount();i++)
		{
			Boolean selected = (Boolean) jtableSearchRelationTypes.getOriginalTableModel().getValueAt(i, 1);
			if(selected)
			{
				IRelationsType rowType = (IRelationsType) jtableSearchRelationTypes.getOriginalTableModel().getValueAt(i, 0);
				rtselected.add(rowType);
			}
		}
		return rtselected;
	}

	@Override
	public void goBack() {
		closeView();
		new REOperationWizard3(getParam());	
	}

	@Override
	public void done() {}

	@Override
	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Corpus_Relation_Extraction#Advanced_Options";
	}
	
	private JCheckBox getJCheckBoxOnlyUseEntitiesNearestVerb() {
		if(jCheckBoxOnlyUseEntitiesNearestVerb == null) {
			jCheckBoxOnlyUseEntitiesNearestVerb = new JCheckBox();
			jCheckBoxOnlyUseEntitiesNearestVerb.setText("Keep only relations where the entities are only associated to the nearest verb ");
			selectContrains.add(jCheckBoxOnlyUseEntitiesNearestVerb);
		}
		return jCheckBoxOnlyUseEntitiesNearestVerb;
	}
	
	private JCheckBox getJCheckBoxrelationConstrainsWithout() {
		if(jCheckBoxrelationConstrainsWithout == null) {
			jCheckBoxrelationConstrainsWithout = new JCheckBox();
			jCheckBoxrelationConstrainsWithout.setText("Without");
			selectContrains.add(jCheckBoxrelationConstrainsWithout);
			jCheckBoxrelationConstrainsWithout.setSelected(true);
		}
		return jCheckBoxrelationConstrainsWithout;
	}

}
