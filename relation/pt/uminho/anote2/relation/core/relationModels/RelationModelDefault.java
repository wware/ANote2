package pt.uminho.anote2.relation.core.relationModels;

import process.IGatePosTagger;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.process.IE.INERProcess;


public class RelationModelDefault {
	
	private ICorpus corpus;
	private INERProcess nerProcess;
	private IGatePosTagger postagger;
	
	public RelationModelDefault(ICorpus corpus,INERProcess nerprocess,IGatePosTagger postagger)
	{
		this.corpus=corpus;
		this.nerProcess=nerprocess;
		this.postagger=postagger;
	}



	public ICorpus getCorpus() {
		return corpus;
	}

	public void setCorpus(ICorpus corpus) {
		this.corpus = corpus;
	}

	public INERProcess getNerProcess() {
		return nerProcess;
	}

	public void setNerProcess(INERProcess nerProcess) {
		this.nerProcess = nerProcess;
	}

	public IGatePosTagger getPostagger() {
		return postagger;
	}

	public void setPostagger(IGatePosTagger postagger) {
		this.postagger = postagger;
	}

}
