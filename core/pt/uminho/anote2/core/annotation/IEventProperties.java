package pt.uminho.anote2.core.annotation;

import java.util.Properties;

public interface IEventProperties {
	
	public void setGeneralProperty(String name,Object value);
	public Object getGeneralProperties(String name);
	public Properties getProperties();
	public void setProperties(Properties properties);
	public String getLemma();
	public void setLemma(String lemma);
	public Integer getPolarity();
	public void setPolarity(int polarity);
	public Boolean isDirectionally();
	public void setDirectionally(boolean directionally);
	public void addProperty(String key,String value);
	
}
