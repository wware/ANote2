package pt.uminho.anote2.core.document;

/**
 * Class that represent a POS Tag tokne
 * 
 * @author Hugo Costa
 *
 */
public interface IPOSToken extends IToken{
	/**
	 * Return POSCategory Category ( Verb, Noun, ..)
	 * @return
	 */
	public String getPOSCategory();
	public String toString2();
}
