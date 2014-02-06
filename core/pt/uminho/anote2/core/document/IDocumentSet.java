package pt.uminho.anote2.core.document;

import java.util.Map;



/**
 * This interface represent a Set of Documents
 * 
 * @author Hugo Costa
 * 
 * @version 1.0 (19 Junho 2009)
 *
 */
public interface IDocumentSet extends Iterable<IDocument>{
	
	/**
	 * Method that add a document
	 * 
	 * @param id - DocumentID
	 * @param document Document
	 */
	public void addDocument(int id,IDocument document);
	
	/**
	 * Method that remove a Document given the id
	 * 
	 * @param id
	 */
	public void removeDocument(int id);
	
	/**
	 * Method that return a Document whit id
	 * 
	 * @param id
	 * @return
	 */
	public IDocument getDocument(int id);
	
	public Map<Integer,IDocument> getAllDocuments();
	
	public int size();
		
}
