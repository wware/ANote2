package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.entrezgene;

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

import pt.uminho.anote2.core.annotation.IExternalID;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import pt.uminho.anote2.datastructures.annotation.ExternalID;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.DictionaryLoaderHelp;
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
 * 
 * 
 * @author Hugo Costa
 *
 * @version 1.0 
 * 
 * @since August 27th 2012
 * 
 * 
 */
public class EntrezGene extends DictionaryLoaderHelp implements IDicionaryFlatFilesLoader{
		
	private String firstLine = "#Format: tax_id GeneID Symbol LocusTag Synonyms dbXrefs";
	private String defaultTypeValue = "NEWENTRY";
	private CSVFileConfigurations csvConfigurations;
	private int columnTAXIDNumber = 0;
	private int columnIDNumber = 1;
	private int columnTermNumber = 2;
	private int columnSynonymNumber = 4;
	private int columnOtherExternalID = 5;
	private int columnOtherSynonymsNumber = 13;

	private String columnTaxtIDName = "tax_id";
	private String columnIDName = "GeneID";
	private String columntermName = "Symbol";
	private String columntermSynonym = "Synonyms";
	private String columnTermExternalDatabaseIDs = "dbXrefs";
	private String columnOtherDesignations = "Other_designations";

	
	public EntrezGene(IDictionary dicionary)
	{
		super(dicionary,GlobalSources.entrezgene);
		initCSVConfigurations();
	}
	
	private void initCSVConfigurations() {
		TextDelimiter textdelimiter = TextDelimiter.NONE;
		Delimiter generalDelimiter = Delimiter.TAB;
		Map<String, ColumnParameters> columnNameColumnParameters = new HashMap<String, ColumnParameters>();
		ColumnParameters columnTaxID = new ColumnParameters(columnTAXIDNumber,Delimiter.USER, DefaultDelimiterValue.HYPHEN);
		ColumnParameters columnID = new ColumnParameters(columnIDNumber,Delimiter.USER, DefaultDelimiterValue.HYPHEN);
		ColumnParameters columnTErm = new ColumnParameters(columnTermNumber,Delimiter.USER, DefaultDelimiterValue.USER);
		columnTErm.getDefaultValue().setUserDelimiter(defaultTypeValue);
		ColumnParameters columnSynonym = new ColumnParameters(columnSynonymNumber,Delimiter.VERTICAL_BAR, DefaultDelimiterValue.USER);
		ColumnParameters columnOtherSynonyms = new ColumnParameters(this.columnOtherSynonymsNumber,Delimiter.VERTICAL_BAR, DefaultDelimiterValue.USER);
		ColumnParameters columnExternalIds = new ColumnParameters(columnOtherExternalID,Delimiter.VERTICAL_BAR, DefaultDelimiterValue.HYPHEN,Delimiter.COLON);
		columnNameColumnParameters.put(columnTaxtIDName , columnTaxID);
		columnNameColumnParameters.put(columnIDName, columnID);
		columnNameColumnParameters.put(columntermName, columnTErm);
		columnNameColumnParameters.put(columntermSynonym, columnSynonym);	
		columnNameColumnParameters.put(columnTermExternalDatabaseIDs, columnExternalIds);
		columnNameColumnParameters.put(columnOtherDesignations, columnOtherSynonyms);
		ColumnDelemiterDefaultValue columsDelemiterDefaultValue = new ColumnDelemiterDefaultValue(columnNameColumnParameters);
		csvConfigurations = new CSVFileConfigurations(generalDelimiter, textdelimiter ,DefaultDelimiterValue.HYPHEN,columsDelemiterDefaultValue,true);
	}
	
