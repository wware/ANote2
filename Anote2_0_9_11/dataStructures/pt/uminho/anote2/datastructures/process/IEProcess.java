package pt.uminho.anote2.datastructures.process;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Properties;

import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IDocumentSet;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.annotation.AnnotationPosition;
import pt.uminho.anote2.datastructures.annotation.AnnotationPositions;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesAnnotatedDocument;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesProcess;
import pt.uminho.anote2.datastructures.documents.AnnotatedDocument;
import pt.uminho.anote2.datastructures.textprocessing.NormalizationForm;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.process.IProcess;
import pt.uminho.anote2.process.IE.IIEProcess;

public class IEProcess extends Observable implements IIEProcess {

	private String name;
	private int id;
	private Properties properties;
	private String type;
	private ICorpus corpus;

	public IEProcess(ICorpus corpus,String description,String type,Properties properties)
	{
		try {
			this.id=registerNERProcess(description,type, properties);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DatabaseLoadDriverException e) {
			e.printStackTrace();
		}
		this.name=description;
		this.type=type;
		this.properties=properties;
		this.corpus=corpus;
	}
	public IEProcess(int id,ICorpus corpus,String description,String type,Properties properties)
	{
		this.id=id;
		this.name=description;
		this.type=type;
		this.properties=properties;
		this.corpus=corpus;
	}

	protected int registerNERProcess(String description,String type, Properties properties) throws SQLException, DatabaseLoadDriverException {
		int processID = IEProcess.insertProcessType(type);
		if(processID==-1)
		{
			return -1;
		}
		return IEProcess.insertProcess(description,processID, properties);
	}


	public String getName() {
		return this.name;
	}

	public int getID() {
		return this.id;
	}

	public Properties getProperties() {
		return this.properties;
	}

	public IDocumentSet getDocuments() throws SQLException, DatabaseLoadDriverException {
		return corpus.getArticlesCorpus();
	}

	public static int insertProcessType(String processType) throws SQLException, DatabaseLoadDriverException
	{
		int result = -1;
		PreparedStatement prep = Configuration.getDatabase().getConnection().prepareStatement(QueriesProcess.existProcessType);
		prep.setNString(1,processType);
		ResultSet rs = prep.executeQuery();
		if(rs.next())
		{
			result = rs.getInt(1);
		}
		else
		{
			PreparedStatement insType = Configuration.getDatabase().getConnection().prepareStatement(QueriesProcess.insertProcessType);
			insType.setNString(1, processType);
			insType.execute();
			insType.close();
			result = HelpDatabase.getNextInsertTableID(GlobalTablesName.processType)-1;
		}
		rs.close();
		prep.close();
		return result;
	}

	public static int insertProcess(String designation,int processTypeID,Properties properties) throws SQLException, DatabaseLoadDriverException
	{
		PreparedStatement prep = Configuration.getDatabase().getConnection().prepareStatement(QueriesProcess.insertProcess);
		prep.setInt(1,processTypeID);
		prep.setNString(2,designation);
		int processID = HelpDatabase.getNextInsertTableID(GlobalTablesName.processes);
		prep.execute();
		insertProcessProperties(processID,properties);
		prep.close();
		return processID;
	}

