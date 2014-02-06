package pt.uminho.anote2.resource.dictionary;

import java.sql.SQLException;
import java.util.Set;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceMergeReport;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;

/**
 * Interface 
 * 
 * @author Hugo Costa
 *
 */
public interface IDictionary extends IResource<IResourceElement>{
	public IResourceMergeReport merge(IDictionary idSncDic) throws DatabaseLoadDriverException, SQLException;
	public IResourceMergeReport merge(IDictionary idSncDic,Set<Integer> classIDs) throws DatabaseLoadDriverException, SQLException;
	public IResourceElementSet<IResourceElement> getTermSynomns(int termID) throws DatabaseLoadDriverException, SQLException;
	public boolean addSynomyn(IResourceElement elem);
	public int addSource(String source) throws DatabaseLoadDriverException, SQLException;
	public IResourceElement getTerm(int termID) throws DatabaseLoadDriverException, SQLException;
	public IResourceElementSet<IResourceElement> getAllTermsAndSynonyms() throws DatabaseLoadDriverException, SQLException;
	public IResourceElementSet<IResourceElement> getAllTermByClass(String termClass) throws DatabaseLoadDriverException, SQLException;
	public IResourceElementSet<IResourceElement> getAllTermByClass(int termClassID) throws DatabaseLoadDriverException, SQLException;
	public void removeElementAllSynonyms(int termID) throws DatabaseLoadDriverException, SQLException;;
	public void removeElementSynonyms(int termID,String elem) throws DatabaseLoadDriverException, SQLException;
	
	
}
