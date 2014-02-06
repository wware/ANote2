package pt.uminho.anote2.relation.cooccurrence.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.annotation.IEventProperties;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IDocumentSet;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.core.document.ISentence;
import pt.uminho.anote2.core.report.processes.IREProcessReport;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesAnnotatedDocument;
import pt.uminho.anote2.datastructures.documents.AnnotatedDocument;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.report.processes.REProcessReport;
import pt.uminho.anote2.datastructures.textprocessing.NormalizationForm;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.process.IE.IREProcess;

public abstract class ARECooccurrence extends IEProcess implements IREProcess{
	
	private Properties properties;
	private ICorpus corpus;
	private IEProcess nerProcess;
	private PreparedStatement insertAnnot;
	private PreparedStatement insertEventAnnot;
	private PreparedStatement insertAnnotSide;
	private PreparedStatement insertEventProperty;
	private Map<Integer, Integer> idNEwIDoldProcess;
	private TimeLeftProgress progress;
	private boolean stop = false;
	private IREProcessReport report;
	private int nextAnnotationID;
	
	
	public abstract List<IEventAnnotation> processDocumetAnnotations(List<IEntityAnnotation> listEntitiesSortedByOffset, IDocument doc) throws SQLException, DatabaseLoadDriverException,Exception;
	
	
	public ARECooccurrence(ICorpus corpus,IEProcess nerProcess,Properties properties,TimeLeftProgress progress) throws SQLException, DatabaseLoadDriverException
	{
		super(corpus, GlobalNames.relationCooccurrence,GlobalNames.re, properties);
		this.progress=progress;
		this.corpus=corpus;
		this.nerProcess=nerProcess;
		this.setProperties(properties);
		this.insertAnnot = Configuration.getDatabase().getConnection().prepareStatement(QueriesAnnotatedDocument.insertEntityAnnotation);
		insertAnnot.setInt(1,this.getID());
		insertAnnot.setInt(2,corpus.getID());
		insertEventAnnot = Configuration.getDatabase().getConnection().prepareStatement(QueriesAnnotatedDocument.insertEventAnnotation);
		insertAnnotSide = Configuration.getDatabase().getConnection().prepareStatement(QueriesAnnotatedDocument.insertAnnotationSide);
		insertEventAnnot.setInt(1,this.getID());
		insertEventAnnot.setInt(2,corpus.getID());
		insertEventProperty =Configuration.getDatabase().getConnection().prepareStatement(QueriesAnnotatedDocument.insertAnnotationProperties);
	}
	
	public IREProcessReport executeRE() throws DatabaseLoadDriverException, SQLException,Exception
	{
		nextAnnotationID = HelpDatabase.getNextInsertTableID(GlobalTablesName.annotations);
		report  = new REProcessReport(GlobalNames.relationCooccurrence,nerProcess,this);
		long start = GregorianCalendar.getInstance().getTimeInMillis();
		int size = corpus.getArticlesCorpus().getAllDocuments().size();
		long starttime = GregorianCalendar.getInstance().getTimeInMillis();
		int position = 0;
		IDocumentSet docs = corpus.getArticlesCorpus();
		Iterator<IDocument> itDocs =docs.iterator();
		while(itDocs.hasNext())
		{
			report.incrementDocument();
			if(stop)
			{
				report.setcancel();
				break;
			}
			idNEwIDoldProcess = new HashMap<Integer, Integer>();
			IDocument doc = itDocs.next();
			IAnnotatedDocument annotDOc = new AnnotatedDocument(nerProcess, corpus, doc);
			List<IEventAnnotation> events = processDocumetAnnotations(annotDOc.getEntitiesAnnotations(),doc);
			insertEventAnnotationInDatabase(report,doc.getID(),annotDOc.getEntitiesAnnotations(),events);
			position++;
			long actualTime = GregorianCalendar.getInstance().getTimeInMillis();
			progress.setTime(actualTime-starttime, position, size);
			progress.setProgress((float) position/(float) size);
		}
		long end = GregorianCalendar.getInstance().getTimeInMillis();
		report.setTime(end-start);
		return report;
	}
	
	private void insertEventAnnotationInDatabase(IREProcessReport report, int documentID,List<IEntityAnnotation> semanticLayer,List<IEventAnnotation> relations) throws SQLException {
		insertEntityAnnotationsDB(documentID,semanticLayer);
		insertRelationsDB(documentID,relations);
		report.incrementEntitiesAnnotated(semanticLayer.size());
		report.increaseRelations(relations.size());
	}
	
