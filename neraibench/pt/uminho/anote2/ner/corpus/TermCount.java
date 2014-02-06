package pt.uminho.anote2.ner.corpus;

public class TermCount {
	private int realCount;
	private boolean caseSensitive;
	
	public TermCount() {
		this.realCount = 0;
		this.caseSensitive = false;
	}
	
	public TermCount(boolean caseSensitive) {
		this.realCount = 0;
		this.caseSensitive = caseSensitive;
	}
	
	public TermCount(int count) {
		this.realCount = count;
		this.caseSensitive = false;
	}
	
	public void incRealCount(){
		this.realCount++;
	}
	
	public void incRealCount(int c){
		this.realCount += c;
	}
	
	public void decRealCount(int c){
		this.realCount -= c;
	}
	
	
	public int getRealCount() {
		return realCount;
	}
	public void setRealCount(int realCount) {
		this.realCount = realCount;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}
	
	
}
