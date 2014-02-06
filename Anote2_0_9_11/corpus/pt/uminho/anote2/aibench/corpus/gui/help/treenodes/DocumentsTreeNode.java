package pt.uminho.anote2.aibench.corpus.gui.help.treenodes;

public class DocumentsTreeNode {
	
	private int documents;
	
	public DocumentsTreeNode(int documents)
	{
		this.documents = documents;
	}

	public int getDocuments() {
		return documents;
	}
	
	public String toString()
	{
		return "Documents ("+documents+")";
	}
}
