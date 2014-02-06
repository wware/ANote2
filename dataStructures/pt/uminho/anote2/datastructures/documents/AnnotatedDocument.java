package pt.uminho.anote2.datastructures.documents;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.annotation.EntityAnnotation;
import pt.uminho.anote2.datastructures.annotation.EventAnnotation;
import pt.uminho.anote2.datastructures.annotation.EventProperties;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesAnnotatedDocument;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.process.IProcess;


public class AnnotatedDocument extends Publication implements IAnnotatedDocument{

	private List<IEntityAnnotation> entities;
	private List<IEventAnnotation> events;
	private IProcess process;
	private ICorpus corpus;
	
	public AnnotatedDocument(int id) {
		super(id);
	}
	
	public AnnotatedDocument(int id,IPublication pub,IProcess process,ICorpus corpus) {
		super(id,pub);
		this.corpus=corpus;
		this.process=process;
		{
			events=null;
			entities=null;
		}
	}
	
	public AnnotatedDocument(int id,IPublication pub,IProcess process,ICorpus corpus,List<IEntityAnnotation> entities,int corpusID) 
	{
		super(id,pub);
		this.corpus=corpus;
		this.entities=entities;
		this.events=null;
	}
	
	public AnnotatedDocument(int id,IPublication pub,IProcess process,ICorpus corpus,List<IEntityAnnotation> entities,List<IEventAnnotation> events,int corpusID) 
	{
		super(id,pub);
		this.corpus=corpus;
		this.entities=entities;
		this.events=events;
	}

	public AnnotatedDocument(IEProcess reProcess, ICorpus corpus,IDocument document) {
		
		super(document.getID(),(IPublication) document);
		this.corpus=corpus;
		this.process=reProcess;
	}

	public List<IEntityAnnotation> getEntitiesAnnotations() {
		if(entities==null)
		{
			entities = loadEntities();
		}
		return entities;
	}


	public List<IEventAnnotation> getEventAnnotations() {
		if(entities==null)
		{
			entities = loadEntities();
		}
		if(events==null)
		{
			events = loadEvents();
		}
		return this.events;
	}

	private List<IEventAnnotation> loadEvents() {
		Map<Integer,IEntityAnnotation> annotationsHash = new HashMap<Integer, IEntityAnnotation>();
		for(IEntityAnnotation ent:entities)
		{
			annotationsHash.put(ent.getID(),ent);
		}
		Map<Integer,IEventAnnotation> events = getEvents();
		getEventEntities(annotationsHash,events);
		getEventProperties(events);
		return new ArrayList<IEventAnnotation>(events.values());
	}

	private void getEventProperties(Map<Integer, IEventAnnotation> events2) {
		
		Connection conn = process.getDB().getConnection();
		if(conn==null)
		{
			return;
		}	
		PreparedStatement getAllAnnotationsPS;
		try {
			getAllAnnotationsPS = conn.prepareStatement(QueriesAnnotatedDocument.selectEventProperties);
			getAllAnnotationsPS.setInt(1,process.getID());
			getAllAnnotationsPS.setInt(2,corpus.getID());
			getAllAnnotationsPS.setInt(3,getID());
			ResultSet rs = getAllAnnotationsPS.executeQuery();
			int id;
			String key,value;
			while(rs.next())
			{
				id = rs.getInt(1);
				key = rs.getString(2);
				value = rs.getString(3);
				events2.get(id).addEventProperty(key, value);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	private void getEventEntities(Map<Integer, IEntityAnnotation> annotationsHash, Map<Integer, IEventAnnotation> events2) {
		
		Connection conn = process.getDB().getConnection();
		if(conn==null)
		{
			return;
		}	
		PreparedStatement getAllAnnotationsPS;
		try {
			getAllAnnotationsPS = conn.prepareStatement(QueriesAnnotatedDocument.selectEventEntitiesAnnotations);
			getAllAnnotationsPS.setInt(1,process.getID());
			getAllAnnotationsPS.setInt(2,corpus.getID());
			getAllAnnotationsPS.setInt(3,getID());
			ResultSet rs = getAllAnnotationsPS.executeQuery();
			int id,entityID;
			String lor;
			while(rs.next())
			{
				id = rs.getInt(1);
				entityID = rs.getInt(2);
				lor = rs.getString(3);
				if(lor.equals("left"))
				{
					events2.get(id).addEntityAtLeft(annotationsHash.get(entityID));
				}
				else
				{
					events2.get(id).addEntityAtRight(annotationsHash.get(entityID));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	private Map<Integer, IEventAnnotation> getEvents() {
		Map<Integer, IEventAnnotation> eventsAnnot = new HashMap<Integer, IEventAnnotation>();
		Connection conn = process.getDB().getConnection();
		IEventAnnotation entAnnot;
		if(conn==null)
		{
			return null;
		}	
		PreparedStatement getAllAnnotationsPS;
		try {
			getAllAnnotationsPS = conn.prepareStatement(QueriesAnnotatedDocument.selectEventAnnotations);
			getAllAnnotationsPS.setInt(1,process.getID());
			getAllAnnotationsPS.setInt(2,corpus.getID());
			getAllAnnotationsPS.setInt(3,getID());
			ResultSet rs = getAllAnnotationsPS.executeQuery();
			int id;
			long start,end;
			String clue,classification;
			while(rs.next())
			{
				id = rs.getInt(1);
				start = rs.getLong(2);
				end = rs.getLong(3);
				clue = rs.getString(4);
				classification = rs.getString(5);
				entAnnot = new EventAnnotation(id, start, end,"RE",new ArrayList<IEntityAnnotation>(),new ArrayList<IEntityAnnotation>(), clue,-1,classification,new EventProperties());
				eventsAnnot.put(id, entAnnot);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return eventsAnnot;
	}

	public IProcess getProcess() {
		return process;
	}
	
	private List<IEntityAnnotation> loadEntities()
	{
		List<IEntityAnnotation> entitiesAnnot = new ArrayList<IEntityAnnotation>();
		Connection conn = process.getDB().getConnection();
		IEntityAnnotation entAnnot;
		if(conn==null)
		{
			return null;
		}
	
		PreparedStatement getAllAnnotationsPS;
		try {
			getAllAnnotationsPS = conn.prepareStatement(QueriesAnnotatedDocument.selectEntityAnnotations);
			getAllAnnotationsPS.setInt(1,process.getID());
			getAllAnnotationsPS.setInt(2,corpus.getID());
			getAllAnnotationsPS.setInt(3,getID());
			ResultSet rs = getAllAnnotationsPS.executeQuery();
			int id,start,end,classID,resourceID;
			String value,normValue;
			while(rs.next())
			{
				id = rs.getInt(1);
				start = rs.getInt(2);
				end = rs.getInt(3);
				value = rs.getString(4);
				resourceID = rs.getInt(5);
				normValue = rs.getString(6);
				classID = rs.getInt(7);
				entAnnot = new EntityAnnotation(id, start, end, classID,resourceID, value,normValue);
				entitiesAnnot.add(entAnnot);		
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return entitiesAnnot;
	}

	public ICorpus getCorpus() {
		return corpus;
	}
}
