package pt.uminho.anote2.workflow.datastructures;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import pt.uminho.anote2.aibench.ner.gui.help.ResourcesFinderGUIHelp;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.resources.lexiacalwords.LexicalWords;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.process.IE.re.IREConfiguration;
import pt.uminho.anote2.process.IE.re.IRelationsType;
import pt.uminho.anote2.relation.configuration.IRERelationAdvancedConfiguration;
import pt.uminho.anote2.relation.configuration.RERelationAdvancedConfiguration;
import pt.uminho.anote2.relation.configuration.RERelationConfiguration;
import pt.uminho.anote2.relation.datastructures.PosTaggerEnem;
import pt.uminho.anote2.relation.datastructures.RelationsModelEnem;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;
import pt.uminho.anote2.workflow.defaultconfiguration.workflow.basic.WorkflowBabicDefaultParameters;
import pt.uminho.anote2.workflow.gui.AREPanel;
import pt.uminho.anote2.workflow.gui.help.RERelationPanel;
import pt.uminho.anote2.workflow.settings.basic.WorkflowBasicsDefaulSettings;

public enum REWorkflowProcessesAvailableEnum {
	Relation
	{		
		public String getDescription(){
			return "Relation Extration based in POS-tagging";
		}

		public String toString(){
			return "RE - Rel@tioN";
		}

		public AREPanel getMainComponent(List<String> defaultSettings) throws SQLException, DatabaseLoadDriverException
		{
			return new RERelationPanel(defaultSettings);
		}
		
		public IREConfiguration getREConfigurationBasicWorkFlow() throws SQLException, DatabaseLoadDriverException
		{
			ResourcesFinderGUIHelp resources = new ResourcesFinderGUIHelp();
			List<IResource<IResourceElement>> lws = resources.getLexicalWords();
			Set<Integer> set = new HashSet<Integer>();
			for(IResource<IResourceElement> lw:lws)
				set.add(lw.getID());
			PosTaggerEnem posTagger = (PosTaggerEnem) PropertiesManager.getPManager().getProperty(WorkflowBasicsDefaulSettings.POSTAGGER);
			RelationsModelEnem relationModel = (RelationsModelEnem) PropertiesManager.getPManager().getProperty(WorkflowBasicsDefaulSettings.MODEL);
			ILexicalWords verbAdditionRBiomedicalVErbs = null;
			ILexicalWords verbFilter = null;
			if(relationModel.equals(RelationsModelEnem.Binary_Biomedical_Verbs))
			{
				int biomedicalVerbs = Integer.valueOf(PropertiesManager.getPManager().getProperty(WorkflowBasicsDefaulSettings.BIOMEDICAL_VERB_MODEL).toString());
				if(set.contains(biomedicalVerbs))
				{
					verbAdditionRBiomedicalVErbs = new LexicalWords(biomedicalVerbs, "", "");
				}
				else
				{
					relationModel = RelationsModelEnem.Binary_Verb_limitation;
				}
			}
			else
			{
				if(Boolean.parseBoolean(PropertiesManager.getPManager().getProperty(WorkflowBasicsDefaulSettings.VERB_ADDITION).toString()))
				{
					int verbAddition = Integer.valueOf(PropertiesManager.getPManager().getProperty(WorkflowBasicsDefaulSettings.VERB_ADDITION_LEXICAL_WORDS_ID).toString());
					if(set.contains(verbAddition))
					{
						verbAdditionRBiomedicalVErbs = new LexicalWords(verbAddition, "", "");
					}
				}
				if(Boolean.parseBoolean(PropertiesManager.getPManager().getProperty(WorkflowBasicsDefaulSettings.VERB_FILTER).toString()))
				{
					int verbFilterID = Integer.valueOf(PropertiesManager.getPManager().getProperty(WorkflowBasicsDefaulSettings.VERB_FILTER_LEXICAL_WORDS_ID).toString());
					if(set.contains(verbFilterID))
					{
						verbAdditionRBiomedicalVErbs = new LexicalWords(verbFilterID, "", "");
					}
				}

			}
			Boolean usenearestEntities = Boolean.valueOf(PropertiesManager.getPManager().getProperty(WorkflowBasicsDefaulSettings.ADVANCED_ONLY_NEAREST_VERB_ENTITIES).toString());
			Boolean useEntitiesNearestVerb = Boolean.valueOf(PropertiesManager.getPManager().getProperty(WorkflowBasicsDefaulSettings.ADVANCED_ONLY_USE_ENTITY_TO_NEAREST_VERB).toString());
			SortedSet<IRelationsType> rt = (SortedSet<IRelationsType>) PropertiesManager.getPManager().getProperty(WorkflowBasicsDefaulSettings.ADVANCED_RELATIONS_TYPE);
			Integer value = Integer.valueOf(PropertiesManager.getPManager().getProperty(WorkflowBasicsDefaulSettings.ADVANCED_VERB_ENTITES_MAX_DISTANCE).toString());
			IRERelationAdvancedConfiguration advancedOptions = new RERelationAdvancedConfiguration(usenearestEntities,useEntitiesNearestVerb,value,rt);

			return new RERelationConfiguration(WorkflowBabicDefaultParameters.corpus
					, WorkflowBabicDefaultParameters.iieprocess, posTagger, relationModel,
					verbFilter, verbAdditionRBiomedicalVErbs,advancedOptions);
		}

	};
	

	public String getDescription() {
		return this.getDescription();
	}

	public AREPanel getMainComponent(List<String> defaultSettings) throws SQLException, DatabaseLoadDriverException
	{
		return this.getMainComponent(defaultSettings);
	}

	public String toString()
	{
		return this.toString();
	}
	
	public IREConfiguration getREConfigurationBasicWorkFlow() throws SQLException, DatabaseLoadDriverException
	{
		return this.getREConfigurationBasicWorkFlow();
	}
	
	public static REWorkflowProcessesAvailableEnum convertStringToRelationProcess(String propValue) {
		if(propValue.equals(REWorkflowProcessesAvailableEnum.Relation.toString()))
		{
			return REWorkflowProcessesAvailableEnum.Relation;
		}
		return null;
	}
}
