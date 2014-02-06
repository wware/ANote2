package pt.uminho.anote2.aibench.utils.operations;

import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation()
public class Anote2WikiOperation {
	
	@Port(name="wiki",direction=Direction.OUTPUT,order=1)
	public String findWiki() throws Exception
	{
		System.gc();
		Help.internetAccess(GlobalOptions.wikiGeneralLink);
		return null;
	}

}
