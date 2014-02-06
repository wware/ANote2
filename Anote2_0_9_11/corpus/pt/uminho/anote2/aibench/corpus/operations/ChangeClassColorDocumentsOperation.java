package pt.uminho.anote2.aibench.corpus.operations;

import java.util.GregorianCalendar;
import java.util.List;

import pt.uminho.anote2.aibench.corpus.datatypes.NERDocumentAnnotation;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;

@Operation(description="Change all Annotated Documents when change Color")
public class ChangeClassColorDocumentsOperation {

	private TimeLeftProgress progress = new TimeLeftProgress("Change Class Colors");
	
	@Port(name="string",direction=Direction.OUTPUT,order=1)
	public String setDocumentColor(){
		System.gc();
		List<ClipboardItem> nerDocs = Core.getInstance().getClipboard().getItemsByClass(NERDocumentAnnotation.class);
		int total = nerDocs.size();
		int position = 0;
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		for(ClipboardItem item:nerDocs)
		{
			NERDocumentAnnotation nerDoc = (NERDocumentAnnotation) item.getUserData();
			nerDoc.notifyViewObservers();
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
