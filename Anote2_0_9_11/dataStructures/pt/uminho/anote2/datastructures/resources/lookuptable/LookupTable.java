package pt.uminho.anote2.datastructures.resources.lookuptable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.ConflitsType;
import pt.uminho.anote2.core.report.resources.IInsertConflits;
import pt.uminho.anote2.core.report.resources.IResourceMergeReport;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.report.resources.ResourceMergeConflits;
import pt.uminho.anote2.datastructures.report.resources.ResourceMergeReport;
import pt.uminho.anote2.datastructures.report.resources.ResourceUpdateReport;
import pt.uminho.anote2.datastructures.resources.IResourceImportFromCSVFiles;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.resources.ResourceElementSet;
import pt.uminho.anote2.datastructures.resources.ResourcesHelp;
import pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.csv.ColumnNames;
import pt.uminho.anote2.datastructures.utils.FileHandling;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.generic.CSVFileConfigurations;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;
import pt.uminho.anote2.resource.lookuptables.ILookupTable;

public class LookupTable extends ResourcesHelp implements ILookupTable,IResourceImportFromCSVFiles{

	private static String delimiter = "\t";
	
	public LookupTable(int id, String name, String info) {
		super( id, name, info);
	}
	
	public LookupTable(int id, String name, String info,boolean active) {
		super(id, name, info,active);
	}

	public boolean exportCSVFile(String pathFile) throws DatabaseLoadDriverException, SQLException, IOException {		
		String entity,classe;
		Connection connection = Configuration.getDatabase().getConnection();
		File file = new File(pathFile);
		if(!file.exists())
			file.createNewFile();
		PreparedStatement termInMemory = connection.prepareStatement(QueriesResources.selectResourceElementAndClass);
		termInMemory.setInt(1,getID());
		ResultSet rs = termInMemory.executeQuery();
		PrintWriter pw = new PrintWriter(file);				
		while(rs.next())
		{
			entity=rs.getString(1);
			classe=rs.getString(2);
			String line = entity+delimiter+classe;
			pw.write(line);
			pw.println();
		}
		pw.close();
		rs.close();
		termInMemory.close();
		return true;
	}

	public IResourceUpdateReport loadTermFromGenericCVSFile(File file,CSVFileConfigurations csvfileconfigurations) throws SQLException, IOException, DatabaseLoadDriverException
	{
		IResourceUpdateReport report = new ResourceUpdateReport(GlobalNames.updateLookupTableReportTitle, this, file, "");
		String line;
		if(file==null || !file.exists())
		{
			report.changePerformedStatus(false);
			return report;
		}
		else
		{
			long startTime = GregorianCalendar.getInstance().getTimeInMillis();
			int step = 0;
			Set<Integer> classes = getClassContent();
			Set<Integer> insertClasses = new HashSet<Integer>();
			int getLines = FileHandling.getFileLines(file);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			loadAllTermsFromDatabaseToMemory();
			if(csvfileconfigurations.isHasHeaders())
			{
				 br.readLine();
			}
			while((line = br.readLine())!=null)
			{	
				processLine(csvfileconfigurations, report, line, classes,insertClasses);
				if(step%1000==0)
				{
					memoryAndProgress(step, getLines);
				}
				step++;
			}
			updateContent(insertClasses);
			deleteTermsInMemory();
			long endTime = GregorianCalendar.getInstance().getTimeInMillis();
			report.setTime(endTime-startTime);
			return report;
		}
	}

	private void processLine(CSVFileConfigurations csvfileconfigurations,
			IResourceUpdateReport report, String line, Set<Integer> classes,
			Set<Integer> insertClasses) throws SQLException, DatabaseLoadDriverException {
		String[] lin = line.split(csvfileconfigurations.getGeneralDelimiter().getValue());
		String classeStr = lin[csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(ColumnNames.classe).getColumnNumber()];
		classeStr = classeStr.replace(csvfileconfigurations.getTextDelimiter().getValue(), "");
		int classID = ClassProperties.getClassIDOrinsertIfNotExist(classeStr);
		updateContent(report, classes, insertClasses, classID);
		addElement(csvfileconfigurations, report, lin, classeStr,classID);
	}

