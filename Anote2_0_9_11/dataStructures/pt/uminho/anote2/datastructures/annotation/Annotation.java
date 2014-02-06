package pt.uminho.anote2.datastructures.annotation;

import pt.uminho.anote2.core.annotation.IAnnotation;

/**
 * 
 * @author Hugo Costa
 *
 */
public class Annotation implements IAnnotation{

	private long startOffset;
	private long endOffset;
	private String type;
	private int id;
	
	public Annotation(int id,long start,long end)
	{
		this.id=id;
		this.startOffset=start;
		this.endOffset=end;
	}

	public long getStartOffset() {
		return startOffset;
	}

	public void setStartOffset(long startOffset) {
		this.startOffset = startOffset;
	}

	public long getEndOffset() {
		return endOffset;
	}

	public void setEndOffset(long endOffset) {
		this.endOffset = endOffset;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getID() {
		return id;
	}
	
	public IAnnotation clone()
	{
		return new Annotation(this.getID(),this.getStartOffset(), this.getEndOffset());
	}
	
	
}
