package pt.uminho.anote2.workflow.wizards.query;

import java.util.List;
import java.util.Set;

import javax.swing.JComponent;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.core.corpora.ICorpusCreateConfiguration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.workflow.datastructures.WorkflowStep;
import pt.uminho.anote2.workflow.gui.ACorpusPanel;
import pt.uminho.anote2.workflow.wizards.query.pane.WorkflowQueryCorpusCreationPane;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class WorkflowQuery3CreateCorpus extends WizardStandard{
	
	private static final long serialVersionUID = 1L;
	private ACorpusPanel pane;
	
	public WorkflowQuery3CreateCorpus(List<Object> list) {
		super(list);
		initGUI();
		if(list.size()==3)
		{
			fill(list.get(2));
			list.remove(2);
		}
		this.setTitle("WorkFlow Information Extraction (From Query) - Create Corpus");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}
	
	
	private void fill(Object object) {
		ICorpusCreateConfiguration conf = (ICorpusCreateConfiguration) object;	
		pane.updataCorpusSettings(conf);
	}


	private void initGUI() {
		Set<WorkflowStep> workflowSteps = (Set<WorkflowStep>) this.getParam().get(1);
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
			pane = new WorkflowQueryCorpusCreationPane(true);
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
			new WorkFlowQuery4NER(list);
		}
	}

	@Override
	public void goBack() {
		List<Object> list = this.getParam();
		closeView();
		new WorkflowQuery2SelectSteps(list);
	}

	@Override
	public void done() {
		ICorpusCreateConfiguration conf = pane.getConfiguration();
		if(conf!=null)
		{
			List<Object> list = this.getParam();
			list.add(conf);
			Object obj = HelpAibench.getSelectedItem(QueryInformationRetrievalExtension.class);
			QueryInformationRetrievalExtension query = (QueryInformationRetrievalExtension) obj;
			ParamSpec[] paramsSpec = new ParamSpec[]{
					new ParamSpec("query", QueryInformationRetrievalExtension.class,query, null),
					new ParamSpec("options", List.class,list, null),			
			};

			for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
				if (def.getID().equals("operations.queryworkflow")){	
					Workbench.getInstance().executeOperation(def, paramsSpec);
					closeView();
					return;
				}
			}
		}
	}

	@Override
	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Workflow_:_Information_Extraction_From_Query#Create_Corpus";
	}


}
