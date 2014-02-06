package pt.uminho.anote2.aibench.publicationmanager.operations;

import java.sql.SQLException;
import java.util.Properties;

import pt.uminho.anote2.aibench.publicationmanager.dataStructures.PubMedSearchExtension;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.operations.TimeLeftProgress;
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
@Operation()
public class PubmedSearchOperation{

	private PublicationManager publicationManager;

	private String keywords;
	private String organism;
	
	PubMedSearchExtension pubmed;
	
	private boolean cancel=false;
	private TimeLeftProgress progress = new TimeLeftProgress();
	
	@Cancel
	public void cancel() throws SQLException{
		Workbench.getInstance().warn("Pubmed Search canceled!");		
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
		pubmed = new PubMedSearchExtension(publicationManager.getProxy(),publicationManager.getDb(),cancel,progress);	
		if(PublicationManager.fullVersion)
		{	
			fullversion(properties);
		}
		else
		{
			if(!properties.containsKey("articleDetails"))
			{
				restrictedversion(properties);
			}
			else 
			{
				String prop = properties.getProperty("articleDetails");
				if(prop.equals("freefulltext"))
				{
					pubmed.setIsfreefulltextquery(true);
					fullversion(properties);
				}
				else
				{
					restrictedversion(properties);
				}
			}
		}
	}

	private void fullversion(Properties properties) {
		if(pubmed.search(keywords, organism,properties))
		{
			progress.setProgress(1.0f);
			publicationManager.notifyViewObserver();	
			new ShowMessagePopup("Pubmed Search finished!");
		}
		else
		{
			new ShowMessagePopup("Pubmed Search Cancel!");
		}

	} 
	
	private void restrictedversion(Properties properties) {
		if(pubmed.search(keywords, organism,properties))
		{
			pubmed.searchAddProperty(keywords, organism,properties);
			progress.setProgress(1.0f);
			publicationManager.notifyViewObserver();	
			new ShowMessagePopup("Pubmed Search finished!");
		}
		else
		{
			new ShowMessagePopup("Pubmed Search Cancel!");
		}
	} 
}
