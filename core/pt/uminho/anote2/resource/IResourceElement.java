package pt.uminho.anote2.resource;

/**
 * Interface that implements a Resource element
 * 
 * @author Hugo Costa
 *
 */
public interface IResourceElement extends Cloneable{
	//public String getElementType();
	public int getID();
	public String getTerm(); // biological name
	public int getTermClassID(); // biological class ID on data base
	public String getTermClass();
	/**
	 * Dictionary and Ontologies
	 * 
	 */
	public String getExtenalID();
	public int getExternalIDSourceID();
	/**
	 * Rules
	 * 
	 */
	public int getPriority();

}
