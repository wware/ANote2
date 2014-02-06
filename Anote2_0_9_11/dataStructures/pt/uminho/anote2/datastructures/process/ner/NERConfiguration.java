package pt.uminho.anote2.datastructures.process.ner;

import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.datastructures.process.IEConfiguration;
import pt.uminho.anote2.process.IE.ner.INERConfiguration;

public class NERConfiguration extends IEConfiguration implements INERConfiguration{

	private String processName;
	
	public NERConfiguration(ICorpus corpus,String processName) {
		super(corpus);
		this.processName = processName;
	}

	@Override
	public String getName() {
		return processName;
	}
	
}
