package pt.uminho.anote2.corpusloaders.operations;

import java.io.File;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Properties;

import pt.uminho.anote2.aibench.corpus.databasemanagement.CorporaManagement;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.operations.TimeLeftProgress;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IDocumentSet;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.corpusloaders.loaders.AIMEDProteinCorpusLoader;
import pt.uminho.anote2.datastructures.documents.Publication;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.process.IE.IIEProcess;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation()
public class CreateCorpusByAIMEDProteinCorpus {
	
	private TimeLeftProgress progress = new TimeLeftProgress();
	private Corpora project;
	private String description = "AIMED Protein Corpus (Original)";
	private Properties prop;

	@Port(name="project",direction=Direction.INPUT,order=1)
	public void setProject(Corpora project)
	{
		this.project=project;
		this.prop = new Properties();
		this.prop.put("textType","abstract");
	}
	
	@Port(name="setDocuments",direction=Direction.INPUT,order=4)
	public void setDocuments(File file) throws SQLException
	{
		AIMEDProteinCorpusLoader loader = new AIMEDProteinCorpusLoader(project.getDb(),file);
		progress.setTimeString("calculating...");
		progress.setProgress(0);	
		if(loader.validateFile())
		{
			IPublication doc,doc2;
			Corpus corpus = project.createCorpus(description,prop);
			IDocumentSet docs = loader.processTextFile();
			IIEProcess ieProcess = new IEProcess(corpus,description,"NER", prop, project.getDb());
			corpus.registerProcess(ieProcess);
			corpus.addProcess(ieProcess);
			Iterator<IDocument> docit = docs.iterator();
			int position = 0;
			int max = docs.size();
			long starttime = GregorianCalendar.getInstance().getTimeInMillis();
			long endtime,differTime;
			while(docit.hasNext())
			{
				doc = (IPublication) docit.next();
				int docID = CorporaManagement.insertPublication(project.getDb(),doc);
				doc2 = new Publication(docID, doc);
				corpus.addDocument(doc2);
				CorporaManagement.insertOnDatabaseAnnotations(project.getDb(),ieProcess.getID(),corpus.getID(),docID,doc);
				progress.setProgress((float)position/ (float) max);
				endtime = GregorianCalendar.getInstance().getTimeInMillis();
				differTime = endtime-starttime;			
				progress.setTime(differTime, position, max);
				position++;
			}	
		}
		else
		{
			Workbench.getInstance().warn("Incorrect Directory!!!");
			return;
		}
		project.notifyViewObservers();
		new ShowMessagePopup("Corpus AIMED Proteins Created");
		
	}
	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}
	

}
