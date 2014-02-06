package pt.uminho.anote2.datastructures.documents;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IDocumentSet;

/**
 * Class that represent a Set of Documents
 * 
 * @author Hugo Costa
 *
 */
public class DocumentSet implements Serializable, IDocumentSet{
	
	
	private static final long serialVersionUID = 2L;
	
	/**
	 * Instance class that contain a information for each article
	 */
	private HashMap<Integer,IDocument> documents; //id -> document

	/**
	 * Constructors
	 */
	public DocumentSet() 
	{
		this.documents=new HashMap<Integer, IDocument>();
	}
	
	public DocumentSet(IDocumentSet docs) 
	{
		this.documents=new HashMap<Integer, IDocument>();
		for(IDocument doc:docs)
		{
			this.documents.put(doc.getID(), doc);
		}
	}
	
	/**
	 * Method that adds a document */
	public void addDocument(int id,IDocument document)
	{
		this.documents.put(id,document);
	}
	
	/**
	 * Method that removes a document */
	public void removeDocument(int id)
	{
		this.documents.remove(id);
	}
	
	/**
	 * Method that removes a document */
	public IDocument getDocument(int id)
	{
		return this.documents.get(id);
	}

	public Iterator<IDocument> iterator() {
		return this.documents.values().iterator();
	}

	public int size() {
		return this.documents.size();
	}


	public Map<Integer, IDocument> getAllDocuments() {
		if(documents==null)
		{
			return null;
		}
		return documents;
	}	
	
}
