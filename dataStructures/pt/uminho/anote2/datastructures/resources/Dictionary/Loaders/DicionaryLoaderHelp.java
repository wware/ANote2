package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders;

public class DicionaryLoaderHelp {
	
	protected int terms;
	protected int syn;
	
	public DicionaryLoaderHelp()
	{
		terms=0;
		syn=0;
	}
	
	public void sumTerm()
	{
		terms++;
	}
	
	public void sumSyn()
	{
		syn++;
	}

	public int getNumberTerms() {
		return terms;
	}

	public int getNumberSyn() {
		return syn;
	}
	
	

}
