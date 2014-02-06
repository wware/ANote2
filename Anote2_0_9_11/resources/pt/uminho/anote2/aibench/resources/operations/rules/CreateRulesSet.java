package pt.uminho.anote2.aibench.resources.operations.rules;

import java.sql.SQLException;
import java.util.List;

import pt.uminho.anote2.aibench.resources.datatypes.Resources;
import pt.uminho.anote2.aibench.resources.datatypes.RulesAibench;
import pt.uminho.anote2.aibench.resources.datatypes.RulesSet;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;


@Operation(enabled=true)
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
		System.gc();
		try {
			Resources.newResource(name, info,GlobalOptions.resourcesRuleSetName);
			rules.notifyViewObservers();
			new ShowMessagePopup("Create RuleSet Done .");
			List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(RulesSet.class);
			RulesSet rst = (RulesSet) cl.get(0).getUserData();	
			IResource<IResourceElement> lt = Resources.getLastResource();
			rst.addRules((RulesAibench) lt);
		} catch (SQLException e) {
			new ShowMessagePopup("Create RuleSet Fail .");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Create RuleSet Fail .");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		}
	}

}
