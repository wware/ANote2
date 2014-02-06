package pt.uminho.anote2.resource.dictionary;

public interface IDictionaryLoader {
	
	public void setcancel(boolean cancel);
	public int getNumberTerms();
	public int getNumberSyn();
	public IDictionary getDictionary();

}
