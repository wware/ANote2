package pt.uminho.anote2.relation.core.relationModels;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import process.IGatePosTagger;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.annotation.IEventProperties;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.datastructures.annotation.EventAnnotation;
import pt.uminho.anote2.datastructures.annotation.EventProperties;
import pt.uminho.anote2.datastructures.utils.GenericPair;
import pt.uminho.anote2.process.IE.INERProcess;
import pt.uminho.anote2.process.IE.re.IRelationModel;
import pt.uminho.anote2.process.IE.re.IVerbInfo;
/**
 * @author Hugo Costa
 * 
 * Objectivo : Classe que implementa um modelo de relação
 * 			   - Limitation verb v1.0 
 *
 */
public class RelationModelVerbLimitation extends RelationModelDefault implements IRelationModel{



	public RelationModelVerbLimitation(ICorpus corpus, INERProcess nerprocess,
			IGatePosTagger postagger) {
		super(corpus, nerprocess, postagger);
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

	public List<IEventAnnotation> extractSentenceRelation(List<IEntityAnnotation> semanticLayer, GenericPair<List<IVerbInfo>, List<Long>> sintatic) {
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
						eventAnnotation = extractRelation(verbsList.get(i),entitiesList.get(0),entitiesList.get(entitiesList.size()-1),
								treeverbPositions,entitiesList,treeEntitiesPositions);
					}
					// The first One
					else
					{
					
						eventAnnotation = extractRelation(verbsList.get(i),entitiesList.get(0),verbsList.get(i+1),treeverbPositions,entitiesList,
								treeEntitiesPositions);
					}			
				}
				// last verb
				else if(i==verbsList.size()-1)
				{
					eventAnnotation = extractRelation(verbsList.get(i),verbsList.get(i-1),entitiesList.get(entitiesList.size()-1),
							treeverbPositions,entitiesList,treeEntitiesPositions);

				}
				// verb in the midle of text
				else
				{
					eventAnnotation = extractRelation(verbsList.get(i),verbsList.get(i-1),verbsList.get(i+1),treeverbPositions,entitiesList
							,treeEntitiesPositions);

				}
				if(eventAnnotation.getEntitiesAtLeft().size()+eventAnnotation.getEntitiesAtRight().size()>1)
				{
					relations.add(eventAnnotation);		
				}
			}		
		}
		return relations;
	}
	
	private IEventAnnotation extractRelation(Long verbPosition,Long startPosition,Long endPosition, Map<Long,IVerbInfo> treeverb,List<Long> keySetEnt,
			Map<Long, IEntityAnnotation> treeEntitiesPositions)
	{
		List<IEntityAnnotation> leftentities=new ArrayList<IEntityAnnotation>();
		List<IEntityAnnotation> rightentities=new ArrayList<IEntityAnnotation>();
		boolean dir = treeverb.get(verbPosition).getDirectionality();
		int polarity = treeverb.get(verbPosition).getPolarity();				
		for(int i=0;i<keySetEnt.size();i++)
		{
			if(keySetEnt.get(i)>=startPosition&&keySetEnt.get(i)<=endPosition)
			{
				// A esquerda a entidade
				if(keySetEnt.get(i)<=verbPosition)
				{
					leftentities.add(treeEntitiesPositions.get(keySetEnt.get(i)));
				}
				// a direita
				else
				{
					rightentities.add(treeEntitiesPositions.get(keySetEnt.get(i)));
				}
			}
		}
		String verb = treeverb.get(verbPosition).getVerb();
		String lemma = treeverb.get(verbPosition).getLemma();
		IEventProperties eventProperties = new EventProperties();
		eventProperties.setDirectionally(dir);
		eventProperties.setLemma(lemma);
		eventProperties.setPolarity(polarity);		
		IEventAnnotation event = new EventAnnotation(-1, treeverb.get(verbPosition).getStartOffset(), treeverb.get(verbPosition).getEndOffset(),
				"", leftentities, rightentities, verb,-1,"", eventProperties);
		return event;
	}
}
