package pt.uminho.anote2.aibench.utils.timeleft;

import java.util.ArrayList;
import java.util.List;


public class TimeLeftProgressSteps extends SimpleTimeLeft implements ITimeLeftProgressSteps{
	
	private String step = "Loading...";
	private float overallProgress = 0.0f; 
	private int currentSteps = 0;
	private List<String> steps;
	private String progressTimeLeft;
	
	public TimeLeftProgressSteps()
	{
		super();
		this.currentSteps = 0;
		this.steps = new ArrayList<String>();
		this.progressTimeLeft = new String();
	}

	public String getStep() {
		return step;
	}
	
	public float getWorkflowProgress() {
		return overallProgress;
	}

	public float getStepProgress() {
		return this.progress;
	}
	
	public void increaseStep()
	{
		if(currentSteps < steps.size())
		{
			progress=0.0f;
			currentSteps++;
			step = "Step :"+currentSteps + "/"+steps.size() + " "+steps.get(currentSteps-1);
			overallProgress = ((float) (currentSteps-1)/(float) steps.size());
		}
	}

	public void setSteps(List<String> steps) {
		this.steps.addAll(steps);
		if(steps.size() > 0)
			this.step = this.steps.get(0);
	}
	
	@Override
	public void setTime(long differTime, int pos, int max) {
		String timeLeft = new String();
		if(pos!=0)
		{
			setProgress((float)pos/(float) max);
			overallProgress = ((float) (currentSteps-1)/(float) steps.size()) + ((float)pos/(float) max)/(float) steps.size();
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
		this.progressTimeLeft=timeLeft;
	}

	@Override
	public String getStepTimeLeft() {
		return progressTimeLeft;
	}



}
