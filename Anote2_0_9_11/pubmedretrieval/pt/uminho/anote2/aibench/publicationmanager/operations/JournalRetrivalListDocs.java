package pt.uminho.anote2.aibench.publicationmanager.operations;

import java.util.List;

import pt.uminho.anote2.aibench.publicationmanager.dataStructures.PubmedCrawlExtention;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.publicationmanager.gui.IRCrawlingReportGUI;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.core.report.processes.ir.IIRCrawlingProcessReport;
import pt.uminho.anote2.datastructures.utils.conf.OtherConfigurations;
import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;

/**
 * Aibench Operation -- Operation that permit download of a List of Pdf IIRCrawl
 * 
 * @author Hugo Costa
 *
 */
@Operation(name="Get A List of Pdf Document",enabled=false)
public class JournalRetrivalListDocs {
	
	private QueryInformationRetrievalExtension query;
	private TimeLeftProgress progress = new TimeLeftProgress("Journal Retrieval");
	private PubmedCrawlExtention crawl;

	@Port(name="query",direction=Direction.INPUT,order=1)
	public void setQuery(QueryInformationRetrievalExtension query){
		this.query=query;	
	}
	
	@Port(name="Publications",direction=Direction.INPUT,order=2)
	public void setListOFPmid(List<IPublication> pubs){
		System.gc();
		crawl = new PubmedCrawlExtention(OtherConfigurations.getFreeFullTextOnly(),progress);
		IIRCrawlingProcessReport report = crawl.getFullText(pubs);
		if(report.isFinishing())
		{
			query.notifyViewObserver();
			new IRCrawlingReportGUI(report);
			new ShowMessagePopup("Journal Retrieval Complete");
		}
		else
		{
			new ShowMessagePopup("Journal Retrieval Cancel");
		}	
	}

	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}

	@Cancel(cancelInBackground=true)
	public void cancel(){
		crawl.stop();
	}

}