package pt.uminho.anote2.corpusloaders;
import java.io.File;
import java.util.List;

import pt.uminho.anote2.core.document.IAnnotatedDocument;


public interface ICorpusEventAnnotationLoader extends ICorpusEntityLoader{
	public List<IAnnotatedDocument> getEventAnnotaions(File filepath);
}
