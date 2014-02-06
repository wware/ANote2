package pt.uminho.anote2.aibench.resources.datatypes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceMergeReport;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.datastructures.utils.FileHandling;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.generic.CSVFileConfigurations;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;
import pt.uminho.anote2.resource.dictionary.IDictionary;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import es.uvigo.ei.aibench.workbench.Workbench;

@Datatype(structure = Structure.SIMPLE,namingMethod="getNameDic",setNameMethod="setName",removable=true,renamed=true,autoOpen=true)
public class DictionaryAibench extends Dictionary{
	
	private BufferedReader br;
	private boolean cancel;
	private long startTime;
	private TimeLeftProgress progress;
	
	public DictionaryAibench(int id, String name, String info) {
		super(id, name, info);
	}

	public String getNameDic()
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
			this.setChanged();
			this.notifyObservers();
			Workbench.getInstance().warn("Dictionary Name can not be empty");	
		}
		else
		{
			try {
				super.setName(name);
				List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(Dictionaries.class);
				Dictionaries dics = (Dictionaries) items.get(0).getUserData();
				dics.notifyViewObservers();
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			}
		}
	}
	
	public void setInfo(String info)
	{
		super.setInfo(info);
	}
	
	public void notifyViewObservers(){
		this.setChanged();
		this.notifyObservers();
	}
	
	public void setCancel()
	{
		cancel = true;
	}
	
	public IResourceMergeReport mergeOperation(IDictionary dicSnd,Set<Integer> classIDs, TimeLeftProgress progress) throws DatabaseLoadDriverException, SQLException 
	{	
		this.progress = progress;
		this.startTime = GregorianCalendar.getInstance().getTimeInMillis();
		return merge(dicSnd,classIDs);
	}
	
	public IResourceMergeReport mergeOperation(IDictionary dicSnd,TimeLeftProgress progress) throws DatabaseLoadDriverException, SQLException 
	{
		this.progress = progress;
		this.startTime = GregorianCalendar.getInstance().getTimeInMillis();
		return merge(dicSnd);
	}
	
	public IResourceUpdateReport loadTermFromGenericCVSFileOperation(File file,CSVFileConfigurations csvfileconfigurations,TimeLeftProgress progress) throws DatabaseLoadDriverException, SQLException, IOException
	{
		this.progress = progress;
		this.startTime = GregorianCalendar.getInstance().getTimeInMillis();
		return loadTermFromGenericCVSFile(file, csvfileconfigurations);
	}
	
	public void inactiveElementsByClassID(int classID,TimeLeftProgress progress) throws SQLException, DatabaseLoadDriverException 
	{
		this.progress = progress;
		this.startTime = GregorianCalendar.getInstance().getTimeInMillis();
		this.inactiveElementsByClassID(classID);
	}
	
	@Override
	public boolean importCVSFile(IResourceUpdateReport report,File file,CSVFileConfigurations csvfileconfigurations,int nextResourceElementID) throws IOException, SQLException, DatabaseLoadDriverException  {	
		String line;
		if(file==null || !file.exists())
		{
			return false;
		}
		else
		{
			Map<String,Integer> sourceSourceID = new HashMap<String, Integer>();
			int step = 0;
			int total = FileHandling.getFileLines(file);
			String term;
			Set<Integer> classesSnd = this.getClassContent();
			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr);
			while((line = br.readLine())!=null)
			{
				if(cancel)
				{
					break;
				}
				String[] lin = line.split(csvfileconfigurations.getGeneralDelimiter().getValue());
				term = getTerm(lin,csvfileconfigurations);
				nextResourceElementID = processLine(report,csvfileconfigurations, nextResourceElementID,sourceSourceID, classesSnd, term, lin);
				if(step%1000==0)
				{
					memoryAndProgress(step, total);
				}
				step++;
			}		
			return true;
		}
	}
	
	public SortedMap<String,Integer> getTerms(String classe) throws SQLException, DatabaseLoadDriverException {
		SortedMap<String,Integer> terms = new TreeMap<String, Integer>();
		IResourceElementSet<IResourceElement> elems = getTermByClass(classe);
		if(elems==null)
		{
			return terms;
		}
		for(IResourceElement elem:elems.getElements())
		{
			terms.put(elem.getTerm(),elem.getID());
		}
		return terms;	
	}

	protected void memoryAndProgress(int step, int total) {
		long end;
		long totalTime;
		end = GregorianCalendar.getInstance().getTimeInMillis();
		totalTime = end-startTime;
		progress.setTime(totalTime, step, total);
		progress.setProgress((float) step / (float) total);
		super.memoryAndProgress(step, total);
	}

	public int compareTo(DictionaryAibench dic)
	{
		if(this.getID()==dic.getID())
		{
			return 0;
		}
		else if(this.getID()<dic.getID())
		{
			return -1;
		}
		return 1;
	}

	public void removeResourceContent(int classID) throws SQLException, DatabaseLoadDriverException {
			PreparedStatement removeClassContente = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.removeClassContent);
			removeClassContente.setInt(1, getID());
			removeClassContente.setInt(2, classID);
			removeClassContente.execute();
			removeClassContente.close();
	}

	

		
}
