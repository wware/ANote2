package pt.uminho.anote2.relation.settings;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.process.re.RelationType;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.AbstractPropertyNode;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.IPropertiesPanel;
import pt.uminho.anote2.process.IE.re.IRelationsType;
import pt.uminho.anote2.relation.datastructures.PosTaggerEnem;
import pt.uminho.anote2.relation.datastructures.RelationsModelEnem;

public class RERelationSettingsNode extends AbstractPropertyNode{

	
	private static final String treepath = "RE.Relation";
	
	private Map<String, Object> defaultOptions;
	private boolean firstRun = true;

	public RERelationSettingsNode() {
		super(treepath);
	}


	@Override
	public Map<String, Object> getDefaultProperties() {
		if(defaultOptions == null){
			defaultOptions = new HashMap<String, Object>();
			defaultOptions.put(RERelationDefaultSettings.POSTAGGER, PosTaggerEnem.LingPipe_POS);
			defaultOptions.put(RERelationDefaultSettings.MODEL, RelationsModelEnem.Binary_Verb_limitation);
			defaultOptions.put(RERelationDefaultSettings.VERB_FILTER, false);
			defaultOptions.put(RERelationDefaultSettings.VERB_FILTER_LEXICAL_WORDS_ID, 0);
			defaultOptions.put(RERelationDefaultSettings.VERB_ADDITION, false);
			defaultOptions.put(RERelationDefaultSettings.VERB_ADDITION_LEXICAL_WORDS_ID, 0);
			defaultOptions.put(RERelationDefaultSettings.BIOMEDICAL_VERB_MODEL, 0);
			defaultOptions.put(RERelationDefaultSettings.ADVANCED_VERB_ENTITES_MAX_DISTANCE, 0);
			defaultOptions.put(RERelationDefaultSettings.ADVANCED_ONLY_NEAREST_VERB_ENTITIES, false);
			defaultOptions.put(RERelationDefaultSettings.ADVANCED_RELATIONS_TYPE, new TreeSet<IRelationsType>());
			defaultOptions.put(RERelationDefaultSettings.ADVANCED_ONLY_USE_ENTITY_TO_NEAREST_VERB,false);
		}
		return defaultOptions;
	}


	@Override
	public IPropertiesPanel getPropertiesPanel() {
		if(firstRun)
		{
			firstRun = false;
			return new RERelationChangeSettingsGUI();
		}
		try {
			return new RERelationChangeSettingsGUI(properties, defaultOptions);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
		return new RERelationChangeSettingsGUI();
	}


	@Override
	public Object revert(String propId, String propValue) {
		if( propId.matches(RERelationDefaultSettings.VERB_FILTER) ||
			propId.matches(RERelationDefaultSettings.VERB_ADDITION) ||
			propId.matches(RERelationDefaultSettings.ADVANCED_ONLY_NEAREST_VERB_ENTITIES) ||
			propId.matches(RERelationDefaultSettings.ADVANCED_ONLY_USE_ENTITY_TO_NEAREST_VERB))
			return Boolean.parseBoolean(propValue);
		else if(propId.matches(RERelationDefaultSettings.POSTAGGER))
		{
			return PosTaggerEnem.convertStringInPosTaggerEnem(propValue);
		}
		else if(propId.matches(RERelationDefaultSettings.MODEL))
		{
			return RelationsModelEnem.convertStringToRelationsModelEnem(propValue);
		}
		else if(propId.matches(RERelationDefaultSettings.VERB_FILTER_LEXICAL_WORDS_ID) || 
				propId.matches(RERelationDefaultSettings.VERB_ADDITION_LEXICAL_WORDS_ID) ||
				propId.matches(RERelationDefaultSettings.BIOMEDICAL_VERB_MODEL) ||
				propId.matches(RERelationDefaultSettings.ADVANCED_VERB_ENTITES_MAX_DISTANCE))
		{
			return Integer.valueOf(propValue);
		}
		else if(propId.matches(RERelationDefaultSettings.ADVANCED_RELATIONS_TYPE))
		{
			return getSetRelationsType(propValue);
		}
		return null;
	}

	public static SortedSet<IRelationsType> getSetRelationsType(String stringValue)
	{
		if(stringValue.isEmpty() || stringValue.equalsIgnoreCase("all"))
		{
			return new TreeSet<IRelationsType>();
		}
		else
		{
			SortedSet<IRelationsType> rt = new TreeSet<IRelationsType>();
			String[] values = stringValue.split(",");
			for(String rtStr:values)
			{
				String[] classes = rtStr.split("--");
				if(classes.length == 2)
				{
					String leftEntClass = classes[0];
					String rightEntClass = classes[1];
					Integer classLeftID = ClassProperties.getClassClassID().get(leftEntClass);
					Integer classRightID = ClassProperties.getClassClassID().get(rightEntClass);
					if(classLeftID!=null && classRightID!=null)
					{
						rt.add(new RelationType(classLeftID, classRightID));
					}
				}
			}
			if(rt.size() == 0)
				return new TreeSet<IRelationsType>();
			else
				return rt;
		}
	}
	
	@Override
	public String convert(String propId, Object propValue) {
		if(propId.matches(RERelationDefaultSettings.ADVANCED_RELATIONS_TYPE))
		{
			return  convertShortedRelationTypeIntoString(propValue);
		}
		return propValue.toString();
	}
	
	public static String convertShortedRelationTypeIntoString(Object relations)
	{
		if(relations instanceof SortedSet)
		{
			String trToString = new String();
			SortedSet<?> rt = (SortedSet<?>) relations;
			for(Object item:rt)
			{
				if(item instanceof IRelationsType)
				{
					IRelationsType rtT = (IRelationsType) item;
					trToString = trToString + rtT.toString() + ",";
				}
			}
			if(trToString.length() < 3)
			{
				return "all";
			}
			else
			{
				return trToString.substring(0, trToString.length()-1);
			}
		}
		else
		{
			return "all";
		}
		
	}
	
	@Override
	public Set<String> getNotExportableProperties() {
		Set<String> notAllowExportableProperties = new HashSet<String>(); 
		return notAllowExportableProperties;
	}


}
