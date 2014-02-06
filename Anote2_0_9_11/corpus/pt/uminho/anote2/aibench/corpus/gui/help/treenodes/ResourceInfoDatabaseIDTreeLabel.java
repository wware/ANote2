package pt.uminho.anote2.aibench.corpus.gui.help.treenodes;

public class ResourceInfoDatabaseIDTreeLabel {
	private int databaseID;
	
	
	public ResourceInfoDatabaseIDTreeLabel(int databaseID) {
		super();
		this.databaseID = databaseID;
	}
	
	public int getDatabaseID() {
		return databaseID;
	}

	public String toString()
	{
		return String.valueOf(getDatabaseID());
	}





}
