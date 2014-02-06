package pt.uminho.anote2.aibench.publicationmanager.operations;

import pt.uminho.anote2.aibench.publicationmanager.dataStructures.PubMedSearchExtensionUpdate;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.operations.TimeLeftProgress;
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
@Operation(name="Operation that update query")
public class UpdateQueryOperation {
	
	private TimeLeftProgress progress = new TimeLeftProgress();
	private boolean cancel=false;
	private PubMedSearchExtensionUpdate pubmed;
	
	@Port(name="query",direction=Direction.INPUT,order=1)
	public void setQuery(QueryInformationRetrievalExtension query){
		if(query==null)
		{
			return;
		}
		if(PublicationManager.fullVersion)
		{
			fullversion(query);
		}
		else
		{
			if(!query.getProperties().containsKey("articleDetails"))
			{
				restrictedversion(query);
			}
			else 
			{
				String prop = query.getProperties().getProperty("articleDetails");
				if(prop.equals("freefulltext"))
				{
					fullversion(query);
				}
				else
				{
					restrictedversion(query);
				}
			}
		}
	}

	private void fullversion(QueryInformationRetrievalExtension query) {
		pubmed = new PubMedSearchExtensionUpdate(query.getPubManager().getProxy(),query.getPubManager().getDb(),cancel,progress);
		pubmed.search(query);
		progress.setProgress(1.0f);
		query.notifyViewObserver();
		if(pubmed.getPublicationAdded()>0)
		{
			query.getPubManager().notifyViewObserver();
		}
		Workbench.getInstance().warn("Update :"+pubmed.getPublicationAdded()+" new Publications");	
		new ShowMessagePopup("Query Update!");
	}
	
	private void restrictedversion(QueryInformationRetrievalExtension query) {
		pubmed = new PubMedSearchExtensionUpdate(query.getPubManager().getProxy(),query.getPubManager().getDb(),cancel,progress);
		pubmed.search(query);
		pubmed.searchUpdate(query);
		query.notifyViewObserver();
		if(pubmed.getPublicationAdded()>0)
		{
			query.getPubManager().notifyViewObserver();
		}
		Workbench.getInstance().warn("Update :"+pubmed.getPublicationAdded()+" new Publications");	
		new ShowMessagePopup("Query Update!");
	}
	
	@Cancel
	public void cancel(){
		Workbench.getInstance().warn("Query Update Canceled!");		
		cancel=true;
		pubmed.setCancel(true);
	}
	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}
	
	
	
	

}
