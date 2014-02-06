package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders;

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
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.dictionary.IDicionaryFlatFilesLoader;
import pt.uminho.anote2.resource.dictionary.IDictionary;

/**
 * @note2
 * 
 * Testes : 373s
 * Termos : 10000
 * Syn    : 16000
 * @author Hugo Costa
 *
 */

public class UniProt extends DicionaryLoaderHelp implements IDicionaryFlatFilesLoader{
	
	private Pattern full = Pattern.compile("Full=(.+?)\\s*;");
	private Pattern shortName = Pattern.compile("Short=(.+?)\\s*;");
	private Pattern name = Pattern.compile("Name=(.+?)\\s*;");
	private Pattern synomys = Pattern.compile("Synonyms=(.+?)\\s*;");
	private Pattern externalID = Pattern.compile("ID\\s(.+?)\\s\\s");
	private Pattern checkFormat = Pattern.compile("ID\\s(.+?)\\s\\s");

	private IDictionary dictionary;
	private int proteinIDInDatabase;
	private int nextResourceElementID;
	private int sourceID;
	
	public int gn=0;
	
	public UniProt(IDictionary dictionary)
	{
		super();
		this.dictionary=dictionary;
		sourceID = dictionary.addSource("UniProt");
	}
	
	public void loadTermsFromFile(File file,Properties prop)
	{

		String line = new String();
		String term = new String();
		Set<String> termSynomns = new HashSet<String>();
		FileReader fr;
		boolean isOrganism = false;
		BufferedReader br;
		String organism="";
		if(prop.containsKey("organism"))
		{
			organism = prop.getProperty("organism");
		}		
		this.proteinIDInDatabase = dictionary.addElementClass("protein");
		this.dictionary.addResourceContent("protein");		
		this.nextResourceElementID = HelpDatabase.getNextInsertTableID(dictionary.getDb(),"resource_elements");	
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String externalID = new String();
			boolean gn1=true;

			while((line = br.readLine())!=null)
			{	
				if(line.startsWith("//"))
				{
					if(isOrganism)
					{
						addTermsAndSyn(term, termSynomns,externalID);
					}
					termSynomns = new HashSet<String>();
					term=new String();
					externalID=new String();
					isOrganism = true;
					gn1 = true;
				}
				else if(line.startsWith("OS"))
				{
					isOrganism = organismMatching(organism, line);
				}
				else if(line.startsWith("GN")&&gn1)
				{
					term = findTErmAndSyn(line, term, termSynomns);
					gn1=false;
				}
				else if(line.startsWith("DE"))
				{
					findSyn(line, termSynomns);
				}
				else if(line.startsWith("ID"))
				{
					Matcher m = this.externalID.matcher(line);
					if(m.find())
					{
						externalID=m.group(1);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void findSyn(String line, Set<String> termSynomns) {
		Matcher m1;
		Matcher m2;
		m1 = full.matcher(line);
		while(m1.find())
		{
			String prot = m1.group(1);
//			if(line.contains("Uncharacterized")){}
//			else
			{
				termSynomns.add(prot);
			}
		}
		
		m2 = shortName.matcher(line);
		while(m2.find())
		{
			String prot = m2.group(1);
//			if(line.contains("Uncharacterized")){}
//			else
			{
				termSynomns.add(prot);
			}
		}
	}

	public String findTErmAndSyn(String line, String term,
			Set<String> termSynomns) {
		String auxTerm;
		Matcher m1;
		Matcher m2;
		m1 = name.matcher(line);
		if(m1.find())
		{
			auxTerm = m1.group(1);
			term = auxTerm;
			m2 = synomys.matcher(line);
			while(m2.find())
			{
				String synsT = m2.group(1);
				String[] str = synsT.split(", ");
				for(String syn:str)
				{
					termSynomns.add(syn);
				}
			}
		}
		return term;
	}
	
	public void addTermsAndSyn(String term, Set<String> termSynomns,String externalID) {
		if(!term.equals("") && term.length()<300)
		{
			IResourceElement elem;
			elem = new ResourceElement(-1,term,this.proteinIDInDatabase,"");
			if(dictionary.addElement(elem))
			{
				sumTerm();
				for(String syn:termSynomns)
				{
					if(syn.length()<300)
					{
						elem = new ResourceElement(nextResourceElementID,syn,this.proteinIDInDatabase,"organism");
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


	public boolean organismMatching(String organism, String line) {
		boolean isOrganism;
		if(line.contains(organism))
		{
			isOrganism=true;	
		}
		else
		{
			isOrganism=false;
		}
		return isOrganism;
	}
	
	public boolean checkFile(File file) {
		
		if(!file.isFile())
		{
			return false;
		}
		FileReader fr;
		BufferedReader br;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			
			String line;
			int i=0;
			Matcher m;
			while((line = br.readLine())!=null&&i<100)
			{
				m = checkFormat.matcher(line);
				if(m.find())
				{
					return true;
				}
				i++;
			}
			
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return false;
	}
	
	public Pattern getExternalID() {
		return externalID;
	}

	public int getSourceID() {
		return sourceID;
	}

	public IDictionary getDictionary() {
		return dictionary;
	}
	
	public int getProteinIDInDatabase() {
		return proteinIDInDatabase;
	}

	public void setProteinIDInDatabase(int proteinIDInDatabase) {
		this.proteinIDInDatabase = proteinIDInDatabase;
	}

	public int getNextResourceElementID() {
		return nextResourceElementID;
	}

	public void setNextResourceElementID(int nextResourceElementID) {
		this.nextResourceElementID = nextResourceElementID;
	}

	public Pattern getFull() {
		return full;
	}

	public Pattern getShortName() {
		return shortName;
	}

	public Pattern getName() {
		return name;
	}

	public Pattern getSynomys() {
		return synomys;
	}
	
	public void setcancel(boolean arg0) {
		
	}




}
