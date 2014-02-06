package pt.uminho.anote2.workflow.gui;

import java.util.Set;

import javax.swing.JPanel;

import pt.uminho.anote2.workflow.datastructures.WorkflowStep;

public abstract class AWorkflowSelectionPane extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public abstract Set<WorkflowStep> getWorkFlowSteps();
	public abstract void setWorkFlowSteps(Set<WorkflowStep> steps) ;
	
}
