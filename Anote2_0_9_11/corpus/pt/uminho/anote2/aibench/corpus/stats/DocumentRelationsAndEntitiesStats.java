package pt.uminho.anote2.aibench.corpus.stats;

public class DocumentRelationsAndEntitiesStats extends DocumentEntityStatistics{

	private int relations;
	
	public DocumentRelationsAndEntitiesStats(int docID) {
		super(docID);
	}
	
	public void addNumberOFRelations(int relationNumber)
	{
		this.relations = relationNumber;
	}
	
	public int getRelationsNumber()
	{
		return this.relations;
	}

}