	private static void insertProcessProperties(int processID,Properties properties) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement insertP;
		insertP = Configuration.getDatabase().getConnection().prepareStatement(QueriesProcess.insertProcessProperties);
		for(String key :properties.stringPropertyNames())
		{
			String value = properties.get(key).toString();
			insertP.setInt(1, processID);
			insertP.setNString(2, key);
			insertP.setNString(3, value);
			insertP.execute();
		}
		insertP.close();

	}

	public String getType() {

		return this.type;
	}

	public int compareTo(IEProcess process)
	{
		if(this.id==process.getID())
		{
			return 0;
		}
		else if(this.id<process.getID())
		{
			return -1;
		}
		else
		{
			return 1;
		}
	}

	synchronized public Map<Integer,IEntityAnnotation> getAllEntities() throws SQLException, DatabaseLoadDriverException {
		Map<Integer,IEntityAnnotation> entities = new HashMap<Integer, IEntityAnnotation>();
		Iterator<IDocument> annotDocsIt = corpus.getArticlesCorpus().iterator();
		while(annotDocsIt.hasNext())
		{
			IPublication annotDoc = (IPublication) annotDocsIt.next();
			IAnnotatedDocument annotatedDocument = new AnnotatedDocument(this, corpus, annotDoc);
			List<IEntityAnnotation> annots = annotatedDocument.getEntitiesAnnotations();
			for(IEntityAnnotation annot:annots)
			{
				entities.put(annot.getID(), annot);
			}
		}
		return entities;
	}

	synchronized public Map<Integer,IEventAnnotation> getAllEvents() throws SQLException, DatabaseLoadDriverException {
		Map<Integer,IEventAnnotation> events = new HashMap<Integer, IEventAnnotation>();
		Iterator<IDocument> annotDocsIt = corpus.getArticlesCorpus().iterator();
		while(annotDocsIt.hasNext())
		{
			IPublication annotDoc = (IPublication) annotDocsIt.next();
			IAnnotatedDocument annotatedDocument = new AnnotatedDocument(this, corpus, annotDoc);
			List<IEventAnnotation> annots = annotatedDocument.getEventAnnotations();
			for(IEventAnnotation annot:annots)
			{
				events.put(annot.getID(), annot);
			}
		}
		return events;
	}	

	public String toString()
	{
		String all = new String();
		all = getID() + "-" + getName() + "(" + getType() + ")" ;
		return all;
	}

	public int getId() {
		return id;
	}


	public ICorpus getCorpus() {
		return corpus;
	}

	public static void insertOnDatabaseEntityAnnotations(IDocument doc,IProcess process,ICorpus corous, AnnotationPositions annotationsPositions) throws SQLException, DatabaseLoadDriverException {

		IEntityAnnotation annot;
		PreparedStatement insertAnnot =  Configuration.getDatabase().getConnection().prepareStatement(QueriesAnnotatedDocument.insertEntityAnnotation);
		insertAnnot.setInt(1,process.getID());
		insertAnnot.setInt(2,corous.getID());
		insertAnnot.setInt(3,doc.getID());
		for(AnnotationPosition annotPos :annotationsPositions.getAnnotations().keySet())
		{
			insertAnnot.setInt(4,annotPos.getStart());
			insertAnnot.setInt(5,annotPos.getEnd());
			annot = (IEntityAnnotation) annotationsPositions.getAnnotations().get(annotPos);
			insertAnnot.setNString(6,annot.getAnnotationValue());
			if(annot.getResourceElementID()>0)
			{
				insertAnnot.setInt(7,annot.getResourceElementID());
			}
			else
			{
				insertAnnot.setNull(7,annot.getResourceElementID());
			}
			insertAnnot.setNString(8,NormalizationForm.getNormalizationForm(annot.getAnnotationValue()));
			insertAnnot.setInt(9,annot.getClassAnnotationID());
			insertAnnot.execute();
		}
		insertAnnot.close();

	}

	public static void insertOnDatabaseEntityAnnotations(int docID,int processID,ICorpus corous, AnnotationPositions annotationsPositions) throws SQLException, DatabaseLoadDriverException {

		Connection conn = Configuration.getDatabase().getConnection();
		IEntityAnnotation annot;
		PreparedStatement insertAnnot = null;
		insertAnnot = conn.prepareStatement(QueriesAnnotatedDocument.insertEntityAnnotation);
		insertAnnot.setInt(1,processID);
		insertAnnot.setInt(2,corous.getID());
		insertAnnot.setInt(3,docID);
		for(AnnotationPosition annotPos :annotationsPositions.getAnnotations().keySet())
		{
			insertAnnot.setInt(4,annotPos.getStart());
			insertAnnot.setInt(5,annotPos.getEnd());
			annot = (IEntityAnnotation) annotationsPositions.getAnnotations().get(annotPos);
			insertAnnot.setNString(6,annot.getAnnotationValue());
			if(annot.getResourceElementID()>0)
			{
				insertAnnot.setInt(7,annot.getResourceElementID());
			}
			else
			{
				insertAnnot.setNull(7,annot.getResourceElementID());
			}
			insertAnnot.setNString(8,NormalizationForm.getNormalizationForm(annot.getAnnotationValue()));
			insertAnnot.setInt(9,annot.getClassAnnotationID());
			insertAnnot.execute();
		}
		insertAnnot.close();
	}


	protected void memoryAndProgress(int step, int total) {
		System.out.println((GlobalOptions.decimalformat.format((double)step/ (double) total * 100)) + " %...");
		System.gc();
		System.out.println((Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory())/(1024*1024) + " MB ");
	}

	protected void memoryAndProgressAndTime(int step, int total,long startTime) {
		System.out.println((GlobalOptions.decimalformat.format((double)step/ (double) total * 100)) + " %...");
		System.gc();
		System.out.println((Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory())/(1024*1024) + " MB ");
	}
	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}
	@Override
	public void setName(String newNAme) {
		this.name = newNAme;
	}
	public void setNameInDatabse(String newName) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesProcess.updateProcessName);
		ps.setInt(2, getID());
		ps.setString(1, newName);
		ps.execute();
		ps.close();	
	}
	
}
