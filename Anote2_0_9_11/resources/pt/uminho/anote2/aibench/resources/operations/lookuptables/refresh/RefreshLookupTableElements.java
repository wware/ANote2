package pt.uminho.anote2.aibench.resources.operations.lookuptables.refresh;

import java.util.List;

import pt.uminho.anote2.aibench.resources.datatypes.LookupTableAibench;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation
public class RefreshLookupTableElements {
	
	@Port(name="refresh",direction=Direction.INPUT,order=1)
	public void refreshLookupTables(int lookupTableID)
	{
		System.gc();
		List<ClipboardItem> list = Core.getInstance().getClipboard().getItemsByClass(LookupTableAibench.class);
		if(list!=null && list.size() > 0)
		{
			for(ClipboardItem item : list)
			{
				LookupTableAibench look = (LookupTableAibench) item.getUserData();
				if(look.getID() == lookupTableID)
				{
					look.notifyViewObservers();
					return;
				}
			}
		}
	}

}
