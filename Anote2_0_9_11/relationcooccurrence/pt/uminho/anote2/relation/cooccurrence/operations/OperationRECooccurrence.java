package pt.uminho.anote2.relation.cooccurrence.operations;

import java.sql.SQLException;
import java.util.Properties;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.RESchema;
import pt.uminho.anote2.aibench.corpus.gui.report.REReportGUI;
import pt.uminho.anote2.aibench.corpus.management.database.CorporaDatabaseManagement;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.report.processes.IREProcessReport;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.relation.cooccurrence.configuration.IRECooccurrenceConfiguration;
import pt.uminho.anote2.relation.cooccurrence.core.ARECooccurrence;
import pt.uminho.anote2.relation.cooccurrence.core.RECooccurrenceModelEnum;
import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;

@Operation(enabled=false)
public class OperationRECooccurrence {
	
	
	private TimeLeftProgress progress = new TimeLeftProgress("RE: Co-occurrence");
	private ARECooccurrence reco;
	
	@Cancel(cancelInBackground=true)
	public void cancel()
	{
		if(reco!=null)
		{
			reco.stop();
		}
	}
	
	@Port(name="Corpus",description="Corpus to Relation Extraction Cooccurrence Process",direction=Direction.INPUT,order=1)
	public void getCorpus(ICorpus corpus)
	{

	}
	
	@Port(name="Configuration",description="Configuration Options",direction=Direction.INPUT,order=2)
	public void getTAgger(IRECooccurrenceConfiguration configuration)
	{
		System.gc();
		Properties prop = new Properties();
		Corpus corpus = (Corpus) configuration.getCorpus();
		RECooccurrenceModelEnum model = configuration.getCooccurrenceModel();
		IEProcess ieProcess = (IEProcess) configuration.getIEProcess();
		if(ieProcess .getProperties().containsKey(GlobalNames.normalization))
		{
			if(Boolean.valueOf(ieProcess.getProperties().getProperty(GlobalNames.normalization)))
			{
				prop.put(GlobalNames.normalization, ieProcess.getProperties().getProperty(GlobalNames.normalization));
			}
		}
		prop.put(GlobalNames.entityBasedProcess,String.valueOf(ieProcess.getID()));
		try {
			reco = model.getRelationCooccurrenceModel(corpus, ieProcess, prop,progress);
			corpus.registerProcess(reco);
			IREProcessReport report = reco.executeRE();
			if(report.isFinishing())
			{
				((Corpus) corpus).notifyViewObserver();
				((Corpus) corpus).addProcess(new RESchema(reco.getID(),reco.getCorpus(),reco.getName(),reco.getType(),reco.getProperties()));
				new REReportGUI(report);
				new ShowMessagePopup("RE Cooccurrence @Note Process Done !!");
			}
			else
			{
				CorporaDatabaseManagement.removeIEProcess(reco.getID());
				new ShowMessagePopup("RE Cooccurrence @Note Process Cancel !!");
			}
			System.gc();
		} catch (SQLException e) {
			new ShowMessagePopup("RE Cooccurrence @Note Process Fail !!");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("RE Cooccurrence @Note Process Fail !!");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (Exception e) {
			new ShowMessagePopup("RE Cooccurrence @Note Process Fail !!");
			TreatExceptionForAIbench.treatExcepion(e);
		}


	}
	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}

}
