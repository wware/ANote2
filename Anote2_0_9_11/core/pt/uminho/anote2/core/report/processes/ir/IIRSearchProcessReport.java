package pt.uminho.anote2.core.report.processes.ir;

import pt.uminho.anote2.core.report.processes.IProcessReport;

public interface IIRSearchProcessReport extends IProcessReport{
	public String getKeywords();
	public String getOrganism();
	public void incrementDocumentRetrieval(int documents);
}
