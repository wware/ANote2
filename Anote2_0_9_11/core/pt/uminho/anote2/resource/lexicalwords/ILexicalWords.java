package pt.uminho.anote2.resource.lexicalwords;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceMergeReport;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;

public interface ILexicalWords extends IResource<IResourceElement>{
	
	public boolean exportCSVFile(String csvFile) throws IOException, SQLException, DatabaseLoadDriverException;
	public Set<String> getLexicalWords() throws SQLException, DatabaseLoadDriverException;
	public int getLexicalWordID(String lexicalWord) throws SQLException, DatabaseLoadDriverException;
	public IResourceMergeReport merge(ILexicalWords idSncDic) throws DatabaseLoadDriverException, SQLException;

}
