package pt.uminho.anote2.core.annotation;

/**
 * Interface that represents an External ID
 * 
 * @author Hugo Costa
 *
 */
public interface IExternalID {
	
	/**
	 * Return externalID
	 * 
	 * @return
	 */
	public String getExternalID();
	
	/**
	 * Return Source Databse ID 
	 * 
	 * @return
	 */
	public int getSourceID();
	
	/**
	 * Return Source Name ( Kegg, Chebi, Uniprot, ...)
	 * 
	 * @return
	 */
	public String getSource();

}
