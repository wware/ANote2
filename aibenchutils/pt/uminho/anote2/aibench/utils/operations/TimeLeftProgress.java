package pt.uminho.anote2.aibench.utils.operations;

import pt.uminho.anote2.aibench.utils.operations.OperationProgress;

public class TimeLeftProgress extends OperationProgress{
	private String time;

	public TimeLeftProgress()
	{
		super();
		this.time=new String();
	}
	
	public String getTime() {
		return time;
	}
	
	public void setTimeString(String information)
	{
		this.time=information;
	}

	public void setTime(long differTime, int pos, int max) {
		String timeLeft = new String();
		if(pos!=0)
		{
			
			float seconds = differTime/(float)1000;
			float eachSeconds = seconds/(float)pos;
			float timeL = eachSeconds*(max-pos); 
			int minutes = (int)timeL/60;
			if(minutes>59)
			{
				minutes = minutes % 60;
			}
			int hour = (int) timeL/3600 ;
			if(minutes<=0 && hour==0)
			{
				timeLeft = timeLeft.concat("Time Left : <1m");
				setProgress((float)pos/(float) max);
			}
			else{
				
				timeLeft = timeLeft.concat("Time Left : "+hour+"h "+minutes+"m ");
				setProgress((float)pos/(float) max);
			}
		}
		else
		{
			timeLeft = timeLeft.concat("Time Left : oo");
		}
		this.time=timeLeft;
	}

}
