package pt.uminho.anote2.corpusloaders.operations;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.NERSchema;
import pt.uminho.anote2.aibench.corpus.datatypes.RESchema;
import pt.uminho.anote2.aibench.corpus.management.database.CorporaDatabaseManagement;
import pt.uminho.anote2.aibench.corpus.settings.corpora.CorporaDefaultSettings;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IDocumentSet;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.corpusloaders.loaders.GeniaEventCorpusLoader;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesAnnotatedDocument;
import pt.uminho.anote2.datastructures.documents.publication.DocumentManager;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.process.IE.IIEProcess;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;


@Operation(enabled=false)
public class CreateCorpusByGeniaEventCorpus {
	
	private TimeLeftProgress progress = new TimeLeftProgress("Create Corpus: Genia Event Files");
	private Corpora project;
	private String description = "Genia Event Corpus";
	private Properties prop;
	
	@Port(name="project",direction=Direction.INPUT,order=1)
	public void setProject(Corpora project)
	{
		this.project=project;
		this.prop = new Properties();
		this.prop.put(GlobalNames.textType,GlobalNames.abstracts);
	}
	
	@Port(name="setDocuments",direction=Direction.INPUT,order=4)
	public void setDocuments(File file)
	{
		System.gc();
		GeniaEventCorpusLoader loader = new GeniaEventCorpusLoader(file);
		progress.setTimeString("calculating...");
		progress.setProgress(0);	
		if(loader.validateFile())
		{
			try
			{
				IPublication doc;
				String documentype = "geniaevent";
				Corpus corpus = project.createCorpus(description,prop);
				IDocumentSet docs = loader.processTextFile();
				Runtime.getRuntime().gc();
				IIEProcess ieProcess = new IEProcess(corpus,description,GlobalNames.ner, new Properties());
				IIEProcess reProcess = new IEProcess(corpus,description,GlobalNames.re, new Properties());
				corpus.registerProcess(ieProcess);
				corpus.registerProcess(reProcess);
				int position = 0;
				int max = docs.size();
				int maxCorpusValue = Integer.valueOf(PropertiesManager.getPManager().getProperty(CorporaDefaultSettings.CORPUS_SIZE_LIMIT).toString());
				if( maxCorpusValue != -1)
				{
					if(maxCorpusValue < max)
						max = maxCorpusValue;
				}
				long starttime = GregorianCalendar.getInstance().getTimeInMillis();
				long endtime,differTime;
				Iterator<IDocument> docit = docs.iterator();
				while(docit.hasNext() && position < max)
				{
					doc = (IPublication) docit.next();
					DocumentManager.addDocument(doc, documentype);
					corpus.addDocument(doc);
					processNERProcess(doc, corpus, ieProcess, doc.getID());
					processREProcess(doc, corpus, reProcess, doc.getID());
					progress.setProgress((float)position/ (float) max);
					endtime = GregorianCalendar.getInstance().getTimeInMillis();
					differTime = endtime-starttime;
					progress.setTime(differTime, position, max);
					position++;
				}
				corpus.addProcess(new NERSchema(ieProcess.getID(),ieProcess.getCorpus(),
						ieProcess.getName(),ieProcess.getType(),ieProcess.getProperties()));
				corpus.addProcess(new RESchema(reProcess.getID(),reProcess.getCorpus(),
						reProcess.getName(),reProcess.getType(),reProcess.getProperties()));
				project.addCorpus(corpus);
			} catch (DatabaseLoadDriverException e) {
				new ShowMessagePopup("Corpus AIMED Proteins Fail");
				TreatExceptionForAIbench.treatExcepion(e);
				return;
			} catch (SQLException e) {
				new ShowMessagePopup("Corpus AIMED Proteins Fail");
				TreatExceptionForAIbench.treatExcepion(e);
				return;
			} catch (IOException e) {
				new ShowMessagePopup("Corpus AIMED Proteins Fail");
				TreatExceptionForAIbench.treatExcepion(e);
				return;
			}
		}
		else
		{
			Workbench.getInstance().warn("Incorrect Directory.");
			return;
		}
		project.notifyViewObservers();
		new ShowMessagePopup("Genia Event Corpus Created");

	}

