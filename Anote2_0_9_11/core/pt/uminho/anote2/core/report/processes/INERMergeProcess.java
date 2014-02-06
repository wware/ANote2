package pt.uminho.anote2.core.report.processes;

import java.util.List;

import pt.uminho.anote2.core.report.IReport;
import pt.uminho.anote2.process.IE.IIEProcess;

public interface INERMergeProcess extends IReport{
	public List<IIEProcess> nerProcessesMerged();
	public IIEProcess getNERSchema();
}
