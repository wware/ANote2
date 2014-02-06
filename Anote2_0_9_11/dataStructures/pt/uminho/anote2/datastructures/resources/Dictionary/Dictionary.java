package pt.uminho.anote2.datastructures.resources.Dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.anote2.core.annotation.IExternalID;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.ConflitsType;
import pt.uminho.anote2.core.report.resources.IInsertConflits;
import pt.uminho.anote2.core.report.resources.IResourceMergeReport;
import pt.uminho.anote2.core.report.resources.IResourceReport;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import pt.uminho.anote2.datastructures.annotation.ExternalID;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.database.schema.TableResourcesElements;
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
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.datastructures.utils.generic.CSVFileConfigurations;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;
import pt.uminho.anote2.resource.dictionary.IDictionary;

/**
 * 
 * @author Hugo Costa
 *
 */
public class Dictionary extends ResourcesHelp implements IDictionary,IResourceImportFromCSVFiles{

	private BufferedReader br;

	public Dictionary(int id,String name,String info)
	{
		super(id,name,info);
	}
	
	public Dictionary(int id,String name,String info,boolean active)
	{
		super(id,name,info,active);
	}

	public IResourceMergeReport merge(IDictionary dicSnd) throws DatabaseLoadDriverException, SQLException 
	{
		IResourceMergeReport report = new ResourceMergeReport(GlobalNames.mergeDictionariesReportTitle, dicSnd, this);
		loadAllTermsFromDatabaseToMemory();	
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		Set<Integer> classesSnd = dicSnd.getClassContent();
		Set<Integer> classes1st = getClassContent();
		for(Integer classID:classesSnd)
		{
			if(!classes1st.contains(classID))
			{
				this.addResourceContent(classID);
				report.addClassesAdding(1);
				classes1st.add(classID);
			}
		}
		int nextResourceElementID = HelpDatabase.getNextInsertTableID(GlobalTablesName.resource_elements);	
		IResourceElementSet<IResourceElement> terms = dicSnd.getResourceElements();	
		updateTerms(dicSnd,report, nextResourceElementID, terms);
		deleteTermsInMemory();
		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		report.setTime(endTime-startTime);
		return report;
	}

