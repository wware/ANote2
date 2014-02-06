package pt.uminho.anote2.process.IE;

import java.sql.SQLException;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.report.processes.INERProcessReport;

public interface INERProcess extends IIEProcess{
	public INERProcessReport executeCorpusNER(ICorpus corpus) throws SQLException, DatabaseLoadDriverException, Exception;
	public void stop();
	
}
