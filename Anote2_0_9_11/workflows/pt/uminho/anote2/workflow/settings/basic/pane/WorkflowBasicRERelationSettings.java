package pt.uminho.anote2.workflow.settings.basic.pane;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.resources.lexiacalwords.LexicalWords;
import pt.uminho.anote2.process.IE.re.IRelationsType;
import pt.uminho.anote2.relation.datastructures.PosTaggerEnem;
import pt.uminho.anote2.relation.datastructures.RelationsModelEnem;
import pt.uminho.anote2.relation.settings.pane.RERelationChangeSettingsPanel;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;
import pt.uminho.anote2.workflow.datastructures.IChangeSettings;
import pt.uminho.anote2.workflow.settings.basic.WorkflowBasicsDefaulSettings;

public class WorkflowBasicRERelationSettings extends RERelationChangeSettingsPanel implements IChangeSettings{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public WorkflowBasicRERelationSettings(Map<String, Object> initial_props,
			Map<String, Object> defaultProps) throws SQLException, DatabaseLoadDriverException {
		super(initial_props, defaultProps);
	}
	
	public void defaultSettings() {
		fillSettings();
	}
	
	@Override
	protected void fillSettings() {
		
		jComboBoxPOSTagger.setSelectedItem(PosTaggerEnem.convertStringInPosTaggerEnem(initial_props.get(WorkflowBasicsDefaulSettings.POSTAGGER).toString()));
		jComboBoxRelationModel.setSelectedItem(RelationsModelEnem.convertStringToRelationsModelEnem(initial_props.get(WorkflowBasicsDefaulSettings.MODEL).toString()));
		jComboBoxBiomedicalVerbs.setSelectedItem(new LexicalWords(Integer.valueOf(initial_props.get(WorkflowBasicsDefaulSettings.BIOMEDICAL_VERB_MODEL).toString()), "", ""));
		jCheckBoxVerbFilter.setSelected(Boolean.valueOf(initial_props.get(WorkflowBasicsDefaulSettings.VERB_FILTER).toString()));
		jComboBoxVerbFilter.setSelectedItem(new LexicalWords(Integer.valueOf(initial_props.get(WorkflowBasicsDefaulSettings.VERB_FILTER_LEXICAL_WORDS_ID).toString()), "", ""));
		jCheckBoxVerbAddition.setSelected(Boolean.parseBoolean(initial_props.get(WorkflowBasicsDefaulSettings.VERB_ADDITION).toString()));
		jComboBoxVerbAddition.setSelectedItem(new LexicalWords(Integer.valueOf(initial_props.get(WorkflowBasicsDefaulSettings.VERB_ADDITION_LEXICAL_WORDS_ID).toString()), "", ""));
		jCheckBoxOnlyUseNearestEntitiesToVerb.setSelected(Boolean.parseBoolean(initial_props.get(WorkflowBasicsDefaulSettings.ADVANCED_ONLY_NEAREST_VERB_ENTITIES).toString()));
		jCheckBoxOnlyUseEntityInNearestVerb.setSelected(Boolean.parseBoolean(initial_props.get(WorkflowBasicsDefaulSettings.ADVANCED_ONLY_USE_ENTITY_TO_NEAREST_VERB).toString()));
		Integer value = Integer.valueOf((initial_props.get(WorkflowBasicsDefaulSettings.ADVANCED_VERB_ENTITES_MAX_DISTANCE).toString()));
		if(value!=0)
		{
			jCheckBoxUsingMaxDistanceVerbEntities.setSelected(true);
			jSpinnerMaxDistance.setValue(value);
		}
		SortedSet<IRelationsType> rt = ( SortedSet<IRelationsType>) initial_props.get(WorkflowBasicsDefaulSettings.ADVANCED_RELATIONS_TYPE);
		fillRelationTypeSettings(rt);
	}
	
