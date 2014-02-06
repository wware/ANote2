package pt.uminho.anote2.aibench.resources.operations.lookuptables;

import java.io.File;

import pt.uminho.anote2.aibench.resources.datatypes.LookupTableAibench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation(description="Save lookup information into csv file")
public class SaveLookupTableCSV {
	
	private LookupTableAibench lookup;

	@Port(name="lookuptable",direction=Direction.INPUT,order=1)
	public void setDLookupTable(LookupTableAibench lookup)
	{
		if(lookup==null)
		{
			Workbench.getInstance().warn("lookuptabel null");
		}
		this.lookup=lookup;
	}
	
	@Port(name="file",direction=Direction.INPUT,order=2)
	public void setfile(File file)
	{
		this.lookup.exportCSVFile(file.getAbsolutePath());
		new ShowMessagePopup("Export Lookup Table Done !!!");
	}
	
	
	
	

}
