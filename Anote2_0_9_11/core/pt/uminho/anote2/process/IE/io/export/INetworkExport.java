package pt.uminho.anote2.process.IE.io.export;

import java.io.File;

import pt.uminho.anote2.core.report.processes.ie.io.exporter.INetworkExportReport;
import pt.uminho.anote2.process.IE.network.IEdge;
import pt.uminho.anote2.process.IE.network.INetwork;
import pt.uminho.anote2.process.IE.network.INode;

public interface INetworkExport {
	public File getFile();
	public INetwork<? extends INode, ? extends IEdge> getNetwork();
	public String getFileExtention();
	public INetworkExportReport export();
}
