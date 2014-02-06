package pt.uminho.anote2.aibench.curator.operation;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation
public class OperationChangeCuratorMenuLinkManager {
	
	@Port(name="remove",direction=Direction.OUTPUT,order=1)
	public String about() throws Exception
	{
		System.gc();
		return null;
	}

}
