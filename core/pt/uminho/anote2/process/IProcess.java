	package pt.uminho.anote2.process;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.document.IDocumentSet;

/**
 * This interface define each Biomedical text mining operation that can change or create a Corpus
 * 
 * 
 * @author Hugo Costa
 *
 * @version 1.0 (16 de Junho 2009)
 */

public interface IProcess {
	
	public IDocumentSet getDocuments();
	public String getType();
	public IDatabase getDB();
	public int getID();
	//public List<IDocument> processDocuments(List<IDocument> m);
}
