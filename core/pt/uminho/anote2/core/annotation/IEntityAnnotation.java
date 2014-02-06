package pt.uminho.anote2.core.annotation;

public interface IEntityAnnotation extends IAnnotation {
	
	public String getAnnotationValue();
	public String getAnnotationValueNormalization();
	public int getResourceElementID();
	public void setResourceElementID(int resourceElmID);
	public int getClassAnnotationID();
	public IEntityAnnotation clone();

}
