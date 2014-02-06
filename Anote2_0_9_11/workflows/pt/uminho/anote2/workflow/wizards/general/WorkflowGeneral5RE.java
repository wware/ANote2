package pt.uminho.anote2.workflow.wizards.general;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JComponent;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.process.IE.re.IREConfiguration;
import pt.uminho.anote2.workflow.gui.AREPanel;
import pt.uminho.anote2.workflow.wizards.general.panes.WorkflowGeneralREPane;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class WorkflowGeneral5RE extends WizardStandard{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AREPanel pane;
	
	public WorkflowGeneral5RE(List<Object> list) {
		super(list);
		initGUI();
		this.setTitle("WorkFlow Information Retrieval and Extraction - Selecting RE Process");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}
	
	
	private void initGUI() {
		setEnableDoneButton(true);
		setEnableNextButton(false);
	}

	@Override
	public JComponent getMainComponent() {
		if(pane ==null)
			try {
				pane = new WorkflowGeneralREPane();
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
	public void goNext() {}

	@Override
	public void goBack() {
		List<Object> list = this.getParam();
		closeView();
		new WorkFlowGeneral4NER(list);
	}

	@Override
	public void done() {
		IREConfiguration conf = pane.getConfiguration(null, null);
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
		return GlobalOptions.wikiGeneralLink+"Workflow_:_Information_Retrieval_and_Extraction#Select_RE_Process";
	}

}
