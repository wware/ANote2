package pt.uminho.anote2.workflow.wizards.query;

import java.util.List;
import java.util.Set;

import javax.swing.JComponent;

import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.workflow.datastructures.WorkflowStep;
import pt.uminho.anote2.workflow.gui.AWorkflowSelectionPane;
import pt.uminho.anote2.workflow.gui.wizard.panes.SelectionQueryPane;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class WorkflowQuery2SelectSteps extends WizardStandard{
	
	private static final long serialVersionUID = 1L;
	private AWorkflowSelectionPane pane;
	
	public WorkflowQuery2SelectSteps(List<Object> param) {
		super(param);
		initGUI();
		if(param.size()==2)
		{
			fill(param.get(1));
			param.remove(1);
		}
		this.setTitle("WorkFlow Information Extraction (From Query) - Select Steps");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}
	
	
	private void fill(Object object) {
		Set<WorkflowStep> steps = (Set<WorkflowStep>) object;
		pane.setWorkFlowSteps(steps);
	}


	private void initGUI() {
		setEnableDoneButton(false);
		setEnableNextButton(true);
		setEnableBackButton(true);
	}

	@Override
	public JComponent getMainComponent() {
		if(pane ==null)
			pane = new SelectionQueryPane();
		return pane;
	}

	@Override
	public void goNext() {
		Set<WorkflowStep> steps = pane.getWorkFlowSteps();
		if(steps.size() > 0)
		{	
			List<Object> param = this.getParam();
			param.add(steps);
			closeView();
			new WorkflowQuery3CreateCorpus(param);
		}
		else
		{
			Workbench.getInstance().warn("Please select at least one step");
		}
	}

	@Override
	public void goBack() {
		closeView();
		new WorkflowQuery1SelectPublications(getParam());
	}

	@Override
	public void done() {

	}

	@Override
	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Workflow_:_Information_Extraction_From_Query#Select_Steps";
	}


}
