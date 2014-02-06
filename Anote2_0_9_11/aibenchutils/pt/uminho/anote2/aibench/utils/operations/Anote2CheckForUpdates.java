package pt.uminho.anote2.aibench.utils.operations;

import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation()
public class Anote2CheckForUpdates {
	
	@Port(name="updates",direction=Direction.OUTPUT,order=1)
	public String findUpdates() throws Exception
	{
		System.gc();
		Help.internetAccess(GlobalOptions.checkForUpdatesURL);
		return null;
	}

}
