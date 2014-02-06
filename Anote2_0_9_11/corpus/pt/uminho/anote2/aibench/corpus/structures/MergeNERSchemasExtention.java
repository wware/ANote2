package pt.uminho.anote2.aibench.corpus.structures;

import java.util.GregorianCalendar;

import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.datastructures.annotation.ner.MergeNERSchemas;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.process.IE.IIEProcess;

public class MergeNERSchemasExtention extends MergeNERSchemas{

	private TimeLeftProgress progress;
	
	public MergeNERSchemasExtention(ICorpus corpus, IIEProcess baseProcess,TimeLeftProgress progress) {
		super(corpus, baseProcess);
		this.progress = progress;
	}
	
	public void memoryAndProgressAndTime(int step, int total, long startTime) {
		System.out.println((GlobalOptions.decimalformat.format((double)step/ (double) total * 100)) + " %...");
		Runtime.getRuntime().gc();
		System.out.println((Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory())/(1024*1024) + " MB ");
		long actualTime = GregorianCalendar.getInstance().getTimeInMillis();
		progress.setTime(actualTime-startTime, step, total);
		progress.setProgress((float) step/(float) total);
	}

}
