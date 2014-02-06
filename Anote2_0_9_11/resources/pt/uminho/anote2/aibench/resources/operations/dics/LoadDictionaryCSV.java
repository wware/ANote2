package pt.uminho.anote2.aibench.resources.operations.dics;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import pt.uminho.anote2.aibench.resources.datatypes.DictionaryAibench;
import pt.uminho.anote2.aibench.resources.gui.report.UpdateReportGUI;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import pt.uminho.anote2.datastructures.utils.generic.CSVFileConfigurations;
import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;

@Operation(name="Load Terms From CSV File", enabled=false)
public class LoadDictionaryCSV {
	
	private DictionaryAibench dic;
	private CSVFileConfigurations csvfileconfigurations;
	private TimeLeftProgress progress = new TimeLeftProgress("Load CSV File");

	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}
	
	@Cancel(cancelInBackground=true)
	public void setCancel()
	{
		dic.setCancel();
	}
	
	@Port(name="dictionary",direction=Direction.INPUT,order=1)
	public void setDictionary(DictionaryAibench dic)
	{
		this.dic=dic;
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
			report = dic.loadTermFromGenericCVSFileOperation(file,csvfileconfigurations,progress);
			if(report.isFinishing())
			{
				new ShowMessagePopup("Dictionary Update Complete .");
			}
			else
			{
				new ShowMessagePopup("Dictionary Update Cancel .");
			}
			dic.notifyViewObservers();
			new UpdateReportGUI(report);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (IOException e) {	
			TreatExceptionForAIbench.treatExcepion(e);
		}


	}
	
	
}
