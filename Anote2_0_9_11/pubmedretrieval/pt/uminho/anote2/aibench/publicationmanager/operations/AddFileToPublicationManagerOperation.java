
package pt.uminho.anote2.aibench.publicationmanager.operations;

import java.io.File;
import java.io.IOException;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.document.IPublication;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation()
public class AddFileToPublicationManagerOperation {
	
	private IPublication pub;
	private QueryInformationRetrievalExtension query;
	
	@Port(name="Publication",direction=Direction.INPUT,order=1)
	public void getPub(IPublication pub){
		
		this.pub=pub;
	}
	
	@Port(name="Query",direction=Direction.INPUT,order=2)
	public void getQuery(QueryInformationRetrievalExtension query){
		
		this.query=query;
	}
	
	@Port(name="File",direction=Direction.INPUT,order=3)
	public void getFile(File file)
	{
		System.gc();
		try {
			pub.addPDFFile(file);
		} catch (IOException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			new ShowMessagePopup("Publication Insert Fail.");
			return;
		}
		new ShowMessagePopup("Publication Insert Sucess.");
	}
	
	

}
