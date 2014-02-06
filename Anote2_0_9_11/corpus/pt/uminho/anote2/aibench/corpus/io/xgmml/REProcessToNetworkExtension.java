package pt.uminho.anote2.aibench.corpus.io.xgmml;

import java.util.GregorianCalendar;

import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.datastructures.process.re.export.network.export.REProcessToNetwork;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;

public class REProcessToNetworkExtension extends REProcessToNetwork{
	
	private TimeLeftProgress progress;
	
	
	public REProcessToNetworkExtension(TimeLeftProgress progress)
	{
		this.progress = progress;
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
