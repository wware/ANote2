package pt.uminho.anote2.aibench.ner.operations;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.NERSchema;
import pt.uminho.anote2.aibench.corpus.gui.report.NERReportGUI;
import pt.uminho.anote2.aibench.corpus.management.database.CorporaDatabaseManagement;
import pt.uminho.anote2.aibench.ner.datastructures.ElementToNer;
import pt.uminho.anote2.aibench.ner.datastructures.NERCorpusExtention;
import pt.uminho.anote2.aibench.ner.datastructures.ResourcesToNerAnote;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.processes.INERProcessReport;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.datastructures.resources.lexiacalwords.LexicalWords;
import pt.uminho.anote2.datastructures.resources.lookuptable.LookupTable;
import pt.uminho.anote2.datastructures.resources.ontology.Ontology;
import pt.uminho.anote2.datastructures.resources.rule.RulesSet;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.ner.ner.NER;
import pt.uminho.anote2.ner.ner.NERPreProcessingPOSTagging;
import pt.uminho.anote2.ner.ner.NERPreProcessingPOSTaggingAndStopWords;
import pt.uminho.anote2.ner.ner.NERPreProcessingStopWords;
import pt.uminho.anote2.ner.ner.rules.HandRules;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;
import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;

@Operation(enabled=false)
public class OperationApplySameNERAnote {
	
	private TimeLeftProgress progress = new TimeLeftProgress("NER: Lexical Resources");
	private Corpus corpus;
	private ILexicalWords stopwords =  null;
	private Set<String> postags =null;
	private NERCorpusExtention nerAnote;
	
