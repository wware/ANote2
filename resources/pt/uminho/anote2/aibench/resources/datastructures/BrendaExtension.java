package pt.uminho.anote2.aibench.resources.datastructures;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;

import pt.uminho.anote2.aibench.utils.operations.TimeLeftProgress;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.Brenda;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class BrendaExtension extends Brenda{


	private boolean cancel;
	private TimeLeftProgress progress;	
	
	public BrendaExtension(IDictionary dictionary,boolean cancel,TimeLeftProgress progress) {
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
		boolean isOrganism = false;
		BufferedReader br;
		long nowTime;
		
		setProteinIDInDatabase(getDictionary().addElementClass("protein"));		
		setNextResourceElementID(HelpDatabase.getNextInsertTableID(getDictionary().getDb(),"resource_elements"));
		getDictionary().addResourceContent("protein");
		String externalID = new String();
		String organism="";
		if(prop.containsKey("organism"))
		{
			organism = prop.getProperty("organism");
		}	
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			
			int total=0;
			
			while((line = br.readLine())!=null)
			{
				if(line.startsWith("///"))
				{
					total++;
				}
			}
			
			long startTime = GregorianCalendar.getInstance().getTimeInMillis();	
			
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			int lineNumber=0;

			while((line = br.readLine())!=null&&(!cancel))
			{	
				if(line.startsWith("///"))
				{
					if(isOrganism)
					{
						addTermsAndSyn(term, termSynomns,externalID);
					}
					termSynomns = new HashSet<String>();
					term=new String();
					externalID=new String();
					isOrganism = false;
					if((lineNumber%500)==0)
					{
						nowTime = GregorianCalendar.getInstance().getTimeInMillis();
						progress.setProgress((float) lineNumber/ (float) total);
						progress.setTime(nowTime-startTime, lineNumber, total);
					}
					lineNumber++;
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
					Matcher m1 = getExternalID().matcher(line);
					m1.find();
					externalID=m1.group(1);
				}	

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		
	}

}
