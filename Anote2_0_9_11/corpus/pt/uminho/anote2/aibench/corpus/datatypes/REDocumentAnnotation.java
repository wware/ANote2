package pt.uminho.anote2.aibench.corpus.datatypes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.annotation.AnnotationPosition;
import pt.uminho.anote2.datastructures.annotation.AnnotationPositions;
import pt.uminho.anote2.datastructures.process.IEProcess;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;

@Datatype(structure = Structure.SIMPLE,
		 namingMethod="getName",
		 removable=true,
		 renamed=false,
		 autoOpen=true
)
public class REDocumentAnnotation extends NERDocumentAnnotation{
	
	private AnnotationPositions annotAllEventndEntitiesPositions;
	private SortedMap<AnnotationPosition,Set<IEventAnnotation>> verbRelations; // Verb associated whit relations;
	private SortedMap<AnnotationPosition,Set<IEventAnnotation>> entityRelations; // entity associated whit relations;
	
	public REDocumentAnnotation(IPublication pub) {
		super(pub);
	}

	
	public REDocumentAnnotation(IEProcess reProcess, ICorpus corpus,IDocument doc) {
		super(reProcess,corpus,doc);
	}


	public String getName()
	{
		return "ID :"+getID()+" Corpus :"+getCorpus().getID()+" Process "+getProcess().getID();
	}

	public AnnotationPositions getEventAnnotationPositions() throws SQLException, DatabaseLoadDriverException
	{
		if(annotAllEventndEntitiesPositions==null)
		{
			annotAllEventndEntitiesPositions = new AnnotationPositions();
			entityRelations = new TreeMap<AnnotationPosition, Set<IEventAnnotation>>();
			verbRelations = new TreeMap<AnnotationPosition, Set<IEventAnnotation>>();
			List<IEventAnnotation> ent = getEventAnnotations();
			for(int i=0;i<ent.size();i++)
			{
				IEventAnnotation event = ent.get(i);
				AnnotationPosition pos = new AnnotationPosition((int)event.getStartOffset(),(int)event.getEndOffset());
				if(event.getEventClue()!=null && !event.getEventClue().equals(""))
				{
					annotAllEventndEntitiesPositions.addAnnotationWhitConflicts(pos, event);
					processClue(event,verbRelations);
				}
				List<IEntityAnnotation> left = event.getEntitiesAtLeft();
				processEntities(event,entityRelations,left);
				List<IEntityAnnotation> right = event.getEntitiesAtRight();
				processEntities(event,entityRelations,right);
			}
		}
		return annotAllEventndEntitiesPositions;
	}

	private void processClue(IEventAnnotation event,SortedMap<AnnotationPosition, Set<IEventAnnotation>> verbRelations) {
		AnnotationPosition pos = new AnnotationPosition((int)event.getStartOffset(), (int) event.getEndOffset());
		if(!verbRelations.containsKey(pos))
		{
			verbRelations.put(pos, new HashSet<IEventAnnotation>());
		}
		verbRelations.get(pos).add(event);
	}


	private void processEntities(IEventAnnotation event, SortedMap<AnnotationPosition, Set<IEventAnnotation>> entityRelations, List<IEntityAnnotation> entities) {
		for(IEntityAnnotation ent:entities)
		{
			AnnotationPosition pos = new AnnotationPosition((int)ent.getStartOffset(), (int) ent.getEndOffset());
			if(!entityRelations.containsKey(pos))
			{
				entityRelations.put(pos, new HashSet<IEventAnnotation>());
			}
			entityRelations.get(pos).add(event);
		}
	}


	public Map<AnnotationPosition, Set<IEventAnnotation>> getEntityRelationSet() {
		return entityRelations;
	}


	public Map<AnnotationPosition, Set<IEventAnnotation>> getVerbRelations() {
		return verbRelations;
	}


