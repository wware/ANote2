package pt.uminho.anote2.aibench.resources.operations.dics;

import java.sql.SQLException;

import pt.uminho.anote2.aibench.resources.views.DictionaryView;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;

@Operation
public class DictionarySearchOperation {
	
	private TimeLeftProgress progress = new TimeLeftProgress("Dictionary Search");

	
	@Port(name="Resources",direction=Direction.INPUT,order=1)
	public void setDictionaryView(DictionaryView view){
		System.gc();
		try {
			view.proceedSearch(progress);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}
	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}
}
