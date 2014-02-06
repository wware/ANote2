package pt.uminho.anote2.aibench.corpus.gui.help.treenodes;

public class ResourceInfoSynonymsTreeNode {
	private boolean isrule;
	
	public ResourceInfoSynonymsTreeNode(boolean isrule)
	{
		this.isrule = isrule;
	}
	
	public String toString()
	{
		if(isrule)
		{
			return "Other Rule Variations :";
		}
		else
		{
			return "Synonyms :";
		}
	}
}
