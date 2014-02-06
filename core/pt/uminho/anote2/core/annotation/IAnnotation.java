package pt.uminho.anote2.core.annotation;

public interface IAnnotation extends Cloneable{

	/**
	 * NER - compound, gene, ...
	 * RE - 
	 * ...
	 * Doc Relevance - relevant, irrelevant...
	 */
	public String getType();
	public long getStartOffset();
	public long getEndOffset();
	public int getID();
	public IAnnotation clone();
}
