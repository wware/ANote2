package pt.uminho.anote2.aibench.resources.operations.rules;

import pt.uminho.anote2.aibench.resources.datatypes.RulesAibench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.resource.IResourceElement;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation
public class NewRule {
	
	private RulesAibench rules;
	private String rule;
	
	@Port(name="rulesAibench",direction=Direction.INPUT,order=1)
	public void setDicionaries(RulesAibench rules)
	{
		this.rules=rules;
	}

	@Port(name="rule",direction=Direction.INPUT,order=2)
	public void setRule(String rule)
	{
		this.rule=rule;
	}

	@Port(name="ruleClass",direction=Direction.INPUT,order=3)
	public void setRuleClass(String ruleClass)
	{
		int classID = rules.addElementClass(ruleClass);
		IResourceElement elem = new ResourceElement(-1,rule,classID,ruleClass);
		rules.addElement(elem);
		rules.addResourceContent(ruleClass);
		rules.notifyViewObservers();
		new ShowMessagePopup("Add New Rule !!!");
	}

}
