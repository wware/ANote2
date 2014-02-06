package pt.uminho.anote2.datastructures.documents;

import java.util.List;

import pt.uminho.anote2.core.document.IChunkerToken;
import pt.uminho.anote2.core.document.IPOSToken;
import pt.uminho.anote2.core.document.ISentence;

public class Sentence implements ISentence{
	
	

	public long startOffset;
	public long endOffset;
	public String text;
	private List<IPOSToken> orderPosTokens;
	private List<IChunkerToken> orderChunkTokens;
	
	public Sentence(long startOffset,long endOffset,String text)
	{
		this.startOffset=startOffset;
		this.endOffset=endOffset;
		this.text=text;
	}
	
	public Sentence(long startOffset,long endOffset,String text,List<IPOSToken> orderPosTokens)
	{
		this.startOffset=startOffset;
		this.endOffset=endOffset;
		this.text=text;
		this.orderPosTokens = orderPosTokens;
	}
	
	public Sentence(long startOffset,long endOffset,String text,List<IPOSToken> orderPosTokens,List<IChunkerToken> orderChunkTokens)
	{
		this.startOffset=startOffset;
		this.endOffset=endOffset;
		this.text=text;
		this.orderPosTokens = orderPosTokens;
		this.orderChunkTokens = orderChunkTokens;
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

	public List<IPOSToken> getOrderPOSTokens() {
		return orderPosTokens;
	}
	
	public String toString()
	{
		String total = new String();
		total = "Limit : "+ getStartOffset() + " <> "+ getEndOffset() + "\n";
		total = total + "Sentence : " + getText() + "\n";
		if(orderPosTokens!=null)
		{
			total = total + "Pos Tags : \n";
			for(IPOSToken token:orderPosTokens)
				total = total + "\t"+token.toString() +"\n";
		}
		if(orderChunkTokens!=null)
		{
			total = total + "Chunker Tags : \n";
			for(IChunkerToken chunkerToken:orderChunkTokens)
				total = total + "\t"+chunkerToken.toString() +"\n";
		}
		return total;
	}
	
	public String toString2()
	{
		String total = new String();
		if(orderPosTokens!=null)
		{
			total = total + "Pos Tags : \n";
			for(IPOSToken token:orderPosTokens)
				total = total + " "+token.toString2();
		}
		if(orderChunkTokens!=null)
		{
			total = total + "Chunker Tags : \n";
			for(IChunkerToken chunkerToken:orderChunkTokens)
				total = total +chunkerToken.toString2();
		}
		return total;
	}


	@Override
	public List<IChunkerToken> getChunkTokens() {
		return orderChunkTokens;
	}

}
