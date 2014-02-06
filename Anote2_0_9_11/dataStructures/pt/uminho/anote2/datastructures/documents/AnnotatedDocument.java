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
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.core.document.ISentence;
import pt.uminho.anote2.datastructures.annotation.AnnotationPosition;
import pt.uminho.anote2.datastructures.annotation.AnnotationPositions;
import pt.uminho.anote2.datastructures.annotation.ner.EntityAnnotation;
import pt.uminho.anote2.datastructures.annotation.re.EventAnnotation;
import pt.uminho.anote2.datastructures.annotation.re.EventProperties;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesAnnotatedDocument;
import pt.uminho.anote2.datastructures.nlptools.OpenNLP;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.textprocessing.TermSeparator;
import pt.uminho.anote2.datastructures.utils.HTMLCodes;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.OtherConfigurations;
import pt.uminho.anote2.process.IProcess;


public class AnnotatedDocument extends Publication implements IAnnotatedDocument{

	private List<IEntityAnnotation> entities;
	private List<IEventAnnotation> events;
	private IProcess process;
	private ICorpus corpus;
	private List<ISentence> sentences;
	
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

	public AnnotatedDocument(IProcess reProcess, ICorpus corpus,IDocument document) {
		
		super(document.getID(),(IPublication) document);
		this.corpus=corpus;
		this.process=reProcess;
	}

	public List<IEntityAnnotation> getEntitiesAnnotations() throws SQLException, DatabaseLoadDriverException {
		if(entities==null)
		{
			entities = loadEntities();
		}
		return entities;
	}


	public List<IEventAnnotation> getEventAnnotations() throws SQLException, DatabaseLoadDriverException {
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
	
	public AnnotationPositions getEntitiesAnnotationsOrderByOffset() throws SQLException, DatabaseLoadDriverException
	{
		AnnotationPositions annotations = new AnnotationPositions();
		List<IEntityAnnotation> ent = getEntitiesAnnotations();
		for(int i=0;i<ent.size();i++)
		{
			IEntityAnnotation entity = ent.get(i);
			AnnotationPosition pos = new AnnotationPosition((int)entity.getStartOffset(),(int)entity.getEndOffset());
			annotations.addAnnotationWhitConflicts(pos, entity);
		}
		return annotations;
	}

	private List<IEventAnnotation> loadEvents() throws SQLException, DatabaseLoadDriverException {
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

	private void getEventProperties(Map<Integer, IEventAnnotation> events2) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement getAllAnnotationsPS;
		getAllAnnotationsPS = Configuration.getDatabase().getConnection().prepareStatement(QueriesAnnotatedDocument.selectEventProperties);
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
		rs.close();
		getAllAnnotationsPS.close();
	}

	private void getEventEntities(Map<Integer, IEntityAnnotation> annotationsHash, Map<Integer, IEventAnnotation> events2) throws SQLException, DatabaseLoadDriverException {

		PreparedStatement getAllAnnotationsPS;
		getAllAnnotationsPS =  Configuration.getDatabase().getConnection().prepareStatement(QueriesAnnotatedDocument.selectEventEntitiesAnnotations);
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
		rs.close();
		getAllAnnotationsPS.close();

	}

	private Map<Integer, IEventAnnotation> getEvents() throws DatabaseLoadDriverException, SQLException {
		Map<Integer, IEventAnnotation> eventsAnnot = new HashMap<Integer, IEventAnnotation>();
		Connection conn = Configuration.getDatabase().getConnection();
		IEventAnnotation entAnnot;	
		PreparedStatement getAllAnnotationsPS;
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
			entAnnot = new EventAnnotation(id, start, end,GlobalNames.re,new ArrayList<IEntityAnnotation>(),new ArrayList<IEntityAnnotation>(), clue,-1,classification,new EventProperties());
			eventsAnnot.put(id, entAnnot);
		}
		rs.close();
		getAllAnnotationsPS.close();	
		return eventsAnnot;
	}

	public IProcess getProcess() {
		return process;
	}
	
	private List<IEntityAnnotation> loadEntities() throws SQLException, DatabaseLoadDriverException
	{
		List<IEntityAnnotation> entitiesAnnot = new ArrayList<IEntityAnnotation>();
		IEntityAnnotation entAnnot;
		PreparedStatement getAllAnnotationsPS;
		getAllAnnotationsPS =  Configuration.getDatabase().getConnection().prepareStatement(QueriesAnnotatedDocument.selectEntityAnnotations);
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
		rs.close();
		getAllAnnotationsPS.close();	
		return entitiesAnnot;
	}

	public ICorpus getCorpus() {
		return corpus;
	}

	public List<ISentence> getSentencesText() throws SQLException, DatabaseLoadDriverException {
		if(sentences==null)
		{
			OpenNLP openNLP = new OpenNLP();
			sentences =  openNLP.getSentencesText(getDocumetAnnotationText());
		}
		return sentences;
	}
	
	public String getDocumetAnnotationText() throws SQLException, DatabaseLoadDriverException {	
		String text = getTExt();
		boolean normalization = false;
		if(((IEProcess)getProcess()).getProperties().containsKey(GlobalNames.normalization))
		{
			if(Boolean.valueOf(((IEProcess)getProcess()).getProperties().getProperty(GlobalNames.normalization)))
			{
				normalization = true;
			}
		}
		if(normalization)
		{
			text = TermSeparator.termSeparator(text);
		}
		return text;
	}
	
	public String getDocumentAnnotationTextHTML() throws SQLException, DatabaseLoadDriverException
	{
		String text = getDocumetAnnotationText();
		text = HTMLCodes.textToHTML(text);
		return text;
	}

	private String getTExt() throws SQLException, DatabaseLoadDriverException {
		if(corpus.getProperties().get(GlobalNames.textType).equals(GlobalNames.abstracts))
		{
			if(OtherConfigurations.getUsingTitleInAbstract() && getTitle()!=null && getTitle().length() > 0)
			{
				return getAbstractSection() + " " + getTitle();
			}
			else
			{
				return getAbstractSection();
			}
		}
		else if(corpus.getProperties().get(GlobalNames.textType).equals(GlobalNames.fullText))
		{
			return findFullTextArticle();
		}
		else if(corpus.getProperties().get(GlobalNames.textType).equals(GlobalNames.abstractOrFullText))
		{
			String fullTExt = findFullTextArticle();;
			if(findFullTextArticle() != null && findFullTextArticle().length()>0)
			{
				return fullTExt;
			}
			else
			{
				if(OtherConfigurations.getUsingTitleInAbstract() && getTitle()!=null && getTitle().length() > 0)
				{
					return getAbstractSection() + " " + getTitle();
				}
				else
				{
					return getAbstractSection();
				}
			}
		}
		return new String();
	}
	
	public void setEntities(List<IEntityAnnotation> entities) {
		this.entities = entities;
	}

	public void setEvents(List<IEventAnnotation> events) {
		this.events = events;
	}

	public void setProcess(IProcess process) {
		this.process = process;
	}

	public void setCorpus(ICorpus corpus) {
		this.corpus = corpus;
	}

	public void setSentences(List<ISentence> sentences) {
		this.sentences = sentences;
	}
	

}
