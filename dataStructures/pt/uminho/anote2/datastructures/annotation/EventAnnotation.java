package pt.uminho.anote2.datastructures.annotation;

import java.util.ArrayList;
import java.util.List;

import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.annotation.IEventProperties;


public class EventAnnotation extends Annotation implements IEventAnnotation{

	private List<IEntityAnnotation> entitiesAtLeft;
	private List<IEntityAnnotation> entitiesAtRight;
	private String eventClue;
	private int ontologicalClassificationID;
	private String ontologicalClass;
	private IEventProperties eventProperties;
	
	public EventAnnotation(int id,long start, long end, String type) {
		super(id,start, end);
		this.entitiesAtLeft=new ArrayList<IEntityAnnotation>();
		this.entitiesAtRight=new ArrayList<IEntityAnnotation>();
		this.eventClue="";
		this.ontologicalClassificationID=-1;
		this.eventProperties = null;
	}
	
	public EventAnnotation(int id,long start, long end, String type,
			List<IEntityAnnotation> left,List<IEntityAnnotation> right,
			String clue,int ontologicalClassID,String ontologicalClass,
			IEventProperties eventProperties) {
		super(id,start, end);
		this.entitiesAtLeft=left;
		this.entitiesAtRight=right;
		this.eventClue=clue;
		this.ontologicalClass=ontologicalClass;
		this.ontologicalClassificationID=ontologicalClassID;
		this.eventProperties=eventProperties;
	}

	public List<IEntityAnnotation> getEntitiesAtLeft() {
		return this.entitiesAtLeft;
	}

	public List<IEntityAnnotation> getEntitiesAtRight() {
		return this.entitiesAtRight;
	}

	public String getEventClue() {
		return this.eventClue;
	}

	public void setEntitiesAtLeft(List<IEntityAnnotation> entitiesAtLeft) {
		this.entitiesAtLeft = entitiesAtLeft;
	}

	public void setEntitiesAtRight(List<IEntityAnnotation> entitiesAtRight) {
		this.entitiesAtRight = entitiesAtRight;
	}

	public void setEventClue(String eventClue) {
		this.eventClue = eventClue;
	}

	public int getOntologicalClassID() {
		return this.ontologicalClassificationID;
	}

	public IEventProperties getEventProperties() {
		return eventProperties;
	}

	public String getOntologycalClass() {
		return this.ontologicalClass;
	}

	public void addEntityAtLeft(IEntityAnnotation ent) {
		if(entitiesAtLeft == null)
			entitiesAtLeft = new ArrayList<IEntityAnnotation>();
		entitiesAtLeft.add(ent);
	}

	public void addEntityAtRight(IEntityAnnotation ent) {
		if(entitiesAtRight == null)
			entitiesAtRight = new ArrayList<IEntityAnnotation>();
		entitiesAtRight.add(ent);	
	}

	public void addEventProperty(String key, String value) {
		if(eventProperties == null)
		{
			eventProperties = new EventProperties();
		}
		if(key.equals("lemma"))
		{
			eventProperties.setLemma(value);
		}
		else if(key.equals("polarity"))
		{
			eventProperties.setPolarity(Integer.valueOf(value));
		}
		else if(key.equals("directionally"))
		{
			eventProperties.setDirectionally(Boolean.valueOf(value));
		}
		else
		{
			eventProperties.addProperty(key, value);
		}
		
	}
	
	public IEventAnnotation clone(){
		return new EventAnnotation(this.getID(), this.getStartOffset(), this.getEndOffset(), this.getType(), this.getEntitiesAtLeft(), this.getEntitiesAtRight(), this.getEventClue(), this.getOntologicalClassID(), this.getOntologycalClass(), this.getEventProperties());
	}
}
