package pt.uminho.anote2.aibench.resources.operations.dics;

import java.sql.SQLException;

import pt.uminho.anote2.aibench.resources.datatypes.DictionaryAibench;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;

@Operation(description="Remove Resource Classe Elements")
public class RemoveClassElementsOperation {
	
	private DictionaryAibench resource;
	private TimeLeftProgress progress = new TimeLeftProgress("Dictionary: Remove Class");

	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}

	
	@Port(name="dictionary",direction=Direction.INPUT,order=1)
	public void setResourcey(DictionaryAibench resource)
	{
		this.resource=resource;
	}
	
	@Port(name="classID",direction=Direction.INPUT,order=2)
	public void setClassID(int classID)
	{
		System.gc();
		try {
			this.resource.inactiveElementsByClassID(classID,progress);
			this.resource.removeResourceContent(classID);
			this.resource.notifyViewObservers();
			new ShowMessagePopup("Remove All Class Terms.");
		} catch (SQLException e) {
			new ShowMessagePopup("Remove All Class Terms Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Remove All Class Terms Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
		}

	}
}
