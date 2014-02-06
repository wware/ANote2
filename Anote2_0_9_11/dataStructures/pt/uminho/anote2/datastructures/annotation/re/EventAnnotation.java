package pt.uminho.anote2.datastructures.annotation.re;

import java.util.ArrayList;
import java.util.List;

import pt.uminho.anote2.core.annotation.DirectionallyEnum;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.annotation.IEventProperties;
import pt.uminho.anote2.core.annotation.PolarityEnum;
import pt.uminho.anote2.datastructures.annotation.Annotation;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;


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
	
	public EventAnnotation(int id,long start, long end, String type,
			IEntityAnnotation left,IEntityAnnotation right,
			String clue,int ontologicalClassID,String ontologicalClass,
			IEventProperties eventProperties) {
		super(id,start, end);
		this.entitiesAtLeft=new ArrayList<IEntityAnnotation>();
		this.entitiesAtLeft.add(left);
		this.entitiesAtRight=new ArrayList<IEntityAnnotation>();
		this.entitiesAtRight.add(right);
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
		if(key.equals(GlobalNames.relationPropertyLemma))
		{
			eventProperties.setLemma(value);
		}
		else if(key.equals(GlobalNames.relationPropertyPolarity))
		{
			eventProperties.setPolarity(PolarityEnum.covertIntToPolarityEnum(Integer.valueOf(value)));
		}
		else if(key.equals(GlobalNames.relationPropertyDirectionally))
		{
			eventProperties.setDirectionally(DirectionallyEnum.covertIntToDirectionallyEnum(Integer.valueOf(value)));
		}
		else
		{
			eventProperties.addProperty(key, value);
		}	
	}
	
	public IEventAnnotation clone(){
		return new EventAnnotation(this.getID(), this.getStartOffset(), this.getEndOffset(), this.getType(), this.getEntitiesAtLeft(), this.getEntitiesAtRight(), this.getEventClue(), this.getOntologicalClassID(), this.getOntologycalClass(), this.getEventProperties());
	}
	
	public String toString()
	{
		String value = new String();
		value = "Relation "+getEventClue()+" ( "+getStartOffset()+"-"+getEndOffset()+" )+\n";
		value = value +"\tEntities At Left :";
		for(IEntityAnnotation entLeft : getEntitiesAtLeft())
			value = value + "\t\t"+entLeft.toString() + "\n";
		value = value +"\tEntities At Right :";
		for(IEntityAnnotation entRight : getEntitiesAtRight())
			value = value + "\t\t"+entRight.toString() + "\n";
		return value;
		
	}

	@Override
	public void setEntityAtLeft(List<IEntityAnnotation> entitiesAtLeft) {
		this.entitiesAtLeft = entitiesAtLeft;
	}

	@Override
	public void setEntityAtRight(List<IEntityAnnotation> entitiesAtRight) {
		this.entitiesAtRight = entitiesAtRight;	
	}

	@Override
	public void setEventProperties(IEventProperties eventProperties) {
		this.eventProperties = eventProperties;
	}
}