	private void insertRelationsDB(int documentID, List<IEventAnnotation> relations) throws SQLException {
		List<IEntityAnnotation> left,right;
		IEventProperties prop;
		for(IEventAnnotation event:relations)
		{
			if(stop)
			{
				report.setcancel();
				break;
			}
			insertEventAnnot.setInt(3,documentID);
			insertEventAnnot.setInt(4, (int) event.getStartOffset());
			insertEventAnnot.setInt(5, (int) event.getEndOffset());
			insertEventAnnot.setNull(6,1);
			insertEventAnnot.setNull(7,1);
			insertEventAnnot.setNull(8,1);
			insertEventAnnot.setNull(9,1);
			insertEventAnnot.setNull(10,1);
			insertEventAnnot.setNString(11,event.getEventClue());
			insertEventAnnot.execute();
			left = event.getEntitiesAtLeft();
			right = event.getEntitiesAtRight();
			insertentitiesAtLeft(left);
			insertentitiesAtRight(right);
			prop = event.getEventProperties();
			insertRelationsProperties(prop);
			nextAnnotationID++;
		}
	}
	
	private void insertRelationsProperties(IEventProperties prop) throws SQLException {
		insertEventProperty.setInt(1,nextAnnotationID);
		if(prop.getLemma()!=null && !stop)
		{
			insertEventProperty.setNString(2,GlobalNames.relationPropertyLemma);
			insertEventProperty.setNString(3,prop.getLemma());
			insertEventProperty.execute();
		}
		if(prop.getDirectionally()!=null && !stop)
		{
			insertEventProperty.setNString(2,GlobalNames.relationPropertyDirectionally);
			insertEventProperty.setNString(3,String.valueOf(prop.getDirectionally().databaseValue()));
			insertEventProperty.execute();
		}
		if(prop.getPolarity()!=null && !stop)
		{
			insertEventProperty.setNString(2,GlobalNames.relationPropertyPolarity);
			insertEventProperty.setNString(3,String.valueOf(prop.getPolarity().databaseValue()));
			insertEventProperty.execute();
		}	
	}

	private void insertentitiesAtLeft(List<IEntityAnnotation> left) throws SQLException {
		for(IEntityAnnotation annotation:left)
		{
			if(stop)
			{
				report.setcancel();
				break;
			}
			insertAnnotSide.setInt(1,nextAnnotationID);
			insertAnnotSide.setInt(2,idNEwIDoldProcess.get(annotation.getID()));
			insertAnnotSide.setInt(3,1);
			insertAnnotSide.execute();	
		}
	}
	
	private void insertentitiesAtRight(List<IEntityAnnotation> left) throws SQLException {
		for(IEntityAnnotation annotation:left)
		{
			if(stop)
			{
				report.setcancel();
				break;
			}
			insertAnnotSide.setInt(1,nextAnnotationID);
			insertAnnotSide.setInt(2,idNEwIDoldProcess.get(annotation.getID()));
			insertAnnotSide.setInt(3,2);
			insertAnnotSide.execute();	
		}
		
	}

	private void insertEntityAnnotationsDB(int documentID, List<IEntityAnnotation> semanticLayer) throws SQLException {
		insertAnnot.setInt(3,documentID);
		for(IEntityAnnotation ent:semanticLayer)
		{
			if(stop)
			{
				report.setcancel();
				break;
			}
			insertAnnot.setLong(4,ent.getStartOffset());
			insertAnnot.setLong(5,ent.getEndOffset());
			insertAnnot.setNString(6,ent.getAnnotationValue());
			if(ent.getResourceElementID()>0)
			{
				insertAnnot.setInt(7,ent.getResourceElementID());
			}
			else
			{
				insertAnnot.setNull(7,1);
			}
			insertAnnot.setNString(8,NormalizationForm.getNormalizationForm(ent.getAnnotationValue()));
			insertAnnot.setInt(9,ent.getClassAnnotationID());
			insertAnnot.execute();
			idNEwIDoldProcess.put(ent.getID(),nextAnnotationID);
			nextAnnotationID++;
		}
	}
	
	protected List<ISentence> getSentencesLimits(IDocument doc) throws SQLException, DatabaseLoadDriverException {
		IPublication pub = (IPublication) doc;
		IAnnotatedDocument annotDoc = new AnnotatedDocument(pub.getID(),pub, this, getCorpus());
		return annotDoc.getSentencesText();
	}

	protected List<IEntityAnnotation> getSentenceEntties(List<IEntityAnnotation> listEntitiesSortedByOffset,ISentence sentence) {
		List<IEntityAnnotation> result = new ArrayList<IEntityAnnotation>();
		for(IEntityAnnotation ent:listEntitiesSortedByOffset)
		{
			if(ent.getStartOffset()>sentence.getStartOffset() && ent.getEndOffset()<sentence.getEndOffset())
			{
				result.add(ent);
			}
		}
		return result;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public void stop() {
		stop = true;
	}


	
}
