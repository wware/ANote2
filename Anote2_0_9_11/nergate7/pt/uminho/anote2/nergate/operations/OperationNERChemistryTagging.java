package pt.uminho.anote2.nergate.operations;


import java.sql.SQLException;
import java.util.Properties;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.NERSchema;
import pt.uminho.anote2.aibench.corpus.gui.report.NERReportGUI;
import pt.uminho.anote2.aibench.corpus.management.database.CorporaDatabaseManagement;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.processes.INERProcessReport;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.nergate.chemistrytagger.ChemistryTagger;
import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;

@Operation(enabled=false)
public class OperationNERChemistryTagging {
	
	private TimeLeftProgress progress = new TimeLeftProgress("NER: Chemistry Tagger");
	private Corpus corpus;
	private boolean chemistryElements;
	private boolean chemistryCompounds;
	private ChemistryTagger tagger;
	private boolean normalization;
	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}
	
	@Cancel
	public void cancel()
	{
		if(tagger!=null)
		{
			tagger.stop();
		}
	}
	
	@Port(name="Corpus",direction=Direction.INPUT,order=1)
	public void setCorpus(Corpus corpus)
	{
		this.corpus=corpus;
	}
	
	@Port(name="normalized",direction=Direction.INPUT,order=2)
	public void setCorpus(boolean normalization)
	{
		this.normalization=normalization;
	}
	
	@Port(name="ChemistryElements",direction=Direction.INPUT,order=3)
	public void setChemistryElements(boolean chemistryElements)
	{
		this.chemistryElements=chemistryElements;
	}
	
	@Port(name="ChemistryCompounds",direction=Direction.INPUT,order=4)
	public void setChemistryCompounds(boolean chemistryCompounds)
	{
		this.chemistryCompounds=chemistryCompounds;
	}
	
	@Port(name="ChemistryIon",direction=Direction.INPUT,order=5)
	public void setChemistrylon(boolean chemistrylion)
	{
		Properties pro = new Properties();
		if(normalization)
		{
			pro.put(GlobalNames.normalization, String.valueOf(normalization));
		}
		if(chemistrylion)
			pro.put(GlobalNames.nerChemistryTaggerChemistrylon, String.valueOf(chemistrylion));
		if(chemistryCompounds)
			pro.put(GlobalNames.nerChemistryTaggerChemistryCompounds, String.valueOf(chemistryCompounds));
		if(chemistryElements)
			pro.put(GlobalNames.nerChemistryTaggerChemistryElements, String.valueOf(chemistryElements));
		tagger = new ChemistryTagger(corpus, GlobalNames.nerChemistryTagger, GlobalNames.ner, pro, progress, chemistrylion, chemistryElements, chemistryCompounds);
		try {
			corpus.registerProcess(tagger);
			INERProcessReport report =  tagger.executeCorpusNER(corpus);
			if(report.isFinishing())
			{
				corpus.addProcess(new NERSchema(tagger));
				corpus.notifyViewObserver();
				new NERReportGUI(report);
				new ShowMessagePopup("NER Chemistry Tagger Done.");
			}
			else
			{
				CorporaDatabaseManagement.removeIEProcess(tagger.getID());
				new ShowMessagePopup("NER Chemistry Tagger cancel.");
			}
		} catch (SQLException e) {
			new ShowMessagePopup("NER Chemistry Tagger Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("NER Chemistry Tagger Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (Exception e) {
			new ShowMessagePopup("NER Chemistry Tagger Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
		}

	}

}
