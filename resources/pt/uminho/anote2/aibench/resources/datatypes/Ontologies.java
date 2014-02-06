package pt.uminho.anote2.aibench.resources.datatypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.ListElements;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;

@Datatype(structure = Structure.LIST,namingMethod="getName",removable=false)
public class Ontologies extends Observable{
	
	private Resources resources;
	private List<OntologyAibench> ontologies;
	
	public Ontologies(Resources resources)
	{
		this.resources=resources;
		this.ontologies=new ArrayList<OntologyAibench>();
	}
	
	public String getName()
	{
		return "Ontologies";
	}
	
	public void notifyViewObservers(){
		this.setChanged();
		this.notifyObservers();
	}


	public Resources getResources() {
		return resources;
	}

	public void addOntology(OntologyAibench ontology) {
		if(this.ontologies==null)
		{
			this.ontologies=new ArrayList<OntologyAibench>();
		}	
		if(alreadyExist(ontology))
		{
			
		}
		else
		{
			this.ontologies.add(ontology);
			notifyViewObservers();
		}
	}
	
	@ListElements(modifiable=true)
	public List<OntologyAibench> getQueries()
	{
		return this.ontologies;
	}



	public boolean alreadyExist(OntologyAibench dic)
	{
		for(OntologyAibench dicAibench:ontologies)
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
