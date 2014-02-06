package pt.uminho.anote2.aibench.corpus.datatypes;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Properties;

import pt.uminho.anote2.aibench.corpus.management.aibench.CorporaAIBenchManagement;
import pt.uminho.anote2.aibench.corpus.management.database.CorporaDatabaseManagement;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IDocumentSet;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.database.queries.corpora.QueriesCorpora;
import pt.uminho.anote2.datastructures.documents.DocumentSet;
import pt.uminho.anote2.datastructures.documents.Publication;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.process.IE.IIEProcess;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.ListElements;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import es.uvigo.ei.aibench.workbench.Workbench;

@Datatype(structure = Structure.LIST,namingMethod="getName",setNameMethod="setName",removable=true,renamed=true,autoOpen=true)
public class Corpus extends Observable implements Serializable,ICorpus{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String description;
	private Corpora corpora;
	private List<IIEProcess> processes = null;

	private Properties prop;
	private IDocumentSet docs;
	
	public Corpus(int id,String description,Corpora project,Properties prop)
	{
		this.id=id;
		this.description=description;
		this.corpora=project;
		this.processes = new ArrayList<IIEProcess>();
		this.prop=prop;
		this.docs = null;
	}
	
	public Properties getProperties() {
		return prop;
	}

	public void addDocument(IDocument doc) throws SQLException, DatabaseLoadDriverException
	{
		if(docs==null)
			docs = getArticlesCorpus();
		docs.addDocument(doc.getID(), doc);
		addRelationCorpusDocument(doc.getID());
	}
	
