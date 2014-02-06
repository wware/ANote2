package pt.uminho.anote2.workflow.settings.basic;

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
import pt.uminho.anote2.workflow.settings.basic.pane.WorkflowBasicNERLexicalResourcesSettings;
import pt.uminho.anote2.workflow.settings.basic.pane.WorkflowBasicRERelationSettings;
import pt.uminho.anote2.workflow.settings.utils.NERProcessChangeSettingBox;
import pt.uminho.anote2.workflow.settings.utils.REProcessChangeSettingBox;

public class WorkflowBasicsSettingsNode  extends AbstractPropertyNode{
	

	private static final String treepath = "Workflow.Basic";
	
	private Map<String, Object> defaultOptions;
	private boolean firstRun = true;

	public WorkflowBasicsSettingsNode() {
		super(treepath);

	}


	@Override
	public Map<String, Object> getDefaultProperties() {
		if(defaultOptions == null){
			defaultOptions = new HashMap<String, Object>();
			
			defaultOptions.put(WorkflowBasicsDefaulSettings.CORPUS_NAME, "New Corpus");
			defaultOptions.put(WorkflowBasicsDefaulSettings.CORPUS_TYPE, CorpusTextType.Abstract);
			defaultOptions.put(WorkflowBasicsDefaulSettings.CORPUS_RETRIEVAL_PDF, false);
			
			defaultOptions.put(WorkflowBasicsDefaulSettings.NER, NERWorkflowProcessesAvailableEnum.NERLexicalResources);
			defaultOptions.put(WorkflowBasicsDefaulSettings.USE_PARTIAL_MATCH_WITH_DICTIONARIES, false);
			defaultOptions.put(WorkflowBasicsDefaulSettings.CASE_SENSITIVE, false);
			defaultOptions.put(WorkflowBasicsDefaulSettings.PRE_PROCESSING, NERLexicalResourcesPreProssecingEnum.POSTagging);
			defaultOptions.put(WorkflowBasicsDefaulSettings.NORMALIZATION, true);
			defaultOptions.put(WorkflowBasicsDefaulSettings.LEXICAL_RESOURCE_STOPWORDS_ID, 0);
			defaultOptions.put(WorkflowBasicsDefaulSettings.RULES_RESOURCE_ID, 0);
			
			defaultOptions.put(WorkflowBasicsDefaulSettings.RE, REWorkflowProcessesAvailableEnum.Relation);
			defaultOptions.put(WorkflowBasicsDefaulSettings.POSTAGGER, PosTaggerEnem.LingPipe_POS);
			defaultOptions.put(WorkflowBasicsDefaulSettings.MODEL, RelationsModelEnem.Binary_Verb_limitation);
			defaultOptions.put(WorkflowBasicsDefaulSettings.VERB_FILTER, false);
			defaultOptions.put(WorkflowBasicsDefaulSettings.VERB_FILTER_LEXICAL_WORDS_ID, 0);
			defaultOptions.put(WorkflowBasicsDefaulSettings.VERB_ADDITION, false);
			defaultOptions.put(WorkflowBasicsDefaulSettings.VERB_ADDITION_LEXICAL_WORDS_ID, 0);
			defaultOptions.put(WorkflowBasicsDefaulSettings.BIOMEDICAL_VERB_MODEL, 0);
			defaultOptions.put(WorkflowBasicsDefaulSettings.ADVANCED_VERB_ENTITES_MAX_DISTANCE, 0);
			defaultOptions.put(WorkflowBasicsDefaulSettings.ADVANCED_ONLY_NEAREST_VERB_ENTITIES, false);
			defaultOptions.put(WorkflowBasicsDefaulSettings.ADVANCED_ONLY_USE_ENTITY_TO_NEAREST_VERB, false);
			defaultOptions.put(WorkflowBasicsDefaulSettings.ADVANCED_RELATIONS_TYPE, new TreeSet<IRelationsType>());
		}
		return defaultOptions;
	}


