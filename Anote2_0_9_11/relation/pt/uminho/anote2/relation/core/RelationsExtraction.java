package pt.uminho.anote2.relation.core;

import gate.util.GateException;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.annotation.IEventProperties;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IDocumentSet;
import pt.uminho.anote2.core.report.processes.IREProcessReport;
import pt.uminho.anote2.core.utils.IGenericPair;
import pt.uminho.anote2.datastructures.annotation.ner.EntityAnnotation;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesAnnotatedDocument;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesIEProcess;
import pt.uminho.anote2.datastructures.documents.AnnotatedDocument;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.report.processes.REProcessReport;
import pt.uminho.anote2.datastructures.textprocessing.NormalizationForm;
import pt.uminho.anote2.datastructures.utils.FileHandling;
import pt.uminho.anote2.datastructures.utils.GenericPair;
import pt.uminho.anote2.datastructures.utils.GenericTriple;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.process.IE.IREProcess;
import pt.uminho.anote2.process.IE.re.IRelationModel;
import pt.uminho.anote2.process.IE.re.clue.IVerbInfo;
import pt.uminho.anote2.relation.datastructures.POSTaggerHelp;
import pt.uminho.gate.process.IGatePosTagger;


public class RelationsExtraction extends IEProcess implements IREProcess{
	
	protected static final int characteres = 500000;
	private IRelationModel relationModel;
	private IGatePosTagger posTagger;
	private ICorpus corpus;
	private IIEProcess ieProcess;
	private Properties propeties;
	private PreparedStatement insertAnnot;
	private PreparedStatement insertEventAnnot;
	private PreparedStatement insertAnnotSide;
	private PreparedStatement insertEventProperty;
	private Map<Integer, Integer> idNEwIDoldProcess;
	private boolean stop=false;
	private int nextAnnotationID;
	
