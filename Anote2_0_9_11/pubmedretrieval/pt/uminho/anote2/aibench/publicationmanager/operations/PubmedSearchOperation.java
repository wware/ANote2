package pt.uminho.anote2.aibench.publicationmanager.operations;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import pt.uminho.anote2.aibench.publicationmanager.dataStructures.PubMedSearchExtension;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.publicationmanager.gui.IRSearchReportGUI;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.processes.ir.IIRSearchProcessReport;
import pt.uminho.anote2.process.IR.exception.InternetConnectionProblemException;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;
/**
 * Aibench Operation -- Operation for IIRSearch 
 * 
 * @author Hugo Costa
 *
 */
@Operation(enabled=true)
public class PubmedSearchOperation{

	private PublicationManager publicationManager;

	private String keywords;
	private String organism;
	
	PubMedSearchExtension pubmed;
	
	private boolean cancel=false;
	private TimeLeftProgress progress = new TimeLeftProgress("Pubmed Search");
	
	@Cancel(cancelInBackground=true) 
	public void cancel(){
		cancel=true;
		pubmed.setCancel(true);
	}
	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}
	
	@Port(name="catalogue",direction=Direction.INPUT,order=1)
	public void setCatalogue(PublicationManager catalogue){
		this.publicationManager = catalogue;
	}
			
	@Port(name="keywords",direction=Direction.INPUT,order=2)
	public void setKeywords(String keywords){
		this.keywords=keywords;
	}
	
	@Port(name="organism",direction=Direction.INPUT,order=3)
	public void setOrganism(String organism){
		this.organism=organism;
	}

	@Port(name="properties",direction=Direction.INPUT,order=4)
	public void setTo_date(Properties properties){
		System.gc();
		pubmed = new PubMedSearchExtension(cancel,progress);
		IIRSearchProcessReport report = null;
		try {
			report = pubmed.search(keywords, organism,properties);
			if(report.isFinishing())
			{
				progress.setProgress(1.0f);
				publicationManager.notifyViewObserver();	
				List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(PublicationManager.class);
				PublicationManager pm = (PublicationManager) cl.get(0).getUserData();
				pm.addQueryInformationRetrievalExtension(PublicationManager.getLastQuery(pm));
				new IRSearchReportGUI(report);
				new ShowMessagePopup("Pubmed Search finished!");
			}
			else
			{
				new ShowMessagePopup("Pubmed Search Cancel!");
			}
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Pubmed Search Fail!");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		} catch (SQLException e) {
			new ShowMessagePopup("Pubmed Search Fail!");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		} catch (InternetConnectionProblemException e) {
			new ShowMessagePopup("Pubmed Search Fail!");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		}
		
	}
}
