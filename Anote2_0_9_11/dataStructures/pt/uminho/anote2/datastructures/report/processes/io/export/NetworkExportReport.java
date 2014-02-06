package pt.uminho.anote2.datastructures.report.processes.io.export;

import pt.uminho.anote2.core.report.processes.ie.io.exporter.INetworkExportReport;
import pt.uminho.anote2.datastructures.report.Report;

public class NetworkExportReport extends Report implements INetworkExportReport{

	private int numberOFNodes;
	private int numberofEdges;
	
	public NetworkExportReport(String title,int numberOFNodes,int numberofEdges) {
		super(title);
		this.numberOFNodes = numberOFNodes;
		this.numberofEdges = numberofEdges;
	}
	
	public int getNumberOFNodes() {
		return numberOFNodes;
	}
	public int getNumberofEdges() {
		return numberofEdges;
	}
	
	
}
