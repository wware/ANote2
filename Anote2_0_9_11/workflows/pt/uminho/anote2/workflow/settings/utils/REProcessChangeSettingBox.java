package pt.uminho.anote2.workflow.settings.utils;

import java.util.HashMap;
import java.util.Map;

import pt.uminho.anote2.workflow.datastructures.IChangeSettings;
import pt.uminho.anote2.workflow.datastructures.REWorkflowProcessesAvailableEnum;
import pt.uminho.anote2.workflow.datastructures.WorkflowsEnum;

public class REProcessChangeSettingBox {
	
	private Map<WorkflowsEnum,Map<REWorkflowProcessesAvailableEnum, IChangeSettings>> reProcessChangeSettingGUI;
	
	static REProcessChangeSettingBox guiMAnager = null;

	
	synchronized public static REProcessChangeSettingBox getInstance(){
		if(guiMAnager == null)
			guiMAnager = new REProcessChangeSettingBox();
		
		return guiMAnager;
	}
	
	private REProcessChangeSettingBox()
	{
		reProcessChangeSettingGUI = new HashMap<WorkflowsEnum, Map<REWorkflowProcessesAvailableEnum,IChangeSettings>>();
	}
	
	public void registerChangeSettingGUI(WorkflowsEnum workflow,REWorkflowProcessesAvailableEnum reProcess,IChangeSettings gui)
	{
		if(!reProcessChangeSettingGUI.containsKey(workflow))
			reProcessChangeSettingGUI.put(workflow, new HashMap<REWorkflowProcessesAvailableEnum, IChangeSettings>());
//		if(!reProcessChangeSettingGUI.get(workflow).containsKey(reProcess))
		reProcessChangeSettingGUI.get(workflow).put(reProcess,gui);
	}
	
	public IChangeSettings getChangeSettings(WorkflowsEnum worklow,REWorkflowProcessesAvailableEnum reporcess)
	{
		if(reProcessChangeSettingGUI.get(worklow).get(reporcess)==null)
			return null;
		return reProcessChangeSettingGUI.get(worklow).get(reporcess);
	}

}
