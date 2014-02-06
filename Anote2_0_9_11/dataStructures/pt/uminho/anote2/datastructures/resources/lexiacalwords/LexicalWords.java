package pt.uminho.anote2.datastructures.resources.lexiacalwords;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.ConflitsType;
import pt.uminho.anote2.core.report.resources.IInsertConflits;
import pt.uminho.anote2.core.report.resources.IResourceMergeReport;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.database.schema.TableResourcesElements;
import pt.uminho.anote2.datastructures.report.resources.ResourceMergeConflits;
import pt.uminho.anote2.datastructures.report.resources.ResourceMergeReport;
import pt.uminho.anote2.datastructures.report.resources.ResourceUpdateReport;
import pt.uminho.anote2.datastructures.resources.IResourceImportFromCSVFiles;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.resources.ResourcesHelp;
import pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.csv.ColumnNames;
import pt.uminho.anote2.datastructures.utils.FileHandling;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.datastructures.utils.generic.CSVFileConfigurations;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;

public class LexicalWords  extends ResourcesHelp implements ILexicalWords,IResourceImportFromCSVFiles{
	

	private Set<String> lexicalWords;
	private Map<String,Integer> lexicalWordDatabaseID;
	private BufferedReader br;
	
	public LexicalWords(int id, String name, String info) {
		super(id, name, info);
		lexicalWords = null;
		lexicalWordDatabaseID = null;
	}
	
	public LexicalWords(int id, String name, String info,boolean active) {
		super(id, name, info,active);
		lexicalWords = null;
		lexicalWordDatabaseID = null;
	}


	public String getType() {
		return GlobalOptions.resourcesLexicalWords;
	}
	
	public void setLexicalWords(Set<String> lexicalWords) {
		this.lexicalWords = lexicalWords;
	}


	public IResourceUpdateReport loadTermFromGenericCVSFile(File file,CSVFileConfigurations csvfileconfigurations) throws DatabaseLoadDriverException, SQLException, IOException {
		IResourceUpdateReport report = new ResourceUpdateReport(GlobalNames.updateLexicalWordsReportTitle, this, file,"");
		int nextElement = (HelpDatabase.getNextInsertTableID(GlobalTablesName.resource_elements));	
		String line;
		if(file==null || !file.exists())
		{
			report.changePerformedStatus(false);
			return report;
		}
		else
		{
			if(lexicalWords==null)
			{
				lexicalWords = new HashSet<String>();
				lexicalWordDatabaseID = new HashMap<String, Integer>();
			}
			long starttime = GregorianCalendar.getInstance().getTimeInMillis();
			loadAllTermsFromDatabaseToMemory();
			FileReader fr;
			int getLines = FileHandling.getFileLines(file);
			int step = 0;
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			// remove first lime if if has headers
			if(csvfileconfigurations.isHasHeaders())
			{
				br.readLine();
			}
			while((line = br.readLine())!=null)
			{
				nextElement = processLine(csvfileconfigurations, report,nextElement, line);
				if(step%100==0)
				{
					memoryAndProgress(step, getLines);
				}
				step++;
			}
			long endtime = GregorianCalendar.getInstance().getTimeInMillis();
			report.setTime(endtime-starttime);
			deleteTermsInMemory();
			return report;
		}
	}

	private int processLine(CSVFileConfigurations csvfileconfigurations,
			IResourceUpdateReport report, int nextElement, String line) {
		String[] lin = line.split(csvfileconfigurations.getGeneralDelimiter().getValue());
		if(lin.length>0)
		{
			String term = lin[csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(ColumnNames.term).getColumnNumber()];	
			term = term.replace(csvfileconfigurations.getTextDelimiter().getValue(),"");
			if(term.length()>TableResourcesElements.mimimumElementSize && term.length()<TableResourcesElements.elementSize)
			{
				nextElement = updateTerm(report, nextElement, term);
			}
		}
		return nextElement;
	}

	private int updateTerm(IResourceUpdateReport report, int nextElement,
			String term) {
		IResourceElement elem = new ResourceElement(term);
		if(!lexicalWords.contains(elem.getTerm()) && this.addElement(elem))
		{
			lexicalWords.add(elem.getTerm());
			report.addTermAdding(1);
			lexicalWordDatabaseID.put(elem.getTerm(), nextElement);
			nextElement++;
		}
		else
		{
			IInsertConflits conflit = new ResourceMergeConflits(ConflitsType.AlreadyHaveTerm, elem, elem);
			report.addConflit(conflit);
		}
		return nextElement;
	}

