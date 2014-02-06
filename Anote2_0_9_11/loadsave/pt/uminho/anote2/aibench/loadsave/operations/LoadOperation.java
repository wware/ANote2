package pt.uminho.anote2.aibench.loadsave.operations;

import java.io.File;

import org.jpedal.utils.sleep;

import pt.uminho.anote2.aibench.utils.file.FileHandling;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation()
public class LoadOperation {
	
	@Port(name="Load File",direction=Direction.INPUT,order=1)
	public void getResources(File file) throws Exception{
		System.gc();
		if(!file.exists())
		{
			Workbench.getInstance().warn("File not exist.");
		}
		else if(FileHandling.testSaveFile(file))
		{
			Workbench.getInstance().warn("File not supported.");
		}
		else
		{
			ParamSpec[] para = new ParamSpec[]{ 
					new ParamSpec("file",File.class,file,null)
				};
			
			for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations())
			{
				if (def.getID().equals("operations.saveload.loadcorpora"))
				{
					Workbench.getInstance().executeOperation(def,para);
				}
			}
			sleep.sleep(1000);
			for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations())
			{
				if (def.getID().equals("operations.saveload.loadresources"))
				{
					Workbench.getInstance().executeOperation(def,para);
				}
			}
			sleep.sleep(1000);
			for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations())
			{
				if (def.getID().equals("operations.saveload.loadpublicationmanager"))
				{
					Workbench.getInstance().executeOperation(def,para);
				}
			}
			new ShowMessagePopup("Project Load .");
		}
	}

}
