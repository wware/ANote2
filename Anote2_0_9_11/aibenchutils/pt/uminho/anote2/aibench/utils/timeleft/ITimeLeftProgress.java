package pt.uminho.anote2.aibench.utils.timeleft;

public interface ITimeLeftProgress extends ISimpleTimeLeft{
	public String getTimeLeft();
	public float getProgress();
	public void setTimeString(String information);
	public void setTime(long differTime, int pos, int max);

}
