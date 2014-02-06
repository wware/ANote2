package pt.uminho.anote2.resource.lookuptables;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceMergeReport;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;

public interface ILookupTable extends IResource<IResourceElement>{

	public boolean exportCSVFile(String csvFile) throws DatabaseLoadDriverException, SQLException, IOException;
	public IResourceMergeReport merge(ILookupTable idSncDic) throws SQLException, DatabaseLoadDriverException;
	public IResourceMergeReport merge(ILookupTable idSncDic,Set<Integer> classIDs) throws SQLException, DatabaseLoadDriverException;
	
}
