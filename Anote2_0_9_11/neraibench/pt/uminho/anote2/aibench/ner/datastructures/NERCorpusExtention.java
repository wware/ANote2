package pt.uminho.anote2.aibench.ner.datastructures;

import java.util.GregorianCalendar;

import pt.uminho.anote2.aibench.utils.timeleft.ISimpleTimeLeft;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.ner.ner.NER;
import pt.uminho.anote2.ner.ner.NERCorpusPipeline;

public class NERCorpusExtention extends NERCorpusPipeline{
	
	private ISimpleTimeLeft progress;
	
	public NERCorpusExtention(ICorpus corpus, String description, String type, ElementToNer elementsToNER,boolean normalization,NER nerDocumentPipeline,boolean caseSensitive,ISimpleTimeLeft progress) {
		super(corpus, description, type,elementsToNER,normalization,nerDocumentPipeline,caseSensitive);
		this.progress = progress;
	}
	
	protected void memoryAndProgressAndTime(int step, int total,long startTime) {
		System.out.println((GlobalOptions.decimalformat.format((double)step/ (double) total * 100)) + " %...");
		System.gc();
		System.out.println((Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory())/(1024*1024) + " MB ");	
		long differTime = GregorianCalendar.getInstance().getTimeInMillis() - startTime;
		progress.setTime(differTime , step, total);
		progress.setProgress((float) step/ (float) total);
	}

}
