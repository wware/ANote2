package pt.uminho.anote2.datastructures.process;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Properties;

import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IDocumentSet;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesProcess;
import pt.uminho.anote2.datastructures.documents.AnnotatedDocument;
import pt.uminho.anote2.process.IE.IIEProcess;

public class IEProcess extends Observable implements IIEProcess {
	
	private String description;
	private int id;
	private Properties properties;
	private String type;
	private IDatabase db;
	private ICorpus corpus;

	public IEProcess(ICorpus corpus,String description,String type,Properties properties,IDatabase db)
	{
		this.db=db;
		this.id=registerNERProcess(description,type, properties);
		this.description=description;
		this.type=type;
		this.properties=properties;
		this.corpus=corpus;
	}
	public IEProcess(int id,ICorpus corpus,String description,String type,Properties properties,IDatabase db)
	{
		this.db=db;
		this.id=id;
		this.description=description;
		this.type=type;
		this.properties=properties;
		this.corpus=corpus;
	}
	
	protected int registerNERProcess(String description,String type, Properties properties) {
		int processID = IEProcess.insertProcessType(type,this.db);
		if(processID==-1)
		{
			return -1;
		}
		return IEProcess.insertProcess(description,processID, properties, db);
	}

	
	public String getDescription() {
		return this.description;
	}

	public int getID() {
		return this.id;
	}

	public Properties getProperties() {
		return this.properties;
	}

	public IDocumentSet getDocuments() {
		return corpus.getArticlesCorpus();
	}
	
	public static int insertProcessType(String processType,IDatabase database)
	{
		Connection conn = database.getConnection();	
		if(conn==null)
		{
			return -1;
		}
		try {
			PreparedStatement prep = conn.prepareStatement(QueriesProcess.existProcessType);
			prep.setString(1,processType);
			ResultSet rs = prep.executeQuery();
			if(rs.next())
			{
				return rs.getInt(1);
			}	
			PreparedStatement insType = conn.prepareStatement(QueriesProcess.insertProcessType);
			insType.setString(1, processType);
			insType.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}	
		return HelpDatabase.getNextInsertTableID(database,"processes_type")-1;
	}
	
	public static int insertProcess(String designation,int processTypeID,Properties properties, IDatabase database)
	{
		Connection conn = database.getConnection();	
		try {
			PreparedStatement prep = conn.prepareStatement(QueriesProcess.insertProcess);
			prep.setInt(1,processTypeID);
			prep.setString(2,designation);
			int processID = HelpDatabase.getNextInsertTableID(database,"processes");
			prep.execute();
			insertProcessProperties(processID,properties,database);
			return processID;

		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		return -1;
	}

	private static void insertProcessProperties(int processID,Properties properties, IDatabase database) {
		
		Connection conn = database.getConnection();
		PreparedStatement insertP;
		try {
			insertP = conn.prepareStatement(QueriesProcess.insertProcessProperties);

			for(String key :properties.stringPropertyNames())
			{
				String value = properties.getProperty(key);
				insertP.setInt(1, processID);
				insertP.setString(2, key);
				insertP.setString(3, value);
				insertP.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public String getType() {

		return this.type;
	}
	
	public int compareTo(IEProcess process)
	{
		if(this.id==process.getID())
		{
			return 0;
		}
		else if(this.id<process.getID())
		{
			return -1;
		}
		else
		{
			return 1;
		}
	}
	
	public Map<Integer,IEntityAnnotation> getAllEntities() {
		Map<Integer,IEntityAnnotation> entities = new HashMap<Integer, IEntityAnnotation>();
		Iterator<IDocument> annotDocsIt = corpus.getArticlesCorpus().iterator();
		while(annotDocsIt.hasNext())
		{
			IAnnotatedDocument annotDoc = (IAnnotatedDocument) annotDocsIt.next();
			List<IEntityAnnotation> annots = annotDoc.getEntitiesAnnotations();
			for(IEntityAnnotation annot:annots)
			{
				entities.put(annot.getID(), annot);
			}
		}
		return entities;
	}
	
	public Map<Integer,IEventAnnotation> getAllEvents() {
		Map<Integer,IEventAnnotation> events = new HashMap<Integer, IEventAnnotation>();
		Iterator<IDocument> annotDocsIt = corpus.getArticlesCorpus().iterator();
		while(annotDocsIt.hasNext())
		{
			IPublication annotDoc = (IPublication) annotDocsIt.next();
			IAnnotatedDocument annotatedDocument = new AnnotatedDocument(this, corpus, annotDoc);
			List<IEventAnnotation> annots = annotatedDocument.getEventAnnotations();
			for(IEventAnnotation annot:annots)
			{
				events.put(annot.getID(), annot);
			}
		}
		return events;
	}	
	
	public String toString()
	{
		String all = new String();
		all = getID() + "-" + getDescription() + "(" + getType() + ")" ;
		return all;
	}
	
	public int getId() {
		return id;
	}
	
	public IDatabase getDB() {
		return db;
	}

	public ICorpus getCorpus() {
		return corpus;
	}


}
