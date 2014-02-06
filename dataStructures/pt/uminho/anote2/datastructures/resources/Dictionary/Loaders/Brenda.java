package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
 * 
 * Testes : 629s
 * Termos : 4000
 * Syn    : 41000
 * @author Hugo
 * 
 * O externalID tem de ser uma string
 *
 */

public class Brenda extends DicionaryLoaderHelp implements IDicionaryFlatFilesLoader{
	
	private Pattern name = Pattern.compile("^RN\\s(.*)$");
	private Pattern syn1 = Pattern.compile("^SY\\s\\s(.*)$");
	private Pattern syn2 = Pattern.compile("^SY\\s#.*#\\s(.*)\\s<");
	private Pattern externalID = Pattern.compile("^ID\\s(.+)$");
	
	private IDictionary dictionary;
	private int proteinIDInDatabase=1;
	private int nextResourceElementID=1;
	private int sourceID;
	
	public Brenda(IDictionary dictionary)
	{
		super();
		this.dictionary=dictionary;
		sourceID = dictionary.addSource("Brenda");
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
			int i=0;
			String line;
			while((line = br.readLine())!=null&&i<200)
			{	
				if(line.contains("RECOMMENDED_NAME"))
				{
					return true;
				}
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}	
		
		return false;
	}

	public void loadTermsFromFile(File file, Properties prop)
	{
		String organism = "";
		if(prop.containsKey("organism"))
		{
			organism = prop.getProperty("organism");
		}
		String line = new String();
		String term = new String();
		Set<String> termSynomns = new HashSet<String>();
		FileReader fr;
		boolean isOrganism = false;
		BufferedReader br;
		
		this.proteinIDInDatabase = dictionary.addElementClass("protein");		
		this.nextResourceElementID = HelpDatabase.getNextInsertTableID(dictionary.getDb(),"resource_elements");
		this.dictionary.addResourceContent("protein");
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String externalID=new String();

			while((line = br.readLine())!=null)
			{	
				if(line.startsWith("///"))
				{
					if(isOrganism)
					{
						addTermsAndSyn(term, termSynomns,externalID);
					}
					termSynomns = new HashSet<String>();
					term=new String();
					externalID=new String();
					isOrganism = false;
				}
				else if(line.startsWith("PR"))
				{
					if(organismMatching(organism, line))
					{
						isOrganism=true;
					}
				}
				else if(line.startsWith("RN")&&!line.contains("#"))
				{
					term = findTerm(line);
				}
				else if(line.startsWith("SYN")||line.startsWith("SYS"))
				{

				}
				else if(line.startsWith("SY"))
				{
					findSyn(line, termSynomns);
				}
				else if(line.startsWith("ID"))
				{
					Matcher m1 = this.externalID.matcher(line);
					m1.find();
					externalID=m1.group(1);
				}		
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Pattern getExternalID() {
		return externalID;
	}

	public int getSourceID() {
		return sourceID;
	}

	public void findSyn(String line,Set<String> termSynomns) throws IOException {
		Matcher m1;
		if(line.contains("#")){ m1 = syn2.matcher(line);}
		else
		{
			m1 = syn1.matcher(line);
		}
		if(m1.find())
			termSynomns.add(m1.group(1));

	}
	
	public String findTerm(String line) {
		String term;
		Matcher m1;
		m1 = name.matcher(line);
		m1.find();
		term=m1.group(1);
		return term;
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
	
	public void addTermsAndSyn(String term, Set<String> termSynomns,String externalID) {
		if(!term.equals("") && term.length()<300)
		{
			IResourceElement elem;
			elem = new ResourceElement(-1,term,this.proteinIDInDatabase,"organism");
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

	public Pattern getName() {
		return name;
	}

	public Pattern getSyn1() {
		return syn1;
	}

	public Pattern getSyn2() {
		return syn2;
	}

	public IDictionary getDictionary() {
		return dictionary;
	}
	
	public void setcancel(boolean arg0) {
		
	}	

}
