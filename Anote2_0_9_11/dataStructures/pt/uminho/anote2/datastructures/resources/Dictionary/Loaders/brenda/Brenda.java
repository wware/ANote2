package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.brenda;

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
import pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.DictionaryLoaderHelp;
import pt.uminho.anote2.datastructures.utils.FileHandling;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalSources;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
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

public class Brenda extends DictionaryLoaderHelp implements IDicionaryFlatFilesLoader{
	
	private Pattern name = Pattern.compile("^RN\\s(.*)$");
	private Pattern syn1 = Pattern.compile("^SY\\s\\s(.*)$");
	private Pattern syn2 = Pattern.compile("^SY\\s#.*#\\s(.*)\\s<");
	private Pattern externalID = Pattern.compile("^ID\\s(.+)$");
	private Pattern externalID2 = Pattern.compile("^CR\\s(.+)$");

	
	public Brenda(IDictionary dictionary)
	{
		super(dictionary,GlobalSources.brenda);
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public IResourceUpdateReport loadTermsFromFile(File file, Properties prop) throws DatabaseLoadDriverException, SQLException, IOException
	{
		String organism = "";
		if(prop.containsKey(GlobalNames.organimsOption))
		{
			organism = prop.getProperty(GlobalNames.organimsOption);
		}
		String line = new String();
		String term = new String();
		Set<String> termSynomns = new HashSet<String>();
		FileReader fr;
		boolean isOrganism = false;

		BufferedReader br;
		setNextResourceElementID(HelpDatabase.getNextInsertTableID(GlobalTablesName.resource_elements));
		getResource().addResourceContent(GlobalNames.enzymes);
		getReport().updateFile(file);
		getReport().addClassesAdding(1);
		fr = new FileReader(file);
		br = new BufferedReader(fr);
		int lineNumber=0;
		int getLines = FileHandling.getFileLines(file);
		List<IExternalID> externalIds = new ArrayList<IExternalID>();
		long startime = GregorianCalendar.getInstance().getTimeInMillis();
		while((line = br.readLine())!=null)
		{	
			if(line.startsWith("///"))
			{
				if(isOrganism)
				{
					addTermsAndSyn(term,GlobalNames.enzymes, termSynomns,externalIds);
				}
				termSynomns = new HashSet<String>();
				term=new String();
				externalIds= new ArrayList<IExternalID>();
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
				if(m1.find())
				{
					IExternalID e = new ExternalID(m1.group(1), GlobalSources.brenda, -1);
					externalIds.add(e);	
				}
			}
			else if(line.startsWith("CR"))
			{
				if(!line.contains("#"))
				{
					Matcher m1 = this.externalID2.matcher(line);
					if(m1.find())
					{
						IExternalID e = new ExternalID(m1.group(1), GlobalSources.cas, -1);
						externalIds.add(e);	
					}
				}
			}
			if((lineNumber%500)==0)
			{
				memoryAndProgress(lineNumber, getLines);
			}
			lineNumber++;
		}
		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		getReport().setTime(endTime-startime);
		return getReport();
	}

	public Pattern getExternalID2() {
		return externalID2;
	}

	public Pattern getExternalID() {
		return externalID;
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
		return getResource();
	}
	
	public void setcancel(boolean arg0) {
		
	}	

}
