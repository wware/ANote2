package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.ByoCyc;

import java.io.File;
import java.util.GregorianCalendar;
import java.util.Properties;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.MySQLDatabase;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.resource.dictionary.IDicionaryFlatFilesLoader;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class BioCycReaction extends BioCyc implements IDicionaryFlatFilesLoader{
	
	
	public BioCycReaction(IDictionary dictionary) {
		super(dictionary);
	}

	public boolean checkFile(File file) {
		return cheackFiles(file,"# File Name: reactions.dat");
	}

	public void loadTermsFromFile(File file, Properties prop) {
		loadReactions(file);
	}

}
