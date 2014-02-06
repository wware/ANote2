package pt.uminho.anote2.aibench.resources.operations.rules;

import pt.uminho.anote2.aibench.resources.datatypes.RulesSet;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;


@Operation
public class CreateRulesSet {
	
	private RulesSet rules;
	private String name;

	@Port(name="dictionaries",direction=Direction.INPUT,order=1)
	public void setDicionaries(RulesSet rules)
	{
		this.rules=rules;
	}

	@Port(name="name",direction=Direction.INPUT,order=2)
	public void setName(String name)
	{
		this.name=name;
	}

	@Port(name="info",direction=Direction.INPUT,order=3)
	public void getInfo(String info)
	{
		rules.getResources().newResource(name, info,"rules");
		rules.notifyViewObservers();
		new ShowMessagePopup("Create RuleSet Done !!!");
	}

}
