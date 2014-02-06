package pt.uminho.anote2.workflow.gui.help;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.ner.configuration.INERLexicalResourcesConfiguration;
import pt.uminho.anote2.aibench.ner.configuration.NERLexicalResourcesConfiguration;
import pt.uminho.anote2.aibench.ner.configuration.NERLexicalResourcesPreProssecingEnum;
import pt.uminho.anote2.aibench.ner.datastructures.ResourcesToNerAnote;
import pt.uminho.anote2.aibench.ner.gui.help.POSTagsHelpGUI;
import pt.uminho.anote2.aibench.ner.gui.help.ResourcesFinderGUIHelp;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.nlptools.PartOfSpeechLabels;
import pt.uminho.anote2.datastructures.resources.lexiacalwords.LexicalWords;
import pt.uminho.anote2.datastructures.resources.rule.RulesSet;
import pt.uminho.anote2.datastructures.utils.GenericTriple;
import pt.uminho.anote2.datastructures.utils.conf.GlobalGUIOptions;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.process.IE.ner.INERConfiguration;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;
import pt.uminho.anote2.resource.rules.IRule;
import pt.uminho.anote2.workflow.datastructures.NERWorkflowProcessesAvailableEnum;
import pt.uminho.anote2.workflow.gui.ANERPanel;
import es.uvigo.ei.aibench.workbench.Workbench;


public class NERLexicalResourcesPanel extends ANERPanel{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPaneDictionaries;
	private JTable jTableDictionaries;
	private JPanel jPanelAdvance;
	private JPanel jPanelBasicsPanel;
	private JTabbedPane jMainComponent;
	private ResourcesFinderGUIHelp resources;
	private JScrollPane jScrollPaneLookupTables;
	private JRadioButton jRadioButtonWithout;
	private JCheckBox jCheckBoxUsingExtraInformation;
	private JPanel jPanelUsingRulesExtraInfo;
	private JTable jTableClasses;
	private JScrollPane jScrollPaneSelectResourceClasses;
	private JTable jTablePOSTags;
	private JTable jTableLexicalWords;
	private JScrollPane jScrollPanePOSTags;
	private JScrollPane jScrollPaneStopWords;
	private JRadioButton jRadioButtonPosTaggingAndStopWords;
	private JRadioButton jRadioButtonPOSTagging;
	private JRadioButton jRadioButtonStopWords;
	private JScrollPane jScrollPaneOntologies;
	private JPanel jPanelPreProcessingOptions;
	private JTabbedPane jTabbedPanePreProcessing;
	private ButtonGroup buttonGroupPreprocessing;
	private JCheckBox jCheckBoxNormalization;
	private JCheckBox jCheckBoxCaseSensitive;
	private JPanel jPanelPreprocessing;
	private JPanel jPanelNormalization;
	private JPanel jPanelCaseSensitive;
	private JPanel jPanelGeneral;
	private JPanel jPanelGeneralOptions;
	private JScrollPane jScrollPaneRulesSet;
	private JTable jTableRulesSet;
	private JTabbedPane jTabbedPaneOtherResources;
	private JTable jTableLookupTables;
	private JTable jTableOntologies;
	private Map<Integer,Set<Integer>> resourceIDClassesSelected;

	public NERLexicalResourcesPanel(List<String> defaultSettings) throws SQLException, DatabaseLoadDriverException
	{
		preprocessing();
		initGUI();
		completeGUI();
		defaultSettings(defaultSettings);
	}	

