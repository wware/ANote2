package pt.uminho.anote2.core.report.corpora;

import java.sql.SQLException;
import java.util.Set;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.CorpusTextType;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.report.IReport;
import pt.uminho.anote2.process.IE.IIEProcess;

public interface ICorpusCreateReport extends IReport{
	public ICorpus getCorpus();
	public String getName();
	public int getDocumentSize() throws SQLException, DatabaseLoadDriverException;
	public Set<IIEProcess> getProcesses(); 
	public CorpusTextType getCorpusTextType();
	public void addProcess(IIEProcess process);
	public void setCorpus(ICorpus corpus);
}
