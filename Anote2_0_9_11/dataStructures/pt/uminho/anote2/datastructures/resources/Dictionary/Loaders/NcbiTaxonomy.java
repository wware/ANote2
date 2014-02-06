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

public class NcbiTaxonomy extends DictionaryLoaderHelp implements IDicionaryFlatFilesLoader{
	
	private Pattern idsentence = Pattern.compile("^(\\d+)\\s");
	private Pattern organismMacthing = Pattern.compile("^\\d+\\s\\|\\s(.*?)\\s\\|");
	//	private Pattern firstline = Pattern.compile("1\\s|\\sall\\s|\\s|\\ssynonym\\s|");

	public NcbiTaxonomy(IDictionary dictionary)
	{
		super(dictionary,GlobalSources.ncbitaxonomy);
	}

	public IResourceUpdateReport loadTermsFromFile(File file, Properties prop) throws DatabaseLoadDriverException, SQLException, IOException 
	{
		getReport().updateFile(file);
		String line = new String();
		String term = new String();
		Set<String> termSynomns = new HashSet<String>();
		Matcher m1,m2;
		FileReader fr;
		BufferedReader br;
		setNextResourceElementID(HelpDatabase.getNextInsertTableID(GlobalTablesName.resource_elements));	
		getDictionary().addResourceContent(GlobalNames.organism);
		getReport().addClassesAdding(1);
		fr = new FileReader(file);
		br = new BufferedReader(fr);
		// the first two lines are irrelevant
		line = br.readLine();
		line = br.readLine();
		String sentenceID="1";
		long start = GregorianCalendar.getInstance().getTimeInMillis();
		int total = FileHandling.getFileLines(file);
		fr = new FileReader(file);
		br = new BufferedReader(fr);			
		int lines=0;
		while((line = br.readLine())!=null)
		{	

			if(!line.startsWith(sentenceID))
			{
				List<IExternalID> externalIDs = new ArrayList<IExternalID>();
				externalIDs.add(new ExternalID(String.valueOf(sentenceID),GlobalSources.ncbitaxonomy ,-1));
				addTermsAndSyn(term,GlobalNames.organism, termSynomns,externalIDs);
				termSynomns = new HashSet<String>();
				term=new String();
				m1 = getIdsentence().matcher(line);
				m1.find();
				sentenceID = m1.group(1);
				m2 = getOrganismMacthing().matcher(line);
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
					m2 = getOrganismMacthing().matcher(line);
					m2.find();
					termSynomns.add(m2.group(1));
				}
			}
			if((lines%500)==0)
			{
				memoryAndProgress(lines, total);
			}
			lines++;
		}
		long end = GregorianCalendar.getInstance().getTimeInMillis();
		getReport().setTime(end-start);
		return getReport();

	}
	
	public boolean checkFile(File file) {

		if(!file.isFile())
		{
			return false;
		}
		getReport().updateFile(file);
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

	public Pattern getOrganismMacthing() {
		return organismMacthing;
	}
	
	public void setcancel(boolean arg0) {
		
	}

	@Override
	public IDictionary getDictionary() {
		// TODO Auto-generated method stub
		return getResource();
	}

}
