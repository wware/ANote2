package pt.uminho.anote2.aibench.publicationmanager.dataStructures;

/**
 * Class that represent a relevance change for one query
 * 
 * @author Rafael Carreira
 *
 */
public class RelevanceChanged{
	
	/**
	 * New Relevance
	 */
	private String new_relevance;
	
	/**
	 * QueryID in database
	 */
	private int idquery;
	
	public RelevanceChanged(int idquery){
		this.new_relevance = null;
		this.idquery = idquery;
	}

	public String getNew_relevance() {
		return new_relevance;
	}

	public void setNew_relevance(String new_relevance) {
		this.new_relevance = new_relevance;
	}
	
	public int getIdquery() {
		return idquery;
	}

	public void setIdquery(int idquery) {
		this.idquery = idquery;
	}
}
