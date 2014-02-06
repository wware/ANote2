package pt.uminho.anote2.aibench.corpus.datatypes;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Properties;
import java.util.Set;

import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IDocumentSet;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.database.queries.corpora.QueriesCorpora;
import pt.uminho.anote2.datastructures.documents.DocumentSet;
import pt.uminho.anote2.datastructures.documents.Publication;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.process.IProcess;
import pt.uminho.anote2.process.IE.IIEProcess;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.ListElements;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;

@Datatype(structure = Structure.LIST,namingMethod="getName")
public class Corpus extends Observable implements Serializable,ICorpus{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String description;
	private Corpora project;
	private List<IIEProcess> processes = null;
	private Properties prop;
	private IDocumentSet docs;
	
	public Corpus(int id,String description,Corpora project,Properties prop)
	{
		this.id=id;
		this.description=description;
		this.project=project;
		processes = new ArrayList<IIEProcess>();
		this.prop=prop;
		this.docs = null;
	}
	
	public Properties getProperties() {
		return prop;
	}

	public void addDocument(IDocument doc)
	{
		if(docs==null)
			docs = getArticlesCorpus();
		docs.addDocument(doc.getID(), doc);
		addRelationCorpusDocuement(doc.getID());
	}
	
	private void addRelationCorpusDocuement(int docID) {
		Connection con = project.getDb().getConnection();
		try {
			PreparedStatement insertpUBiNcORPUS = con.prepareStatement(QueriesCorpora.insertCorpusPublication);
			insertpUBiNcORPUS.setInt(1,this.id);
			insertpUBiNcORPUS.setInt(2,docID);
			insertpUBiNcORPUS.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void remove(int docID)
	{
		if(docs!=null)
			docs.removeDocument(docID);
	}
	
	public Iterator<IDocument> iterator() {
		if(docs==null)
			docs = getArticlesCorpus();
		return docs.iterator();
	}
	
	public void addProcess(IIEProcess ieProcess)
	{	
		if(alreadyExist(ieProcess))
		{
			
		}
		else
		{
			processes.add(ieProcess);
			notifyViewObserver();		
		}

	}
	
	public void registerProcess(IIEProcess ieProcess) {		
		Connection  conn = getCorpora().getDb().getConnection();	
		try {
			PreparedStatement registProcess = conn.prepareStatement(QueriesCorpora.insertCorpusProcess);
			registProcess.setInt(1,this.id);
			registProcess.setInt(2,ieProcess.getID());
			registProcess.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
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
		return project;
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
	
	public IDocumentSet getArticlesCorpus()
	{
		if(docs == null)
		{
			docs = getArticlesCorpusDB();
		}
		return docs;
	}
	
	private IDocumentSet getArticlesCorpusDB() {
		IDocumentSet articles = new DocumentSet();
		Connection con = getCorpora().getDb().getConnection();
		try {
			PreparedStatement findArticleCorpus = con.prepareStatement(QueriesCorpora.selectCorpusPublications);
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return articles;
	}
	
	public Set<IProcess> getIProcesses(String type) {
		Set<IProcess> processes = new HashSet<IProcess>();
		Connection con = getCorpora().getDb().getConnection();
		
		String findCorpus = QueriesCorpora.selectCorpusProcesses;
		try {	
			if(type==null||type.equals("")){}
			else
			{
				findCorpus = findCorpus+" AND type="+type;
			}			
			PreparedStatement findCorpusProcesses = con.prepareStatement(findCorpus);
			PreparedStatement findProcessProperties = con.prepareStatement(QueriesCorpora.selectProcessProperties);		
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
				IEProcess processIE = new IEProcess(processID,this,processDescription,processType,prop,getCorpora().getDb());
				processes.add(processIE);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return processes;
	}

	public int getID() {
		return id;
	}
	
	public IPublication getPublication(int pubID)
	{
		if(getArticlesCorpus()==null)
		{

		}
		return (IPublication) docs.getAllDocuments().get(pubID);
	}

	@Override
	public int size() {
		if(getArticlesCorpus()==null)
		{
			
		}
		return docs.size();
	}
	
	public void notifyViewObserver()
	{
		this.setChanged();
		this.notifyObservers();
	}
		
}
