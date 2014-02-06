package pt.uminho.anote2.process.IE.re.export.configuration;

import java.util.EnumSet;
import java.util.SortedSet;

import pt.uminho.anote2.core.annotation.DirectionallyEnum;
import pt.uminho.anote2.core.annotation.PolarityEnum;

public interface IREToNetworkConfigurationRelationOptions {
	public EnumSet<PolarityEnum> getPolaritiesAllowed();
	public EnumSet<DirectionallyEnum> getDirectionallyAllowed();
	public SortedSet<ICardinality> getCardinalitiesAllowed();
}
