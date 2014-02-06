package pt.uminho.anote2.aibench.publicationmanager.operations;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.operations.TimeLeftProgress;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.process.ir.pubmed.PubMedCrawl;
import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;

/**
 * Aibench Operation -- Operation that permit download of a List of Pdf IIRCrawl
 * 
 * @author Hugo Costa
 *
 */
@Operation(name="Get A List of Pdf Document")
public class JournalRetrivalListDocs {
	
	private QueryInformationRetrievalExtension query;
	private boolean run = true;
	private TimeLeftProgress progress = new TimeLeftProgress();

	@Port(name="query",direction=Direction.INPUT,order=1)
	public void setQuery(QueryInformationRetrievalExtension query){
		this.query=query;	
	}
	
	@Port(name="Publications",direction=Direction.INPUT,order=2)
	public void setListOFPmid(ArrayList<IPublication> pubs){
		if(PublicationManager.fullVersion)
		{
			findPubs(pubs);
		}
		else
		{
			findPubsRestrict(pubs);
		}
	}

	private void findPubs(ArrayList<IPublication> pubs) {
		PubMedCrawl crawl = new PubMedCrawl(query.getPubManager().getProxy(),query.getPubManager().getDb());
		int size = pubs.size();
		long initialTime = GregorianCalendar.getInstance().getTimeInMillis();
		long nowTime,differTime;
		int total =0;
		for(int i=0;i< size && run;i++)
		{
			String pmid = pubs.get(i).getOtherID();
			File file = crawl.getFullText(pmid,PublicationManager.saveDocs);
			if(file!=null)
			{
				if(!file.exists())
				{
					//updatePmidURL(query.getPubManager().getDb(),pmid,"na");
				}
				else
				{
					//updatePmidURL(query.getPubManager().getDb(),pmid,"available");
					total++;
				}
			}
			else
			{
			}
			nowTime = GregorianCalendar.getInstance().getTimeInMillis();

			differTime= nowTime-initialTime;
			progress.setTime(differTime,i+1,size);
			float prog = ((float)i+1)/(float)size;
			progress.setProgress(prog);
		}	
		progress.setProgress(1.0f);
		if(run==true)
		{
			new ShowMessagePopup("Journal Retrieval Complete");
		}
		else
		{
			new ShowMessagePopup("Journal Retrieval Cancel");
		}
		query.notifyViewObserver();
	}
	
	private void findPubsRestrict(ArrayList<IPublication> pubs) {
		PubMedCrawl crawl = new PubMedCrawl(query.getPubManager().getProxy(),query.getPubManager().getDb());
		int size = pubs.size();
		long initialTime = GregorianCalendar.getInstance().getTimeInMillis();
		long nowTime,differTime;
		for(int i=0;i< size && run;i++)
		{
			if(pubs.get(i).getAvailableFreeFullTExt())
			{
				String pmid = pubs.get(i).getOtherID();
				File file = crawl.getFullText(pmid,PublicationManager.saveDocs);
				if(file!=null)
				{
					if(!file.exists())
					{
						//updatePmidURL(query.getPubManager().getDb(),pmid,"na");
					}
					else
					{
						//updatePmidURL(query.getPubManager().getDb(),pmid,"available");
					}
				}
				else
				{
				}
			}
			else
			{
			}
			nowTime = GregorianCalendar.getInstance().getTimeInMillis();

			differTime= nowTime-initialTime;
			progress.setTime(differTime,i+1,size);
			float prog = ((float)i+1)/(float)size;
			progress.setProgress(prog);
		}	
		progress.setProgress(1.0f);
		if(run==true)
		{
			new ShowMessagePopup("Journal Retrieval Complete");
		}
		else
		{
			new ShowMessagePopup("Journal Retrieval Cancel");
		}
		query.notifyViewObserver();
	}

	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}

	@Cancel
	public void cancel(){
		run = false;
		Workbench.getInstance().warn("Journal Retrieval canceled!");	
	}

}