package pt.uminho.anote2.aibench.curator.operation;

import java.sql.SQLException;
import java.util.Properties;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.NERSchema;
import pt.uminho.anote2.aibench.corpus.datatypes.RESchema;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.documents.Publication;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.process.IE.manualcuration.ManualCurationEnum;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation(enabled=false)
public class OperationManualCuration {

	private Corpus corpus;
	private ManualCurationEnum manualCurationType;
	
	@Port(name="Corpus",direction=Direction.INPUT,order=1)
	public void setCorpus(Corpus corpus)
	{
		this.corpus=corpus;
	
	}
	
	@Port(name="CuratorType",direction=Direction.INPUT,order=2)
	public void setCorpus(ManualCurationEnum manualCurationType)
	{
		this.manualCurationType=manualCurationType;
	
	}

	@Port(name="Process Name",direction=Direction.INPUT,order=3)
	public void setProcessName(String processName)
	{
		System.gc();
		Properties prop = new Properties();
		String textType = corpus.getProperties().getProperty(GlobalNames.textType);
		prop.put(GlobalNames.textType,textType);	
		IEProcess process = new IEProcess(corpus,processName,manualCurationType.getProcessName(),prop);
		try {
			corpus.registerProcess(process);
			if(textType.equals(GlobalNames.fullText))
			{
				findFullText();
			}
			else if(textType.equals(GlobalNames.abstractOrFullText))
			{
				findFullText();
			}
			corpus.notifyViewObserver();
			if(manualCurationType == ManualCurationEnum.NER)
			{
				corpus.addProcess(new NERSchema(process.getID(),process.getCorpus(),process.getName(),process.getType(),process.getProperties()));
			}
			else
			{
				corpus.addProcess(new RESchema(process.getID(),process.getCorpus(),process.getName(),process.getType(),process.getProperties()));
			}
			new ShowMessagePopup("Manual Curation Process Created");
		} catch (SQLException e) {
			new ShowMessagePopup("Manual Curation Process Created Fail");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Manual Curation Process Created Fail");
			TreatExceptionForAIbench.treatExcepion(e);
		}

	}
	
	private void findFullText() throws SQLException, DatabaseLoadDriverException {
		for(IDocument doc:corpus.getArticlesCorpus())
		{
			IPublication pub = (IPublication) doc;
			((Publication) pub).findFullTextArticle();
		}
		
	}	

}
