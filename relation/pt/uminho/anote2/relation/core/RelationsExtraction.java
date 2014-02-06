package pt.uminho.anote2.relation.core;

import gate.util.GateException;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import process.IGatePosTagger;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.annotation.IEventProperties;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IDocumentSet;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.annotation.EntityAnnotation;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesAnnotatedDocument;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesIEProcess;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.textprocessing.NormalizationForm;
import pt.uminho.anote2.datastructures.utils.FileHandling;
import pt.uminho.anote2.datastructures.utils.GenericPair;
import pt.uminho.anote2.datastructures.utils.GenericTriple;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.anote2.process.IE.INERProcess;
import pt.uminho.anote2.process.IE.IREProcess;
import pt.uminho.anote2.process.IE.re.IRelationModel;
import pt.uminho.anote2.process.IE.re.IVerbInfo;
import pt.uminho.anote2.relation.datastructures.POSTaggerHelp;


public class RelationsExtraction extends IEProcess implements IREProcess{
	
	protected static final int characteres = 200000;
	private IRelationModel relationModel;
	private IGatePosTagger posTagger;
	private ICorpus corpus;
	private INERProcess nerProcess;
	private Properties propeties;
	private PreparedStatement insertAnnot;
	private PreparedStatement insertEventAnnot;
	private PreparedStatement insertAnnotSide;
	private PreparedStatement insertEventProperty;
	private Map<Integer, Integer> idNEwIDoldProcess;
	private int relationsAnnotated = 0;
	private int entitiesAssociation = 0;
	
