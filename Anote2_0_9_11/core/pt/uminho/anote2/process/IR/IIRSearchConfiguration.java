package pt.uminho.anote2.process.IR;

import java.util.Properties;

/**
 * Class that implemets a Information Retrieval Search Configuration
 * 
 * @author Hugo Costa
 *
 */
public interface IIRSearchConfiguration {
	
	/**
	 * Return keywords
	 * 
	 * @return
	 */
	public String getKeywords();
	
	/**
	 * Return Organism
	 * @return
	 */
	public String getOrganism();
	
	/**
	 * Return Configuration properties
	 * 
	 * @return
	 */
	public Properties getProperties();
}
