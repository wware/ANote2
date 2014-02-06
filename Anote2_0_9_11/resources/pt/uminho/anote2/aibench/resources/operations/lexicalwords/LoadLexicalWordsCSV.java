package pt.uminho.anote2.aibench.resources.operations.lexicalwords;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import pt.uminho.anote2.aibench.resources.datatypes.LexicalWordsAibench;
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

@Operation(description="Import term from csv file to Lexical Words",enabled=false)
public class LoadLexicalWordsCSV {
	
	private LexicalWordsAibench lexical;
	private CSVFileConfigurations configurations;
	private TimeLeftProgress progress = new TimeLeftProgress("Update Lexical Words: From CSV File");
	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress ;
	}

	@Port(name="lexicalwords",direction=Direction.INPUT,order=1)
	public void setLookupTable(LexicalWordsAibench lexical)
	{
		this.lexical=lexical;
	}
	
	@Port(name="parameters",direction=Direction.INPUT,order=2)
	public void setCSVFile(CSVFileConfigurations configurations)
	{
		this.configurations=configurations;
	}
	
	@Port(name="file",direction=Direction.INPUT,order=3)
	public void setfile(File file)
	{
		System.gc();
		IResourceUpdateReport report;
		try {
			report = lexical.loadTermFromGenericCVSFileOperation(file, configurations,progress);
			this.lexical.notifyViewObservers();
			new UpdateReportGUI(report);
			new ShowMessagePopup("Import Lexical Words Done .");
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Import Lexical Words Fail .");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (SQLException e) {
			new ShowMessagePopup("Import Lexical Words Fail .");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (IOException e) {
			new ShowMessagePopup("Import Lexical Words Fail .");
			TreatExceptionForAIbench.treatExcepion(e);
		}

	}
}
