package pt.uminho.anote2.resource.ontologies;

import java.io.File;

import pt.uminho.anote2.core.database.IDatabase;


public interface IOntologyLoader {
	
	public int getNumberTerms();
	public int getNumberSyn();
	public void getOntology(IOntology ontolgy);
	public IDatabase getDB();
	public boolean validateFile(File file);

}
