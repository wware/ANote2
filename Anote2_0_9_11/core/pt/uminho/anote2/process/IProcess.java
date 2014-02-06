	package pt.uminho.anote2.process;

import java.sql.SQLException;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
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
	
	public IDocumentSet getDocuments() throws SQLException, DatabaseLoadDriverException;
	public String getType();
	public int getID();
	public void stop();
}
