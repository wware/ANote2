package pt.uminho.anote2.relation.core.relationModels;

import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.relation.configuration.IRERelationAdvancedConfiguration;
import pt.uminho.gate.process.IGatePosTagger;


public class RelationModelDefault {
	
	private ICorpus corpus;
	private IIEProcess ieProcess;
	private IGatePosTagger postagger;
	private IRERelationAdvancedConfiguration advanceConfiguration;
	
	public RelationModelDefault(ICorpus corpus,IIEProcess nerProcess,IGatePosTagger postagger,IRERelationAdvancedConfiguration advanedConfigurations)
	{
		this.corpus=corpus;
		this.ieProcess=nerProcess;
		this.postagger=postagger;
		this.setAdvanceConfiguration(advanedConfigurations);
	}



	public ICorpus getCorpus() {
		return corpus;
	}

	public void setCorpus(ICorpus corpus) {
		this.corpus = corpus;
	}

	public IIEProcess getNerProcess() {
		return ieProcess;
	}

	public void setNerProcess(IEProcess nerProcess) {
		this.ieProcess = nerProcess;
	}

	public IGatePosTagger getPostagger() {
		return postagger;
	}

	public void setPostagger(IGatePosTagger postagger) {
		this.postagger = postagger;
	}



	public IRERelationAdvancedConfiguration getAdvanceConfiguration() {
		return advanceConfiguration;
	}



	public void setAdvanceConfiguration(IRERelationAdvancedConfiguration advanceConfiguration) {
		this.advanceConfiguration = advanceConfiguration;
	}

}
