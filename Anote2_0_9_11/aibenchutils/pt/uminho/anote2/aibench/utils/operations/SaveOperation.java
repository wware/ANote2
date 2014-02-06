package pt.uminho.anote2.aibench.utils.operations;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.configuration.ISaveModule;
import es.uvigo.ei.aibench.Launcher;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation(name="Save Projects Operation an Exit Option",enabled=true)
public class SaveOperation {
	ArrayList<ISaveModule> arrayList;
	
	@Port(name="savemodule",direction=Direction.INPUT,order=1)
	public void quit(ArrayList<ISaveModule> prjs) throws IOException{
		arrayList = prjs;
	}

	@Port(name="file",direction=Direction.INPUT,order=2)
	public void file(File file) throws Exception
	{
		if(arrayList.size()!=0 && file!=null)
		{
			for(ISaveModule p:arrayList){
				p.saveFile(file);
			}
			new ShowMessagePopup("Projects Save.");
		}
		else
		{
			new ShowMessagePopup("No Projects to Save.");
		}
	}
	
	@Port(name="file",direction=Direction.INPUT,order=2)
	public void file(boolean shutdown)
	{
		if(shutdown)
		{
			new ShowMessagePopup("@Note2 was Shut Down.");
			new Thread(){ //killer thread
				public void run(){						
					Launcher.getPluginEngine().shutdown();			
					System.exit(0);	
				}
			}.start();
		}
	}
	
	

}
