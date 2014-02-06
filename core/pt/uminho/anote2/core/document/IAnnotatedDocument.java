	package pt.uminho.anote2.core.document;

import java.util.List;

import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.process.IProcess;

/**
 * This interface implements a interface for representing as Annotated for a Document
 * 
 * @author Hugo Costa
 * 
 * @version 1.0 (16 junho 2009)
 *
 */
public interface IAnnotatedDocument extends IPublication{
	
	/**
	 * Method that return all entities annotation for document
	 * 
	 * @return List<IEntityAnnotation>
	 */
	public List<IEntityAnnotation> getEntitiesAnnotations();
	
	/**
	 * Method that return all event annotation for document
	 * 
	 * @return
	 */
	public List<IEventAnnotation> getEventAnnotations();
	
	/**
	 * 
	 */
	public IProcess getProcess();
	
	
	/**
	 * 
	 */
	public ICorpus getCorpus();
	
	
	
}
