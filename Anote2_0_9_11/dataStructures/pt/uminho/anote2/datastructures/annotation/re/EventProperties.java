package pt.uminho.anote2.datastructures.annotation.re;

import java.util.Properties;

import pt.uminho.anote2.core.annotation.DirectionallyEnum;
import pt.uminho.anote2.core.annotation.IEventProperties;
import pt.uminho.anote2.core.annotation.PolarityEnum;

/**
 * 
 * @author Hugo Costa
 *
 */
public class EventProperties implements IEventProperties{
	
	private Properties properties;
	private String lemma;
	private PolarityEnum polarity;
	private DirectionallyEnum directionally;
	
	
	public EventProperties()
	{
		this.properties = new Properties();
		this.lemma = new String();
		this.polarity = PolarityEnum.Unknown;
		this.directionally = DirectionallyEnum.Unknown;
	}
	
	public EventProperties(Properties properties)
	{
		this.properties=properties;
	}
	
	public void setGeneralProperty(String name,Object value)
	{
		properties.put(name, value);
	}
	
	public Object getGeneralProperties(String name)
	{
		return properties.get(name);
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public PolarityEnum getPolarity() {
		return polarity;
	}

	public void setPolarity(PolarityEnum polarity) {
		this.polarity = polarity;
	}

	public DirectionallyEnum getDirectionally() {
		return directionally;
	}

	public void setDirectionally(DirectionallyEnum directionally) {
		this.directionally = directionally;
	}

	public void addProperty(String key, String value) {
		this.properties.put(key, value);
	}


	
}
