package pt.uminho.anote2.aibench.corpus.gui.help.treenodes;

public class ResourceInfoStatusTreeLabel {
	private boolean active;
	
	
	
	
	public ResourceInfoStatusTreeLabel(boolean active) {
		super();
		this.active = active;
	}


	public String toString()
	{
		if(active)
		{
			return "Active";
		}
		else
		{
			return "Inactive";
		}
	}
}
