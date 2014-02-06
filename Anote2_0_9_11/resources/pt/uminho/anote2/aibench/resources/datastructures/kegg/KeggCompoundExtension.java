package pt.uminho.anote2.aibench.resources.datastructures.kegg;

import java.util.GregorianCalendar;

import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.kegg.KeggCompound;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class KeggCompoundExtension extends KeggCompound{
	
	private boolean cancel;
	private TimeLeftProgress progress;
	private long startTime;	
	
	public KeggCompoundExtension(IDictionary dictionary,boolean cancel,TimeLeftProgress progress) {
		super(dictionary);
		this.cancel=cancel;
		this.progress=progress;	
		this.startTime = GregorianCalendar.getInstance().getTimeInMillis();
	}
	
	protected void memoryAndProgress(int step, int total) {
		System.out.println((GlobalOptions.decimalformat.format((double)step/ (double) total * 100)) + " %...");
		Runtime.getRuntime().gc();
		System.out.println((Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory())/(1024*1024) + " MB ");
		long nowTime = GregorianCalendar.getInstance().getTimeInMillis();
		progress.setProgress((float) step/ (float) total);
		progress.setTime(nowTime-startTime, step, total);
	}
	
	
	public boolean isCancel() {
		return cancel;
	}

	public void setcancel(boolean cancel) {
		this.cancel = cancel;
	}
	
	public void setProgress(TimeLeftProgress progress) {
		this.progress = progress;
	}
	
	public TimeLeftProgress getProgress() {
		return progress;
	}
	
	
	

}
