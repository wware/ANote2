package pt.uminho.anote2.aibench.corpus.gui.help.treenodes;

public class DocumentTreeNode {
	private int id;
	private String pmid;
	private int occurrences;
	
	
	public DocumentTreeNode(int id, String pmid, int occurrences) {
		super();
		this.id = id;
		this.pmid = pmid;
		this.occurrences = occurrences;
	}

	public int getId() {
		return id;
	}


	public String getPmid() {
		return pmid;
	}


	public int getOccurrences() {
		return occurrences;
	}
	
	public String toString()
	{
		if(pmid == null || pmid.length() == 0)
			return "ID : "+getId() + " ("+getOccurrences()+")";
		else
			return "PMID : "+getPmid()+ " ("+getOccurrences()+")";
	}
}
