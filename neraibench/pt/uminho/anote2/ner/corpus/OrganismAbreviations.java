package pt.uminho.anote2.ner.corpus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrganismAbreviations {

	/** Associates the abreviation of organism to its long form 
	 * @throws Exception */
	public Map<String, TreeSet<Abreviation>> getOrganismAssocs() throws Exception{
		Map<String, TreeSet<Abreviation>> assocs = new HashMap<String, TreeSet<Abreviation>>();
/*
		for(String pmid : globalAnnotationsCname.keySet())
		{
			assocs.put(pmid, new TreeSet<Abreviation>());
			Set<String> terms = globalAnnotationsCname.get(pmid).get("organism").keySet();

			for(String norm : terms)
				if(isAbbr(norm))
				{
					String longForm = longForm(norm, terms);
					if(longForm != null)
						assocs.get(pmid).add(new Abreviation(longForm, norm, null, null));			
				}
			if(assocs.get(pmid).size()==0)
				assocs.remove(pmid);
		}
		
		for(String pmid : globalAnnotationsId.keySet())
		{
			if(!assocs.containsKey(pmid))
				assocs.put(pmid, new TreeSet<Abreviation>());
			
			Map<String, String> map = new HashMap<String, String>();

			for(String id : globalAnnotationsId.get(pmid).get("organism").keySet())
				for(String term : globalAnnotationsId.get(pmid).get("organism").get(id))
					map.put(term, id);
			
			for(String term : map.keySet())
				if(isAbbr(term))
				{
					String longForm = longForm(term, map.keySet());
					if(longForm != null)
						assocs.get(pmid).add(new Abreviation(longForm, term, map.get(longForm), map.get(term)));			
				}
			if(assocs.get(pmid).size()==0)
				assocs.remove(pmid);
		}
		*/
		return assocs;
	}
	
	public boolean isAbbr(String term){
		Pattern p = Pattern.compile("^[A-Z]+\\.\\s[a-zA-Z]+$");
		Matcher m = p.matcher(term);
		return m.find();
	}
	
	public String longForm(String term, Set<String> terms){
		String tokens[] = term.split(". ");
		Pattern p = Pattern.compile("^" + tokens[0] + "[a-zA-Z]+\\s" + tokens[1] + "$");
		for(String t: terms)
		{
			Matcher m = p.matcher(t);
			if(m.find())
				return t;
		}
		
		return null;
	}
	
}
