package pt.uminho.anote2.corpusloaders.operations;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import pt.uminho.anote2.aibench.corpus.databasemanagement.CorporaManagement;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.operations.TimeLeftProgress;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IDocumentSet;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.corpusloaders.loaders.GeniaEventCorpusLoader;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesAnnotatedDocument;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;
import pt.uminho.anote2.datastructures.documents.Publication;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.process.IE.IIEProcess;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;


@Operation()
public class CreateCorpusByGeniaEventCorpus {
	
	private TimeLeftProgress progress = new TimeLeftProgress();
	private Corpora project;
	private String description = "Genia Event Corpus";
	private Properties prop;
	
	@Port(name="project",direction=Direction.INPUT,order=1)
	public void setProject(Corpora project)
	{
		this.project=project;
		this.prop = new Properties();
		this.prop.put("textType","full text");
	}
	
	@Port(name="setDocuments",direction=Direction.INPUT,order=4)
	public void setDocuments(File file) throws SQLException
	{
		GeniaEventCorpusLoader loader = new GeniaEventCorpusLoader(project.getDb(),file);
		progress.setTimeString("calculating...");
		progress.setProgress(0);	
		if(loader.validateFile())
		{
			PreparedStatement add_pub_stat = project.getDb().getConnection().prepareStatement(QueriesPublication.insertPublications);
			HashMap<String, Integer> tablesSize = CorporaManagement.getDbColumnsInfo(project.getDb().getConnection());
			IPublication doc,doc2;
			Corpus corpus = project.createCorpus(description,prop);
			IDocumentSet docs = loader.processTextFile();
			Runtime.getRuntime().gc();
			IIEProcess ieProcess = new IEProcess(corpus,description,"NER", prop, project.getDb());
			IIEProcess reProcess = new IEProcess(corpus,description,"RE", prop, project.getDb());
			corpus.registerProcess(ieProcess);
			corpus.registerProcess(reProcess);
			corpus.addProcess(ieProcess);
			corpus.addProcess(reProcess);
			int position = 0;
			int max = docs.size();
			long starttime = GregorianCalendar.getInstance().getTimeInMillis();
			long endtime,differTime;
			Iterator<IDocument> docit = docs.iterator();
			while(docit.hasNext())
			{
				doc = (IPublication) docit.next();
				int docID = CorporaManagement.insertPublicationLowMemory(project.getDb(),doc,add_pub_stat,tablesSize);
				doc2 = new Publication(docID, doc);
				corpus.addDocument(doc2);
				processNERProcess(doc, corpus, ieProcess, docID);
				processREProcess(doc, corpus, reProcess, docID);
				doc2.setFullTExtOnDatabase(project.getDb(), doc.getContent());
				progress.setProgress((float)position/ (float) max);
				endtime = GregorianCalendar.getInstance().getTimeInMillis();
				differTime = endtime-starttime;
				progress.setTime(differTime, position, max);
				position++;
				Runtime.getRuntime().gc();
			}	
		}
		else
		{
			Workbench.getInstance().warn("Incorrect Directory!!!");
			return;
		}
		project.notifyViewObservers();
		new ShowMessagePopup("Genia Event Corpus Created");
		
	}

	private void processREProcess(IPublication doc, Corpus corpus, IIEProcess ieProcess, int docID) throws SQLException {
		IAnnotatedDocument docAnnot = (IAnnotatedDocument) doc;
		List<IEventAnnotation> events = docAnnot.getEventAnnotations();
		int eventAnnotationID;
		Map<String,Integer> offsetPositionIDEntity = new HashMap<String, Integer>();
		List<IEntityAnnotation> left,right;
		PreparedStatement insertAnnot = null;
		insertAnnot = project.getDb().getConnection().prepareStatement(QueriesAnnotatedDocument.insertEntityAnnotation);
		insertAnnot.setInt(1,ieProcess.getID());
		insertAnnot.setInt(2,corpus.getId());
		insertAnnot.setInt(3,docID);
		PreparedStatement insertAnnotSide = project.getDb().getConnection().prepareStatement(QueriesAnnotatedDocument.insertAnnotationSide);
		for(IEventAnnotation event:events)
		{
			CorporaManagement.insertEventOnDataBase(project.getDb(),corpus.getID(),ieProcess.getID(),docID,event);
			eventAnnotationID = HelpDatabase.getNextInsertTableID(project.getDb(),"annotations")-1;
			left = event.getEntitiesAtLeft();
			right = event.getEntitiesAtRight();
			insertAnnotSide.setInt(1,eventAnnotationID);
			insertentitiesAtLeft(insertAnnot,insertAnnotSide,eventAnnotationID,left,offsetPositionIDEntity);
			insertentitiesAtRight(insertAnnot,insertAnnotSide,eventAnnotationID,right,offsetPositionIDEntity);
		}
	}

	private void insertentitiesAtRight(PreparedStatement stat,PreparedStatement stat2,int eventAnnotationID,
			List<IEntityAnnotation> right, Map<String, Integer> offsetPositionIDEntity) throws SQLException {
		int entID;
		String pair;
		for(IEntityAnnotation annotation:right)
		{
			pair = String.valueOf(annotation.getStartOffset())+ String.valueOf(annotation.getEndOffset());
			if(offsetPositionIDEntity.containsKey(pair))
			{
				entID = offsetPositionIDEntity.get(pair);
				CorporaManagement.insertEvenEntitySide(stat2,entID,2);
			}
			else
			{
				CorporaManagement.insertEntityAnnotation(stat,annotation);
				entID = HelpDatabase.getNextInsertTableID(project.getDb(), "annotations")-1;
				offsetPositionIDEntity.put(pair, entID);
				CorporaManagement.insertEvenEntitySide(stat2,entID,2);
			}
		}
	}

	private void insertentitiesAtLeft(PreparedStatement stat,PreparedStatement stat2,int eventAnnotationID,
			List<IEntityAnnotation> left, Map<String, Integer> offsetPositionIDEntity) throws SQLException {
		int entID;
		String pair;
		for(IEntityAnnotation annotation:left)
		{
			pair = String.valueOf(annotation.getStartOffset())+ String.valueOf(annotation.getEndOffset());
			if(offsetPositionIDEntity.containsKey(pair))
			{
				entID = offsetPositionIDEntity.get(pair);
				CorporaManagement.insertEvenEntitySide(stat2,entID,1);
			}
			else
			{
				CorporaManagement.insertEntityAnnotation(stat,annotation);
				entID = HelpDatabase.getNextInsertTableID(project.getDb(), "annotations")-1;
				offsetPositionIDEntity.put(pair, entID);
				CorporaManagement.insertEvenEntitySide(stat2,entID,1);
			}
		}
		
	}

	private void processNERProcess(IPublication doc, Corpus corpus,IIEProcess ieProcess, int docID) {
		CorporaManagement.insertOnDatabaseAnnotations(project.getDb(),ieProcess.getID(),corpus.getID(),docID,doc);
	}
	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}

}
