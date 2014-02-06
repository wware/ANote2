package pt.uminho.anote2.core.document;

/**
 * Class that represents a Token
 * 
 * @author Hugo Costa
 *
 */
public interface IToken {
	
	public long getStartOffset();
	public long getEndOffset();
	public String getText();

}
