package pt.uminho.anote2.relation.cooccurrence.configuration;

import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.datastructures.process.re.export.REConfiguration;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.relation.cooccurrence.core.RECooccurrenceModelEnum;

public class RECooccurrenceConfiguration extends REConfiguration implements IRECooccurrenceConfiguration{

	private RECooccurrenceModelEnum reCoocurrenceModel;
	
	public RECooccurrenceConfiguration(ICorpus corpus, IIEProcess entityProcess,RECooccurrenceModelEnum reCoocurrenceModel) {
		super(corpus, entityProcess);
		this.reCoocurrenceModel = reCoocurrenceModel;
	}

	@Override
	public RECooccurrenceModelEnum getCooccurrenceModel() {
		return reCoocurrenceModel;
	}

}
