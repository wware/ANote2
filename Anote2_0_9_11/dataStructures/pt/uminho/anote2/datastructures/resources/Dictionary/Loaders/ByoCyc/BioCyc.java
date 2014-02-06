package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.ByoCyc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

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
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class BioCyc extends DictionaryLoaderHelp{
	
	static Logger logger = Logger.getLogger(BioCyc.class.getName());

	
	private Pattern name = Pattern.compile("^COMMON-NAME - (.*)");
	private Pattern syn = Pattern.compile("^SYNONYMS - (.*)");
	private Pattern externalID = Pattern.compile("^UNIQUE-ID - (.*)");
	private Pattern externalID2 = Pattern.compile("^DBLINKS - \\((.*) \"(.*)\"");
	protected String cleanTag = "<.*?>";
	protected String classe;

	public BioCyc(IDictionary dictionary)
	{
		super(dictionary,GlobalSources.biocyc);
		
	}

	public IResourceUpdateReport loadCoumpound(File file) throws DatabaseLoadDriverException, SQLException, IOException
	{	
		classe = GlobalNames.compounds;
		genericLoadFile(file);
		return getReport();
	}
	
	public IResourceUpdateReport loadEnzymes(File file) throws DatabaseLoadDriverException, SQLException, IOException
	{	
		classe = GlobalNames.enzymes;
		genericLoadFile(file);
		return getReport();
	}
	
	public IResourceUpdateReport loadGenes(File file) throws DatabaseLoadDriverException, SQLException, IOException
	{	
		classe = GlobalNames.gene;
		genericLoadFile(file);
		return getReport();
	}
	
	public IResourceUpdateReport loadPathways(File file) throws DatabaseLoadDriverException, SQLException, IOException
	{	
		classe = GlobalNames.pathways;
		genericLoadFile(file);
		return getReport();
	}
	
	public IResourceUpdateReport loadProteins(File file) throws DatabaseLoadDriverException, SQLException, IOException
	{	
		classe = GlobalNames.protein;
		genericLoadFile(file);	
		return getReport();
	}
	
	public IResourceUpdateReport loadReactions(File file) throws DatabaseLoadDriverException, SQLException, IOException
	{	
		classe = GlobalNames.enzymes;
		genericLoadFile(file);	
		return getReport();
	}
	
	public boolean cheackFiles(File file,String type)
	{
		if(!file.isFile())
		{
			return false;
		}
		getReport().updateFile(file);
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
	
	public IResourceUpdateReport genericLoadFile(File file) throws DatabaseLoadDriverException, SQLException, IOException
	{
		getReport().updateFile(file);
		setNextResourceElementID(HelpDatabase.getNextInsertTableID(GlobalTablesName.resource_elements));	
		getResource().addResourceContent(classe);
		getReport().addClassesAdding(1);
		String line = new String();
		String term = new String();
		Set<String> termSynomns = new HashSet<String>();
		FileReader fr;
		BufferedReader br;
		fr = new FileReader(file);
		br = new BufferedReader(fr);
		List<IExternalID> externalIDs = new ArrayList<IExternalID>();
		fr = new FileReader(file);
		br = new BufferedReader(fr);
		int lineNumber=0;	
		int totalLines = FileHandling.getFileLines(file);
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();		
		long nowTime;
		while((line = br.readLine())!=null)
		{	
			if(line.startsWith("//"))
			{
				addTermsAndSyn(term,classe,termSynomns,externalIDs);
				termSynomns = new HashSet<String>();
				externalIDs = new ArrayList<IExternalID>();
				term=new String();

			}
			else if(line.startsWith("COMMON-NAME"))
			{
				term = findTerm(line);
			}
			else if(line.startsWith("SYNONYMS"))
			{
				updateSynonyms(line, termSynomns);

			}
			else if(line.startsWith("UNIQUE-ID"))
			{	
				Matcher m = getExternalID().matcher(line);
				if(m.find())
				{
					IExternalID e = new ExternalID(m.group(1), GlobalSources.biocyc, -1);
					externalIDs.add(e);
				}
			}
			else if(line.startsWith("DBLINKS"))
			{	
				Matcher m = getExternalID2().matcher(line);
				if(m.find())
				{
					IExternalID e = new ExternalID(m.group(2),m.group(1), -1);
					externalIDs.add(e);
				}
			}
			if((lineNumber%500)==0)
			{
				memoryAndProgress(lineNumber, totalLines);
			}
			lineNumber++;
		}
		long end = GregorianCalendar.getInstance().getTimeInMillis();
		nowTime = end-startTime;
		getReport().setTime(nowTime);
		return getReport();

	}

	protected void updateSynonyms(String line, Set<String> termSynomns) {
		String synonym;
		Matcher m = getSyn().matcher(line);
		m.find();
		synonym = m.group(1).replaceAll(cleanTag, "");
		termSynomns.add(synonym);
	}

	protected String findTerm(String line) {
		String term;
		Matcher m = getName().matcher(line);
		m.find();
		term = m.group(1).replaceAll(cleanTag, "");
		return term;
	}

	public Pattern getExternalID() {
		return externalID;
	}

	public Pattern getName() {
		return name;
	}

	public Pattern getSyn() {
		return syn;
	}

	public IDictionary getDictionary() {
		return getResource();
	}
	
	public void setcancel(boolean arg0) {
		
	}
	
	public Pattern getExternalID2() {
		return externalID2;
	}
	
	

}