	@Override
	public IPropertiesPanel getPropertiesPanel() {

		if(firstRun)
		{
			firstRun = false;
			return new WorkflowBasicChangeSettingsGUI();
		}
		else
		{
			try {
				NERProcessChangeSettingBox.getInstance().registerChangeSettingGUI(WorkflowsEnum.BASIC, NERWorkflowProcessesAvailableEnum.NERLexicalResources, new WorkflowBasicNERLexicalResourcesSettings(properties,getDefaultProperties()));
				REProcessChangeSettingBox.getInstance().registerChangeSettingGUI(WorkflowsEnum.BASIC, REWorkflowProcessesAvailableEnum.Relation, new WorkflowBasicRERelationSettings(properties,getDefaultProperties()));
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				return new WorkflowBasicChangeSettingsGUI();
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				return new WorkflowBasicChangeSettingsGUI();
			}
			return new WorkflowBasicChangeSettingsGUI(properties,getDefaultProperties());
		}
	}


	@Override
	public Object revert(String propId, String propValue) {
		if( propId.matches(WorkflowBasicsDefaulSettings.VERB_FILTER) ||
				propId.matches(WorkflowBasicsDefaulSettings.VERB_ADDITION) ||
				propId.matches(WorkflowBasicsDefaulSettings.ADVANCED_ONLY_NEAREST_VERB_ENTITIES)||
				propId.matches(WorkflowBasicsDefaulSettings.ADVANCED_ONLY_USE_ENTITY_TO_NEAREST_VERB))
			return Boolean.parseBoolean(propValue);
		else if(propId.matches(WorkflowBasicsDefaulSettings.POSTAGGER))
		{
			return PosTaggerEnem.convertStringInPosTaggerEnem(propValue);
		}
		else if(propId.matches(WorkflowBasicsDefaulSettings.MODEL))
		{
			return RelationsModelEnem.convertStringToRelationsModelEnem(propValue);
		}
		else if(propId.matches(WorkflowBasicsDefaulSettings.VERB_FILTER_LEXICAL_WORDS_ID) || 
				propId.matches(WorkflowBasicsDefaulSettings.VERB_ADDITION_LEXICAL_WORDS_ID) ||
				propId.matches(WorkflowBasicsDefaulSettings.BIOMEDICAL_VERB_MODEL) ||
				propId.matches(WorkflowBasicsDefaulSettings.RULES_RESOURCE_ID)||
				propId.matches(WorkflowBasicsDefaulSettings.ADVANCED_VERB_ENTITES_MAX_DISTANCE))
		{
			return Integer.valueOf(propValue);
		}
		else if(propId.matches(WorkflowBasicsDefaulSettings.RE))
		{
			return REWorkflowProcessesAvailableEnum.convertStringToRelationProcess(propValue);
		}
		else if( propId.matches(WorkflowBasicsDefaulSettings.USE_PARTIAL_MATCH_WITH_DICTIONARIES) ||
				propId.matches(WorkflowBasicsDefaulSettings.CASE_SENSITIVE) 
				|| propId.matches(WorkflowBasicsDefaulSettings.NORMALIZATION))
			return Boolean.parseBoolean(propValue);
		else if(propId.matches(WorkflowBasicsDefaulSettings.PRE_PROCESSING))
		{
			return NERLexicalResourcesPreProssecingEnum.convertStringToNERLexicalResourcesPreProssecingEnum(propValue);
		}
		else if(propId.matches(WorkflowBasicsDefaulSettings.LEXICAL_RESOURCE_STOPWORDS_ID))
		{
			return Integer.valueOf(propValue);
		}
		else if(propId.matches(WorkflowBasicsDefaulSettings.NER))
		{
			return NERWorkflowProcessesAvailableEnum.convertStringtoNERPRocess(propValue);
		}
		else if(propId.matches(WorkflowBasicsDefaulSettings.CORPUS_NAME))
		{
			return propValue;
		}
		else if(propId.matches(WorkflowBasicsDefaulSettings.CORPUS_TYPE))
		{
			return CorpusTextType.convertStringToCorpusType(propValue);
		}
		else if(propId.matches(WorkflowBasicsDefaulSettings.CORPUS_RETRIEVAL_PDF))
		{
			return Boolean.parseBoolean(propValue);
		}
		else if(propId.matches(WorkflowBasicsDefaulSettings.ADVANCED_RELATIONS_TYPE))
		{
			return RERelationSettingsNode.getSetRelationsType(propValue);
		}
		return null;
	}


	@Override
	public String convert(String propId, Object propValue) {
		if(propId.matches(WorkflowBasicsDefaulSettings.ADVANCED_RELATIONS_TYPE))
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
