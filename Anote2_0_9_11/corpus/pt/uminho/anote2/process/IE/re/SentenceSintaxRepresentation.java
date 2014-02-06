package pt.uminho.anote2.process.IE.re;

import java.util.Iterator;
import java.util.TreeMap;

import pt.uminho.anote2.datastructures.utils.GenericTriple;


public class SentenceSintaxRepresentation implements ISentenceSintaxRepresentation{
	
	/**
	 * Non contain spaces 
	 * 
	 * Offset -> (word,sintax,lemma)
	 */
	private TreeMap<Long,GenericTriple<String,String,String>> sentenceSintax;
	
	public TreeMap<Long, GenericTriple<String, String, String>> getSentenceSintax() {
		return sentenceSintax;
	}

	public SentenceSintaxRepresentation()
	{
		this.sentenceSintax=new TreeMap<Long, GenericTriple<String,String,String>>();
	}
	
	public void addElement(Long startOffset,String word,String sintax,String lemma)
	{
		if(word.equals(""))
			System.err.print("Element can not be empty " + word +" "+ sintax +" "+ lemma);
		else
		{
			GenericTriple<String,String,String> triple = 
					new GenericTriple<String, String, String>(word,sintax,lemma);
			this.sentenceSintax.put(startOffset, triple);
		}
	}
	
	public GenericTriple<String, String, String> getNextElement(Long offset)
	{
		Iterator<Long> it = this.sentenceSintax.keySet().iterator();
		while(it.hasNext())
		{
			Long offsetSintaxPosition = it.next();
			if(offsetSintaxPosition>=offset)
			{
				GenericTriple<String, String, String> triple =
					this.sentenceSintax.get(offsetSintaxPosition);
				return triple;
			}
		}
		return null;
	}

}
