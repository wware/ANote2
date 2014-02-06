package pt.uminho.anote2.core.document;


/**
 * Interface that represent a Chunker Token ( From Shallow PArsing results)
 * 
 * @author Hugo Costa
 *
 */
public interface IChunkerToken extends IToken{
	
	/**
	 * return Chunker category ( NN,NP,VP)
	 * 
	 * @return
	 */
	public String getChunkerCategory();

	public String toString2();
}
