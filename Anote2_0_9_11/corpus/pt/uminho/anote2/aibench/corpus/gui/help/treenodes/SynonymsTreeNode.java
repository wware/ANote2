package pt.uminho.anote2.aibench.corpus.gui.help.treenodes;

public class SynonymsTreeNode {
	
	private String name;
	private int differentSynonyms;
	
	
	public SynonymsTreeNode(String name,int differentSynonyms) {
		super();
		this.name = name;
		this.differentSynonyms = differentSynonyms;
	}
	
	public int getDifferentSynonyms() {
		return differentSynonyms;
	}

	public String getName() {
		return name;
	}
	
	public String toString()
	{
		return getName() + " ("+getDifferentSynonyms()+")";
	}

}
