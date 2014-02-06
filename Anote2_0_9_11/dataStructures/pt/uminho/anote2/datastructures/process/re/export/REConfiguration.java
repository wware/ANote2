package pt.uminho.anote2.datastructures.process.re.export;

import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.datastructures.process.IEConfiguration;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.process.IE.re.IREConfiguration;

public class REConfiguration extends IEConfiguration implements IREConfiguration{

	private IIEProcess entityProcess;
	
	
	public REConfiguration(ICorpus corpus,IIEProcess entityProcess) {
		super(corpus);
		this.entityProcess = entityProcess;
	}

	@Override
	public IIEProcess getIEProcess() {
		return entityProcess;
	}

}
