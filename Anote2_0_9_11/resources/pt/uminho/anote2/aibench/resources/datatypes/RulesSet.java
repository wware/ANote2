package pt.uminho.anote2.aibench.resources.datatypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.ListElements;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;

@Datatype(structure = Structure.LIST,namingMethod="getName",removable=false,renamed=false)
public class RulesSet extends Observable{
	
	private Resources resources;
	private List<RulesAibench> rules;
	
	public RulesSet(Resources resources) 
	{
		this.resources=resources;
		this.rules=new ArrayList<RulesAibench>();
	}
	
	public String getName()
	{
		return "Rules";
	}
	
	public void notifyViewObservers(){
		this.setChanged();
		this.notifyObservers();
	}

	public Resources getResources() {
		return resources;
	}
	
	public void addRules(RulesAibench rulesSet)
	{
		if(this.rules==null)
		{
			this.rules = new ArrayList<RulesAibench>();
		}
		if(alreadyExist(rulesSet))
		{
			new ShowMessagePopup("Rule Set Already In Clipboard.");
		}
		else
		{
			this.rules.add(rulesSet);
			new ShowMessagePopup("Rule Set Added To Clipboard.");
			this.notifyViewObservers();
		}
	}
	
	@ListElements(modifiable=true)
	public List<RulesAibench> getRulesClipboard()
	{
		return this.rules;
	}
	
	public boolean alreadyExist(RulesAibench dic)
	{
		for(RulesAibench dicAibench:rules)
		{
			int compare = dicAibench.compareTo(dic);
			if(compare==0)
			{
				return true;
			}
		}
		return false;
	}
	
	


}
