package pt.uminho.anote2.aibench.resources.operations.lookuptables;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import pt.uminho.anote2.aibench.resources.datatypes.LookupTableAibench;
import pt.uminho.anote2.aibench.resources.gui.report.UpdateReportGUI;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import pt.uminho.anote2.datastructures.utils.generic.CSVFileConfigurations;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;

@Operation(description="Import term from csv file to Lookup Table",enabled=false)
public class LoadLookupTableCSV {
	
	private LookupTableAibench lookup;
	private CSVFileConfigurations csvfileconfigurations;
	private TimeLeftProgress progress = new TimeLeftProgress("Update Lookup Table: From CSV File");

	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}
	
	@Port(name="lookuptable",direction=Direction.INPUT,order=1)
	public void setLookupTable(LookupTableAibench lookup)
	{
		this.lookup=lookup;
	}
	
	@Port(name="parameters",direction=Direction.INPUT,order=2)
	public void setParameters(CSVFileConfigurations csvfileconfigurations)
	{
		this.csvfileconfigurations=csvfileconfigurations;
	}
	
	@Port(name="file",direction=Direction.INPUT,order=3)
	public void setfile(File file)
	{
		System.gc();
		IResourceUpdateReport report;
		try {
			report = lookup.loadTermFromGenericCVSFileOperation(file,csvfileconfigurations,progress);
			this.lookup.notifyViewObservers();
			new UpdateReportGUI(report);
			new ShowMessagePopup("Import Lookup Table Done .");
		} catch (SQLException e) {
			new ShowMessagePopup("Import Lookup Table Fail .");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (IOException e) {
			new ShowMessagePopup("Import Lookup Table Fail .");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Import Lookup Table Fail .");
			TreatExceptionForAIbench.treatExcepion(e);
		}

	}
}
