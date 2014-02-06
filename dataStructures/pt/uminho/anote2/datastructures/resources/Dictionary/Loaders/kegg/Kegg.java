package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.kegg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.DicionaryLoaderHelp;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class Kegg extends DicionaryLoaderHelp{
	
	private Pattern findEntity = Pattern.compile("\\s+(.*)$");
	private Pattern externalID = Pattern.compile("ENTRY\\s+(.+)\\s+?");
	
	private IDictionary dictionary;
	private int classIDInDatabase;
	private int nextResourceElementID;
	private int sourceID;
	
	public Kegg(IDictionary dictionary)
	{
		super();
		this.dictionary=dictionary;
		sourceID = dictionary.addSource("Kegg");
	}
	
	public void loadFileCoumpounds(File file,String organism)
	{
		genericLoadFile(file,organism,"compound");
	}
	
	public void loadFileEnzymes(File file,String organism)
	{
		genericLoadFile(file,organism,"enzyme");
	}
	
	
	private void genericLoadFile(File file,String organism,String biologicalClass)
	{

		String line = new String();
		String term = new String();
		Set<String> termSynomns = new HashSet<String>();
		FileReader fr;
		boolean isOrganism = false;
		boolean inorganism = false;
		BufferedReader br;
		
		this.classIDInDatabase = dictionary.addElementClass(biologicalClass);		
		this.nextResourceElementID = HelpDatabase.getNextInsertTableID(dictionary.getDb(),"resource_elements");	
		this.dictionary.addResourceContent(biologicalClass);
		try {
			fr = new FileReader(file);

		br = new BufferedReader(fr);
		String externalID = new String();
		while((line = br.readLine())!=null)
		{	
			if(line.startsWith("///"))
			{
				if(isOrganism||inorganism==false)
				{
					addTermsAndSyn(term, termSynomns,externalID);
				}
				termSynomns = new HashSet<String>();
				term=new String();
				isOrganism = false;
				inorganism = false;
				externalID = new String();
			}
			else if(line.startsWith("  ORGANISM"))
			{
				isOrganism = organismMatching(organism, line);
				inorganism = true;
			}
			else if(line.startsWith("NAME "))
			{
				term = finTerm(line);
				findSyn(termSynomns, br);
			}
			else if(line.startsWith("ENTRY"))
			{
				
				Matcher m = this.externalID.matcher(line);
				if(m.find())
				{
					externalID = m.group(1);
					String[] externals=externalID.split("  ");
					externalID = externals[0];
				}
			}
		}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	
	public boolean checkAllFile(File file,String type) {
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
			while((line = br.readLine())!=null&&i<100)
			{	
				Matcher m = externalID.matcher(line);
				if(m.find())
				{
					if(line.contains(type)){ return true;}
					else { return false;}
				}
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return false;
	}

	
	public Pattern getExternalID() {
		return externalID;
	}

	public int getSourceID() {
		return sourceID;
	}

	public String finTerm(String line) {
		String term;
		Matcher m1;
		m1 = findEntity.matcher(line);
		m1.find();

		term=m1.group(1);
		if(term.endsWith(";"))
		{
			term = term.substring(0,term.length()-1);
		}
		return term;
	}

	public void findSyn(Set<String> termSynomns, BufferedReader br) throws IOException {
		boolean namesZone=true;
		String line;
		Matcher m1;
		while((line = br.readLine())!=null && namesZone )
		{
			if(!line.startsWith("  "))
			{
				namesZone=false;
			}
			else
			{
				m1 = findEntity.matcher(line);
				m1.find();
				String syn = m1.group(1);
				if(syn.endsWith(";"))
				{
					syn = syn.substring(0,syn.length()-1);
				}
				termSynomns.add(syn);
			}
		}
	}
	
	public boolean organismMatching(String organism, String line) {
		boolean isOrganism;
		if(line.contains(organism)||organism.equals(""))
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
			elem = new ResourceElement(-1,term,this.classIDInDatabase,"organism");
			if(dictionary.addElement(elem))
			{
				sumTerm();
				for(String syn:termSynomns)
				{
					if(syn.length()<300)
					{
						elem = new ResourceElement(nextResourceElementID,syn,this.classIDInDatabase,"organism");
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
	
	public int getClassIDInDatabase() {
		return classIDInDatabase;
	}

	public void setClassIDInDatabase(int classIDInDatabase) {
		this.classIDInDatabase = classIDInDatabase;
	}

	public int getNextResourceElementID() {
		return nextResourceElementID;
	}

	public void setNextResourceElementID(int nextResourceElementID) {
		this.nextResourceElementID = nextResourceElementID;
	}

	public Pattern getFindEntity() {
		return findEntity;
	}

	public IDictionary getDictionary() {
		return dictionary;
	}

}
