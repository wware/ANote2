package pt.uminho.anote2.datastructures.documents;

import pt.uminho.anote2.core.document.IChunkerToken;

public class ChunkerToken extends Token implements IChunkerToken{

	private String chunkCAtagory;
	
	public ChunkerToken(long startOffset, long endOffset,String chunkCatagory, String text) {
		super(startOffset, endOffset, text);
		this.chunkCAtagory = chunkCatagory;
	}

	@Override
	public String getChunkerCategory() {
		return chunkCAtagory;
	}
	
	public String toString()
	{
		return super.toString() + "_" + getChunkerCategory();
	}

	@Override
	public String toString2() {

		return "*" + getText() + "*_" + getChunkerCategory() + " ";
	}

}
