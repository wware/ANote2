package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.ByoCyc;

import java.io.File;
import java.util.Properties;

import pt.uminho.anote2.resource.dictionary.IDicionaryFlatFilesLoader;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class BioCycGene extends BioCyc implements IDicionaryFlatFilesLoader{
	
	public BioCycGene(IDictionary dictionary) {
		super(dictionary);
	}

	public boolean checkFile(File file) {
		return cheackFiles(file,"# File Name: genes.dat");
	}

	public void loadTermsFromFile(File file, Properties prop) {
		loadGenes(file);
	}
	
}
