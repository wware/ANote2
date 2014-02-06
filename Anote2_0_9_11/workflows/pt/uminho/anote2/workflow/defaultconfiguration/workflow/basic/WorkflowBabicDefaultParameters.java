package pt.uminho.anote2.workflow.defaultconfiguration.workflow.basic;

import java.util.HashSet;
import java.util.Set;

import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.datastructures.nlptools.PartOfSpeechLabels;
import pt.uminho.anote2.process.IE.IIEProcess;

public class WorkflowBabicDefaultParameters {

//	public static final boolean caseSensitive = false;
	public static final ICorpus corpus = null;
//	public static final NERLexicalResourcesPreProssecingEnum preProcessing = NERLexicalResourcesPreProssecingEnum.POSTagging;
	public static final Set<String> postags = getDefaultPOStags();
//	public static final ILexicalWords stopwords = null;
//	public static final boolean normalization = true;
//	public static final ILexicalWords verbaddition = null;
//	public static final ILexicalWords verbfilter = null ;
//	public static final RelationsModelEnem relationModel = RelationsModelEnem.Binary_Verb_limitation;
//	public static final PosTaggerEnem posTagger = PosTaggerEnem.LingPipe_POS;
	public static final IIEProcess iieprocess = null;
//	public static boolean usingOtherResourceInfoToImproveRuleAnnotstions = true;
	
	public static Set<String> getDefaultPOStags()
	{
		Set<String> posTags = new HashSet<String>();
		for(PartOfSpeechLabels label:PartOfSpeechLabels.values())
		{
			if(label.getEnableDefaultValue())
			{
				posTags.add(label.value());
			}
		}
		return posTags;
	}
}
