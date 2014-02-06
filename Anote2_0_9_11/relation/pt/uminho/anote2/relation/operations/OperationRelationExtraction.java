package pt.uminho.anote2.relation.operations;

import java.sql.SQLException;
import java.util.Properties;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.RESchema;
import pt.uminho.anote2.aibench.corpus.gui.report.REReportGUI;
import pt.uminho.anote2.aibench.corpus.management.database.CorporaDatabaseManagement;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.report.processes.IREProcessReport;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.process.IE.IIEProcess;
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
import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;

@Operation(description="Extract Relations",enabled=false)
public class OperationRelationExtraction {
	
	private TimeLeftProgress progress = new TimeLeftProgress("RE: Rel@tioN");
	private RelationExtraxtionExtension relExtraction;
	
	@Cancel(cancelInBackground=true)
	public void cancel()
	{
		if(relExtraction!=null)
		{
			relExtraction.stop();
		}
	}
	

	@Port(name="corpus",direction=Direction.INPUT,order=1)
	public void getCorpus(ICorpus corpus)
	{

	}
	
	@Port(name="configuration",direction=Direction.INPUT,order=2)
	public void getVerbAddition(IRERelationConfiguration relationConfiguration) throws Exception
	{
		findRelations(relationConfiguration);
	}
	
	public void findRelations(IRERelationConfiguration relationConfiguration) throws Exception
	{
		PosTaggerEnem tagger = relationConfiguration.getPOSTagger();
		ILexicalWords verbFilter = relationConfiguration.getVerbsFilter();
		ILexicalWords verbAddition = relationConfiguration.getVerbsAddition();
		IGatePosTagger postagger = tagger.getPostagger(new Directionality(), new Polarity(), verbFilter , verbAddition);
		IIEProcess ieProcess = relationConfiguration.getIEProcess();
		RelationsModelEnem model = relationConfiguration.getRelationModel();
		ICorpus corpus = relationConfiguration.getCorpus();
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
		relExtraction.closePS();
		if(report.isFinishing())
		{
			((Corpus) corpus).notifyViewObserver();
			((Corpus) corpus).addProcess(new RESchema(relExtraction));
			new REReportGUI(report);
			new ShowMessagePopup("RE @Note Process Done !!");
		}
		else
		{
			try {
				CorporaDatabaseManagement.removeIEProcess(relExtraction.getID());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			new ShowMessagePopup("RE @Note Process Cancel !!");
		}
	}
		
	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}


}
