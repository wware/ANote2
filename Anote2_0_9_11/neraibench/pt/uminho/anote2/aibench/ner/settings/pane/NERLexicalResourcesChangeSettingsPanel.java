package pt.uminho.anote2.aibench.ner.settings.pane;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import pt.uminho.anote2.aibench.ner.configuration.NERLexicalResourcesPreProssecingEnum;
import pt.uminho.anote2.aibench.ner.gui.help.ResourcesFinderGUIHelp;
import pt.uminho.anote2.aibench.ner.settings.NERLexicalResourcesDefaultSettings;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.resources.lexiacalwords.LexicalWords;
import pt.uminho.anote2.datastructures.resources.rule.RulesSet;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;
import pt.uminho.anote2.resource.rules.IRule;



public class NERLexicalResourcesChangeSettingsPanel extends JPanel{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Map<String, Object> initial_props;
	protected JCheckBox jCheckBoxCaseSensitive;
	protected JCheckBox jCheckBoxNormalization;
	protected JComboBox jComboBoxRulesSet;
	private JPanel jPanelRuleSetSelection;
	protected JCheckBox jCheckBoxPartialMatchWithDictionaries;
	protected JPanel jPanelStopWords;
	protected JComboBox jComboBoxStopWords;
	protected JComboBox jComboBoxPreProcessing;
	protected JPanel jPanelPreProcessing;
	protected JPanel jPanelRulesMAtchingWithDictionaries;
	protected JPanel jPanelNormalization;
	protected JPanel jPanelCaseSensitive;
	private ResourcesFinderGUIHelp resourceHelp;

	
	public NERLexicalResourcesChangeSettingsPanel(Map<String, Object> initial_props, Map<String, Object> defaultProps) throws SQLException, DatabaseLoadDriverException {
		
		if(initial_props.isEmpty()) 
			initial_props = defaultProps;
		else
			this.initial_props = initial_props;
		{
			resourceHelp = new ResourcesFinderGUIHelp();
			initGUI();
			fillSettings();
		}
	}


