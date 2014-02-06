package pt.uminho.anote2.aibench.corpus.operations;

import java.util.GregorianCalendar;
import java.util.List;

import pt.uminho.anote2.aibench.corpus.datatypes.REDocumentAnnotation;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;

@Operation(description="Change all open document backgorund color")
public class ChangeVerbBackgroundColorOperation {
	
	private TimeLeftProgress progress = new TimeLeftProgress("Change Clues Background Color");
	
	@Port(name="string",direction=Direction.OUTPUT,order=1)
	public String setDocumentColor(){
		System.gc();
		List<ClipboardItem> nerDocs = Core.getInstance().getClipboard().getItemsByClass(REDocumentAnnotation.class);
		int total = nerDocs.size();
		int position = 0;
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		for(ClipboardItem item:nerDocs)
		{
			REDocumentAnnotation reDoc = (REDocumentAnnotation) item.getUserData();
			reDoc.notifyViewObservers();
			position ++;
			long nowTime = GregorianCalendar.getInstance().getTimeInMillis();
			progress.setProgress((float) position / (float) total);
			progress.setTime(nowTime-startTime, position, total);
		}
		new ShowMessagePopup("Color Change.");
		return null;
	}

	@Progress
	public TimeLeftProgress getProgress() {
		return progress;
	}

}
