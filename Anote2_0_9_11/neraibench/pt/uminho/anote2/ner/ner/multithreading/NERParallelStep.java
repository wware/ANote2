package pt.uminho.anote2.ner.ner.multithreading;

import java.util.List;

import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.datastructures.annotation.AnnotationPositions;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.utils.multithearding.IParallelJob;
import pt.uminho.anote2.ner.ner.NER;
import pt.uminho.anote2.process.IProcess;

public class NERParallelStep implements IParallelJob<Integer>{

	private String text;
	private List<Integer> classIdCaseSensative;
	private IDocument doc;
	private IProcess process;
	private ICorpus corpus;
	private int entitiesAdded=0;
	private NER ner;
	private boolean stop = false;
	private boolean caseSensitive;
	
	
	public NERParallelStep(NER ner,IDocument doc,IProcess process,ICorpus corpus,String text,List<Integer> classIdCaseSensative,boolean caseSensitive)
	{
		this.ner=ner;
		this.doc=doc;
		this.process=process;
		this.corpus=corpus;
		this.text=text;
		this.classIdCaseSensative = classIdCaseSensative;
		this.caseSensitive = caseSensitive;
	}
	
	public void run() {
		AnnotationPositions annotationsPositions = new AnnotationPositions();
		try {
			annotationsPositions = ner.executeNer(text,classIdCaseSensative,caseSensitive);
			if(!stop)
			{
				IEProcess.insertOnDatabaseEntityAnnotations(doc,process,corpus,annotationsPositions);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		entitiesAdded = annotationsPositions.getAnnotations().size();
		annotationsPositions = null;
	}


	public Integer getResultJob() {
		return entitiesAdded;
	}

	@Override
	public void kill() {
		stop = true;
		ner.stop();	
	}

}
