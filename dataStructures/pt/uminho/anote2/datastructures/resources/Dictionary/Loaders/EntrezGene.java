package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.MySQLDatabase;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.dictionary.IDicionaryFlatFilesLoader;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class EntrezGene extends DicionaryLoaderHelp implements IDicionaryFlatFilesLoader{
	
	private IDictionary dictionary;
	
	private Pattern geneFinder = Pattern.compile("^(\\d+)\\s+(\\d+)\\s+(\\w+)\\s(.+?)\\s(.+?)\\s");
	private int geneIDInDatabase;
	private int nexgeneElementID;

	private int sourceID;
	
	public EntrezGene(IDictionary dicionary)
	{
		super();
		this.dictionary=dicionary;
		sourceID = dictionary.addSource("EntrezGene");
	}
	
	public boolean checkFile(File file) {
		if(!file.isFile())
		{
			return false;
		}
		Matcher m;
		BufferedReader br;
		String line;
		FileReader fr;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			line = br.readLine();
			m = geneFinder.matcher(line);
			if(m.find())
			{
				return true;
			}
		
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public void loadTermsFromFile(File file, Properties prop)
	{
		Matcher m;
		BufferedReader br;
		String line;
		this.geneIDInDatabase = dictionary.addElementClass("gene");		
		this.nexgeneElementID = HelpDatabase.getNextInsertTableID(dictionary.getDb(), "resource_elements");
		this.dictionary.addResourceContent("gene");
		String organismId = "";
		if(prop.containsKey("organism"))
		{
			organismId = prop.getProperty("organism");
		}
		
		FileReader fr;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			while((line = br.readLine())!=null)
			{
				if(!line.contains("hypothetical protein"))
				{
					m = geneFinder.matcher(line);
					if(m.find())
					{
						if(organismId.equals("")||m.group(1).equals(organismId))
						{
							findGene(m);
						}
					}				
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void findGene(Matcher m) {
		String gene = m.group(3);
		String syn = m.group(4);
		String sy2 = m.group(5);
		if(gene.equals("NEWENTRY")){}
		else
		{
			IResourceElement elem = new ResourceElement(-1,gene,geneIDInDatabase,"gene");
			if(dictionary.addElement(elem))
			{
				sumTerm();
				if(!syn.equals("-"))
				{
					dictionary.addSynomns(new ResourceElement(this.nexgeneElementID,syn,geneIDInDatabase,"gene"));
					sumSyn();
				}
				if(sy2.equals("-"))
				{
					
				}
				else
				{
					String[] synms = sy2.split("\\|");
					for(String synm:synms)
					{
						dictionary.addSynomns(new ResourceElement(this.nexgeneElementID,synm,geneIDInDatabase,"gene"));
						sumSyn();
					}
				}
				this.dictionary.addExternalID(nexgeneElementID,m.group(2),sourceID);			
				this.nexgeneElementID++;
			}
		}
	}

	public int getNexgeneElementID() {
		return nexgeneElementID;
	}

	public void setNexgeneElementID(int nexgeneElementID) {
		this.nexgeneElementID = nexgeneElementID;
	}

	public IDictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(IDictionary dictionary) {
		this.dictionary = dictionary;
	}

	public int getGeneIDInDatabase() {
		return geneIDInDatabase;
	}

	public void setGeneIDInDatabase(int geneIDInDatabase) {
		this.geneIDInDatabase = geneIDInDatabase;
	}

	public Pattern getGeneFinder() {
		return geneFinder;
	}
	
	public void setcancel(boolean arg0) {
	}
}
