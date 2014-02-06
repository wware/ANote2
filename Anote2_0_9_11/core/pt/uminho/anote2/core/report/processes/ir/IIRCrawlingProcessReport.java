package pt.uminho.anote2.core.report.processes.ir;

import java.util.Set;

import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.core.report.IReport;


public interface IIRCrawlingProcessReport extends IReport{
	public int getDocumentsRetrieval();
	public Set<IPublication> getListPublicationsDownloaded();
	public Set<IPublication> getListPublicationsNotDownloaded();
	public Set<IPublication> getListPublicationRetrictedDownloaded();
	public Set<IPublication> getListPublicationsAlreadyDownloaded();
	public void addFileDownloaded(IPublication pub);
	public void addFileNotDownloaded(IPublication pub);
	public void addFileRestrictedDownloaded(IPublication pub);
	public void addFileAlreadyDownloaded(IPublication pub);
}
