package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.kegg;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.resource.dictionary.IDicionaryFlatFilesLoader;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class KeggEnzymes extends Kegg implements IDicionaryFlatFilesLoader{
	
	
	
	public KeggEnzymes(IDictionary dictionary) {
		super(dictionary);
	}

	public boolean checkFile(File file) {
		return checkAllFile(file, "Enzyme");
	}

	public IResourceUpdateReport loadTermsFromFile(File file, Properties prop) throws DatabaseLoadDriverException, SQLException, IOException {
		String organism = "";
		if(prop.containsKey(GlobalNames.organimsOption))
		{
			organism=prop.getProperty(GlobalNames.organimsOption);
		}
		this.loadFileEnzymes(file,organism);
		return getReport();
	}
	
	public void setcancel(boolean arg0) {
		
	}
	

}
