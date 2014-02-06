package pt.uminho.anote2.aibench.utils.operations;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation
public class ChangeSettings {
	
	
	@Port(name="about",direction=Direction.OUTPUT,order=1)
	public String about() throws Exception
	{
		return null;
	}
}
