package pt.uminho.anote2.aibench.corpus.datatypes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import pt.uminho.anote2.aibench.corpus.stats.NerStatitics;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IExternalID;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.report.processes.INERSchemaExportReport;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesProcess;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.report.processes.io.export.NERSchemaExportReport;
import pt.uminho.anote2.datastructures.resources.ResourcesHelp;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.process.IE.INERProcess;
import pt.uminho.anote2.process.IE.INERSchema;
import pt.uminho.anote2.process.IE.ner.export.INERCSVConfiguration;
import pt.uminho.anote2.resource.dictionary.IDictionary;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.ListElements;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import es.uvigo.ei.aibench.workbench.Workbench;

@Datatype(structure = Structure.LIST,namingMethod="getNERName",setNameMethod="setNERName",removable=true,renamed=true,autoOpen=true)
public class NERSchema extends IEProcess implements INERSchema{

	private List<IAnnotatedDocument> annotatedDocs;
	private Map<Integer,IAnnotatedDocument> allProcessDocs;
	private Map<Integer,IEntityAnnotation> entityAnnotations;
	private NerStatitics statistics;
	
	public NERSchema(int id,ICorpus corpus, String description, String type,Properties properties) {
		super(id,corpus, description, type, properties);
		this.annotatedDocs = new ArrayList<IAnnotatedDocument>();
		this.allProcessDocs = null;
		this.entityAnnotations=null;
		this.statistics = null;
	}

	public NERSchema(INERProcess nerProcess) {
		super(nerProcess.getID(),nerProcess.getCorpus(), nerProcess.getName(), nerProcess.getType(), nerProcess.getProperties());
		this.annotatedDocs = new ArrayList<IAnnotatedDocument>();
		this.allProcessDocs = null;
		this.entityAnnotations=null;
		this.statistics = null;
	}

	@ListElements(modifiable=true)
	public List<IAnnotatedDocument> getAnnotatedDocs() {
		return annotatedDocs;
	}
	
	public void addAnnotatedDocument(IAnnotatedDocument doc)
	{
		if(!alreadyExist(doc))
		{
			annotatedDocs.add(doc);
			new ShowMessagePopup("Annotated Document Added .");
			notifyViewObservers();
		}	
	}
	
	public boolean alreadyExist(IAnnotatedDocument doc)
	{
		for(IAnnotatedDocument docEx:annotatedDocs)
		{
			if(docEx.getID()==doc.getID())
			{
				return true;
			}
		}
		return false;
	}
	
	public void notifyViewObservers()
	{
		this.setChanged();
		this.notifyObservers();
	}
	
	public String getNERName()
	{
		return getName()+" ("+getType()+") "+getId();
	}

	public void setNERName(String newName) throws SQLException, DatabaseLoadDriverException
	{
		if(newName.equals(getName()) || newName.endsWith(getName()+" ("+getType()+") "+getId()))
		{
			
		}
		else if(newName==null  || newName.length() == 0)
		{
			this.setChanged();
			this.notifyObservers();
			Workbench.getInstance().warn("Process Name can not be empty");
		}
		else
		{
			if(!newName.endsWith(" ("+getType()+") "+getId()))
				newName = newName + " ("+getType()+") "+getId();
			super.setName(newName);
			super.setNameInDatabse(newName);
			((Corpus) getCorpus()).notifyViewObserver();
		}
	}

	synchronized public Map<Integer,IAnnotatedDocument> getAllProcessDocs() throws SQLException, DatabaseLoadDriverException {
		if(allProcessDocs==null)
		{
			allProcessDocs = getAllAnnotatedDocs();
		}
		return allProcessDocs;
	}

	synchronized public Map<Integer,IEntityAnnotation> getEntityAnnotations() throws SQLException, DatabaseLoadDriverException {
		if(entityAnnotations==null)
		{
			entityAnnotations = getAllEntities();
		}
		return entityAnnotations;
	}
	
	synchronized public NerStatitics getStatistics() throws SQLException, DatabaseLoadDriverException {
		if(statistics == null)
		{	
			statistics = new NerStatitics(getAllAnnotatedDocs());
		}
		return statistics;
	}
	
