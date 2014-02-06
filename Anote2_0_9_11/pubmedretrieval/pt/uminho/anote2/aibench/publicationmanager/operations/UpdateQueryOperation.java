package pt.uminho.anote2.aibench.publicationmanager.operations;

import java.sql.SQLException;

import pt.uminho.anote2.aibench.publicationmanager.dataStructures.PubMedSearchExtensionUpdate;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.publicationmanager.gui.IRSearchUpdateReportGUI;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.report.processes.IRSearchReport;
import pt.uminho.anote2.process.IR.exception.InternetConnectionProblemException;
import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;
/**
 * Aibench Operation -- Operation for update a Query
 * 
 * @author Hugo Costa
 *
 */
@Operation(name="Operation that update a query",enabled=false)
public class UpdateQueryOperation {
	
	private TimeLeftProgress progress = new TimeLeftProgress("Query Update");
	private boolean cancel=false;
	private PubMedSearchExtensionUpdate pubmed;
	
	@Port(name="query",direction=Direction.INPUT,order=1)
	public void setQuery(QueryInformationRetrievalExtension query){
		System.gc();
		pubmed = new PubMedSearchExtensionUpdate(cancel, progress);
		IRSearchReport report;
		try {
			report = pubmed.updateQuery(query);
		} catch (DatabaseLoadDriverException e) {
			Workbench.getInstance().warn("Query Update Fail!");		
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		} catch (SQLException e) {
			Workbench.getInstance().warn("Query Update Fail!");		
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		} catch (InternetConnectionProblemException e) {
			Workbench.getInstance().warn("Query Update Fail!");		
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		}

		if(report.isFinishing())
		{
			progress.setProgress(1.0f);
			query.notifyViewObserver();
			query.getPubManager().notifyViewObserver();
			new IRSearchUpdateReportGUI(report);
			new ShowMessagePopup("Query Update!");
		}
		else
		{
			Workbench.getInstance().warn("Query Update Canceled!");		
		}
	}
	
	@Cancel(cancelInBackground=true)
	public void cancel(){
		cancel=true;
		pubmed.setCancel(true);
	}
	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}
	
	
	
	

}