	private void updateContent(IResourceUpdateReport report,
			Set<Integer> classes, Set<Integer> insertClasses, int classID) {
		if(!classes.contains(classID))
		{
			if(!insertClasses.contains(classID))
			{
				report.addClassesAdding(1);
				insertClasses.add(classID);
			}
		}
	}

	private void updateContent(Set<Integer> insertClasses) throws SQLException, DatabaseLoadDriverException {
		for(Integer id:insertClasses)
		{
			addResourceContent(id);	
		}
	}

	private void addElement(CSVFileConfigurations csvfileconfigurations,
			IResourceUpdateReport report, String[] lin, String classeStr,
			int classID) {
		IResourceElement elem;
		String term = lin[csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(ColumnNames.term).getColumnNumber()];	
		term = term.replace(csvfileconfigurations.getTextDelimiter().getValue(), "");
		elem = new ResourceElement(-1,term,classID,classeStr);
		if(addElement(elem))
		{
			report.addTermAdding(1);
		}
		else
		{
			IInsertConflits conflit = new ResourceMergeConflits(ConflitsType.AlreadyHaveTerm, elem, elem);
			report.addConflit(conflit);
		}
	}

	public int compareTo(ILookupTable dic)
	{
		if(this.getID()==dic.getID())
		{
			return 0;
		}
		else if(this.getID()<=dic.getID())
		{
			return -1;
		}
		return 1;
	}
	
	public String toString()
	{
		String info = new String();
		info ="Lookup Table : " + getName() + " (ID :"+ getID() + " ) ";
		if(!getInfo().equals(""))
		{
			info = info + "Notes: "+getInfo();
		}
		if(!isActive())
		{
			info = info + " (Inactive) ";
		}
		return info;
	}

	public String getType() {
		return GlobalOptions.resourcesLookupTableName;
	}
	
	public IResourceMergeReport merge(ILookupTable looSnd) throws SQLException, DatabaseLoadDriverException 
	{
		IResourceMergeReport report = new ResourceMergeReport(GlobalNames.mergeLookupTablesReportTitle, looSnd, this);
		loadAllTermsFromDatabaseToMemory();	
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		Set<Integer> classesSnd = looSnd.getClassContent();
		Set<Integer> classes1st = getClassContent();
		for(Integer classID:classesSnd)
		{
			if(!classes1st.contains(classID))
			{
				this.addResourceContent(classID);
				report.addClassesAdding(1);
			}
		}
		IResourceElementSet<IResourceElement> terms = looSnd.getResourceElements();
		int getLines = terms.getElements().size();
		int step = 0;
		for(IResourceElement elem:terms.getElements())
		{		
			addElement(report, elem);
			if(step%1000==0)
			{
				memoryAndProgress(step, getLines);
			}
			step++;
		}
		deleteTermsInMemory();
		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		report.setTime(endTime-startTime);
		return report;
	}

	private void addElement(IResourceMergeReport report, IResourceElement elem) {
		if(this.addElement(elem))
		{
			report.addTermAdding(1);
		}
		else
		{
			IInsertConflits conflit = new ResourceMergeConflits(ConflitsType.AlreadyHaveTerm, elem, elem);
			report.addConflit(conflit);
		}
	}
	
	public IResourceMergeReport merge(ILookupTable looSnd,Set<Integer> classIds) throws SQLException, DatabaseLoadDriverException 
	{
		IResourceMergeReport report = new ResourceMergeReport(GlobalNames.mergeLookupTablesReportTitle, looSnd, this);
		loadAllTermsFromDatabaseToMemory();	
		IResourceElementSet<IResourceElement> terms = new ResourceElementSet<IResourceElement>();
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		Set<Integer> classesSnd = looSnd.getClassContent();
		Set<Integer> classes1st = getClassContent();
		for(Integer classID:classesSnd)
		{
			if(!classes1st.contains(classID))
			{
				this.addResourceContent(classID);
				report.addClassesAdding(1);
			}
			terms.addAllElementResource(looSnd.getTermByClass(classID).getElements());	
		}
		int getLines = terms.getElements().size();
		int step = 0;
		for(IResourceElement elem:terms.getElements())
		{
			addElement(report, elem);
			if(step%1000==0)
			{
				memoryAndProgress(step, getLines);
			}
			step++;
		}
		deleteTermsInMemory();
		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		report.setTime(endTime-startTime);
		return report;
	}

}



