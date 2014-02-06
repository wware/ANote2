package pt.uminho.anote2.aibench.utils.timeleft;

import java.util.List;

public interface ITimeLeftProgressSteps extends ISimpleTimeLeft{
	public String getStepTimeLeft();
	public void increaseStep();
	public float getWorkflowProgress();
	public float getStepProgress();
	public String getStep();
	public void setSteps(List<String> steps);
}
