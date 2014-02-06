package pt.uminho.anote2.process.IE;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.processes.INERSchemaExportReport;
import pt.uminho.anote2.process.IE.ner.export.INERCSVConfiguration;

public interface INERSchema extends IIEProcess{
	public INERSchemaExportReport exportToCSV(File file,INERCSVConfiguration configuration) throws FileNotFoundException, SQLException, DatabaseLoadDriverException;
}
