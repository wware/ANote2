package pt.uminho.anote2.aibench.resources.datastructures;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.regex.Matcher;

import pt.uminho.anote2.aibench.utils.operations.TimeLeftProgress;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.MySQLDatabase;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.EntrezGene;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class EntrezGeneExtension extends EntrezGene{

	private boolean cancel;
	private TimeLeftProgress progress;	

	public EntrezGeneExtension(IDictionary dicionary,boolean cancel,TimeLeftProgress progress) {
		super(dicionary);
		this.cancel=cancel;
		this.progress=progress;
	}
	
	public void loadTermsFromFile(File file,Properties prop)
	{
		Matcher m;
		BufferedReader br;
		String line;
		setGeneIDInDatabase(getDictionary().addElementClass("gene"));
		setNexgeneElementID(HelpDatabase.getNextInsertTableID(getDictionary().getDb(), "resource_elements"));
		getDictionary().addResourceContent("gene");
		long nowTime;
		int total=0;
		FileReader fr;
		String organismId = "";
		if(prop.containsKey("organism"))
		{
			organismId = prop.getProperty("organism");
		}
		try {

			long startTime = GregorianCalendar.getInstance().getTimeInMillis();			
			int lineNumber = 0;	
			total = getLines(file);
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			while((line = br.readLine())!=null&&(!cancel))
			{
				if(!line.contains("hypothetical protein"))
				{
					m = getGeneFinder().matcher(line);
					if(m.find())
					{
						if(organismId.equals("")||m.group(1).equals(organismId))
						{
							findGene(m);
						}
					}
				}
				if((lineNumber%500)==0)
				{
					nowTime = GregorianCalendar.getInstance().getTimeInMillis();
					progress.setProgress((float) lineNumber/ (float) total);
					progress.setTime(nowTime-startTime, lineNumber, total);
				}
				lineNumber++;			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	protected int getLines(File file)
	{
		int total=0;
		FileReader fr;
		try {
			fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			total=0;
			while((br.readLine())!=null)
			{	
				total++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}

		return total;
	}
	
	
	public boolean isCancel() {
		return cancel;
	}

	public void setcancel(boolean cancel) {
		this.cancel = cancel;
	}
	
	public void setProgress(TimeLeftProgress progress) {
		this.progress = progress;
	}
	
	public TimeLeftProgress getProgress() {
		return progress;
	}
	
}
