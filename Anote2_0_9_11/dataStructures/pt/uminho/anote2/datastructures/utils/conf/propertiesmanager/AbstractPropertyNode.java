package pt.uminho.anote2.datastructures.utils.conf.propertiesmanager;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public abstract class AbstractPropertyNode implements IPropertyNode{
	
	protected Map<String, Object> properties;
	protected Set<String> propertiesNotAllowForImportAndExport;
	private String treePath;
	
	public AbstractPropertyNode(String treePath){
		
		super();
		this.treePath = treePath;
		properties = new HashMap<String, Object>(getDefaultProperties());
		propertiesNotAllowForImportAndExport = getNotExportableProperties();
		
	}
	
	public abstract Map<String, Object> getDefaultProperties();
	public abstract Set<String> getNotExportableProperties();

	public abstract IPropertiesPanel getPropertiesPanel();
	public abstract Object revert(String propId, String propValue);
	public abstract String convert(String propId, Object propValue);
	
//	public void populatePropertiesByGUI() throws PropertiesManagerException{
//		setProperties(getPropertiesPanel().getProperties());
//	}
	
	public String getTreePath(){
		return treePath;
	}
	
	public Object getProperty(String propName){
		return properties.get(propName);
	}
	

	@Override
	public void populateProperties(Properties p) {

		for(String key : getPropertiesIdentifiers()){
			p.setProperty(key, convert(key, properties.get(key)));
		}
			
	}
	
	@Override
	public void populatePropertiesRestricted(Properties p) {

		for(String key : getPropertiesIdentifiers()){
			if(!propertiesNotAllowForImportAndExport.contains(key))
				p.setProperty(key, convert(key, properties.get(key)));
		}
			
	}


	@Override
	public void loadProperties(Properties p) {

		for(String key : getPropertiesIdentifiers()){
			String temp = p.getProperty(key);
			if(temp!=null)
				properties.put(key, revert(key, temp));
			else
				properties.put(key, getDefaultProperties().get(key));
		}
		//        getPropertiesPanel().setProperties(properties);        
	}
	
	public Set<String> getPropertiesIdentifiers(){
		return getDefaultProperties().keySet();
	}

	public void setProperties(Map<String, Object> props) throws PropertiesManagerException{
		for(String pid : props.keySet()){
			Object old = properties.get(pid);
			if(old!=null)
			{
				Object now = props.get(pid);
				if(Enum.class.isAssignableFrom(old.getClass())){
					if(!(((Enum) old).getDeclaringClass().equals(((Enum) now).getDeclaringClass())))
						throw new PropertiesManagerException("Can't update properties: different class of property objects in Configuration Panels, and Property Manager.");
				}
				else{
					if(!(old.getClass().equals(now.getClass()))){
						throw new PropertiesManagerException("Can't update properties: different class of property objects in Configuration Panels, and Property Manager.");
					}
				}
			}
		}
		properties = new HashMap<String, Object>(props);
	}
	
}
