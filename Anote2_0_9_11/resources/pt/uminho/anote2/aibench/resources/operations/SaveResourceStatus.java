package pt.uminho.anote2.aibench.resources.operations;

import java.io.File;

import pt.uminho.anote2.aibench.resources.datatypes.Resources;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation(name="saveresources",description="Save Resources",enabled=true)
public class SaveResourceStatus {
	
	private Resources pm;
	
	@Port(name="Resources",direction=Direction.INPUT,order=1)
	public void setPublicationManager(Resources pm){
		this.pm=pm;	
	}
	
	@Port(name="File",direction=Direction.INPUT,order=2)
	public void saveFile(File file) throws Exception{
		System.gc();
		if(pm.saveFile(file))
		{
			new ShowMessagePopup("Resources Status Save");
		}
		else
		{
			new ShowMessagePopup("Resources Status Save Cancel .");
		}
	}
}
