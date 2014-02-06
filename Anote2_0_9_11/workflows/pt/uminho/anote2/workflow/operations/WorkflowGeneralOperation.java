package pt.uminho.anote2.workflow.operations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.NERSchema;
import pt.uminho.anote2.aibench.corpus.datatypes.RESchema;
import pt.uminho.anote2.aibench.publicationmanager.dataStructures.PubMedSearchExtension;
import pt.uminho.anote2.aibench.publicationmanager.dataStructures.PubmedCrawlExtention;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.ISimpleTimeLeft;
import pt.uminho.anote2.aibench.utils.timeleft.ITimeLeftProgressSteps;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgressSteps;
import pt.uminho.anote2.core.corpora.ICorpusCreateConfiguration;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.report.corpora.ICorpusCreateReport;
import pt.uminho.anote2.core.report.processes.INERProcessReport;
import pt.uminho.anote2.core.report.processes.IREProcessReport;
import pt.uminho.anote2.core.report.processes.ir.IIRCrawlingProcessReport;
import pt.uminho.anote2.core.report.processes.ir.IIRSearchProcessReport;
import pt.uminho.anote2.datastructures.documents.query.Query;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.process.IE.ner.INERConfiguration;
import pt.uminho.anote2.process.IE.re.IREConfiguration;
import pt.uminho.anote2.process.IR.IIRSearchConfiguration;
import pt.uminho.anote2.process.IR.exception.InternetConnectionProblemException;
import pt.uminho.anote2.workflow.datastructures.WorkflowStep;
import pt.uminho.anote2.workflow.gui.GeneralReportGUI;
import pt.uminho.anote2.workflow.processes.WorkflowCreateCorpus;
import pt.uminho.anote2.workflow.processes.WorkflowNER;
import pt.uminho.anote2.workflow.processes.WorkflowPubmedSearch;
import pt.uminho.anote2.workflow.processes.WorkflowRE;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation
public class WorkflowGeneralOperation {
	
	private ITimeLeftProgressSteps progress = new TimeLeftProgressSteps();
	private GeneralReportGUI report;
	private boolean cancel;
	private PubMedSearchExtension pubmed;
	private PubmedCrawlExtention crawl;
	private Corpus corpus;
	private WorkflowNER ners;
	private WorkflowRE res;
	private String corpusname;
	private String createdBy;
	
	@Cancel
	public void cancel() throws SQLException{
		cancel=true;
		if(pubmed!=null)
		{
			pubmed.setCancel(true);
			return;
		}
		if(crawl!=null)
		{
			crawl.stop();
			return;
		}
		if(ners!=null)
		{
			ners.stop();
			return;
		}
		if(res!=null)
		{
			res.stop();	
			return;
		}		
	}
	
	
	@Progress
	public ISimpleTimeLeft getProgress() {
		return progress;
	}

	
	
