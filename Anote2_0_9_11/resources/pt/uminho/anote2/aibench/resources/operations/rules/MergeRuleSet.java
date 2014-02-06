package pt.uminho.anote2.aibench.resources.operations.rules;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import pt.uminho.anote2.aibench.resources.datatypes.Resources;
import pt.uminho.anote2.aibench.resources.datatypes.RulesAibench;
import pt.uminho.anote2.aibench.resources.datatypes.RulesSet;
import pt.uminho.anote2.aibench.resources.gui.report.MergeRulesSerReportGUI;
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
public class MergeRuleSet {
	
	private TimeLeftProgress progress = new TimeLeftProgress("Merge Rule Sets");
	private RulesAibench destiny;
	private RulesAibench source;
	private Set<Integer> destinyClassIds;
	private boolean newResource;
	private String name;
	private String notes;

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

	@Port(name="rules dest",direction=Direction.INPUT,order=4)
	public void setRulesDest(RulesAibench destiny)
	{
		this.destiny=destiny;
	}
	
	@Port(name="rules dest classes",direction=Direction.INPUT,order=5)
	public void getClassIdDestiny(Set<Integer> classIDsDestiny)
	{
		this.destinyClassIds = classIDsDestiny;

	}
	
	@Port(name="rules source",direction=Direction.INPUT,order=6)
	public void setRulesSource(RulesAibench source)
	{
		this.source=source;
	}
	
	@Port(name="rules source classes",direction=Direction.INPUT,order=7)
	public void getClassIdSource(Set<Integer> classIDsSource)
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
					List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(RulesSet.class);
					RulesSet ruls = (RulesSet) items.get(0).getUserData();
					Resources.newResource(name,notes,GlobalOptions.resourcesRuleSetName);
					int lastElement = HelpDatabase.getNextInsertTableID(DatabaseTablesName.resources)-1;
					RulesAibench newResource = new RulesAibench(lastElement, name, notes);
					reportPrevious = newResource.mergeOperation(destiny,destinyClassIds, progress);
					report = newResource.mergeOperation(source,classIDsSource, progress);
					report.setResourceSource2(destiny);
					report.addClassesAdding(reportPrevious.getClassesAdding());
					report.addTermAdding(reportPrevious.getTermsAdding());
					report.addExternalIDs(reportPrevious.getExternalIDs());
					report.addSynonymsAdding(reportPrevious.getSynonymsAdding());
					ruls.notifyViewObservers();
					ruls.addRules(newResource);
				} catch (DatabaseLoadDriverException e) {
					new ShowMessagePopup("Merge RuleSet Fail.");
					TreatExceptionForAIbench.treatExcepion(e);
					return;
				} catch (SQLException e) {
					new ShowMessagePopup("Merge RuleSet Fail.");
					TreatExceptionForAIbench.treatExcepion(e);
					return;
				}
			}
			else
			{
				try {
					report = destiny.mergeOperation(source,classIDsSource, progress);
					destiny.notifyViewObservers();
				} catch (DatabaseLoadDriverException e) {
					new ShowMessagePopup("Merge RuleSet Fail.");
					TreatExceptionForAIbench.treatExcepion(e);
					return;
				} catch (SQLException e) {
					new ShowMessagePopup("Merge RuleSet Fail.");
					TreatExceptionForAIbench.treatExcepion(e);
					return;
				}
			}
			{
				new ShowMessagePopup("Merge RuleSet Done.");
				new MergeRulesSerReportGUI(report);
			}
		}
	}
	
}
