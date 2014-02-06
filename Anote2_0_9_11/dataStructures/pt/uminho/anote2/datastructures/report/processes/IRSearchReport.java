package pt.uminho.anote2.datastructures.report.processes;

import pt.uminho.anote2.core.report.processes.ir.IIRSearchProcessReport;
import pt.uminho.anote2.datastructures.report.Report;
import pt.uminho.anote2.process.ProcessTypeEnum;

public class IRSearchReport extends Report implements IIRSearchProcessReport{

	private int numberofdocuments;
	private String organism;
	private String keywords;
	
	public IRSearchReport(String keywords,String organism) {
		super("IR Report");
		this.keywords = keywords;
		this.organism = organism;
		this.numberofdocuments = 0;
	}

	@Override
	public int getNumberOfDocuments() {
		return numberofdocuments;
	}

	@Override
	public ProcessTypeEnum getProcessType() {
		return ProcessTypeEnum.IR;
	}

	@Override
	public String getKeywords() {
		return keywords;
	}

	@Override
	public String getOrganism() {
		return organism;
	}

	@Override
	public void incrementDocumentRetrieval(int moreDocuments) {
		numberofdocuments +=moreDocuments;
	}

}
