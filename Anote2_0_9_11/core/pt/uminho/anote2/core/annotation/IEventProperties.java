package pt.uminho.anote2.core.annotation;

import java.util.Properties;

/**
 * Interface that represent a generic Event Properties with some specific properties for @Note2 Relation
 * 
 * @author Hugo Costa
 *
 */
public interface IEventProperties {
	
	/**
	 * Method to add a generic Property
	 * 
	 * @param name
	 * @param value
	 */
	public void setGeneralProperty(String name,Object value);
	
	/**
	 * Method to give a Property given propertyName
	 * 
	 * @param name
	 * @return
	 */
	public Object getGeneralProperties(String propertyName);
	
	/**
	 * Method that returns all {@link IEventAnnotation} Properties
	 * 
	 * @return
	 */
	public Properties getProperties();
	
	/**
	 * Method that set all {@link IEventProperties}
	 * 
	 * @param properties
	 */
	public void setProperties(Properties properties);
	
	/**
	 * Method that return specific {@link IEventAnnotation} property Lemma  
	 * 
	 * @return
	 */
	public String getLemma();
	
	/**
	 * method that set specific {@link IEventAnnotation} property lemma
	 * 
	 * @param lemma
	 */
	public void setLemma(String lemma);
	
	/**
	 * Method that return specific {@link IEventAnnotation} property Polarity  
	 *  
	 * @return 
	 * 0 - Negative
	 * 1 - Conditional
	 * 2 - Positive
	 * 3 - Not defined
	 */
	public PolarityEnum getPolarity();
	
	/**
	 * method that set specific {@link IEventAnnotation} property polarity
	 * 
	 * @param polarity
	 */
	public void setPolarity(PolarityEnum polarity);
	
	/**
	 * Method that return specific {@link IEventAnnotation} property Directionally
	 * 
	 * 0 - LeftToRight
	 * 1 - RightToLeft
	 * 2 - Unknown
	 * 3 - Both
	 * 
	 * @return
	 */
	public DirectionallyEnum getDirectionally();
	
	/**
	 * Method that set specific {@link IEventAnnotation} property Directionally
	 * 
	 * @param directionally
	 */
	public void setDirectionally(DirectionallyEnum directionally);
	
	/**
	 * Method that add a generic Property
	 * 
	 * @param key
	 * @param value
	 */
	public void addProperty(String key,String value);
	
}
