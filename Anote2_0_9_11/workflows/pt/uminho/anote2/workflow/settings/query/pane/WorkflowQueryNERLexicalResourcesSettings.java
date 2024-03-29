package pt.uminho.anote2.workflow.settings.query.pane;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import pt.uminho.anote2.aibench.ner.settings.pane.NERLexicalResourcesChangeSettingsPanel;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.resources.lexiacalwords.LexicalWords;
import pt.uminho.anote2.datastructures.resources.rule.RulesSet;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;
import pt.uminho.anote2.resource.rules.IRule;
import pt.uminho.anote2.workflow.datastructures.IChangeSettings;
import pt.uminho.anote2.workflow.settings.query.WorkflowQueryDefaulSettings;

public class WorkflowQueryNERLexicalResourcesSettings extends NERLexicalResourcesChangeSettingsPanel implements IChangeSettings{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public WorkflowQueryNERLexicalResourcesSettings(Map<String, Object> initial_props, Map<String, Object> defaultProps) throws SQLException, DatabaseLoadDriverException {
		super(initial_props, defaultProps);
	}
	
	@Override
	protected void fillSettings() {
		jCheckBoxCaseSensitive.setSelected(Boolean.parseBoolean(initial_props.get(WorkflowQueryDefaulSettings.CASE_SENSITIVE).toString()));
		jCheckBoxNormalization.setSelected(Boolean.parseBoolean(initial_props.get(WorkflowQueryDefaulSettings.NORMALIZATION).toString()));
		jCheckBoxPartialMatchWithDictionaries.setSelected(Boolean.parseBoolean(initial_props.get(WorkflowQueryDefaulSettings.USE_PARTIAL_MATCH_WITH_DICTIONARIES).toString()));
		jComboBoxPreProcessing.setSelectedItem(initial_props.get(WorkflowQueryDefaulSettings.PRE_PROCESSING));
		jComboBoxStopWords.setSelectedItem(new LexicalWords(Integer.valueOf(initial_props.get(WorkflowQueryDefaulSettings.LEXICAL_RESOURCE_STOPWORDS_ID).toString()), "", "") );
		jComboBoxRulesSet.setSelectedItem(new RulesSet(Integer.valueOf(initial_props.get(WorkflowQueryDefaulSettings.RULES_RESOURCE_ID).toString()), "", "") );

	}
	
	public void defaultSettings() {
		fillSettings();
	}
	
	
	@Override
	public Map<String, Object> getProperties() {
		HashMap<String, Object> settings = new HashMap<String, Object>();
		settings.put(WorkflowQueryDefaulSettings.USE_PARTIAL_MATCH_WITH_DICTIONARIES, jCheckBoxPartialMatchWithDictionaries.isSelected());
		settings.put(WorkflowQueryDefaulSettings.CASE_SENSITIVE, jCheckBoxCaseSensitive.isSelected());
		settings.put(WorkflowQueryDefaulSettings.PRE_PROCESSING, jComboBoxPreProcessing.getSelectedItem());
		settings.put(WorkflowQueryDefaulSettings.NORMALIZATION, jCheckBoxNormalization.isSelected());
		settings.put(WorkflowQueryDefaulSettings.LEXICAL_RESOURCE_STOPWORDS_ID, ((ILexicalWords) jComboBoxStopWords.getSelectedItem()).getID());
		settings.put(WorkflowQueryDefaulSettings.RULES_RESOURCE_ID, ((IRule) jComboBoxRulesSet.getSelectedItem()).getID());
		return settings;
	}

	@Override
	public boolean haveChanged() {
		if(!initial_props.get(WorkflowQueryDefaulSettings.USE_PARTIAL_MATCH_WITH_DICTIONARIES).equals(jCheckBoxPartialMatchWithDictionaries.isSelected()))
		{
			return true;
		}
		else if(!initial_props.get(WorkflowQueryDefaulSettings.CASE_SENSITIVE).equals(jCheckBoxCaseSensitive.isSelected()))
		{
			return true;
		}
		else if(!initial_props.get(WorkflowQueryDefaulSettings.PRE_PROCESSING).equals(jComboBoxPreProcessing.getSelectedItem()))
		{
			return true;
		}
		else if(!initial_props.get(WorkflowQueryDefaulSettings.NORMALIZATION).equals(jCheckBoxNormalization.isSelected()))
		{
			return true;
		}
		else if(!initial_props.get(WorkflowQueryDefaulSettings.LEXICAL_RESOURCE_STOPWORDS_ID).equals(((ILexicalWords) jComboBoxStopWords.getSelectedItem()).getID()))
		{
			return true;
		}
		return false;
	}



}
