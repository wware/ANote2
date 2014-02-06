package pt.uminho.anote2.aibench.corpus.settings.cytoscape;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.AbstractPropertyNode;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.IPropertiesPanel;

public class CytoscapeSettingsNode extends AbstractPropertyNode{
	
	private static final String treepath = "Corpora.Cytoscape";
	
	private Map<String, Object> defaultOptions;
	private boolean firstRun = true;
	
	
	public CytoscapeSettingsNode() {
		super(treepath);
	}
	
	
	@Override
	public Map<String, Object> getDefaultProperties() {
		if(defaultOptions == null){
			defaultOptions = new HashMap<String, Object>();
			defaultOptions.put(CytoscapeDefaultSettings.OPEN_CYTOSCAPE_AFTER_CREATE_XGMML_FILE, false);
			defaultOptions.put(CytoscapeDefaultSettings.CYTOSCAPE_DIRECTORY, new String());
		}
		return defaultOptions;
	}


	@Override
	public IPropertiesPanel getPropertiesPanel() {
		if(firstRun)
		{
			firstRun = false;
			return new CytoscapeChangeSettingsGUI();		
		}
		return new CytoscapeChangeSettingsGUI(properties,defaultOptions);
	}
	
	@Override
	public Object revert(String propId, String propValue) {
		if( propId.matches(CytoscapeDefaultSettings.OPEN_CYTOSCAPE_AFTER_CREATE_XGMML_FILE))
		{
			return Boolean.valueOf(propValue);
		}
		else if(propId.matches(CytoscapeDefaultSettings.CYTOSCAPE_DIRECTORY))
		{
			return propValue;
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
		notAllowExportableProperties.add(CytoscapeDefaultSettings.OPEN_CYTOSCAPE_AFTER_CREATE_XGMML_FILE);
		notAllowExportableProperties.add(CytoscapeDefaultSettings.CYTOSCAPE_DIRECTORY);
		return notAllowExportableProperties;
	}

}
