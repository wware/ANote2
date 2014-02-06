package pt.uminho.anote2.aibench.ner.configuration;

import java.util.Set;

import pt.uminho.anote2.aibench.ner.datastructures.ResourcesToNerAnote;
import pt.uminho.anote2.process.IE.ner.INERConfiguration;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;

public interface INERLexicalResourcesConfiguration extends INERConfiguration{
	
	public NERLexicalResourcesPreProssecingEnum getPreProcessingOption();
	public ResourcesToNerAnote getResourceToNER();
	public Set<String> getPOSTags();
	public ILexicalWords getStopWords();
	public boolean isNormalized();
	public void setNormalized(boolean newNormalizedOption);
	public boolean isCaseSensitive();
	public boolean usingOtherResourceInfoToImproveRuleAnnotstions();
}
