package pt.uminho.anote2.aibench.corpus.operations;

import java.io.File;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation()
public class InitProject {

	@Port(name="project",direction=Direction.OUTPUT,order=1)
	public Corpora getProject()
	{
		System.gc();
		Corpora project = new Corpora();
		if(new File("conf/settings.conf").exists())
		{
			new ShowMessagePopup("Anote Ready");
		}
		else
		{
			new ShowMessagePopup("Anote Configuration");
		}
		return project;	
	}

}
