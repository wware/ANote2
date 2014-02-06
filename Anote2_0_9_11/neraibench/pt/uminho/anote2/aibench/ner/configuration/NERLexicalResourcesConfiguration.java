package pt.uminho.anote2.aibench.ner.configuration;

import java.util.Set;

import pt.uminho.anote2.aibench.ner.datastructures.ResourcesToNerAnote;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.datastructures.process.ner.NERConfiguration;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;

public class NERLexicalResourcesConfiguration extends NERConfiguration implements INERLexicalResourcesConfiguration{

	private NERLexicalResourcesPreProssecingEnum preProcessing;
	private ResourcesToNerAnote resourceToNER;
	private Set<String> posTags;
	private boolean caseSensitive;
	private ILexicalWords stopWords;
	private boolean normalized;
	private boolean usingOtherResourceInfoToImproveRuleAnnotstions;
	
	
	public NERLexicalResourcesConfiguration(ICorpus corpus,NERLexicalResourcesPreProssecingEnum preProcessing,ResourcesToNerAnote resourceToNER,
			Set<String> posTgas,ILexicalWords stopWords, boolean caseSensitive,boolean normalized,boolean usingOtherResourceInfoToImproveRuleAnnotstions) {
		super(corpus,"NERLexicalResources");
		this.preProcessing = preProcessing;
		this.resourceToNER = resourceToNER;
		this.posTags = posTgas;
		this.stopWords = stopWords;
		this.normalized = normalized;
		this.usingOtherResourceInfoToImproveRuleAnnotstions = usingOtherResourceInfoToImproveRuleAnnotstions;
	}
	

	@Override
	public NERLexicalResourcesPreProssecingEnum getPreProcessingOption() {
		return preProcessing;
	}

	@Override
	public ResourcesToNerAnote getResourceToNER() {
		return resourceToNER;
	}

	@Override
	public Set<String> getPOSTags() {
		return posTags;
	}

	@Override
	public ILexicalWords getStopWords() {
		return stopWords;
	}

	@Override
	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	@Override
	public boolean isNormalized() {
		return normalized;
	}

	@Override
	public void setNormalized(boolean newNormalizedOption) {
		normalized = newNormalizedOption;
	}


	@Override
	public boolean usingOtherResourceInfoToImproveRuleAnnotstions() {
		return usingOtherResourceInfoToImproveRuleAnnotstions;
	}

}
