package pt.uminho.anote2.ner.corpus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import pt.uminho.anote2.datastructures.utils.OrderedMap;
import pt.uminho.anote2.ner.comparators.TermComparator;


public class GetGlobalAnnotations {
	
	private OrderedMap<String,TermCount> termCounts; // Map<term, count>
	private Map<String, Map<String,NormTerm>> annotations; // Map<class, Map<cname,ListTerm>>
	
	
	@SuppressWarnings("unchecked")
	public GetGlobalAnnotations(){
		@SuppressWarnings("rawtypes")
		TermComparator comparator = new TermComparator<Object>();
		termCounts = new OrderedMap<String,TermCount>(comparator);
		annotations = new HashMap<String, Map<String,NormTerm>>();
	}
	
	public static boolean isSensitive(String cls, String term){
		if(cls.equals("gene"))
			return true;
		if(cls.equals("protein"))
			return true;
		if( cls.equals("enzyme") && ( term.equals("ppk") || term.equals("Ppx") || term.equals("SpoT") || term.equals("RelA") ) )
			return true;
		if(cls.equals("compound") &&
			(term.equals("Ile") || term.equals("Arg") || term.equals("Glu") || term.equals("Gln") || term.equals("Leu") || term.equals("Ser") || term.equals("ALA") || term.equals("Ala")))
				return true;
		return false;
	}
	
	public OrderedMap<String, TermCount> getTermCounts() {
		return termCounts;
	}

	public Map<String, Map<String, NormTerm>> getAnnotations() {
		return annotations;
	}

	
	public String convertFile(String text) throws IOException{
		text = text.replace("&beta", "bbbbbbbb");
		text = text.replace("&alpha", "aaaaaaaa");
		text = text.replace("&epsilon", "eeeeeeee");
		text = text.replace("<sup>", "");
		text = text.replace("</sup>", "");
		text = text.replace("<sub>", "");
		text = text.replace("</sub>", "");
		text = text.replace("<I>", "");
		text = text.replace("</I>", "");
		text = text.replace("<i>", "");
		text = text.replace("</i>", "");
		return text;
	}
	
	
	
	/** Returns the id of a given term with a given class */
	public String getTermId(String termClass, String term){	
		term = term.toLowerCase();
		for(NormTerm nTerm : annotations.get(termClass).values())
			for(String t : nTerm.getTerms())
				if(term.equals(t.toLowerCase()))
					return nTerm.getId();
		return null;
		
	}

	
}
