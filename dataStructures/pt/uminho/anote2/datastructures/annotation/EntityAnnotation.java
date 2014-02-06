package pt.uminho.anote2.datastructures.annotation;

import pt.uminho.anote2.core.annotation.IEntityAnnotation;

public class EntityAnnotation extends Annotation implements IEntityAnnotation{

	private int classAnnotationID;
	private String annotationValue;
	private int resourceElementID;
	private String annotationValueNormalization;
	
	
	public EntityAnnotation(int id, long start, long end,int classAnnotationID,int resourceElementID,String value,String annotationValueNormalization) {
		super(id, start, end);
		this.classAnnotationID=classAnnotationID;
		this.annotationValue=value;
		this.resourceElementID=resourceElementID;
		this.setAnnotationValueNormalization(annotationValueNormalization);
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


}
