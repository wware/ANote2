package pt.uminho.anote2.aibench.curator.settings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.AbstractPropertyNode;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.IPropertiesPanel;

public class CuratorSettingsNode extends AbstractPropertyNode{

	
	private static final String treepath = "Corpora.Curator";
	
	private Map<String, Object> defaultOptions;
	private boolean firstRun = true;

	public CuratorSettingsNode() {
		super(treepath);
	}


	@Override
	public Map<String, Object> getDefaultProperties() {
		if(defaultOptions == null){
			defaultOptions = new HashMap<String, Object>();
			defaultOptions.put(CuratorDefaultSettings.ONLY_TERM_EXTERNALIDS_AVAILABLE, false);
			defaultOptions.put(CuratorDefaultSettings.USING_TERM_NAME_LINKOUT_SEARCH, false);
		}
		return defaultOptions;
	}


	@Override
	public IPropertiesPanel getPropertiesPanel() {
		if(firstRun)
		{
			firstRun = false;
			return new CuratorChangeSettingsGUI();		
		}
		return new CuratorChangeSettingsGUI(properties,defaultOptions);
	}


	@Override
	public Object revert(String propId, String propValue) {
		if( propId.matches(CuratorDefaultSettings.ONLY_TERM_EXTERNALIDS_AVAILABLE) ||
				propId.matches(CuratorDefaultSettings.USING_TERM_NAME_LINKOUT_SEARCH))
		{
			return Boolean.valueOf(propValue);
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
