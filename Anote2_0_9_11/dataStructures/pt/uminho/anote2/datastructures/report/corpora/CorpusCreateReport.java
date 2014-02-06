package pt.uminho.anote2.datastructures.report.corpora;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.CorpusTextType;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.report.corpora.ICorpusCreateReport;
import pt.uminho.anote2.datastructures.report.Report;
import pt.uminho.anote2.process.IE.IIEProcess;

public class CorpusCreateReport extends Report implements ICorpusCreateReport{

	private ICorpus corpus;
	private Set<IIEProcess> processes;
	private CorpusTextType corpusTextType;
	private String name;
	
	public CorpusCreateReport(CorpusTextType corpusTextType,String name) {
		super("Create Corpus Report");
		this.processes = new HashSet<IIEProcess>();
		this.corpusTextType = corpusTextType;
		this.name = name;
	}

	@Override
	public ICorpus getCorpus() {
		return corpus;
	}

	@Override
	public int getDocumentSize() throws SQLException, DatabaseLoadDriverException {
		return corpus.getArticlesCorpus().size();
	}

	@Override
	public Set<IIEProcess> getProcesses() {
		return processes;
	}

	@Override
	public CorpusTextType getCorpusTextType() {
		return corpusTextType;
	}


	@Override
	public void addProcess(IIEProcess process) {
		this.processes.add(process);
	}

	@Override
	public void setCorpus(ICorpus corpus) {
		this.corpus = corpus;
	}

	@Override
	public String getName() {
		return name;
	}

}