	public NERLexicalResourcesPanel(INERConfiguration defaultSettings) throws SQLException, DatabaseLoadDriverException {
		preprocessing();
		initGUI();
		completeGUI();
		if(defaultSettings instanceof INERLexicalResourcesConfiguration)
		{
			INERLexicalResourcesConfiguration conf = (INERLexicalResourcesConfiguration) defaultSettings;
			jCheckBoxUsingExtraInformation.setSelected(conf.usingOtherResourceInfoToImproveRuleAnnotstions());
			jCheckBoxCaseSensitive.setSelected(conf.getResourceToNER().isCaseSensitive());
			jCheckBoxNormalization.setSelected(conf.isNormalized());
			NERLexicalResourcesPreProssecingEnum prepreocessing = conf.getPreProcessingOption();
			if(prepreocessing.equals(NERLexicalResourcesPreProssecingEnum.No))
			{
				jRadioButtonWithout.setSelected(true);
			}
			else if(prepreocessing.equals(NERLexicalResourcesPreProssecingEnum.POSTagging))
			{
				jRadioButtonPOSTagging.setSelected(true);
			}
			else if(prepreocessing.equals(NERLexicalResourcesPreProssecingEnum.StopWords))
			{
				jRadioButtonStopWords.setSelected(true);
				fillLExicalWords(conf);
			}
			else
			{
				jRadioButtonPosTaggingAndStopWords.setSelected(true);
				fillLExicalWords(conf);
			}
			Set<Integer> ids = new HashSet<Integer>();
			List<GenericTriple<IResource<IResourceElement>, Set<Integer>, Set<Integer>>> listResources = conf.getResourceToNER().getList();
			for(GenericTriple<IResource<IResourceElement>, Set<Integer>, Set<Integer>> resource : listResources)
			{
				ids.add(resource.getX().getID());
				resourceIDClassesSelected.put(resource.getX().getID(), resource.getZ());
			}
			fillDictionaries(ids );
			filllookupdaTables(ids);
			fillOntologies(ids);
			fillRuleSet(ids);
		}
	}

