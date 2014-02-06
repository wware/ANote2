package pt.uminho.anote2.aibench.ner.operations;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.NERSchema;
import pt.uminho.anote2.aibench.corpus.gui.report.NERReportGUI;
import pt.uminho.anote2.aibench.corpus.management.database.CorporaDatabaseManagement;
import pt.uminho.anote2.aibench.ner.configuration.INERLexicalResourcesConfiguration;
import pt.uminho.anote2.aibench.ner.configuration.NERLexicalResourcesPreProssecingEnum;
import pt.uminho.anote2.aibench.ner.datastructures.ElementToNer;
import pt.uminho.anote2.aibench.ner.datastructures.NERCorpusExtention;
import pt.uminho.anote2.aibench.ner.datastructures.ResourcesToNerAnote;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.report.processes.INERProcessReport;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.ner.ner.NER;
import pt.uminho.anote2.ner.ner.NERPreProcessingPOSTagging;
import pt.uminho.anote2.ner.ner.NERPreProcessingPOSTaggingAndStopWords;
import pt.uminho.anote2.ner.ner.NERPreProcessingStopWords;
import pt.uminho.anote2.ner.ner.rules.HandRules;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;
import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;

@Operation(enabled=false)
public class OperationNerAnote {
	
	private TimeLeftProgress progress = new TimeLeftProgress("NER: Lexical Resources");
	private NERCorpusExtention nerAnote;
	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}
	
	@Cancel(cancelInBackground=true)
	public void cancel()
	{
		if(nerAnote!=null)
		{
			nerAnote.stop();
		}
	}
	

	@Port(name="Corpus",direction=Direction.INPUT,order=1)
	public void setCorpus(Corpus corpus)
	{
	}
	
	
	@Port(name="configuration",direction=Direction.INPUT,order=2)
	public void setConfiguration(INERLexicalResourcesConfiguration configuration)
	{
		progress.setTimeString("Load Terms From DB...");		
		ResourcesToNerAnote resources = configuration.getResourceToNER();
		boolean normalization = configuration.isNormalized();
		boolean isCaseSensitive = configuration.getResourceToNER().isCaseSensitive();
		ElementToNer elementToNER;
		try {
			elementToNER = new ElementToNer(resources, normalization);
			ICorpus corpus = configuration.getCorpus();
			List<IResourceElement> rules = elementToNER.getRules();
			List<IEntityAnnotation> elements = elementToNER.getTerms();
			NER ner;
			if(configuration.getPreProcessingOption() == NERLexicalResourcesPreProssecingEnum.StopWords) // Using Stop Words
			{
				ILexicalWords lexicalwords = configuration.getStopWords();
				if(rules==null || rules.size()==0)
					ner = new NERPreProcessingStopWords(elements,lexicalwords);
				else
					ner = new NERPreProcessingStopWords(elements, new HandRules(elementToNER),lexicalwords);
			}
			else if(configuration.getPreProcessingOption() == NERLexicalResourcesPreProssecingEnum.POSTagging) // Using POS
			{
				Set<String> posTags = configuration.getPOSTags();
				if(rules==null || rules.size()==0)
					ner = new NERPreProcessingPOSTagging(elements,posTags);
				else
					ner = new NERPreProcessingPOSTagging(elements, new HandRules(elementToNER),posTags);
			}
			else if(configuration.getPreProcessingOption() == NERLexicalResourcesPreProssecingEnum.Hybrid) // Hybrid
			{
				Set<String> posTags = configuration.getPOSTags();
				ILexicalWords lexicalwords = configuration.getStopWords();
				if(rules==null || rules.size()==0)
					ner = new NERPreProcessingPOSTaggingAndStopWords(elements,posTags,lexicalwords);
				else
					ner = new NERPreProcessingPOSTaggingAndStopWords(elements, new HandRules(elementToNER),posTags,lexicalwords);
			}
			else // No Processing
			{
				if(rules==null || rules.size()==0)
					ner = new NER(elements);
				else
					ner = new NER(elements, new HandRules(elementToNER));
			}
			nerAnote = new NERCorpusExtention(corpus,GlobalNames.nerAnote, GlobalNames.ner,elementToNER,normalization,ner,isCaseSensitive,progress);
			INERProcessReport report = nerAnote.executeCorpusNER(corpus);
			if(report.isFinishing())
			{			
				((Corpus) corpus).notifyViewObserver();
				((Corpus) corpus).addProcess(new NERSchema(nerAnote.getID(),nerAnote.getCorpus(),
						nerAnote.getName(),nerAnote.getType(),nerAnote.getProperties()));
				new NERReportGUI(report);
				new ShowMessagePopup("NER Lexical Resources Done.");
			}
			else
			{
				CorporaDatabaseManagement.removeIEProcess(nerAnote.getID());
				new ShowMessagePopup("NER Lexical Resources Cancel !!");
			}
			nerAnote = null;
			System.gc();
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("NER Lexical Resources Fail !!");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (SQLException e) {
			new ShowMessagePopup("NER Lexical Resources Fail !!");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (Exception e) {
			new ShowMessagePopup("NER Anote Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
		}
		
	}

	

	
}