	public RelationsExtraction(ICorpus corpus,IIEProcess ieProcess2, IGatePosTagger postagger, IRelationModel relationModel2,Properties properties)
	{
		super(corpus,GlobalNames.relation, GlobalNames.re, properties);
		this.ieProcess=ieProcess2;
		this.posTagger=postagger;
		this.relationModel=relationModel2;
		this.propeties = properties;
		this.corpus=corpus;
		try {
			initPS(corpus);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}

	private void initPS(ICorpus corpus) throws SQLException,DatabaseLoadDriverException {
		this.insertAnnot = Configuration.getDatabase().getConnection().prepareStatement(QueriesAnnotatedDocument.insertEntityAnnotation);
		insertAnnot.setInt(1,this.getID());
		insertAnnot.setInt(2,corpus.getID());
		insertEventAnnot = Configuration.getDatabase().getConnection().prepareStatement(QueriesAnnotatedDocument.insertEventAnnotation);
		insertAnnotSide = Configuration.getDatabase().getConnection().prepareStatement(QueriesAnnotatedDocument.insertAnnotationSide);
		insertEventAnnot.setInt(1,this.getID());
		insertEventAnnot.setInt(2,corpus.getID());
		insertEventProperty = Configuration.getDatabase().getConnection().prepareStatement(QueriesAnnotatedDocument.insertAnnotationProperties);
	}
	
	private void insertEventAnnotationInDatabase(IREProcessReport report, int documentID,List<IEntityAnnotation> semanticLayer,List<IEventAnnotation> relations) throws SQLException {
		insertEntityAnnotationsDB(documentID,semanticLayer);
		insertRelationsDB(documentID,relations);
		report.incrementEntitiesAnnotated(semanticLayer.size());
		report.increaseRelations(relations.size());
	}

	private void insertRelationsDB(int documentID, List<IEventAnnotation> relations) throws SQLException {
		List<IEntityAnnotation> left,right;
		IEventProperties prop;
		for(IEventAnnotation event:relations)
		{
			if(stop)
			{
				break;
			}
			insertEventAnnot.setInt(3,documentID);
			insertEventAnnot.setInt(4, (int) event.getStartOffset());
			insertEventAnnot.setInt(5, (int) event.getEndOffset());
			insertEventAnnot.setNull(6,1);
			insertEventAnnot.setNull(7,1);
			insertEventAnnot.setNull(8,1);
			insertEventAnnot.setNull(9,1);
			insertEventAnnot.setNull(10,1);
			insertEventAnnot.setNString(11,event.getEventClue());
			insertEventAnnot.execute();
			left = event.getEntitiesAtLeft();
			right = event.getEntitiesAtRight();
			insertentitiesAtLeft(left);
			insertentitiesAtRight(right);
			prop = event.getEventProperties();
			insertRelationsProperties(prop);
			nextAnnotationID ++;
		}
	}
	
	private void insertRelationsProperties(IEventProperties prop) throws SQLException {
		insertEventProperty.setInt(1,nextAnnotationID);
		if(prop.getLemma()!=null && !stop)
		{
			insertEventProperty.setNString(2,GlobalNames.relationPropertyLemma);
			insertEventProperty.setNString(3,prop.getLemma());
			insertEventProperty.execute();
		}
		if(prop.getDirectionally()!=null && !stop)
		{
			insertEventProperty.setNString(2,GlobalNames.relationPropertyDirectionally);
			insertEventProperty.setNString(3,String.valueOf(prop.getDirectionally().databaseValue()));
			insertEventProperty.execute();
		}
		if(prop.getPolarity()!=null && !stop)
		{
			insertEventProperty.setNString(2,GlobalNames.relationPropertyPolarity);
			insertEventProperty.setNString(3,String.valueOf(prop.getPolarity().databaseValue()));
			insertEventProperty.execute();
		}	
	}

	private void insertentitiesAtLeft(List<IEntityAnnotation> left) throws SQLException {
		for(IEntityAnnotation annotation:left)
		{
			if(stop)
			{
				break;
			}
			insertAnnotSide.setInt(1,nextAnnotationID);
			insertAnnotSide.setInt(2,idNEwIDoldProcess.get(annotation.getID()));
			insertAnnotSide.setInt(3,1);
			insertAnnotSide.execute();	
		}
	}
	
	private void insertentitiesAtRight(List<IEntityAnnotation> left) throws SQLException {
		for(IEntityAnnotation annotation:left)
		{
			if(stop)
			{
				break;
			}
			insertAnnotSide.setInt(1,nextAnnotationID);
			insertAnnotSide.setInt(2,idNEwIDoldProcess.get(annotation.getID()));
			insertAnnotSide.setInt(3,2);
			insertAnnotSide.execute();	
		}
		
	}

	private void insertEntityAnnotationsDB(int documentID, List<IEntityAnnotation> semanticLayer) throws SQLException {
		insertAnnot.setInt(3,documentID);
		for(IEntityAnnotation ent:semanticLayer)
		{
			if(stop)
			{
				break;
			}
			insertAnnot.setLong(4,ent.getStartOffset());
			insertAnnot.setLong(5,ent.getEndOffset());
			insertAnnot.setNString(6,ent.getAnnotationValue());
			if(ent.getResourceElementID()>0)
			{
				insertAnnot.setInt(7,ent.getResourceElementID());
			}
			else
			{
				insertAnnot.setNull(7,1);
			}
			insertAnnot.setNString(8,NormalizationForm.getNormalizationForm(ent.getAnnotationValue()));
			insertAnnot.setInt(9,ent.getClassAnnotationID());
			insertAnnot.execute();
			idNEwIDoldProcess.put(ent.getID(),nextAnnotationID);
			nextAnnotationID++;
		}
	}


	private List<IEntityAnnotation> getNERProcessEntityAnnotationsSentence(int DocumentID ,int corpusID,int nerProcessID, Long start,Long end) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesIEProcess.selectAnnotationFilterByOffset);
		ps.setInt(1,nerProcessID);
		ps.setInt(2,corpusID);
		ps.setInt(3,DocumentID);
		ps.setLong(4, start);
		ps.setLong(5, end);
		ResultSet rs = ps.executeQuery();
		IEntityAnnotation entAnnot;
		List<IEntityAnnotation> entitiesAnnot = new ArrayList<IEntityAnnotation>();
		int id,startEnt,endEnt,classID,resourceID;
		String value,normValue;
		while(rs.next())
		{
			id = rs.getInt(1);
			startEnt = rs.getInt(2);
			endEnt = rs.getInt(3);
			value = rs.getString(4);
			resourceID = rs.getInt(5);
			normValue = rs.getString(6);
			classID = rs.getInt(7);
			entAnnot = new EntityAnnotation(id, startEnt, endEnt, classID,resourceID, value,normValue);
			entitiesAnnot.add(entAnnot);		
		}
		rs.close();
		ps.close();
		return entitiesAnnot;
	}

	public String getName() {
		return "Rel@tioN @Note2";
	}
	
	public Properties getProperties() {
		return propeties;
	}
	
	public ICorpus getAssociativeCorpus() {
		return corpus;
	}
	
	public void setAssociativeCorpus(ICorpus corpus) {
		this.corpus=corpus;
		
	}
	
	public IDocumentSet getDocuments() throws SQLException, DatabaseLoadDriverException {
		return corpus.getArticlesCorpus();
	}
	
	
	public void setDocuments(IDocumentSet documents) {

	}
	
	public String getType() {
		return GlobalNames.re;
	}

	public IREProcessReport executeRE() throws  SQLException, DatabaseLoadDriverException,Exception {
		IREProcessReport report = new REProcessReport(GlobalNames.relation, ieProcess,this);
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		relationProcessing(report);
		long end = GregorianCalendar.getInstance().getTimeInMillis();
		report.setTime(end-startTime);
		return report;
	}

	protected void relationProcessing(IREProcessReport report) throws IOException, GateException, SQLException, DatabaseLoadDriverException {
		IDocumentSet docs = getCorpus().getArticlesCorpus();
		Iterator<IDocument> itDocs =docs.iterator();
		int max = docs.size();
		StringBuffer stringWhitManyDocuments = new StringBuffer();
		int size = 0;
		int docInDocumentGate = 0;
		int position = 0;
		long starttime = GregorianCalendar.getInstance().getTimeInMillis();
		nextAnnotationID = HelpDatabase.getNextInsertTableID(GlobalTablesName.annotations); 
		while(itDocs.hasNext())
		{
			if(stop)
			{
				report.setcancel();
				break;
			}
			docInDocumentGate++;
			IDocument doc = itDocs.next();
			IAnnotatedDocument annotDoc = new AnnotatedDocument(this, corpus, doc);
			String text = annotDoc.getDocumetAnnotationText();
			size = size+text.length();
			if(size>characteres && !stop)
			{
				stringWhitManyDocuments.append("<Doc id=\""+doc.getID()+"\">"+Utils.treatSentenceForXMLProblems(text)+"</Doc>");
				documetsRelationExtraction(report,stringWhitManyDocuments);
				stringWhitManyDocuments = new StringBuffer();
				size=0;
				position = position + docInDocumentGate;
				docInDocumentGate=0;
				memoryAndProgressAndTime(position, max,starttime);
			}
			else if(stop)
			{
				report.setcancel();
				break;
			}
			else
			{
				stringWhitManyDocuments.append("<Doc id=\""+doc.getID()+"\">"+Utils.treatSentenceForXMLProblems(text)+"</Doc>");
			}
			report.incrementDocument();
		}
		if(stringWhitManyDocuments.length()>0 && !stop)
		{
			documetsRelationExtraction(report,stringWhitManyDocuments);
		}
		cleanDocument();
	}

	private void cleanDocument() throws IOException, GateException {	
		posTagger.cleanALL();
	}

	protected void documetsRelationExtraction(IREProcessReport report, StringBuffer stringWhitManyDocuments) throws GateException, SQLException, IOException, DatabaseLoadDriverException {
		Set<String> termionations = relationModel.getRelationTerminations();
		idNEwIDoldProcess = new HashMap<Integer, Integer>();
		if(!stop)
		{
			File fileTmp = new File("fileTmp.xml");
			String fileText = "<Docs>"+stringWhitManyDocuments.toString()+"</Docs>";
			FileHandling.writeInformationOnFile(fileTmp,fileText);
			posTagger.completePLSteps(fileTmp);
			List<GenericTriple<Long, Long, Integer>> documentsLimit = POSTaggerHelp.getGateDocumentlimits(posTagger.getGateDoc());
			for(GenericTriple<Long, Long, Integer> docProcessing:documentsLimit)
			{
				if(stop)
				{
					report.setcancel();
					break;
				}
				long documentStartOffset = docProcessing.getX();
				long documentEndOffset = docProcessing.getY();
				int documentID = docProcessing.getZ();
				IDocument doc = getCorpus().getArticlesCorpus().getDocument(documentID);
				IAnnotatedDocument annotDoc = new AnnotatedDocument(this, corpus, doc);
				List<GenericPair<Long, Long>> sentencesLimits = POSTaggerHelp.getGateDocumentSentencelimits(posTagger.getGateDoc(),documentStartOffset,documentEndOffset);	
				IGenericPair<List<IVerbInfo>, List<Long>> sintaticLayer;
				List<IEntityAnnotation> semanticLayer;
				List<IEventAnnotation> relations;
				for(GenericPair<Long, Long> setenceLimits:sentencesLimits)
				{
					long sentenceStartOffset = setenceLimits.getX();
					long sentenceEndOffset = setenceLimits.getY();
					sintaticLayer = posTagger.getSentenceSintaticLayer(termionations,setenceLimits,documentStartOffset);
					semanticLayer = getNERProcessEntityAnnotationsSentence(documentID,corpus.getID(),ieProcess.getID(),sentenceStartOffset-documentStartOffset,sentenceEndOffset-documentStartOffset);
					relations = relationModel.extractSentenceRelation(annotDoc,semanticLayer, sintaticLayer);
					insertEventAnnotationInDatabase(report,documentID,semanticLayer,relations);
				}
			}
			fileTmp.delete();
		}
	}

	public IRelationModel getRelationModel() {
		return relationModel;
	}

	public IGatePosTagger getPosTagger() {
		return posTagger;
	}

	public ICorpus getCorpus() {
		return corpus;
	}

	public IIEProcess getNerProcess() {
		return ieProcess;
	}

	public void stop() {
		this.stop = true;
		
	}	

	public void closePS()
	{
		try {
			if(insertAnnot!=null)
				insertAnnot.close();
			if(insertEventAnnot!=null)
				insertEventAnnot.close();
			if(insertAnnotSide!=null)
				insertAnnotSide.close();
			if(insertEventProperty!=null)
				insertEventProperty.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
