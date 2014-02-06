package pt.uminho.anote2.aibench.corpus.gui.help.treenodes;

public class ClassTreeNode {
	
	private int count;
	private String classe;
	private int classeID;
	
	public ClassTreeNode(String classe,int classID,int count) {
		super();
		this.count = count;
		this.classe = classe;
		this.classeID = classID;
	}
	

	public int getCount() {
		return count;
	}
	public String getClasse() {
		return classe;
	}
	
	@Override
	public String toString() {
		return getClasse() + " ("+getCount()+")";
	}


	public int getClasseID() {
		return classeID;
	}	
	
}
