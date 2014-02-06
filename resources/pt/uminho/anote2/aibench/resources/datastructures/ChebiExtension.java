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
import pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.Chebi;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class ChebiExtension extends Chebi{

	private boolean cancel;
	private TimeLeftProgress progress;	
	
	public ChebiExtension(IDictionary dicionary,boolean cancel,TimeLeftProgress progress) {
		super(dicionary);
		this.cancel=cancel;
		this.progress=progress;	
	}
	
	public void setcancel(boolean cancel) {
		this.cancel = cancel;
	}

	public void loadTermsFromFile(File file, Properties prop)
	{
		Matcher m;
		BufferedReader br;
		String comp;
		String term = new String();
		Set<String> syn = new HashSet<String>();
		String line;
		setNextResourceElementID(HelpDatabase.getNextInsertTableID(getDictionary().getDb(),"resource_elements"));		
		setCoupoundIDInDatabase(getDictionary().addElementClass("compound"));		
		getDictionary().addResourceContent("compound");
		FileReader fr;
		long nowTime;
		
		try {
			int total=0;
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			while((line = br.readLine())!=null)
			{
				total++;
			}

			fr = new FileReader(file);
			br = new BufferedReader(fr);
			int lineNumber=0;
			// The first line is file field description
			line = br.readLine();
			int id=1;		
			long startTime = GregorianCalendar.getInstance().getTimeInMillis();	
			
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			// The first line is file field description
			line = br.readLine();
			
			while((line = br.readLine())!=null&&(!cancel))
			{
				m = getGeneCounpound().matcher(line);
				m.find();
				int lineId = Integer.decode(m.group(2));
				if(lineId!=id)
				{
					addTermsAndSyn(term,syn,String.valueOf(id));
					term = m.group(3);
					syn=new HashSet<String>();
					id=lineId;
				}
				else
				{
					comp = m.group(3);
					syn.add(comp);
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

}
