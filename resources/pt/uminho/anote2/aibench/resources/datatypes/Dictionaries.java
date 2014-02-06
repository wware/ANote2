package pt.uminho.anote2.aibench.resources.datatypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.ListElements;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;

@Datatype(structure = Structure.LIST,namingMethod="getName",removable=false)
public class Dictionaries extends Observable implements Serializable{

	private static final long serialVersionUID = 5558615946630708439L;
	private Resources resources;
	private List<DictionaryAibench> dics;
	
	public Dictionaries(Resources resources)
	{
		this.setResources(resources);
		this.dics=new ArrayList<DictionaryAibench>();
	}
	
	public void notifyViewObservers(){
		this.setChanged();
		this.notifyObservers();
	}
	
	public String getName()
	{
		return "Dictionaries";
	}

	public void setResources(Resources resources) {
		this.resources = resources;
	}

	public Resources getResources() {
		return resources;
	}
	
	public void addDictionary(DictionaryAibench dic)
	{	
		if(alreadyExist(dic))
		{
			
		}
		else
		{
			this.dics.add(dic);
			notifyViewObservers();
		}

	}
	
	public boolean alreadyExist(DictionaryAibench dic)
	{
		for(DictionaryAibench dicAibench:dics)
		{
			int compare = dicAibench.compareTo(dic);
			if(compare==0)
			{
				return true;
			}
		}
		return false;
	}
	
	@ListElements(modifiable=true)
	public List<DictionaryAibench> getQueries()
	{
		return this.dics;
	}
	
}
