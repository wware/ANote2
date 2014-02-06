package pt.uminho.anote2.aibench.publicationmanager.gui.help;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.core.report.processes.ir.IIRSearchProcessReport;

public class IRSearchUpdateReport extends IRSearchPanelReport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public IRSearchUpdateReport(IIRSearchProcessReport report) {
		super(report);
		super.changeDocumentRetrieval("Number of New Documents :");
		
	}

	@Override
	protected void putBorder() {
		super.setBorder(BorderFactory.createTitledBorder(null, "Pubmed Search Update Report", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
	}

}
