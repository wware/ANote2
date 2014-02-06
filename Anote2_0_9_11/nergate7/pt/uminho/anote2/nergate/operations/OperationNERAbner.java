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
import pt.uminho.anote2.nergate.abner.ABNER;
import pt.uminho.anote2.nergate.abner.ABNERTrainingModel;
import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;

@Operation(enabled=false)
public class OperationNERAbner {
	
	private TimeLeftProgress progress = new TimeLeftProgress("NER: Abner");
	private Corpus corpus;
	private ABNER tagger = null;
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
	public void setNormalization(boolean normalization)
	{
		this.normalization=normalization;
	}
	
	@Port(name="model",direction=Direction.INPUT,order=3)
	public void setModel(ABNERTrainingModel model)
	{
		Properties pro = new Properties();
		if(normalization)
		{
			pro.put(GlobalNames.normalization, String.valueOf(normalization));
		}
		pro.put(GlobalNames.nerAbnerModel, model.toValue());
		tagger = new ABNER(corpus, GlobalNames.nerAbner, GlobalNames.ner, pro,model, progress);
		try {
			corpus.registerProcess(tagger);
			INERProcessReport report = tagger.executeCorpusNER(corpus);
			if(report.isFinishing())
			{
				corpus.addProcess(new NERSchema(tagger));
				corpus.notifyViewObserver();
				new NERReportGUI(report);
				new ShowMessagePopup("NER ABNER Done.");
			}
			else
			{
				CorporaDatabaseManagement.removeIEProcess(tagger.getID());
				new ShowMessagePopup("NER ABNER cancel.");
			}
		} catch (SQLException e) {
			new ShowMessagePopup("NER ABNER Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("NER ABNER Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (Exception e) {
			new ShowMessagePopup("NER ABNER Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
		}

	}

}
