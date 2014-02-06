package pt.uminho.anote2.aibench.corpus.gui.help.treenodes;


public class EntityTreeNode {
	private String mainTerm;
	private int occurrences;
	boolean haveSynonyms;
	
	
	
	public EntityTreeNode(String mainTerm, int occurrences, boolean haveSynonyms) {
		super();
		this.mainTerm = mainTerm;
		this.occurrences = occurrences;
		this.haveSynonyms = haveSynonyms;
	}
	
	
	public String getMainTerm() {
		return mainTerm;
	}
	public void setMainTerm(String mainTerm) {
		this.mainTerm = mainTerm;
	}
	public int getOccurrences() {
		return occurrences;
	}
	public boolean isHaveSynonyms() {
		return haveSynonyms;
	}
	
	
	public String toString()
	{
		String result = getMainTerm();
		if(haveSynonyms)
		{
			result =result + " and Synonyms ";
		}
		result = result + " ("+occurrences+")";
		return result;
	}
	
}
