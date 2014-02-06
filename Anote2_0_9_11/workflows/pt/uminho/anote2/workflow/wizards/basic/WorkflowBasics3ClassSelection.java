package pt.uminho.anote2.workflow.wizards.basic;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;

import pt.uminho.anote2.aibench.ner.datastructures.ResourcesToNerAnote;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.process.IE.ner.INERConfiguration;
import pt.uminho.anote2.process.IE.re.IREConfiguration;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.workflow.datastructures.NERWorkflowProcessesAvailableEnum;
import pt.uminho.anote2.workflow.datastructures.REWorkflowProcessesAvailableEnum;
import pt.uminho.anote2.workflow.gui.panes.ResourcesSelectClasses;
import pt.uminho.anote2.workflow.settings.basic.WorkflowBasicsDefaulSettings;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class WorkflowBasics3ClassSelection extends WizardStandard{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ResourcesSelectClasses selectClasses;
	
	
	
	public WorkflowBasics3ClassSelection(List<Object> param) {
		super(param);
		initGUI();
		this.setTitle("Basic Workflow - Step 3");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}
	
	public void initGUI()
	{
		setEnableDoneButton(true);
		setEnableBackButton(true);
		setEnableNextButton(false);
	}


	@Override
	public JComponent getMainComponent() {
		List<IResource<IResourceElement>> resources =  (List<IResource<IResourceElement>>) getParam().get(2);
		if(selectClasses == null)
		{
			try {
				selectClasses = new ResourcesSelectClasses(resources);
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			}
		}
		return selectClasses;
	}

	@Override
	public void goNext() {}

	@Override
	public void goBack() {
		closeView();
		new WorkflowBasics2DictionarySelection(getParam());
	}

	@Override
	public void done() {
		List<Integer> classes = selectClasses.getSelectedClasses();
		if(classes.size()!= 0)
		{
			ResourcesToNerAnote resourceToNER;
			try {
				resourceToNER = getResources(classes);
				INERConfiguration nerConfiguration = getNERConfiguration(resourceToNER);		
				IREConfiguration reRelationConfiguration = getREConfiguration();
				getParam().remove(2);
				getParam().add(nerConfiguration);
				getParam().add(reRelationConfiguration);
				List<Object> list = this.getParam();
				ParamSpec[] paramsSpec = new ParamSpec[]{
						new ParamSpec("list", List.class,list, null)
				};

				for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
					if (def.getID().equals("operations.basicsworkflow")){	
						Workbench.getInstance().executeOperation(def, paramsSpec);
						closeView();
						return;
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
		else
		{
			Workbench.getInstance().warn("Please select at least one class");
		}
	}

	private IREConfiguration getREConfiguration() throws SQLException, DatabaseLoadDriverException {	
		return ((REWorkflowProcessesAvailableEnum) PropertiesManager.getPManager().getProperty(WorkflowBasicsDefaulSettings.RE)).getREConfigurationBasicWorkFlow();
	}

	private INERConfiguration getNERConfiguration(ResourcesToNerAnote resourceToNER) throws SQLException, DatabaseLoadDriverException {
		return ((NERWorkflowProcessesAvailableEnum) PropertiesManager.getPManager().getProperty(WorkflowBasicsDefaulSettings.NER)).getNERConfigurationBasicWorkFlow(resourceToNER);
	}

	
	private ResourcesToNerAnote getResources(List<Integer> classes) throws DatabaseLoadDriverException, SQLException {
		boolean caseSensitive = Boolean.parseBoolean(PropertiesManager.getPManager().getProperty(WorkflowBasicsDefaulSettings.CASE_SENSITIVE).toString());
		boolean rulesWithDics = Boolean.parseBoolean(PropertiesManager.getPManager().getProperty(WorkflowBasicsDefaulSettings.USE_PARTIAL_MATCH_WITH_DICTIONARIES).toString());
		ResourcesToNerAnote resourceToNER = new ResourcesToNerAnote(caseSensitive,rulesWithDics);
		List<IResource<IResourceElement>> resources =  (List<IResource<IResourceElement>>) getParam().get(2);
		for(IResource<IResourceElement> resource :resources)
		{
			Set<Integer> resourceSelectedClass = getResourceSelectedClasses(resource,classes);
			if(resourceSelectedClass.size() > 0)
			{
				resourceToNER.add(resource, resource.getClassContent(), resourceSelectedClass);
			}
		}
		return resourceToNER;
	}

	private Set<Integer> getResourceSelectedClasses(IResource<IResourceElement> resource, List<Integer> classes) throws DatabaseLoadDriverException, SQLException {
		Set<Integer> classContent = resource.getClassContent();
		Set<Integer> result = new HashSet<Integer>();
		for(int classID : classes)
		{
			if(classContent.contains(classID))
			{
				result.add(classID);
			}
		}
		return result;
	}
	
	@Override
	public String getHelpLink() {
		return "Workflow_:_Information_Retrieval_and_Extraction_Basic#Step_3:_Class.28es.29_Selection";
	}

}
