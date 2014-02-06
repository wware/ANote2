package pt.uminho.anote2.datastructures.utils.conf;

public class GlobalNames {
	
	/**
	 * NER Options
	 */
	public final static String stopWords= "Stop Words";
	public final static String normalization = "Normalization";
	public final static String stopWordsResourceID = "Stop Words Resource ID";
	public final static String allclasses = "All Classes";
	public final static String casesensitive = "Case Sensitive";
	public final static String nerAbnerModel = "model";
	public final static String nerChemistryTaggerChemistrylon = "ChemicalIon";
	public final static String nerChemistryTaggerChemistryElements = "ChemicalElement";
	public final static String nerChemistryTaggerChemistryCompounds = "ChemicalCompound";
	public final static String recooccurrenceModel = "Rel@tion Cooccurrence Model";
	public final static String nerpreProcessing = "NER Preprocessing :";
	public final static String nerpreProcessingTags = "NER Preprocessing (POS Tags) :";
	public final static String nerpreProcessingPosTagging = "POS Tagging"; 
	public final static String nerpreProcessingNo = "No";
	public final static String nerpreProcessingPosTaggingAndStopWords = nerpreProcessingPosTagging+" + "+stopWords;
	public final static String useOtherResourceInformationInRules = "Use Other Resource Information In Rules";



	
	/**
	 * Corpus Properties
	 */
	public final static String fullText = "Full Text";
	public final static String abstracts = "Abstract";
	public final static String abstractOrFullText = "Abstract or Full Text";
	public final static String textType = "Text Type";
	public final static String sourceQueries = "Source Queries";
	
	/**
	 * Native Process Names
	 */
	public final static String nerAnote = "@Note2 NER Lexical Resources";
	public final static String nerAbner = "Abner Tagger";
	public final static String nerChemistryTagger = "Chemistry Tagger";
	public final static String relation = "Rel@tion RE";
	public final static String relationCooccurrence = "Rel@tion RE Cooccurrence";
	public final static String mergeNER = "Merged Schemas";

	public final static String mergeNERSchema = "mergeNERSchema";
	public final static String NERSchema = "merged Schema";

	
	/**
	 * Processes Type
	 */
	public final static String ner = "NER";
	public final static String re = "RE";

	
	/**
	 * Relation
	 */
	
	public final static String entityBasedProcess = "Entity Based Process";
	public final static String taggerName = "Tagger";
	public final static String relationModel = "Relation model";
	public final static String verbFilter = "Verb Filter Resource (ID) ";
	public final static String verbAddition = "Verb Addition Resource (ID)";
	public final static String verbAdditionOnly = "Verb List Resource (ID)";
	
	/**
	 * Relation Properties
	 */
	public final static String relationPropertyLemma = "lemma";
	public final static String relationPropertyDirectionally = "directionally";
	public final static String relationPropertyPolarity = "polarity";

	
	/**
	 * Reports
	 */
	public final static String mergeDictionariesReportTitle = "Dictionary Merge Report";
	public final static String mergeLexicalWordsReportTitle = "Lexical Words Merge Report";
	public final static String mergeRulesReportTitle = "Rules Merge Report";
	public final static String mergeLookupTablesReportTitle = "Lookup Table Merge Report";
	public final static String updateDictionariesReportTitle = "Dictionary Update Report";
	public final static String updateLookupTableReportTitle = "Lookup Table Update Report";
	public final static String updateLexicalWordsReportTitle = "Lexical Words Update Report";
	public final static String nerprocessresultinfo = "NER Finishing "+GlobalTextInfoSmall.statistics;
	
	/**
	 * Entities
	 */
	public final static String protein = "Protein";
	public final static String compounds = "Compound";
	public final static String enzymes = "Enzyme";
	public final static String gene = "Gene";
	public final static String pathways = "Pathways";
	public final static String organism = "Organism";
	public final static String reactions = "Reactions";
	public final static String metabolicGenes = "Metabolic Genes";



	
	/**
	 * Dictionary Loaders Options
	 */
	
	public final static String organimsOption = "organism";
	
	
	public final static String entrezGeneHyphoteticalProtein = "Hypothetical protein";
	public final static String menuLinkOut = "Link Out";
	
	public static final String pmid = "pmid";
	public static final String createdby ="Created by";



}
