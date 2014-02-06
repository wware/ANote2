package pt.uminho.anote2.workflow.settings.query;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import pt.uminho.anote2.aibench.ner.configuration.NERLexicalResourcesPreProssecingEnum;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.CorpusTextType;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.AbstractPropertyNode;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.IPropertiesPanel;
import pt.uminho.anote2.process.IE.re.IRelationsType;
import pt.uminho.anote2.relation.datastructures.PosTaggerEnem;
import pt.uminho.anote2.relation.datastructures.RelationsModelEnem;
import pt.uminho.anote2.relation.settings.RERelationSettingsNode;
import pt.uminho.anote2.workflow.datastructures.NERWorkflowProcessesAvailableEnum;
import pt.uminho.anote2.workflow.datastructures.REWorkflowProcessesAvailableEnum;
import pt.uminho.anote2.workflow.datastructures.WorkflowsEnum;
import pt.uminho.anote2.workflow.settings.query.pane.WorkflowQueryNERLexicalResourcesSettings;
import pt.uminho.anote2.workflow.settings.query.pane.WorkflowQueryRERelationSettings;
import pt.uminho.anote2.workflow.settings.utils.NERProcessChangeSettingBox;
import pt.uminho.anote2.workflow.settings.utils.REProcessChangeSettingBox;

public class WorkflowQuerySettingsNode  extends AbstractPropertyNode{
	

	private static final String treepath = "Workflow.Query";
	
	private Map<String, Object> defaultOptions;
	private boolean firstRun = true; 

	public WorkflowQuerySettingsNode() {
		super(treepath);
	}
	


	@Override
	public Map<String, Object> getDefaultProperties() {
		if(defaultOptions == null){
			defaultOptions = new HashMap<String, Object>();
			
			defaultOptions.put(WorkflowQueryDefaulSettings.CORPUS_NAME, "New Corpus");
			defaultOptions.put(WorkflowQueryDefaulSettings.CORPUS_TYPE, CorpusTextType.Abstract);
			defaultOptions.put(WorkflowQueryDefaulSettings.CORPUS_RETRIEVAL_PDF, false);
			
			defaultOptions.put(WorkflowQueryDefaulSettings.NER, NERWorkflowProcessesAvailableEnum.NERLexicalResources);
			defaultOptions.put(WorkflowQueryDefaulSettings.USE_PARTIAL_MATCH_WITH_DICTIONARIES, false);
			defaultOptions.put(WorkflowQueryDefaulSettings.CASE_SENSITIVE, false);
			defaultOptions.put(WorkflowQueryDefaulSettings.PRE_PROCESSING, NERLexicalResourcesPreProssecingEnum.POSTagging);
			defaultOptions.put(WorkflowQueryDefaulSettings.NORMALIZATION, true);
			defaultOptions.put(WorkflowQueryDefaulSettings.LEXICAL_RESOURCE_STOPWORDS_ID, 0);
			defaultOptions.put(WorkflowQueryDefaulSettings.RULES_RESOURCE_ID, 0);

			
			defaultOptions.put(WorkflowQueryDefaulSettings.RE, REWorkflowProcessesAvailableEnum.Relation);
			defaultOptions.put(WorkflowQueryDefaulSettings.POSTAGGER, PosTaggerEnem.LingPipe_POS);
			defaultOptions.put(WorkflowQueryDefaulSettings.MODEL, RelationsModelEnem.Binary_Verb_limitation);
			defaultOptions.put(WorkflowQueryDefaulSettings.VERB_FILTER, false);
			defaultOptions.put(WorkflowQueryDefaulSettings.VERB_FILTER_LEXICAL_WORDS_ID, 0);
			defaultOptions.put(WorkflowQueryDefaulSettings.VERB_ADDITION, false);
			defaultOptions.put(WorkflowQueryDefaulSettings.VERB_ADDITION_LEXICAL_WORDS_ID, 0);
			defaultOptions.put(WorkflowQueryDefaulSettings.BIOMEDICAL_VERB_MODEL, 0);
			defaultOptions.put(WorkflowQueryDefaulSettings.ADVANCED_VERB_ENTITES_MAX_DISTANCE, 0);
			defaultOptions.put(WorkflowQueryDefaulSettings.ADVANCED_ONLY_NEAREST_VERB_ENTITIES, false);
			defaultOptions.put(WorkflowQueryDefaulSettings.ADVANCED_ONLY_USE_ENTITY_TO_NEAREST_VERB, false);
			defaultOptions.put(WorkflowQueryDefaulSettings.ADVANCED_RELATIONS_TYPE, new TreeSet<IRelationsType>());
		}
		return defaultOptions;
	}


