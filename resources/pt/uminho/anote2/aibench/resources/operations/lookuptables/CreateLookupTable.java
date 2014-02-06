package pt.uminho.anote2.aibench.resources.operations.lookuptables;

import pt.uminho.anote2.aibench.resources.datatypes.LookupTables;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation(enabled=true)
public class CreateLookupTable {
	
	private LookupTables lookups;
	private String name;

	@Port(name="dictionaries",direction=Direction.INPUT,order=1)
	public void setDicionaries(LookupTables lookups)
	{
		this.lookups=lookups;
	}

	@Port(name="name",direction=Direction.INPUT,order=2)
	public void setName(String name)
	{
		this.name=name;
	}

	@Port(name="info",direction=Direction.INPUT,order=3)
	public void getInfo(String info)
	{
		lookups.getResources().newResource(name, info,"lookuptable");
		lookups.notifyViewObservers();
		new ShowMessagePopup("Create a Lookup Table !!!");
	}


}
