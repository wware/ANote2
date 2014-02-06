package pt.uminho.anote2.aibench.corpus.gui.help.listnodes;

public class Top10Term {
	private String term;
	private boolean haveSynonyms;
	private int ocurrences;
	private int classID;
	
	
	public Top10Term(String term,int classID, boolean haveSynonyms, int ocurrences) {
		super();
		this.term = term;
		this.haveSynonyms = haveSynonyms;
		this.ocurrences = ocurrences;
		this.classID = classID;
	}
	
	
	public int getClassID() {
		return classID;
	}

	public String getTerm() {
		return term;
	}
	public boolean isHaveSynonyms() {
		return haveSynonyms;
	}
	public int getOcurrences() {
		return ocurrences;
	}
	
	public String toString()
	{
		String result = new String();
		if(haveSynonyms)
		{
			result = getTerm() + " and Synonyms";
		}
		else
		{
			result = getTerm();
		}
		return result + " ("+getOcurrences()+")";
	}
	
	
}
