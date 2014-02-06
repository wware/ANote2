package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.kegg;

import java.io.BufferedReader;
import java.io.File;
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

public class KeggReactions extends Kegg implements IDicionaryFlatFilesLoader{
	
	private Pattern name = Pattern.compile("NAME\\s+(.*)?");

	public Pattern getName() {
		return name;
	}

	public KeggReactions(IDictionary dictionary) {
		super(dictionary);
	}
	
	public boolean checkFile(File file) {
		return checkAllFile(file, "Reaction");
	}

	public IResourceUpdateReport loadTermsFromFile(File file, Properties prop) throws DatabaseLoadDriverException, SQLException, IOException {
		this.loadFileReactions(file);
		return getReport();
	}
	
	protected void loadFileReactions(File file) throws DatabaseLoadDriverException, SQLException, IOException
	{
		classe = GlobalNames.reactions;
		genericLoadFile(file);
	}
	
	private void genericLoadFile(File file) throws DatabaseLoadDriverException, SQLException, IOException
	{
		setNextResourceElementID(HelpDatabase.getNextInsertTableID(GlobalTablesName.resource_elements));	
		getDictionary().addResourceContent(classe);
		String line = new String();
		String term = new String();
		Set<String> termSynomns = new HashSet<String>();
		FileReader fr;
		BufferedReader br;
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
				term=new String();
				externalID = new String();
			}
			else if(line.startsWith("NAME "))
			{
				Matcher m = name.matcher(line);
				if(m.find())
				{
					String nameReaction = m.group(1);
					if(nameReaction!=null)
					{
						term=nameReaction;
					}
				}			
			}
			else if(line.startsWith("ENTRY"))
			{
				Matcher m = getExternalID().matcher(line);
				if(m.find())
				{
					externalID = m.group(1);
					String[] externals=externalID.split("  ");
					externalID = externals[0];
				}
			}
		}

	}
	
	public void setcancel(boolean arg0) {
		
	}


}

