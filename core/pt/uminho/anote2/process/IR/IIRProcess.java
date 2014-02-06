package pt.uminho.anote2.process.IR;

import pt.uminho.anote2.core.configuration.IProxy;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.process.IProcess;

/**
 * This interface define a Information Retrieval Process in TExt Mining
 * 
 * @author Hugo Costa
 *
 * @version 1.0 ( 16 Junho de 2009)
 */
public interface IIRProcess extends IProcess{
	
	/**
	 * Method that return a proxy configuration for IR Process
	 * 
	 * @return Proxy Configuration
	 */
	public IProxy getProxy();
	
	/**
	 * Method that return a database configuration for IR Process
	 * 
	 * @return Database Configuration
	 */
	public IDatabase getDatabase();
}
