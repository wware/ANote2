package pt.uminho.anote2.process.IE;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.processes.IRESchemaExportReport;
import pt.uminho.anote2.process.IE.re.IRECSVConfiguration;

public interface IRESchema extends IIEProcess{
	public IRESchemaExportReport exportToCSV(File file,IRECSVConfiguration configuration) throws FileNotFoundException, SQLException, DatabaseLoadDriverException;
}
