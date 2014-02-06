package pt.uminho.anote2.datastructures.documents;

import pt.uminho.anote2.core.document.IPOSToken;

public class POSToken extends Token implements IPOSToken{

	private String posCategory;
	
	public POSToken(long startOffset, long endOffset, String text,String posCategory) {
		super(startOffset,endOffset,text);
		this.posCategory = posCategory;
	}


	public String getPOSCategory() {
		return posCategory;
	}
	
	public String toString()
	{
		return super.toString() + "_" + getPOSCategory();
	}


	@Override
	public String toString2() {
		return getText() + "_" + getPOSCategory();
	}

	
}