	@Port(name="test",direction=Direction.INPUT,order=1)
	public void getOutput(List<Object> list)
	{
		System.gc();
		report = new GeneralReportGUI("Workflow Information Retrieval and Extraction - Report");
		createdBy = "Workflow Information Retrieval and Extraction on date "+Utils.currentTime();
		list.remove(0);
		try {
			processinformation(list);
			report.viewReport();
		} catch (InternetConnectionProblemException e) {
			new ShowMessagePopup("Workflow Information Retrieval and Extraction Fail");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Workflow Information Retrieval and Extraction Fail");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (SQLException e) {
			new ShowMessagePopup("Workflow Information Retrieval and Extraction Fail");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (Exception e) {
			new ShowMessagePopup("Workflow Information Retrieval and Extraction Fail");
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}

	private void processinformation(List<Object> list) throws SQLException, DatabaseLoadDriverException, Exception {
		int totalSteps = list.size();
		List<String> steps = calculateSteps(list);
		progress.setSteps(steps);
		if(totalSteps > 0 && !cancel)
		{
			if(totalSteps > 1)
			{
				ICorpusCreateConfiguration conf = (ICorpusCreateConfiguration) list.get(1);
				this.corpusname = conf.getCorpusName();
			}
			progress.increaseStep();
			informationRetrieval(list.get(0));
		}
		if(totalSteps > 1 && !cancel)
		{
			progress.increaseStep();
			createCorpus(list.get(1));
		}
		if(totalSteps > 2 && !cancel)
		{
			progress.increaseStep();
			ner(list.get(2));
		}
		if(totalSteps > 3 && !cancel)
		{
			progress.increaseStep();
			re(list.get(3));
		}
	}


	private void informationRetrieval(Object object) throws InternetConnectionProblemException, DatabaseLoadDriverException, SQLException {
		if(object instanceof IIRSearchConfiguration)
		{
			pubmed = new PubMedSearchExtension(cancel,progress);
			pubmed.setViewPrelimiraryInfo(false);
			IIRSearchConfiguration configuration = (IIRSearchConfiguration) object;
			IIRSearchProcessReport irreport = WorkflowPubmedSearch.processPubmedSearch(pubmed,configuration);
			report.addReport(irreport);
			List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(PublicationManager.class);
			PublicationManager pm = (PublicationManager) cl.get(0).getUserData();
			QueryInformationRetrievalExtension query = PublicationManager.getLastQuery(pm);
			if(!corpusname.isEmpty())
				query.setQueryName(corpusname);
			query.addProperty(GlobalNames.createdby, createdBy);
			pm.addQueryInformationRetrievalExtension(query);
		}
		else
		{
			Workbench.getInstance().error("Problem with Search Configuration");
			return;
		}
	}


	private void createCorpus(Object object) throws DatabaseLoadDriverException, SQLException {
		if(object instanceof ICorpusCreateConfiguration)
		{
			ICorpusCreateConfiguration conf = (ICorpusCreateConfiguration) object;
			Query query = WorkflowCreateCorpus.getLastQuery();
			IIRCrawlingProcessReport rep = WorkflowCreateCorpus.processIRCrawl(crawl,conf, query,progress);
			if(rep!=null)
			{
				report.addReport(rep);
				progress.increaseStep();
			}
			List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(Corpora.class);
			Corpora project = (Corpora) items.get(0).getUserData();
			ICorpusCreateReport reportCreateCorpus = WorkflowCreateCorpus.processCreateCorpus(project,conf,query,progress);
			ICorpus cp = reportCreateCorpus.getCorpus();
			corpus = new Corpus(cp.getID(), cp.getDescription(), project, cp.getProperties());
			corpus.addCorpusProperties(GlobalNames.createdby, createdBy);
			report.addReport(reportCreateCorpus);
			project.addCorpus(corpus);
		}
		else
		{
			Workbench.getInstance().error("Problem with CreateCorpus Configuration");
			return;
		}
	}

	private void ner(Object object) throws DatabaseLoadDriverException, SQLException, Exception {
		if(object instanceof INERConfiguration)
		{
			INERConfiguration conf = (INERConfiguration) object;
			ners = new WorkflowNER();
			INERProcessReport nerreport = ners.ner(this.corpus,conf,progress);
			report.addReport(nerreport);
			NERSchema nerSchema = new NERSchema(nerreport.getNERProcess());
			nerSchema.addProperty(GlobalNames.createdby, createdBy);
			((Corpus) corpus).addProcess(nerSchema);
		}
		else
		{
			Workbench.getInstance().error("Problem with NER Configuration");
			return;
		}
	}


	private void re(Object object) throws SQLException, DatabaseLoadDriverException, Exception {;
		if(object instanceof IREConfiguration)
		{
			IREConfiguration conf = (IREConfiguration) object;
			res = new WorkflowRE();
			IREProcessReport rereport = res.re(this.corpus,conf,progress);
			report.addReport(rereport);
			RESchema reSchema = new RESchema(rereport.getREProcess());	
			reSchema.addProperty(GlobalNames.createdby, createdBy);
			((Corpus) corpus).addProcess(reSchema);
		}
		else
		{
			Workbench.getInstance().error("Problem with NER Configuration");
			return;
		}
		
	}

	
	private List<String> calculateSteps(List<Object> list) {
		List<String> steps = new ArrayList<String>();
		if(list.size() > 0)
		{
			steps.add(WorkflowStep.PubmedSearch.toString());
		}
		if(list.size() > 1)
		{

			Object object = list.get(1);
			if(object  instanceof ICorpusCreateConfiguration)
			{
				ICorpusCreateConfiguration conf = (ICorpusCreateConfiguration) object;
				if(conf.processJournalRetrievalBefore())
				{
					steps.add("Journal Retrieval");
				}
			}
			steps.add(WorkflowStep.CreateCorpus.toString());
		}
		if(list.size() > 2)
		{
			steps.add(WorkflowStep.NER.toString());
		}
		if(list.size() > 3)
		{
			steps.add(WorkflowStep.RE.toString());
		}
		return steps;
	}

}
