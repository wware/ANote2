package pt.uminho.anote2.datastructures.annotation;

import java.util.Properties;

import pt.uminho.anote2.core.annotation.IEventProperties;

public class EventProperties implements IEventProperties{
	
	private Properties properties;
	private String lemma;
	private Integer polarity;
	private Boolean directionally;
	
	
	public EventProperties()
	{
		this.properties = new Properties();
		this.lemma = new String();
		this.polarity = null;
		this.directionally = null;
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

	public Integer getPolarity() {
		return polarity;
	}

	public void setPolarity(int polarity) {
		this.polarity = polarity;
	}

	public Boolean isDirectionally() {
		return directionally;
	}

	public void setDirectionally(boolean directionally) {
		this.directionally = directionally;
	}

	public void addProperty(String key, String value) {
		this.properties.put(key, value);
	}
	
}
