package pt.uminho.anote2.process.IE.re.export.configuration;

public interface IREToNetworkConfiguration {
	public IREToNetworkConfigurationEntityOptions getEntityConfigutrationOptions();
	public IREToNetworkConfigurationRelationOptions getRelationConfigurationOptions();
	public IREToNetworkConfigurationAdvanceOptions getAdvanceConfigurations();
	public IREToNetworkConfigurationAutoOpen getREToNetworkConfigurationAutoOpen();
}
