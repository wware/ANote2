package pt.uminho.anote2.process.IE;

import java.util.Properties;

import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.process.IProcess;

/**
 * Interface that represents a Information Extration Process in Biomedical Text Miming
 * 
 * @author Hugo Costa
 *
 */
public interface IIEProcess extends IProcess{
	
	/**
	 * Return Process Name
	 * 
	 * @return
	 */
	public String getName();
	
	/**
	 * Set Process NAme
	 * 
	 * @param newNAme
	 */
	public void setName(String newNAme);
	
	/**
	 * Return Properties
	 * 
	 * @return
	 */
	public Properties getProperties();
	
	/**
	 * Return {@link ICorpus} associated to Process
	 * 
	 * @return
	 */
	public ICorpus getCorpus();

}
