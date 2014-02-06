package pt.uminho.anote2.aibench.resources.operations.rules;

import pt.uminho.anote2.aibench.resources.datatypes.RulesAibench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.operations.TimeLeftProgress;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation()
public class MergeRuleSet {
	

	
	private TimeLeftProgress progress = new TimeLeftProgress();
	private RulesAibench dic1;

	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}

//	@Cancel
//	public void cancel(){
//		cancelMethod();
//	}

	@Port(name="dictionary dest",direction=Direction.INPUT,order=1)
	public void setDicionariesDest(RulesAibench dic1)
	{
		this.dic1=dic1;
	}
	
	@Port(name="dictionary source",direction=Direction.INPUT,order=2)
	public void setDicionariesSource(RulesAibench dic2)
	{
		if(dic1.compareTo(dic2)==0)
		{
			Workbench.getInstance().warn("Insert the same dictionaries");
		}
		else
		{
			dic1.merge(dic2);
			dic1.notifyViewObservers();
			new ShowMessagePopup("Merge RuleSet Done!!!");
		}
	}

	

}
