package pt.uminho.anote2.process.IE.re;

import java.util.Map;

import pt.uminho.anote2.datastructures.utils.GenericTriple;

public interface ISentenceSintaxRepresentation {

	public void addElement(Long startOffset,String word,String sintax,String lemma);
	public GenericTriple<String, String, String> getNextElement(Long offset);	
	public Map<Long, GenericTriple<String, String, String>> getSentenceSintax();
}