	synchronized private Map<Integer, IAnnotatedDocument> getAllAnnotatedDocs() throws SQLException, DatabaseLoadDriverException {
		Map<Integer, IAnnotatedDocument> docAnnotation = new HashMap<Integer, IAnnotatedDocument>();
		IAnnotatedDocument annotDoc;
		int docID;
		for(IDocument doc:getCorpus().getArticlesCorpus())
		{
			docID = doc.getID();
			annotDoc = new NERDocumentAnnotation(this,getCorpus(),getCorpus().getArticlesCorpus().getDocument(docID));
			docAnnotation.put(docID, annotDoc);
		}
		return docAnnotation;
	}

	
	public INERSchemaExportReport exportToCSV(File file,INERCSVConfiguration configuration,TimeLeftProgress timeLeft) throws FileNotFoundException, SQLException, DatabaseLoadDriverException {
		IDictionary dictionary = new Dictionary(1, "", "");
		PrintWriter pw;
		INERSchemaExportReport report = new NERSchemaExportReport();
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		long nowTime;
		pw = new PrintWriter(file);
		Map<Integer, IAnnotatedDocument> docs = getAllAnnotatedDocs();
		Map<Integer,String> externalIdsSTr = new HashMap<Integer, String>();
		Map<Integer,String> resourceIdsSTr = new HashMap<Integer, String>();
		int step = 0;
		int total = docs.size();
		writeHeaderLine(configuration,pw);
		for(IAnnotatedDocument docID:docs.values())
		{	
			IAnnotatedDocument docAnnot = new NERDocumentAnnotation(this, getCorpus(), docID);
			for(IEntityAnnotation ent : docAnnot.getEntitiesAnnotations())
			{
				writeline(configuration,pw,docAnnot,ent,dictionary,externalIdsSTr,resourceIdsSTr);
				report.incremetExportedEntity(1);
			}
			timeLeft.setProgress((float) step / (float)total);
			nowTime = GregorianCalendar.getInstance().getTimeInMillis();
			timeLeft.setTime(nowTime-startTime, step, total);
			step++;
		}
		pw.close();
		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		report.setTime(endTime-startTime);
		return report;
	}


	@Override
	public INERSchemaExportReport exportToCSV(File file,INERCSVConfiguration configuration) throws FileNotFoundException, SQLException, DatabaseLoadDriverException {
		IDictionary dictionary = new Dictionary(1, "", "");
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		PrintWriter pw;
		INERSchemaExportReport report = new NERSchemaExportReport();
		pw = new PrintWriter(file);
		Map<Integer, IAnnotatedDocument> docs = getAllAnnotatedDocs();
		Map<Integer,String> externalIdsSTr = new HashMap<Integer, String>();
		Map<Integer,String> resourceIdsSTr = new HashMap<Integer, String>();
		writeHeaderLine(configuration,pw);
		for(IAnnotatedDocument docID:docs.values())
		{
			IAnnotatedDocument docAnnot = new NERDocumentAnnotation(this, getCorpus(), docID);;
			for(IEntityAnnotation ent : docAnnot.getEntitiesAnnotations())
			{
				writeline(configuration,pw,docAnnot,ent, dictionary,externalIdsSTr,resourceIdsSTr);
				report.incremetExportedEntity(1);
			}
		}
		pw.close();
		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		report.setTime(endTime-startTime);
		return report;
	}

	private void writeHeaderLine(INERCSVConfiguration configuration,PrintWriter pw) {
		String[] toWrite = new String[9];
		toWrite[configuration.getColumnConfiguration().getAnnotationIDColumn()] = "AnnotationID";
		toWrite[configuration.getColumnConfiguration().getPublicationIDColumn()] = "PublicationID/PMID";
		toWrite[configuration.getColumnConfiguration().getElementColumn()] = "Term";
		toWrite[configuration.getColumnConfiguration().getClassColumn()] = "Class";
		toWrite[configuration.getColumnConfiguration().getStartOffset()] = "StartOffset";
		toWrite[configuration.getColumnConfiguration().getEndOffset()] = "EndOffset";
		toWrite[configuration.getColumnConfiguration().getResourceIDColumn()] = "ResourceID";
		toWrite[configuration.getColumnConfiguration().getResourceInformation()] = "Resource Name";
		toWrite[configuration.getColumnConfiguration().getResourceExternalIDs()] = "External IDs";
		String header = getLineToWrite(configuration,toWrite);
		pw.write(header);
		pw.println();
	}

