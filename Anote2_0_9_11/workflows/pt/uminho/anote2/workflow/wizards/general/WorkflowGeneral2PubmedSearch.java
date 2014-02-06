package pt.uminho.anote2.workflow.wizards.general;

import java.util.List;
import java.util.Set;

import javax.swing.JComponent;

import pt.uminho.anote2.aibench.publicationmanager.gui.help.APubmedSeach;
import pt.uminho.anote2.aibench.publicationmanager.gui.help.PubmedSearchOptionsPane;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.process.IR.IIRSearchConfiguration;
import pt.uminho.anote2.workflow.datastructures.WorkflowStep;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class WorkflowGeneral2PubmedSearch extends WizardStandard{
	
	private static final long serialVersionUID = 1L;
	private APubmedSeach pane;
	
	public WorkflowGeneral2PubmedSearch(List<Object> list) {
		super(list);
		initGUI();
		if(list.size()==2)
		{
			fillSettings(list.get(1));
			list.remove(1);
		}
		this.setTitle("WorkFlow Information Retrieval and Extraction - Pubmed Search");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}
	
	
	private void fillSettings(Object object) {
		IIRSearchConfiguration pubmedSearchConfigutration = (IIRSearchConfiguration) object;
		pane.updateInfo(pubmedSearchConfigutration);
	}


	private void initGUI() {
		Set<WorkflowStep> workflowSteps = (Set<WorkflowStep>) this.getParam().get(0);
		if(workflowSteps.contains(WorkflowStep.CreateCorpus))
		{
			setEnableDoneButton(false);
			setEnableNextButton(true);
		}
		else
		{
			setEnableDoneButton(true);
			setEnableNextButton(false);
		}
	}

	@Override
	public JComponent getMainComponent() {
		if(pane ==null)
			pane = new PubmedSearchOptionsPane();
		return pane;
	}

	@Override
	public void goNext() {
		IIRSearchConfiguration conf = pane.getConfiguration();
		if(conf!=null)
		{
			List<Object> list = this.getParam();
			list.add(conf);
			closeView();
			new WorkflowGeneral3CreateCorpus(list);
		}
	}

	@Override
	public void goBack() {
		closeView();
		new WorkflowGeneral1SelectSteps(getParam());
	}

	@Override
	public void done() {
		IIRSearchConfiguration conf = pane.getConfiguration();
		if(conf!=null)
		{
			List<Object> list = this.getParam();
			list.add(conf);
			ParamSpec[] paramsSpec = new ParamSpec[]{
					new ParamSpec("list", List.class,list, null)
			};

			for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
				if (def.getID().equals("operations.generalworkflow")){	
					Workbench.getInstance().executeOperation(def, paramsSpec);
					closeView();
					return;
				}
			}
		}

	}

	@Override
	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Workflow_:_Information_Retrieval_and_Extraction#PubMed_Search";
	}


}
