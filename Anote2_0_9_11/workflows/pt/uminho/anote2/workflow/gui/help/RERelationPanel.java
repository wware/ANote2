package pt.uminho.anote2.workflow.gui.help;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.ner.gui.help.ResourcesFinderGUIHelp;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.process.re.RelationType;
import pt.uminho.anote2.datastructures.resources.lexiacalwords.LexicalWords;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.anote2.datastructures.utils.conf.GlobalGUIOptions;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.process.IE.re.IREConfiguration;
import pt.uminho.anote2.process.IE.re.IRelationsType;
import pt.uminho.anote2.relation.configuration.IRERelationAdvancedConfiguration;
import pt.uminho.anote2.relation.configuration.RERelationAdvancedConfiguration;
import pt.uminho.anote2.relation.configuration.RERelationConfiguration;
import pt.uminho.anote2.relation.datastructures.PosTaggerEnem;
import pt.uminho.anote2.relation.datastructures.RelationsModelEnem;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;
import pt.uminho.anote2.workflow.datastructures.REWorkflowProcessesAvailableEnum;
import pt.uminho.anote2.workflow.gui.AREPanel;
import pt.uminho.generic.genericpanel.tablesearcher.TableSearchPanel;
import es.uvigo.ei.aibench.workbench.Workbench;

