package pt.uminho.anote2.aibench.publicationmanager.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import pt.uminho.anote2.aibench.publicationmanager.gui.help.IRCrawlingPanelReport;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewOkButtonOnly;
import pt.uminho.anote2.core.report.processes.ir.IIRCrawlingProcessReport;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class IRCrawlingReportGUI extends DialogGenericViewOkButtonOnly{
	
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IIRCrawlingProcessReport report;
	
	
	public IRCrawlingReportGUI(IIRCrawlingProcessReport report)
	{
		this.setTitle(report.getTitle());
		this.report=report;
		initGUI();
		this.setSize(GlobalOptions.smallWidth, GlobalOptions.smallHeight);
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
		
	}	

	private void initGUI() {
		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.1, 0.0};
		thisLayout.rowHeights = new int[] {7, 7};
		thisLayout.columnWeights = new double[] {0.1};
		thisLayout.columnWidths = new int[] {7};
		getContentPane().setLayout(thisLayout);
		{
			getContentPane().add(new IRCrawlingPanelReport(report), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));	
		}
		{
			getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
	}

	protected void okButtonAction() {
		finish();
	}

	protected String getHelpLink() {
		return null;
	}

}
