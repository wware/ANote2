package pt.uminho.anote2.workflow.wizards.query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.publicationmanager.gui.help.ASelectQueryDocumentsPanel;
import pt.uminho.anote2.aibench.publicationmanager.gui.help.SelectQueryDocumentPanel;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class WorkflowQuery1SelectPublications extends WizardStandard{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ASelectQueryDocumentsPanel panel;
	
	
	public WorkflowQuery1SelectPublications() {
		super(new ArrayList<Object>());
		initGUI();
		this.setTitle("WorkFlow Information Extraction (From Query) - Select Publications");
		Utilities.centerOnOwner(this);
		this.setModal(true);
	}
	
	public WorkflowQuery1SelectPublications(List<Object> objects) {
		super(new ArrayList<Object>());
		initGUI();
		fillFields(objects.get(0));
		this.setTitle("WorkFlow Information Extraction (From Query) - Select Publications");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}
	
	private void fillFields(Object object) {
		Set<IPublication> publications = (Set<IPublication>) object;
		panel.updateSelectedPublications(publications);
	}

	private void initGUI() {
		setEnableDoneButton(false);
		setEnableNextButton(true);
		setEnableBackButton(false);
	}
	
	@Override
	public JComponent getMainComponent() {
		if(panel ==null)
			try {
				panel = new SelectQueryDocumentPanel(getselectedQuery());
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			}
		return panel;
	}

	private QueryInformationRetrievalExtension getselectedQuery() {
		Object obj = HelpAibench.getSelectedItem(QueryInformationRetrievalExtension.class);
		if(obj!=null)
		{
			return (QueryInformationRetrievalExtension) obj;
		}
		return null;
	}
	
	
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		Object obj = HelpAibench.getSelectedItem(QueryInformationRetrievalExtension.class);
		if(obj==null)
		{
			Workbench.getInstance().warn("No Query selected on clipboard");
			dispose();
		}
		else
		{
			this.setVisible(true);
		}
	}
	
	
	@Override
	public void goNext() {
		if(panel.getPublications()!=null)
		{	
			List<Object> param = new ArrayList<Object>();
			Set<IPublication> pubs = panel.getPublications();
			param.add(pubs);
			closeView();
			new WorkflowQuery2SelectSteps(param);
		}
	}
	
	@Override
	public void goBack() {}

	@Override
	public void done() {}

	@Override
	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Workflow_:_Information_Extraction_From_Query#Select_Query_Publications";
	}

}
