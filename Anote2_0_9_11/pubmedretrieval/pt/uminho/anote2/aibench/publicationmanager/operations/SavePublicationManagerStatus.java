package pt.uminho.anote2.aibench.publicationmanager.operations;

import java.io.File;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation(name="savepublicationmanagerstatus",description="Save Publication Manager Status",enabled=true)
public class SavePublicationManagerStatus {

	private PublicationManager pm;
	
	@Port(name="publication Manager",direction=Direction.INPUT,order=1)
	public void setPublicationManager(PublicationManager pm){
		this.pm=pm;	
	}
	
	@Port(name="File",direction=Direction.INPUT,order=2)
	public void saveFile(File file) throws Exception{
		System.gc();
		if(pm.saveFile(file))
		{
			new ShowMessagePopup("Publication Manager Status Save");
		}
		else
		{
			new ShowMessagePopup("Publication Manager Status Save Cancel .");
		}
	}
}
