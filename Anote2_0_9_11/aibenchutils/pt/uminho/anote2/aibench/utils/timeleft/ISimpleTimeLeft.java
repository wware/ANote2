package pt.uminho.anote2.aibench.utils.timeleft;

public interface ISimpleTimeLeft {
	public void setProgress(float total);
	public void setTime(long differTime, int pos, int max);
}
