package pt.uminho.anote2.core.document;

import java.util.List;

/**
 * Class that represent a Sentence
 * 
 * @author Hugo Costa
 *
 */
public interface ISentence {
	
	/**
	 * Return start offset sentence
	 * 
	 * @return
	 */
	public long getStartOffset();
	
	/**
	 * Return end offset sentence
	 * 
	 * @return
	 */
	public long getEndOffset();
	
	/**
	 * Return sentence Text
	 * 
	 * @return
	 */
	public String getText();
	
	/**
	 * Return {@link List} {@link IPOSToken} from sentence
	 * 
	 * @return
	 */
	public List<IPOSToken> getOrderPOSTokens();
	
	/**
	 * Return {@link List} {@link IChunkerToken} from Sentence
	 * 
	 * @return
	 */
	public List<IChunkerToken> getChunkTokens();
	
	
	public String toString2();

}