	public void addCorpusProperties(String valueName,String valueProperty) throws SQLException, DatabaseLoadDriverException
	{
		if(!getProperties().containsKey(valueName))
		{
			PreparedStatement insertCorpusProperties = Configuration.getDatabase().getConnection().prepareStatement(QueriesCorpora.insertCorpusProperties);
			insertCorpusProperties.setInt(1, getID());
			insertCorpusProperties.setNString(2,valueName);
			insertCorpusProperties.setNString(3,valueProperty);
			insertCorpusProperties.execute();
			insertCorpusProperties.close();
			getProperties().put(valueName, valueProperty);
		}
	}


	
	private void addRelationCorpusDocument(int docID) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement insertpUBiNcORPUS = Configuration.getDatabase().getConnection().prepareStatement(QueriesCorpora.insertCorpusPublication);
		insertpUBiNcORPUS.setInt(1,this.id);
		insertpUBiNcORPUS.setInt(2,docID);
		insertpUBiNcORPUS.execute();
		insertpUBiNcORPUS.close();
	}
	
	public List<IIEProcess> getProcesses() {
		return processes;
	}


	public void remove(int docID)
	{
		if(docs!=null)
			docs.removeDocument(docID);
	}
	
	public Iterator<IDocument> iterator() throws SQLException, DatabaseLoadDriverException {
		if(docs==null)
			docs = getArticlesCorpus();
		return docs.iterator();
	}
	
	public void addProcess(IIEProcess ieProcess)
	{	
		if(alreadyExist(ieProcess))
		{
			new ShowMessagePopup("Process Already In Clipboard");
		}
		else
		{
			processes.add(ieProcess);
			new ShowMessagePopup("Process Added To Clipboard");
			notifyViewObserver();		
		}

	}
	
	public void registerProcess(IIEProcess ieProcess) throws SQLException, DatabaseLoadDriverException {		
		PreparedStatement registProcess = Configuration.getDatabase().getConnection().prepareStatement(QueriesCorpora.insertCorpusProcess);
		registProcess.setInt(1,this.id);
		registProcess.setInt(2,ieProcess.getID());
		registProcess.execute();
		registProcess.close();
	}

	public boolean alreadyExist(IIEProcess ieProcesses)
	{
		for(IIEProcess ieProcessesExists:processes)
		{
			if(ieProcesses.getID()==ieProcessesExists.getID())
			{
				return true;
			}
		}
		return false;
	}
	
	@ListElements(modifiable=true)
	public List<IIEProcess> getIEProcesses()
	{
		return this.processes;
	}

	public Corpora getCorpora() {
		return corpora;
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}
	
	public String getName()
	{
		return getDescription();
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
			Workbench.getInstance().warn("Corpus Name can not be empty");	
		}
		else
		{
			try {
				CorporaDatabaseManagement.updateCorpusName(getID(),name);
			} catch (SQLException e) {
				Workbench.getInstance().warn("Corpus Renamed fail can not be empty");	
				TreatExceptionForAIbench.treatExcepion(e);
				return;
			} catch (DatabaseLoadDriverException e) {
				Workbench.getInstance().warn("Corpus Renamed fail can not be empty");	
				TreatExceptionForAIbench.treatExcepion(e);
				return;
			}
			this.description = name;
			corpora.notifyViewObservers();
		}
	}
	
	public synchronized IDocumentSet getArticlesCorpus() throws SQLException, DatabaseLoadDriverException
	{
		if(docs == null)
		{
			docs = getArticlesCorpusDB();
		}
		return docs;
	}
	
	synchronized private IDocumentSet getArticlesCorpusDB() throws SQLException, DatabaseLoadDriverException {
		IDocumentSet articles = new DocumentSet();
		PreparedStatement findArticleCorpus = Configuration.getDatabase().getConnection().prepareStatement(QueriesCorpora.selectCorpusPublications);
		findArticleCorpus.setInt(1,getId());
		ResultSet rs = findArticleCorpus.executeQuery();
		while(rs.next())
		{
			IPublication pub = new 	Publication(rs.getInt(1), 
					rs.getString(2),//int pmid
					rs.getString(3),//String title
					rs.getString(4),//String authors
					rs.getString(5),//int date
					rs.getString(6),//String status
					rs.getString(7),//String journal
					rs.getString(8),//String volume
					rs.getString(9),//String issue
					rs.getString(10),//String pages
					rs.getString(11),
					rs.getString(12),//String aBstract
					rs.getBoolean(13));
			articles.addDocument(rs.getInt(1), pub);
		}
		rs.close();
		findArticleCorpus.close();
		return articles;
	}
	
	public List<IIEProcess> getIProcesses() throws SQLException, DatabaseLoadDriverException {
		return  getIProcessesFilterByTypes(null);
	}
	
	
	public List<IIEProcess> getIProcessesFilterByTypes(List<String> types) throws SQLException, DatabaseLoadDriverException {
		List<IIEProcess> processes = new ArrayList<IIEProcess>();
		String findCorpus = QueriesCorpora.selectCorpusProcesses;
		if(types==null||types.size()==0){}
		else
		{
			String filterTypes = " AND (";
			for(String type :types)
			{
				filterTypes = filterTypes + " type='"+type+"' OR";
			}
			filterTypes = filterTypes.substring(0,filterTypes.length()-3);
			findCorpus = findCorpus+filterTypes + ")";
		}
		findCorpus = findCorpus + " ORDER BY idprocesses DESC ";
		PreparedStatement findCorpusProcesses = Configuration.getDatabase().getConnection().prepareStatement(findCorpus);
		PreparedStatement findProcessProperties = Configuration.getDatabase().getConnection().prepareStatement(QueriesCorpora.selectProcessProperties);		
		findCorpusProcesses.setInt(1,getId());
		ResultSet rs = findCorpusProcesses.executeQuery();
		int processID;
		String processDescription,processType;
		while(rs.next())
		{
			Properties prop = new Properties();
			String propertyKey,propertyValue;
			processID = rs.getInt(1);
			processDescription = rs.getString(2);
			processType = rs.getString(3);
			findProcessProperties.setInt(1, processID);
			ResultSet rsProperties = findProcessProperties.executeQuery();
			while(rsProperties.next())
			{
				propertyKey = rsProperties.getString(1);
				propertyValue = rsProperties.getString(2);
				prop.put(propertyKey, propertyValue);
			}
			rsProperties.close();
			IEProcess processIE = new IEProcess(processID,this,processDescription,processType,prop);
			processes.add(processIE);
		}
		rs.close();
		findCorpusProcesses.close();
		findProcessProperties.close();
		return processes;
	}

	

	public int getID() {
		return id;
	}
	
	public IPublication getPublication(int pubID) throws SQLException, DatabaseLoadDriverException
	{
		return (IPublication) docs.getAllDocuments().get(pubID);
	}

	@Override
	public int size() {
		return docs.size();
	}
	
	public void notifyViewObserver()
	{
		this.setChanged();
		this.notifyObservers();
	}
	
	public String toString()
	{
		String result = new String();
		if(description!=null)
			result = description;
		if(id>0)
			result = result + "( "+id+") ";		
		return description;
	}
	
    public void remove(){
    	CorporaAIBenchManagement.removeCorpus(this);
     }

	public void freeMemory() {
		this.id = -1;
		this.description = null;
		this.corpora = null;
		this.processes = null;
		this.prop = null;
		this.docs = null;
	}
	
	
		
}
