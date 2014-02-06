package pt.uminho.anote2.datastructures.resources.ontology.loaders;

import java.util.List;

public class OntologicalClass {
	
	private String name;
	private String defenition;
	private List<String> is_a;
	private List<String> synonyms;
	
	
	public OntologicalClass(String name, String defenition, List<String> is_a,
			List<String> synonyms) {
		this.name = name;
		this.defenition = defenition;
		this.is_a = is_a;
		this.synonyms = synonyms;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDefenition() {
		return defenition;
	}


	public void setDefenition(String defenition) {
		this.defenition = defenition;
	}


	public List<String> getIs_a() {
		return is_a;
	}


	public void setIs_a(List<String> is_a) {
		this.is_a = is_a;
	}


	public List<String> getSynonyms() {
		return synonyms;
	}


	public void setSynonyms(List<String> synonyms) {
		this.synonyms = synonyms;
	}
	
	
	
	

}
