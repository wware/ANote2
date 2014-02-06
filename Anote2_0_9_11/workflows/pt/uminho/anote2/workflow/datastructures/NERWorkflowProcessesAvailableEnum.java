package pt.uminho.anote2.workflow.datastructures;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.uminho.anote2.aibench.ner.configuration.NERLexicalResourcesConfiguration;
import pt.uminho.anote2.aibench.ner.configuration.NERLexicalResourcesPreProssecingEnum;
import pt.uminho.anote2.aibench.ner.datastructures.ResourcesToNerAnote;
import pt.uminho.anote2.aibench.ner.gui.help.ResourcesFinderGUIHelp;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.resources.lexiacalwords.LexicalWords;
import pt.uminho.anote2.datastructures.resources.rule.RulesSet;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.process.IE.ner.INERConfiguration;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;
import pt.uminho.anote2.resource.rules.IRule;
import pt.uminho.anote2.workflow.defaultconfiguration.workflow.basic.WorkflowBabicDefaultParameters;
import pt.uminho.anote2.workflow.gui.ANERPanel;
import pt.uminho.anote2.workflow.gui.help.NERLexicalResourcesPanel;
import pt.uminho.anote2.workflow.settings.basic.WorkflowBasicsDefaulSettings;


public enum NERWorkflowProcessesAvailableEnum {
	NERLexicalResources
	{		
		public String getDescription(){
			return "Named Entity Recogniton based in Lexical Resources";
		}
		
		public String toString(){
			return "NER - Lexical Resources";
		}
		
		public ANERPanel getMainComponent(List<String> defaultSettings) throws SQLException, DatabaseLoadDriverException
		{
			return new NERLexicalResourcesPanel(defaultSettings);
		}
		
		public ANERPanel getMainComponent(INERConfiguration conf) throws SQLException, DatabaseLoadDriverException
		{
			return new NERLexicalResourcesPanel(conf);
		}
		
		public INERConfiguration getNERConfigurationBasicWorkFlow(ResourcesToNerAnote resourceToNER) throws SQLException, DatabaseLoadDriverException
		{
			ResourcesFinderGUIHelp resources = new ResourcesFinderGUIHelp();
			List<IResource<IResourceElement>> lws = resources.getLexicalWords();
			Set<Integer> set = new HashSet<Integer>();
			for(IResource<IResourceElement> lw:lws)
				set.add(lw.getID());
			NERLexicalResourcesPreProssecingEnum preprocessing = (NERLexicalResourcesPreProssecingEnum) PropertiesManager.getPManager().getProperty(WorkflowBasicsDefaulSettings.PRE_PROCESSING);
			ILexicalWords stoWords = null;
			int lexicalWordsID = Integer.valueOf(PropertiesManager.getPManager().getProperty(WorkflowBasicsDefaulSettings.LEXICAL_RESOURCE_STOPWORDS_ID).toString());
			if((preprocessing.equals(NERLexicalResourcesPreProssecingEnum.StopWords) || preprocessing.equals(NERLexicalResourcesPreProssecingEnum.Hybrid)))
			{
				if(lexicalWordsID>0 && set.contains(lexicalWordsID))
				{
					stoWords = new LexicalWords(lexicalWordsID, "", "");
				}
				else
				{
					if(preprocessing.equals(NERLexicalResourcesPreProssecingEnum.StopWords))
					{
						preprocessing = NERLexicalResourcesPreProssecingEnum.No;
					}
					else
					{
						preprocessing = NERLexicalResourcesPreProssecingEnum.POSTagging;
					}
				}
			}		
			boolean caseSensitive = Boolean.parseBoolean(PropertiesManager.getPManager().getProperty(WorkflowBasicsDefaulSettings.CASE_SENSITIVE).toString());
			boolean normalization = Boolean.parseBoolean(PropertiesManager.getPManager().getProperty(WorkflowBasicsDefaulSettings.NORMALIZATION).toString());
			boolean rulesWithDics = Boolean.parseBoolean(PropertiesManager.getPManager().getProperty(WorkflowBasicsDefaulSettings.USE_PARTIAL_MATCH_WITH_DICTIONARIES).toString());
			int rulesID = Integer.valueOf(PropertiesManager.getPManager().getProperty(WorkflowBasicsDefaulSettings.RULES_RESOURCE_ID).toString());
			if(rulesID > 0)
			{
				IRule rule = new RulesSet(rulesID, "", "");
				resourceToNER.add(rule, rule.getClassContent(), rule.getClassContent());
			}
			return new NERLexicalResourcesConfiguration(WorkflowBabicDefaultParameters.corpus
					, preprocessing, resourceToNER, WorkflowBabicDefaultParameters.postags,
					stoWords, caseSensitive, normalization,
					rulesWithDics);
		}
		
	};
	
	
	public String getDescription() {
		return this.getDescription();
	}
	
	public ANERPanel getMainComponent(List<String> defaultSettings) throws SQLException, DatabaseLoadDriverException
	{
		return this.getMainComponent(defaultSettings);
	}
	
	public ANERPanel getMainComponent(INERConfiguration conf) throws SQLException, DatabaseLoadDriverException
	{
		return this.getMainComponent(conf);
	}
	
	public String toString()
	{
		return this.toString();
	}
	
	public INERConfiguration getNERConfigurationBasicWorkFlow(ResourcesToNerAnote resourceToNER) throws SQLException, DatabaseLoadDriverException
	{
		return this.getNERConfigurationBasicWorkFlow(resourceToNER);
	}

	public static NERWorkflowProcessesAvailableEnum convertStringtoNERPRocess(String propValue) {
		if(propValue.equals(NERWorkflowProcessesAvailableEnum.NERLexicalResources.toString()))
		{
			return NERWorkflowProcessesAvailableEnum.NERLexicalResources;
		}
		else
			return NERWorkflowProcessesAvailableEnum.NERLexicalResources;	
	}
}
