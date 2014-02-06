package pt.uminho.anote2.aibench.corpus.gui.help.treenodes;

import pt.uminho.anote2.core.annotation.IExternalID;

public class ResourceInfoExternalIDsLabel {
	
	private IExternalID external;
	

	public ResourceInfoExternalIDsLabel(IExternalID external)
	{
		this.external = external;
	}
	
	public String toString()
	{
		return external.toString();
	}
	
	public IExternalID getExternal() {
		return external;
	}


}
