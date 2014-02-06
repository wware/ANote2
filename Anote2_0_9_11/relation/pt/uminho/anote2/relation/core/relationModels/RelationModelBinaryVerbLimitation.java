package pt.uminho.anote2.relation.core.relationModels;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import pt.uminho.anote2.core.annotation.DirectionallyEnum;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.annotation.IEventProperties;
import pt.uminho.anote2.core.annotation.PolarityEnum;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.utils.IGenericPair;
import pt.uminho.anote2.datastructures.annotation.re.EventAnnotation;
import pt.uminho.anote2.datastructures.annotation.re.EventProperties;
import pt.uminho.anote2.datastructures.process.re.RelationType;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.process.IE.re.IRelationsType;
import pt.uminho.anote2.process.IE.re.clue.IVerbInfo;
import pt.uminho.anote2.relation.configuration.IRERelationAdvancedConfiguration;
import pt.uminho.anote2.relation.core.relationModels.specialproperties.VerbClassificationInSentenceEnum;
import pt.uminho.gate.process.IGatePosTagger;

public class RelationModelBinaryVerbLimitation extends RelationModelVerbLimitation {


	public RelationModelBinaryVerbLimitation(ICorpus corpus,IIEProcess nerProcess, IGatePosTagger postagger,IRERelationAdvancedConfiguration advanceConfiguration) {
		super(corpus, nerProcess, postagger,advanceConfiguration);
	}
	
	public List<IEventAnnotation> extractSentenceRelation(IAnnotatedDocument document,List<IEntityAnnotation> semanticLayer, IGenericPair<List<IVerbInfo>, List<Long>> sintatic) throws SQLException, DatabaseLoadDriverException {
		Map<Long, IEntityAnnotation> treeEntitiesPositions = RelationModelutils.getEntitiesPosition(semanticLayer); // offset->entityID
		List<IVerbInfo> verbsInfo = sintatic.getX();
		Map<Long, IVerbInfo> treeverbPositions = getPostagger().getVerbsPosition(verbsInfo); 
		Set<Long> verbsSet = treeverbPositions.keySet();
		List<Long> verbsList = new ArrayList<Long>(verbsSet);
		Set<Long> entitiesSet = treeEntitiesPositions.keySet();
		List<Long> entitiesList = new ArrayList<Long>(entitiesSet);
		List<IEventAnnotation> relations = new ArrayList<IEventAnnotation>();	
		for(int i=0;i<verbsList.size();i++)
		{
			if(entitiesList.size()>1)
			{
				if(i==0)
				{
					// Just one verb
					if(i==verbsList.size()-1)
					{
						extractRelation(document,relations,verbsList.get(i),entitiesList.get(0),entitiesList.get(entitiesList.size()-1),
								treeverbPositions,entitiesList,treeEntitiesPositions,VerbClassificationInSentenceEnum.UNIQUE);
					}
					// The first One
					else
					{
					
						extractRelation(document,relations,verbsList.get(i),entitiesList.get(0),verbsList.get(i+1),treeverbPositions,entitiesList,
								treeEntitiesPositions,VerbClassificationInSentenceEnum.FIRST);
					}			
				}
				// last verb
				else if(i==verbsList.size()-1)
				{
					extractRelation(document,relations,verbsList.get(i),verbsList.get(i-1),entitiesList.get(entitiesList.size()-1),
							treeverbPositions,entitiesList,treeEntitiesPositions,VerbClassificationInSentenceEnum.LAST);

				}
				// verb in the midle of text
				else
				{
					extractRelation(document,relations,verbsList.get(i),verbsList.get(i-1),verbsList.get(i+1),treeverbPositions,entitiesList
							,treeEntitiesPositions,VerbClassificationInSentenceEnum.MIDDLE);

				}
			}		
		}
		return relations;
	}
	
	protected void extractRelation(IAnnotatedDocument document,List<IEventAnnotation> relations, Long verbPosition,Long startPosition,Long endPosition, Map<Long,IVerbInfo> treeverb,List<Long> keySetEnt,
			Map<Long, IEntityAnnotation> treeEntitiesPositions, VerbClassificationInSentenceEnum verbClassidfication) throws SQLException, DatabaseLoadDriverException
	{
		List<IEntityAnnotation> leftentities=new ArrayList<IEntityAnnotation>();
		List<IEntityAnnotation> rightentities=new ArrayList<IEntityAnnotation>();
		DirectionallyEnum dir = treeverb.get(verbPosition).getDirectionality();
		PolarityEnum polarity = treeverb.get(verbPosition).getPolarity();	
		String verb = treeverb.get(verbPosition).getVerb();
		String lemma = treeverb.get(verbPosition).getLemma();
		IEventProperties eventProperties = new EventProperties();
		eventProperties.setDirectionally(dir);
		eventProperties.setLemma(lemma);
		eventProperties.setPolarity(polarity);	
		
		SortedSet<IEntityAnnotation> setOfEntitiesAnnotations = new TreeSet<IEntityAnnotation>();
		SortedSet<IEntityAnnotation> setOfRightAnnotations = new TreeSet<IEntityAnnotation>();
		if(getAdvanceConfiguration()!=null && getAdvanceConfiguration().usingOnlyVerbNearestEntities())
		{
			processindOnlyNearestEntities(verbPosition, startPosition, endPosition,keySetEnt, treeEntitiesPositions, leftentities, rightentities,setOfEntitiesAnnotations, setOfRightAnnotations);
		}
		else if(getAdvanceConfiguration()!=null && getAdvanceConfiguration().usingOnlyEntitiesNearestVerb())
		{
			processingEntitiesNearestToVerb(verb,verbPosition, startPosition, endPosition, keySetEnt,treeEntitiesPositions, leftentities, rightentities,setOfEntitiesAnnotations, setOfRightAnnotations,verbClassidfication);
		}
		else if(getAdvanceConfiguration()!=null && getAdvanceConfiguration().usingVerbEntitiesDistance() && getAdvanceConfiguration().getVerbEntitieMaxDistance() > 0)
		{
			processindEntitiesWithMaxVerbDistance(document,verb,verbPosition, startPosition, endPosition, keySetEnt,treeEntitiesPositions, leftentities, rightentities,setOfEntitiesAnnotations, setOfRightAnnotations,getAdvanceConfiguration().getVerbEntitieMaxDistance());
		}
		else
		{
			processindEntitiesWithoutRestrictions(verb,verbPosition, startPosition, endPosition,keySetEnt, treeEntitiesPositions, leftentities, rightentities,setOfEntitiesAnnotations, setOfRightAnnotations);
		}
		if(getAdvanceConfiguration()!=null && getAdvanceConfiguration().getRelationsType()!=null && getAdvanceConfiguration().getRelationsType().size() > 0)
		{
			withRelationTypeFilter(relations, verbPosition, treeverb,leftentities, rightentities, verb, eventProperties,getAdvanceConfiguration().getRelationsType());
		}
		else
		{
			withouRelationTypeFilter(relations, verbPosition, treeverb,leftentities, rightentities, verb, eventProperties);
		}
	}
	
	

