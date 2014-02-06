package pt.uminho.anote2.aibench.resources.datatypes;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.List;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceMergeReport;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.resources.lexiacalwords.LexicalWords;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.generic.CSVFileConfigurations;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import es.uvigo.ei.aibench.workbench.Workbench;

@Datatype(structure = Structure.SIMPLE,namingMethod="getNameDic",setNameMethod="setName",renamed=true,removable=true,autoOpen=true)
public class LexicalWordsAibench extends LexicalWords{
		
	private int termsMergeAdding = 0;
	private TimeLeftProgress progress;
	private long startTime;

	
	
	public LexicalWordsAibench(int id, String name, String info) {
		super( id, name, info);
	}


	public String getNameDic()
	{
		return getName();
	}


	public void setInfo(String info)
	{
		super.setInfo(info);
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
			this.notifyViewObservers();
			Workbench.getInstance().warn("Lexical Words Name can not be empty");	
		}
		else
		{
			try {
				super.setName(name);
				this.notifyViewObservers();
				List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(LexicalWordsSet.class);
				LexicalWordsSet dics = (LexicalWordsSet) items.get(0).getUserData();
				dics.notifyViewObservers();
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			}
		}
	}


	public int getTermsMergeAdding() {
		return termsMergeAdding;
	}


	public void setTermsMergeAdding(int termsMergeAdding) {
		this.termsMergeAdding = termsMergeAdding;
	}


	public int compareTo(LexicalWordsAibench lexicalWords) {
		if(this.getID()==lexicalWords.getID())
		{
			return 0;
		}
		else if(this.getID()<lexicalWords.getID())
		{
			return -1;
		}
		return 1;
	}
	
	
	public IResourceMergeReport mergeOperation(ILexicalWords idSncDic,TimeLeftProgress progress) throws DatabaseLoadDriverException, SQLException {
		this.progress = progress;
		this.startTime = GregorianCalendar.getInstance().getTimeInMillis();
		return merge(idSncDic);
	}
	
	public IResourceUpdateReport loadTermFromGenericCVSFileOperation(File file,CSVFileConfigurations csvfileconfigurations,TimeLeftProgress progress) throws DatabaseLoadDriverException, SQLException, IOException {
		this.progress=progress;
		this.startTime = GregorianCalendar.getInstance().getTimeInMillis();
		return loadTermFromGenericCVSFile(file, csvfileconfigurations);
	}
	
	public void updateElement(String oldTerm, String newterm) throws SQLException, DatabaseLoadDriverException
	{
		int elemID = getLexicalWordID(oldTerm);
		if(elemID!=-1)
		{
			updateElement(new ResourceElement(elemID, newterm));
			getLexicalWords().remove(oldTerm);
			getLexicalWordDatabaseID().remove(oldTerm);
			getLexicalWords().add(newterm);
			getLexicalWordDatabaseID().put(newterm, elemID);
		}
		else
		{
			Workbench.getInstance().error("Can not edit element");
		}
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
	public String toString()
	{
		return "ID: "+getID()+" Name: "+getName()+" Info : "+getInfo();
		
	}
}
