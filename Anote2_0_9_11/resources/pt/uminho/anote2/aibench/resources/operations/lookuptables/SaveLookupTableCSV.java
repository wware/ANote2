package pt.uminho.anote2.aibench.resources.operations.lookuptables;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import pt.uminho.anote2.aibench.resources.datatypes.LookupTableAibench;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation(description="Save lookup information into csv file",enabled=false)
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
		System.gc();
		if(file.isDirectory())
			file = new File(file.getAbsoluteFile()+"/"+"lookuptetable_"+lookup.getID()+".tsv");
		try {
			this.lookup.exportCSVFile(file.getAbsolutePath());
		} catch (SQLException e) {
			new ShowMessagePopup("Export Lookup Table Fail .");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Export Lookup Table Fail .");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		} catch (IOException e) {
			new ShowMessagePopup("Export Lookup Table Fail .");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		}
		new ShowMessagePopup("Export Lookup Table Done .");
	}
	
	
	
	

}
