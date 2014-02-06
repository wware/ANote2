package pt.uminho.anote2.relation.configuration;

import java.util.SortedSet;

import pt.uminho.anote2.process.IE.re.IRelationsType;

public interface IRERelationAdvancedConfiguration {
	public boolean usingVerbEntitiesDistance();
	public int getVerbEntitieMaxDistance();
	public boolean usingOnlyVerbNearestEntities();
	public SortedSet<IRelationsType> getRelationsType();
	public boolean usingOnlyEntitiesNearestVerb();
	
}
