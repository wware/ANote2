package pt.uminho.anote2.workflow.settings.query;

public class WorkflowQueryDefaulSettings {
	
	public static final String CORPUS_NAME = "Workflow.Query.CorpusName";
	public static final String CORPUS_TYPE = "Workflow.Query.CorpusType";
	public static final String CORPUS_RETRIEVAL_PDF = "Workflow.Query.CorpusRetrievalPDF";
	
	public static final String NER = "Workflow.Query.NER";
	public static final String USE_PARTIAL_MATCH_WITH_DICTIONARIES = "Workflow.Query.NERLexicalResourcesPartialMatchWithDictionaries";
	public static final String CASE_SENSITIVE = "Workflow.Query.NERLexicalResourcesCaseSensitive";
	public static final String PRE_PROCESSING = "Workflow.Query.NERLexicalResourcesPreProcessing";
	public static final String NORMALIZATION = "Workflow.Query.NERLexicalResourcesNormalization";
	public static final String LEXICAL_RESOURCE_STOPWORDS_ID = "Workflow.Query.NERLexicalResourcesStopWords";
	public static final String RULES_RESOURCE_ID = "Workflow.Query.NERLexicalResourcesRuleID";

	
	public static final String RE = "Workflow.Query.RE";
	public static final String POSTAGGER = "Workflow.Query.RERelationPOSTagger";
	public static final String MODEL = "Workflow.Query.RERelationModel";
	public static final String VERB_FILTER = "Workflow.Query.RERelationVerbFilter";
	public static final String VERB_FILTER_LEXICAL_WORDS_ID = "Workflow.Query.RERelationVerbFilterLexicalWordID";
	public static final String VERB_ADDITION = "Workflow.Query.RERelationVerbAddition";
	public static final String VERB_ADDITION_LEXICAL_WORDS_ID = "Workflow.Query.RERelationVerbAdditionLexicalWordID";
	public static final String BIOMEDICAL_VERB_MODEL = "Workflow.Query.RERelationBiomedicalVerbModelLexicalWordID";
	public static final String ADVANCED_VERB_ENTITES_MAX_DISTANCE = "Workflow.Query.RERelationVerbEntitiesMaxDistance";
	public static final String ADVANCED_ONLY_NEAREST_VERB_ENTITIES = "Workflow.Query.RERelationOnlyNearestVErbEntities";
	public static final String ADVANCED_ONLY_USE_ENTITY_TO_NEAREST_VERB = "Workflow.Query.RERelationOnlyUSeEntityToNearestVerb";
	public static final String ADVANCED_RELATIONS_TYPE = "Workflow.Query.RERelationRelationsTypes";

}