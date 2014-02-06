package pt.uminho.anote2.aibench.curator.datastructures;

import java.io.Serializable;

public class ANoteAnnotation implements Serializable {

	private static final long serialVersionUID = 880592887849344858L;
	private int count;
	private String commonName;
	private String term;
	private String id;
	private String title;
	private String lemma;

	
	public ANoteAnnotation(int count, String commonName, String term, String id, String title, String lemma) {
		this.count = count;
		this.commonName = commonName;
		this.term = term;
		this.id = id;
		this.title = title;
		this.lemma = lemma;
	}
	
	public void incCount(){
		this.count++;
	}
	
	public void incCount(int c){
		this.count += c;
	}
	
	public void decCount(){
		this.count--;
	}
	
	public void decCount(int c){
		this.count -= c;
	}

	
	public ANoteAnnotation clone(){
		return new ANoteAnnotation(this.count, this.commonName, this.term, this.id, this.title, this.lemma);
	}
	
	public int getCount() {return count;}
	public void setCount(int count) {this.count = count;}
	public String getCommonName() {return commonName;}
	public void setCommonName(String commonName) {this.commonName = commonName;}
	public String getTerm() {return term;}
	public void setTerm(String term) {this.term = term;}
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}
	public String getTitle() {return title;}
	public void setTitle(String title) {this.title = title;}
	public String getLemma() {return lemma;}
	public void setLemma(String lemma) {this.lemma = lemma;}
}