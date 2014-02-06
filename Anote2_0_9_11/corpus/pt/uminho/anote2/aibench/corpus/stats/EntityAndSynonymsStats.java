package pt.uminho.anote2.aibench.corpus.stats;

import java.io.Serializable;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 
 * @author Hugo Costa
 * 
 * Objectivo : Pequena classe que guarda informa��o sobre ocorrencia de entidades no corpus
 * 
 * Classe que no futuro podera ganhar maior importancia
 *
 */

public class EntityAndSynonymsStats implements Serializable{




	/**
	 * 
	 */
	private static final long serialVersionUID = -1958522754699452976L;
	private int entityAndSynonymsOccurences;
	private SortedMap<String,Integer> synonymOccurences;
	private boolean haveSynonyms = false;
	private String originalName;


	private int mainTermAnnotations;
	
	public EntityAndSynonymsStats(String originalName)
	{
		this.originalName = originalName;
		this.entityAndSynonymsOccurences=0;
		this.synonymOccurences = new TreeMap<String, Integer>();
		this.mainTermAnnotations = 0;
	}

	public void update(String termORsynonym)
	{
		if(!originalName.equals(termORsynonym))
		{
			addSynonym(termORsynonym);
		}
		else
		{
			this.mainTermAnnotations++;
		}
		this.entityAndSynonymsOccurences++;
	}
	
	private void addSynonym(String synonym)
	{
		if(synonymOccurences.containsKey(synonym))
		{
			int number = synonymOccurences.get(synonym);
			synonymOccurences.put(synonym, ++number);
		}
		else
		{
			synonymOccurences.put(synonym, 1);
		}
		this.haveSynonyms = true;
	}

	public int getOccurrencesNumber() {
		return entityAndSynonymsOccurences;
	}

	public boolean isHaveSynonyms() {
		return haveSynonyms;
	}	
	
	public int getMainTermAnnotations() {
		return mainTermAnnotations;
	}
	
	
	public String getOriginalName() {
		return originalName;
	}
	
	public Map<String, Integer> getSynonymOccurences() {
		return synonymOccurences;
	}

}
