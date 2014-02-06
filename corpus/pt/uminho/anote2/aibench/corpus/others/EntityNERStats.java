package pt.uminho.anote2.aibench.corpus.others;

import java.io.Serializable;

/**
 * 
 * @author Hugo Costa
 * 
 * Objectivo : Pequena classe que guarda informação sobre ocorrencia de entidades no corpus
 * 
 * Classe que no futuro podera ganhar maior importancia
 *
 */

public class EntityNERStats implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1958522754699452976L;
	protected int occurrencesNumber;
	
	public EntityNERStats()
	{
		this.occurrencesNumber=0;
	}
	
	public void add()
	{
		this.occurrencesNumber++;
	}

	public int getOccurrencesNumber() {
		return occurrencesNumber;
	}
	
	
	
	

}
