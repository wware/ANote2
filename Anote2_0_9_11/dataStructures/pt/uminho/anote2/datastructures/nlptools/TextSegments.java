package pt.uminho.anote2.datastructures.nlptools;

import pt.uminho.anote2.core.document.ITextSegment;
import pt.uminho.anote2.datastructures.documents.Token;

public class TextSegments extends Token implements ITextSegment{

	public TextSegments(long startOffset, long endOffset, String text) {
		super(startOffset, endOffset, text);
	}
	
	public String toString()
	{
		return super.toString();
	}
	
}
