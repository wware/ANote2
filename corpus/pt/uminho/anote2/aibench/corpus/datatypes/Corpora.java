package pt.uminho.anote2.aibench.corpus.datatypes;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Properties;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.QueriesGeneral;
import pt.uminho.anote2.datastructures.database.queries.corpora.QueriesCorpora;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.process.IProcess;
import pt.uminho.anote2.process.IE.IIEProcess;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.ListElements;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;

@Datatype(structure = Structure.LIST,namingMethod="getNameProject")
public class Corpora extends Observable implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7424015800573471356L;
	private List<ICorpus> allCorpus;
	private List<ICorpus> corpusAibench;
	private IDatabase db;
	public static String saveDocs = "Docs/";
	
	
	public Corpora(IDatabase db)
	{
		this.db=db;
		this.corpusAibench = new ArrayList<ICorpus>();
		this.allCorpus = null;
	}

	@ListElements(modifiable=true)
	public List<ICorpus> getCorpusSet() {
		return corpusAibench;
	}

	public IDatabase getDb() {
		return db;
	}
	
	public Corpus createCorpus(String description,Properties prop)
	{
		int nextCorpus;
		Connection con = db.getConnection();
		try {
			nextCorpus = HelpDatabase.getNextInsertTableID(db,"corpus");
			PreparedStatement insertCorpus = con.prepareStatement(QueriesCorpora.insertCorpus);
			insertCorpus.setString(1,description);
			insertCorpus.execute();
			PreparedStatement insertCorpusProperties = con.prepareStatement(QueriesCorpora.insertCorpusProperties);
			insertCorpusProperties.setInt(1, nextCorpus);
			for(String pro:prop.stringPropertyNames())
			{
				String value = prop.getProperty(pro);
				insertCorpusProperties.setString(2,pro);
				insertCorpusProperties.setString(3,value);
				insertCorpusProperties.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}	
		return new Corpus(nextCorpus,description,this,prop);
	}
	
	public void addCorpus(ICorpus corpus)
	{
		if(!alreadyExist(corpus))
		{
			this.corpusAibench.add(corpus);
			notifyViewObservers();
		}
	}
	
	private boolean alreadyExist(ICorpus corpus) {
		for(ICorpus cor:corpusAibench)
		{
			if(cor.getID()==corpus.getID())
			{
				return true;
			}
		}
		return false;	
	}

	public void notifyViewObservers()
	{
		this.setChanged();
		this.notifyObservers();
	}
	
	
	public List<ICorpus> getAllCorpus() {
		if(allCorpus==null)
		{
			findCorpus();
		}
		return allCorpus;
	}

	public void findCorpus()
	{
		allCorpus = new ArrayList<ICorpus>();
		Connection con = db.getConnection();	
		try {
			PreparedStatement findCorpus = con.prepareStatement(QueriesCorpora.selectCorpora);
			PreparedStatement findCorpusProperties = con.prepareStatement(QueriesCorpora.selectCorpusProperties);
			ResultSet rs = findCorpus.executeQuery();
			int corpusID;
			String corpusDescription;
			Corpus corpus;
			Properties prop = new Properties();
			while(rs.next())
			{
				corpusID = rs.getInt(1);
				corpusDescription = rs.getString(2);
				findCorpusProperties.setInt(1, corpusID);
				ResultSet rs2 = findCorpusProperties.executeQuery();
				while(rs2.next())
				{
					prop.put(rs2.getString(2), rs2.getString(3));
				}
				corpus = new Corpus(corpusID,corpusDescription,this,prop);
				allCorpus.add(corpus);
				prop = new Properties();
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getNameProject()
	{
		return "Corpora";
	}
	
	public List<IProcess> getListProcess()
	{
		List<IProcess> list = new ArrayList<IProcess>();
		PreparedStatement findProc;
		try {
			findProc = getDb().getConnection().prepareStatement(QueriesCorpora.selectProcesses);
			PreparedStatement finProcProp = getDb().getConnection().prepareStatement(QueriesCorpora.selectProcessProperties);
			ResultSet rs = findProc.executeQuery();
			int id;
			String name,type;
			Properties prop;
			ResultSet rs2;
			while(rs.next())
			{
				id = rs.getInt(1);
				name = rs.getString(2);
				type = rs.getString(3);
				prop = new Properties();
				finProcProp.setInt(1,id);
				rs2 = finProcProp.executeQuery();
				while(rs2.next())
				{
					prop.put(rs2.getString(1), rs2.getString(2));
				}
				IIEProcess process = new IEProcess(id,null,name,type,prop,getDb());
				list.add(process);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static String getColorForClass(int classID,IDatabase db) throws SQLException
	{
		String color = "#0000FF";
		Connection con = db.getConnection();
		if(con==null)
		{
			return color;
		}
		else
		{
			PreparedStatement getcolorPS = con.prepareStatement(QueriesGeneral.selectClassColor);
			getcolorPS.setInt(1, classID);
			ResultSet rs = getcolorPS.executeQuery();
			if(rs.next())
			{
				return rs.getString(1);
			}
		}
		PreparedStatement putcolor = con.prepareStatement(QueriesGeneral.insertClassColor);
		putcolor.setInt(1, classID);
		putcolor.setString(2, color);
		putcolor.execute();	
		return color;
	}

	public void updateCorpus() {
		allCorpus = new ArrayList<ICorpus>();
		findCorpus();
	}	
	
}
