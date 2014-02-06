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
import pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.kegg.KeggEnzymes;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class KeggEnzymesExtension extends KeggEnzymes{
	
	private boolean cancel;
	private TimeLeftProgress progress;	
	
	public KeggEnzymesExtension(IDictionary dictionary,boolean cancel,TimeLeftProgress progress) {
		super(dictionary);
		this.cancel=cancel;
		this.progress=progress;	
	}
	
	
	public void setcancel(boolean cancel) {
		this.cancel = cancel;
	}

	public void loadTermsFromFile(File file, Properties prop) {
		String organism = "";
		if(prop.containsKey("organism"))
		{
			organism=prop.getProperty("organism");
		}
		genericLoadFile(file,organism,"enzyme");
	}
	private void genericLoadFile(File file,String organism,String biologicalClass)
	{

		String line = new String();
		String term = new String();
		Set<String> termSynomns = new HashSet<String>();
		FileReader fr;
		boolean isOrganism = false;
		boolean inorganism = false;
		long nowTime,startTime;
		BufferedReader br;
		int total=0;
		
		setClassIDInDatabase(getDictionary().addElementClass(biologicalClass));		
		setNextResourceElementID(HelpDatabase.getNextInsertTableID(getDictionary().getDb(),"resource_elements"));	
		getDictionary().addResourceContent(biologicalClass);
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
			String externalID = new String();
			int lineNumber=0;
			startTime = GregorianCalendar.getInstance().getTimeInMillis();
			while((line = br.readLine())!=null&&(!cancel))
			{	
				if(line.startsWith("///"))
				{
					if(isOrganism||inorganism==false)
					{
						addTermsAndSyn(term, termSynomns,externalID);
					}
					termSynomns = new HashSet<String>();
					term=new String();
					isOrganism = false;
					inorganism = false;
					externalID = new String();
					if((lineNumber%500)==0)
					{
						nowTime = GregorianCalendar.getInstance().getTimeInMillis();
						progress.setProgress((float) lineNumber/ (float) total);
						progress.setTime(nowTime-startTime, lineNumber, total);
					}
					lineNumber++;
					
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

