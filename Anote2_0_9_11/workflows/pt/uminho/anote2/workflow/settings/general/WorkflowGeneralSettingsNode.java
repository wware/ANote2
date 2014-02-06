package pt.uminho.anote2.workflow.settings.general;

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
import pt.uminho.anote2.workflow.settings.general.pane.WorkflowGeneralNERLexicalResourcesSettings;
import pt.uminho.anote2.workflow.settings.general.pane.WorkflowGeneralRERelationSettings;
import pt.uminho.anote2.workflow.settings.utils.NERProcessChangeSettingBox;
import pt.uminho.anote2.workflow.settings.utils.REProcessChangeSettingBox;

public class WorkflowGeneralSettingsNode  extends AbstractPropertyNode{
	

	private static final String treepath = "Workflow.General";
	
	private Map<String, Object> defaultOptions;
	private boolean firstRun = true;
	
	public WorkflowGeneralSettingsNode() {
		super(treepath);
		
	}


	@Override
	public Map<String, Object> getDefaultProperties() {
		if(defaultOptions == null){
			defaultOptions = new HashMap<String, Object>();
			
			defaultOptions.put(WorkflowGeneralDefaulSettings.CORPUS_NAME, "New Corpus");
			defaultOptions.put(WorkflowGeneralDefaulSettings.CORPUS_TYPE, CorpusTextType.Abstract);
			defaultOptions.put(WorkflowGeneralDefaulSettings.CORPUS_RETRIEVAL_PDF, false);
			
			defaultOptions.put(WorkflowGeneralDefaulSettings.NER, NERWorkflowProcessesAvailableEnum.NERLexicalResources);
			defaultOptions.put(WorkflowGeneralDefaulSettings.USE_PARTIAL_MATCH_WITH_DICTIONARIES, false);
			defaultOptions.put(WorkflowGeneralDefaulSettings.CASE_SENSITIVE, false);
			defaultOptions.put(WorkflowGeneralDefaulSettings.PRE_PROCESSING, NERLexicalResourcesPreProssecingEnum.POSTagging);
			defaultOptions.put(WorkflowGeneralDefaulSettings.NORMALIZATION, true);
			defaultOptions.put(WorkflowGeneralDefaulSettings.LEXICAL_RESOURCE_STOPWORDS_ID, 0);
			defaultOptions.put(WorkflowGeneralDefaulSettings.RULES_RESOURCE_ID, 0);

			
			defaultOptions.put(WorkflowGeneralDefaulSettings.RE, REWorkflowProcessesAvailableEnum.Relation);
			defaultOptions.put(WorkflowGeneralDefaulSettings.POSTAGGER, PosTaggerEnem.LingPipe_POS);
			defaultOptions.put(WorkflowGeneralDefaulSettings.MODEL, RelationsModelEnem.Binary_Verb_limitation);
			defaultOptions.put(WorkflowGeneralDefaulSettings.VERB_FILTER, false);
			defaultOptions.put(WorkflowGeneralDefaulSettings.VERB_FILTER_LEXICAL_WORDS_ID, 0);
			defaultOptions.put(WorkflowGeneralDefaulSettings.VERB_ADDITION, false);
			defaultOptions.put(WorkflowGeneralDefaulSettings.VERB_ADDITION_LEXICAL_WORDS_ID, 0);
			defaultOptions.put(WorkflowGeneralDefaulSettings.BIOMEDICAL_VERB_MODEL, 0);
			defaultOptions.put(WorkflowGeneralDefaulSettings.ADVANCED_VERB_ENTITES_MAX_DISTANCE, 0);
			defaultOptions.put(WorkflowGeneralDefaulSettings.ADVANCED_ONLY_NEAREST_VERB_ENTITIES, false);
			defaultOptions.put(WorkflowGeneralDefaulSettings.ADVANCED_ONLY_USE_ENTITY_TO_NEAREST_VERB, false);
			defaultOptions.put(WorkflowGeneralDefaulSettings.ADVANCED_RELATIONS_TYPE, new TreeSet<IRelationsType>());
		}
		return defaultOptions;
	}


