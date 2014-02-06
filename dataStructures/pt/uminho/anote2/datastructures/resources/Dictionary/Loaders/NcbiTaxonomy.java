package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.dictionary.IDicionaryFlatFilesLoader;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class NcbiTaxonomy extends DicionaryLoaderHelp implements IDicionaryFlatFilesLoader{
	
	private Pattern idsentence = Pattern.compile("^(\\d+)\\s");
	private Pattern organismMacthing = Pattern.compile("^\\d+\\s\\|\\s(.*?)\\s\\|");
//	private Pattern firstline = Pattern.compile("1\\s|\\sall\\s|\\s|\\ssynonym\\s|");
	
	private IDictionary dictionary;
	private int organismIDInDatabase;
	private int nextResourceElementID;
	private int sourceID;
	
	public NcbiTaxonomy(IDictionary dictionary)
	{
		super();
		this.dictionary=dictionary;
		sourceID = dictionary.addSource("NcbiTaxonomy");
	}

	public void loadTermsFromFile(File file, Properties prop) 
	{

		String line = new String();
		String term = new String();
		Set<String> termSynomns = new HashSet<String>();
		Matcher m1,m2;
		FileReader fr;
		BufferedReader br;

		this.organismIDInDatabase = dictionary.addElementClass("organism");		
		this.nextResourceElementID = HelpDatabase.getNextInsertTableID(dictionary.getDb(),"resource_elements");	
		this.dictionary.addResourceContent("organism");
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			// the first two lines are irrelevant
			line = br.readLine();
			line = br.readLine();
			String sentenceID="1";
			while((line = br.readLine())!=null)
			{	

				if(!line.startsWith(sentenceID))
				{
					addTermsAndSyn(term, termSynomns,sentenceID);
					termSynomns = new HashSet<String>();
					term=new String();
					m1 = idsentence.matcher(line);
					m1.find();
					sentenceID = m1.group(1);
					m2 = organismMacthing.matcher(line);
					if(m2.find())
					{
						term = m2.group(1);
					}

				}
				else
				{
					if(line.contains("authority"))
					{
						
					}
					else
					{
						m2 = organismMacthing.matcher(line);
						m2.find();
						termSynomns.add(m2.group(1));
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addTermsAndSyn(String term, Set<String> termSynomns,String externalID) {
		if(!term.equals("") && term.length()<300)
		{
			IResourceElement elem;
			elem = new ResourceElement(-1,term,this.organismIDInDatabase,"organism");
			
			if(dictionary.addElement(elem))
			{
				sumTerm();
				for(String syn:termSynomns)
				{
					if(syn.length()<300)
					{
						elem = new ResourceElement(nextResourceElementID,syn,this.organismIDInDatabase,"organism");
						dictionary.addSynomns(elem);
						sumSyn();
					}
				}
				if(!externalID.equals("")&&externalID.length()<50)
				{
					this.dictionary.addExternalID(nextResourceElementID, externalID,sourceID);
				}
				nextResourceElementID++;
			}
		}
	}
	
	public boolean checkFile(File file) {
		
		if(!file.isFile())
		{
			return false;
		}
		FileReader fr = null;
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		try {
			line = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(line==null)
		{
			return false;
		}
		else
		{
			if(!line.contains("1"))
				return false;
			if(!line.contains("all"))
				return false;
			if(!line.contains("synonym"))
			{
				return false;
			}
			else
			{
				return true;
			}
		}
	}	
	
	public Pattern getIdsentence() {
		return idsentence;
	}

	public void setIdsentence(Pattern idsentence) {
		this.idsentence = idsentence;
	}

	public int getOrganismIDInDatabase() {
		return organismIDInDatabase;
	}

	public void setOrganismIDInDatabase(int organismIDInDatabase) {
		this.organismIDInDatabase = organismIDInDatabase;
	}

	public int getNextResourceElementID() {
		return nextResourceElementID;
	}

	public void setNextResourceElementID(int nextResourceElementID) {
		this.nextResourceElementID = nextResourceElementID;
	}

	public Pattern getOrganismMacthing() {
		return organismMacthing;
	}

	public IDictionary getDictionary() {
		return dictionary;
	}
	
	public void setcancel(boolean arg0) {
		
	}

}
