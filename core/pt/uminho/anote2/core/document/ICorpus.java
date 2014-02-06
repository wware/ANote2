package pt.uminho.anote2.core.document;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import pt.uminho.anote2.process.IProcess;
import pt.uminho.anote2.process.IE.IIEProcess;

public interface ICorpus {
	
	public int getID();
	public String getDescription();
	public void addDocument(IDocument doc);
	public Iterator<IDocument> iterator();
	public void registerProcess(IIEProcess ieProcess);
	public IDocumentSet getArticlesCorpus();
	public Set<IProcess> getIProcesses(String type);
	public int size();
	public Properties getProperties();

}
