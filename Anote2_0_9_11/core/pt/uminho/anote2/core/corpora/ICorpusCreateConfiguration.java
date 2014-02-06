package pt.uminho.anote2.core.corpora;

import java.util.Set;

import pt.uminho.anote2.core.document.CorpusTextType;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IPublication;

/**
 * Interface to define parameter to create Corpus
 * 
 * @author Hugo Costa
 *
 */
public interface ICorpusCreateConfiguration {
	
	/**
	 * Method that return corpus name
	 * 
	 * @return
	 */
	public String getCorpusName();
	
	/**
	 * Method that return set of {@link IPublication} for add  to new {@link ICorpus}
	 * 
	 * @return
	 */
	public Set<IPublication> getDocuments();
	
	
	public void setDocuments( Set<IPublication> docs);
	
	/**
	 * Method that return {@link CorpusTextType}
	 * 
	 * @return
	 */
	public CorpusTextType getCorpusTextType();
	
	/**
	 * Method that return is during the {@link Corpus} creation executes a Journal Retrieval
	 * 
	 * @return
	 */
	public boolean processJournalRetrievalBefore();
}
