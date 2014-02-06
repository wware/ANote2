package pt.uminho.anote2.core.annotation;

/**
 * Interface that represents a Simple Annotation
 * 
 * @author Hugo Costa
 *
 */
public interface IAnnotation extends Cloneable{

	/**
	 * Return Annotation Type (NER,RE)
	 * 
	 * @return {@link String}
	 */
	public String getType();
	
	/**
	 * Return start offset in text for Annotation
	 * 
	 * @return {@link Long}
	 */
	public long getStartOffset();
	
	/**
	 * Return end offset in text for Annotation
	 * 
	 * @return
	 */
	public long getEndOffset();
	
	/**
	 * Return DatabaseID for annotation
	 * 
	 * @return
	 */
	public int getID();
	
	
	public IAnnotation clone();
}
