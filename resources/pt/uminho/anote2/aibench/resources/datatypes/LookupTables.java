package pt.uminho.anote2.aibench.resources.datatypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.ListElements;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;

@Datatype(structure = Structure.LIST,namingMethod="getName")
public class LookupTables extends Observable implements Serializable{

	private static final long serialVersionUID = -1144760816074064549L;
	
	private Resources resources;
	private List<LookupTableAibench> looks;
	
	public LookupTables(Resources resources) {
		this.setResources(resources);
		looks=new ArrayList<LookupTableAibench>();
	}
	
	public String getName()
	{
		return "LookupTables";
	}
	
	public void notifyViewObservers(){
		this.setChanged();
		this.notifyObservers();
	}

	public void setResources(Resources resources) {
		this.resources = resources;
	}

	public Resources getResources() {
		return resources;
	}
	
	public void addLookupTable(LookupTableAibench look)
	{
		if(alreadyExist(look))
		{
			
		}
		else
		{
			this.looks.add(look);
			notifyViewObservers();
		}

	}
	
	@ListElements(modifiable=true)
	public List<LookupTableAibench> getQueries()
	{
		return this.looks;
	}
	
	
	public boolean alreadyExist(LookupTableAibench dic)
	{
		for(LookupTableAibench dicAibench:looks)
		{
			int compare = dicAibench.compareTo(dic);
			if(compare==0)
			{
				return true;
			}
		}
		return false;
	}

}
