package pt.uminho.anote2.ner.corpus;

import java.util.ArrayList;
import java.util.List;

public class NormTerm {

	private List<String> terms;
	private String id;
	
	public NormTerm(String id){
		this.id = id;
		this.terms = new ArrayList<String>();
	}
	
	public NormTerm(List<String> terms, String id) {
		this.terms = terms;
		this.id = id;
	}
	
	public void addTerm(String term){
		this.terms.add(term);
	}
	
	public List<String> getTerms() {
		return terms;
	}
	public void setTerms(List<String> terms) {
		this.terms = terms;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
