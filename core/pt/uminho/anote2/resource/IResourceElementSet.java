package pt.uminho.anote2.resource;

import java.util.Set;

public interface IResourceElementSet<T extends IResourceElement>{
	public Set<T> getElements();
	public boolean addElementResource(T element);
	public boolean updateElementResource(T element);
	public boolean removeElementResource(T element);
	public int size();

}
