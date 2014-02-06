package pt.uminho.anote2.workflow.wizards.general;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;

import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.workflow.datastructures.WorkflowStep;
import pt.uminho.anote2.workflow.gui.AWorkflowSelectionPane;
import pt.uminho.anote2.workflow.gui.wizard.panes.SelectionGeneralPane;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class WorkflowGeneral1SelectSteps extends WizardStandard{
	
	private static final long serialVersionUID = 1L;
	private AWorkflowSelectionPane pane;
	
	public WorkflowGeneral1SelectSteps() {
		super(new ArrayList<Object>());
		initGUI();
		this.setTitle("WorkFlow Information Retrieval and Extraction - Select Steps");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}
	
	public WorkflowGeneral1SelectSteps(List<Object> list) {
		super(new ArrayList<Object>());
		initGUI();
		fill(list.get(0));
		this.setTitle("WorkFlow Information Retrieval and Extraction - Select Steps");
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
		setEnableBackButton(false);
	}

	@Override
	public JComponent getMainComponent() {
		if(pane ==null)
			pane = new SelectionGeneralPane();
		return pane;
	}

	@Override
	public void goNext() {
		Set<WorkflowStep> steps = pane.getWorkFlowSteps();
		if(steps.size() > 0)
		{	
			List<Object> param = new ArrayList<Object>();
			param.add(steps);
			closeView();
			new WorkflowGeneral2PubmedSearch(param );
		}
		else
		{
			Workbench.getInstance().warn("Please select at least one step");
		}
	}

	@Override
	public void goBack() {}

	@Override
	public void done() {}

	@Override
	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Workflow_:_Information_Retrieval_and_Extraction#Select_Steps";
	}


}
