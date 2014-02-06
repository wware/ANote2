package pt.uminho.anote2.resource.dictionary;

import java.util.Set;

import pt.uminho.anote2.core.database.IDatabase;

public interface IBiowareHouseLoader extends IDictionaryLoader{
	
	public IDatabase getBiowareHouseDB();
	public void loadTermsFromBiowareHouse(Set<String> classForLoading,boolean synonyms);

}
