package pt.uminho.anote2.datastructures.documents;

import pt.uminho.anote2.core.document.IToken;

public class Token implements IToken{
	private long startOffset;
	private long endOffset;
	private String text;
	
	public Token(long startOffset, long endOffset, String text) {
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.text = text;
	}

	public long getStartOffset() {
		return startOffset;
	}

	public long getEndOffset() {
		return endOffset;
	}

	public String getText() {
		return text;
	}
	
	public String toString()
	{
		return getStartOffset() + " <> " + getEndOffset() + " " + getText();
	}

}
