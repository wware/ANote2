package pt.uminho.anote2.aibench.resources.datatypes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import pt.uminho.anote2.aibench.utils.operations.TimeLeftProgress;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;
import pt.uminho.anote2.resource.dictionary.IDictionary;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;

@Datatype(structure = Structure.SIMPLE,namingMethod="getNameDic",setNameMethod="setNameDic")
public class DictionaryAibench extends Dictionary{
	
	private int termsMergeAdding = 0;
	private int termsMergeSynonymsAdding = 0;
	private int classsesMergeAdding = 0;
	
	public DictionaryAibench(IDatabase db, int id, String name, String info) {
		super(db, id, name, info);
	}

	public String getNameDic()
	{
		return getName();
	}
	
	public void setNameDic(String name)
	{
		setName(name);
	}
	
	public void setInfo(String info)
	{
		super.setInfo(info);
	}
	
	public void notifyViewObservers(){
		this.setChanged();
		this.notifyObservers();
	}
	
	public SortedMap<Integer,String> getDicionaryContentClasses()
	{
		SortedMap<Integer,String> classSize = new TreeMap<Integer, String>();
		Connection conn = getDb().getConnection();
		if(conn==null)
		{
			return null;
		}	
		try {		
			PreparedStatement getclassContentPS = conn.prepareStatement(QueriesResources.selectResourceclassContent);
			getclassContentPS.setInt(1,getId());
			ResultSet rs = getclassContentPS.executeQuery();
			while(rs.next())
			{
				classSize.put(rs.getInt(1), rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return classSize;
	}


	public SortedMap<String,Integer> getTerms(String classe) {
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
	
	public int getnumberTerms()
	{	
		Connection conn = getDb().getConnection();	
		try {
			PreparedStatement totalTermsDic = conn.prepareStatement(QueriesResources.totalResourceTerms);
			totalTermsDic.setInt(1,getId());
			ResultSet rsResultSet = totalTermsDic.executeQuery();
			if(rsResultSet.next())
			{
				return rsResultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int getNumberSynonyms()
	{		
		Connection conn = getDb().getConnection();	
		try {
			PreparedStatement totalTermsDic = conn.prepareStatement(QueriesResources.totalResourceSyn);
			totalTermsDic.setInt(1,getId());
			ResultSet rsResultSet = totalTermsDic.executeQuery();
			if(rsResultSet.next())
			{
				return rsResultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public Object[] getClassContentStats(int classID)
	{		
		Object[] obj = new Object[3];
		obj[0] = classID;
		Connection conn = getDb().getConnection();
		try {
			PreparedStatement totalTermsDic = conn.prepareStatement(QueriesResources.totalResourceClassTerms);
			totalTermsDic.setInt(1,getId());
			totalTermsDic.setInt(2,classID);
			ResultSet rsResultSet = totalTermsDic.executeQuery();
			if(rsResultSet.next())
			{
				obj[1] = rsResultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			PreparedStatement totalTermsDic = conn.prepareStatement(QueriesResources.totalResourceClassSyn);
			totalTermsDic.setInt(1,getId());
			totalTermsDic.setInt(2,classID);
			ResultSet rsResultSet = totalTermsDic.executeQuery();
			if(rsResultSet.next())
			{
				obj[2] = rsResultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return obj;
	}
	
	public boolean merge(IDictionary dicSnd,TimeLeftProgress progress,boolean cancel) 
	{	
		setTermsMergeAdding(0);
		termsMergeSynonymsAdding = 0;
		classsesMergeAdding = 0;
		Connection connection = getDb().getConnection();	
		if(connection==null)
		{
			return false;
		}
		else
		{
			progress.setTimeString("Load Terms Destinary Dictionary");
			loadAllTerms();
			int nextResourceElementID = HelpDatabase.getNextInsertTableID(this.getDb(),"resource_elements");
			progress.setTimeString("Load Terms Source Dictionary");
			IResourceElementSet<IResourceElement> terms = dicSnd.getResourceElements();
			Set<Integer> classesSnd = dicSnd.getClassContent();
			Set<Integer> classes1st = getClassContent();
			Map<Integer, String> classesDB = new HashMap<Integer, String>();
			try {
				classesDB = getClassIDClassOnDatabase(getDb());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			classsesMergeAdding = classesSnd.size();
			for(Integer classID:classesSnd)
			{
				if(!classes1st.contains(classID))
				{
					this.addResourceContent(classesDB.get(classID));
				}
			}
			int total = terms.size();
			int step = 0;
			long start = GregorianCalendar.getInstance().getTimeInMillis();
			long end;
			long totalTime;
			for(IResourceElement elem:terms.getElements())
			{
				if(this.addElement(elem))
				{
					IResourceElementSet<IResourceElement> syns = getTermSynomns(elem.getID());
					setTermsMergeAdding(getTermsMergeAdding() + 1);
					for(IResourceElement elem2:syns.getElements())
					{
						addSynomns(new ResourceElement(nextResourceElementID,elem2.getTerm(),-1,""));
						termsMergeSynonymsAdding++;
					}
					this.addExternalID(nextResourceElementID, elem.getExtenalID(),elem.getExternalIDSourceID());
					nextResourceElementID++;
				}
				else
				{
					this.addExternalID(nextResourceElementID, elem.getExtenalID(),elem.getExternalIDSourceID());
				}
				if(step%500==0)
				{
					end = GregorianCalendar.getInstance().getTimeInMillis();
					totalTime = end-start;
					progress.setTime(totalTime, step, total);
					progress.setProgress((float) step / (float) total);
				}
				step++;
			}
			deleteTerms();
			return true;
		}
	}
	
	public int compareTo(DictionaryAibench dic)
	{
		if(this.getId()==dic.getId())
		{
			return 0;
		}
		else if(this.getId()<dic.getId())
		{
			return -1;
		}
		return 1;
	}

	public int getTermsMergeSynonymsAdding() {
		return termsMergeSynonymsAdding;
	}

	public void setTermsMergeSynonymsAdding(int termsMergeSynonymsAdding) {
		this.termsMergeSynonymsAdding = termsMergeSynonymsAdding;
	}

	public int getClasssesMergeAdding() {
		return classsesMergeAdding;
	}

	public void setClasssesMergeAdding(int classsesMergeAdding) {
		this.classsesMergeAdding = classsesMergeAdding;
	}

	public int getTermsMergeAdding() {
		return termsMergeAdding;
	}

	public void setTermsMergeAdding(int termsMergeAdding) {
		this.termsMergeAdding = termsMergeAdding;
	}
	
	
		
}
