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
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.annotation.IExternalID;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.ISentence;
import pt.uminho.anote2.core.report.processes.IRESchemaExportReport;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesProcess;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.report.processes.io.export.RESchemaExportReport;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.process.IE.IREProcess;
import pt.uminho.anote2.process.IE.IRESchema;
import pt.uminho.anote2.process.IE.re.IRECSVConfiguration;
import pt.uminho.anote2.resource.dictionary.IDictionary;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.ListElements;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import es.uvigo.ei.aibench.workbench.Workbench;

@Datatype(structure = Structure.LIST,namingMethod="getREName",setNameMethod="setREName",removable=true,renamed=true,autoOpen=true)
public class RESchema extends IEProcess implements IRESchema,Cloneable{
	
	private List<IAnnotatedDocument> annotatedDocs;
	private Map<Integer,IAnnotatedDocument> allProcessDocs;
	private Map<Integer,IEntityAnnotation> entityAnnotations;
	private Map<Integer,IEventAnnotation> eventAnnotations;
	private NerStatitics nerStatistics;
	
	public RESchema(int id,ICorpus corpus, String description, String type,Properties properties) {
		super(id,corpus, description, type, properties);
		annotatedDocs = new ArrayList<IAnnotatedDocument>();
		allProcessDocs = null;
		entityAnnotations = null;
		eventAnnotations = null;;
		nerStatistics = null;
	}

	public RESchema(IREProcess reProcess) {
		super(reProcess.getID(),reProcess.getCorpus(), reProcess.getName(), reProcess.getType(), reProcess.getProperties());
		annotatedDocs = new ArrayList<IAnnotatedDocument>();
		allProcessDocs = null;
		entityAnnotations = null;
		eventAnnotations = null;;
		nerStatistics = null;
	}

	@ListElements(modifiable=true)
	public List<IAnnotatedDocument> getAnnotatedDocs() {
		return annotatedDocs;
	}
	
	public void addAnnotatedDocument(REDocumentAnnotation doc)
	{
		if(alreadyExist(doc))
		{
			new ShowMessagePopup("Annotated Document Already In Clipboard.");
		}
		else
		{
			annotatedDocs.add(doc);
			new ShowMessagePopup("Annotated Document Added To Clipboard.");
			notifyViewObservers();
		}
	}
	
	public void setREName(String newName) throws SQLException, DatabaseLoadDriverException
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
	
