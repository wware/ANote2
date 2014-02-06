package pt.uminho.anote2.workflow.wizards.general;

import java.util.List;
import java.util.Set;

import javax.swing.JComponent;

import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.core.corpora.ICorpusCreateConfiguration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.workflow.datastructures.WorkflowStep;
import pt.uminho.anote2.workflow.gui.ACorpusPanel;
import pt.uminho.anote2.workflow.wizards.general.panes.WorkflowGeneralCorpusCreationPane;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class WorkflowGeneral3CreateCorpus extends WizardStandard{
	
	private static final long serialVersionUID = 1L;
	private ACorpusPanel pane;
	
	public WorkflowGeneral3CreateCorpus(List<Object> list) {
		super(list);
		initGUI();
		if(list.size()==3)
		{
			fillSettings(list.get(2));
			list.remove(2);
		}
		this.setTitle("WorkFlow Information Retrieval and Extraction - Create Corpus");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}
	
	
	private void fillSettings(Object object) {
		ICorpusCreateConfiguration conf = (ICorpusCreateConfiguration) object;	
		pane.updataCorpusSettings(conf);
	}


	private void initGUI() {
		Set<WorkflowStep> workflowSteps = (Set<WorkflowStep>) this.getParam().get(0);
		if(workflowSteps.contains(WorkflowStep.NER))
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
			pane = new WorkflowGeneralCorpusCreationPane(true);
		return pane;
	}

	@Override
	public void goNext() {
		ICorpusCreateConfiguration conf = pane.getConfiguration();
		if(conf!=null)
		{
			List<Object> list = this.getParam();
			list.add(conf);
			closeView();
			new WorkFlowGeneral4NER(list);
		}
	}

	@Override
	public void goBack() {
		List<Object> list = this.getParam();
		closeView();
		new WorkflowGeneral2PubmedSearch(list);
	}

	@Override
	public void done() {
		ICorpusCreateConfiguration conf = pane.getConfiguration();
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
		return GlobalOptions.wikiGeneralLink+"Workflow_:_Information_Retrieval_and_Extraction#Create_Corpus";
	}


}
