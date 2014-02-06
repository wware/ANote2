package pt.uminho.anote2.aibench.publicationmanager.dataStructures;

import java.util.GregorianCalendar;

import pt.uminho.anote2.aibench.utils.timeleft.ISimpleTimeLeft;
import pt.uminho.anote2.process.ir.pubmed.PubMedCrawl;

public class PubmedCrawlExtention extends PubMedCrawl{

	private ISimpleTimeLeft progress;
	
	public PubmedCrawlExtention(boolean onlyFreeFullText,ISimpleTimeLeft progress) {
		super(onlyFreeFullText);
		this.progress = progress;
	}
	
	@Override
	protected void memoryAndProgress(long start, int step, int total) {
		super.memoryAndProgress(start, step, total);
		long nowTime = GregorianCalendar.getInstance().getTimeInMillis();
		progress.setProgress((float) step/ (float) total);
		progress.setTime(nowTime-start, step, total);
	}


}
