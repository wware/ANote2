package pt.uminho.anote2.workflow.processes;

import java.sql.SQLException;
import java.util.Properties;

import pt.uminho.anote2.aibench.utils.timeleft.ITimeLeftProgressSteps;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.report.processes.IREProcessReport;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.process.IE.re.IREConfiguration;
import pt.uminho.anote2.relation.configuration.IRERelationAdvancedConfiguration;
import pt.uminho.anote2.relation.configuration.IRERelationConfiguration;
import pt.uminho.anote2.relation.core.RelationExtraxtionExtension;
import pt.uminho.anote2.relation.datastructures.Directionality;
import pt.uminho.anote2.relation.datastructures.Polarity;
import pt.uminho.anote2.relation.datastructures.PosTaggerEnem;
import pt.uminho.anote2.relation.datastructures.RelationsModelEnem;
import pt.uminho.anote2.relation.properties.RERelationNames;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;
import pt.uminho.gate.process.IGatePosTagger;

public class WorkflowRE {
	
	private RelationExtraxtionExtension relExtraction;
	
	public WorkflowRE()
	{
		
	}
	
	public void stop() {
		if(relExtraction!=null)
			relExtraction.stop();
	}

	public IREProcessReport re(ICorpus corpus, IREConfiguration conf,ITimeLeftProgressSteps progress) throws SQLException, DatabaseLoadDriverException,Exception {
		if(conf instanceof IRERelationConfiguration)
		{
			IRERelationConfiguration relationConfiguration = (IRERelationConfiguration) conf;
			PosTaggerEnem tagger = relationConfiguration.getPOSTagger();
			ILexicalWords verbFilter = relationConfiguration.getVerbsFilter();
			ILexicalWords verbAddition = relationConfiguration.getVerbsAddition();
			IGatePosTagger postagger = tagger.getPostagger(new Directionality(), new Polarity(), verbFilter , verbAddition);
			IIEProcess ieProcess = corpus.getIProcesses().get(0);
			RelationsModelEnem model = relationConfiguration.getRelationModel();
			Properties prop = new Properties();
			prop.put(GlobalNames.entityBasedProcess,String.valueOf(ieProcess.getID()));
			prop.put(GlobalNames.taggerName,String.valueOf(tagger.name()));
			prop.put(GlobalNames.relationModel,model.toString());
			if(verbFilter!=null)
			{
				prop.put(GlobalNames.verbFilter,String.valueOf(verbFilter.getID()));
			}
			if(verbAddition!=null )
			{
				if(model.getDescription().equals("Binary Selected Verbs Only"))
				{
					prop.put(GlobalNames.verbAdditionOnly,String.valueOf(verbAddition.getID()));
				}
				else
				{
					prop.put(GlobalNames.verbAddition,String.valueOf(verbAddition.getID()));
				}
			}
			if(ieProcess.getProperties().containsKey(GlobalNames.normalization))
			{
				if(Boolean.valueOf(ieProcess.getProperties().getProperty(GlobalNames.normalization)))
				{
					prop.put(GlobalNames.normalization, ieProcess.getProperties().getProperty(GlobalNames.normalization));
				}
			}
			IRERelationAdvancedConfiguration advancedConfiguration = relationConfiguration.getAdvancedConfiguration();
			if(advancedConfiguration!=null)
			{
				if(advancedConfiguration.usingOnlyVerbNearestEntities())
				{
					prop.put(RERelationNames.usingOnlyVerbNearestEntities,"true");
				}
				else if(advancedConfiguration.usingOnlyEntitiesNearestVerb())
				{
					prop.put(RERelationNames.usingOnlyEntitiesNearestVerb,"true");
				}
				else if(advancedConfiguration.usingVerbEntitiesDistance())
				{
					if(advancedConfiguration.getVerbEntitieMaxDistance()>0)
					{
						prop.put(RERelationNames.usingVerbEntitiesDistance,"true");
						prop.put(RERelationNames.verbEntitiesDistance,String.valueOf(advancedConfiguration.getVerbEntitieMaxDistance()));
					}
				}
				if(advancedConfiguration.getRelationsType()==null || advancedConfiguration.getRelationsType().size() == 0)
				{
					prop.put(RERelationNames.relationsTypeSelected,"All");
				}
				else if(advancedConfiguration.getRelationsType()!=null )
				{
					prop.put(RERelationNames.relationsTypeSelected,"Filter");
				}
			}
			relExtraction = new RelationExtraxtionExtension(corpus,ieProcess,postagger,model.getRelationModel(corpus, ieProcess, postagger,verbAddition,advancedConfiguration),prop,progress);
			corpus.registerProcess(relExtraction);
			IREProcessReport report = relExtraction.executeRE();
			return report;
		}
		return null;
	}
	
	

}
