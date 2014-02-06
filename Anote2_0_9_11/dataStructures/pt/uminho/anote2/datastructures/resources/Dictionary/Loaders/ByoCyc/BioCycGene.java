package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.ByoCyc;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import pt.uminho.anote2.resource.dictionary.IDicionaryFlatFilesLoader;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class BioCycGene extends BioCyc implements IDicionaryFlatFilesLoader{
	


	public BioCycGene(IDictionary dictionary) {
		super(dictionary);
	}

	public boolean checkFile(File file) {
		return cheackFiles(file,"# File Name: genes.dat");
	}

	public IResourceUpdateReport loadTermsFromFile(File file, Properties prop) throws DatabaseLoadDriverException, SQLException, IOException {
		return loadGenes(file);
	}
	
}
