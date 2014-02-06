package pt.uminho.anote2.aibench.resources.datastructures;

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
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.MySQLDatabase;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.UniProt;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class UniprotExtension extends UniProt{
	
	private boolean cancel;
	private TimeLeftProgress progress;	

	public UniprotExtension(IDictionary dicionary,boolean cancel,TimeLeftProgress progress) {
		super(dicionary);
		this.cancel=cancel;
		this.progress=progress;
	}
	
	public void setcancel(boolean cancel) {
		this.cancel = cancel;
	}
	
	public void loadTermsFromFile(File file,Properties prop)
	{

		String line = new String();
		String term = new String();
		Set<String> termSynomns = new HashSet<String>();
		FileReader fr;
		BufferedReader br;
		boolean isOrganism = false;	
		String organism="";
		long nowTime,startTime;
		if(prop.containsKey("organism"))
		{
			organism = prop.getProperty("organism");
		}		
		int total = getNumberProteins(file);
		setProteinIDInDatabase(getDictionary().addElementClass("protein"));
		getDictionary().addResourceContent("protein");
		setNextResourceElementID(HelpDatabase.getNextInsertTableID(getDictionary().getDb(),"resource_elements"));
		int lineNumber=0;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String externalID = new String();
			startTime = GregorianCalendar.getInstance().getTimeInMillis();
			while((line = br.readLine())!=null&&(!cancel))
			{	
				if(line.startsWith("//"))
				{
					if(isOrganism)
					{
						addTermsAndSyn(term, termSynomns,externalID);
					}
					termSynomns = new HashSet<String>();
					term=new String();
					externalID=new String();
					isOrganism = true;
					if((lineNumber%500)==0)
					{
						nowTime = GregorianCalendar.getInstance().getTimeInMillis();
						progress.setProgress((float) lineNumber/ (float) total);
						progress.setTime(nowTime-startTime, lineNumber, total);			
					}	
					lineNumber++;	
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
						externalID=m.group(1);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected int getNumberProteins(File file)
	{
		FileReader fr;
		BufferedReader br;
		int total=0;
		progress.setTimeString("Time Left: Calculating...");
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line = new String();;
			while((line = br.readLine())!=null&&cancel==false)
			{	
				if(line.startsWith("//"))
				{
					total++;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			return -2;
		}
		return total;
	}

}
