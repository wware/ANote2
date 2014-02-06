package pt.uminho.anote2.ner.ner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import pt.uminho.anote2.aibench.ner.datastructures.ElementToNer;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.report.processes.INERProcessReport;
import pt.uminho.anote2.datastructures.documents.AnnotatedDocument;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.report.processes.NERProcessReport;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.multithearding.IParallelJob;
import pt.uminho.anote2.datastructures.utils.multithearding.ThreadProcessManager;
import pt.uminho.anote2.ner.ner.multithreading.NERParallelStep;
import pt.uminho.anote2.process.IE.INERProcess;
import es.uvigo.ei.aibench.workbench.Workbench;

public class NERCorpusPipeline extends IEProcess implements INERProcess{

	private NER nerDocumentPipeline;
	private boolean stop = false;
	private ThreadProcessManager multi = new ThreadProcessManager(false);
	private NERProcessReport report = new NERProcessReport(GlobalNames.nerAnote,this);
	private boolean caseSensitive;

	
	public NERCorpusPipeline(ICorpus corpus, String description, String type,ElementToNer elementsToNER,boolean normalization,NER nerDocumentPipeline,boolean caseSensitive) {
		super(corpus, description, type,nerDocumentPipeline.getProperties(elementsToNER.getResourceToNER(),normalization));
		this.nerDocumentPipeline = nerDocumentPipeline;
		this.caseSensitive = caseSensitive;
	}
	
	public INERProcessReport executeCorpusNER(ICorpus corpus) throws SQLException, DatabaseLoadDriverException,Exception {
		if(!stop)
		{
			corpus.registerProcess(this);
			processingParallelNER(report,corpus);
		}
		else
		{
			report.setFinishing(false);
		}
		return report;
	}
	
	private void processingParallelNER(NERProcessReport report, ICorpus corpus) throws InterruptedException, SQLException, DatabaseLoadDriverException {
		int size = corpus.size();
		long startTime = Calendar.getInstance().getTimeInMillis();
		long actualTime,differTime;
		int i=0;
		Iterator<IDocument> itDocs = corpus.iterator();
		while(itDocs.hasNext())
		{
			if(!stop)
			{
				List<Integer> classIdCaseSensative = new ArrayList<Integer>();
				IDocument doc = itDocs.next();
				IAnnotatedDocument annotDoc = new AnnotatedDocument(this, corpus, doc);
				String text = annotDoc.getDocumetAnnotationText();
				if(text==null)
				{
					Logger logger = Logger.getLogger(Workbench.class.getName());
					logger.warn("The article whit id: "+doc.getID()+"not contains abstract ");
				}
				else
				{
					executeNER(corpus, multi,classIdCaseSensative, annotDoc, text,caseSensitive);
				}
				report.incrementDocument();
			}
			else
			{
				report.setFinishing(false);
				break;
			}
		}

		startTime = Calendar.getInstance().getTimeInMillis();
		multi.run();
		while(!multi.isComplete() && !stop) {
			actualTime = Calendar.getInstance().getTimeInMillis();
			differTime = actualTime - startTime;
			if(differTime > 10000 * i) {
				memoryAndProgressAndTime(multi.numberOfCompleteJobs(), size, startTime);
				i++;
			};
		}
		if(stop)
		{
			report.setFinishing(false);
		}
		else
		{
			multi.join();
			List<Object> list = multi.getResults();
			for(Object elem:list)
			{
				if(elem instanceof Integer)
				{
					report.incrementEntitiesAnnotated((Integer) elem);
				}
			}
			actualTime = Calendar.getInstance().getTimeInMillis();
			report.setTime(actualTime-startTime);
		}
	}

	private void executeNER(ICorpus corpus,ThreadProcessManager multi,List<Integer> classIdCaseSensative,IAnnotatedDocument annotDoc,String text,boolean caseSensitive) {
		IParallelJob<Integer> job = new NERParallelStep(nerDocumentPipeline,annotDoc, this, corpus, text, classIdCaseSensative,caseSensitive);
		multi.addJob(job);
	}

	public void stop() {
		stop = true;
		multi.kill();
		report.setFinishing(false);
	}

}
