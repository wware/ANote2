package pt.uminho.anote2.datastructures.process.re.export.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentInformation {
	private Map<Integer, List<String>> documentIDRelationSetences;
	
	public DocumentInformation()
	{
		documentIDRelationSetences = new HashMap<Integer, List<String>>();
	}
	
	public void update(int docID,String sentence)
	{
		if(!documentIDRelationSetences.containsKey(docID))
		{
			documentIDRelationSetences.put(docID, new ArrayList<String>());
		}
		documentIDRelationSetences.get(docID).add(sentence);
	}

	public Map<Integer, List<String>> getDocumentIDRelationSetences() {
		return documentIDRelationSetences;
	}
}