	private void fillLExicalWords(INERLexicalResourcesConfiguration conf) {
		int lexID = conf.getStopWords().getID();
		for(int i=0;i<jTableLexicalWords.getRowCount();i++)
		{
			if(((ILexicalWords)jTableLexicalWords.getValueAt(i, 0)).getID() == lexID)
			{
				jTableLexicalWords.setValueAt(true, i, 1);
			}
			else
			{
				jTableLexicalWords.setValueAt(false, i, 1);
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

	private void defaultSettings(List<String> defaultSettings) {
		if(defaultSettings!=null && !defaultSettings.isEmpty() && defaultSettings.size() == 6)
		{
			jCheckBoxUsingExtraInformation.setSelected(Boolean.parseBoolean(PropertiesManager.getPManager().getProperty(defaultSettings.get(0)).toString()));
			jCheckBoxCaseSensitive.setSelected(Boolean.parseBoolean(PropertiesManager.getPManager().getProperty(defaultSettings.get(1)).toString()));
			jCheckBoxNormalization.setSelected(Boolean.parseBoolean(PropertiesManager.getPManager().getProperty(defaultSettings.get(3)).toString()));
			NERLexicalResourcesPreProssecingEnum prepreocessing = (NERLexicalResourcesPreProssecingEnum) PropertiesManager.getPManager().getProperty(defaultSettings.get(2));
			if(prepreocessing.equals(NERLexicalResourcesPreProssecingEnum.No))
			{
				jRadioButtonWithout.setSelected(true);
			}
			else if(prepreocessing.equals(NERLexicalResourcesPreProssecingEnum.POSTagging))
			{
				jRadioButtonPOSTagging.setSelected(true);
			}
			else if(prepreocessing.equals(NERLexicalResourcesPreProssecingEnum.StopWords))
			{
				jRadioButtonStopWords.setSelected(true);
			}
			else
			{
				jRadioButtonPosTaggingAndStopWords.setSelected(true);
			}
			ILexicalWords lex = new LexicalWords(Integer.valueOf(PropertiesManager.getPManager().getProperty(defaultSettings.get(4)).toString()),"","");
			for(int i=0;i<jTableLexicalWords.getRowCount();i++)
			{
				if(jTableLexicalWords.getValueAt(i, 0).equals(lex))
				{
					jTableLexicalWords.setValueAt(true, i, 1);
				}
			}
			IRule ruleSet = new RulesSet(Integer.valueOf(PropertiesManager.getPManager().getProperty(defaultSettings.get(5)).toString()), "", "");
			for(int i=0;i<jTableRulesSet.getRowCount();i++)
			{
				if(jTableRulesSet.getValueAt(i, 0).equals(ruleSet))
				{
					jTableRulesSet.setValueAt(true, i, 1);
				}
			}
		}

	}

	private void preprocessing() throws SQLException, DatabaseLoadDriverException {
		resourceIDClassesSelected = new HashMap<Integer, Set<Integer>>();
		resources = new ResourcesFinderGUIHelp();
	}
	
	
	private void completeGUI() {
		getButtonGroupPreprocessing();
	}

	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1};
			thisLayout.rowHeights = new int[] {7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			{
				jMainComponent = new JTabbedPane();
				this.add(jMainComponent, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					jPanelBasicsPanel = new JPanel();
					GridBagLayout jPanelBasicsPanelLayout = new GridBagLayout();
					jMainComponent.addTab("Basic", null, jPanelBasicsPanel, null);
					jPanelBasicsPanelLayout.rowWeights = new double[] {0.1, 0.1};
					jPanelBasicsPanelLayout.rowHeights = new int[] {7, 20};
					jPanelBasicsPanelLayout.columnWeights = new double[] {0.1};
					jPanelBasicsPanelLayout.columnWidths = new int[] {7};
					jPanelBasicsPanel.setLayout(jPanelBasicsPanelLayout);
					jPanelBasicsPanel.setBorder(BorderFactory.createTitledBorder("Select Dictionaries"));
					{
						jScrollPaneDictionaries = new JScrollPane();
						jPanelBasicsPanel.add(jScrollPaneDictionaries, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jPanelBasicsPanel.add(getJScrollPaneSelectResourceClasses(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						{
							jScrollPaneDictionaries.setViewportView(getJtableDictionries());
						}
					}
				}
				{
					jPanelAdvance = new JPanel();
					GridBagLayout jPanelAdvanceLayout = new GridBagLayout();
					jMainComponent.addTab("Advanced", null, jPanelAdvance, null);
					jPanelAdvanceLayout.rowWeights = new double[] {0.1, 0.1};
					jPanelAdvanceLayout.rowHeights = new int[] {7, 7};
					jPanelAdvanceLayout.columnWeights = new double[] {0.1};
					jPanelAdvanceLayout.columnWidths = new int[] {7};
					jPanelAdvance.setLayout(jPanelAdvanceLayout);
					{
						jTabbedPaneOtherResources = new JTabbedPane();
						jPanelAdvance.add(jTabbedPaneOtherResources, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jTabbedPaneOtherResources.setBorder(BorderFactory.createTitledBorder("Other Lexical Resources"));
						{
							jScrollPaneLookupTables = new JScrollPane();
							jTabbedPaneOtherResources.addTab("Lookup Tables", null, jScrollPaneLookupTables, null);
							{
								jScrollPaneLookupTables.setViewportView(getLookupTablesjTable());
							}
						}
						{
							jScrollPaneRulesSet = new JScrollPane();
							jTabbedPaneOtherResources.addTab("Rules Set", null, jScrollPaneRulesSet, null);
							{
								jScrollPaneRulesSet.setViewportView(getRulesSetJtable());
							}
						}
						{
							jScrollPaneOntologies = new JScrollPane();
							jTabbedPaneOtherResources.addTab("Ontologies", null, jScrollPaneOntologies, null);
							{
								jScrollPaneOntologies.setViewportView(getOntologiesJtable());
							}
						}
					}
					{
						jPanelGeneralOptions = new JPanel();
						GridBagLayout jPanelGeneralOptionsLayout = new GridBagLayout();
						jPanelAdvance.add(jPanelGeneralOptions, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jPanelGeneralOptionsLayout.rowWeights = new double[] {0.1};
						jPanelGeneralOptionsLayout.rowHeights = new int[] {7};
						jPanelGeneralOptionsLayout.columnWeights = new double[] {0.15, 0.6};
						jPanelGeneralOptionsLayout.columnWidths = new int[] {7, 7};
						jPanelGeneralOptions.setLayout(jPanelGeneralOptionsLayout);
						{
							jPanelGeneral = new JPanel();
							GridBagLayout jPanelGeneralLayout = new GridBagLayout();
							jPanelGeneralOptions.add(jPanelGeneral, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
							jPanelGeneralLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
							jPanelGeneralLayout.rowHeights = new int[] {7, 7, 20};
							jPanelGeneralLayout.columnWeights = new double[] {0.1};
							jPanelGeneralLayout.columnWidths = new int[] {7};
							jPanelGeneral.setLayout(jPanelGeneralLayout);
							{
								jPanelCaseSensitive = new JPanel();
								GridBagLayout jPanelCaseSensitiveLayout = new GridBagLayout();
								jPanelGeneral.add(jPanelCaseSensitive, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
								jPanelCaseSensitive.setBorder(BorderFactory.createTitledBorder("Case Sensitive"));
								jPanelCaseSensitiveLayout.rowWeights = new double[] {0.1};
								jPanelCaseSensitiveLayout.rowHeights = new int[] {7};
								jPanelCaseSensitiveLayout.columnWeights = new double[] {0.1};
								jPanelCaseSensitiveLayout.columnWidths = new int[] {7};
								jPanelCaseSensitive.setLayout(jPanelCaseSensitiveLayout);
								{
									jCheckBoxCaseSensitive = new JCheckBox();
									jPanelCaseSensitive.add(jCheckBoxCaseSensitive, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
									jCheckBoxCaseSensitive.setText("Case Sensitive");
								}
							}
							{
								jPanelNormalization = new JPanel();
								GridBagLayout jPanelNormalizationLayout = new GridBagLayout();
								jPanelGeneral.add(jPanelNormalization, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
								jPanelGeneral.add(getJPanelUsingRulesExtraInfo(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
								jPanelNormalization.setBorder(BorderFactory.createTitledBorder("Normalization"));
								jPanelNormalizationLayout.rowWeights = new double[] {0.1};
								jPanelNormalizationLayout.rowHeights = new int[] {7};
								jPanelNormalizationLayout.columnWeights = new double[] {0.1};
								jPanelNormalizationLayout.columnWidths = new int[] {7};
								jPanelNormalization.setLayout(jPanelNormalizationLayout);
								{
									jCheckBoxNormalization = new JCheckBox();
									jPanelNormalization.add(jCheckBoxNormalization, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
									jCheckBoxNormalization.setText("Normalization");
								}
							}
						}
						{
							jPanelPreprocessing = new JPanel();
							GridBagLayout jPanelPreprocessingLayout = new GridBagLayout();
							jPanelGeneralOptions.add(jPanelPreprocessing, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
							jPanelPreprocessing.setBorder(BorderFactory.createTitledBorder("Pre-Processing"));
							jPanelPreprocessingLayout.rowWeights = new double[] {0.6};
							jPanelPreprocessingLayout.rowHeights = new int[] {7};
							jPanelPreprocessingLayout.columnWeights = new double[] {0.0, 0.4};
							jPanelPreprocessingLayout.columnWidths = new int[] {7, 20};
							jPanelPreprocessing.setLayout(jPanelPreprocessingLayout);
							jPanelPreprocessing.add(getJTabbedPanePreProcessing(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
							jPanelPreprocessing.add(getJPanelPreProcessingOptions(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						}
					}
				}
			}
		}
		
	}




	private JTable getJtableDictionries() {
		if(jTableDictionaries == null)
			jTableDictionaries = resources.getDictionariesjTable();
			jTableDictionaries.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {try {
				changeResourceClasses();
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			}
			}
		});
			jTableDictionaries.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent arg0) {}
			public void keyReleased(KeyEvent arg0) {try {
				changeResourceClasses();
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			}}
			public void keyPressed(KeyEvent arg0) {}
		});				
		return jTableDictionaries;
	}
	
	protected void changeResourceClasses() throws DatabaseLoadDriverException, SQLException {
		IResource<IResourceElement> resource = (IResource<IResourceElement>) jTableDictionaries.getValueAt(jTableDictionaries.getSelectedRow(), 0);
		boolean selected = (Boolean) jTableDictionaries.getValueAt(jTableDictionaries.getSelectedRow(), 1);
		if(selected)
		{
			if(!resourceIDClassesSelected.containsKey(resource.getID()))
			{
				resourceIDClassesSelected.put(resource.getID(), resource.getClassContent());
			}
			jTableClasses.setModel(getResourceClasses(resource));
			completeTable(jTableClasses);
		}
		else
		{
			jTableClasses.setModel(new DefaultTableModel());
		}
	}

	private void completeTable(JTable jTableClasses2) {
		jTableClasses2.getColumnModel().getColumn(1).setMaxWidth(GlobalGUIOptions.tableSelectionSize);
		jTableClasses2.getColumnModel().getColumn(1).setMinWidth(GlobalGUIOptions.tableSelectionSize);
		jTableClasses2.getColumnModel().getColumn(1).setPreferredWidth(GlobalGUIOptions.tableSelectionSize);
	}

	private TableModel getResourceClasses(IResource<IResourceElement> resource) throws DatabaseLoadDriverException, SQLException {
		String[] columns = new String[] {"Class","Sel"};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);	
		for(Integer classID: resource.getClassContent())
		{
			Object[] data = new Object[2];
			data[0] = ClassProperties.getClassIDClass().get(classID);
			if(resourceIDClassesSelected.get(resource.getID()).contains(classID))
			{
				data[1] = true;
			}
			else
			{
				data[1] = false;
			}
			tableModel.addRow(data);
		}
		return tableModel;
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

	private JTable getOntologiesJtable() {
		if(jTableOntologies == null)
			jTableOntologies = resources.getOntologiesjTable();
		return jTableOntologies;
	}

	
	private List<IResource<IResourceElement>> getResources() {
		List<IResource<IResourceElement>> list = new ArrayList<IResource<IResourceElement>>();
		getSelectresources(jTableDictionaries,list);
		getSelectresources(jTableLookupTables,list);
		getSelectresources(jTableRulesSet,list);
		getSelectresources(jTableOntologies,list);
		return list;
	}
	
	private void getSelectresources(JTable jtable,List<IResource<IResourceElement>> list) {
		int rows = jtable.getModel().getRowCount();
		for(int i=0;i<rows;i++)
		{
			boolean isactive = (Boolean) jtable.getModel().getValueAt(i, 1);
			if(isactive)
			{
				IResource<IResourceElement> resource = (IResource<IResourceElement>) jtable.getModel().getValueAt(i, 0);
				list.add(resource);
			}
		}
		
	}

	@Override
	public boolean validateOptions() {
		
		List<IResource<IResourceElement>> resources = getResources();
		if(resources.size()==0)
		{
			Workbench.getInstance().warn("Please select at least one resource");
			return false;
		}
		NERLexicalResourcesPreProssecingEnum preProcessing = getPreprocessing();
		Set<String> posTags = getPOSTagges();
		if(preProcessing == NERLexicalResourcesPreProssecingEnum.StopWords)
		{
			if(getStopWords() == null)
			{
				jTabbedPanePreProcessing.setSelectedIndex(1);
				Workbench.getInstance().warn("Please select one Lexical Words Resource");
				return false;
			}
		}	
		else if(preProcessing == NERLexicalResourcesPreProssecingEnum.POSTagging)
		{
			if(posTags.size() == 0)
			{
				jTabbedPanePreProcessing.setSelectedIndex(2);
				Workbench.getInstance().warn("Please select at least one POS-Tag label");
				return false;
			}
		}
		else if(preProcessing == NERLexicalResourcesPreProssecingEnum.Hybrid)
		{
			if(getStopWords() == null)
			{
				jTabbedPanePreProcessing.setSelectedIndex(1);
				Workbench.getInstance().warn("Please select one Lexical Words Resource");
				return false;
			}
			if(posTags.size() == 0)
			{
				jTabbedPanePreProcessing.setSelectedIndex(2);
				Workbench.getInstance().warn("Please select at least one POS-Tag label");
				return false;
			}
		}
		return true;
	}
	
	private Set<String> getPOSTagges() {
		Set<String> posTAgsSelected = new HashSet<String>();
		int rowSize = jTablePOSTags.getModel().getRowCount();
		for(int i=0;i<rowSize;i++)
		{
			boolean sel = (Boolean) jTablePOSTags.getValueAt(i,1);
			if(sel)
			{
				posTAgsSelected.add(((PartOfSpeechLabels) jTablePOSTags.getValueAt(i, 0)).value());
			}
		}
		return posTAgsSelected;
	}

	@Override
	public INERConfiguration getConfiguration(ICorpus corpus) throws DatabaseLoadDriverException, SQLException {
		if(validateOptions())
			return getNERLexicalResourceConfiguration(corpus);
		return null;
	}

	private INERConfiguration getNERLexicalResourceConfiguration(ICorpus corpus) throws DatabaseLoadDriverException, SQLException {
		List<IResource<IResourceElement>> resources = getResources();
		Set<String> posTgas = null;
		NERLexicalResourcesPreProssecingEnum preProcessing = getPreprocessing();
		ILexicalWords stopWords = null;
		if(preProcessing == NERLexicalResourcesPreProssecingEnum.StopWords)
		{
			stopWords = getStopWords();
		}
		else if(preProcessing == NERLexicalResourcesPreProssecingEnum.POSTagging)
		{
			posTgas = getPOSTagges();
		}
		else if(preProcessing == NERLexicalResourcesPreProssecingEnum.Hybrid)
		{
			stopWords = getStopWords();
			posTgas = getPOSTagges();
		}
		boolean caseSensitive = jCheckBoxCaseSensitive.isSelected();
		ResourcesToNerAnote resourceToNER = new ResourcesToNerAnote(caseSensitive,jCheckBoxUsingExtraInformation.isSelected());
		for(IResource<IResourceElement> resource :resources)
		{
			if(this.resourceIDClassesSelected.containsKey(resource.getID()))
			{
				resourceToNER.add(resource, resource.getClassContent(), this.resourceIDClassesSelected.get(resource.getID()));
			}
			else
			{
				resourceToNER.add(resource, resource.getClassContent(), resource.getClassContent());
			}
		}
		boolean normalized = jCheckBoxNormalization.isSelected();
		INERLexicalResourcesConfiguration configuration = new NERLexicalResourcesConfiguration(corpus, preProcessing, resourceToNER, posTgas, stopWords, caseSensitive,normalized,jCheckBoxUsingExtraInformation.isSelected());
		return configuration;
	}
	

	private ILexicalWords getStopWords() {
		for(int i=0;i<jTableLexicalWords.getRowCount();i++)
		{
			if((Boolean) jTableLexicalWords.getValueAt(i, 1))
			{
				return (ILexicalWords) jTableLexicalWords.getValueAt(i, 0);
			}
		}
		return null;
	}

	private NERLexicalResourcesPreProssecingEnum getPreprocessing() {
		if(jRadioButtonWithout.isSelected())
			return NERLexicalResourcesPreProssecingEnum.No;
		else if(jRadioButtonStopWords.isSelected())
			return NERLexicalResourcesPreProssecingEnum.StopWords;
		else if(jRadioButtonPOSTagging.isSelected())
			return NERLexicalResourcesPreProssecingEnum.POSTagging;
		else if(jRadioButtonPosTaggingAndStopWords.isSelected())
			return NERLexicalResourcesPreProssecingEnum.Hybrid;
		return null;
	}

	@Override
	public NERWorkflowProcessesAvailableEnum getNERProcess() {
		return NERWorkflowProcessesAvailableEnum.NERLexicalResources;
	}
	
	private ButtonGroup getButtonGroupPreprocessing() {
		if(buttonGroupPreprocessing == null) {
			buttonGroupPreprocessing = new ButtonGroup();
			buttonGroupPreprocessing.add(jRadioButtonPOSTagging);
			buttonGroupPreprocessing.add(jRadioButtonPosTaggingAndStopWords);
			buttonGroupPreprocessing.add(jRadioButtonWithout);
			buttonGroupPreprocessing.add(jRadioButtonStopWords);
		}
		return buttonGroupPreprocessing;
	}
	
	private JTabbedPane getJTabbedPanePreProcessing() {
		if(jTabbedPanePreProcessing == null) {
			jTabbedPanePreProcessing = new JTabbedPane();
			jTabbedPanePreProcessing.addTab("Stop Words", null, getJScrollPaneStopWords(), null);
			jTabbedPanePreProcessing.addTab("POS Tags", null, getJScrollPanePOSTags(), null);
		}
		return jTabbedPanePreProcessing;
	}
	
	private JPanel getJPanelPreProcessingOptions() {
		if(jPanelPreProcessingOptions == null) {
			jPanelPreProcessingOptions = new JPanel();
			GridBagLayout jPanelPreProcessingOptionsLayout = new GridBagLayout();
			jPanelPreProcessingOptionsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelPreProcessingOptionsLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelPreProcessingOptionsLayout.columnWeights = new double[] {0.1};
			jPanelPreProcessingOptionsLayout.columnWidths = new int[] {7};
			jPanelPreProcessingOptions.setLayout(jPanelPreProcessingOptionsLayout);
			jPanelPreProcessingOptions.add(getJRadioButtonWithout(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
			jPanelPreProcessingOptions.add(getJRadioButtonStopWords(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
			jPanelPreProcessingOptions.add(getJRadioButtonPOSTagging(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
			jPanelPreProcessingOptions.add(getJRadioButtonPosTaggingAndStopWords(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
		}
		return jPanelPreProcessingOptions;
	}
	
	private JRadioButton getJRadioButtonWithout() {
		if(jRadioButtonWithout == null) {
			jRadioButtonWithout = new JRadioButton();
			jRadioButtonWithout.setText("None");
		}
		return jRadioButtonWithout;
	}
	
	private JRadioButton getJRadioButtonStopWords() {
		if(jRadioButtonStopWords == null) {
			jRadioButtonStopWords = new JRadioButton();
			jRadioButtonStopWords.setText("StopWords");
		}
		return jRadioButtonStopWords;
	}
	
	private JRadioButton getJRadioButtonPOSTagging() {
		if(jRadioButtonPOSTagging == null) {
			jRadioButtonPOSTagging = new JRadioButton();
			jRadioButtonPOSTagging.setText("POS Tagging");
		}
		return jRadioButtonPOSTagging;
	}
	
	private JRadioButton getJRadioButtonPosTaggingAndStopWords() {
		if(jRadioButtonPosTaggingAndStopWords == null) {
			jRadioButtonPosTaggingAndStopWords = new JRadioButton();
			jRadioButtonPosTaggingAndStopWords.setText("POS Tagging + Stop Words");
		}
		return jRadioButtonPosTaggingAndStopWords;
	}
	
	private JScrollPane getJScrollPaneStopWords() {
		if(jScrollPaneStopWords == null) {
			jScrollPaneStopWords = new JScrollPane();
			jScrollPaneStopWords.setViewportView(getJTableLexicalWords());
		}
		return jScrollPaneStopWords;
	}
	
	private JScrollPane getJScrollPanePOSTags() {
		if(jScrollPanePOSTags == null) {
			jScrollPanePOSTags = new JScrollPane();
			jScrollPanePOSTags.setViewportView(getJTablePOSTags());
		}
		return jScrollPanePOSTags;
	}
	
	private JTable getJTableLexicalWords() {
		if(jTableLexicalWords == null) {
			jTableLexicalWords = resources.getLexicalWordsjTable();
		}
		return jTableLexicalWords;
	}
	
	private JTable getJTablePOSTags() {
		if(jTablePOSTags == null) {
			jTablePOSTags = POSTagsHelpGUI.getJtablePOSTags();
		}
		return jTablePOSTags;
	}
	
	private JScrollPane getJScrollPaneSelectResourceClasses() {
		if(jScrollPaneSelectResourceClasses == null) {
			jScrollPaneSelectResourceClasses = new JScrollPane();
			jScrollPaneSelectResourceClasses.setBorder(BorderFactory.createTitledBorder("Select Resource Classe(s)"));
			jScrollPaneSelectResourceClasses.setViewportView(getJTableClasses());
		}
		return jScrollPaneSelectResourceClasses;
	}

	private JTable getJTableClasses() {
		if(jTableClasses == null) {
			TableModel jTableClassesModel = 
					new DefaultTableModel();
			jTableClasses = new JTable(){

				private static final long serialVersionUID = 1L;

				public boolean isCellEditable(int x,int y){
					if(y==1)
						return true;
					return false;
				}

				public Class<?> getColumnClass(int column){
					if(column == 1)
						return Boolean.class;
					return String.class;
				}
			};
			jTableClasses.setModel(jTableClassesModel);
			jTableClasses.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {changeResourceClassesOption();
			}
		});
		}
		return jTableClasses;
	}

	protected void changeResourceClassesOption() {
		IResource<IResourceElement> resource = (IResource<IResourceElement>) jTableDictionaries.getValueAt(jTableDictionaries.getSelectedRow(), 0);
		String classe = (String) jTableClasses.getValueAt(jTableClasses.getSelectedRow(), 0);
		int classID = ClassProperties.getClassClassID().get(classe);
		Boolean selected = (Boolean) jTableClasses.getValueAt(jTableClasses.getSelectedRow(), 1);
		if(selected)
		{
			this.resourceIDClassesSelected.get(resource.getID()).add(classID);
		}
		else
		{
			this.resourceIDClassesSelected.get(resource.getID()).remove(classID);
		}

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