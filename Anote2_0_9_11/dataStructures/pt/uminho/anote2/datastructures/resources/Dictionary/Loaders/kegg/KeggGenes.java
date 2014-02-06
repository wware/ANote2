package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.kegg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalSources;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.resource.dictionary.IDicionaryFlatFilesLoader;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class KeggGenes extends Kegg implements IDicionaryFlatFilesLoader{

	private Pattern ntseq = Pattern.compile("NTSEQ");
	
	public KeggGenes(IDictionary dictionary) {
		super(dictionary);
	}

	public boolean checkFile(File file) {
		
		FileReader fr;
		BufferedReader br;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line;
			int i=0;
			while((line = br.readLine())!=null&&i<100)
			{
				Matcher m = ntseq.matcher(line);
				if(m.find())
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

	
	public IResourceUpdateReport loadTermsFromFile(File file, Properties prop) throws DatabaseLoadDriverException, SQLException, IOException {
		loadFileGenes(file);
		return getReport();
	}
	
	public void loadFileGenes(File file) throws DatabaseLoadDriverException, SQLException, IOException
	{
		classe = GlobalNames.gene;
		loadGenericWithoutOrgnanism(file);
	}
	
	private void loadGenericWithoutOrgnanism(File file) throws DatabaseLoadDriverException, SQLException, IOException
	{
		String line = new String();
		String term = new String();
		int total = getTotalEntries(file);
		int step = 0;
		Set<String> termSynomns = new HashSet<String>();
		FileReader fr;
		BufferedReader br;
		setNextResourceElementID(HelpDatabase.getNextInsertTableID(GlobalTablesName.resource_elements));	
		getDictionary().addResourceContent(classe);
		fr = new FileReader(file);
		br = new BufferedReader(fr);
		String externalID = new String();
		
		while((line = br.readLine())!=null)
		{	
			if(line.startsWith("///"))
			{
				List<IExternalID> externalIDs = new ArrayList<IExternalID>();
				externalIDs.add(new ExternalID(externalID,GlobalSources.kegg ,-1));
				addTermsAndSyn(term,classe, termSynomns,externalIDs);
				termSynomns = new HashSet<String>();
				term=new String();
				externalID=new String();
				if(step % 100 == 0)
				{
					memoryAndProgress(step, total);
				}
				step++;
			}
			else if(line.startsWith("NAME "))
			{
				term = finTerm(line,termSynomns);
			}
			else if(line.startsWith("ENTRY"))
			{
				Matcher m = getExternalID().matcher(line);
				m.find();
				externalID = m.group(1);
				String[] externals=externalID.split("  ");
				externalID = externals[0];
			}
		}	
	}
	
	public String finTerm(String line,Set<String> syn) {
		String term;
		Matcher m1;
		m1 = getFindEntity().matcher(line);
		m1.find();

		term=m1.group(1);
		String[] terms = term.split(", ");
		for(String termAux:terms)
		{
			syn.add(termAux);
		}
		term=terms[0];
		return term;
	}
	
	public void setcancel(boolean arg0) {
		
	}

}
