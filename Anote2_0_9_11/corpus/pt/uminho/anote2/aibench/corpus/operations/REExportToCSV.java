package pt.uminho.anote2.aibench.corpus.operations;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;

import pt.uminho.anote2.aibench.corpus.datatypes.RESchema;
import pt.uminho.anote2.aibench.corpus.gui.report.REExportCSVReport;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.processes.IRESchemaExportReport;
import pt.uminho.anote2.datastructures.process.io.export.RECSVConfigutarion;
import pt.uminho.anote2.process.IE.re.IRECSVConfiguration;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;

@Operation(enabled=false)
public class REExportToCSV {
	
	private RESchema reProcess;
	private TimeLeftProgress progress = new TimeLeftProgress("Export RE Process to TSV File");
	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress ;
	}
	

	@Port(name="process",direction=Direction.INPUT,order=1)
	public void setREProcess(RESchema reProcess)
	{
		this.reProcess=reProcess;
	}
	
	@Port(name="file",direction=Direction.INPUT,order=2)
	public void setREProcess(File file)
	{
		System.gc();
		IRECSVConfiguration configuration = RECSVConfigutarion.getDefaultValues();
		IRESchemaExportReport report;
		try {
			report = reProcess.exportToCSV(file, configuration , progress);
			if(report.isFinishing())
			{
				new REExportCSVReport(report);
				new ShowMessagePopup("RE Export Complete");
			}
			else
			{
				new ShowMessagePopup("RE Export Cancel");
			}
		} catch (FileNotFoundException e) {
			new ShowMessagePopup("RE Export Fail");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (SQLException e) {
			new ShowMessagePopup("RE Export Fail");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("RE Export Fail");
			TreatExceptionForAIbench.treatExcepion(e);
		}

	}

}
