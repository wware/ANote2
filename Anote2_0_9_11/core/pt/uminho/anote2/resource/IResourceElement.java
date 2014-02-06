package pt.uminho.anote2.resource;

import java.util.List;

import pt.uminho.anote2.core.annotation.IExternalID;
import pt.uminho.anote2.resource.ontologies.ITermBDID;

/**
 * Interface that implements a Resource element
 * 
 * @author Hugo Costa
 *
 */
public interface IResourceElement extends Cloneable,ITermBDID{
	
	public String getTerm(); // biological name
	public int getTermClassID(); // biological class ID on database
	public String getTermClass();
	public boolean isActive();
	public void changeTermClassID(int newClasse);
	/**
	 * Dictionary and Ontologies
	 * 
	 */
	public List<IExternalID> getExtenalIDs();
	public List<String> getSynonyms();

	/**
	 * Rules
	 * 
	 */
	public int getPriority();
	

}
