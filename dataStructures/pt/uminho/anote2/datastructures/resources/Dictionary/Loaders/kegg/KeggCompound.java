package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.kegg;

import java.io.File;
import java.util.GregorianCalendar;
import java.util.Properties;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.MySQLDatabase;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.resource.dictionary.IDicionaryFlatFilesLoader;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class KeggCompound extends Kegg implements IDicionaryFlatFilesLoader{
	

	public KeggCompound(IDictionary dictionary) {
		super(dictionary);
	}

	public boolean checkFile(File file) {
		return checkAllFile(file,"Compound");
	}

	public void loadTermsFromFile(File file, Properties prop) {
		String organism = "";
		if(prop.containsKey("organism"))
		{
			organism=prop.getProperty("organism");
		}
		this.loadFileCoumpounds(file,organism);
	}
	
	public void setcancel(boolean arg0) {
		
	}
	
}
