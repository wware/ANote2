package pt.uminho.anote2.relation.core.relationModels;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.process.IE.re.IRelationModel;
import pt.uminho.anote2.process.IE.re.clue.IVerbInfo;
import pt.uminho.anote2.relation.configuration.IRERelationAdvancedConfiguration;
import pt.uminho.gate.process.IGatePosTagger;

/**
 * 
 * @author Hugo Costa
 *
 */
public class RelationModelSimple extends RelationModelDefault implements IRelationModel{


	public RelationModelSimple(ICorpus corpus, IIEProcess nerProcess,IGatePosTagger postagger,IRERelationAdvancedConfiguration advancedConfiguration) {
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

	public List<IEventAnnotation> extractSentenceRelation(IAnnotatedDocument document,List<IEntityAnnotation> semanticLayer, IGenericPair<List<IVerbInfo>, List<Long>> sintatic) throws SQLException, DatabaseLoadDriverException{
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
			DirectionallyEnum dir = treeverbPositions.get(verbPosition).getDirectionality();
			PolarityEnum polarity = treeverbPositions.get(verbPosition).getPolarity();			
			leftentities=new ArrayList<IEntityAnnotation>();
			rightentities=new ArrayList<IEntityAnnotation>();		
			Iterator<Long> itEnt = treeEntitiesPositions.keySet().iterator();
			SortedSet<IEntityAnnotation> setOfEntitiesAnnotations = new TreeSet<IEntityAnnotation>();
			SortedSet<IEntityAnnotation> setOfRightAnnotations = new TreeSet<IEntityAnnotation>();
			while(itEnt.hasNext())
			{

				Long entityPositon = itEnt.next();	
				if(treeEntitiesPositions.get(entityPositon).getEndOffset()<verbPosition)
				{
					if(!setOfEntitiesAnnotations.contains(treeEntitiesPositions.get(entityPositon)))
					{
						leftentities.add(treeEntitiesPositions.get(entityPositon));
						setOfEntitiesAnnotations.add(treeEntitiesPositions.get(entityPositon));
					}				
				}
				else if(entityPositon > verbPosition + verb.length())
				{
					if(!setOfRightAnnotations.contains(treeEntitiesPositions.get(entityPositon)))
					{
						rightentities.add(treeEntitiesPositions.get(entityPositon));
						setOfRightAnnotations.add(treeEntitiesPositions.get(entityPositon));
					}
				}
			}
			eventProperties = new EventProperties();
			eventProperties.setDirectionally(dir);
			eventProperties.setLemma(lemma);
			eventProperties.setPolarity(polarity);		
			IEventAnnotation event = new EventAnnotation(-1, treeverbPositions.get(verbPosition).getStartOffset(), treeverbPositions.get(verbPosition).getEndOffset(),
					"", leftentities, rightentities, verb,-1,"", eventProperties);
			if(leftentities.size()+rightentities.size()>1)
			{
				// Remove binary relation with the same entity in right and left
				if(!(leftentities.size()==1 && rightentities.size() == 1 && leftentities.get(0).equals(rightentities.get(0))))
				{					
					relations.add(event);
				}
			}
		}	
		return relations;
	}
	
	public String toString()
	{
		return "Simple RE Model";
		
	}
}
