package pt.uminho.anote2.aibench.resources.datastructures.kegg;

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

import pt.uminho.anote2.aibench.utils.operations.TimeLeftProgress;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.kegg.KeggReactions;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class KeggReactionsExtension extends KeggReactions{
	
	private boolean cancel;
	private TimeLeftProgress progress;	
	
	public KeggReactionsExtension(IDictionary dictionary,boolean cancel,TimeLeftProgress progress) {
		super(dictionary);
		this.cancel=cancel;
		this.progress=progress;	
	}
	
	public void setcancel(boolean cancel) {
		this.cancel = cancel;
	}
	
	public void loadTermsFromFile(File file, Properties prop)
	{

		String line = new String();
		String term = new String();
		Set<String> termSynomns = new HashSet<String>();
		FileReader fr;
		BufferedReader br;
		int total=0;
		long nowTime,startTime;

		setClassIDInDatabase(getDictionary().addElementClass("reaction"));		
		setNextResourceElementID(HelpDatabase.getNextInsertTableID(getDictionary().getDb(),"resource_elements"));	
		getDictionary().addResourceContent("reaction");
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			while((line = br.readLine())!=null)
			{	
				if(line.startsWith("///"))
				{
					total++;
				}
			}
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			int lineNumber=0;
			String externalID = new String();
			startTime = GregorianCalendar.getInstance().getTimeInMillis();
			while((line = br.readLine())!=null&&(!cancel))
			{	
				if(line.startsWith("///"))
				{
					addTermsAndSyn(term, termSynomns,externalID);
					term=new String();
					externalID = new String();
					if((lineNumber%500)==0)
					{
						nowTime = GregorianCalendar.getInstance().getTimeInMillis();
						progress.setProgress((float) lineNumber/ (float) total);
						progress.setTime(nowTime-startTime, lineNumber, total);
					}
					lineNumber++;	
				}
				else if(line.startsWith("NAME "))
				{
					Matcher m = getName().matcher(line);
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

}
