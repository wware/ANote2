package pt.uminho.anote2.workflow.processes;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import pt.uminho.anote2.aibench.ner.configuration.INERLexicalResourcesConfiguration;
import pt.uminho.anote2.aibench.ner.configuration.NERLexicalResourcesPreProssecingEnum;
import pt.uminho.anote2.aibench.ner.datastructures.ElementToNer;
import pt.uminho.anote2.aibench.ner.datastructures.NERCorpusExtention;
import pt.uminho.anote2.aibench.ner.datastructures.ResourcesToNerAnote;
import pt.uminho.anote2.aibench.utils.timeleft.ISimpleTimeLeft;
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
import pt.uminho.anote2.process.IE.ner.INERConfiguration;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;

public class WorkflowNER {

	private NERCorpusExtention nerAnote;
	
	
	public WorkflowNER()
	{
		
	}
	
	public void stop() {
		if(nerAnote!=null)
			nerAnote.stop();
	}

	public INERProcessReport ner(ICorpus corpus, INERConfiguration conf,ISimpleTimeLeft progress) throws DatabaseLoadDriverException, SQLException,Exception {
		if(conf instanceof INERLexicalResourcesConfiguration)
		{
			INERLexicalResourcesConfiguration configuration = (INERLexicalResourcesConfiguration) conf;
			ResourcesToNerAnote resources = configuration.getResourceToNER();
			boolean normalization = configuration.isNormalized();
			boolean isCaseSensitive = configuration.getResourceToNER().isCaseSensitive();
			ElementToNer elementToNER = new ElementToNer(resources, normalization);
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
			INERProcessReport report;
			report = nerAnote.executeCorpusNER(corpus);
			return report;
		}
		return null;
	}

}
