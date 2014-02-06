package pt.uminho.anote2.datastructures.settings.proxy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.uminho.anote2.datastructures.configuration.Proxy;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.AbstractPropertyNode;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.IPropertiesPanel;

public class ProxySettingsNode extends AbstractPropertyNode{

	
	private static final String treepath = "General.Proxy";
	
	private Map<String, Object> defaultOptions;
	private boolean firstRun = true;

	public ProxySettingsNode() {
		super(treepath);
	}


	@Override
	public Map<String, Object> getDefaultProperties() {
		if(defaultOptions == null){
			defaultOptions = new HashMap<String, Object>();
			defaultOptions.put(ProxyDefaultSettings.PROXY, new Proxy());
		}
		return defaultOptions;
	}


	@Override
	public IPropertiesPanel getPropertiesPanel() {
		if(firstRun)
		{
			firstRun = false;
			return new ProxyChangeSettingsGUI();		
		}
		return new ProxyChangeSettingsGUI(properties,defaultOptions);
	}


	@Override
	public Object revert(String propId, String propValue) {
		if(propId.matches(ProxyDefaultSettings.PROXY))
		{
			return Proxy.getProxyByString(propValue);
		}
		return null;
	}


	@Override
	public String convert(String propId, Object propValue) {
		return propValue.toString();
	}


	@Override
	public Set<String> getNotExportableProperties() {
		Set<String> notAllowExportableProperties = new HashSet<String>(); 
		return notAllowExportableProperties;
	}

	

}
