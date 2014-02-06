package pt.uminho.anote2.core.report.processes;

import pt.uminho.anote2.core.report.IReport;
import pt.uminho.anote2.process.ProcessTypeEnum;

public interface IProcessReport extends IReport{
	
	public int getNumberOfDocuments();
	public ProcessTypeEnum getProcessType();

}