	protected void withRelationTypeFilter(List<IEventAnnotation> relations,
			Long verbPosition, Map<Long, IVerbInfo> treeverb,
			List<IEntityAnnotation> leftentities,
			List<IEntityAnnotation> rightentities, String verb,
			IEventProperties eventProperties,
			SortedSet<IRelationsType> relationsType) {
		IEventAnnotation event;
		for(IEntityAnnotation left:leftentities)
		{
			for(IEntityAnnotation right:rightentities)
			{

				if(!left.equals(right))
				{
					IRelationsType rt = new RelationType(left.getClassAnnotationID(), right.getClassAnnotationID());
					if(relationsType.contains(rt))
					{			
						event = new EventAnnotation(-1, treeverb.get(verbPosition).getStartOffset(), treeverb.get(verbPosition).getEndOffset(),
								"", left, right, verb,-1,"", eventProperties);
						relations.add(event);
					}
				}
			}
		}
	}

	protected void withouRelationTypeFilter(List<IEventAnnotation> relations,
			Long verbPosition, Map<Long, IVerbInfo> treeverb,
			List<IEntityAnnotation> leftentities,
			List<IEntityAnnotation> rightentities, String verb,
			IEventProperties eventProperties) {
		IEventAnnotation event;
		for(IEntityAnnotation left:leftentities)
		{
			for(IEntityAnnotation right:rightentities)
			{
				
				if(!left.equals(right))
				{
					event = new EventAnnotation(-1, treeverb.get(verbPosition).getStartOffset(), treeverb.get(verbPosition).getEndOffset(),
							"", left, right, verb,-1,"", eventProperties);
					relations.add(event);
				}
			}
		}
	}

	
	protected void processindOnlyNearestEntities(Long verbPosition,
			Long startPosition, Long endPosition, List<Long> keySetEnt,
			Map<Long, IEntityAnnotation> treeEntitiesPositions,
			List<IEntityAnnotation> leftentities,
			List<IEntityAnnotation> rightentities,
			SortedSet<IEntityAnnotation> setOfEntitiesAnnotations,
			SortedSet<IEntityAnnotation> setOfRightAnnotations) {
		IEntityAnnotation closeastToVerbAtLeft = null;
		IEntityAnnotation closeastToVerbAtRight = null;
		for(int i=0;i<keySetEnt.size();i++)
		{

			if(keySetEnt.get(i)>=startPosition&&keySetEnt.get(i)<=endPosition)
			{
				// left
				if(keySetEnt.get(i)<=verbPosition)
				{
					if(closeastToVerbAtLeft==null)
					{
						closeastToVerbAtLeft = treeEntitiesPositions.get(keySetEnt.get(i));
					}
					else
					{
						long actualverbDistance = verbPosition - closeastToVerbAtLeft.getEndOffset();
						long candidateVerbDistance = verbPosition - treeEntitiesPositions.get(keySetEnt.get(i)).getEndOffset();
						if(candidateVerbDistance < actualverbDistance)
						{
							closeastToVerbAtLeft = treeEntitiesPositions.get(keySetEnt.get(i));
						}
					}
				}
				// right
				else
				{
					if(closeastToVerbAtRight == null)
					{
						closeastToVerbAtRight = treeEntitiesPositions.get(keySetEnt.get(i));
					}
					else
					{
						long actualverbDistance =  closeastToVerbAtRight.getEndOffset() - verbPosition;
						long candidateVerbDistance = treeEntitiesPositions.get(keySetEnt.get(i)).getEndOffset() - verbPosition;
						if(candidateVerbDistance < actualverbDistance)
						{
							closeastToVerbAtRight = treeEntitiesPositions.get(keySetEnt.get(i));
						}
					}
				}
			}
		}
		if(closeastToVerbAtLeft!=null && closeastToVerbAtRight!=null)
		{
			leftentities.add(closeastToVerbAtLeft);
			rightentities.add(closeastToVerbAtRight);
		}
		
	}

	public String toString()
	{
		return "Binary Verb Limitaion RE Model";	
	}

}
