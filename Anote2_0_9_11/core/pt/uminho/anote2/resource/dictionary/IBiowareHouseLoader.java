package pt.uminho.anote2.resource.dictionary;

import java.sql.SQLException;
import java.util.Set;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;

public interface IBiowareHouseLoader extends IDictionaryLoader{
	
	public IDatabase getBiowareHouseDB();
	public IResourceUpdateReport loadTermsFromBiowareHouse(Set<String> classForLoading,boolean synonyms) throws SQLException, DatabaseLoadDriverException;

}
