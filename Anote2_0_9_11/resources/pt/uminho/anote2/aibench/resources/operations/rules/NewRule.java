package pt.uminho.anote2.aibench.resources.operations.rules;

import java.sql.SQLException;

import pt.uminho.anote2.aibench.resources.datatypes.RulesAibench;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.schema.TableResourcesElements;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.resource.IResourceElement;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation(enabled=false)
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
		System.gc();
		try {

		int classID;
			classID = rules.addElementClass(ruleClass);
		IResourceElement elem = new ResourceElement(-1,rule,classID,ruleClass);
		if(rules.verifyIfTermExist(elem))
		{
			new ShowMessagePopup("The term \""+rule+"\" is already in Rule Set Resource");	
			Workbench.getInstance().warn("The term \""+rule+"\" is already in Rule Set Resource");
		}
		if(rules.addElement(elem))
		{
			rules.addResourceContent(ruleClass);
			rules.notifyViewObservers();
			new ShowMessagePopup("Add New Rule.");
		}
		else
		{
			if(ruleClass.length()<2)
			{
				new ShowMessagePopup("The term must contain more than one character.");	
				Workbench.getInstance().warn("The term must contain more than one character.");
			}
			else if(ruleClass.length()>TableResourcesElements.elementSize)
			{		
				new ShowMessagePopup("the term must not contain more than "+TableResourcesElements.elementSize+" character.");	
				Workbench.getInstance().warn("the term must not contain more than "+TableResourcesElements.elementSize+" character.");
			}
			else
			{
				new ShowMessagePopup("The term \""+ruleClass+"\" is already in Lexical Word Resource");	
				Workbench.getInstance().warn("The term \""+ruleClass+"\" is already in Lexical Word Resource");
			}
		}
		} catch (SQLException e) {
			new ShowMessagePopup("Add New Rule Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Add New Rule Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}

}
