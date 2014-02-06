package pt.uminho.anote2.corpusloaders.operations;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Properties;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.NERSchema;
import pt.uminho.anote2.aibench.corpus.management.database.CorporaDatabaseManagement;
import pt.uminho.anote2.aibench.corpus.settings.corpora.CorporaDefaultSettings;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IDocumentSet;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.corpusloaders.loaders.ANoteCorpusLoader;
import pt.uminho.anote2.datastructures.documents.publication.DocumentManager;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.process.IE.IIEProcess;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation(enabled=false)
public class CreateCorpusByAnotev1Corpus {
	
	private TimeLeftProgress progress = new TimeLeftProgress("Create Corpus: @Note v1 Files");
	private Corpora project;
	private String description = "@Note V1.0 Corpus";
	private Properties prop;

	@Port(name="project",direction=Direction.INPUT,order=1)
	public void setProject(Corpora project)
	{
		this.project=project;
		this.prop = new Properties();
		this.prop.put(GlobalNames.textType,GlobalNames.fullText);
	}
	
	@Port(name="setDocuments",direction=Direction.INPUT,order=4)
	public void setDocuments(File file)
	{
		System.gc();
		ANoteCorpusLoader loader = new ANoteCorpusLoader(file);
		progress.setTimeString("calculating...");
		progress.setProgress(0);
		String documentype = "anotev1";
		if(loader.validateFile())
		{
			try
			{
				IPublication doc;
				Corpus corpus = project.createCorpus(description,prop);
				IDocumentSet docs = loader.processTextFile();
				IIEProcess ieProcess = new IEProcess(corpus,description,GlobalNames.ner, new Properties());
				corpus.registerProcess(ieProcess);
				Iterator<IDocument> docit = docs.iterator();
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
				while(docit.hasNext() && position < max)
				{
					doc = (IPublication) docit.next();
					DocumentManager.addDocument(doc, documentype);
					corpus.addDocument(doc);
					CorporaDatabaseManagement.insertOnDatabaseAnnotations(ieProcess.getID(),corpus.getID(),doc.getID(),doc);
					doc.setFullTExtOnDatabase( doc.getContent());
					progress.setProgress((float)position/ (float) max);
					endtime = GregorianCalendar.getInstance().getTimeInMillis();
					differTime = endtime-starttime;			
					progress.setTime(differTime, position, max);
					position++;
				}
				corpus.addProcess(new NERSchema(ieProcess.getID(),ieProcess.getCorpus(),
						ieProcess.getName(),ieProcess.getType(),ieProcess.getProperties()));
				project.addCorpus(corpus);
				project.notifyViewObservers();
				new ShowMessagePopup("@Note v1.0 Corpus Created");
			} catch (DatabaseLoadDriverException e) {
				new ShowMessagePopup("@Note v1.0 Corpus Fail");
				TreatExceptionForAIbench.treatExcepion(e);
				return;
			} catch (SQLException e) {
				new ShowMessagePopup("@Note v1.0 Corpus Fail");
				TreatExceptionForAIbench.treatExcepion(e);
				return;
			} catch (IOException e) {
				new ShowMessagePopup("@Note v1.0 Corpus Fail");
				TreatExceptionForAIbench.treatExcepion(e);
				return;
			}
		}
		else
		{
			Workbench.getInstance().warn("Incorrect Directory.");
			return;
		}

		
	}
	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}

}
