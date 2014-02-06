package pt.uminho.anote2.workflow.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;

import javax.swing.JTabbedPane;

import pt.uminho.anote2.aibench.corpus.gui.help.CorpusCreatePanel;
import pt.uminho.anote2.aibench.corpus.gui.help.NERAdvancePanel;
import pt.uminho.anote2.aibench.corpus.gui.help.REAdvancedPanel;
import pt.uminho.anote2.aibench.publicationmanager.gui.help.IRCrawlingPanelReport;
import pt.uminho.anote2.aibench.publicationmanager.gui.help.IRSearchPanelReport;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewOkButtonOnly;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.corpora.ICorpusCreateReport;
import pt.uminho.anote2.core.report.processes.INERProcessReport;
import pt.uminho.anote2.core.report.processes.IREProcessReport;
import pt.uminho.anote2.core.report.processes.ir.IIRCrawlingProcessReport;
import pt.uminho.anote2.core.report.processes.ir.IIRSearchProcessReport;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.workflow.gui.help.GeneralReportHelpGUI;
import pt.uminho.anote2.workflow.gui.help.ReportGeneralPanel;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class GeneralReportGUI extends DialogGenericViewOkButtonOnly{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTabbedPane panes;
	private ReportGeneralPanel reportPAne;
	

	public GeneralReportGUI(String title)
	{
		super(title);
		initGUI();
	}

	private void initGUI() {
		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.1, 0.0};
		thisLayout.rowHeights = new int[] {7, 7};
		thisLayout.columnWeights = new double[] {0.1};
		thisLayout.columnWidths = new int[] {7};
		this.setLayout(thisLayout);
		panes = new JTabbedPane();
		reportPAne = GeneralReportHelpGUI.getReportGeneralPanel();
		panes.add("General", reportPAne);
		{
			getContentPane().add(panes, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		{
			getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
	}
	
	public void addReport(IIRSearchProcessReport report) throws SQLException, DatabaseLoadDriverException
	{
		if(panes!=null)
			panes.add("Pubmed Search", new IRSearchPanelReport(report));
		reportPAne.setTime(report.getTime());
		reportPAne.addStepToWorkflow("Pubmed Search");
	}
	
	public void addReport(IIRCrawlingProcessReport report) throws SQLException, DatabaseLoadDriverException
	{
		if(panes!=null)
			panes.add("Journal Retrieval", new IRCrawlingPanelReport(report));
		reportPAne.setTime(report.getTime());
		reportPAne.addStepToWorkflow("Journal Retrieval");
	}
	
	public void addReport(ICorpusCreateReport report) throws SQLException, DatabaseLoadDriverException
	{
		if(panes!=null)
			panes.add("Create Corpus", new CorpusCreatePanel(report));
		reportPAne.setTime(report.getTime());
		reportPAne.addStepToWorkflow("Create Corpus");

	}
	
	public void addReport(INERProcessReport report) throws SQLException, DatabaseLoadDriverException
	{
		if(panes!=null)
			panes.add("NER", new NERAdvancePanel(report));
		reportPAne.setTime(report.getTime());
		reportPAne.addStepToWorkflow("NER");
	}
	
	public void addReport(IREProcessReport report) throws SQLException, DatabaseLoadDriverException
	{
		if(panes!=null)
			panes.add("RE", new REAdvancedPanel(report));
		reportPAne.setTime(report.getTime());
		reportPAne.addStepToWorkflow("RE");

	}
	
	@Override
	protected void okButtonAction() {
		finish();
	}

	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink + "Workflow_report";
	}
	
	public void viewReport()
	{
		this.setSize(GlobalOptions.generalWidth, GlobalOptions.generalHeight);
		Utilities.centerOnOwner(this);
//		this.setModal(true);
		this.setVisible(true);
	}

}
