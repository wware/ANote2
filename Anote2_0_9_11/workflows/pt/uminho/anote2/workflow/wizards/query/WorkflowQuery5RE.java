package pt.uminho.anote2.workflow.wizards.query;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JComponent;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.process.IE.re.IREConfiguration;
import pt.uminho.anote2.workflow.gui.AREPanel;
import pt.uminho.anote2.workflow.wizards.query.pane.WorkflowQueryREPane;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class WorkflowQuery5RE extends WizardStandard{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AREPanel pane;
	
	public WorkflowQuery5RE(List<Object> list) {
		super(list);
		initGUI();
		this.setTitle("WorkFlow Information Extraction (From Query) - Selecting RE Process");
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
				pane = new WorkflowQueryREPane();
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();	
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();			}
		return pane;
	}

	@Override
	public void goNext() {}

	@Override
	public void goBack() {
		List<Object> list = this.getParam();
		closeView();
		new WorkFlowQuery4NER(list);
	}

	@Override
	public void done() {
		IREConfiguration conf = pane.getConfiguration(null, null);
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
		return GlobalOptions.wikiGeneralLink+"Workflow_:_Information_Extraction_From_Query#Select_RE_Process";
	}

}
