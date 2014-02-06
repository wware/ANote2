package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.kegg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.uminho.anote2.core.annotation.IExternalID;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.annotation.ExternalID;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.DictionaryLoaderHelp;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalSources;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class Kegg extends DictionaryLoaderHelp{
	
	private Pattern findEntity = Pattern.compile("\\s+(.*)$");
	private Pattern externalID = Pattern.compile("ENTRY\\s+(.+)\\s+?");
	protected String classe;
	
	public Kegg(IDictionary dictionary)
	{
		super(dictionary,GlobalSources.kegg);
	}
	
	public void loadFileCoumpounds(File file,String organism) throws DatabaseLoadDriverException, SQLException, IOException
	{
		classe = GlobalNames.compounds;
		genericLoadFile(file,organism);
	}
	
	public void loadFileEnzymes(File file,String organism) throws DatabaseLoadDriverException, SQLException, IOException
	{
		classe = GlobalNames.enzymes;
		genericLoadFile(file,organism);
	}
	
	
	private void genericLoadFile(File file,String organism) throws DatabaseLoadDriverException, SQLException, IOException
	{
		setNextResourceElementID(HelpDatabase.getNextInsertTableID(GlobalTablesName.resource_elements));
		int total = getTotalEntries(file);
		int step = 0;
		getResource().addElementClass(classe);
		String line = new String();
		String term = new String();
		Set<String> termSynomns = new HashSet<String>();
		FileReader fr;
		boolean isOrganism = false;
		boolean inorganism = false;
		BufferedReader br;
		fr = new FileReader(file);
		br = new BufferedReader(fr);
		String externalID = new String();
		while((line = br.readLine())!=null)
		{	
			if(line.startsWith("///"))
			{
				if(isOrganism||inorganism==false)
				{
					List<IExternalID> externalIDs = new ArrayList<IExternalID>();
					externalIDs.add(new ExternalID(externalID,GlobalSources.kegg ,-1));
					addTermsAndSyn(term,classe,termSynomns,externalIDs);
				}
				termSynomns = new HashSet<String>();
				term=new String();
				isOrganism = false;
				inorganism = false;
				externalID = new String();
				if(step % 100 == 0)
				{
					memoryAndProgress(step, total);
				}
				step++;
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
	}
	
	
	protected int getTotalEntries(File file) throws IOException {
		BufferedReader br;
		FileReader fr = new FileReader(file);
		br = new BufferedReader(fr);
		String line;
		int result = 0;
		while((line = br.readLine())!=null)
		{
			if(line.startsWith("///"))
			{
				result++;
			}
		}
		return result;

	}

	public boolean checkAllFile(File file,String type) {
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
			return false;
		}		
		return false;
	}

	
	public Pattern getExternalID() {
		return externalID;
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
	
	
	public Pattern getFindEntity() {
		return findEntity;
	}

	public IDictionary getDictionary() {
		return getResource();
	}

}
