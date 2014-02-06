package pt.uminho.anote2.aibench.curator.view.panes.tree.term;

public class TermNode {
	
	private int count;
	private String term;
	private int databaseID;
	private boolean synonyms;
	
	
	public TermNode(String term,int count,int databaseID)
	{
		this.term=term;
		this.count=count;
		this.databaseID=databaseID;
		this.synonyms = false;
	}
	
	public void addCount(int count)
	{
		this.count = this.count+count;
		this.synonyms = true;
	}


	public int getCount() {
		return count;
	}


	public String getTerm() {
		return term;
	}


	public int getDatabaseID() {
		return databaseID;
	}


	public boolean isSynonyms() {
		return synonyms;
	}
	
	public String toString()
	{
		if(synonyms)
		{
			return term + " and Synonyms (" + count + ")"; 
		}
		else
		{
			return term + " (" + count + ")"; 
		}
	}
	

}
