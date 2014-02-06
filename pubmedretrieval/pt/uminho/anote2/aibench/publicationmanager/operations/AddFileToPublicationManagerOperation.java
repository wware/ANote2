
package pt.uminho.anote2.aibench.publicationmanager.operations;

import java.io.File;
import java.io.IOException;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.utils.FileHandling;
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
		File newFile;
		if(pub.getOtherID().equals(""))
		{
			newFile = new File(PublicationManager.saveDocs+"id"+pub.getID()+".pdf");
		}
		else
		{
			newFile = new File(PublicationManager.saveDocs+"id"+pub.getID()+"-"+pub.getOtherID()+".pdf");
		}
		try {
			FileHandling.copy(file, newFile);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		new ShowMessagePopup("Publication Insert Sucess!!!");
		query.notifyViewObserver();
	}
	
	

}
