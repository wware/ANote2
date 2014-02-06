package pt.uminho.anote2.aibench.corpus.stats;

import java.util.SortedMap;
import java.util.TreeMap;

import pt.uminho.anote2.datastructures.annotation.ner.SimpleEntity;


public class ClassEntities {
	
	private SortedMap<SimpleEntity,EntityAndSynonymsStats> classIDEntities;
	
	public ClassEntities()
	{
		setClassIDEntities(new TreeMap<SimpleEntity, EntityAndSynonymsStats>());
	}

	public SortedMap<SimpleEntity,EntityAndSynonymsStats> getClassIDEntities() {
		return classIDEntities;
	}

	public void setClassIDEntities(SortedMap<SimpleEntity,EntityAndSynonymsStats> classIDEntities) {
		this.classIDEntities = classIDEntities;
	}
}
