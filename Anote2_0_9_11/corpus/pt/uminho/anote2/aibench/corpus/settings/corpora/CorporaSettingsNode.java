package pt.uminho.anote2.aibench.corpus.settings.corpora;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.AbstractPropertyNode;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.IPropertiesPanel;

public class CorporaSettingsNode extends AbstractPropertyNode{

	
	private static final String treepath = "Corpora";
	
	private Map<String, Object> defaultOptions;
	private boolean firstRun = true;

	public CorporaSettingsNode() {
		super(treepath);
	}


	@Override
	public Map<String, Object> getDefaultProperties() {
		if(defaultOptions == null){
			defaultOptions = new HashMap<String, Object>();
			defaultOptions.put(CorporaDefaultSettings.CORPUS_SIZE_LIMIT, -1);
		}
		return defaultOptions;
	}


	@Override
	public IPropertiesPanel getPropertiesPanel() {
		if(firstRun)
		{
			firstRun = false;
			return new CorporaChangeSettingsGUI();		
		}
		return new CorporaChangeSettingsGUI(properties,defaultOptions);
	}


	@Override
	public Object revert(String propId, String propValue) {
		if( propId.matches(CorporaDefaultSettings.CORPUS_SIZE_LIMIT))
		{
			return Integer.valueOf(propValue);
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
