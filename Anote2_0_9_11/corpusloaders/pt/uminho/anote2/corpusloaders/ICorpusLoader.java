package pt.uminho.anote2.corpusloaders;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IDocumentSet;

public interface ICorpusLoader {
	public IDocumentSet processTextFile() throws SQLException, DatabaseLoadDriverException, IOException;
	public boolean validateFile();
	public File getFileorDirectory();
	public int corpusSize();
	public int corpusLoadPosition();
}
