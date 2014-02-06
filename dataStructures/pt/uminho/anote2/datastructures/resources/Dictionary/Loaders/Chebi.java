package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders;

import java.io.BufferedReader;
import java.io.File;
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
 * Testes : 841s
 * Termos : 44000
 * Syn    : 32000
 * 
 * @author Hugo
 *
 */


public class Chebi extends DicionaryLoaderHelp implements IDicionaryFlatFilesLoader{
	
	private IDictionary dictionary;
	
	private Pattern geneCounpound = Pattern.compile("^(\\d+)\\s(\\d+)\\s.+?\\t.+?\\t(.*?)\\s+[FT]\\s");
	private Pattern checkFile = Pattern.compile("^ID\\s+COMPOUND_ID\\s+TYPE");
	
	private int coupoundIDInDatabase;
	private int nextResourceElementID;
	private int sourceID;
	
	public Chebi(IDictionary dicionary)
	{
		this.dictionary=dicionary;
		sourceID = dictionary.addSource("Chebi");
	}
	
	public boolean checkFile(File file) {
		if(!file.isFile())
		{
			return false;
		}
		BufferedReader br;
		FileReader fr;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line = br.readLine();
			Matcher m = checkFile.matcher(line);
			if(m.find())
			{
				return true;
			}
			else
			{
				return false;
			}

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void loadTermsFromFile(File file, Properties prop)
	{
		Matcher m;
		BufferedReader br;
		String comp;
		String term = new String();
		Set<String> syn = new HashSet<String>();
		this.nextResourceElementID = HelpDatabase.getNextInsertTableID(dictionary.getDb(),"resource_elements");	

		String line;
		this.coupoundIDInDatabase = dictionary.addElementClass("compound");		
		this.dictionary.addResourceContent("compound");
		FileReader fr;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			// The first line is file field description
			line = br.readLine();
			int id=1;
			
			while((line = br.readLine())!=null)
			{
				m = geneCounpound.matcher(line);
				m.find();
				int lineId = Integer.decode(m.group(2));
				if(lineId!=id)
				{
					addTermsAndSyn(term,syn,String.valueOf(id));
					term = m.group(3);
					syn=new HashSet<String>();
					id=lineId;
				}
				else
				{
					comp = m.group(3);
					syn.add(comp);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void addTermsAndSyn(String term, Set<String> termSynomns,String externalID) {
		if(!term.equals("") && term.length()<300)
		{
			IResourceElement elem;
			elem = new ResourceElement(-1,term,this.coupoundIDInDatabase,"organism");
			if(dictionary.addElement(elem))
			{
				sumTerm();
				for(String syn:termSynomns)
				{
					if(syn.length()<300)
					{
						elem = new ResourceElement(nextResourceElementID,syn,this.coupoundIDInDatabase,"organism");
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
	
	
	
	public Pattern getGeneCounpound() {
		return geneCounpound;
	}

	public void setGeneCounpound(Pattern geneCounpound) {
		this.geneCounpound = geneCounpound;
	}

	public int getCoupoundIDInDatabase() {
		return coupoundIDInDatabase;
	}

	public void setCoupoundIDInDatabase(int coupoundIDInDatabase) {
		this.coupoundIDInDatabase = coupoundIDInDatabase;
	}

	public int getNextResourceElementID() {
		return nextResourceElementID;
	}

	public void setNextResourceElementID(int nextResourceElementID) {
		this.nextResourceElementID = nextResourceElementID;
	}

	public IDictionary getDictionary() {
		return dictionary;
	}
	
	public void setcancel(boolean arg0) {
		
	}

}
