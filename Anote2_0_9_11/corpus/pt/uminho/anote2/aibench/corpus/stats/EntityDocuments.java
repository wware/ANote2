package pt.uminho.anote2.aibench.corpus.stats;

import java.util.HashMap;
import java.util.Map;

import pt.uminho.anote2.core.document.IAnnotatedDocument;

public class EntityDocuments{
	
	private Map<Integer,Integer> documnentIDOcurreences;
	
	public Map<Integer, Integer> getDocumnentIDOcurreences() {
		return documnentIDOcurreences;
	}
	
	public void addEntityToDocument(IAnnotatedDocument doc)
	{
		if(!documnentIDOcurreences.containsKey(doc.getID()))
		{
			documnentIDOcurreences.put(doc.getID(), 1);
		}
		else
		{
			int occurrences = documnentIDOcurreences.get(doc.getID());
			documnentIDOcurreences.put(doc.getID(), ++occurrences);
		}
	}
	
	public EntityDocuments(){
		this.documnentIDOcurreences = new HashMap<Integer, Integer>();
	}
	
}