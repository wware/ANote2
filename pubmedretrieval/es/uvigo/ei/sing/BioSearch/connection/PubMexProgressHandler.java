package es.uvigo.ei.sing.BioSearch.connection;

import es.uvigo.ei.sing.BioSearch.datatypes.CollectionNCBI;

public interface PubMexProgressHandler {
	
	
	public void count(int count);
	
	public void getted(int getted);
	
	public void finished(CollectionNCBI results);
	
	public void error(Throwable error);
	
	public void cancelled();
}
