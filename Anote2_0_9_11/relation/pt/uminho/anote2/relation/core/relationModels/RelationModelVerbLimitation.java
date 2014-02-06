package pt.uminho.anote2.relation.core.relationModels;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
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
import pt.uminho.anote2.datastructures.textprocessing.TextProcessor;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.process.IE.re.IRelationModel;
import pt.uminho.anote2.process.IE.re.clue.IVerbInfo;
import pt.uminho.anote2.relation.configuration.IRERelationAdvancedConfiguration;
import pt.uminho.anote2.relation.core.relationModels.specialproperties.VerbClassificationInSentenceEnum;
import pt.uminho.gate.process.IGatePosTagger;
/**
 * @author Hugo Costa
 * 
« * 			   - Limitation verb v1.0 
 *
 */
public class RelationModelVerbLimitation extends RelationModelDefault implements IRelationModel{



	public RelationModelVerbLimitation(ICorpus corpus, IIEProcess nerProcess,IGatePosTagger postagger,IRERelationAdvancedConfiguration advancedConfiguration) {
		super(corpus, nerProcess, postagger,advancedConfiguration);
	}
	
	public Set<String> getRelationTerminations() 
	{
		HashSet<String> terminations= new HashSet<String>(); 
		terminations.add(".");
		terminations.add("!");
		terminations.add(",");
		terminations.add(":");
		terminations.add(";");
		terminations.add("?");
		return terminations;		
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
		IEventAnnotation eventAnnotation;
		for(int i=0;i<verbsList.size();i++)
		{
			if(entitiesList.size()>1)
			{
				if(i==0)
				{
					// Just one verb
					if(i==verbsList.size()-1)
					{
						eventAnnotation = extractRelation(document,verbsList.get(i),entitiesList.get(0),entitiesList.get(entitiesList.size()-1),
								treeverbPositions,entitiesList,treeEntitiesPositions,VerbClassificationInSentenceEnum.UNIQUE);
					}
					// The first One
					else
					{
						
						eventAnnotation = extractRelation(document,verbsList.get(i),entitiesList.get(0),verbsList.get(i+1),treeverbPositions,entitiesList,
								treeEntitiesPositions,VerbClassificationInSentenceEnum.FIRST);
					}			
				}
				// last verb
				else if(i==verbsList.size()-1)
				{
					eventAnnotation = extractRelation(document,verbsList.get(i),verbsList.get(i-1),entitiesList.get(entitiesList.size()-1),
							treeverbPositions,entitiesList,treeEntitiesPositions,VerbClassificationInSentenceEnum.LAST);

				}
				// verb in the middle of text
				else
				{
					eventAnnotation = extractRelation(document,verbsList.get(i),verbsList.get(i-1),verbsList.get(i+1),treeverbPositions,entitiesList
							,treeEntitiesPositions,VerbClassificationInSentenceEnum.MIDDLE);

				}
				if(eventAnnotation.getEntitiesAtLeft().size()+eventAnnotation.getEntitiesAtRight().size()>1)
				{
					// Remove binary relation with the same entity in right and left
					if(!(eventAnnotation.getEntitiesAtLeft().size()==1 && eventAnnotation.getEntitiesAtRight().size() == 1 && eventAnnotation.getEntitiesAtLeft().get(0).equals(eventAnnotation.getEntitiesAtRight().get(0))))
					{
						relations.add(eventAnnotation);	
					}
				}
			}		
		}
		return relations;
	}
	
