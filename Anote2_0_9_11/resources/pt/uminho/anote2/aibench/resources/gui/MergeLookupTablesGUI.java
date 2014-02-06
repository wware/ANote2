package pt.uminho.anote2.aibench.resources.gui;

import java.util.Set;

import pt.uminho.anote2.aibench.resources.datatypes.LookupTableAibench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class MergeLookupTablesGUI extends MergeResources{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MergeLookupTablesGUI() {
		super(LookupTableAibench.class);
		this.setModal(true);
	}

	public String getResourceType() {
		return "Lookup Table";
	}

	protected void okButtonAction() {
		boolean createNewResource = getJRadioResourcesYes().isSelected();
		LookupTableAibench lookuptableSources = (LookupTableAibench) getComboBoxSource().getSelectedItem();
		LookupTableAibench lookuptableDestiny = (LookupTableAibench) getComboBoxDestiny().getSelectedItem();
		Set<Integer> classIdsSource = getResourceSourceSelectedClasses();
		Set<Integer> classIdsDestiny = getResourceDestinySelectedClasses();
		String name = new String();
		String notes = new String();
		if(classIdsSource.size()==0)
		{
			new ShowMessagePopup("You have to select at least one class in [source] Lookup Table ");
			Workbench.getInstance().warn("You have to select at least one class in [source] Lookup Table ");
		}
		else
		{
			if(createNewResource)
			{
				name = getNewResourceName();
				notes = getNewResourceNotes();
				if(classIdsDestiny.size() == 0)
				{
					new ShowMessagePopup("You have to select at least one class in [target] Lookup Table ");	
					Workbench.getInstance().warn("You have to select at least one class in [target] Lookup Table ");
				}
				else if(name.length()>0)
				{				
					testSameResourcesANOperation(createNewResource,
							lookuptableSources, lookuptableDestiny, classIdsSource,
							classIdsDestiny, name, notes);
				}
				else
				{
					new ShowMessagePopup("The new Lookup Table name cannot be empty");	
					Workbench.getInstance().warn("The new Lookup Table name cannot be empty");
				}
			}
			else
			{
				testSameResourcesANOperation(createNewResource,
						lookuptableSources, lookuptableDestiny, classIdsSource,
						classIdsDestiny, name, notes);
			}
		}

	}

	private void testSameResourcesANOperation(boolean createNewResource,
			LookupTableAibench lookuptableSources,
			LookupTableAibench lookuptableDestiny, Set<Integer> classIdsSource,
			Set<Integer> classIdsDestiny, String name, String notes) {
		if(lookuptableSources.compareTo(lookuptableDestiny)==0)
		{
			Workbench.getInstance().warn("Source and Target Lookup Tables are the same");
			new ShowMessagePopup("Source and Target Lookup Tables are the same");	

		}
		else
		{
			callOperation(createNewResource, lookuptableSources,
					lookuptableDestiny, classIdsSource, classIdsDestiny, name,
					notes);	
		}
	}

	private void callOperation(boolean createNewResource,
			LookupTableAibench lookuptableSources,
			LookupTableAibench lookuptableDestiny, Set<Integer> classIdsSource,
			Set<Integer> classIdsDestiny, String name, String notes) {
		paramsRec.paramsIntroduced( new ParamSpec[]{ 
				new ParamSpec("new resource option",Boolean.class,createNewResource,null),
				new ParamSpec("name",String.class,name,null),
				new ParamSpec("notes",String.class,notes,null),
				new ParamSpec("lt dest",LookupTableAibench.class,lookuptableDestiny,null),
				new ParamSpec("lt dest classes",Set.class,classIdsDestiny,null),
				new ParamSpec("lt source",LookupTableAibench.class,lookuptableSources,null),
				new ParamSpec("lt source classes",Set.class,classIdsSource,null)
		});
	}

	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"LookupTable_Merge#Configuration";
	}
	
	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		Object obj = HelpAibench.getSelectedItem(LookupTableAibench.class);
		if(obj==null)
		{
			Workbench.getInstance().warn("No Lookup Table selected on clipboard");
			dispose();
		}
		else
		{
			this.paramsRec = arg0;
			this.setSize(GlobalOptions.generalWidth,GlobalOptions.generalHeight);
			Utilities.centerOnOwner(this);
			this.setVisible(true);	
		}
	}
}
