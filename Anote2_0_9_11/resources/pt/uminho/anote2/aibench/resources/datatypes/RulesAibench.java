package pt.uminho.anote2.aibench.resources.datatypes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceMergeReport;
import pt.uminho.anote2.datastructures.database.queries.resources.rules.QueriesRules;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.resources.rule.RulesSet;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.rules.IRule;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import es.uvigo.ei.aibench.workbench.Workbench;

@Datatype(structure = Structure.SIMPLE,namingMethod="getNameRules",setNameMethod="setName",removable=true,renamed=true,autoOpen=true)
public class RulesAibench extends RulesSet{
	

	
	private TimeLeftProgress progress;
	private long startTime;

	public RulesAibench(int id, String name, String info) {
		super( id, name, info);
	}
	
	public String getNameRules()
	{
		return getName();
	}
	
	
	public void setName(String name)
	{
		if(name.equals(getName()))
		{
			
		}
		else if(name==null  || name.length() == 0)
		{
			this.notifyViewObservers();
			Workbench.getInstance().warn("Rules Set Name can not be empty");	
		}
		else
		{
			try {
				super.setName(name);
				this.notifyViewObservers();
				List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(RulesSet.class);
				RulesSet dics = (RulesSet) items.get(0).getUserData();
				dics.notifyViewObservers();
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			}
		}
	}
	
	public void notifyViewObservers(){
		this.setChanged();
		this.notifyObservers();
	}
	
	public IResourceMergeReport mergeOperation(IRule dicSnd,Set<Integer> classIDs, TimeLeftProgress progress) throws DatabaseLoadDriverException, SQLException 
	{	
		this.progress = progress;
		this.startTime = GregorianCalendar.getInstance().getTimeInMillis();
		return merge(dicSnd,classIDs);
	}
	
	public IResourceMergeReport mergeOperation(IRule dicSnd,TimeLeftProgress progress) throws DatabaseLoadDriverException, SQLException 
	{
		this.progress = progress;
		this.startTime = GregorianCalendar.getInstance().getTimeInMillis();
		return merge(dicSnd);
	}
	
	public void changePriorety(IResourceElement fst,IResourceElement snd) throws DatabaseLoadDriverException, SQLException
	{
		int priorety = fst.getPriority();
		PreparedStatement changePrioretyPS = Configuration.getDatabase().getConnection().prepareStatement(QueriesRules.changePriorety);
		changePrioretyPS.setInt(1, priorety);
		changePrioretyPS.setInt(2, snd.getID());
		changePrioretyPS.execute();
		changePrioretyPS.setInt(1, priorety-1);
		changePrioretyPS.setInt(2, fst.getID());
		changePrioretyPS.execute();
		changePrioretyPS.close();	
	}
	
	public List<IResourceElement> getRules() throws SQLException, DatabaseLoadDriverException {
		List<IResourceElement> resourceElems = new ArrayList<IResourceElement>();
		IResourceElement elem = null;
		String term=null;
		int idResourceElement,classID;	
		int priorety;	
		PreparedStatement getElems = Configuration.getDatabase().getConnection().prepareStatement(QueriesRules.getAllElementsWhitPriorety);
		getElems.setInt(1,getID());	
		ResultSet rs = getElems.executeQuery();
		while(rs.next())
		{
			idResourceElement = rs.getInt(1);
			classID = rs.getInt(3);
			term = rs.getString(4);
			priorety = rs.getInt(8);
			elem = new ResourceElement(idResourceElement,term,classID,ClassProperties.getClassIDClass().get(classID),priorety);
			resourceElems.add(elem);
		}
		rs.close();		
		getElems.close();

		return resourceElems;
	}
	
	protected void memoryAndProgress(int step, int total) {
		System.out.println((GlobalOptions.decimalformat.format((double)step/ (double) total * 100)) + " %...");
		Runtime.getRuntime().gc();
		System.out.println((Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory())/(1024*1024) + " MB ");
		updateTimeleftProgress(progress, total, step, this.startTime);
	}

	private void updateTimeleftProgress(TimeLeftProgress progress, int total,
			int step, long start) {
		long end;
		long totalTime;
		end = GregorianCalendar.getInstance().getTimeInMillis();
		totalTime = end-start;
		progress.setTime(totalTime, step, total);
		progress.setProgress((float) step / (float) total);
	}

	public void reoderpriorities(int priorety) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement reorderPrioreties = Configuration.getDatabase().getConnection().prepareCall(QueriesRules.reorderPriorety);
		reorderPrioreties.setInt(1,getID());
		reorderPrioreties.setInt(2,priorety);
		reorderPrioreties.execute();
		reorderPrioreties.close();
	}
	
	
}
