package pt.uminho.anote2.corpusloaders;

import java.io.File;

import pt.uminho.anote2.core.document.IDocumentSet;

public interface ICorpusLoader {
	public IDocumentSet processTextFile();
	public boolean validateFile();
	public File getFileorDirectory();
	public int corpusSize();
	public int corpusLoadPosition();
}
