package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.uminho.anote2.core.annotation.IExternalID;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import pt.uminho.anote2.datastructures.annotation.ExternalID;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.utils.FileHandling;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalSources;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.resource.dictionary.IDicionaryFlatFilesLoader;
import pt.uminho.anote2.resource.dictionary.IDictionary;

/**
 * @note2
 * 
 * @author Hugo Costa
 *
 */

public class UniProt extends DictionaryLoaderHelp implements IDicionaryFlatFilesLoader{
	
	private Pattern full = Pattern.compile("Full=(.+?)\\s*;");
	private Pattern shortName = Pattern.compile("Short=(.+?)\\s*;");
	private Pattern name = Pattern.compile("Name=(.+?)\\s*;");
	private Pattern synomys = Pattern.compile("Synonyms=(.+?)\\s*;");
	private Pattern externalID = Pattern.compile("ID\\s(.+?)\\s\\s");
	private Pattern checkFormat = Pattern.compile("ID\\s(.+?)\\s\\s");
	
	public UniProt(IDictionary dictionary)
	{
		super(dictionary,GlobalSources.uniprot);
	}
	
	public IResourceUpdateReport loadTermsFromFile(File file,Properties prop) throws DatabaseLoadDriverException, SQLException, IOException
	{
		getResource().addResourceContent(GlobalNames.protein);		
		getReport().addClassesAdding(1);
		getReport().updateFile(file);
		setNextResourceElementID(HelpDatabase.getNextInsertTableID(GlobalTablesName.resource_elements));	
		String line = new String();
		String term = new String();
		Set<String> termSynomns = new HashSet<String>();
		FileReader fr;
		boolean isOrganism = false;
		BufferedReader br;
		String organism="";
		ExternalID ext;
		if(prop.containsKey(GlobalNames.organimsOption))
		{
			organism = prop.getProperty(GlobalNames.organimsOption);
		}		
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		fr = new FileReader(file);
		br = new BufferedReader(fr);
		List<IExternalID> externalIDs = new ArrayList<IExternalID>();
		int total = FileHandling.getFileLines(file);
		int lineNumber=0;
		while((line = br.readLine())!=null)
		{	
			if(line.startsWith("//"))
			{
				if(isOrganism)
				{
					addTermsAndSyn(term,GlobalNames.protein, termSynomns,externalIDs);
				}
				termSynomns = new HashSet<String>();
				term=new String();
				externalIDs = new ArrayList<IExternalID>();
				isOrganism = true;
			}
			else if(line.startsWith("OS"))
			{
				isOrganism = organismMatching(organism, line);
			}
			else if(line.startsWith("GN"))
			{
				term = findTErmAndSyn(line, term, termSynomns);
			}
			else if(line.startsWith("DE"))
			{
				findSyn(line, termSynomns);
			}
			else if(line.startsWith("ID"))
			{
				Matcher m = getExternalID().matcher(line);
				if(m.find())
				{
					ext = new ExternalID(m.group(1).trim(), GlobalSources.uniprot, -1);
					externalIDs.add(ext);
				}
			}
			else if(line.startsWith("DR"))
			{
				String[] linediv = line.split("\\s");
				if(linediv.length>4)
				{
					ext = new ExternalID(linediv[4].replace(";", ""), linediv[3].replace(";", ""), -1);
					externalIDs.add(ext);
				}
			}
			if((lineNumber%500)==0)
			{
				memoryAndProgress(lineNumber, total);
			}	
			lineNumber++;	
		}
		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		getReport().setTime(endTime-startTime);
		return getReport();
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
		}
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
	
	public boolean checkFile(File file) {
		
		if(!file.isFile())
		{
			return false;
		}
		getReport().updateFile(file);
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

	public IDictionary getDictionary() {
		return getResource();
	}




}
