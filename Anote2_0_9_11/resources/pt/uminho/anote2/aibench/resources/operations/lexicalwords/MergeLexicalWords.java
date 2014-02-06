package pt.uminho.anote2.aibench.resources.operations.lexicalwords;

import java.sql.SQLException;
import java.util.List;

import pt.uminho.anote2.aibench.resources.datatypes.LexicalWordsAibench;
import pt.uminho.anote2.aibench.resources.datatypes.LexicalWordsSet;
import pt.uminho.anote2.aibench.resources.datatypes.Resources;
import pt.uminho.anote2.aibench.resources.gui.report.MergeLexicalWordsReportGUI;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceMergeReport;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.schema.DatabaseTablesName;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation(enabled=false)
public class MergeLexicalWords {
		
	private TimeLeftProgress progress = new TimeLeftProgress("Merge Lexical Words");
	private LexicalWordsAibench destiny;
	private boolean newResource;
	private String name;
	private String notes;
	private boolean cancel;

	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}

//	@Cancel
//	public void cancel(){
//		cancelMethod();
//	}
	
	@Port(name="new resource option",direction=Direction.INPUT,order=1)
	public void getIsnewResource(boolean newResource)
	{
		this.newResource = newResource;
	}
	
	@Port(name="name",direction=Direction.INPUT,order=2)
	public void setName(String name)
	{
		this.name=name;
	}

	@Port(name="notes",direction=Direction.INPUT,order=3)
	public void getInfo(String notes)
	{
		this.notes = notes;

	}

	@Port(name="lexical words dest",direction=Direction.INPUT,order=4)
	public void setDicionariesDest(LexicalWordsAibench destiny)
	{
		this.destiny=destiny;
	}
	
	@Port(name="lexical words source",direction=Direction.INPUT,order=5)
	public void setDicionariesSource(LexicalWordsAibench source)
	{
		System.gc();
		if(destiny.compareTo(source)==0)
		{
			Workbench.getInstance().warn("Source and Destiny Resources are the same");
		}
		else
		{
			IResourceMergeReport report;
			if(newResource)
			{
				try {
					IResourceMergeReport reportPrevious;
					List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(LexicalWordsSet.class);
					LexicalWordsSet lws = (LexicalWordsSet) items.get(0).getUserData();
					Resources.newResource(name, notes,GlobalOptions.resourcesLexicalWords);
					int lastElement = HelpDatabase.getNextInsertTableID(DatabaseTablesName.resources)-1;
					LexicalWordsAibench newResource = new LexicalWordsAibench(lastElement, name, notes);
					reportPrevious = newResource.mergeOperation(destiny,progress);
					report = newResource.mergeOperation(source, progress);
					report.setResourceSource2(destiny);
					report.addClassesAdding(reportPrevious.getClassesAdding());
					report.addTermAdding(reportPrevious.getTermsAdding());
					report.addExternalIDs(reportPrevious.getExternalIDs());
					report.addSynonymsAdding(reportPrevious.getSynonymsAdding());
					lws.notifyViewObservers();
					lws.addLexicalWords(newResource);
				} catch (SQLException e) {
					new ShowMessagePopup("Merge Lexical Words Fail.");
					TreatExceptionForAIbench.treatExcepion(e);
					return;
				} catch (DatabaseLoadDriverException e) {
					new ShowMessagePopup("Merge Lexical Words Fail.");
					TreatExceptionForAIbench.treatExcepion(e);
					return;
				}
			}
			else
			{
				try {
					report = destiny.mergeOperation(source, progress);
					destiny.notifyViewObservers();
				} catch (DatabaseLoadDriverException e) {
					new ShowMessagePopup("Merge Lexical Words Fail.");
					TreatExceptionForAIbench.treatExcepion(e);
					return;
				} catch (SQLException e) {
					new ShowMessagePopup("Merge Lexical Words Fail.");
					TreatExceptionForAIbench.treatExcepion(e);
					return;
				}
			}
			new MergeLexicalWordsReportGUI(report);
			new ShowMessagePopup("Merge Lexical Words Done.");


		}
	}

}
