package pt.uminho.anote2.relation.configuration;

import java.util.SortedSet;

import pt.uminho.anote2.process.IE.re.IRelationsType;

public class RERelationAdvancedConfiguration implements IRERelationAdvancedConfiguration{

	private boolean usingVerbEntitiesDistance;
	private int verbEntitieMaxDistance;
	private boolean usingOnlyVerbNearestEntities;
	private boolean usingOnlyEntitiesNearestVerb;
	private SortedSet<IRelationsType> relationsType;
	
	public RERelationAdvancedConfiguration(boolean usingOnlyVerbNearestEntities,boolean usingOnlyEntitiesNearestVerb,int verbEntitieMaxDistance,SortedSet<IRelationsType> relationsType)
	{
		this.usingVerbEntitiesDistance = false;
		this.usingOnlyVerbNearestEntities = usingOnlyVerbNearestEntities;
		this.usingOnlyEntitiesNearestVerb = usingOnlyEntitiesNearestVerb;
		this.verbEntitieMaxDistance = verbEntitieMaxDistance;
		if(this.verbEntitieMaxDistance > 0)
			usingVerbEntitiesDistance = true;
		this.relationsType = relationsType;
	}
	
	
	@Override
	public boolean usingVerbEntitiesDistance() {
		return usingVerbEntitiesDistance;
	}

	@Override
	public int getVerbEntitieMaxDistance() {
		return verbEntitieMaxDistance;
	}

	@Override
	public boolean usingOnlyVerbNearestEntities() {
		return usingOnlyVerbNearestEntities;
	}

	@Override
	public SortedSet<IRelationsType> getRelationsType() {
		return relationsType;
	}


	@Override
	public boolean usingOnlyEntitiesNearestVerb() {
		return usingOnlyEntitiesNearestVerb;
	}



}
