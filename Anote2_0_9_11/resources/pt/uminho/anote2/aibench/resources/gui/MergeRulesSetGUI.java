package pt.uminho.anote2.aibench.resources.gui;

import java.util.Set;

import pt.uminho.anote2.aibench.resources.datatypes.RulesAibench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class MergeRulesSetGUI extends MergeResources{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MergeRulesSetGUI() {
		super(RulesAibench.class);
		this.setModal(true);
	}

	public String getResourceType() {
		return "Rule Set";
	}

	protected void okButtonAction() {
		boolean createNewResource = getJRadioResourcesYes().isSelected();
		RulesAibench rulesSources = (RulesAibench) getComboBoxSource().getSelectedItem();
		RulesAibench rulessDestiny = (RulesAibench) getComboBoxDestiny().getSelectedItem();
		Set<Integer> classIdsSource = getResourceSourceSelectedClasses();
		Set<Integer> classIdsDestiny = getResourceDestinySelectedClasses();
		String name = new String();
		String notes = new String();
		if(classIdsSource.size()==0)
		{
			new ShowMessagePopup("You have to select at least one class in[source] Rule Set");
			Workbench.getInstance().warn("You have to select at least one class in [source] Rule Set");
		}
		else
		{
			if(createNewResource)
			{
				name = getNewResourceName();
				notes = getNewResourceNotes();
				if(classIdsDestiny.size() == 0)
				{
					new ShowMessagePopup("You have to select at least one class in [destiny]Rule Set ");	
					Workbench.getInstance().warn("You have to select at least one class in [destiny] Rule Set");
				}
				else if(name.length()>0)
				{
					testSameResourceAndOperation(createNewResource,
							rulesSources, rulessDestiny, classIdsSource,
							classIdsDestiny, name, notes);
				}
				else
				{
					new ShowMessagePopup("The new Rule Set name cannot be empty");	
					Workbench.getInstance().warn("The new Rule Set name cannot be empty");
				}
			}
			else
			{		   
				testSameResourceAndOperation(createNewResource,
						rulesSources, rulessDestiny, classIdsSource,
						classIdsDestiny, name, notes);
			}
		}

	}

	private void testSameResourceAndOperation(boolean createNewResource,
			RulesAibench rulesSources, RulesAibench rulesDestiny,
			Set<Integer> classIdsSource, Set<Integer> classIdsDestiny,
			String name, String notes) {
		if(rulesSources.compareTo(rulesDestiny)==0)
		{
			new ShowMessagePopup("Source and Target Rule Sets are the same");	
			Workbench.getInstance().warn("Source and Target Rule Sets are the same");
		}
		else
		{
			callOperation(createNewResource, rulesSources,
					rulesDestiny, classIdsSource, classIdsDestiny, name,
					notes);	
		}
	}

	private void callOperation(boolean createNewResource,
			RulesAibench rulesSources, RulesAibench rulesDestiny,
			Set<Integer> classIdsSource, Set<Integer> classIdsDestiny,
			String name, String notes) {
		paramsRec.paramsIntroduced( new ParamSpec[]{ 
				new ParamSpec("new resource option",Boolean.class,createNewResource,null),
				new ParamSpec("name",String.class,name,null),
				new ParamSpec("notes",String.class,notes,null),
				new ParamSpec("rules dest",RulesAibench.class,rulesDestiny,null),
				new ParamSpec("rules dest classes",Set.class,classIdsDestiny,null),
				new ParamSpec("rules source",RulesAibench.class,rulesSources,null),
				new ParamSpec("rules source classes",Set.class,classIdsSource,null)
		});
	}
	
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"RulesSet_Merge#Configuration";
	}
	
	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		this.paramsRec = arg0;
		this.setSize(GlobalOptions.generalWidth,GlobalOptions.generalHeight);
		Utilities.centerOnOwner(this);
		this.setVisible(true);	
	}

}
