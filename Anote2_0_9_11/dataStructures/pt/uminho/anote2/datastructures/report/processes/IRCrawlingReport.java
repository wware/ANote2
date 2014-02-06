package pt.uminho.anote2.datastructures.report.processes;

import java.util.HashSet;
import java.util.Set;

import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.core.report.processes.ir.IIRCrawlingProcessReport;
import pt.uminho.anote2.datastructures.report.Report;

public class IRCrawlingReport extends Report implements IIRCrawlingProcessReport{

	private Set<IPublication> dowloaded;
	private Set<IPublication> notDowloaded;
	private Set<IPublication> restrictedDowloaded;
	private Set<IPublication> restrictedAlreadyDowloaded;
	
	public IRCrawlingReport() {
		super("Journal Retrieval");
		this.dowloaded = new HashSet<IPublication>();
		this.notDowloaded = new HashSet<IPublication>();
		this.restrictedDowloaded = new HashSet<IPublication>();
		this.restrictedAlreadyDowloaded = new HashSet<IPublication>();
	}

	@Override
	public int getDocumentsRetrieval() {
		return dowloaded.size();
	}

	@Override
	public Set<IPublication> getListPublicationsDownloaded() {
		return dowloaded;
	}

	@Override
	public Set<IPublication> getListPublicationsNotDownloaded() {
		return notDowloaded;
	}

	@Override
	public Set<IPublication> getListPublicationRetrictedDownloaded() {
		return restrictedDowloaded;
	}

	@Override
	public void addFileDownloaded(IPublication pub) {
		this.dowloaded.add(pub);
	}

	@Override
	public void addFileNotDownloaded(IPublication pub) {
		this.notDowloaded.add(pub);
	}

	@Override
	public void addFileRestrictedDownloaded(IPublication pub) {
		this.restrictedDowloaded.add(pub);		
	}

	@Override
	public Set<IPublication> getListPublicationsAlreadyDownloaded() {
		return restrictedAlreadyDowloaded;
	}

	@Override
	public void addFileAlreadyDownloaded(IPublication pub) {
		restrictedAlreadyDowloaded.add(pub);		
	}
	
}
