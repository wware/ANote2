package pt.uminho.anote2.resource.dictionary;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;

public interface IDicionaryFlatFilesLoader extends IDictionaryLoader{
	
	public boolean checkFile(File file);
	public IResourceUpdateReport loadTermsFromFile(File file,Properties properties) throws DatabaseLoadDriverException, SQLException, IOException,Exception;

}
