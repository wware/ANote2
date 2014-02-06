package pt.uminho.anote2.datastructures.resources;

import java.util.Set;
import java.util.TreeSet;

import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;

public class ResourceElementSet<T extends IResourceElement> implements IResourceElementSet<T>{

	private Set<T> resourceElements;

	public ResourceElementSet()
	{
		this.resourceElements=new TreeSet<T>();
	}
	
	public boolean addElementResource(T element) {
		if(resourceElements.contains(element))
		{
			return false;
		}
		else
		{
			resourceElements.add(element);
			return true;
		}
	}

	@Override
	public Set<T> getElements() {
		return  resourceElements;
	}

	@Override
	public boolean removeElementResource(T element) {
		if(this.resourceElements.contains(element))
		{
			this.resourceElements.remove(element);
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean updateElementResource(T arg0) {
		/**
		 * Este metodo e para implementar usando base de dados
		 * Não s enquadra muito bem 
		 */
		return false;
	}

	
	public int size() {
		return resourceElements.size();
	}
	
	

}
