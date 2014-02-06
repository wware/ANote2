package pt.uminho.anote2.datastructures.annotation.ner;

import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.datastructures.annotation.Annotation;

/**
 * Class that represent an bio-entity annotation
 * 
 * @author Hugo Costa
 *
 */
public class EntityAnnotation extends Annotation implements IEntityAnnotation{

	private int classAnnotationID;
	private String annotationValue;
	private int resourceElementID;
	private String annotationValueNormalization;
	
	/**
	 * 
	 * @param id
	 * @param start
	 * @param end
	 * @param classAnnotationID
	 * @param resourceElementID
	 * @param value
	 * @param annotationValueNormalized
	 */
	public EntityAnnotation(int id, long start, long end,int classAnnotationID,int resourceElementID,String value,String annotationValueNormalized) {
		super(id, start, end);
		this.classAnnotationID=classAnnotationID;
		this.annotationValue=value;
		this.resourceElementID=resourceElementID;
		this.setAnnotationValueNormalization(annotationValueNormalized);
	}

	public String getAnnotationValue() {
		return annotationValue;
	}

	public int getClassAnnotationID() {
		return classAnnotationID;
	}

	public void setResourceElementID(int resourceElementID) {
		this.resourceElementID = resourceElementID;
	}

	public int getResourceElementID() {
		return resourceElementID;
	}
	
	public IEntityAnnotation clone()
	{
		IEntityAnnotation ent = new EntityAnnotation(super.getID(),super.getStartOffset(),super.getEndOffset(),getClassAnnotationID(),getResourceElementID(), getAnnotationValue(),getAnnotationValueNormalization());
		return ent;		
	}

	public String getAnnotationValueNormalization() {
		return annotationValueNormalization;
	}

	public void setAnnotationValueNormalization(
			String annotationValueNormalization) {
		this.annotationValueNormalization = annotationValueNormalization;
	}

	public String toString()
	{
		return getAnnotationValue()+ "( "+getStartOffset()+"-"+getEndOffset()+" )";
	}
	
	public int compareTo(IEntityAnnotation o) {
		if(this.getResourceElementID()!=0 && this.getResourceElementID()==o.getResourceElementID())
		{
			return 0;
		}
		else if(this.getAnnotationValue().equals(o.getAnnotationValue()))
		{
			return this.getClassAnnotationID() - o.getClassAnnotationID();
		}
		else
		{
			return this.getAnnotationValue().compareTo(o.getAnnotationValue());
		}
	}
	
	public boolean equals(IEntityAnnotation o)
	{
		return compareTo(o) == 0;
	}
	
	public boolean equals(Object o)
	{
		if(o instanceof IEntityAnnotation)
		{
			return compareTo((IEntityAnnotation)o) == 0;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void setClass(int classAnnotationID) {
		this.classAnnotationID=classAnnotationID;
	}

}
