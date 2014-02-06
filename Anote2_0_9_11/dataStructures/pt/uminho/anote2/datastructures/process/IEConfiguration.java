package pt.uminho.anote2.datastructures.process;

import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.process.IE.IIEConfiguration;

public class IEConfiguration implements IIEConfiguration{

	private ICorpus corpus;
	
	public IEConfiguration(ICorpus corpus)
	{
		this.corpus = corpus;
	}
	
	public ICorpus getCorpus() {
		return corpus;
	}

}