public class RERelationPanel extends AREPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTabbedPane jTabbedPaneMainComponent;
	private JPanel jPanelBasics;
	private JRadioButton jRadioButtonVerbLimitation;
	private JRadioButton jRadioButtonSimpleModel;
	private JRadioButton jRadioButtonBinaryVerbLimitation;
	private ButtonGroup buttonGroupModels;
	private ButtonGroup buttonGroupPOSTagging;
	private JRadioButton jRadioButtonBinaryModel;
	private JCheckBox jCheckBoxRelationConstrainsWithout;
	private JCheckBox jCheckBoxOnlyUseEntitiesNearestVerb;
	private JPanel jPanelRelationTypes;
	private JLabel jLabelImage;
	private JPanel jPanelModelImage;
	private JTable jTableVerbFilter;
	private JScrollPane jScrollPaneVerbFilter;
	private JScrollPane jScrollPaneVerbAddition;
	private JTabbedPane jTabbedPaneVerbManagement;
	private JRadioButton jRadioButtonLingPipePOSTAgging;
	private JRadioButton jRadioButtonGatePosTagging;
	private JPanel jPanelPosTagging;
	private JTable jTableVerbAddition;
	private JScrollPane jScrollPaneBinaryModelVerbsList;
	private JPanel jPanelBinaryModelVerbSelection;
	private JPanel jPanelModelOptions;
	private JPanel jPanelAdvanced;
	private ResourcesFinderGUIHelp resources;
	private JTable jTableSelectVerbs;
	private JTabbedPane jTabbedPaneAdvanced;
	private JPanel jPanelModelSetting;
	private JPanel jPanelAdvenacedOptionPane;
	private JPanel jPanelRelationContrains;
	private JCheckBox jCheckBoxUsingMaxDistanceVerbEntities;
	private JSpinner jSpinnerMaxDistance;
	private JCheckBox jCheckBoxOnlyUseNearestEntitiesToVerb;
	private JLabel jLabelMaxDistance;
	private JPanel jPanelRelationTypesMainPAnel;
	private JButton jButtonSelectAllOrNot;
	private boolean jButtonAllSelected = true;
	private TableSearchPanel jtableSearchRelationTypes;
	private ButtonGroup buttonGroup;

	public RERelationPanel(List<String> defaultSettings) throws SQLException, DatabaseLoadDriverException
	{
		preprocessing();
		initGUI();
		completeGUI();
		defaultSettings(defaultSettings);
	}
	
	private void defaultSettings(List<String> defaultSettings) {
		if(defaultSettings!=null && !defaultSettings.isEmpty())
		{
			
			RelationsModelEnem model = (RelationsModelEnem) PropertiesManager.getPManager().getProperty(defaultSettings.get(1));
			ILexicalWords biomedicalVerbs = new LexicalWords(Integer.valueOf(PropertiesManager.getPManager().getProperty(defaultSettings.get(6)).toString()),"","");
			if(model.equals(RelationsModelEnem.Simple_Model))
			{
				jRadioButtonSimpleModel.setSelected(true);
			}
			else if(model.equals(RelationsModelEnem.Verb_Limitation))
			{
				jRadioButtonVerbLimitation.setSelected(true);
			}
			else if(model.equals(RelationsModelEnem.Binary_Verb_limitation))
			{
				jRadioButtonBinaryVerbLimitation.setSelected(true);
			}
			else
			{
				jRadioButtonBinaryModel.setSelected(true);
			}
			for(int i=0;i<jTableSelectVerbs.getRowCount();i++)
			{
				if(jTableSelectVerbs.getValueAt(i, 0).equals(biomedicalVerbs))
				{
					jTableSelectVerbs.setValueAt(true, i, 1);
				}
			}
			changeModel();
			PosTaggerEnem posTagger = (PosTaggerEnem) PropertiesManager.getPManager().getProperty(defaultSettings.get(0));
			if(posTagger.equals(PosTaggerEnem.Gate_POS))
			{
				jRadioButtonGatePosTagging.setSelected(true);
			}
			else
			{
				jRadioButtonLingPipePOSTAgging.setSelected(true);
			}
			ILexicalWords verbAddition = new LexicalWords(Integer.valueOf(PropertiesManager.getPManager().getProperty(defaultSettings.get(3)).toString()),"","");
			ILexicalWords verbFilter = new LexicalWords(Integer.valueOf(PropertiesManager.getPManager().getProperty(defaultSettings.get(5)).toString()),"","");
			for(int i=0;i<jTableVerbAddition.getRowCount();i++)
			{
				if(jTableVerbAddition.getValueAt(i, 0).equals(verbAddition))
				{
					jTableVerbAddition.setValueAt(true, i, 1);
				}
			}
			for(int i=0;i<jTableVerbFilter.getRowCount();i++)
			{
				if(jTableVerbFilter.getValueAt(i, 0).equals(verbFilter))
				{
					jTableVerbFilter.setValueAt(true, i, 1);
				}
			}
			jCheckBoxOnlyUseNearestEntitiesToVerb.setSelected(Boolean.parseBoolean(PropertiesManager.getPManager().getProperty(defaultSettings.get(7)).toString()));
			Integer value = Integer.valueOf(PropertiesManager.getPManager().getProperty(defaultSettings.get(8)).toString());
			if(value!=0)
			{
				jCheckBoxUsingMaxDistanceVerbEntities.setSelected(true);
				jSpinnerMaxDistance.setValue(value);
			}
			SortedSet<IRelationsType> rt = (SortedSet<IRelationsType>) PropertiesManager.getPManager().getProperty(defaultSettings.get(9));
			fillRelationTypeSettings(rt);
			jCheckBoxOnlyUseEntitiesNearestVerb.setSelected(Boolean.parseBoolean(PropertiesManager.getPManager().getProperty(defaultSettings.get(10)).toString()));

		}
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
	private void completeGUI() {
		getButtonGroupModels();
		getButtonGroupPOSTagging();
	}

	private void preprocessing() throws SQLException, DatabaseLoadDriverException {
		resources = new ResourcesFinderGUIHelp();
	}

	private void initGUI() {
		GridBagLayout thisLayout = new GridBagLayout();
		this.setPreferredSize(new java.awt.Dimension(800, 500));
		thisLayout.rowWeights = new double[] {0.1};
		thisLayout.rowHeights = new int[] {7};
		thisLayout.columnWeights = new double[] {0.1};
		thisLayout.columnWidths = new int[] {7};
		this.setLayout(thisLayout);
		{
			jTabbedPaneMainComponent = new JTabbedPane();
			this.add(jTabbedPaneMainComponent, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			{
				jPanelBasics = new JPanel();
				GridBagLayout jPanelBasicsLayout = new GridBagLayout();
				jTabbedPaneMainComponent.addTab("Basic", null, jPanelBasics, null);
				jPanelBasics.setBorder(BorderFactory.createTitledBorder("Select Relation Model"));
				jPanelBasicsLayout.rowWeights = new double[] {0.1, 0.5};
				jPanelBasicsLayout.rowHeights = new int[] {7, 7};
				jPanelBasicsLayout.columnWeights = new double[] {0.1};
				jPanelBasicsLayout.columnWidths = new int[] {7};
				jPanelBasics.setLayout(jPanelBasicsLayout);
				{
					jPanelModelOptions = new JPanel();
					GridBagLayout jPanelModelOptionsLayout = new GridBagLayout();
					jPanelBasics.add(jPanelModelOptions, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 0), 0, 0));
					jPanelModelOptionsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
					jPanelModelOptionsLayout.rowHeights = new int[] {7, 7, 7, 7};
					jPanelModelOptionsLayout.columnWeights = new double[] {0.025, 0.1};
					jPanelModelOptionsLayout.columnWidths = new int[] {7, 20};
					jPanelModelOptions.setLayout(jPanelModelOptionsLayout);
					{
						jRadioButtonBinaryModel = new JRadioButton();
						jPanelModelOptions.add(jRadioButtonBinaryModel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
						jRadioButtonBinaryModel.setText("Binary Selected Verbs only (1x1)");
						jRadioButtonBinaryModel.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent arg0) {
								changeModel();
							}
						});
					}
					{
						jRadioButtonBinaryVerbLimitation = new JRadioButton();
						jPanelModelOptions.add(jRadioButtonBinaryVerbLimitation, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 20, 5, 0), 0, 0));
						jRadioButtonBinaryVerbLimitation.setText("Binary Verb Limitation (1x1)");
						jRadioButtonBinaryVerbLimitation.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent arg0) {
								changeModel();
							}
						});

					}
					{
						jRadioButtonVerbLimitation = new JRadioButton();
						jPanelModelOptions.add(jRadioButtonVerbLimitation, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 20, 5, 0), 0, 0));
						jRadioButtonVerbLimitation.setText("Verb Limitation (MxM)");
						jRadioButtonVerbLimitation.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent arg0) {
								changeModel();
							}
						});
					}
					{
						jRadioButtonSimpleModel = new JRadioButton();
						jPanelModelOptions.add(jRadioButtonSimpleModel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 20, 5, 0), 0, 0));
						jPanelModelOptions.add(getJPanelModelImage(), new GridBagConstraints(1, 0, 1, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jRadioButtonSimpleModel.setText("Simple Model (MxM)");
						jRadioButtonSimpleModel.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent arg0) {
								changeModel();
							}
						});
					}
				}
				{
					jPanelBinaryModelVerbSelection = new JPanel();
					BoxLayout jPanelBinaryModelVerbSelectionLayout = new BoxLayout(jPanelBinaryModelVerbSelection, javax.swing.BoxLayout.X_AXIS);
					jPanelBinaryModelVerbSelection.setLayout(jPanelBinaryModelVerbSelectionLayout);
					jPanelBasics.add(jPanelBinaryModelVerbSelection, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					{
						jScrollPaneBinaryModelVerbsList = new JScrollPane();
						jPanelBinaryModelVerbSelection.add(jScrollPaneBinaryModelVerbsList);
						{
							jScrollPaneBinaryModelVerbsList.setViewportView(getJtableLexicalResourcesTobinaryVerbModel());
						}
					}
					jPanelBinaryModelVerbSelection.setVisible(false);
				}
			}
			{
				jTabbedPaneAdvanced = new JTabbedPane();
				jTabbedPaneMainComponent.addTab("Advanced", jTabbedPaneAdvanced);
				jPanelAdvanced = new JPanel();
				buttonGroup = new ButtonGroup();
				GridBagLayout jPanelAdvancedLayout = new GridBagLayout();
				jTabbedPaneAdvanced.addTab("POS-Tagging", null, jPanelAdvanced, null);
				jTabbedPaneAdvanced.addTab("Model Settings", null, getModelSettingPanel(), null);
				jTabbedPaneAdvanced.addTab("Relation Types", null, getJPanelRelationTypes(), null);
				jPanelAdvancedLayout.rowWeights = new double[] {0.1, 0.1};
				jPanelAdvancedLayout.rowHeights = new int[] {7, 7};
				jPanelAdvancedLayout.columnWeights = new double[] {0.1};
				jPanelAdvancedLayout.columnWidths = new int[] {7};
				jPanelAdvanced.setLayout(jPanelAdvancedLayout);
				jPanelAdvanced.add(getJPanelPosTagging(), new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}		
	}

	private JPanel getModelSettingPanel() {
		if(jPanelModelSetting == null)
		{
			jPanelModelSetting = new JPanel();
			GridBagLayout jPanelModelSettingLayout = new GridBagLayout();
			jPanelModelSettingLayout.rowWeights = new double[] {0.1, 0.1};
			jPanelModelSettingLayout.rowHeights = new int[] {7, 7};
			jPanelModelSettingLayout.columnWeights = new double[] {0.1};
			jPanelModelSettingLayout.columnWidths = new int[] {7};
			jPanelModelSetting.setLayout(jPanelModelSettingLayout);
			jPanelModelSetting.add(getJTabbedPaneVerbManagement(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelModelSetting.add(getJPanelAdvenacedOptionPane(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelModelSetting;
	}

	private JTable getJtableLexicalResourcesTobinaryVerbModel() {
		if(jTableSelectVerbs == null)
			jTableSelectVerbs = resources.getLexicalWordsjTable();
		return jTableSelectVerbs;
	}

	protected void changeModel() {
		URL icon = null;
		if(jRadioButtonBinaryModel.isSelected())
		{
			jPanelBinaryModelVerbSelection.setVisible(true);
			icon = getClass().getClassLoader().getResource("icons/relation_model_binary_verb_select_user.png");
		}
		else 
		{
			jPanelBinaryModelVerbSelection.setVisible(false);
			if(jRadioButtonBinaryVerbLimitation.isSelected())
			{
				icon = getClass().getClassLoader().getResource("icons/relation_model_binary_verb_limitation.png");
			}
			else if(jRadioButtonVerbLimitation.isSelected())
			{
				icon = getClass().getClassLoader().getResource("icons/relation_model_verb_limitation.png");

			}
			else
			{
				icon = getClass().getClassLoader().getResource("icons/relation_model_simple.png");
			}
		}
		jLabelImage.setIcon(new ImageIcon( Utils.getScaledImage(new ImageIcon(icon).getImage(), 400, 250)));
		jLabelImage.setOpaque(false);
	}

	@Override
	public boolean validateOptions() {
		if(jRadioButtonBinaryModel.isSelected())
		{
			if(getVerbLW() == null)
			{
				Workbench.getInstance().warn("Please select Lexical Words Resource that contains Verb");
				return false;
			}
		}
		RERelationAdvancedConfiguration advancedConfiguration = getRERelationAdvancedOptions() ;
		if(advancedConfiguration.getRelationsType()!=null && advancedConfiguration.getRelationsType().size() == 0)
		{
			Workbench.getInstance().warn("Please select at least one Relation Type");
			return false;
		}
		return true;
	}

	@Override
	public IREConfiguration getConfiguration(ICorpus corpus,IIEProcess entityProcess) {
		if(validateOptions())
		{
			RelationsModelEnem relationModel = getRelationModel();
			ILexicalWords verbAdittion = getVerbAddition();
			PosTaggerEnem posTagger = getPOSTagging();
			ILexicalWords verbFilter = getVerbFilter();
			IRERelationAdvancedConfiguration advanceConfiguration = getRERelationAdvancedOptions();
			IREConfiguration configuratin = new RERelationConfiguration(corpus, entityProcess, posTagger, relationModel, verbFilter, verbAdittion,advanceConfiguration);
			return configuratin;
		}
		return null;
	}
	
	private RERelationAdvancedConfiguration getRERelationAdvancedOptions() {
		SortedSet<IRelationsType> rt = getRelationsTypes();
		if(rt.size() == jtableSearchRelationTypes.getOriginalTableModel().getRowCount())
		{
			rt = null;
		}
		return new RERelationAdvancedConfiguration(jCheckBoxOnlyUseNearestEntitiesToVerb.isSelected(),jCheckBoxOnlyUseEntitiesNearestVerb.isSelected(),
				(Integer)jSpinnerMaxDistance.getValue(),rt);
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

	private ILexicalWords getVerbFilter() {
		for(int i=0;i<jTableVerbFilter.getRowCount();i++)
		{
			if((Boolean) jTableVerbFilter.getValueAt(i, 1))
			{
				return (ILexicalWords) jTableVerbFilter.getValueAt(i, 0);
			}
		}
		return null;
	}

	private PosTaggerEnem getPOSTagging() {
		if(jRadioButtonGatePosTagging.isSelected())
			return PosTaggerEnem.Gate_POS;
		else
			return PosTaggerEnem.LingPipe_POS;
	}

	private ILexicalWords getVerbAddition() {
		if(jRadioButtonBinaryModel.isSelected())
		{
			for(int i=0;i<jTableSelectVerbs.getRowCount();i++)
			{
				if((Boolean) jTableSelectVerbs.getValueAt(i, 1))
				{
					return (ILexicalWords) jTableSelectVerbs.getValueAt(i, 0);
				}
			}
		}
		else
		{
			for(int i=0;i<jTableVerbAddition.getRowCount();i++)
			{
				if((Boolean) jTableVerbAddition.getValueAt(i, 1))
				{
					return (ILexicalWords) jTableVerbAddition.getValueAt(i, 0);
				}
			}
		}
		return null;
	}
	
	private ILexicalWords getVerbLW()
	{
		for(int i=0;i<jTableSelectVerbs.getRowCount();i++)
		{
			if((Boolean) jTableSelectVerbs.getValueAt(i, 1))
			{
				return (ILexicalWords) jTableSelectVerbs.getValueAt(i, 0);
			}
		}
		return null;
	}

	private RelationsModelEnem getRelationModel() {
		if(jRadioButtonBinaryModel.isSelected())
			return RelationsModelEnem.Binary_Biomedical_Verbs;
		else if(jRadioButtonBinaryVerbLimitation.isSelected())
			return RelationsModelEnem.Binary_Verb_limitation;
		else if(jRadioButtonVerbLimitation.isSelected())
			return RelationsModelEnem.Verb_Limitation;
		else
			return RelationsModelEnem.Simple_Model;
	}

	@Override
	public REWorkflowProcessesAvailableEnum getREProcess() {
		return REWorkflowProcessesAvailableEnum.Relation;
	}
	
	private ButtonGroup getButtonGroupModels() {
		if(buttonGroupModels == null) {
			buttonGroupModels = new ButtonGroup();
			buttonGroupModels.add(jRadioButtonBinaryModel);
			buttonGroupModels.add(jRadioButtonBinaryVerbLimitation);
			buttonGroupModels.add(jRadioButtonSimpleModel);
			buttonGroupModels.add(jRadioButtonVerbLimitation);
		}
		return buttonGroupModels;
	}
	
	private ButtonGroup getButtonGroupPOSTagging() {
		if(buttonGroupPOSTagging == null) {
			buttonGroupPOSTagging = new ButtonGroup();
			buttonGroupPOSTagging.add(jRadioButtonGatePosTagging);
			buttonGroupPOSTagging.add(jRadioButtonLingPipePOSTAgging);
		}
		return buttonGroupPOSTagging;
	}
	
	private JPanel getJPanelPosTagging() {
		if(jPanelPosTagging == null) {
			jPanelPosTagging = new JPanel();
			GridBagLayout jPanelPosTaggingLayout = new GridBagLayout();
			jPanelPosTaggingLayout.rowWeights = new double[] {0.1, 0.1};
			jPanelPosTaggingLayout.rowHeights = new int[] {7, 7};
			jPanelPosTaggingLayout.columnWeights = new double[] {0.1};
			jPanelPosTaggingLayout.columnWidths = new int[] {7};
			jPanelPosTagging.setLayout(jPanelPosTaggingLayout);
			jPanelPosTagging.setBorder(BorderFactory.createTitledBorder("Select POS-Tagging"));
			jPanelPosTagging.add(getJRadioButtonGatePosTagging(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 20, 5, 0), 0, 0));
			jPanelPosTagging.add(getJCheckBoxLingPipePOSTAgging(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 20, 5, 0), 0, 0));
		}
		return jPanelPosTagging;
	}
	
	private JRadioButton getJRadioButtonGatePosTagging() {
		if(jRadioButtonGatePosTagging == null) {
			jRadioButtonGatePosTagging = new JRadioButton();
			jRadioButtonGatePosTagging.setText("Gate (Standard)");
		}
		return jRadioButtonGatePosTagging;
	}
	
	private JRadioButton getJCheckBoxLingPipePOSTAgging() {
		if(jRadioButtonLingPipePOSTAgging == null) {
			jRadioButtonLingPipePOSTAgging = new JRadioButton();
			jRadioButtonLingPipePOSTAgging.setText("Ling Pipe (Training with Biomedical model)");
		}
		return jRadioButtonLingPipePOSTAgging;
	}
	
	private JTabbedPane getJTabbedPaneVerbManagement() {
		if(jTabbedPaneVerbManagement == null) {
			jTabbedPaneVerbManagement = new JTabbedPane();
			jTabbedPaneVerbManagement.setBorder(BorderFactory.createTitledBorder("Select list of verbs (Clues)"));
			jTabbedPaneVerbManagement.addTab("Addition", null, getJScrollPaneVerbAddition(), null);
			jTabbedPaneVerbManagement.addTab("Filter", null, getJScrollPaneVerbFilter(), null);
		}
		return jTabbedPaneVerbManagement;
	}
	
	private JScrollPane getJScrollPaneVerbAddition() {
		if(jScrollPaneVerbAddition == null) {
			jScrollPaneVerbAddition = new JScrollPane();
			jScrollPaneVerbAddition.setViewportView(getJTableVerbAddition());
		}
		return jScrollPaneVerbAddition;
	}
	
	private JScrollPane getJScrollPaneVerbFilter() {
		if(jScrollPaneVerbFilter == null) {
			jScrollPaneVerbFilter = new JScrollPane();
			jScrollPaneVerbFilter.setViewportView(getJTableVerbFilter());
		}
		return jScrollPaneVerbFilter;
	}
	
	private JTable getJTableVerbAddition() {
		if(jTableVerbAddition == null) {
			jTableVerbAddition = resources.getLexicalWordsjTable();
		}
		return jTableVerbAddition;
	}
	
	private JTable getJTableVerbFilter() {
		if(jTableVerbFilter == null) {
			jTableVerbFilter = resources.getLexicalWordsjTable();
		}
		return jTableVerbFilter;
	}
	
	private JPanel getJPanelModelImage() {
		if(jPanelModelImage == null) {
			jPanelModelImage = new JPanel();
			GridBagLayout jPanelModelImageLayout = new GridBagLayout();
//			jPanelModelImage.setBorder(BorderFactory.createTitledBorder("Model Details"));
			jPanelModelImageLayout.rowWeights = new double[] {0.1};
			jPanelModelImageLayout.rowHeights = new int[] {7};
			jPanelModelImageLayout.columnWeights = new double[] {0.1};
			jPanelModelImageLayout.columnWidths = new int[] {7};
			jPanelModelImage.setLayout(jPanelModelImageLayout);
			jPanelModelImage.add(getJLabelImage(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelModelImage;
	}
	
	private JLabel getJLabelImage() {
		if(jLabelImage == null) {
			jLabelImage = new JLabel();
			URL icon = getClass().getClassLoader().getResource("icons/relation_model_binary_verb_limitation.png");
			ImageIcon imageicon = new ImageIcon( Utils.getScaledImage(new ImageIcon(icon).getImage(), 400, 250));
			jLabelImage.setIcon(imageicon);
		}
		return jLabelImage;
	}
	
	private JPanel getJPanelRelationTypes() {
		if(jPanelRelationTypes == null) {
			jPanelRelationTypes = new JPanel();
			GridBagLayout jPanelRelationTypesLayout = new GridBagLayout();
			jPanelRelationTypesLayout.rowWeights = new double[] {0.1};
			jPanelRelationTypesLayout.rowHeights = new int[] {7};
			jPanelRelationTypesLayout.columnWeights = new double[] {0.1};
			jPanelRelationTypesLayout.columnWidths = new int[] {7};
			jPanelRelationTypes.setLayout(jPanelRelationTypesLayout);
			jPanelRelationTypes.add(getJPanelRelationTypesMainPAnel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelRelationTypes;
	}
	
	private JPanel getJPanelAdvenacedOptionPane() {
		if(jPanelAdvenacedOptionPane == null) {
			jPanelAdvenacedOptionPane = new JPanel();
			GridBagLayout jPanelAdvenacedOptionPaneLayout = new GridBagLayout();
			jPanelAdvenacedOptionPane.setPreferredSize(new java.awt.Dimension(219, 202));
			jPanelAdvenacedOptionPaneLayout.rowWeights = new double[] {0.1};
			jPanelAdvenacedOptionPaneLayout.rowHeights = new int[] {7};
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
			jPanelRelationverbEntitiesDistanceLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.05};
			jPanelRelationverbEntitiesDistanceLayout.rowHeights = new int[] {7, 7, 20, 7, 20};
			jPanelRelationverbEntitiesDistanceLayout.columnWeights = new double[] {0.1, 0.1, 0.025};
			jPanelRelationverbEntitiesDistanceLayout.columnWidths = new int[] {7, 20, 7};
			jPanelRelationContrains.setLayout(jPanelRelationverbEntitiesDistanceLayout);
			jPanelRelationContrains.add(getJCheckBoxUsingMaxDistanceVerbEntities(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
			jPanelRelationContrains.add(getJSpinnerMaxDistance(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelRelationContrains.add(getJCheckBoxOnlyUseMearestEntitiesToVerb(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
			jPanelRelationContrains.add(getJLabelMaxDistance(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelRelationContrains.add(getJCheckBoxOnlyUseEntitiesNearestVerb(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
			jPanelRelationContrains.add(getJCheckBoxRelationConstrainsWithout(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
		}
		return jPanelRelationContrains;
	}
	
	private JCheckBox getJCheckBoxUsingMaxDistanceVerbEntities() {
		if(jCheckBoxUsingMaxDistanceVerbEntities == null) {
			jCheckBoxUsingMaxDistanceVerbEntities = new JCheckBox();
			jCheckBoxUsingMaxDistanceVerbEntities.setText("Using Max Distance Verb-Entities (In Words)");
			buttonGroup.add(jCheckBoxUsingMaxDistanceVerbEntities);
		}
		return jCheckBoxUsingMaxDistanceVerbEntities;
	}
	
	private JSpinner getJSpinnerMaxDistance() {
		if(jSpinnerMaxDistance == null) {
			SpinnerNumberModel jSpinnerMaxDistanceModel = new SpinnerNumberModel(100, 1, 500, 1);
			jSpinnerMaxDistance = new JSpinner();
			jSpinnerMaxDistance.setModel(jSpinnerMaxDistanceModel);
		}
		return jSpinnerMaxDistance;
	}
	
	private JLabel getJLabelMaxDistance() {
		if(jLabelMaxDistance == null) {
			jLabelMaxDistance = new JLabel();
			jLabelMaxDistance.setText("Max Words Distance :");
		}
		return jLabelMaxDistance;
	}
	
	private JCheckBox getJCheckBoxOnlyUseMearestEntitiesToVerb() {
		if(jCheckBoxOnlyUseNearestEntitiesToVerb == null) {
			jCheckBoxOnlyUseNearestEntitiesToVerb = new JCheckBox();
			jCheckBoxOnlyUseNearestEntitiesToVerb.setText("Keep only relations where verbs are associated only with the nearest entities");
			buttonGroup.add(jCheckBoxOnlyUseNearestEntitiesToVerb);
		}
		return jCheckBoxOnlyUseNearestEntitiesToVerb;
	}
	
	private JPanel getJPanelRelationTypesMainPAnel() {
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
	
	private void completeTable(JTable mainTable) {
		mainTable.setRowHeight(20);
		mainTable.getColumnModel().getColumn(1).setMaxWidth(GlobalGUIOptions.tableSelectionSize);
		mainTable.getColumnModel().getColumn(1).setMinWidth(GlobalGUIOptions.tableSelectionSize);
		mainTable.getColumnModel().getColumn(1).setPreferredWidth(GlobalGUIOptions.tableSelectionSize);
	}
	
	
	private TableModel getRelationTypeModal() {
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
		Set<Integer> classIDS = ClassProperties.getClassIDClass().keySet();
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
	private TableSearchPanel getJPanel1() {
		if(jtableSearchRelationTypes == null) {
			jtableSearchRelationTypes = new TableSearchPanel(getRelationTypeModal(),false);
			completeTable(jtableSearchRelationTypes.getMainTable());
		}
		return jtableSearchRelationTypes;
	}
	
	protected void changeOriginalTableModel() {
		boolean selectedAll = !jButtonAllSelected   ;
		for(int i=0;i<this.jtableSearchRelationTypes.getOriginalTableModel().getRowCount();i++)
		{
			this.jtableSearchRelationTypes.getOriginalTableModel().setValueAt(selectedAll, i, 1);
		}			
		if(selectedAll)
			jButtonSelectAllOrNot.setText("Unselect");
		else
			jButtonSelectAllOrNot.setText("Select All");
		jButtonAllSelected= selectedAll;
		jPanelRelationTypesMainPAnel.updateUI();
	}
	
	private JCheckBox getJCheckBoxOnlyUseEntitiesNearestVerb() {
		if(jCheckBoxOnlyUseEntitiesNearestVerb == null) {
			jCheckBoxOnlyUseEntitiesNearestVerb = new JCheckBox();
			jCheckBoxOnlyUseEntitiesNearestVerb.setText("keep only relations where the entities are only associated to the nearest verb");
			buttonGroup.add(jCheckBoxOnlyUseEntitiesNearestVerb);
		}
		return jCheckBoxOnlyUseEntitiesNearestVerb;
	}
	
	private JCheckBox getJCheckBoxRelationConstrainsWithout() {
		if(jCheckBoxRelationConstrainsWithout == null) {
			jCheckBoxRelationConstrainsWithout = new JCheckBox();
			jCheckBoxRelationConstrainsWithout.setText("Without");
			jCheckBoxRelationConstrainsWithout.setSelected(true);
			buttonGroup.add(jCheckBoxOnlyUseEntitiesNearestVerb);
		}
		return jCheckBoxRelationConstrainsWithout;
	}
}
