package pt.uminho.anote2.datastructures.process.re.export.configuration;

import pt.uminho.anote2.process.IE.re.export.configuration.IREToNetworkConfigurationAutoOpen;

public class REToNetworkConfigurationAutoOpen implements IREToNetworkConfigurationAutoOpen{

	private boolean autoOpen;

	public REToNetworkConfigurationAutoOpen(boolean autoOpen)
	{
		this.autoOpen=autoOpen;
	}
	
	@Override
	public boolean autoOpen() {
		return autoOpen;
	}

}
