package pt.uminho.anote2.aibench.corpus.stats;

import pt.uminho.anote2.datastructures.general.ClassProperties;

public class ClassNumberOfOccurrences {
	private int classID;
	private int numberOfOccurrences;
	
	public ClassNumberOfOccurrences(int classID, int numberOfOccurrences) {
		super();
		this.classID = classID;
		this.numberOfOccurrences = numberOfOccurrences;
	}
	
	public String toString()
	{
		return ClassProperties.getClassIDClass().get(classID) + ": "+numberOfOccurrences;
	}
	
}