	protected void fillSettings() {
		jCheckBoxCaseSensitive.setSelected(Boolean.parseBoolean(initial_props.get(NERLexicalResourcesDefaultSettings.CASE_SENSITIVE).toString()));
		jCheckBoxNormalization.setSelected(Boolean.parseBoolean(initial_props.get(NERLexicalResourcesDefaultSettings.NORMALIZATION).toString()));
		jCheckBoxPartialMatchWithDictionaries.setSelected(Boolean.parseBoolean(initial_props.get(NERLexicalResourcesDefaultSettings.USE_PARTIAL_MATCH_WITH_DICTIONARIES).toString()));
		jComboBoxPreProcessing.setSelectedItem(initial_props.get(NERLexicalResourcesDefaultSettings.PRE_PROCESSING));
		jComboBoxStopWords.setSelectedItem(new LexicalWords(Integer.valueOf(initial_props.get(NERLexicalResourcesDefaultSettings.LEXICAL_RESOURCE_STOPWORDS_ID).toString()), "", "") );
		jComboBoxRulesSet.setSelectedItem(new RulesSet(Integer.valueOf(initial_props.get(NERLexicalResourcesDefaultSettings.RULES_RESOURCE_ID).toString()), "", "") );

	}


	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			this.setPreferredSize(new java.awt.Dimension(631, 495));
			thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			thisLayout.rowHeights = new int[] {7, 7, 7, 20, 7, 20};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			{
				jPanelCaseSensitive = new JPanel();
				GridBagLayout jPanelCaseSensitiveLayout = new GridBagLayout();
				this.add(jPanelCaseSensitive, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelCaseSensitive.setBorder(BorderFactory.createTitledBorder("Case Sensitive"));
				jPanelCaseSensitiveLayout.rowWeights = new double[] {0.0, 0.1, 0.0};
				jPanelCaseSensitiveLayout.rowHeights = new int[] {7, 7, 7};
				jPanelCaseSensitiveLayout.columnWeights = new double[] {0.1, 0.1, 0.1};
				jPanelCaseSensitiveLayout.columnWidths = new int[] {7, 7, 7};
				jPanelCaseSensitive.setLayout(jPanelCaseSensitiveLayout);
				{
					jCheckBoxCaseSensitive = new JCheckBox();
					jPanelCaseSensitive.add(jCheckBoxCaseSensitive, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jCheckBoxCaseSensitive.setText("Case Sensitive");
				}
			}
			{
				jPanelNormalization = new JPanel();
				GridBagLayout jPanelNormalizationLayout = new GridBagLayout();
				this.add(jPanelNormalization, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelNormalization.setBorder(BorderFactory.createTitledBorder("Normalization"));
				jPanelNormalizationLayout.rowWeights = new double[] {0.0, 0.1, 0.0};
				jPanelNormalizationLayout.rowHeights = new int[] {7, 7, 7};
				jPanelNormalizationLayout.columnWeights = new double[] {0.1, 0.1, 0.1};
				jPanelNormalizationLayout.columnWidths = new int[] {7, 7, 7};
				jPanelNormalization.setLayout(jPanelNormalizationLayout);
				{
					jCheckBoxNormalization = new JCheckBox();
					jPanelNormalization.add(jCheckBoxNormalization, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jCheckBoxNormalization.setText("Normalization");
				}
			}
			{
				jPanelRulesMAtchingWithDictionaries = new JPanel();
				GridBagLayout jPanelRulesMAtchingWithDictionariesLayout = new GridBagLayout();
				this.add(jPanelRulesMAtchingWithDictionaries, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelRulesMAtchingWithDictionaries.setBorder(BorderFactory.createTitledBorder("Partial Match with Dictionaries"));
				jPanelRulesMAtchingWithDictionariesLayout.rowWeights = new double[] {0.0, 0.1, 0.0};
				jPanelRulesMAtchingWithDictionariesLayout.rowHeights = new int[] {7, 7, 7};
				jPanelRulesMAtchingWithDictionariesLayout.columnWeights = new double[] {0.1, 0.1, 0.1};
				jPanelRulesMAtchingWithDictionariesLayout.columnWidths = new int[] {7, 7, 7};
				jPanelRulesMAtchingWithDictionaries.setLayout(jPanelRulesMAtchingWithDictionariesLayout);
				{
					jCheckBoxPartialMatchWithDictionaries = new JCheckBox();
					jPanelRulesMAtchingWithDictionaries.add(jCheckBoxPartialMatchWithDictionaries, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jCheckBoxPartialMatchWithDictionaries.setText("Partial Match with Dictionaries");
				}
			}
			{
				jPanelPreProcessing = new JPanel();
				GridBagLayout jPanelPreProcessingLayout = new GridBagLayout();
				this.add(jPanelPreProcessing, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelPreProcessing.setBorder(BorderFactory.createTitledBorder("Pre-Processing"));
				jPanelPreProcessingLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
				jPanelPreProcessingLayout.rowHeights = new int[] {7, 7, 7};
				jPanelPreProcessingLayout.columnWeights = new double[] {0.025, 0.1, 0.025};
				jPanelPreProcessingLayout.columnWidths = new int[] {7, 7, 7};
				jPanelPreProcessing.setLayout(jPanelPreProcessingLayout);
				{
					ComboBoxModel jComboBoxPreProcessingModel = 
							new DefaultComboBoxModel(NERLexicalResourcesPreProssecingEnum.values());
					jComboBoxPreProcessing = new JComboBox();
					jPanelPreProcessing.add(jComboBoxPreProcessing, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jComboBoxPreProcessing.setModel(jComboBoxPreProcessingModel);
				}
			}
			{
				jPanelStopWords = new JPanel();
				GridBagLayout jPanelStopWordsLayout = new GridBagLayout();
				this.add(jPanelStopWords, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				this.add(getJPanelRuleSetSelection(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelStopWords.setBorder(BorderFactory.createTitledBorder("Stop Words"));
				jPanelStopWordsLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
				jPanelStopWordsLayout.rowHeights = new int[] {7, 7, 7};
				jPanelStopWordsLayout.columnWeights = new double[] {0.025, 0.1, 0.025};
				jPanelStopWordsLayout.columnWidths = new int[] {7, 7, 7};
				jPanelStopWords.setLayout(jPanelStopWordsLayout);
				{
					ComboBoxModel jComboBoxStopWordsModel = getLexicalWords();
					jComboBoxStopWords = new JComboBox();
					jPanelStopWords.add(jComboBoxStopWords, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jComboBoxStopWords.setModel(jComboBoxStopWordsModel);
				}
			}
		}
		
	}


	private ComboBoxModel getLexicalWords() {
		ILexicalWords lw0 = new LexicalWords(0, "", "");
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		model.addElement(lw0);
		List<IResource<IResourceElement>> list = resourceHelp.getLexicalWords();
		for(IResource<IResourceElement> rsource : list)
		{
			model.addElement(rsource);
		}
		return model;
	}


	public Map<String, Object> getProperties() {
		HashMap<String, Object> settings = new HashMap<String, Object>();
		settings.put(NERLexicalResourcesDefaultSettings.USE_PARTIAL_MATCH_WITH_DICTIONARIES, jCheckBoxPartialMatchWithDictionaries.isSelected());
		settings.put(NERLexicalResourcesDefaultSettings.CASE_SENSITIVE, jCheckBoxCaseSensitive.isSelected());
		settings.put(NERLexicalResourcesDefaultSettings.PRE_PROCESSING, jComboBoxPreProcessing.getSelectedItem());
		settings.put(NERLexicalResourcesDefaultSettings.NORMALIZATION, jCheckBoxNormalization.isSelected());
		settings.put(NERLexicalResourcesDefaultSettings.LEXICAL_RESOURCE_STOPWORDS_ID, ((ILexicalWords) jComboBoxStopWords.getSelectedItem()).getID());
		settings.put(NERLexicalResourcesDefaultSettings.RULES_RESOURCE_ID, ((IRule) jComboBoxRulesSet.getSelectedItem()).getID());
		return settings;
	}


	public boolean haveChanged() {
		if(!initial_props.get(NERLexicalResourcesDefaultSettings.USE_PARTIAL_MATCH_WITH_DICTIONARIES).equals(jCheckBoxPartialMatchWithDictionaries.isSelected()))
		{
			return true;
		}
		else if(!initial_props.get(NERLexicalResourcesDefaultSettings.CASE_SENSITIVE).equals(jCheckBoxCaseSensitive.isSelected()))
		{
			return true;
		}
		else if(!initial_props.get(NERLexicalResourcesDefaultSettings.PRE_PROCESSING).equals(jComboBoxPreProcessing.getSelectedItem()))
		{
			return true;
		}
		else if(!initial_props.get(NERLexicalResourcesDefaultSettings.NORMALIZATION).equals(jCheckBoxNormalization.isSelected()))
		{
			return true;
		}
		else if(!initial_props.get(NERLexicalResourcesDefaultSettings.LEXICAL_RESOURCE_STOPWORDS_ID).equals(((ILexicalWords) jComboBoxStopWords.getSelectedItem()).getID()))
		{
			return true;
		}
		else if(!initial_props.get(NERLexicalResourcesDefaultSettings.RULES_RESOURCE_ID).equals(((IRule) jComboBoxRulesSet.getSelectedItem()).getID()))
		{
			return true;
		}
		return false;
	}
	
	public void setInitial_props(Map<String, Object> initial_props) {
		this.initial_props = initial_props;
	}
	
	private JPanel getJPanelRuleSetSelection() {
		if(jPanelRuleSetSelection == null) {
			jPanelRuleSetSelection = new JPanel();
			GridBagLayout jPanelRuleSetSelectionLayout = new GridBagLayout();
			jPanelRuleSetSelection.setBorder(BorderFactory.createTitledBorder("Rule-Set Selecting"));
			jPanelRuleSetSelectionLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanelRuleSetSelectionLayout.rowHeights = new int[] {7, 7, 7};
			jPanelRuleSetSelectionLayout.columnWeights = new double[] {0.025, 0.1, 0.025};
			jPanelRuleSetSelectionLayout.columnWidths = new int[] {7, 7, 7};
			jPanelRuleSetSelection.setLayout(jPanelRuleSetSelectionLayout);
			jPanelRuleSetSelection.add(getJComboBoxRulesSet(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelRuleSetSelection;
	}
	
	private JComboBox getJComboBoxRulesSet() {
		if(jComboBoxRulesSet == null) {
			ComboBoxModel jComboBoxRulesSetModel = getRuleComboBox();
			jComboBoxRulesSet = new JComboBox();
			jComboBoxRulesSet.setModel(jComboBoxRulesSetModel);
		}
		return jComboBoxRulesSet;
	}


	private ComboBoxModel getRuleComboBox() {
		IRule rule = new RulesSet(0, "", "");
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		model.addElement(rule);
		List<IResource<IResourceElement>> list = resourceHelp.getRules();
		for(IResource<IResourceElement> rsource : list)
		{
			model.addElement(rsource);
		}
		return model;
	}

}
