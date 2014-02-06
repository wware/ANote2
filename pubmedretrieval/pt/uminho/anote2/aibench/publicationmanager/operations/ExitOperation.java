package pt.uminho.anote2.aibench.publicationmanager.operations;

import java.util.List;

import pt.uminho.anote2.aibench.publicationmanager.dataStructures.PubMedSearchExtension;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation()
public class ExitOperation {
	
	@Port(name="Exit",direction=Direction.OUTPUT,order=1)
	public String getReferenceManager(){
		
		List<ClipboardItem> lisRm = Core.getInstance().getClipboard().getItemsByClass(PublicationManager.class);
		if(lisRm.size()==1)
		{
			if(stopQuestion())
			{
				Workbench.getInstance().elementRemoved(lisRm.get(0));
				Core.getInstance().getClipboard().removeClipboardItem(lisRm.get(0));
				new ShowMessagePopup("Plugin Publication Manager Disable");
			}
		}
		return null;
	}
	
	private boolean stopQuestion(){
		Object[] options = new String[]{"Yes", "No"};
		int opt = PubMedSearchExtension.showOptionPane("Close Plugin", "", options);
		switch (opt) {
		case 0:
			return true;
		default:
			return false;
		}
	}
}
