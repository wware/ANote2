package pt.uminho.anote2.aibench.resources.datastructures.biocyc;

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
import pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.ByoCyc.BioCycGene;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class BioCycGeneExtension extends BioCycGene{
	
	private boolean cancel;
	private TimeLeftProgress progress;	
	
	public BioCycGeneExtension(IDictionary dictionary,boolean cancel,TimeLeftProgress progress) {
		super(dictionary);
		this.cancel=cancel;
		this.progress=progress;	
	}
	
	public boolean isCancel() {
		return cancel;
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


		setGenericIDInDatabase(getDictionary().addElementClass("gene"));		
		setNextResourceElementID(HelpDatabase.getNextInsertTableID(getDictionary().getDb(),"resource_elements"));	
		getDictionary().addResourceContent("gene");
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			
			int total=0;
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			while((line = br.readLine())!=null)
			{
				if(line.startsWith("//"))
				{
					total++;
				}
			}
			String externalID = new String();
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			int lineNumber=0;
			
			long startTime = GregorianCalendar.getInstance().getTimeInMillis();	
			
			while((line = br.readLine())!=null&&(!cancel))
			{	
				if(line.startsWith("//"))
				{

					addTermsAndSyn(term, termSynomns,externalID);
					termSynomns = new HashSet<String>();
					term=new String();
					if((lineNumber%500)==0)
					{
						long nowTime = GregorianCalendar.getInstance().getTimeInMillis();
						progress.setProgress((float) lineNumber/ (float) total);
						progress.setTime(nowTime-startTime, lineNumber, total);
					}
					lineNumber++;
				}
				else if(line.startsWith("COMMON-NAME"))
				{
					Matcher m = getName().matcher(line);
					m.find();
					term = m.group(1);
				}
				else if(line.startsWith("SYNONYMS"))
				{
					Matcher m = getSyn().matcher(line);
					m.find();
					termSynomns.add(m.group(1));
				}
				else if(line.startsWith("UNIQUE-ID"))
				{	
					Matcher m = getExternalID().matcher(line);
					if(m.find())
					{
						externalID = m.group(1);
					}
				}
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
