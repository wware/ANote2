package pt.uminho.anote2.aibench.corpus.operations;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;

import pt.uminho.anote2.aibench.corpus.datatypes.NERSchema;
import pt.uminho.anote2.aibench.corpus.gui.report.NERExportReportGUI;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.processes.INERSchemaExportReport;
import pt.uminho.anote2.datastructures.process.io.export.NERCVSConfiguration;
import pt.uminho.anote2.process.IE.ner.export.INERCSVConfiguration;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;

@Operation(enabled=false)
public class NERExportToCSV {
	
	
	private NERSchema nerProcess;
	private TimeLeftProgress progress = new TimeLeftProgress("Export NER Process to CSV File");
	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress ;
	}
	

	@Port(name="process",direction=Direction.INPUT,order=1)
	public void setREProcess(NERSchema nerProcess)
	{
		this.nerProcess=nerProcess;
	}
	
	@Port(name="file",direction=Direction.INPUT,order=2)
	public void setREProcess(File file)
	{
		System.gc();
		INERCSVConfiguration configuration = NERCVSConfiguration.getDefaultSettings();
		INERSchemaExportReport report;
		try {
			report = nerProcess.exportToCSV(file, configuration , progress);
			if(report.isFinishing())
			{
				new NERExportReportGUI(report);
				new ShowMessagePopup("NER Export Complete");
			}
			else
			{
				new ShowMessagePopup("NER Export Cancel");
			}
		} catch (FileNotFoundException e) {
			new ShowMessagePopup("NER Export Fail");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (SQLException e) {
			new ShowMessagePopup("NER Export Fail");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("NER Export Fail");
			TreatExceptionForAIbench.treatExcepion(e);
		}

	}

}