	@Override
	public IPropertiesPanel getPropertiesPanel() {

		if(firstRun)
		{
			firstRun = false;
			return new WorkflowGeneralChangeSettingsGUI();
		}
		else
		{
			try {
				NERProcessChangeSettingBox.getInstance().registerChangeSettingGUI(WorkflowsEnum.GENERAL, NERWorkflowProcessesAvailableEnum.NERLexicalResources, new WorkflowGeneralNERLexicalResourcesSettings(properties,getDefaultProperties()));
				REProcessChangeSettingBox.getInstance().registerChangeSettingGUI(WorkflowsEnum.GENERAL, REWorkflowProcessesAvailableEnum.Relation, new WorkflowGeneralRERelationSettings(properties,getDefaultProperties()));
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				return new WorkflowGeneralChangeSettingsGUI();
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				return new WorkflowGeneralChangeSettingsGUI();
			}
			return new WorkflowGeneralChangeSettingsGUI(properties, getDefaultProperties());
		}
	}


	@Override
	public Object revert(String propId, String propValue) {
		if( propId.matches(WorkflowGeneralDefaulSettings.VERB_FILTER) ||
				propId.matches(WorkflowGeneralDefaulSettings.VERB_ADDITION) ||
				propId.matches(WorkflowGeneralDefaulSettings.ADVANCED_ONLY_NEAREST_VERB_ENTITIES)||
				propId.matches(WorkflowGeneralDefaulSettings.ADVANCED_ONLY_USE_ENTITY_TO_NEAREST_VERB))
			return Boolean.parseBoolean(propValue);
		else if(propId.matches(WorkflowGeneralDefaulSettings.POSTAGGER))
		{
			return PosTaggerEnem.convertStringInPosTaggerEnem(propValue);
		}
		else if(propId.matches(WorkflowGeneralDefaulSettings.MODEL))
		{
			return RelationsModelEnem.convertStringToRelationsModelEnem(propValue);
		}
		else if(propId.matches(WorkflowGeneralDefaulSettings.VERB_FILTER_LEXICAL_WORDS_ID) || 
				propId.matches(WorkflowGeneralDefaulSettings.VERB_ADDITION_LEXICAL_WORDS_ID) ||
				propId.matches(WorkflowGeneralDefaulSettings.BIOMEDICAL_VERB_MODEL) ||
				propId.matches(WorkflowGeneralDefaulSettings.RULES_RESOURCE_ID) ||
				propId.matches(WorkflowGeneralDefaulSettings.ADVANCED_VERB_ENTITES_MAX_DISTANCE))
		{
			return Integer.valueOf(propValue);
		}
		else if(propId.matches(WorkflowGeneralDefaulSettings.RE))
		{
			return REWorkflowProcessesAvailableEnum.convertStringToRelationProcess(propValue);
		}
		else if( propId.matches(WorkflowGeneralDefaulSettings.USE_PARTIAL_MATCH_WITH_DICTIONARIES) ||
				propId.matches(WorkflowGeneralDefaulSettings.CASE_SENSITIVE) 
				|| propId.matches(WorkflowGeneralDefaulSettings.NORMALIZATION))
			return Boolean.parseBoolean(propValue);
		else if(propId.matches(WorkflowGeneralDefaulSettings.PRE_PROCESSING))
		{
			return NERLexicalResourcesPreProssecingEnum.convertStringToNERLexicalResourcesPreProssecingEnum(propValue);
		}
		else if(propId.matches(WorkflowGeneralDefaulSettings.LEXICAL_RESOURCE_STOPWORDS_ID))
		{
			return Integer.valueOf(propValue);
		}
		else if(propId.matches(WorkflowGeneralDefaulSettings.NER))
		{
			return NERWorkflowProcessesAvailableEnum.convertStringtoNERPRocess(propValue);
		}
		else if(propId.matches(WorkflowGeneralDefaulSettings.CORPUS_NAME))
		{
			return propValue;
		}
		else if(propId.matches(WorkflowGeneralDefaulSettings.CORPUS_TYPE))
		{
			return CorpusTextType.convertStringToCorpusType(propValue);
		}
		else if(propId.matches(WorkflowGeneralDefaulSettings.CORPUS_RETRIEVAL_PDF))
		{
			return Boolean.parseBoolean(propValue);
		}
		else if(propId.matches(WorkflowGeneralDefaulSettings.ADVANCED_RELATIONS_TYPE))
		{
			return RERelationSettingsNode.getSetRelationsType(propValue);
		}
		return null;
	}


	@Override
	public String convert(String propId, Object propValue) {
		if(propId.matches(WorkflowGeneralDefaulSettings.ADVANCED_RELATIONS_TYPE))
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
