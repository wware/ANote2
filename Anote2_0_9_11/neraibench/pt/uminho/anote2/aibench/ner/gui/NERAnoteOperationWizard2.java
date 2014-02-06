package pt.uminho.anote2.aibench.ner.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.ner.gui.help.ResourcesFinderGUIHelp;
import pt.uminho.anote2.aibench.ner.settings.NERLexicalResourcesDefaultSettings;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.resources.rule.RulesSet;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.rules.IRule;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class NERAnoteOperationWizard2 extends WizardStandard{
	
	private static final long serialVersionUID = 1L;
	private JTabbedPane jTabbedPaneResources;
	private JScrollPane jScrollPane1LookupTables;
	private JTable jTableRulesSet;
	private JScrollPane jScrollPaneontologies;
	private JScrollPane jScrollPaneRulesSet;
	private JTable jTableLookupTables;
	private JTable jTableDictionaries;
	private JScrollPane jScrollPaneDictionaries;
	private JTable jTableOntologies;
	private JPanel jPanelOntologies;
	private JPanel jPanelRulesSet;
	private JPanel jPanellookuptables;
	private JPanel jPanelDictionaries;
	private ResourcesFinderGUIHelp resources;
	private JPanel jPanelUsingRulesExtraInfo;
	private JCheckBox jCheckBoxUsingExtraInformation;

	public NERAnoteOperationWizard2(List<Object> param) {
		super(param);
		initGUI();
		if(param.size()==0 || param.size()==1)
		{
			fillDefaultSetting();
		}
		else
		{
			fillWithPreviousSettings(param);
		}
		this.setTitle("NER @Note - Select Resources");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}


	

	private void fillWithPreviousSettings(List<Object> param) {
		List<IResource<IResourceElement>> resources = (List<IResource<IResourceElement>>) param.get(0);
		boolean usingextraInfo = (Boolean) param.get(1);
		jCheckBoxUsingExtraInformation.setSelected(usingextraInfo);
		Set<Integer> ids = new HashSet<Integer>();
		for(IResource<IResourceElement> resource:resources)
		{
			ids.add(resource.getID());
		}
		fillDictionaries(ids);
		filllookupdaTables(ids);
		fillOntologies(ids);
		fillRuleSet(ids);
	}




	private void fillDefaultSetting() {
		boolean usePartialMacthWithDictionaries = (Boolean) PropertiesManager.getPManager().getProperty(NERLexicalResourcesDefaultSettings.USE_PARTIAL_MATCH_WITH_DICTIONARIES);
		jCheckBoxUsingExtraInformation.setSelected(usePartialMacthWithDictionaries);
		int rulesID = (Integer) PropertiesManager.getPManager().getProperty(NERLexicalResourcesDefaultSettings.RULES_RESOURCE_ID);
		selectRules(rulesID);
	}
	
	private void selectRules(int rulesID) {
		IRule rule = new RulesSet(rulesID, "", "");
		for(int i=0;i<jTableRulesSet.getRowCount();i++)
		{
			if(jTableRulesSet.getValueAt(i, 0).equals(rule))
			{
				jTableRulesSet.setValueAt(true, i, 1);
			}
		}
	}
	
	private void fillDictionaries(Set<Integer> ids)
	{
		for(int i=0;i<jTableDictionaries.getRowCount();i++)
		{
			if(ids.contains(((IResource<IResourceElement>) jTableDictionaries.getValueAt(i, 0)).getID()))
			{
				jTableDictionaries.setValueAt(true, i, 1);
			}
			else
			{
				jTableDictionaries.setValueAt(false, i, 1);
			}
		}
	}
	
	private void filllookupdaTables(Set<Integer> ids)
	{
		for(int i=0;i<jTableLookupTables.getRowCount();i++)
		{
			if(ids.contains(((IResource<IResourceElement>) jTableLookupTables.getValueAt(i, 0)).getID()))
			{
				jTableLookupTables.setValueAt(true, i, 1);
			}
			else
			{
				jTableLookupTables.setValueAt(false, i, 1);
			}
		}
	}
	
	private void fillRuleSet(Set<Integer> ids)
	{
		for(int i=0;i<jTableRulesSet.getRowCount();i++)
		{
			if(ids.contains(((IResource<IResourceElement>) jTableRulesSet.getValueAt(i, 0)).getID()))
			{
				jTableRulesSet.setValueAt(true, i, 1);
			}
			else
			{
				jTableRulesSet.setValueAt(false, i, 1);
			}
		}
	}
	
	private void fillOntologies(Set<Integer> ids)
	{
		for(int i=0;i<jTableOntologies.getRowCount();i++)
		{
			if(ids.contains(((IResource<IResourceElement>) jTableOntologies.getValueAt(i, 0)).getID()))
			{
				jTableOntologies.setValueAt(true, i, 1);
			}
			else
			{
				jTableOntologies.setValueAt(false, i, 1);
			}
		}
	}

	private void initGUI() {
		setEnableDoneButton(false);
		setEnableNextButton(true);
	}

	
	public void done() {}

	public void goBack() {
		closeView();
		try {
			new NERAnoteOperationWizard1(false);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		}
	}

	public void goNext() {

		List<IResource<IResourceElement>> resources = getResources();
		if(resources.size()==0)
		{
			Workbench.getInstance().warn("Please select at least one resource");
		}
		else
		{
			List<Object> objs = new ArrayList<Object>();
			objs.add(resources);
			objs.add(jCheckBoxUsingExtraInformation.isSelected());
			closeView();
			try {
				new NERAnoteOperationWizard3(objs);
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			}
		}
	}
	
	private List<IResource<IResourceElement>> getResources() {
		List<IResource<IResourceElement>> list = new ArrayList<IResource<IResourceElement>>();
		getSelectresources(jTableDictionaries,list);
		getSelectresources(jTableLookupTables,list);
		getSelectresources(jTableRulesSet,list);
		getSelectresources(jTableOntologies,list);
		return list;
	}

	private void getSelectresources(JTable jTableRulesSet2,List<IResource<IResourceElement>> list) {
		int rows = jTableRulesSet2.getModel().getRowCount();
		for(int i=0;i<rows;i++)
		{
			boolean isactive = (Boolean) jTableRulesSet2.getModel().getValueAt(i, 1);
			if(isactive)
			{
				IResource<IResourceElement> resource = (IResource<IResourceElement>) jTableRulesSet2.getModel().getValueAt(i, 0);
				list.add(resource);
			}
		}
		
	}

	public JComponent getMainComponent() {
		try {
			resources = new ResourcesFinderGUIHelp();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		}
		if(jTabbedPaneResources == null)
		{
			jTabbedPaneResources = new JTabbedPane();
			{
				jTabbedPaneResources.setBorder(BorderFactory.createTitledBorder(null, "Select Resources", TitledBorder.LEADING, TitledBorder.BELOW_TOP));
				jTabbedPaneResources.setPreferredSize(new java.awt.Dimension(482, 347));
				jPanelDictionaries = new JPanel();		
				GridBagLayout jPanelDictionariesLayout = new GridBagLayout();
				jTabbedPaneResources.addTab("Dictionaries", null, jPanelDictionaries, null);
				jPanelDictionariesLayout.rowWeights = new double[] {0.1};
				jPanelDictionariesLayout.rowHeights = new int[] {7};
				jPanelDictionariesLayout.columnWeights = new double[] {0.1};
				jPanelDictionariesLayout.columnWidths = new int[] {7};
				jPanelDictionaries.setLayout(jPanelDictionariesLayout);
				{
					jScrollPaneDictionaries = new JScrollPane();
					jPanelDictionaries.add(jScrollPaneDictionaries, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					{
						jScrollPaneDictionaries.setViewportView(getDictionariesJTable());
					}
				}
			}
			{
				jPanellookuptables = new JPanel();
				GridBagLayout jPanellookuptablesLayout = new GridBagLayout();
				jTabbedPaneResources.addTab("Lookup Tables", null, jPanellookuptables, null);
				jPanellookuptablesLayout.rowWeights = new double[] {0.1};
				jPanellookuptablesLayout.rowHeights = new int[] {7};
				jPanellookuptablesLayout.columnWeights = new double[] {0.1};
				jPanellookuptablesLayout.columnWidths = new int[] {7};
				jPanellookuptables.setLayout(jPanellookuptablesLayout);
				{
					jScrollPane1LookupTables = new JScrollPane();
					jPanellookuptables.add(jScrollPane1LookupTables, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					{
						jScrollPane1LookupTables.setViewportView(getLookupTablesjTable());
					}
				}
			}
			{
				jPanelRulesSet = new JPanel();
				GridBagLayout jPanelRulesSetLayout = new GridBagLayout();
				jTabbedPaneResources.addTab("Rule Sets", null, jPanelRulesSet, null);
				jPanelRulesSetLayout.rowWeights = new double[] {0.1, 0.0};
				jPanelRulesSetLayout.rowHeights = new int[] {7, 20};
				jPanelRulesSetLayout.columnWeights = new double[] {0.1};
				jPanelRulesSetLayout.columnWidths = new int[] {7};
				jPanelRulesSet.setLayout(jPanelRulesSetLayout);
				{
					jScrollPaneRulesSet = new JScrollPane();
					jPanelRulesSet.add(jScrollPaneRulesSet, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelRulesSet.add(getJPanelUsingRulesExtraInfo(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					{
						jScrollPaneRulesSet.setViewportView(getRulesSetJtable());
					}
				}
			}
			{
				jPanelOntologies = new JPanel();
				GridBagLayout jPanelOntologiesLayout = new GridBagLayout();
				jTabbedPaneResources.addTab("Ontologies", null, jPanelOntologies, null);
				jPanelOntologiesLayout.rowWeights = new double[] {0.1};
				jPanelOntologiesLayout.rowHeights = new int[] {7};
				jPanelOntologiesLayout.columnWeights = new double[] {0.1};
				jPanelOntologiesLayout.columnWidths = new int[] {7};
				jPanelOntologies.setLayout(jPanelOntologiesLayout);
				{
					jScrollPaneontologies = new JScrollPane();
					jPanelOntologies.add(jScrollPaneontologies, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					{
						jScrollPaneontologies.setViewportView(getOntologiesJtable());
					}
				}
			}
		}
		return jTabbedPaneResources;
	}
	
	private JTable getOntologiesJtable() {
		if(jTableOntologies == null)
			jTableOntologies = resources.getOntologiesjTable();
		return jTableOntologies;
	}



	private JTable getRulesSetJtable() {
		if(jTableRulesSet == null)
			jTableRulesSet = resources.getRulesSetjTable();
		return jTableRulesSet;
	}



	private JTable getLookupTablesjTable() {
		if(jTableLookupTables == null)
			jTableLookupTables = resources.getLookupTablejTable();
		return jTableLookupTables;
	}



	private JTable getDictionariesJTable() {
		if(jTableDictionaries == null)
			jTableDictionaries = resources.getDictionariesjTable();
		return jTableDictionaries;
	}



	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Corpus_Create_Annotation_Schema_By_NER_Lexical_Resources#Resources_Selection";
	}
	
	private JPanel getJPanelUsingRulesExtraInfo() {
		if(jPanelUsingRulesExtraInfo == null) {
			jPanelUsingRulesExtraInfo = new JPanel();
			GridBagLayout jPanelUsingRulesExtraInfoLayout = new GridBagLayout();
			jPanelUsingRulesExtraInfo.setBorder(BorderFactory.createTitledBorder("Rules"));
			jPanelUsingRulesExtraInfoLayout.rowWeights = new double[] {0.1};
			jPanelUsingRulesExtraInfoLayout.rowHeights = new int[] {7};
			jPanelUsingRulesExtraInfoLayout.columnWeights = new double[] {0.1};
			jPanelUsingRulesExtraInfoLayout.columnWidths = new int[] {7};
			jPanelUsingRulesExtraInfo.setLayout(jPanelUsingRulesExtraInfoLayout);
			jPanelUsingRulesExtraInfo.add(getJCheckBoxUsingExtraInformation(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelUsingRulesExtraInfo;
	}
	
	
	private JCheckBox getJCheckBoxUsingExtraInformation() {
		if(jCheckBoxUsingExtraInformation == null) {
			jCheckBoxUsingExtraInformation = new JCheckBox();
			jCheckBoxUsingExtraInformation.setText("Partial Match with Dictionaries");
		}
		return jCheckBoxUsingExtraInformation;
	}

}
