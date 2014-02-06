package pt.uminho.anote2.aibench.resources.operations.lookuptables.refresh;

import java.util.List;

import pt.uminho.anote2.aibench.resources.datatypes.LookupTables;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation
public class RefreshLookupTables {

	
	@Port(name="refresh",direction=Direction.OUTPUT,order=1)
	public String refreshLookupTables()
	{
		System.gc();
		List<ClipboardItem> list = Core.getInstance().getClipboard().getItemsByClass(LookupTables.class);
		if(list!=null && list.size() > 0)
		{
			LookupTables look = (LookupTables) list.get(0).getUserData();	
			look.notifyViewObservers();
		}
		return null;
	}
	
}
