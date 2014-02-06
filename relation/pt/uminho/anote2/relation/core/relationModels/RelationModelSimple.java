package pt.uminho.anote2.relation.core.relationModels;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
 * 
 * @author Hugo Costa
 *
 */
public class RelationModelSimple extends RelationModelDefault implements IRelationModel{


	public RelationModelSimple(ICorpus corpus, INERProcess nerprocess,
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

	public List<IEventAnnotation> extractSentenceRelation( List<IEntityAnnotation> semanticLayer, GenericPair<List<IVerbInfo>, List<Long>> sintatic) {
		Map<Long, IEntityAnnotation> treeEntitiesPositions = RelationModelutils.getEntitiesPosition(semanticLayer); // offset->entityID
		List<IVerbInfo> verbsInfo = sintatic.getX();
		Map<Long, IVerbInfo> treeverbPositions = getPostagger().getVerbsPosition(verbsInfo); 		
		List<IEventAnnotation> relations = new ArrayList<IEventAnnotation>();	
		Iterator<Long> itverbs = treeverbPositions.keySet().iterator();	
		List<IEntityAnnotation> leftentities;
		List<IEntityAnnotation> rightentities;
		IEventProperties eventProperties;
		while(itverbs.hasNext()) 
		{  
			Long verbPosition = itverbs.next();	
			String verb = treeverbPositions.get(verbPosition).getVerb();
			String lemma = treeverbPositions.get(verbPosition).getLemma();
			boolean dir = treeverbPositions.get(verbPosition).getDirectionality();
			int polarity = treeverbPositions.get(verbPosition).getPolarity();			
			leftentities=new ArrayList<IEntityAnnotation>();
			rightentities=new ArrayList<IEntityAnnotation>();		
			Iterator<Long> itEnt = treeEntitiesPositions.keySet().iterator();
			while(itEnt.hasNext())
			{
				Long entityPositon = itEnt.next();	
				if(entityPositon<verbPosition)
				{
					leftentities.add(treeEntitiesPositions.get(entityPositon));
				}
				else
				{
					rightentities.add(treeEntitiesPositions.get(entityPositon));
				}
			}
			eventProperties = new EventProperties();
			eventProperties.setDirectionally(dir);
			eventProperties.setLemma(lemma);
			eventProperties.setPolarity(polarity);		
			IEventAnnotation event = new EventAnnotation(-1, treeverbPositions.get(verbPosition).getStartOffset(), treeverbPositions.get(verbPosition).getEndOffset(),
					"", leftentities, rightentities, verb,-1,"", eventProperties);
			if(leftentities.size()+rightentities.size()>1)
				relations.add(event);
		}	
		return relations;
	}
}
