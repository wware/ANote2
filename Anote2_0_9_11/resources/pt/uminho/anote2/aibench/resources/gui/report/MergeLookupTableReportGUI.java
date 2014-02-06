package pt.uminho.anote2.aibench.resources.gui.report;

import pt.uminho.anote2.core.report.resources.IResourceMergeReport;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;

public class MergeLookupTableReportGUI extends MergeRulesSerReportGUI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MergeLookupTableReportGUI(IResourceMergeReport report) {
		super(report);
	}
	
	
	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"LookupTable_Merge#Report";
	}
}