	public boolean exportCSVFile(String csvFile) throws IOException, SQLException, DatabaseLoadDriverException {
		String entity;
		File file = new File(csvFile);
		if(!file.exists())
			file.createNewFile();
		PreparedStatement termInMemory = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.selectResourceElement);
		termInMemory.setInt(1,getID());
		ResultSet rs = termInMemory.executeQuery();
		PrintWriter pw = new PrintWriter(file);	
		String line = new String();
		while(rs.next())
		{
			entity=rs.getString(1);
			line = line + entity+"\n";
		}
		pw.write(line);
		pw.close();
		rs.close();		
		rs.close();
		termInMemory.close();	
		return true;
	}

	public Set<String> getLexicalWords() throws SQLException, DatabaseLoadDriverException {
		{
			getLexicalWordsAux();
		}
		return lexicalWords;
	}

	private synchronized void getLexicalWordsAux() throws SQLException, DatabaseLoadDriverException {
		if(lexicalWords==null)
		{
			String entity;
			Set<String> words = new HashSet<String>();
			HashMap<String, Integer> lexicalWordDatabaseID = new HashMap<String, Integer>();
			{
				PreparedStatement termInMemory = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.selectResourceElement);
				termInMemory.setInt(1,getID());
				ResultSet rs = termInMemory.executeQuery();
				while(rs.next())
				{
					entity=rs.getString(1);
					words.add(entity);
					lexicalWordDatabaseID.put(entity, rs.getInt(2));
				}
				rs.close();	
				termInMemory.close();

			}
			this.lexicalWords = words;
			this.lexicalWordDatabaseID = lexicalWordDatabaseID;
		}
	}


	public IResourceMergeReport merge(ILexicalWords idSncDic) throws DatabaseLoadDriverException, SQLException {
		int nextElement = (HelpDatabase.getNextInsertTableID(GlobalTablesName.resource_elements));	
		IResourceMergeReport report = new ResourceMergeReport(GlobalNames.mergeLexicalWordsReportTitle, idSncDic, this);
		{
			loadAllTermsFromDatabaseToMemory();	
			long starttime = GregorianCalendar.getInstance().getTimeInMillis();
			if(lexicalWords==null)
			{
				lexicalWords = new HashSet<String>();
				lexicalWordDatabaseID = new HashMap<String, Integer>();
			}
			Set<String> terms = idSncDic.getLexicalWords();	
			int getLines = terms.size();
			int step = 0;
			for(String elem:terms)
			{
				nextElement = updateElement(nextElement, report, elem);
				if(step%100==0)
				{
					memoryAndProgress(step, getLines);
				}
				step++;
			}
			deleteTermsInMemory();
			long endtime = GregorianCalendar.getInstance().getTimeInMillis();
			report.setTime(endtime-starttime);
			return report;
		}
	}

	private int updateElement(int nextElement, IResourceMergeReport report,
			String elem) throws SQLException {
		if(this.addElement(new ResourceElement(elem)))
		{
			lexicalWords.add(elem);
			report.addTermAdding(1);
			lexicalWordDatabaseID.put(elem,nextElement);
			nextElement++;
		}
		else
		{
			IResourceElement elemValue = getFirstTermByName(elem);
			IInsertConflits conflit = new ResourceMergeConflits(ConflitsType.AlreadyHaveTerm, elemValue, new ResourceElement(elem)) ;
			report.addConflit(conflit);
		}
		return nextElement;
	}
	
	public String toString()
	{
		if(getID() < 1)
		{
			return "None";
		}
		String info = "Lexical Words : " + getName() + "( ID :"+ getID() + ") ";
		if(!getInfo().equals(""))
		{
			info = info + "Notes: "+getInfo();
		}
		if(!isActive())
		{
			info = info + " (Inactive) ";
		}
		return  info;
	}

	@Override
	public int getLexicalWordID(String lexicalWord) throws SQLException, DatabaseLoadDriverException {
		Set<String> words = getLexicalWords();
		if(words.contains(lexicalWord))
		{
			return lexicalWordDatabaseID.get(lexicalWord);
		}
		return -1;
	}
	
	public Map<String, Integer> getLexicalWordDatabaseID() {
		return lexicalWordDatabaseID;
	}

	public boolean equals(Object lexicalWordsw)
	{
		if(!(lexicalWordsw instanceof ILexicalWords))
			return false;
		if(this.getID() == ((ILexicalWords) lexicalWordsw).getID())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}