	public IResourceMergeReport merge(IDictionary dicSnd,Set<Integer> classIDs) throws DatabaseLoadDriverException, SQLException 
	{
		IResourceMergeReport report = new ResourceMergeReport(GlobalNames.mergeDictionariesReportTitle, dicSnd, this);
		loadAllTermsFromDatabaseToMemory();	
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		Set<Integer> classes1st = getClassContent();
		IResourceElementSet<IResourceElement> all = new ResourceElementSet<IResourceElement>();
		for(Integer classID:classIDs)
		{
			if(!classes1st.contains(classID))
			{
				this.addResourceContent(classID);
				report.addClassesAdding(1);
				classes1st.add(classID);
			}
			all.addAllElementResource(dicSnd.getTermByClass(classID).getElements());
		}
		int nextResourceElementID = HelpDatabase.getNextInsertTableID(GlobalTablesName.resource_elements);	
		updateTerms(dicSnd,report, nextResourceElementID, all);
		deleteTermsInMemory();
		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		report.setTime(endTime-startTime);
		return report;
	}
	
	
	private void updateTerms(IDictionary dicSnd, IResourceMergeReport report,int nextResourceElementID,IResourceElementSet<IResourceElement> terms) throws SQLException, DatabaseLoadDriverException {
		Map<String, Integer> sourceSourceID = new HashMap<String, Integer>();
		IResourceElementSet<IResourceElement> syns;
		List<IExternalID> list;
		int lineNumber = 0;
		int totalLines = terms.getElements().size();
		Map<Integer,IResourceElementSet<IResourceElement>> termIDSynonyms = new HashMap<Integer, IResourceElementSet<IResourceElement>>();
		termIDSynonyms = getAllSynonyms(dicSnd);
		Map<Integer,List<IExternalID>> termIDExternalIds = new HashMap<Integer, List<IExternalID>>();
		termIDExternalIds = getAllExtenalIds(dicSnd);	
		for(IResourceElement elem:terms.getElements())
		{
			if(termIDSynonyms.containsKey(elem.getID()))
			{
				syns = termIDSynonyms.get(elem.getID());
			}
			else
			{
				syns  = new ResourceElementSet<IResourceElement>();
			}
			if(termIDExternalIds.containsKey(elem.getID()))
			{
				list = termIDExternalIds.get(elem.getID());
			}
			else
			{
				list = new ArrayList<IExternalID>();
			}
			if(getShortElems().containsKey(elem.getTerm()))
			{
				reportAlreadyHaveTermConflit(report, elem);
				int termID = getShortElems().get(elem.getTerm()).getY();
				synonymUpdateMErge(report, termID, elem,syns.getElements());
				externalIDupdate(report, termID,sourceSourceID, elem, list);
			}
			else if(this.addElement(elem))
			{
				report.addTermAdding(1);
				synonymUpdateMErge(report, nextResourceElementID, elem,syns.getElements());
				externalIDupdate(report, nextResourceElementID,sourceSourceID, elem, list);
				nextResourceElementID++;
			}
			else
			{
				reportupdateTermConflit(report, elem);
			}
			if((lineNumber%500)==0)
			{
				memoryAndProgress(lineNumber, totalLines);
			}
			lineNumber++;
		}
	}
	
	
	private Map<Integer, List<IExternalID>> getAllExtenalIds(IDictionary dicSnd) throws SQLException, DatabaseLoadDriverException {
		Map<Integer, List<IExternalID>> termIDExtIds = new HashMap<Integer, List<IExternalID>>();
		int termID,sourceID;
		String extID;
		String source;
		PreparedStatement ps = GlobalOptions.database.getConnection().prepareStatement(QueriesResources.getallExternalIdsMerge);
		ps.setInt(1, dicSnd.getID());
		ResultSet rs = ps.executeQuery();
		while(rs.next())
		{
			termID = rs.getInt(1);
			extID = rs.getString(2);
			sourceID = rs.getInt(3);
			source = rs.getString(4);
			if(!termIDExtIds.containsKey(termID))
			{
				termIDExtIds.put(termID,new ArrayList<IExternalID>());
			}
			termIDExtIds.get(termID).add(new ExternalID(extID,source, sourceID));
		}
		rs.close();
		ps.close();
		return termIDExtIds;
	}

	private Map<Integer, IResourceElementSet<IResourceElement>> getAllSynonyms(IDictionary dicSnd) throws SQLException, DatabaseLoadDriverException {
		int termID;
		String synonym;
		Map<Integer, IResourceElementSet<IResourceElement>> termIDSynonyms = new HashMap<Integer, IResourceElementSet<IResourceElement>>();
		PreparedStatement ps = GlobalOptions.database.getConnection().prepareStatement(QueriesResources.getallSynonymsMerg);
		ps.setInt(1, dicSnd.getID());
		ResultSet rs = ps.executeQuery();
		while(rs.next())
		{
			termID = rs.getInt(1);
			synonym = rs.getString(2);
			if(!termIDSynonyms.containsKey(termID))
			{
				termIDSynonyms.put(termID, new ResourceElementSet<IResourceElement>());
			}
			termIDSynonyms.get(termID).addElementResource(new ResourceElement(synonym));
		}
		rs.close();
		ps.close();
		return termIDSynonyms;
	}

