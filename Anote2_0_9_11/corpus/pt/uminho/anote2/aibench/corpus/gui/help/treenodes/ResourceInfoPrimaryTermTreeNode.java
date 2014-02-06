package pt.uminho.anote2.aibench.corpus.gui.help.treenodes;

public class ResourceInfoPrimaryTermTreeNode {
	
	private boolean isRule;
	
	public ResourceInfoPrimaryTermTreeNode(boolean isrule)
	{
		this.isRule = isrule;
	}
	
	public String toString()
	{
		if(isRule)
		{
			return "Main Rule:";
		}
		else
		{
			return "Primary Term:";
		}
	}

}
