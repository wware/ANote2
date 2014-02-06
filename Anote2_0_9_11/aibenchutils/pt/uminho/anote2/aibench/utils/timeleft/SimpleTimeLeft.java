package pt.uminho.anote2.aibench.utils.timeleft;

public class SimpleTimeLeft implements ISimpleTimeLeft{
	
	protected float progress=0.0f;

	public void setProgress(float total) {
		this.progress = total;
	}

	public void setTime(long differTime, int pos, int max) {
		
	}
}
