package pt.uminho.anote2.process.IE;

import java.util.Properties;

import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.process.IProcess;

public interface IIEProcess extends IProcess{
	
	public String getDescription();
	public Properties getProperties();
	public ICorpus getCorpus();

}
