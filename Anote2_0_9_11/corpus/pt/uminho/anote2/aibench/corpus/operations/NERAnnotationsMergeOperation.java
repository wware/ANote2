package pt.uminho.anote2.aibench.corpus.operations;

import java.sql.SQLException;
import java.util.List;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.NERSchema;
import pt.uminho.anote2.aibench.corpus.gui.report.NERMergeReportGUI;
import pt.uminho.anote2.aibench.corpus.management.database.CorporaDatabaseManagement;
import pt.uminho.anote2.aibench.corpus.structures.MergeNERSchemasExtention;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.processes.INERMergeProcess;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.process.IE.INERProcess;
import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;

@Operation(description="Merge annotations schemas",enabled=false)
public class NERAnnotationsMergeOperation {
	
	
	private Corpus corpus;
	private MergeNERSchemasExtention mergeSchema;
	private TimeLeftProgress progress = new TimeLeftProgress("Merge NER Schemas");

	@Cancel(cancelInBackground=true)
	public void cancel()
	{
		if(mergeSchema!=null)
		{
			mergeSchema.stop();
		}
	}
	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}
	
	@Port(name="Corpus",description="Corpus to Relation Extraction Process",direction=Direction.INPUT,order=1)
	public void getCorpus(Corpus corpus)
	{
		this.corpus=corpus;
	}
	
	@Port(name="PortIEProcess",description="Process that contains Entity information (NER or RE) ",direction=Direction.INPUT,order=2)
	public void getCorpus(List<IIEProcess> ieProcess)
	{
		System.gc();
		IIEProcess baseProcess = ieProcess.get(0);
		ieProcess.remove(0);
		mergeSchema = new MergeNERSchemasExtention(corpus, baseProcess , progress);
		INERMergeProcess mergeReport;
		try {
			mergeReport = mergeSchema.mergeNERProcessesMergeAnnotations(ieProcess);
			if(mergeReport.isFinishing())
			{
				corpus.notifyViewObserver();
				((Corpus) corpus).notifyViewObserver();
				corpus.addProcess(new NERSchema(mergeReport.getNERSchema().getID(),mergeReport.getNERSchema().getCorpus(),
						mergeReport.getNERSchema().getName(),mergeReport.getNERSchema().getType(),mergeReport.getNERSchema().getProperties()));
				new NERMergeReportGUI(mergeReport);
				new ShowMessagePopup("Merged NER Schema Done.");
			}
			else
			{
				int id = HelpDatabase.getNextInsertTableID(GlobalTablesName.processes)-1;
				CorporaDatabaseManagement.removeIEProcess(id);
				new ShowMessagePopup("Merged NER Schema Cancel.");
			}
		} catch (SQLException e) {
			new ShowMessagePopup("Merged NER Schema Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Merged NER Schema Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
		}
		
	}
}
