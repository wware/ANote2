package pt.uminho.anote2.aibench.resources.datatypes;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceMergeReport;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import pt.uminho.anote2.datastructures.resources.lookuptable.LookupTable;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.generic.CSVFileConfigurations;
import pt.uminho.anote2.resource.lookuptables.ILookupTable;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import es.uvigo.ei.aibench.workbench.Workbench;

@Datatype(structure = Structure.SIMPLE,namingMethod="getNameAibench",setNameMethod="setName",removable=true,renamed=true,autoOpen=true)
public class LookupTableAibench extends LookupTable{
	
	private int addingTerms = 0;
	private TimeLeftProgress progress;
	private long startTime;

	public LookupTableAibench(int id, String name, String info) {
		super(id, name, info);
	}
	
	public String getNameAibench()
	{
		return getName();
	}
	
	public void notifyViewObservers(){
		this.setChanged();
		this.notifyObservers();
	}
	
	
	public void setName(String name)
	{
		if(name.equals(getName()))
		{
			
		}
		else if(name==null  || name.length() == 0)
		{
			this.setChanged();
			this.notifyObservers();
			Workbench.getInstance().warn("Lookup Table Name can not be empty");	
		}
		else
		{
			try {
				super.setName(name);
				this.notifyViewObservers();
				List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(LookupTables.class);
				LookupTables dics = (LookupTables) items.get(0).getUserData();
				dics.notifyViewObservers();
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			}
		}
	}
	
	
	public IResourceUpdateReport loadTermFromGenericCVSFileOperation(File file,CSVFileConfigurations csvfileconfigurations,TimeLeftProgress progress) throws SQLException, IOException, DatabaseLoadDriverException {
		this.progress=progress;
		this.startTime = GregorianCalendar.getInstance().getTimeInMillis();
		return loadTermFromGenericCVSFile(file, csvfileconfigurations);
	}
	
	public IResourceMergeReport mergeOperation(ILookupTable dicSnd,Set<Integer> classIDs, TimeLeftProgress progress) throws SQLException, DatabaseLoadDriverException 
	{	
		this.progress = progress;
		this.startTime = GregorianCalendar.getInstance().getTimeInMillis();
		return merge(dicSnd,classIDs);
	}
	
	public IResourceMergeReport mergeOperation(ILookupTable dicSnd,TimeLeftProgress progress) throws SQLException, DatabaseLoadDriverException 
	{
		this.progress = progress;
		this.startTime = GregorianCalendar.getInstance().getTimeInMillis();
		return merge(dicSnd);
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

	

	public int  getTermsMergeAdding() {
		return addingTerms;
	}



}