	public boolean checkFile(File file) {
		if(!file.isFile())
		{
			return false;
		}
		try {
		getReport().updateFile(file);
		BufferedReader br;
		String line;
		FileReader fr;

			fr = new FileReader(file);
			br = new BufferedReader(fr);
			line = br.readLine();
			if(line.startsWith(firstLine))
			{
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public IResourceUpdateReport loadTermsFromFile(File file, Properties prop) throws DatabaseLoadDriverException, SQLException, IOException
	{
		getReport().updateFile(file);
		setNextResourceElementID(HelpDatabase.getNextInsertTableID(GlobalTablesName.resource_elements));	
		getResource().addResourceContent(GlobalNames.gene);
		BufferedReader br;
		String line;
		long nowTime;
		int total=0;
		FileReader fr;
		String organismId = new String();
		boolean hyphotetical = false;
		if(prop.containsKey(GlobalNames.organimsOption))
		{
			organismId = prop.getProperty(GlobalNames.organimsOption);
		}
		if(prop.containsKey(GlobalNames.entrezGeneHyphoteticalProtein) && Boolean.valueOf(prop.get(GlobalNames.entrezGeneHyphoteticalProtein).toString()))
		{
			hyphotetical = true;
		}
		total = getLines(file);
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();			
		int lineNumber = 0;	
		fr = new FileReader(file);
		br = new BufferedReader(fr);
		// Read First Line
		br.readLine();
		while((line = br.readLine())!=null)
		{
			if(!hyphotetical||!line.contains("hypothetical protein"))
			{
				processLine(line,organismId);
			}
			if((lineNumber%500)==0)
			{
				memoryAndProgress(lineNumber, total);
			}
			lineNumber++;			
		}
		long end = GregorianCalendar.getInstance().getTimeInMillis();
		nowTime = end-startTime;
		getReport().setTime(nowTime);
		return getReport();
	}
	
	protected void processLine(String line, String organismId) throws DatabaseLoadDriverException, SQLException {
		String[] columns = line.split(csvConfigurations.getGeneralDelimiter().getValue());
		String taxomomyID = columns[csvConfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(columnTaxtIDName).getColumnNumber()];
		String geneid = columns[csvConfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(columnIDName).getColumnNumber()];
		String term = columns[csvConfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(columntermName).getColumnNumber()];
		String syns = columns[csvConfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(columntermSynonym).getColumnNumber()];
		String otherSynonym = columns[csvConfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(columnOtherDesignations).getColumnNumber()];
		String externalIDs = columns[csvConfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(columnTermExternalDatabaseIDs).getColumnNumber()];
		if(organismId.equals("") || taxomomyID.equals(organismId))
		{
			if(!term.equals(defaultTypeValue))
			{
				Set<String> termSynomns = parseSynonyms(syns);
				Set<String> otherSynonyms = parseOtherSynonyms(otherSynonym);
				termSynomns.addAll(otherSynonyms);
				List<IExternalID> externalDs = parseExternalIDs(externalIDs);
				externalDs.add(new ExternalID(geneid, GlobalSources.entrezgene,-1));
				addTermsAndSyn(term,GlobalNames.gene,termSynomns,externalDs);
			}
		}

	}
	
	private List<IExternalID> parseExternalIDs(String externalIDs) {
		String database;
		String databaseID;
		List<IExternalID> externalDs = new ArrayList<IExternalID>();
		if(!externalIDs.equals(csvConfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(columnTermExternalDatabaseIDs).getDefaultValue().getValue()))
		{
			String[] ids = externalIDs.split(csvConfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(columnTermExternalDatabaseIDs).getDelimiter().getValue());
			
			for(String sourceandID:ids)
			{
				String[] sourceNameExtID = sourceandID.split(csvConfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(columnTermExternalDatabaseIDs).getSubDelimiter().getValue());
				database = sourceNameExtID[0];
				databaseID = sourceNameExtID[1];
				externalDs.add(new ExternalID(databaseID, database, -1));
			}
		}
		return externalDs;
	}

	private Set<String> parseSynonyms(String syns) {
		Set<String> result = new HashSet<String>();
		if(!syns.equals(csvConfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(columntermSynonym).getDefaultValue().getValue()))
		{
			String[] columns = syns.split(csvConfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(columntermSynonym).getDelimiter().getValue());
			for(String sy:columns)
			{
				result.add(sy);
			}
		}
		return result;
	}

	private Set<String> parseOtherSynonyms(String syns) {
		Set<String> result = new HashSet<String>();
		if(!syns.equals(csvConfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(columnOtherDesignations).getDefaultValue().getValue()))
		{
			String[] columns = syns.split(csvConfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(columnOtherDesignations).getDelimiter().getValue());
			for(String sy:columns)
			{
				result.add(sy);
			}
		}
		return result;
	}
	
	public void setcancel(boolean arg0) {
	}

	@Override
	public IDictionary getDictionary() {
		// TODO Auto-generated method stub
		return getResource();
	}

	protected int getLines(File file) throws IOException
	{
		int total=0;
		FileReader fr;
		fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		total=0;
		while((br.readLine())!=null)
		{	
			total++;
		}

		return total;
	}
}
