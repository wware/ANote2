package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.ByoCyc;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import pt.uminho.anote2.datastructures.utils.HTMLCodes;
import pt.uminho.anote2.resource.dictionary.IDicionaryFlatFilesLoader;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class BioCycCompound extends BioCyc implements IDicionaryFlatFilesLoader {
	
	
	private HTMLCodes htmlCodes = new HTMLCodes();

	public BioCycCompound(IDictionary dictionary) {
		super(dictionary);
		this.htmlCodes = new HTMLCodes();
	}



	public boolean checkFile(File file) {
		return cheackFiles(file,"# File Name: compounds.dat");
	}

	public IResourceUpdateReport loadTermsFromFile(File file, Properties prop) throws DatabaseLoadDriverException, SQLException, IOException {
		return loadCoumpound(file);
	}
	
	public void setcancel(boolean arg0) {
		
	}
	
	protected void updateSynonyms(String line, Set<String> termSynomns) {
		String synonym;
		Matcher m = getSyn().matcher(line);
		m.find();
		synonym = m.group(1).replaceAll(cleanTag, "");
		synonym = htmlCodes.cleanString(synonym);
		termSynomns.add(synonym);
	}

	protected String findTerm(String line) {
		String term;
		Matcher m = getName().matcher(line);
		m.find();
		term = m.group(1).replaceAll(cleanTag, "");
		term = htmlCodes.cleanString(term);
		return term;
	}
	
}