	private void processREProcess(IPublication doc, Corpus corpus, IIEProcess ieProcess, int docID) throws SQLException, DatabaseLoadDriverException {
		IAnnotatedDocument docAnnot = (IAnnotatedDocument) doc;
		List<IEventAnnotation> events = docAnnot.getEventAnnotations();
		int eventAnnotationID;
		Map<String,Integer> offsetPositionIDEntity = new HashMap<String, Integer>();
		List<IEntityAnnotation> left,right;
		PreparedStatement insertAnnot = null;
		insertAnnot = Configuration.getDatabase().getConnection().prepareStatement(QueriesAnnotatedDocument.insertEntityAnnotation);
		insertAnnot.setInt(1,ieProcess.getID());
		insertAnnot.setInt(2,corpus.getId());
		insertAnnot.setInt(3,docID);
		PreparedStatement insertAnnotSide = Configuration.getDatabase().getConnection().prepareStatement(QueriesAnnotatedDocument.insertAnnotationSide);
		for(IEventAnnotation event:events)
		{
			CorporaDatabaseManagement.insertEventOnDataBase(corpus.getID(),ieProcess.getID(),docID,event);
			eventAnnotationID = HelpDatabase.getNextInsertTableID(GlobalTablesName.annotations)-1;
			left = event.getEntitiesAtLeft();
			right = event.getEntitiesAtRight();
			insertAnnotSide.setInt(1,eventAnnotationID);
			insertentitiesAtLeft(insertAnnot,insertAnnotSide,eventAnnotationID,left,offsetPositionIDEntity);
			insertentitiesAtRight(insertAnnot,insertAnnotSide,eventAnnotationID,right,offsetPositionIDEntity);
		}
		insertAnnot.close();
		insertAnnotSide.close();
	}

	private void insertentitiesAtRight(PreparedStatement stat,PreparedStatement stat2,int eventAnnotationID,
			List<IEntityAnnotation> right, Map<String, Integer> offsetPositionIDEntity) throws SQLException, DatabaseLoadDriverException {
		int entID;
		String pair;
		for(IEntityAnnotation annotation:right)
		{
			pair = String.valueOf(annotation.getStartOffset())+ String.valueOf(annotation.getEndOffset());
			if(offsetPositionIDEntity.containsKey(pair))
			{
				entID = offsetPositionIDEntity.get(pair);
				CorporaDatabaseManagement.insertEvenEntitySide(stat2,entID,2);
			}
			else
			{
				CorporaDatabaseManagement.insertEntityAnnotation(stat,annotation);
				entID = HelpDatabase.getNextInsertTableID(GlobalTablesName.annotations)-1;
				offsetPositionIDEntity.put(pair, entID);
				CorporaDatabaseManagement.insertEvenEntitySide(stat2,entID,2);
			}
		}
	}

	private void insertentitiesAtLeft(PreparedStatement stat,PreparedStatement stat2,int eventAnnotationID,
			List<IEntityAnnotation> left, Map<String, Integer> offsetPositionIDEntity) throws SQLException, DatabaseLoadDriverException {
		int entID;
		String pair;
		for(IEntityAnnotation annotation:left)
		{
			pair = String.valueOf(annotation.getStartOffset())+ String.valueOf(annotation.getEndOffset());
			if(offsetPositionIDEntity.containsKey(pair))
			{
				entID = offsetPositionIDEntity.get(pair);
				CorporaDatabaseManagement.insertEvenEntitySide(stat2,entID,1);
			}
			else
			{
				CorporaDatabaseManagement.insertEntityAnnotation(stat,annotation);
				entID = HelpDatabase.getNextInsertTableID(GlobalTablesName.annotations)-1;
				offsetPositionIDEntity.put(pair, entID);
				CorporaDatabaseManagement.insertEvenEntitySide(stat2,entID,1);
			}
		}
		
	}

	private void processNERProcess(IPublication doc, Corpus corpus,IIEProcess ieProcess, int docID) throws SQLException, DatabaseLoadDriverException {
		CorporaDatabaseManagement.insertOnDatabaseAnnotations(ieProcess.getID(),corpus.getID(),docID,doc);
	}
	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}

}
