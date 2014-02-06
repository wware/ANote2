package pt.uminho.anote2.aibench.ner.datastructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pt.uminho.anote2.datastructures.utils.GenericTriple;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;

public class ResourcesToNerAnote {
	
	private List<GenericTriple<IResource<IResourceElement>,Set<Integer>,Set<Integer>>> list;
	
	public ResourcesToNerAnote()
	{
		list = new ArrayList<GenericTriple<IResource<IResourceElement>,Set<Integer>,Set<Integer>>>();
	}
	
	public void add(IResource<IResourceElement> resElem,Set<Integer> classContent,Set<Integer> selectedClass)
	{
		GenericTriple<IResource<IResourceElement>,Set<Integer>,Set<Integer>> triple =
			new GenericTriple<IResource<IResourceElement>, Set<Integer>, Set<Integer>>(resElem,classContent,selectedClass);
		list.add(triple);
	}

	public List<GenericTriple<IResource<IResourceElement>, Set<Integer>, Set<Integer>>> getList() {
		return list;
	}
	
	

}
