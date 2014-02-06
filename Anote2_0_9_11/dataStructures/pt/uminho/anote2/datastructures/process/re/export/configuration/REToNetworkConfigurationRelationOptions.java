package pt.uminho.anote2.datastructures.process.re.export.configuration;

import java.util.EnumSet;
import java.util.SortedSet;

import pt.uminho.anote2.core.annotation.DirectionallyEnum;
import pt.uminho.anote2.core.annotation.PolarityEnum;
import pt.uminho.anote2.process.IE.re.export.configuration.ICardinality;
import pt.uminho.anote2.process.IE.re.export.configuration.IREToNetworkConfigurationRelationOptions;

public class REToNetworkConfigurationRelationOptions implements IREToNetworkConfigurationRelationOptions {

	private EnumSet<PolarityEnum> polaritiesAllowed;
	private EnumSet<DirectionallyEnum> directionallyAllowed;
	private SortedSet<ICardinality> getCardinalitiesAllowed;
	
	
	public REToNetworkConfigurationRelationOptions(
			EnumSet<PolarityEnum> polaritiesAllowed, EnumSet<DirectionallyEnum> directionallyAllowed,
			SortedSet<ICardinality> getCardinalitiesAllowed) {
		super();
		this.polaritiesAllowed = polaritiesAllowed;
		this.directionallyAllowed = directionallyAllowed;
		this.getCardinalitiesAllowed = getCardinalitiesAllowed;
	}

	public EnumSet<PolarityEnum> getPolaritiesAllowed() {
		return polaritiesAllowed;
	}

	public EnumSet<DirectionallyEnum> getDirectionallyAllowed() {
		return directionallyAllowed;
	}

	public SortedSet<ICardinality> getCardinalitiesAllowed() {
		return getCardinalitiesAllowed;
	}

}
