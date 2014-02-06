package pt.uminho.anote2.resource;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import pt.uminho.anote2.core.annotation.IExternalID;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;

/**
 * This interface define a generic resource for Biomedical text mining
 * 
 * @author Hugo Costa
 *
 */
public interface IResource<T extends IResourceElement>{

	
	public boolean addElement(T element) throws DatabaseLoadDriverException, SQLException;
	public void updateElement(T element) throws DatabaseLoadDriverException, SQLException;;
	public void removeElement(T element) throws DatabaseLoadDriverException, SQLException;;
	public void inactiveElement(T element) throws DatabaseLoadDriverException, SQLException;;
	public void inactiveElementsByClassID(int classID) throws DatabaseLoadDriverException, SQLException;;
	public int addElementClass(String classElement) throws DatabaseLoadDriverException, SQLException;;
	public boolean addResourceContent(String classContent) throws DatabaseLoadDriverException, SQLException;
	public boolean addResourceContent(int classContentID) throws DatabaseLoadDriverException, SQLException;
	public boolean addExternalID(int termID,String externalID,int sourceID) throws DatabaseLoadDriverException, SQLException;
	public IResourceElementSet<IResourceElement> getResourceElements() throws DatabaseLoadDriverException, SQLException;
	public IResourceElementSet<IResourceElement> getTermByClass(String termClass) throws DatabaseLoadDriverException, SQLException;
	public IResourceElementSet<IResourceElement> getTermByClass(int termClassID) throws DatabaseLoadDriverException, SQLException;
	public IResourceElement getTerm(int termID) throws DatabaseLoadDriverException, SQLException;
	public IResourceElementSet<IResourceElement> getTermsByName(String name) throws DatabaseLoadDriverException, SQLException;;
	public IResourceElement getFirstTermByName(String name) throws DatabaseLoadDriverException, SQLException;;
	public List<IExternalID> getexternalIDandSorceIDandSource(int termID) throws DatabaseLoadDriverException, SQLException; ;
	public Set<Integer> getClassContent() throws DatabaseLoadDriverException, SQLException;
	public String getInfo();
	public String getType();
	public int getID();
	public boolean isActive();
	public String getName();
	public void loadAllTermsFromDatabaseToMemory() throws DatabaseLoadDriverException, SQLException;
	public void deleteTermsInMemory();
}