	@Cancel(cancelInBackground=true)
	public void cancel()
	{
		if(nerAnote!=null)
		{
			nerAnote.stop();
		}
	}
	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}
	
	@Port(name="Corpus",direction=Direction.INPUT,order=1)
	public void setCorpus(Corpus corpus)
	{
		this.corpus=corpus;
	}
	
	@Port(name="Process",direction=Direction.INPUT,order=2)
	public void setIEProcess(IIEProcess process)
	{
		progress.setTimeString("Load Terms From DB...");
		try {

		Properties prop = process.getProperties();
		getPreProcessing(prop);
		boolean caseSensitive = getCaseSensativeOption(prop);		
		boolean normalization = getNormalizationOption(prop);
		boolean useOtherResourceInformationInRules = getUseOtherResourceInformationInRules(prop);
		ResourcesToNerAnote resources = new ResourcesToNerAnote(caseSensitive,useOtherResourceInformationInRules);
		resources = getResources(prop,caseSensitive,useOtherResourceInformationInRules);
		ElementToNer elementsToNER = new ElementToNer(resources, normalization);
		List<IEntityAnnotation> elements = elementsToNER.getTerms();
		List<IResourceElement> rules = elementsToNER.getRules();
		NER ner;
		String preprocessingProcess = prop.getProperty(GlobalNames.nerpreProcessing);
		if(preprocessingProcess==null)
			preprocessingProcess = "No";
		if(preprocessingProcess.equals(GlobalNames.stopWords)) // Using Stop Words
		{
			if(rules==null || rules.size()==0)
				ner = new NERPreProcessingStopWords(elements,stopwords);
			else
				ner = new NERPreProcessingStopWords(elements, new HandRules(elementsToNER),stopwords);
		}
		else if(preprocessingProcess.equals(GlobalNames.nerpreProcessingPosTagging))// Using POS
		{
			if(rules==null || rules.size()==0)
				ner = new NERPreProcessingPOSTagging(elements,postags);
			else
				ner = new NERPreProcessingPOSTagging(elements, new HandRules(elementsToNER),postags);
		}
		else if(preprocessingProcess.equals(GlobalNames.nerpreProcessingPosTaggingAndStopWords)) // Hybrid
		{
			if(rules==null || rules.size()==0)
				ner = new NERPreProcessingPOSTaggingAndStopWords(elements,postags,stopwords);
			else
				ner = new NERPreProcessingPOSTaggingAndStopWords(elements, new HandRules(elementsToNER),postags,stopwords);
		}
		else // No Processing
		{
			if(rules==null || rules.size()==0)
				ner = new NER(elements);
			else
				ner = new NER(elements, new HandRules(elementsToNER));
		}
		nerAnote = new NERCorpusExtention(corpus,GlobalNames.nerAnote, GlobalNames.ner,elementsToNER,normalization,ner,resources.isCaseSensitive(),progress);
		INERProcessReport report = nerAnote.executeCorpusNER(corpus);
		if(report.isFinishing())
		{
			corpus.notifyViewObserver();
			((Corpus) corpus).addProcess(new NERSchema(nerAnote.getID(),nerAnote.getCorpus(),
					nerAnote.getName(),nerAnote.getType(),nerAnote.getProperties()));
			new NERReportGUI(report);
			new ShowMessagePopup("NER Anote Done.");
		}
		else
		{
			CorporaDatabaseManagement.removeIEProcess(nerAnote.getID());
			new ShowMessagePopup("NER cancel !!");
		}
		} catch (SQLException e) {
			new ShowMessagePopup("NER Anote Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("NER Anote Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (Exception e) {
			new ShowMessagePopup("NER Anote Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}

	private boolean getUseOtherResourceInformationInRules(Properties prop) {
		if(prop.containsKey(GlobalNames.useOtherResourceInformationInRules))
		{
			return true;
		}
		return false;
	}

	private void getPreProcessing(Properties prop) {
		if(prop.containsKey(GlobalNames.nerpreProcessing))
		{
			prop.put(GlobalNames.nerpreProcessing, prop.getProperty(GlobalNames.nerpreProcessing));
			getStopWordProperty(prop);
			getPOStagsOption(prop);
		}
	}

	private void getPOStagsOption(Properties prop) {
		if(prop.containsKey(GlobalNames.nerpreProcessingTags))
		{
			postags = new HashSet<String>();
			String valueID = prop.getProperty(GlobalNames.nerpreProcessingTags);
			String[] values = valueID.split("\\|");
			for(String tag:values)
			{
				postags.add(tag);
			}
		}
	}

	private boolean getCaseSensativeOption(Properties prop) {
		if(prop.containsKey(GlobalNames.casesensitive))
		{
			if(Boolean.valueOf(prop.getProperty(GlobalNames.casesensitive)))
			{
				return true;
			}
		}
		return false;
	}

	private boolean getNormalizationOption(Properties prop) {
		boolean normalization = false;
		if(prop.containsKey(GlobalNames.normalization))
		{
			if(Boolean.valueOf(prop.getProperty(GlobalNames.normalization)))
			{
				normalization = true;
			}
		}
		return normalization;
	}

	private void getStopWordProperty(Properties prop) {
		if(prop.containsKey(GlobalNames.stopWordsResourceID))
		{
//			if(Boolean.valueOf(prop.getProperty(GlobalNames.stopWords)))
			{
				String valueID = prop.getProperty(GlobalNames.stopWordsResourceID);
				int value = Integer.valueOf(valueID);
				if(value>0)
				{
					stopwords = new LexicalWords(value, "", "");			
				}
			}
		}
	}

	private ResourcesToNerAnote getResources(Properties prop, boolean caseSensitive, boolean useOtherResourceInformationInRules) throws SQLException, DatabaseLoadDriverException {
		ResourcesToNerAnote resources = new ResourcesToNerAnote(caseSensitive,useOtherResourceInformationInRules);
		for(String prop_name:prop.stringPropertyNames())
		{
			try {
				if(Integer.parseInt(prop_name)>0)
				{
					String value = prop.getProperty(prop_name);
					{
						IResource<IResourceElement> resourceElem = fillModelQuerysTable(Integer.valueOf(prop_name));
						if(resourceElem.isActive())
						{
							if(value.equals("All Classes"))
							{
								resources.add(resourceElem, resourceElem.getClassContent(), resourceElem.getClassContent());
							}
							else
							{
								Set<Integer> listClassContent = getClassContent(value);
								resources.add(resourceElem, resourceElem.getClassContent(), listClassContent);
							}
						}
					}
				}
			}
			catch(NumberFormatException e) {
				
			}
		}
		return resources;
	}

	private Set<Integer> getClassContent(String value) {
		String[] split = value.split("\\,");
		Set<Integer> classContentID = new HashSet<Integer>();
		for(String contentID:split)
		{
			if(contentID.length()>0)
			{
				classContentID.add(Integer.valueOf(contentID));
			}
		}
		return classContentID;
	}

	private IResource<IResourceElement> fillModelQuerysTable(int resourceID) throws SQLException, DatabaseLoadDriverException{						 
		PreparedStatement statement = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.getResourcesInfo);
		IResource<IResourceElement> resource = null;
		statement.setInt(1, resourceID);
		ResultSet rs = statement.executeQuery();
		int id;
		String type,name,note;
		boolean active;
		if(rs.next())
		{
			id = rs.getInt(1);
			type = rs.getString(2);
			name = rs.getString(3);
			note = rs.getString(4);
			active = rs.getBoolean(5);
			resource = makeResource(id,type,name,note,active);
		}
		rs.close();
		statement.close();
		return resource;
	}
	
	private IResource<IResourceElement> makeResource(int id, String type,String name, String note, boolean active) {
		if(type.equals(GlobalOptions.resourcesDictionaryName))
		{
			return new Dictionary( id, name, note,active);
		}
		else if(type.equals(GlobalOptions.resourcesLookupTableName))
		{
			return new LookupTable(id, name, note,active);
		}
		else if(type.equals(GlobalOptions.resourcesOntologyName))
		{
			return new Ontology(id, name, note,active);
		}
		else if(type.equals(GlobalOptions.resourcesRuleSetName))
		{
			return new RulesSet( id, name, note,active);
		}
		return null;
	}
}