	@Override
	public IPropertiesPanel getPropertiesPanel() {

		if(firstRun)
		{
			firstRun = false;
			return new WorkflowQueryChangeSettingsGUI();
		}
		else
		{
			try {
				NERProcessChangeSettingBox.getInstance().registerChangeSettingGUI(WorkflowsEnum.QUERY, NERWorkflowProcessesAvailableEnum.NERLexicalResources, new WorkflowQueryNERLexicalResourcesSettings(properties,getDefaultProperties()));
				REProcessChangeSettingBox.getInstance().registerChangeSettingGUI(WorkflowsEnum.QUERY, REWorkflowProcessesAvailableEnum.Relation, new WorkflowQueryRERelationSettings(properties,getDefaultProperties()));
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				return new WorkflowQueryChangeSettingsGUI();
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				return new WorkflowQueryChangeSettingsGUI();
			}
			return new WorkflowQueryChangeSettingsGUI(properties, getDefaultProperties());
		}
	}


	@Override
	public Object revert(String propId, String propValue) {
		if( propId.matches(WorkflowQueryDefaulSettings.VERB_FILTER) ||
				propId.matches(WorkflowQueryDefaulSettings.VERB_ADDITION) ||
				propId.matches(WorkflowQueryDefaulSettings.ADVANCED_ONLY_NEAREST_VERB_ENTITIES)||
				propId.matches(WorkflowQueryDefaulSettings.ADVANCED_ONLY_USE_ENTITY_TO_NEAREST_VERB))
			return Boolean.parseBoolean(propValue);
		else if(propId.matches(WorkflowQueryDefaulSettings.POSTAGGER))
		{
			return PosTaggerEnem.convertStringInPosTaggerEnem(propValue);
		}
		else if(propId.matches(WorkflowQueryDefaulSettings.MODEL))
		{
			return RelationsModelEnem.convertStringToRelationsModelEnem(propValue);
		}
		else if(propId.matches(WorkflowQueryDefaulSettings.VERB_FILTER_LEXICAL_WORDS_ID) || 
				propId.matches(WorkflowQueryDefaulSettings.VERB_ADDITION_LEXICAL_WORDS_ID) ||
				propId.matches(WorkflowQueryDefaulSettings.BIOMEDICAL_VERB_MODEL)||
				propId.matches(WorkflowQueryDefaulSettings.RULES_RESOURCE_ID) ||
				propId.matches(WorkflowQueryDefaulSettings.ADVANCED_VERB_ENTITES_MAX_DISTANCE))
		{
			return Integer.valueOf(propValue);
		}
		else if(propId.matches(WorkflowQueryDefaulSettings.RE))
		{
			return REWorkflowProcessesAvailableEnum.convertStringToRelationProcess(propValue);
		}
		else if( propId.matches(WorkflowQueryDefaulSettings.USE_PARTIAL_MATCH_WITH_DICTIONARIES) ||
				propId.matches(WorkflowQueryDefaulSettings.CASE_SENSITIVE) 
				|| propId.matches(WorkflowQueryDefaulSettings.NORMALIZATION))
			return Boolean.parseBoolean(propValue);
		else if(propId.matches(WorkflowQueryDefaulSettings.PRE_PROCESSING))
		{
			return NERLexicalResourcesPreProssecingEnum.convertStringToNERLexicalResourcesPreProssecingEnum(propValue);
		}
		else if(propId.matches(WorkflowQueryDefaulSettings.LEXICAL_RESOURCE_STOPWORDS_ID))
		{
			return Integer.valueOf(propValue);
		}
		else if(propId.matches(WorkflowQueryDefaulSettings.NER))
		{
			return NERWorkflowProcessesAvailableEnum.convertStringtoNERPRocess(propValue);
		}
		else if(propId.matches(WorkflowQueryDefaulSettings.CORPUS_NAME))
		{
			return propValue;
		}
		else if(propId.matches(WorkflowQueryDefaulSettings.CORPUS_TYPE))
		{
			return CorpusTextType.convertStringToCorpusType(propValue);
		}
		else if(propId.matches(WorkflowQueryDefaulSettings.CORPUS_RETRIEVAL_PDF))
		{
			return Boolean.parseBoolean(propValue);
		}
		else if(propId.matches(WorkflowQueryDefaulSettings.ADVANCED_RELATIONS_TYPE))
		{
			return RERelationSettingsNode.getSetRelationsType(propValue);
		}
		return null;
	}


	@Override
	public String convert(String propId, Object propValue) {
		if(propId.matches(WorkflowQueryDefaulSettings.ADVANCED_RELATIONS_TYPE))
		{
			return  RERelationSettingsNode.convertShortedRelationTypeIntoString(propValue);
		}
		return propValue.toString();
	}
	
	@Override
	public Set<String> getNotExportableProperties() {
		Set<String> notAllowExportableProperties = new HashSet<String>(); 
		return notAllowExportableProperties;
	}


}
