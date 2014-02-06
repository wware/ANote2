package pt.uminho.anote2.workflow.wizards.query;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.process.IE.ner.INERConfiguration;
import pt.uminho.anote2.workflow.datastructures.WorkflowStep;
import pt.uminho.anote2.workflow.gui.ANERPanel;
import pt.uminho.anote2.workflow.wizards.query.pane.WorkflowQueryNERPane;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class WorkFlowQuery4NER extends WizardStandard{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ANERPanel pane;
	
	public WorkFlowQuery4NER(List<Object> list) {
		super(list);
		initGUI();
		this.setTitle("WorkFlow Information Extraction (From Query) - Selecting NER Process");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}
	


	private void initGUI() {
		Set<WorkflowStep> workflowSteps = (Set<WorkflowStep>) this.getParam().get(1);
		if(workflowSteps.contains(WorkflowStep.RE))
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
			try {
				if(getParam().size() == 4)
				{
					pane = new WorkflowQueryNERPane((INERConfiguration) getParam().get(3));
					getParam().remove(3);
				}
				else
				{
					pane = new WorkflowQueryNERPane();
				}
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			}
		return pane;
	}

	@Override
	public void goNext() {
		INERConfiguration conf;
		try {
			conf = pane.getConfiguration(null);
			if(conf!=null)
			{
				List<Object> list = this.getParam();
				list.add(conf);
				closeView();
				new WorkflowQuery5RE(list);
			}
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		}

	}

	@Override
	public void goBack() {
		List<Object> list = this.getParam();
		closeView();
		new WorkflowQuery3CreateCorpus(list);
	}

	@Override
	public void done() {
		INERConfiguration conf;
		try {
			conf = pane.getConfiguration(null);
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
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		}
		
	}

	@Override
	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Workflow_:_Information_Extraction_From_Query#Select_NER_Process";
	}

	

}
