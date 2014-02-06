package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.uminho.anote2.core.annotation.IExternalID;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import pt.uminho.anote2.datastructures.annotation.ExternalID;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.utils.FileHandling;
import pt.uminho.anote2.datastructures.utils.GenericPair;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalSources;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.datastructures.utils.generic.CSVFileConfigurations;
import pt.uminho.anote2.datastructures.utils.generic.ColumnDelemiterDefaultValue;
import pt.uminho.anote2.datastructures.utils.generic.ColumnParameters;
import pt.uminho.anote2.process.IE.io.export.DefaultDelimiterValue;
import pt.uminho.anote2.process.IE.io.export.Delimiter;
import pt.uminho.anote2.process.IE.io.export.TextDelimiter;
import pt.uminho.anote2.resource.dictionary.IDicionaryFlatFilesLoader;
import pt.uminho.anote2.resource.dictionary.IDictionary;

/**
 * @note2
 * 
 * @author Hugo
 *
 */


public class Chebi extends DictionaryLoaderHelp implements IDicionaryFlatFilesLoader{
	
	private Pattern checkFile = Pattern.compile("^ID\\s+COMPOUND_ID\\s+TYPE");
	private Map<Integer,GenericPair<String,Set<String>>> coupoundIDNameSynonyms;
	private String columnIDName = "ID";
	private String termOrSynonymName = "TermSynonym";
	private String typeName = "Type";
	private String defaultTypeValue = "NAME";
	private int columnIDNumber = 1;
	private int columnTermSynonymNumber = 4;
	private int columnTypeNumber = 2;
	private CSVFileConfigurations csvConfigurations;
	
	public Chebi(IDictionary dicionary)
	{
		super(dicionary,GlobalSources.chebi);
		this.coupoundIDNameSynonyms = new HashMap<Integer, GenericPair<String,Set<String>>>();
		initCSVConfigurations();
	}
	
	private void initCSVConfigurations() {
		TextDelimiter textdelimiter = TextDelimiter.NONE;
		Delimiter generalDelimiter = Delimiter.TAB;
		Map<String, ColumnParameters> columnNameColumnParameters = new HashMap<String, ColumnParameters>();
		ColumnParameters columnID = new ColumnParameters(columnIDNumber,Delimiter.USER, DefaultDelimiterValue.USER);
		ColumnParameters columnTErmSynonym = new ColumnParameters(columnTermSynonymNumber,Delimiter.USER, DefaultDelimiterValue.USER);
		columnTErmSynonym.getDefaultValue().setUserDelimiter(defaultTypeValue);
		ColumnParameters columnType = new ColumnParameters(columnTypeNumber,Delimiter.USER, DefaultDelimiterValue.USER);
		columnNameColumnParameters.put(columnIDName, columnID);
		columnNameColumnParameters.put(termOrSynonymName, columnTErmSynonym);
		columnNameColumnParameters.put(typeName, columnType);	
		ColumnDelemiterDefaultValue columsDelemiterDefaultValue = new ColumnDelemiterDefaultValue(columnNameColumnParameters);
		csvConfigurations = new CSVFileConfigurations(generalDelimiter, textdelimiter ,DefaultDelimiterValue.HYPHEN,columsDelemiterDefaultValue,true);
	}

	public boolean checkFile(File file) {
		if(!file.isFile())
		{
			return false;
		}
		getReport().updateFile(file);
		BufferedReader br;
		FileReader fr;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line = br.readLine();
			Matcher m = checkFile.matcher(line);
			if(m.find())
			{
				return true;
			}
			else
			{
				return false;
			}

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public IResourceUpdateReport loadTermsFromFile(File file, Properties prop) throws DatabaseLoadDriverException, SQLException, IOException
	{
		BufferedReader br;
		setNextResourceElementID(HelpDatabase.getNextInsertTableID(GlobalTablesName.resource_elements));	
		String line;
		getResource().addResourceContent(GlobalNames.compounds);
		getReport().addClassesAdding(1);
		getReport().updateFile(file);
		FileReader fr;
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		int lineNumber = 0;
		int totalLines = FileHandling.getFileLines(file);
		fr = new FileReader(file);
		br = new BufferedReader(fr);
		// The first line is file field description
		line = br.readLine();		
		while((line = br.readLine())!=null)
		{
			processLine(line);
		}
		for(Integer externalID:coupoundIDNameSynonyms.keySet())
		{
			Set<String> termSynomns = coupoundIDNameSynonyms.get(externalID).getY();
			String term = coupoundIDNameSynonyms.get(externalID).getX();
			List<IExternalID> externalIDs = new ArrayList<IExternalID>();
			externalIDs.add(new ExternalID(String.valueOf(externalID),GlobalSources.chebi ,-1));
			addTermsAndSyn(term,GlobalNames.compounds,termSynomns,externalIDs);
			if((lineNumber%500)==0)
			{
				memoryAndProgress(lineNumber, totalLines);
			}
			lineNumber++;
		}
		long endtime = GregorianCalendar.getInstance().getTimeInMillis();
		getReport().setTime(endtime-startTime);


		return getReport();
	}

	protected void processLine(String line) {
		String[] columns = line.split(csvConfigurations.getGeneralDelimiter().getValue());
		int id = Integer.parseInt(columns[csvConfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(columnIDName).getColumnNumber()]);
		String type = columns[csvConfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(typeName).getColumnNumber()];
		String value = columns[csvConfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(termOrSynonymName).getColumnNumber()];
		if(!coupoundIDNameSynonyms.containsKey(id))
		{
			GenericPair<String, Set<String>> nameSynonyms = new GenericPair<String, Set<String>>(new String(), new HashSet<String>());
			coupoundIDNameSynonyms.put(id, nameSynonyms);
		}
		if(type.equals(csvConfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(typeName).getDefaultValue().getValue()))
		{
			coupoundIDNameSynonyms.get(id).setX(value);
		}
		else
		{
			coupoundIDNameSynonyms.get(id).getY().add(value);
		}
		
	}

	

	public IDictionary getDictionary() {
		return getResource();
	}
	
	public void setcancel(boolean arg0) {
		
	}
	
	public Map<Integer, GenericPair<String, Set<String>>> getCoupoundIDNameSynonyms() {
		return coupoundIDNameSynonyms;
	}

}
