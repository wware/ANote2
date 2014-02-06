package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.kegg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
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

	public void loadTermsFromFile(File file, Properties prop) {
		this.loadFileReactions(file);
	}
	
	public void loadFileReactions(File file)
	{
		genericLoadFile(file,"","reaction");
	}
	
	private void genericLoadFile(File file,String organism,String biologicalClass)
	{

		String line = new String();
		String term = new String();
		Set<String> termSynomns = new HashSet<String>();
		FileReader fr;
		BufferedReader br;

		setClassIDInDatabase(getDictionary().addElementClass(biologicalClass));		
		setNextResourceElementID(HelpDatabase.getNextInsertTableID(getDictionary().getDb(),"resource_elements"));	
		getDictionary().addResourceContent(biologicalClass);
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String externalID = new String();
			while((line = br.readLine())!=null)
			{	
				if(line.startsWith("///"))
				{
					addTermsAndSyn(term, termSynomns,externalID);
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void setcancel(boolean arg0) {
		
	}


}

