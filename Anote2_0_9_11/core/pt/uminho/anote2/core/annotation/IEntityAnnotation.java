package pt.uminho.anote2.core.annotation;

public interface IEntityAnnotation extends IAnnotation,Comparable<IEntityAnnotation> {
	
	/**
	 * Method that returns annotation text value
	 * 
	 * @return annotation original representation
	 */
	public String getAnnotationValue();
	
	/**
	 * Method that returns associated resourceID if annotation was provide for a resource element
	 * 
	 * @return returns associated resourceID
	 * 		  -1 if do not have resourceID associated
	 */
	public int getResourceElementID();
	
	/**
	 * Method that return classID for annotation
	 * 
	 * @return 
	 */
	public int getClassAnnotationID();
	
	/**
	 * Method that set ResourceID for element
	 * 
	 * @param resourceElmID
	 */
	public void setResourceElementID(int resourceElmID);
	
	public IEntityAnnotation clone();
	
	/**
	 * Method that returns annotation text value normalized
	 * 
	 * @return annotation normalized representation
	 */
	public String getAnnotationValueNormalization();
	
	public void setClass(int classID);

}
