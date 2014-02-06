package pt.uminho.anote2.aibench.resources.operations.ontologies;

import pt.uminho.anote2.aibench.resources.datatypes.Ontologies;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation()
public class CreateOntology {
	
	private Ontologies ontologies;
	private String name;

	@Port(name="dictionaries",direction=Direction.INPUT,order=1)
	public void setDicionaries(Ontologies lookups)
	{
		this.ontologies=lookups;
	}

	@Port(name="name",direction=Direction.INPUT,order=2)
	public void setName(String name)
	{
		this.name=name;
	}

	@Port(name="info",direction=Direction.INPUT,order=3)
	public void getInfo(String info)
	{
		ontologies.getResources().newResource(name, info,"ontology");
		ontologies.notifyViewObservers();
		new ShowMessagePopup("Ontology Created Done !!!");
	}


}