	private IEventAnnotation extractRelation(IAnnotatedDocument document,Long verbPosition,Long startPosition,Long endPosition, Map<Long,IVerbInfo> treeverb,List<Long> keySetEnt,
			Map<Long, IEntityAnnotation> treeEntitiesPositions,VerbClassificationInSentenceEnum verbClassidfication) throws SQLException, DatabaseLoadDriverException
	{
		List<IEntityAnnotation> leftentities=new ArrayList<IEntityAnnotation>();
		List<IEntityAnnotation> rightentities=new ArrayList<IEntityAnnotation>();
		DirectionallyEnum dir = treeverb.get(verbPosition).getDirectionality();
		PolarityEnum polarity = treeverb.get(verbPosition).getPolarity();
		String verb = treeverb.get(verbPosition).getVerb();
		SortedSet<IEntityAnnotation> setOfEntitiesAnnotations = new TreeSet<IEntityAnnotation>();
		SortedSet<IEntityAnnotation> setOfRightAnnotations = new TreeSet<IEntityAnnotation>();
		if(getAdvanceConfiguration()!=null && getAdvanceConfiguration().usingOnlyEntitiesNearestVerb())
		{
			processingEntitiesNearestToVerb(verb,verbPosition, startPosition, endPosition, keySetEnt,treeEntitiesPositions, leftentities, rightentities,setOfEntitiesAnnotations, setOfRightAnnotations,verbClassidfication);
		}
		else if(getAdvanceConfiguration()!=null && getAdvanceConfiguration().usingVerbEntitiesDistance() && getAdvanceConfiguration().getVerbEntitieMaxDistance() > 0)
		{
			processindEntitiesWithMaxVerbDistance(document,verb,verbPosition, startPosition, endPosition, keySetEnt,treeEntitiesPositions, leftentities, rightentities,setOfEntitiesAnnotations, setOfRightAnnotations,getAdvanceConfiguration().getVerbEntitieMaxDistance());
		}
		else
		{
			processindEntitiesWithoutRestrictions(verb,verbPosition, startPosition, endPosition, keySetEnt,treeEntitiesPositions, leftentities, rightentities,setOfEntitiesAnnotations, setOfRightAnnotations);
		}
		String lemma = treeverb.get(verbPosition).getLemma();
		IEventProperties eventProperties = new EventProperties();
		eventProperties.setDirectionally(dir);
		eventProperties.setLemma(lemma);
		eventProperties.setPolarity(polarity);	
		IEventAnnotation event = new EventAnnotation(-1, treeverb.get(verbPosition).getStartOffset(), treeverb.get(verbPosition).getEndOffset(),
				"", leftentities, rightentities, verb,-1,"", eventProperties);
		return event;
	}

	protected void processingEntitiesNearestToVerb(String verb,
			Long verbPosition, Long startPosition, Long endPosition,
			List<Long> keySetEnt,
			Map<Long, IEntityAnnotation> treeEntitiesPositions,
			List<IEntityAnnotation> leftentities,
			List<IEntityAnnotation> rightentities,
			SortedSet<IEntityAnnotation> setOfEntitiesAnnotations,
			SortedSet<IEntityAnnotation> setOfRightAnnotations,
			VerbClassificationInSentenceEnum verbClassidfication) {
		for(int i=0;i<keySetEnt.size();i++)
		{
			
			if(keySetEnt.get(i)>=startPosition&&keySetEnt.get(i)<=endPosition)
			{
				// left Entities
				if(treeEntitiesPositions.get(keySetEnt.get(i)).getEndOffset()<verbPosition)
				{
					// Entity proximity To verb
					// Verb is unique or the first one no left entities restriction
					if(verbClassidfication == VerbClassificationInSentenceEnum.UNIQUE || verbClassidfication == VerbClassificationInSentenceEnum.FIRST)
					{
						if(!setOfEntitiesAnnotations.contains(treeEntitiesPositions.get(keySetEnt.get(i))))
						{
							leftentities.add(treeEntitiesPositions.get(keySetEnt.get(i)));
							setOfEntitiesAnnotations.add(treeEntitiesPositions.get(keySetEnt.get(i)));
						}
					}
					else
					{
						long entityDistancePreviousVerb = treeEntitiesPositions.get(keySetEnt.get(i)).getStartOffset() - startPosition;
						long entityDistanceThisVerb = verbPosition - treeEntitiesPositions.get(keySetEnt.get(i)).getEndOffset();
						if(entityDistanceThisVerb <= entityDistancePreviousVerb)
						{
							if(!setOfEntitiesAnnotations.contains(treeEntitiesPositions.get(keySetEnt.get(i))))
							{
								leftentities.add(treeEntitiesPositions.get(keySetEnt.get(i)));
								setOfEntitiesAnnotations.add(treeEntitiesPositions.get(keySetEnt.get(i)));
							}
						}	
					}
				}
				// right Entities
				else if(treeEntitiesPositions.get(keySetEnt.get(i)).getStartOffset() > verbPosition + verb.length())
				{
					// Entity proximity To verb
					// Verb is unique or the last one no left entities restriction
					if(verbClassidfication == VerbClassificationInSentenceEnum.UNIQUE || verbClassidfication == VerbClassificationInSentenceEnum.LAST)
					{
						if(!setOfRightAnnotations.contains(treeEntitiesPositions.get(keySetEnt.get(i))))
						{
							rightentities.add(treeEntitiesPositions.get(keySetEnt.get(i)));
							setOfRightAnnotations.add(treeEntitiesPositions.get(keySetEnt.get(i)));
						}
					}
					else
					{
						long entityDistanceFutherVerb = endPosition - treeEntitiesPositions.get(keySetEnt.get(i)).getEndOffset();
						long entityDistanceThisVerb = treeEntitiesPositions.get(keySetEnt.get(i)).getStartOffset() - verbPosition-verb.length();
						if(entityDistanceThisVerb <= entityDistanceFutherVerb)
						{
							if(!setOfRightAnnotations.contains(treeEntitiesPositions.get(keySetEnt.get(i))))
							{
								rightentities.add(treeEntitiesPositions.get(keySetEnt.get(i)));
								setOfRightAnnotations.add(treeEntitiesPositions.get(keySetEnt.get(i)));
							}
						}
					}

				}
			}
		}		
		
	}

