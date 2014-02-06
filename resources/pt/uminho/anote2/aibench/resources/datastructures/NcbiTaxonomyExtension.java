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
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.NcbiTaxonomy;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class NcbiTaxonomyExtension extends NcbiTaxonomy{
	
	private boolean cancel;
	private TimeLeftProgress progress;	

	public NcbiTaxonomyExtension(IDictionary dicionary,boolean cancel,TimeLeftProgress progress) {
		super(dicionary);
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
		Matcher m1,m2;
		FileReader fr;
		BufferedReader br;
		long nowTime;

		setOrganismIDInDatabase(getDictionary().addElementClass("organism"));		
		setNextResourceElementID(HelpDatabase.getNextInsertTableID(getDictionary().getDb(),"resource_elements"));	
		getDictionary().addResourceContent("organism");
		try {
			fr = new FileReader(file);

			br = new BufferedReader(fr);
			// the first two lines are irrelevant
			line = br.readLine();
			line = br.readLine();
			String sentenceID="1";
			int total = 0;
			while((line = br.readLine())!=null)
			{	
				total++;
			}
			
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			
			long startTime = GregorianCalendar.getInstance().getTimeInMillis();	
			
			int lines=0;
			while((line = br.readLine())!=null&&(!cancel))
			{	

				if(!line.startsWith(sentenceID))
				{
					addTermsAndSyn(term, termSynomns,sentenceID);
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
					nowTime = GregorianCalendar.getInstance().getTimeInMillis();
					progress.setProgress((float) lines/ (float) total);
					progress.setTime(nowTime-startTime, lines, total);
				}
				lines++;
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}	

}