	@Override
	public Map<String, Object> getProperties() {
		HashMap<String, Object> settings = new HashMap<String, Object>();
		settings.put(WorkflowBasicsDefaulSettings.POSTAGGER, jComboBoxPOSTagger.getSelectedItem());
		settings.put(WorkflowBasicsDefaulSettings.MODEL, jComboBoxRelationModel.getSelectedItem());
		settings.put(WorkflowBasicsDefaulSettings.BIOMEDICAL_VERB_MODEL, ((ILexicalWords)jComboBoxBiomedicalVerbs.getSelectedItem()).getID());
		settings.put(WorkflowBasicsDefaulSettings.VERB_FILTER, jCheckBoxVerbFilter.isSelected());
		settings.put(WorkflowBasicsDefaulSettings.VERB_FILTER_LEXICAL_WORDS_ID, ((ILexicalWords)jComboBoxVerbFilter.getSelectedItem()).getID());
		settings.put(WorkflowBasicsDefaulSettings.VERB_ADDITION, jCheckBoxVerbAddition.isSelected());
		settings.put(WorkflowBasicsDefaulSettings.VERB_ADDITION_LEXICAL_WORDS_ID, ((ILexicalWords)jComboBoxVerbAddition.getSelectedItem()).getID());
		settings.put(WorkflowBasicsDefaulSettings.ADVANCED_ONLY_NEAREST_VERB_ENTITIES, jCheckBoxOnlyUseNearestEntitiesToVerb.isSelected());
		settings.put(WorkflowBasicsDefaulSettings.ADVANCED_ONLY_USE_ENTITY_TO_NEAREST_VERB, jCheckBoxOnlyUseEntityInNearestVerb.isSelected());
		if(jCheckBoxUsingMaxDistanceVerbEntities.isSelected())
		{
			settings.put(WorkflowBasicsDefaulSettings.ADVANCED_VERB_ENTITES_MAX_DISTANCE, jSpinnerMaxDistance.getValue());
		}
		else
		{
			settings.put(WorkflowBasicsDefaulSettings.ADVANCED_VERB_ENTITES_MAX_DISTANCE, 0);
		}
		settings.put(WorkflowBasicsDefaulSettings.ADVANCED_RELATIONS_TYPE, getRelationsTypes());
		return settings;
	}


	public boolean haveChanged() {
		if(!initial_props.get(WorkflowBasicsDefaulSettings.POSTAGGER).equals(jComboBoxPOSTagger.getSelectedItem()))
		{
			return true;
		}
		else if(!initial_props.get(WorkflowBasicsDefaulSettings.MODEL).equals(jComboBoxRelationModel.getSelectedItem()))
		{
			return true;
		}
		else if(!initial_props.get(WorkflowBasicsDefaulSettings.BIOMEDICAL_VERB_MODEL).equals(((ILexicalWords)jComboBoxBiomedicalVerbs.getSelectedItem()).getID()))
		{
			return true;
		}
		else if(!initial_props.get(WorkflowBasicsDefaulSettings.VERB_FILTER).equals(jCheckBoxVerbFilter.isSelected()))
		{
			return true;
		}
		else if(!initial_props.get(WorkflowBasicsDefaulSettings.VERB_FILTER_LEXICAL_WORDS_ID).equals( ((ILexicalWords)jComboBoxVerbFilter.getSelectedItem()).getID()))
		{
			return true;
		}
		else if(!initial_props.get(WorkflowBasicsDefaulSettings.VERB_ADDITION).equals(jCheckBoxVerbAddition.isSelected()))
		{
			return true;
		}
		else if(!initial_props.get(WorkflowBasicsDefaulSettings.VERB_ADDITION_LEXICAL_WORDS_ID).equals( ((ILexicalWords)jComboBoxVerbAddition.getSelectedItem()).getID()))
		{
			return true;
		}
		int value = 0;
		if(jCheckBoxUsingMaxDistanceVerbEntities.isSelected())
		{
			value = (Integer) jSpinnerMaxDistance.getValue();
		}
		if(!initial_props.get(WorkflowBasicsDefaulSettings.ADVANCED_VERB_ENTITES_MAX_DISTANCE).equals(value))
		{
			return true;
		}
		else if(!initial_props.get(WorkflowBasicsDefaulSettings.ADVANCED_ONLY_NEAREST_VERB_ENTITIES).equals(jCheckBoxOnlyUseNearestEntitiesToVerb.isSelected()))
		{
			return true;
		}
		else if(!initial_props.get(WorkflowBasicsDefaulSettings.ADVANCED_ONLY_USE_ENTITY_TO_NEAREST_VERB).equals(jCheckBoxOnlyUseEntityInNearestVerb.isSelected()))
		{
			return true;
		}
		return false;
	}

}
