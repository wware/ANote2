package pt.uminho.anote2.aibench.resources.operations;

import pt.uminho.anote2.aibench.resources.datatypes.Resources;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation(enabled=true)
public class InitResources{
	
	@Port(name="Resources",direction=Direction.OUTPUT,order=1)
	public Resources getReferenceManager()
	{
		System.gc();
		Resources resources = new Resources();
		return resources;
	}	

}