	protected void processindEntitiesWithMaxVerbDistance(IAnnotatedDocument document,String verb, Long verbPosition,
			Long startPosition, Long endPosition, List<Long> keySetEnt,
			Map<Long, IEntityAnnotation> treeEntitiesPositions,
			List<IEntityAnnotation> leftentities,
			List<IEntityAnnotation> rightentities,
			SortedSet<IEntityAnnotation> setOfEntitiesAnnotations,
			SortedSet<IEntityAnnotation> setOfRightAnnotations,
			int verbEntitieMaxWordDistance) throws SQLException, DatabaseLoadDriverException {
		for(int i=0;i<keySetEnt.size();i++)
		{

			if(keySetEnt.get(i)>=startPosition&&keySetEnt.get(i)<=endPosition)
			{				
				// left
				if(treeEntitiesPositions.get(keySetEnt.get(i)).getEndOffset()<verbPosition)
				{
					// Verb Distance Condition in Words
					if(numberOffWordsBetweenTwoOffsets(document,treeEntitiesPositions.get(keySetEnt.get(i)).getEndOffset(),verbPosition)<=verbEntitieMaxWordDistance)
					{
						if(!setOfEntitiesAnnotations.contains(treeEntitiesPositions.get(keySetEnt.get(i))))
						{
							leftentities.add(treeEntitiesPositions.get(keySetEnt.get(i)));
							setOfEntitiesAnnotations.add(treeEntitiesPositions.get(keySetEnt.get(i)));
						}
					}
				}
				// right
				else if(treeEntitiesPositions.get(keySetEnt.get(i)).getStartOffset() > verbPosition + verb.length())
				{
					// Verb Distance Condition in Words
					if(numberOffWordsBetweenTwoOffsets(document,verbPosition+verb.length(),treeEntitiesPositions.get(keySetEnt.get(i)).getStartOffset())<=verbEntitieMaxWordDistance)
					{
						if(!setOfRightAnnotations.contains(treeEntitiesPositions.get(keySetEnt.get(i))))
						{
							rightentities.add(treeEntitiesPositions.get(keySetEnt.get(i)));
							setOfRightAnnotations.add(treeEntitiesPositions.get(keySetEnt.get(i)));
						}
					}
				}
			}
		}		
	}
	
	private int numberOffWordsBetweenTwoOffsets(IAnnotatedDocument document,long offsetStart,long longoffsetEnd) throws SQLException, DatabaseLoadDriverException
	{
		int words = TextProcessor.getNumberOFwords(document.getDocumetAnnotationText().substring((int)offsetStart, (int)longoffsetEnd));
		return words;
		
	}

	protected void processindEntitiesWithoutRestrictions(String verb,Long verbPosition, Long startPosition,
			Long endPosition, List<Long> keySetEnt,
			Map<Long, IEntityAnnotation> treeEntitiesPositions,
			List<IEntityAnnotation> leftentities,
			List<IEntityAnnotation> rightentities,
			SortedSet<IEntityAnnotation> setOfEntitiesAnnotations,
			SortedSet<IEntityAnnotation> setOfRightAnnotations) {
		for(int i=0;i<keySetEnt.size();i++)
		{

			if(keySetEnt.get(i)>=startPosition&&keySetEnt.get(i)<=endPosition)
			{
				// left
				if(treeEntitiesPositions.get(keySetEnt.get(i)).getEndOffset()<verbPosition)
				{
					if(!setOfEntitiesAnnotations.contains(treeEntitiesPositions.get(keySetEnt.get(i))))
					{
						leftentities.add(treeEntitiesPositions.get(keySetEnt.get(i)));
						setOfEntitiesAnnotations.add(treeEntitiesPositions.get(keySetEnt.get(i)));
					}
				}
				// right
				else if(treeEntitiesPositions.get(keySetEnt.get(i)).getStartOffset() > verbPosition + verb.length())
				{
					if(!setOfRightAnnotations.contains(treeEntitiesPositions.get(keySetEnt.get(i))))
					{
						rightentities.add(treeEntitiesPositions.get(keySetEnt.get(i)));
						setOfRightAnnotations.add(treeEntitiesPositions.get(keySetEnt.get(i)));
					}
				}
			}
		}
	}
	
	public String toString()
	{
		return "Verb Limitation RE Model";
		
	}
}
