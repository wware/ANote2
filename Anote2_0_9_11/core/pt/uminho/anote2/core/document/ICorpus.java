package pt.uminho.anote2.core.document;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.process.IE.IIEProcess;

public interface ICorpus {
	
	public int getID();
	public String getDescription();
	public void addDocument(IDocument doc) throws SQLException, DatabaseLoadDriverException;
	public Iterator<IDocument> iterator() throws SQLException, DatabaseLoadDriverException;
	public void registerProcess(IIEProcess ieProcess) throws SQLException, DatabaseLoadDriverException;
	public IDocumentSet getArticlesCorpus() throws SQLException, DatabaseLoadDriverException;
	public List<IIEProcess> getIProcesses() throws SQLException, DatabaseLoadDriverException;
	public List<IIEProcess> getIProcessesFilterByTypes(List<String> types) throws SQLException, DatabaseLoadDriverException;
	public int size();
	public Properties getProperties();

}
