package pt.uminho.anote2.datastructures.utils.conf.propertiesmanager;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

public interface IPropertyNode{
	
	String getTreePath();
	
	Set<String> getPropertiesIdentifiers();
	Map<String, Object> getDefaultProperties();
	void setProperties(Map<String, Object> props) throws PropertiesManagerException;
//	Map<String, Object> getProperties();
	
	Object getProperty(String propName);
	IPropertiesPanel getPropertiesPanel();
	
	void populateProperties(Properties p);
	void populatePropertiesRestricted(Properties p);

//	void populatePropertiesByGUI() throws PropertiesManagerException;
	void loadProperties(Properties p);
	
	Object revert(String propId, String propValue);
	String convert(String propId, Object propValue);
	
}
