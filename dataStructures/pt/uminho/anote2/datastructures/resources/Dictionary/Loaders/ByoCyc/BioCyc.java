package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.ByoCyc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.MySQLDatabase;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.DicionaryLoaderHelp;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class BioCyc extends DicionaryLoaderHelp{
	
	private Pattern name = Pattern.compile("^COMMON-NAME - (.*)");
	private Pattern syn = Pattern.compile("^SYNONYMS - (.*)");
	private Pattern externalID = Pattern.compile("^UNIQUE-ID - (.*)");
	
	private IDictionary dictionary;
	private int genericIDInDatabase=1;
	private int nextResourceElementID=1;
	private int sourceID;
	
	public BioCyc(IDictionary dictionary)
	{
		super();
		this.dictionary=dictionary;
		sourceID = dictionary.addSource("ByoCyc");
	}
	

	public void loadCoumpound(File file)
	{	
		genericLoadFile(file,"compound");	
	}
	
	public void loadEnzymes(File file)
	{	
		genericLoadFile(file,"enzyme");	
	}
	
	public void loadGenes(File file)
	{	
		genericLoadFile(file,"gene");	
	}
	
	public void loadPathways(File file)
	{	
		genericLoadFile(file,"pathways");	
	}
	
	public void loadProteins(File file)
	{	
		genericLoadFile(file,"protein");	
	}
	
	public void loadReactions(File file)
	{	
		genericLoadFile(file,"enzyme");	
	}
	
	public boolean cheackFiles(File file,String type)
	{
		if(!file.isFile())
		{
			return false;
		}
		FileReader fr;
		BufferedReader br;
		String line;
		int i=0;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			while(((line = br.readLine())!=null) && i<200)
			{	
				if(line.equals(type))
				{
					return true;
				}
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void genericLoadFile(File file,String biologicalClass)
	{

		String line = new String();
		String term = new String();
		Set<String> termSynomns = new HashSet<String>();
		FileReader fr;
		BufferedReader br;
		String externalID=new String();
		
		
		this.genericIDInDatabase = dictionary.addElementClass(biologicalClass);		
		this.nextResourceElementID = HelpDatabase.getNextInsertTableID(dictionary.getDb(),"resource_elements");	
		this.dictionary.addResourceContent(biologicalClass);
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);

			while((line = br.readLine())!=null)
			{	
				if(line.startsWith("//"))
				{

					addTermsAndSyn(term, termSynomns,externalID);
					termSynomns = new HashSet<String>();
					term=new String();
				}
				else if(line.startsWith("COMMON-NAME"))
				{
					Matcher m = name.matcher(line);
					m.find();
					term = m.group(1);
				}
				else if(line.startsWith("SYNONYMS"))
				{
					Matcher m = syn.matcher(line);
					m.find();
					termSynomns.add(m.group(1));
				}
				else if(line.startsWith("UNIQUE-ID"))
				{	
					Matcher m = this.externalID.matcher(line);
					if(m.find())
					{
						externalID = m.group(1);
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Pattern getExternalID() {
		return externalID;
	}


	public void addTermsAndSyn(String term, Set<String> termSynomns,String externalID) {
		if(!term.equals("") && term.length()<300)
		{
			IResourceElement elem;
			elem = new ResourceElement(-1,term,this.genericIDInDatabase,"organism");
			if(dictionary.addElement(elem))
			{
				sumTerm();
				for(String syn:termSynomns)
				{
					if(syn.length()<300)
					{
						elem = new ResourceElement(nextResourceElementID,syn,this.genericIDInDatabase,"organism");
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

	
	public int getGenericIDInDatabase() {
		return genericIDInDatabase;
	}

	public void setGenericIDInDatabase(int genericIDInDatabase) {
		this.genericIDInDatabase = genericIDInDatabase;
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

	public Pattern getSyn() {
		return syn;
	}

	public IDictionary getDictionary() {
		return dictionary;
	}
	
	public void setcancel(boolean arg0) {
		
	}
	
	

}
