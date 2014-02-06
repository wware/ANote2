package pt.uminho.anote2.aibench.ner.settings;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.uminho.anote2.aibench.ner.configuration.NERLexicalResourcesPreProssecingEnum;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.AbstractPropertyNode;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.IPropertiesPanel;

public class NERLexicalResourcesSettingsNode extends AbstractPropertyNode{

	
	private static final String treepath = "NER.LexicalResources";
	
	private Map<String, Object> defaultOptions;
	private boolean firstRun = true;

	public NERLexicalResourcesSettingsNode() {
		super(treepath);
	}


	@Override
	public Map<String, Object> getDefaultProperties() {
		if(defaultOptions == null){
			defaultOptions = new HashMap<String, Object>();
			defaultOptions.put(NERLexicalResourcesDefaultSettings.USE_PARTIAL_MATCH_WITH_DICTIONARIES, false);
			defaultOptions.put(NERLexicalResourcesDefaultSettings.CASE_SENSITIVE, false);
			defaultOptions.put(NERLexicalResourcesDefaultSettings.PRE_PROCESSING, NERLexicalResourcesPreProssecingEnum.POSTagging);
			defaultOptions.put(NERLexicalResourcesDefaultSettings.NORMALIZATION, true);
			defaultOptions.put(NERLexicalResourcesDefaultSettings.LEXICAL_RESOURCE_STOPWORDS_ID, 0);
			defaultOptions.put(NERLexicalResourcesDefaultSettings.RULES_RESOURCE_ID, 0);
		}
		return defaultOptions;
	}


	@Override
	public IPropertiesPanel getPropertiesPanel() {
		if(firstRun)
		{
			firstRun = false;
			return new NERLexicalResourcesChangeSettingsGUI();		
		}
		try {
			return new NERLexicalResourcesChangeSettingsGUI(properties,defaultOptions);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
		return new NERLexicalResourcesChangeSettingsGUI();		

	}


	@Override
	public Object revert(String propId, String propValue) {
		if( propId.matches(NERLexicalResourcesDefaultSettings.USE_PARTIAL_MATCH_WITH_DICTIONARIES) ||
				propId.matches(NERLexicalResourcesDefaultSettings.CASE_SENSITIVE) 
				|| propId.matches(NERLexicalResourcesDefaultSettings.NORMALIZATION))
			return Boolean.parseBoolean(propValue);
		else if(propId.matches(NERLexicalResourcesDefaultSettings.PRE_PROCESSING))
		{
			return NERLexicalResourcesPreProssecingEnum.convertStringToNERLexicalResourcesPreProssecingEnum(propValue);
		}
		else if(propId.matches(NERLexicalResourcesDefaultSettings.LEXICAL_RESOURCE_STOPWORDS_ID) ||
				propId.matches(NERLexicalResourcesDefaultSettings.RULES_RESOURCE_ID))
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
