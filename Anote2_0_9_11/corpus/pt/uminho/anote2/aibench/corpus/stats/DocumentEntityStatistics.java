package pt.uminho.anote2.aibench.corpus.stats;

import java.util.HashMap;
import java.util.Map;

public class DocumentEntityStatistics {
	
	private int numberOFEntities;
	private Map<Integer,Integer> classIDStats;
	private int documentID;
	
	public DocumentEntityStatistics(int docID)
	{
		this.documentID = docID;
		this.classIDStats = new HashMap<Integer, Integer>();

	}
	
	public void addEntityClassStatistics(int classID, int numberOFEntities)
	{
		if(!classIDStats.containsKey(classID))
		{
			classIDStats.put(classID, 0);
		}
		Integer data = classIDStats.get(classID);
		classIDStats.put(classID, data + numberOFEntities);
		this.numberOFEntities += numberOFEntities;
	}
	
	public int getNumberOFEntities() {
		return numberOFEntities;
	}

	public Map<Integer, Integer> getClassIDStats() {
		return classIDStats;
	}

	public int getDocumentID() {
		return documentID;
	}


}