	public RelationsExtraction(ICorpus corpus,INERProcess nerProcess, IGatePosTagger postagger, IRelationModel relationModel2,Properties properties)
	{
		super(corpus,"Rel@tion RE", "RE", properties, ((Corpus) corpus).getCorpora().getDb());
		this.nerProcess=nerProcess;
		this.posTagger=postagger;
		this.relationModel=relationModel2;
		this.propeties = properties;
		this.corpus=corpus;
		try {
			this.insertAnnot = ((Corpus) corpus).getCorpora().getDb().getConnection().prepareStatement(QueriesAnnotatedDocument.insertEntityAnnotation);
			insertAnnot.setInt(1,this.getID());
			insertAnnot.setInt(2,corpus.getID());
			insertEventAnnot = ((Corpus) corpus).getCorpora().getDb().getConnection().prepareStatement(QueriesAnnotatedDocument.insertEventAnnotation);
			insertAnnotSide = ((Corpus) corpus).getCorpora().getDb().getConnection().prepareStatement(QueriesAnnotatedDocument.insertAnnotationSide);
			insertEventAnnot.setInt(1,this.getID());
			insertEventAnnot.setInt(2,corpus.getID());
			insertEventProperty = ((Corpus) corpus).getCorpora().getDb().getConnection().prepareStatement(QueriesAnnotatedDocument.insertAnnotationProperties);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void insertEventAnnotationInDatabase(int documentID,List<IEntityAnnotation> semanticLayer,List<IEventAnnotation> relations) throws SQLException {
		insertEntityAnnotationsDB(documentID,semanticLayer);
		insertRelationsDB(documentID,relations);

		entitiesAssociation = entitiesAssociation + semanticLayer.size();
		relationsAnnotated = relationsAnnotated + relations.size();
	}

	private void insertRelationsDB(int documentID, List<IEventAnnotation> relations) throws SQLException {
		int eventAnnotationID;
		List<IEntityAnnotation> left,right;
		IEventProperties prop;
		for(IEventAnnotation event:relations)
		{
			insertEventAnnot.setInt(3,documentID);
			insertEventAnnot.setInt(4, (int) event.getStartOffset());
			insertEventAnnot.setInt(5, (int) event.getEndOffset());
			insertEventAnnot.setNull(6,1);
			insertEventAnnot.setNull(7,1);
			insertEventAnnot.setNull(8,1);
			insertEventAnnot.setNull(9,1);
			insertEventAnnot.setNull(10,1);
			insertEventAnnot.setString(11,event.getEventClue());
			insertEventAnnot.execute();
			eventAnnotationID = HelpDatabase.getNextInsertTableID(((Corpus) corpus).getCorpora().getDb(),"annotations")-1;
			left = event.getEntitiesAtLeft();
			right = event.getEntitiesAtRight();
			insertentitiesAtLeft(eventAnnotationID,left);
			insertentitiesAtRight(eventAnnotationID,right);
			prop = event.getEventProperties();
			insertRelationsProperties(eventAnnotationID,prop);
		}
	}
	
	private void insertRelationsProperties(int eventAnnotationID, IEventProperties prop) throws SQLException {
		insertEventProperty.setInt(1,eventAnnotationID);
		if(prop.getLemma()!=null)
		{
			insertEventProperty.setString(2,"lemma");
			insertEventProperty.setString(3,prop.getLemma());
			insertEventProperty.execute();
		}
		if(prop.isDirectionally()!=null)
		{
			insertEventProperty.setString(2,"directionally");
			insertEventProperty.setString(3,String.valueOf(prop.isDirectionally()));
			insertEventProperty.execute();
		}
		if(prop.getPolarity()!=null)
		{
			insertEventProperty.setString(2,"polarity");
			insertEventProperty.setString(3,String.valueOf(prop.getPolarity()));
			insertEventProperty.execute();
		}	
	}

	private void insertentitiesAtLeft(int eventAnnotationID,List<IEntityAnnotation> left) throws SQLException {
		for(IEntityAnnotation annotation:left)
		{
			insertAnnotSide.setInt(1,eventAnnotationID);
			insertAnnotSide.setInt(2,idNEwIDoldProcess.get(annotation.getID()));
			insertAnnotSide.setInt(3,1);
			insertAnnotSide.execute();	
		}
	}
	
	private void insertentitiesAtRight(int eventAnnotationID,List<IEntityAnnotation> left) throws SQLException {
		for(IEntityAnnotation annotation:left)
		{
			insertAnnotSide.setInt(1,eventAnnotationID);
			insertAnnotSide.setInt(2,idNEwIDoldProcess.get(annotation.getID()));
			insertAnnotSide.setInt(3,2);
			insertAnnotSide.execute();	
		}
		
	}

	private void insertEntityAnnotationsDB(int documentID, List<IEntityAnnotation> semanticLayer) throws SQLException {
		insertAnnot.setInt(3,documentID);
		int nextAnnotaionID = HelpDatabase.getNextInsertTableID(((Corpus) corpus).getCorpora().getDb(),"annotations");
		for(IEntityAnnotation ent:semanticLayer)
		{
			insertAnnot.setLong(4,ent.getStartOffset());
			insertAnnot.setLong(5,ent.getEndOffset());
			insertAnnot.setString(6,ent.getAnnotationValue());
			insertAnnot.setNull(7,1);
			insertAnnot.setString(8,NormalizationForm.getNormalizationForm(ent.getAnnotationValue()));
			insertAnnot.setInt(9,ent.getClassAnnotationID());
			insertAnnot.execute();
			idNEwIDoldProcess.put(ent.getID(),nextAnnotaionID);
			nextAnnotaionID++;
		}
	}


	private List<IEntityAnnotation> getNERProcessEntityAnnotationsSentence(int DocumentID ,int corpusID,int nerProcessID, Long start,Long end) throws SQLException {
		PreparedStatement ps = ((Corpus) corpus).getCorpora().getDb().getConnection().prepareStatement(QueriesIEProcess.selectAnnotationFilterByOffset);
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
		return entitiesAnnot;
	}

	public String getDescription() {
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
	
	public IDocumentSet getDocuments() {
		return corpus.getArticlesCorpus();
	}
	
	
	public void setDocuments(IDocumentSet documents) {

	}
	
	public String getType() {
		return "RE";
	}
	@Override
	public IDatabase getDB() {
		return ((Corpus) corpus).getCorpora().getDb();
	}

	@Override
	public void executeRE() {
		try {
			if(corpus.getProperties().get("textType").equals("abstract"))
			{
				abstractProcessing();
			}
			else if(corpus.getProperties().get("textType").equals("full text"))
			{
				fullTextProcessing();
			}
			else if(corpus.getProperties().get("textType").equals("abstract OR full text"))
			{
				fullOrAbstractProcessing();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	protected void fullOrAbstractProcessing() throws IOException, GateException, SQLException {
		IDocumentSet docs = corpus.getArticlesCorpus();
		Iterator<IDocument> itDocs =docs.iterator();
		StringBuffer stringWhitManyDocuments = new StringBuffer();
		int size = 0;
		while(itDocs.hasNext())
		{
			IDocument doc = itDocs.next();
			IPublication pub = (IPublication) doc;
			String fullText = doc.getFullTextFromDatabase(((Corpus) getCorpus()).getCorpora().getDb());
			if(fullText.equals("")||fullText==null)
			{
				fullText = pub.getAbstractSection();
			}
			if(fullText==null)
			{
				fullText = "";
			}
			int lenthText = fullText.length();
			size = size+lenthText;
			if(size>characteres)
			{
				stringWhitManyDocuments.append("<Doc id=\""+doc.getID()+"\"> "+Utils.treatSentenceForXMLProblems(fullText)+"</Doc>");
				documetsRelationExtraction(stringWhitManyDocuments);
				stringWhitManyDocuments = new StringBuffer();
				size=0;
			}
			else
			{
				stringWhitManyDocuments.append("<Doc id=\""+doc.getID()+"\"> "+Utils.treatSentenceForXMLProblems(fullText)+"</Doc>");
			}
		}
		if(stringWhitManyDocuments.length()>0)
		{
			documetsRelationExtraction(stringWhitManyDocuments);
		}
	}

	protected void fullTextProcessing() throws IOException, GateException, SQLException {
		IDocumentSet docs = corpus.getArticlesCorpus();
		Iterator<IDocument> itDocs =docs.iterator();
		StringBuffer stringWhitManyDocuments = new StringBuffer();
		int size = 0;
		while(itDocs.hasNext())
		{
			IDocument doc = itDocs.next();
			String fulltext = doc.getFullTextFromDatabase(((Corpus) getCorpus()).getCorpora().getDb());
			int lenthText = fulltext.length();
			size = size+lenthText;
			if(size>characteres)
			{
				stringWhitManyDocuments.append("<Doc id=\""+doc.getID()+"\"> "+Utils.treatSentenceForXMLProblems(fulltext)+"</Doc>");
				documetsRelationExtraction(stringWhitManyDocuments);
				stringWhitManyDocuments = new StringBuffer();
				size=0;
			}
			else
			{
				stringWhitManyDocuments.append("<Doc id=\""+doc.getID()+"\"> "+Utils.treatSentenceForXMLProblems(fulltext)+"</Doc>");
			}
		}
		if(stringWhitManyDocuments.length()>0)
		{
			documetsRelationExtraction(stringWhitManyDocuments);
		}
	}

	protected void abstractProcessing() throws IOException, GateException, SQLException {
		IDocumentSet docs = corpus.getArticlesCorpus();
		Iterator<IDocument> itDocs =docs.iterator();
		StringBuffer stringWhitManyDocuments = new StringBuffer();
		int size = 0;
		while(itDocs.hasNext())
		{
			IDocument doc = itDocs.next();
			String abstractText = ((IPublication) doc).getAbstractSection();
			int lenthText = abstractText.length();
			size = size+lenthText;
			if(size>characteres)
			{
				stringWhitManyDocuments.append("<Doc id=\""+doc.getID()+"\"> "+Utils.treatSentenceForXMLProblems(abstractText)+"</Doc>");
				documetsRelationExtraction(stringWhitManyDocuments);
				stringWhitManyDocuments = new StringBuffer();
				size=0;
			}
			else
			{
				stringWhitManyDocuments.append("<Doc id=\""+doc.getID()+"\"> "+Utils.treatSentenceForXMLProblems(abstractText)+"</Doc>");
			}
		}
		if(stringWhitManyDocuments.length()>0)
		{
			documetsRelationExtraction(stringWhitManyDocuments);
		}
		
	}

	protected void documetsRelationExtraction(StringBuffer stringWhitManyDocuments) throws GateException, SQLException, IOException {
		Set<String> termionations = relationModel.getRelationTerminations();
		idNEwIDoldProcess = new HashMap<Integer, Integer>();
		File fileTmp = new File("fileTmp.xml");
		String fileText = "<Docs>"+stringWhitManyDocuments.toString()+"</Docs>";
		FileHandling.writeInformationOnFile(fileTmp,fileText);
		posTagger.completePLSteps(fileTmp);
		List<GenericTriple<Long, Long, Integer>> documentsLimit = POSTaggerHelp.getGateDocumentlimits(posTagger.getGateDoc());
		for(GenericTriple<Long, Long, Integer> docProcessing:documentsLimit)
		{
			List<GenericPair<Long, Long>> sentencesLimits = POSTaggerHelp.getGateDocumentSentencelimits(posTagger.getGateDoc(),docProcessing.getX(),docProcessing.getY());	
			GenericPair<List<IVerbInfo>, List<Long>> sintaticLayer;
			List<IEntityAnnotation> semanticLayer;
			List<IEventAnnotation> relations;
			for(GenericPair<Long, Long> setenceLimits:sentencesLimits)
			{			
				sintaticLayer = posTagger.getSentenceSintaticLayer(termionations,setenceLimits,docProcessing.getX());
				semanticLayer = getNERProcessEntityAnnotationsSentence(docProcessing.getZ(),corpus.getID(),nerProcess.getID(),setenceLimits.getX()-docProcessing.getX(),setenceLimits.getY()-docProcessing.getX());
				relations = relationModel.extractSentenceRelation(semanticLayer, sintaticLayer);
				insertEventAnnotationInDatabase(docProcessing.getZ(),semanticLayer,relations);
			}
		}
		posTagger.setGateDoc(null);
		System.gc();
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

	public INERProcess getNerProcess() {
		return nerProcess;
	}

	public int getRelationsAnnotated() {
		return relationsAnnotated;
	}

	public void setRelationsAnnotated(int relationsAnnotated) {
		this.relationsAnnotated = relationsAnnotated;
	}

	public int getEntitiesAssociation() {
		return entitiesAssociation;
	}

	public void setEntitiesAssociation(int entitiesAssociation) {
		this.entitiesAssociation = entitiesAssociation;
	}
	
}