	public IResourceUpdateReport loadTermFromGenericCVSFile(File file,CSVFileConfigurations csvfileconfigurations) throws DatabaseLoadDriverException, SQLException, IOException
	{
		IResourceUpdateReport report = new ResourceUpdateReport(GlobalNames.updateDictionariesReportTitle, this, file, "");
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		loadAllTermsFromDatabaseToMemory();
		int nextResourceElementID = HelpDatabase.getNextInsertTableID(GlobalTablesName.resource_elements);
		importCVSFile(report,file,csvfileconfigurations,nextResourceElementID);
		deleteTermsInMemory();
		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		report.setTime(endTime-startTime);
		return report;
	}

	public boolean importCVSFile(IResourceUpdateReport report,File file,CSVFileConfigurations csvfileconfigurations,int nextResourceElementID) throws IOException, SQLException, DatabaseLoadDriverException {	
		if(file==null || !file.exists())
		{
			return false;
		}
		else
		{
			int nextResourceID = nextResourceElementID;
			Map<String,Integer> sourceSourceID = new HashMap<String, Integer>();
			String line;
			int step = 0;
			int total = FileHandling.getFileLines(file);
			Set<Integer> classesSnd = this.getClassContent();
			String term;
			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr);
			while((line = br.readLine())!=null)
			{
				String[] lin = line.split(csvfileconfigurations.getGeneralDelimiter().getValue());
				term = getTerm(lin,csvfileconfigurations);
				nextResourceID = processLine(report,csvfileconfigurations, nextResourceID,sourceSourceID, classesSnd, term, lin);	
				if(step%1000==0)
				{
					memoryAndProgress(step, total);
				}
				step++;
			}		
			return true;
		}
	}

	protected int processLine(IResourceUpdateReport report,
			CSVFileConfigurations csvfileconfigurations,
			int nextResourceElementID,
			Map<String, Integer> sourceSourceID, Set<Integer> classesSnd,
			String term, String[] lin) throws SQLException, DatabaseLoadDriverException {
		int classID;
		String classe;
		List<String> synList;
		if(term != null && term.length()>=TableResourcesElements.mimimumElementSize && term.length()<TableResourcesElements.elementSize)
		{
			classe = getClass(lin,csvfileconfigurations);
			if(classe != null && classe.length()>=TableResourcesElements.mimimumClasseElementSize && classe.length()<TableResourcesElements.classeSize)
			{
				synList = getSynonyms(lin,csvfileconfigurations);
				List<IExternalID> extLis = getExternalIds(lin,csvfileconfigurations);
				classID = ClassProperties.getClassIDOrinsertIfNotExist(classe);
				nextResourceElementID = updateTerm(report,nextResourceElementID, sourceSourceID, classID,term, classe, synList, extLis);
				updateClasses(report, classID, classesSnd);
			}
		}
		return nextResourceElementID;
	}

	private int updateTerm(IResourceUpdateReport report,
			int nextResourceElementID, Map<String, Integer> sourceSourceID,
			int classID, String term, String classe, List<String> synList,
			List<IExternalID> extLis) {
		IResourceElement elem;
		elem = new ResourceElement(nextResourceElementID, term, classID, classe, extLis,0);
		if(getShortElems()!= null && getShortElems().containsKey(elem.getTerm()))
		{
			if(getShortElems().get(elem.getTerm()).getX() == elem.getTermClassID())
			{
				reportAlreadyHaveTermConflit(report,elem);
				int termID = getShortElems().get(elem.getTerm()).getY();
				elem = new ResourceElement(termID, term, classID, classe, extLis,0);
				synonymUpdate(report, termID, elem,synList);
				externalIDupdate(report, termID,sourceSourceID, elem, extLis);
			}
			else
			{
				IInsertConflits conflit = new ResourceMergeConflits(ConflitsType.TermInDiferentClasses, elem, new ResourceElement(-1,"",getShortElems().get(elem.getTerm()).getX(),""));
				report.addConflit(conflit);
			}
		}
		else if(getSynonyms()!=null && getSynonyms().containsKey(elem.getTerm()))
		{
			IInsertConflits conflit;
			if(elem.getTermClassID()==getSynonyms().get(elem.getTerm()).getX())
			{
				// Already Have term as Synonym
				conflit = new ResourceMergeConflits(ConflitsType.AlreadyHaveTerm, elem, elem);
			}
			else
			{
				// Already Have Term as Synonym and Class are diferent
				conflit = new ResourceMergeConflits(ConflitsType.TermInDiferentClasses, elem, new ResourceElement(-1,"",getSynonyms().get(elem.getTerm()).getX(),""));
			}
			report.addConflit(conflit);
		}
		else if(this.addElement(elem)) // Try insert
		{
			synonymUpdate(report, nextResourceElementID, elem,synList);
			externalIDupdate(report, nextResourceElementID,sourceSourceID, elem, extLis);
			nextResourceElementID++;
			report.addTermAdding(1);
		} 
		else // Error
		{
			System.err.println("Could not insert elem "+elem.getTerm() + " whit "+ elem.getSynonyms().toString() +" synonym and "+elem.getExtenalIDs().toString() + " external Ids");
		}
		return nextResourceElementID;
	}

	private void reportAlreadyHaveTermConflit(IResourceReport report,IResourceElement elem) {
		IInsertConflits conflit = new ResourceMergeConflits(ConflitsType.AlreadyHaveTerm, elem, new ResourceElement(elem.getTerm()));
		report.addConflit(conflit);	
	}

	private void synonymUpdateMErge(IResourceReport report,
			int nextResourceElementID, IResourceElement elem,
			Set<IResourceElement> set) {
		for(IResourceElement syn :set)
		{
			if(addSynomyn(new ResourceElement(nextResourceElementID,syn.getTerm(),elem.getTermClassID(),"")))
			{
				report.addSynonymsAdding(1);
			}
			else
			{
				reportSynonymUpdate(report, elem);
			}
		}
	}
	
	private void synonymUpdate(IResourceReport report,
			int nextResourceElementID, IResourceElement elem,
			List<String> synList) {
		for(String syn :synList)
		{
			IResourceElement synonym = new ResourceElement(nextResourceElementID,syn,elem.getTermClassID(),"");
			
			if(addSynomyn(synonym))
			{
				report.addSynonymsAdding(1);
			}
			else
			{
				reportSynonymUpdate(report, synonym);
			}
		}
	}

	private void reportSynonymUpdate(IResourceReport report,
			IResourceElement elem) {
		IInsertConflits conflit = new ResourceMergeConflits(ConflitsType.AlreadyHaveSynonyms, elem, new ResourceElement(elem.getTerm()));
		report.addConflit(conflit);
	}

	private void externalIDupdate(IResourceReport report,
			int elementeID, Map<String, Integer> sourceSourceID,
			IResourceElement elem, List<IExternalID> extLis) {
		int sourceID;
		for(IExternalID id:extLis)
		{
			if(!sourceSourceID.containsKey(id.getSource()))
			{
				sourceID = addSource(id.getSource());
				sourceSourceID.put(id.getSource(), sourceID);
			}
			else
			{
				sourceID = sourceSourceID.get(id.getSource());
			}
			if(addExternalID(elementeID, id.getExternalID(),sourceID))
			{
				report.addExternalIDs(1);
			}
			else
			{
				reportExternalID(report, elem, id);
			}
		}
	}

	private void reportExternalID(IResourceReport report,IResourceElement elem, IExternalID id) {
		IResourceElement newElem = new ResourceElement(-1, "", 1, "", id);
		IInsertConflits conflit = new ResourceMergeConflits(ConflitsType.AlteradyHaveExternalID, newElem,newElem );
		report.addConflit(conflit);
	}


	private void updateClasses(IResourceUpdateReport report, int classID,Set<Integer> classesSnd) throws SQLException, DatabaseLoadDriverException {
		if(!classesSnd.contains(classID))
		{
			addResourceContent(classID);
			classesSnd.add(classID);
			report.addClassesAdding(1);
		}
	}

	private void reportupdateTermConflit(IResourceReport report,IResourceElement elem) {
		IInsertConflits conflit;
		if(getSynonyms().get(elem.getTerm())!=null && elem.getTermClassID()==getSynonyms().get(elem.getTerm()).getX())
		{
			// Already HAve term as Synonym
			conflit = new ResourceMergeConflits(ConflitsType.AlreadyHaveTerm, elem, elem);
		}
		else
		{
			// Already HAve Term as Synonym and Class are diferent
			conflit = new ResourceMergeConflits(ConflitsType.TermInDiferentClasses, elem, new ResourceElement(-1,"",getSynonyms().get(elem.getTerm()).getX(),""));
		}
		report.addConflit(conflit);
	}

	private List<IExternalID> getExternalIds(String[] lin,CSVFileConfigurations csvfileconfigurations) {
		if(csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(ColumnNames.externalID)==null)
		{
			return new ArrayList<IExternalID>();
		}
		String value = lin[csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(ColumnNames.externalID).getColumnNumber()];
		if(value.equals(csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(ColumnNames.externalID).getDefaultValue().getValue()))
		{
			return new ArrayList<IExternalID>();
		}
		String[] extIDs = value.split(csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(ColumnNames.externalID).getDelimiter().getValue());
		List<IExternalID> listExtID = new ArrayList<IExternalID>();
		for(String extID:extIDs)
		{
			String[] ex = extID.split(csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(ColumnNames.externalID).getSubDelimiter().getValue());
			if(ex.length>1)
			{
				String id = ex[0].replace(csvfileconfigurations.getTextDelimiter().getValue(),"");
				String source = ex[1].replace(csvfileconfigurations.getTextDelimiter().getValue(),"");
				listExtID.add(new ExternalID(id, source, 0));
			}
		}
		return listExtID;
	}

	private String getClass(String[] lin,CSVFileConfigurations csvfileconfigurations) {
		String value = lin[csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(ColumnNames.classe).getColumnNumber()];
		value = value.replace(csvfileconfigurations.getTextDelimiter().getValue(),"");
		return value;
	}

	private List<String> getSynonyms(String[] lin,CSVFileConfigurations csvfileconfigurations) {
		if(csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(ColumnNames.synonyms)==null)
		{
			return new ArrayList<String>();
		}
		String value = lin[csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(ColumnNames.synonyms).getColumnNumber()];
		if(value.equals(csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(ColumnNames.synonyms).getDefaultValue().getValue()))
		{
			return new ArrayList<String>();
		}
		else if(value.equals(""))
		{
			return new ArrayList<String>();
		}
		String[] syns = value.split(csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(ColumnNames.synonyms).getDelimiter().getValue());
		List<String> synList = new ArrayList<String>();
		String synElem;
		for(int i=0;i<syns.length;i++)
		{
			synElem = syns[i];
			synElem = synElem.replace(csvfileconfigurations.getTextDelimiter().getValue(),"");
			synList.add(synElem);
		}
		return synList;
	}

	protected String getTerm(String[] lin,CSVFileConfigurations csvfileconfigurations) {
		String value = lin[csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get(ColumnNames.term).getColumnNumber()];
		if(value!=null)
			value = value.replace(csvfileconfigurations.getTextDelimiter().getValue(),"");
		return value;
	}
	
	public int compareTo(IDictionary dic)
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
		info ="Dictionary : " + getName() + " (ID :"+ getID() + " ) ";
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
		return GlobalOptions.resourcesDictionaryName;
	}

	public String getResourceType(int id) throws SQLException, DatabaseLoadDriverException {
		String result = null;
		PreparedStatement ps;
		ResultSet rs;
		ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.getResourceElementtype);
		ps.setInt(1, id);
		rs = ps.executeQuery();
		if(rs.next())
		{
			result =  rs.getString(1);
		}
		rs.close();
		ps.close();
		return result;
	}
}
