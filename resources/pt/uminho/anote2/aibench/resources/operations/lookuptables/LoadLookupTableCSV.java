package pt.uminho.anote2.aibench.resources.operations.lookuptables;

import java.io.File;

import pt.uminho.anote2.aibench.resources.datatypes.LookupTableAibench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation(description="Import term from csv file to Lookup Table")
public class LoadLookupTableCSV {
	
	private LookupTableAibench lookup;

	@Port(name="lookuptable",direction=Direction.INPUT,order=1)
	public void setLookupTable(LookupTableAibench lookup)
	{
		this.lookup=lookup;
	}
	
	@Port(name="file",direction=Direction.INPUT,order=2)
	public void setfile(File file)
	{
		if(lookup.importCVSFile(file.getAbsolutePath()))
		{
			this.lookup.notifyViewObservers();
			new ShowMessagePopup("Import Lookup Table Done !!!");
		}
		else
		{
			Workbench.getInstance().warn("File not Exist!!!");
		}
	}
}
