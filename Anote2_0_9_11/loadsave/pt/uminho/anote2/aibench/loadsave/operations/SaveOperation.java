package pt.uminho.anote2.aibench.loadsave.operations;

import java.io.File;
import java.util.ArrayList;

import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.configuration.ISaveModule;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation(name="Save Projects",description="Save all projects (Resources, Publication Manager and Corpora)",enabled=false)
public class SaveOperation {
	ArrayList<ISaveModule> arrayList;
	
	@Port(name="savemodule",direction=Direction.INPUT,order=1)
	public void quit(ArrayList<ISaveModule> prjs){
		arrayList = prjs;
	}

	@Port(name="file",direction=Direction.INPUT,order=2)
	public void file(File file) throws Exception
	{
		System.gc();
		if(arrayList.size()!=0)
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

}