	public String getREName()
	{
		return getName()+" ("+getType()+") "+getId();
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
			entityAnnotations = super.getAllEntities();
		}
		return entityAnnotations;
	}
	
	synchronized public Map<Integer,IEventAnnotation> getEventsAnnotations() throws SQLException, DatabaseLoadDriverException {
		if(eventAnnotations==null)
		{
			eventAnnotations = super.getAllEvents();
		}
		return eventAnnotations;
	}
	
	synchronized public NerStatitics getStatistics() throws SQLException, DatabaseLoadDriverException {
		if(nerStatistics == null)
		{	
			nerStatistics = new NerStatitics(getAllAnnotatedDocs());
		}
		return nerStatistics;
	}
	
	synchronized private Map<Integer, IAnnotatedDocument> getAllAnnotatedDocs() throws SQLException, DatabaseLoadDriverException {
		Map<Integer, IAnnotatedDocument> docAnnotation = new HashMap<Integer, IAnnotatedDocument>();
		IAnnotatedDocument annotDoc;
		int docID;
		for(IDocument doc:getCorpus().getArticlesCorpus())
		{
			docID = doc.getID();
			annotDoc = new REDocumentAnnotation(this,getCorpus(),doc);
			docAnnotation.put(docID, annotDoc);
		}
		return docAnnotation;
	}

	
	public IRESchemaExportReport exportToCSV(File file,IRECSVConfiguration configuration,TimeLeftProgress progress) throws FileNotFoundException, SQLException, DatabaseLoadDriverException {
		IDictionary dictionary = new Dictionary(1, "", "");
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		long nowTime;
		PrintWriter pw;
		IRESchemaExportReport report = new RESchemaExportReport();
		pw = new PrintWriter(file);
		Map<Integer, IAnnotatedDocument> docs = getAllAnnotatedDocs();		
		Map<Integer,String> externalID = new HashMap<Integer, String>();
		int step = 0;
		int total = docs.size();
		writeHeaderLine(configuration,pw);
		for(IAnnotatedDocument docID:docs.values())
		{
			IAnnotatedDocument docAnnot = new NERDocumentAnnotation(this, getCorpus(), docID);;
			for(IEventAnnotation ev : docAnnot.getEventAnnotations())
			{
				writeline(configuration,pw,docAnnot,ev,dictionary,externalID);
				report.incrementeRelationsExported(1);
			}
			progress.setProgress((float) step / (float)total);
			nowTime = GregorianCalendar.getInstance().getTimeInMillis();
			progress.setTime(nowTime-startTime, step, total);
			step++;
		}
		pw.close();
		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		report.setTime(endTime-startTime);
		return report;
	}

	
	@Override
	public IRESchemaExportReport exportToCSV(File file,IRECSVConfiguration configuration) throws FileNotFoundException, SQLException, DatabaseLoadDriverException {
		IDictionary dictionary = new Dictionary(1, "", "");
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		PrintWriter pw;
		IRESchemaExportReport report = new RESchemaExportReport();
		pw = new PrintWriter(file);
		Map<Integer, IAnnotatedDocument> docs = getAllAnnotatedDocs();
		Map<Integer,String> externalID = new HashMap<Integer, String>();
		for(IAnnotatedDocument docID:docs.values())
		{
			IAnnotatedDocument docAnnot = new NERDocumentAnnotation(this, getCorpus(), docID);;
			for(IEventAnnotation ev : docAnnot.getEventAnnotations())
			{
				writeline(configuration,pw,docAnnot,ev,dictionary,externalID);
				report.incrementeRelationsExported(1);
			}
		}
		pw.close();
		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		report.setTime(endTime-startTime);
		return report;
	}
	
	private void writeHeaderLine(IRECSVConfiguration configuration,PrintWriter pw) {
		String[] toWrite = new String[10];
		toWrite[configuration.getColumnConfiguration().getAnnotationIDColumn()] = "RelationID";
		toWrite[configuration.getColumnConfiguration().getPublicationIDColumn()] = "PublicationID/PMID";
		toWrite[configuration.getColumnConfiguration().getElementColumn()] = "Verb (Clue)";
		toWrite[configuration.getColumnConfiguration().getStartOffset()] = "Start Offset";
		toWrite[configuration.getColumnConfiguration().getEndOffset()] = "End Offset";
		toWrite[configuration.getColumnConfiguration().getLeftEntitiesColumn()] = "LeftEntities";
		toWrite[configuration.getColumnConfiguration().getRightEntitiesColumn()] = "RightEntities";
		toWrite[configuration.getColumnConfiguration().getLeftEntitiesExternalIDs()] = "LeftEntities External IDs";
		toWrite[configuration.getColumnConfiguration().getRightEntitiesExternalIDs()] = "RightEntities External IDs";
		toWrite[configuration.getColumnConfiguration().getSentenceColumn()] = "Sentence";
		String header = getLineToWrite(configuration,toWrite);
		pw.write(header);
		pw.println();
	}
	

	private void writeline(IRECSVConfiguration configuration, PrintWriter pw,IAnnotatedDocument docAnnot, IEventAnnotation ev, IDictionary dictionary,Map<Integer,String> externalID) throws DatabaseLoadDriverException, SQLException {
		String[] toWrite = new String[10];
		toWrite[configuration.getColumnConfiguration().getAnnotationIDColumn()] = configuration.getTextDelimiter().getValue() + ev.getID() + configuration.getTextDelimiter().getValue();
		if(configuration.exportPublicationOtherID() && docAnnot.getOtherID()!=null && !docAnnot.getOtherID().equals(""))
		{
			toWrite[configuration.getColumnConfiguration().getPublicationIDColumn()] = configuration.getTextDelimiter().getValue() + "PMID:"+docAnnot.getOtherID() + configuration.getTextDelimiter().getValue();
		}
		else
		{
			toWrite[configuration.getColumnConfiguration().getPublicationIDColumn()] = configuration.getTextDelimiter().getValue() + "ID:"+docAnnot.getID() + configuration.getTextDelimiter().getValue();
		}
		toWrite[configuration.getColumnConfiguration().getElementColumn()] = configuration.getTextDelimiter().getValue() + ev.getEventClue() + configuration.getTextDelimiter().getValue();
		toWrite[configuration.getColumnConfiguration().getStartOffset()] = configuration.getTextDelimiter().getValue() + ev.getStartOffset() + configuration.getTextDelimiter().getValue();
		toWrite[configuration.getColumnConfiguration().getEndOffset()] = configuration.getTextDelimiter().getValue() + ev.getEndOffset() + configuration.getTextDelimiter().getValue();
		toWrite[configuration.getColumnConfiguration().getLeftEntitiesColumn()] = getEntitiesToString(configuration,ev.getEntitiesAtLeft());
		toWrite[configuration.getColumnConfiguration().getRightEntitiesColumn()] = getEntitiesToString(configuration,ev.getEntitiesAtRight());
		toWrite[configuration.getColumnConfiguration().getSentenceColumn()] = getSentenceAnnotation(configuration,docAnnot,ev);
		toWrite[configuration.getColumnConfiguration().getLeftEntitiesExternalIDs()] = getExternalIDs(configuration,docAnnot,ev.getEntitiesAtLeft(),dictionary,externalID);
		toWrite[configuration.getColumnConfiguration().getRightEntitiesExternalIDs()] = getExternalIDs(configuration,docAnnot,ev.getEntitiesAtRight(),dictionary,externalID);
		String lineToFile = getLineToWrite(configuration,toWrite);
		pw.write(lineToFile);
		pw.println();		
	}
	
	
	
	private String getExternalIDs(IRECSVConfiguration configuration,IAnnotatedDocument docAnnot, List<IEntityAnnotation> entities, IDictionary dictionary,Map<Integer,String> externalID) throws DatabaseLoadDriverException, SQLException {
		if(!configuration.exportResourceExternalID())
		{
			return null;
		}
		String strExternalIds = new String();
		for(IEntityAnnotation entity : entities)
		{
			String entiTyExternalID = getEntityExternalIDs(configuration, dictionary, entity, externalID);
			if(entiTyExternalID!=null)
			{
				strExternalIds = strExternalIds + configuration.getEntityDelimiter().getValue() + entiTyExternalID;
			}
		}
		if(strExternalIds.length() == 0)
		{
			return null;
		}
		return configuration.getTextDelimiter().getValue() + strExternalIds.substring(1) + configuration.getTextDelimiter().getValue();
	}

	private String getEntityExternalIDs(IRECSVConfiguration configuration,IDictionary dictionary, IEntityAnnotation entity,Map<Integer,String> externalID) throws DatabaseLoadDriverException, SQLException {
		if(entity.getResourceElementID() > 0)
		{
			if(!externalID.containsKey(entity.getResourceElementID()))
			{
				List<IExternalID> extIDs = dictionary.getexternalIDandSorceIDandSource(entity.getResourceElementID());
				if(extIDs.size() > 0)
				{
					String strExternalIds = new String();
					for(IExternalID extID: extIDs)
					{
						strExternalIds = strExternalIds + configuration.getEntityExternalIDMainDelimiter().getValue() + extID.getExternalID() + configuration.getEntityExternalIDIntraDelimiter().getValue() + extID.getSource();
					}
					
					externalID.put(entity.getResourceElementID(), strExternalIds.substring(1));
				}
				else
				{
					externalID.put(entity.getResourceElementID(), null);
				}
			}

				return externalID.get(entity.getResourceElementID());	
		}
		else
		{
			return null;
		}
	}

	private String getSentenceAnnotation(IRECSVConfiguration configuration, IAnnotatedDocument docAnnot,IEventAnnotation ev) throws SQLException, DatabaseLoadDriverException {
		long startOffset = getStartRelationOffset(ev);
		long endOffset = getEndRelationOffset(ev);
		return configuration.getTextDelimiter().getValue() + getSentence(docAnnot,startOffset,endOffset) + configuration.getTextDelimiter().getValue() ;
	}
	
	private long getStartRelationOffset(IEventAnnotation ev) {
		long startOffsets = ev.getStartOffset();
		for(IEntityAnnotation lentities :ev.getEntitiesAtLeft())
		{
			if(startOffsets > lentities.getStartOffset())
			{
				startOffsets = lentities.getStartOffset();
			}
		}
		return startOffsets;
	}
	
	private long getEndRelationOffset(IEventAnnotation ev) {
		long endOffsets = ev.getEndOffset();
		for(IEntityAnnotation lentities :ev.getEntitiesAtLeft())
		{
			if(endOffsets < lentities.getEndOffset())
			{
				endOffsets = lentities.getEndOffset();
			}
		}
		return endOffsets;
	}

	private String getSentence(IAnnotatedDocument annotDOc, long startOffset,long endOffset) throws SQLException, DatabaseLoadDriverException {
		List<ISentence> sentences = annotDOc.getSentencesText();
		ISentence sentenceInit = findSentence(sentences,(int)startOffset);	
		ISentence sentenceEnd = findSentence(sentences,(int)endOffset);
		int start = (int)sentenceInit.getStartOffset();
		int end = (int)sentenceEnd.getEndOffset();
		return annotDOc.getDocumetAnnotationText().substring(start,end);
	}
	
	private ISentence findSentence(List<ISentence> sentences, int offset) {
		for(ISentence set:sentences)
		{
			if(set.getStartOffset() <= offset && offset <= set.getEndOffset())
			{
				return set;
			}
		}		
		return null;
	}

	private String getEntitiesToString(IRECSVConfiguration configuration,List<IEntityAnnotation> entitiesAtLeft) {
		if(entitiesAtLeft.size()==0)
			return null;
		String result = new String();
		for(IEntityAnnotation ent : entitiesAtLeft)
			result = result + ent.getAnnotationValue() + configuration.getEntityDelimiter().getValue();
		result = result.substring(0, result.length()-configuration.getEntityDelimiter().getValue().length());
		return configuration.getTextDelimiter().getValue() + result + configuration.getTextDelimiter().getValue();
	}

	private String getLineToWrite(IRECSVConfiguration configuration,String[] toWrite) {
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
