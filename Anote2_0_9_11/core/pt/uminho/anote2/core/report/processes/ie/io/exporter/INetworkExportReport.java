package pt.uminho.anote2.core.report.processes.ie.io.exporter;

import pt.uminho.anote2.core.report.IReport;

public interface INetworkExportReport extends IReport{
	public int getNumberOFNodes();
	public int getNumberofEdges();
}
