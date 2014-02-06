package pt.uminho.anote2.relation.cooccurrence.configuration;

import pt.uminho.anote2.process.IE.re.IREConfiguration;
import pt.uminho.anote2.relation.cooccurrence.core.RECooccurrenceModelEnum;

public interface IRECooccurrenceConfiguration extends IREConfiguration{
	
	public RECooccurrenceModelEnum getCooccurrenceModel();

}
