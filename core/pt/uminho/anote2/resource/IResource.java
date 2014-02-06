package pt.uminho.anote2.resource;

import java.util.Set;

import pt.uminho.anote2.core.database.IDatabase;

/**
 * This interface define a generic resource for Biomedical text mining
 * 
 * @author Hugo Costa
 *
 */
public interface IResource<T extends IResourceElement> {

	public boolean addElement(T element);
	public boolean updateElement(T element);
	public boolean removeElement(T element);
	public int addElementClass(String classElement);
	public boolean addResourceContent(String classContent);
	public boolean addResourceContent(int classContentID);
	public IResourceElementSet<IResourceElement> getResourceElements();
	public IResourceElementSet<IResourceElement> getTermByClass(String termClass);
	public IResourceElementSet<IResourceElement> getTermByClass(int termClassID);
	public IResourceElement getTerm(int termID);
	public IResourceElementSet<IResourceElement> getTermsByName(String name);
	public IResourceElement getFirstTermByName(String name);
	public Set<Integer> getClassContent();
	public String getInfo();
	public String getType();
	public int getId();
	public String getName();
	public IDatabase getDb();
}