	private void writeline(INERCSVConfiguration configuration, PrintWriter pw,IAnnotatedDocument docID, IEntityAnnotation entAnnot, IDictionary dictionary, Map<Integer, String> externalIdsSTr, Map<Integer, String> resourceIdsSTr) throws SQLException, DatabaseLoadDriverException {
		String[] toWrite = new String[9];
		toWrite[configuration.getColumnConfiguration().getAnnotationIDColumn()] = configuration.getTextDelimiter().getValue() + entAnnot.getID() + configuration.getTextDelimiter().getValue();
		if(configuration.exportPublicationOtherID() && docID.getOtherID()!=null && !docID.getOtherID().equals(""))
		{
			toWrite[configuration.getColumnConfiguration().getPublicationIDColumn()] = configuration.getTextDelimiter().getValue() + "PMID:"+docID.getOtherID() + configuration.getTextDelimiter().getValue();
		}
		else
		{
			toWrite[configuration.getColumnConfiguration().getPublicationIDColumn()] = configuration.getTextDelimiter().getValue() + "ID:"+docID.getID() + configuration.getTextDelimiter().getValue();
		}
		toWrite[configuration.getColumnConfiguration().getElementColumn()] = configuration.getTextDelimiter().getValue() + entAnnot.getAnnotationValue() + configuration.getTextDelimiter().getValue();
		toWrite[configuration.getColumnConfiguration().getClassColumn()] = configuration.getTextDelimiter().getValue() + ClassProperties.getClassIDClass().get(entAnnot.getClassAnnotationID()) + configuration.getTextDelimiter().getValue();
		toWrite[configuration.getColumnConfiguration().getStartOffset()] = configuration.getTextDelimiter().getValue() + entAnnot.getStartOffset() + configuration.getTextDelimiter().getValue();
		toWrite[configuration.getColumnConfiguration().getEndOffset()] = configuration.getTextDelimiter().getValue() + entAnnot.getEndOffset() + configuration.getTextDelimiter().getValue();
		if(entAnnot.getResourceElementID() > 0)
		{
			toWrite[configuration.getColumnConfiguration().getResourceIDColumn()] = configuration.getTextDelimiter().getValue() + entAnnot.getResourceElementID() + configuration.getTextDelimiter().getValue();
			if(configuration.exportResourceInformation())
			{
				if(!resourceIdsSTr.containsKey(entAnnot.getResourceElementID()))
				{
					resourceIdsSTr.put(entAnnot.getResourceElementID(), ResourcesHelp.getTermResource(entAnnot.getResourceElementID()).toString());
				}
				toWrite[configuration.getColumnConfiguration().getResourceInformation()] = resourceIdsSTr.get(entAnnot.getResourceElementID());
			}
			else
			{
				toWrite[configuration.getColumnConfiguration().getResourceInformation()] = null;
			}
			if(configuration.exportResourceExternalID())
			{
				if(!externalIdsSTr.containsKey(entAnnot.getResourceElementID()))
				{
					String strExternalIds = null;
					List<IExternalID> extIDs = dictionary.getexternalIDandSorceIDandSource(entAnnot.getResourceElementID());
					if(extIDs.size() > 0)
					{
						strExternalIds = new String();
						for(IExternalID extID: extIDs)
						{
							strExternalIds = strExternalIds + configuration.getExternalIDDelimiter().getValue() + extID.getExternalID() + configuration.getIntraExtenalIDdelimiter().getValue() + extID.getSource();
						}
						strExternalIds = strExternalIds.substring(1);
						strExternalIds = configuration.getTextDelimiter().getValue() + strExternalIds  + configuration.getTextDelimiter().getValue();
					}
					externalIdsSTr.put(entAnnot.getResourceElementID(), strExternalIds);
				}
			}
			toWrite[configuration.getColumnConfiguration().getResourceExternalIDs()] = externalIdsSTr.get(entAnnot.getResourceElementID());
		}
		else
		{
			toWrite[configuration.getColumnConfiguration().getResourceIDColumn()] = null;
			toWrite[configuration.getColumnConfiguration().getResourceInformation()] = null;
			toWrite[configuration.getColumnConfiguration().getResourceExternalIDs()] = null;
		}
		String lineToFile = getLineToWrite(configuration,toWrite);
		pw.write(lineToFile);
		pw.println();
	}

	private String getLineToWrite(INERCSVConfiguration configuration,String[] toWrite) {
		String line = new String();
		for(String value:toWrite)
		{
			if(value == null)
				line = line + configuration.getDefaultDelimiter().getValue();
			else
				line = line + value;
			line = line + configuration.getMainDelimiter().getValue();
		}
		return line.substring(0, line.length()-configuration.getMainDelimiter().getValue().length());
	}
	
	public void freeMemory()
	{
		this.annotatedDocs = null;
		this.allProcessDocs = null;
		this.entityAnnotations = null;
		this.statistics.freeMemory();
		this.statistics = null;
	}

	public void addProperty(String key, String value) throws SQLException, DatabaseLoadDriverException {
		if(!getProperties().containsKey(key))
		{
			PreparedStatement insertP = Configuration.getDatabase().getConnection().prepareStatement(QueriesProcess.insertProcessProperties);
			insertP.setInt(1, getID());
			insertP.setNString(2, key);
			insertP.setNString(3, value);
			insertP.execute();
			insertP.close();
			getProperties().put(key, value);
		}
	}		
	
}
