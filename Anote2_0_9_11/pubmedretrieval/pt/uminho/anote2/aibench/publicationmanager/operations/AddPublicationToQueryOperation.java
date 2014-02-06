package pt.uminho.anote2.aibench.publicationmanager.operations;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.core.document.RelevanceType;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation(enabled=false)
public class AddPublicationToQueryOperation {
	
	private QueryInformationRetrievalExtension query;
	private IPublication pub;
	private File file;
	private String publictionType;
	
	@Port(name="Query",direction=Direction.INPUT,order=1)
	public void getQuery(QueryInformationRetrievalExtension query){
		this.query=query;
	}
	
	/**
	 * This is an object and not a IPublication because AIbench not permits remove links for Dataypes that don´t have depencecies
	 * 
	 * @param pub
	 */
	@Port(name="Pub",direction=Direction.INPUT,order=2)
	public void getPub(List pub){
		this.pub=(IPublication) pub.get(0);
	}
	
	@Port(name="Type",direction=Direction.INPUT,order=3)
	public void getPublicationType(String publictionType){
		this.publictionType=publictionType;
	}
	
	@Port(name="File",direction=Direction.INPUT,order=3)
	public void getFile(File file){
		this.file=file;
	
	}
	
	@Port(name="Relevance",direction=Direction.INPUT,order=4)
	public void getRelevance(String relavance){
		System.gc();
		RelevanceType relevance = RelevanceType.convertString(relavance);
		try {
			query.addOusidePublication(pub,publictionType,file, relevance);
		} catch (SQLException e) {
			new ShowMessagePopup("Publication Insert Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Publication Insert Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		} catch (IOException e) {
			new ShowMessagePopup("Publication Insert Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		}
		query.notifyViewObserver();
		new ShowMessagePopup("Publication Insert.");
	}  

}
