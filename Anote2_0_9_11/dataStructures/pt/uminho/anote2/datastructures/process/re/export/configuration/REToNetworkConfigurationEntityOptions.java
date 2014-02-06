package pt.uminho.anote2.datastructures.process.re.export.configuration;

import java.util.Set;

import pt.uminho.anote2.process.IE.re.export.configuration.IREToNetworkConfigurationEntityOptions;

public class REToNetworkConfigurationEntityOptions implements IREToNetworkConfigurationEntityOptions{

	
	public Set<Integer> classIdsAllowed;
	
	public REToNetworkConfigurationEntityOptions(Set<Integer> classIdsAllowed) {
		super();
		this.classIdsAllowed = classIdsAllowed;
	}

	public Set<Integer> getClassIdsAllowed() {
		return classIdsAllowed;
	}

}
