package pt.uminho.anote2.process.IE;

import java.sql.SQLException;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.processes.IREProcessReport;

public interface IREProcess extends IIEProcess{
	public IREProcessReport executeRE() throws DatabaseLoadDriverException, SQLException, Exception;
	public void stop();
}
