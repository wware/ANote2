package pt.uminho.anote2.aibench.corpus.gui.help.treenodes;

public class SynonymTreeNode {
	

	private String name;
	private int occurrences;
	
	public SynonymTreeNode(String name, int occurrences) {
		super();
		this.name = name;
		this.occurrences = occurrences;
	}
	
	public String getName() {
		return name;
	}
	public int getOccurrences() {
		return occurrences;
	}
	
	public String toString()
	{
		return getName() + " ("+getOccurrences()+")";
	}
	
}
