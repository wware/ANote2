package pt.uminho.anote2.relation.core;

import java.util.GregorianCalendar;
import java.util.Properties;

import pt.uminho.anote2.aibench.utils.timeleft.ISimpleTimeLeft;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.process.IE.re.IRelationModel;
import pt.uminho.gate.process.IGatePosTagger;

public class RelationExtraxtionExtension extends RelationsExtraction{
	
	private ISimpleTimeLeft progress;
	
	public RelationExtraxtionExtension(ICorpus corpus,IIEProcess ieProcess, IGatePosTagger postagger,
			IRelationModel relationModel2, Properties properties,ISimpleTimeLeft progress) {
		super(corpus, ieProcess, postagger, relationModel2, properties);
		this.progress=progress;
		this.progress.setProgress(0);
	}
	
	protected void memoryAndProgressAndTime(int step, int total,long startTime) {
		System.out.println((GlobalOptions.decimalformat.format((double)step/ (double) total * 100)) + " %...");
		Runtime.getRuntime().gc();
		System.out.println((Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory())/(1024*1024) + " MB ");
		long actualTime = GregorianCalendar.getInstance().getTimeInMillis();
		progress.setTime(actualTime-startTime, step, total);
		progress.setProgress((float) step/(float) total);
	}
	

}
