package pt.uminho.anote2.aibench.corpus.stats;

import java.util.HashMap;
import java.util.Map;

import pt.uminho.anote2.datastructures.annotation.ner.SimpleEntity;


public class DocumentClassEntities {
	


	private Map<Integer,ClassEntities> classIDEntities;
	private Map<Integer,Integer> classIDCount;
	
	public DocumentClassEntities()
	{
		this.classIDEntities = new HashMap<Integer, ClassEntities>();
		this.classIDCount = new HashMap<Integer, Integer>();
	}

	public void update(SimpleEntity simpleEntity) {
		if(classIDEntities.containsKey(simpleEntity.getClassID()))
		{
			if(!classIDEntities.get(simpleEntity.getClassID()).getClassIDEntities().containsKey(simpleEntity))
			{
				classIDEntities.get(simpleEntity.getClassID()).getClassIDEntities().put(simpleEntity,new EntityAndSynonymsStats(simpleEntity.getName()));
			}
			classIDEntities.get(simpleEntity.getClassID()).getClassIDEntities().get(simpleEntity).update(simpleEntity.getName());
			int number = classIDCount.get(simpleEntity.getClassID());
			classIDCount.put(simpleEntity.getClassID(), ++number);
		}
		else
		{
			ClassEntities classEntities = new ClassEntities();
			EntityAndSynonymsStats entStats = new EntityAndSynonymsStats(simpleEntity.getName());
			entStats.update(simpleEntity.getName());
			classEntities.getClassIDEntities().put(simpleEntity,entStats);
			classIDCount.put(simpleEntity.getClassID(), 1);
			classIDEntities.put(simpleEntity.getClassID(), classEntities);
		}
	}

	public Map<Integer, Integer> getClassIDCount() {
		return classIDCount;
	}
	
	public Map<Integer, ClassEntities> getClassIDEntities() {
		return classIDEntities;
	}
	
	
}
