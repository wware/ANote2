package pt.uminho.anote2.aibench.utils.timeleft;



public class TimeLeftProgress extends SimpleTimeLeft implements ITimeLeftProgress{
	
	private String time;
	private String name;

	public TimeLeftProgress(String name)
	{
		super();
		this.time=new String();
		this.name = name;
	}
	
	public String getTimeLeft() {
		return time;
	}
	
	public void setTimeString(String information)
	{
		this.time=information;
	}
	
	public String getProcess()
	{
		return this.name;
	}

	@Override
	public void setTime(long differTime, int pos, int max) {
		String timeLeft = new String();
		if(pos!=0)
		{
			
			float seconds = differTime/(float)1000;
			float eachSeconds = seconds/(float)pos;
			float timeL = eachSeconds*(max-pos); 
			int minutes = (int)(timeL/60+0.25);
			if(minutes>59)
			{
				minutes = minutes % 60;
			}
			int hour = (int) timeL/3600 ;
			if(minutes<=0 && hour==0)
			{
				timeLeft = timeLeft.concat("less than 1 minute");
				setProgress((float)pos/(float) max);
			}
			else{
				
				timeLeft = timeLeft.concat(hour+"h "+minutes+"m ");
				setProgress((float)pos/(float) max);
			}
		}
		else
		{
			timeLeft = timeLeft.concat("oo");
		}
		this.time=timeLeft;
	}

	public float getProgress() {
		return this.progress;
	}

}
