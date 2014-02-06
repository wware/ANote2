package pt.uminho.anote2.core.annotation;

import java.util.List;

public interface IEventAnnotation extends IAnnotation{
	
	public List<IEntityAnnotation> getEntitiesAtLeft();
	public List<IEntityAnnotation> getEntitiesAtRight();
	public String getEventClue(); // Verb
	public int getOntologicalClassID();
	public String getOntologycalClass();
	public IEventProperties getEventProperties();
	public void addEntityAtLeft(IEntityAnnotation ent);
	public void addEntityAtRight(IEntityAnnotation ent);
	public void addEventProperty(String key,String value);
	public IEventAnnotation clone();
}
