package pt.uminho.anote2.aibench.corpus.operations;

import java.io.File;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation(name="Save Corpus Status",description="Save Corpus Status",enabled=true)
public class SaveCorpusStatus {
	
	private Corpora pm;
	
	@Port(name="Corpora",direction=Direction.INPUT,order=1)
	public void setCorporar(Corpora pm){
		this.pm=pm;	
	}
	
	@Port(name="File",direction=Direction.INPUT,order=2)
	public void saveFile(File file) throws Exception{
		System.gc();
		if(pm.saveFile(file))
		{
			new ShowMessagePopup("Corpora Status Save");
		}
		else
		{
			new ShowMessagePopup("Corpora Status Save Cancel .");
		}
	}

}
