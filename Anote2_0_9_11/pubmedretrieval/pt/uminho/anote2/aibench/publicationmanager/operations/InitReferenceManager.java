package pt.uminho.anote2.aibench.publicationmanager.operations;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

/**
 * Aibench Operation -- for Init Reference Manager
 * 
 * @author Hugo Costa
 *
 */
@Operation()
public class InitReferenceManager{
	
	@Port(name="Publication Manager",direction=Direction.OUTPUT,order=1)
	public PublicationManager getReferenceManager(){
		System.gc();
		PublicationManager pubManager = new PublicationManager();
		return pubManager;	
	}
}

