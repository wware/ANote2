package pt.uminho.anote2.corpusloaders;

import java.util.List;

import pt.uminho.anote2.core.document.IAnnotatedDocument;

public interface ICorpusEntityLoader extends ICorpusLoader{
	public List<IAnnotatedDocument> getEntityAnnotaions();
}
