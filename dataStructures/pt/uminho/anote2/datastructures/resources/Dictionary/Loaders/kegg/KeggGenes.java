package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.kegg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.MySQLDatabase;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.resource.dictionary.IDicionaryFlatFilesLoader;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class KeggGenes extends Kegg implements IDicionaryFlatFilesLoader{

	private Pattern ntseq = Pattern.compile("NTSEQ");
	
	public KeggGenes(IDictionary dictionary) {
		super(dictionary);
	}

	public boolean checkFile(File file) {
		
		FileReader fr;
		BufferedReader br;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line;
			int i=0;
			while((line = br.readLine())!=null&&i<100)
			{
				Matcher m = ntseq.matcher(line);
				if(m.find())
				{
					return true;
				}
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	
	public void loadTermsFromFile(File file, Properties prop) {
		loadFileGenes(file);
	}
	
	public void loadFileGenes(File file)
	{
		loadGenericWithoutOrgnanism(file,"gene");
	}
	
	private void loadGenericWithoutOrgnanism(File file,String biologicalClass)
	{
		String line = new String();
		String term = new String();
		Set<String> termSynomns = new HashSet<String>();
		FileReader fr;
		BufferedReader br;
		
		setClassIDInDatabase(getDictionary().addElementClass(biologicalClass));		
		setNextResourceElementID(HelpDatabase.getNextInsertTableID(getDictionary().getDb(),"resource_elements"));	
		getDictionary().addResourceContent(biologicalClass);

		try {
			fr = new FileReader(file);

		br = new BufferedReader(fr);
		String externalID = new String();
		
		while((line = br.readLine())!=null)
		{	
			if(line.startsWith("///"))
			{
				addTermsAndSyn(term, termSynomns,externalID);
				termSynomns = new HashSet<String>();
				term=new String();
				externalID=new String();
			}
			else if(line.startsWith("NAME "))
			{
				term = finTerm(line,termSynomns);
			}
			else if(line.startsWith("ENTRY"))
			{
				Matcher m = getExternalID().matcher(line);
				m.find();
				externalID = m.group(1);
				String[] externals=externalID.split("  ");
				externalID = externals[0];
			}
		}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public String finTerm(String line,Set<String> syn) {
		String term;
		Matcher m1;
		m1 = getFindEntity().matcher(line);
		m1.find();

		term=m1.group(1);
		String[] terms = term.split(", ");
		for(String termAux:terms)
		{
			syn.add(termAux);
		}
		term=terms[0];
		return term;
	}
	
	public void setcancel(boolean arg0) {
		
	}

}
