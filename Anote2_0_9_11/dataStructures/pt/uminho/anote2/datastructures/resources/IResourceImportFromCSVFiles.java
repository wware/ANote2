package pt.uminho.anote2.datastructures.resources;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import pt.uminho.anote2.datastructures.utils.generic.CSVFileConfigurations;

public interface IResourceImportFromCSVFiles {
	IResourceUpdateReport loadTermFromGenericCVSFile(File file,CSVFileConfigurations csvfileconfigurations) throws DatabaseLoadDriverException, SQLException, IOException;

}
