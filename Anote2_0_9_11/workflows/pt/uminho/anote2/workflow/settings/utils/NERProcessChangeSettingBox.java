package pt.uminho.anote2.workflow.settings.utils;

import java.util.HashMap;
import java.util.Map;

import pt.uminho.anote2.workflow.datastructures.IChangeSettings;
import pt.uminho.anote2.workflow.datastructures.NERWorkflowProcessesAvailableEnum;
import pt.uminho.anote2.workflow.datastructures.WorkflowsEnum;

public class NERProcessChangeSettingBox {
	
	private Map<WorkflowsEnum,Map<NERWorkflowProcessesAvailableEnum, IChangeSettings>> nerProcessChangeSettingGUI;
	
	static NERProcessChangeSettingBox guiMAnager = null;

	
	synchronized public static NERProcessChangeSettingBox getInstance(){
		if(guiMAnager == null)
			guiMAnager = new NERProcessChangeSettingBox();
		
		return guiMAnager;
	}
	
	private NERProcessChangeSettingBox()
	{
		nerProcessChangeSettingGUI = new HashMap<WorkflowsEnum, Map<NERWorkflowProcessesAvailableEnum,IChangeSettings>>();
	}
	
	public void registerChangeSettingGUI(WorkflowsEnum workflow,NERWorkflowProcessesAvailableEnum nerProcess,IChangeSettings gui)
	{
		if(!nerProcessChangeSettingGUI.containsKey(workflow))
			nerProcessChangeSettingGUI.put(workflow, new HashMap<NERWorkflowProcessesAvailableEnum, IChangeSettings>());
//		if(!nerProcessChangeSettingGUI.get(workflow).containsKey(nerProcess))
		nerProcessChangeSettingGUI.get(workflow).put(nerProcess,gui);
	}
	
	public IChangeSettings getChangeSettingsAndUpdate(WorkflowsEnum worklow,NERWorkflowProcessesAvailableEnum nerProcess)
	{
//		if(nerProcessChangeSettingGUI.get(worklow).get(nerProcess)!=null)
//			nerProcessChangeSettingGUI.get(worklow).get(nerProcess).defaultSettings();		
		return nerProcessChangeSettingGUI.get(worklow).get(nerProcess);
	}
	
	public IChangeSettings getProperties(WorkflowsEnum worklow,NERWorkflowProcessesAvailableEnum nerProcess)
	{
		if(nerProcessChangeSettingGUI.get(worklow).get(nerProcess)==null)
			return null;
		return nerProcessChangeSettingGUI.get(worklow).get(nerProcess);
	}
	
	

}