	public void setVerbRelations(SortedMap<AnnotationPosition, Set<IEventAnnotation>> verbRelations) {
		this.verbRelations = verbRelations;
	}
	
	public void cleanEmptyClues()
	{
		// For Clues
		List<AnnotationPosition> positionsToRemove = new ArrayList<AnnotationPosition>();
		for(AnnotationPosition pos:verbRelations.keySet())
		{
			if(verbRelations.get(pos).size()==0)
			{
				positionsToRemove.add(pos);
			}
		}
		for(AnnotationPosition posToRem :positionsToRemove)
		{
			verbRelations.remove(posToRem);
			annotAllEventndEntitiesPositions.removeAnnotation(posToRem);
		}
		// For Entities
		List<AnnotationPosition> positionsEntiesToRemove = new ArrayList<AnnotationPosition>();
		for(AnnotationPosition pos:entityRelations.keySet())
		{
			if(entityRelations.get(pos).size()==0)
			{
				positionsEntiesToRemove.add(pos);
			}
		}
		for(AnnotationPosition posToRem :positionsEntiesToRemove)
		{
			entityRelations.remove(posToRem);
		}
	}


	public void removeAllEventReferences(IEventAnnotation event) {
		// Remove From ClueList
		AnnotationPosition eventPostition = new AnnotationPosition((int)event.getStartOffset(), (int)event.getEndOffset());
		if(getVerbRelations().containsKey(eventPostition))
		{
			getVerbRelations().get(eventPostition).remove(event);
			//Remove Annotation From Event Annotation
			if(getVerbRelations().get(eventPostition).size()==0)
			{
				annotAllEventndEntitiesPositions.removeAnnotation(eventPostition);
			}
		}

		// Remove reference to all left relation entities
		for(IEntityAnnotation left : event.getEntitiesAtLeft())
		{
			getEntityRelationSet().get(new AnnotationPosition((int)left.getStartOffset(), (int)left.getEndOffset())).remove(event);
		}
		// Remove reference to all right relation entities
		for(IEntityAnnotation right : event.getEntitiesAtRight())
		{
			getEntityRelationSet().get(new AnnotationPosition((int)right.getStartOffset(), (int)right.getEndOffset())).remove(event);
		}	
	}


	public void addEventReferences(IEventAnnotation event) throws SQLException, DatabaseLoadDriverException {
		AnnotationPosition eventPostition = new AnnotationPosition((int)event.getStartOffset(), (int)event.getEndOffset());
		if(eventPostition.diference()>0)
		{
			if(!getVerbRelations().containsKey(eventPostition))
			{
				getVerbRelations().put(eventPostition, new HashSet<IEventAnnotation>());
			}
			getVerbRelations().get(eventPostition).add(event);
			if(!getEventAnnotationPositions().containsKey(eventPostition))
			{
				getEventAnnotationPositions().addAnnotationWhitConflicts(eventPostition, event);
			}
		}
		
		// Add reference to all left relation entities
		for(IEntityAnnotation left : event.getEntitiesAtLeft())
		{
			AnnotationPosition entAnnotationPosition = new AnnotationPosition((int)left.getStartOffset(), (int)left.getEndOffset());
			if(!getEntityRelationSet().containsKey(entAnnotationPosition))
			{
				getEntityRelationSet().put(entAnnotationPosition, new HashSet<IEventAnnotation>());
			}
			getEntityRelationSet().get(entAnnotationPosition).add(event);
		}
		
		// Add reference to all right relation entities
		for(IEntityAnnotation right : event.getEntitiesAtRight())
		{
			AnnotationPosition entAnnotationPosition = new AnnotationPosition((int)right.getStartOffset(), (int)right.getEndOffset());
			if(!getEntityRelationSet().containsKey(entAnnotationPosition))
			{
				getEntityRelationSet().put(entAnnotationPosition, new HashSet<IEventAnnotation>());
			}
			getEntityRelationSet().get(entAnnotationPosition).add(event);
		}	
		
	}
	

}
