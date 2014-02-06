package pt.uminho.anote2.aibench.resources.gui;

import java.util.HashSet;
import java.util.Set;

import pt.uminho.anote2.aibench.resources.datatypes.DictionaryAibench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class MergeDictionaries extends MergeResources{

	private static final long serialVersionUID = 1L;
	
	public MergeDictionaries() {
		super(DictionaryAibench.class);
		this.setModal(true);
	}

	public String getResourceType() {
		return "Dictionary";
	}

	protected void okButtonAction() {
		boolean createNewResource = getJRadioResourcesYes().isSelected();
		DictionaryAibench dictionarySources = (DictionaryAibench) getComboBoxSource().getSelectedItem();
		DictionaryAibench dictionaryDestiny = (DictionaryAibench) getComboBoxDestiny().getSelectedItem();
		Set<Integer> dicSourceClasses = getResourceSourceSelectedClasses();
		Set<Integer> dicDestinyClasses = getResourceDestinySelectedClasses();
		String name = new String();
		String notes = new String();
		if(dicSourceClasses.size()==0)
		{
			new ShowMessagePopup("You have to select at least one class in [source] dictionary ");
			Workbench.getInstance().warn("You have to select at least one class in [source] dictionary ");
		}
		else
		{
			Set<Integer> dicDestinyClasse = new HashSet<Integer>();
			if(createNewResource)
			{
				dicDestinyClasse = getResourceDestinySelectedClasses();
				name = getNewResourceName();
				notes = getNewResourceNotes();
				if(dicDestinyClasses.size() == 0)
				{
					new ShowMessagePopup("You have to select at least one class in [target] dictionary ");
					Workbench.getInstance().warn("You have to select at least one class in [target] dictionary ");
				}
				else if(name.length()>0)
				{
					testSameResourcesAndOperation(createNewResource,
							dictionarySources, dictionaryDestiny, dicSourceClasses,
							name, notes, dicDestinyClasse);
				}
				else
				{
					new ShowMessagePopup("The new Dictionary name cannot be empty");	
					Workbench.getInstance().warn("The new Dictionary name cannot be empty");
				}
			}
			else
			{
				testSameResourcesAndOperation(createNewResource,
						dictionarySources, dictionaryDestiny, dicSourceClasses,
						name, notes, dicDestinyClasse);
			}

		}
	}

	private void testSameResourcesAndOperation(boolean createNewResource,
			DictionaryAibench dictionarySources,
			DictionaryAibench dictionaryDestiny, Set<Integer> dicSourceClasses,
			String name, String notes, Set<Integer> dicDestinyClasse) {
		if(dictionarySources.compareTo(dictionaryDestiny)==0)
		{
			new ShowMessagePopup("Source and Target Dictionaries are the same");	
			Workbench.getInstance().warn("Source and Target Dictionaries are the same");
		}
		else
		{
			callOperaton(createNewResource, dictionarySources,
					dictionaryDestiny, dicSourceClasses, name, notes,
					dicDestinyClasse);
		}
	}

	private void callOperaton(boolean createNewResource,
			DictionaryAibench dictionarySources,
			DictionaryAibench dictionaryDestiny, Set<Integer> dicSourceClasses,
			String name, String notes, Set<Integer> dicDestinyClasse) {
		paramsRec.paramsIntroduced( new ParamSpec[]{ 
				   new ParamSpec("new resource option",Boolean.class,createNewResource,null),
				   new ParamSpec("name",String.class,name,null),
				   new ParamSpec("notes",String.class,notes,null),
				   new ParamSpec("dic dest",DictionaryAibench.class,dictionaryDestiny,null),
				   new ParamSpec("dic dest classes",Set.class,dicDestinyClasse,null),
				   new ParamSpec("dic source",DictionaryAibench.class,dictionarySources,null),
				   new ParamSpec("dic source classes",Set.class,dicSourceClasses,null)
		   });
	}
	
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Dictionary_Merge#Configuration";
	}
	
	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		Object obj = HelpAibench.getSelectedItem(DictionaryAibench.class);
		if(obj==null)
		{
			Workbench.getInstance().warn("No Dictionary selected on clipboard");
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
