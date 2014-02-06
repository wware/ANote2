package pt.uminho.anote2.corpusloaders.loaders;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Gate;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;
import gate.util.InvalidOffsetException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import pt.uminho.anote2.aibench.corpus.databasemanagement.CorporaManagement;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.annotation.IEventProperties;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IDocumentSet;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.corpusloaders.ICorpusEventAnnotationLoader;
import pt.uminho.anote2.corpusloaders.utils.GateLoader;
import pt.uminho.anote2.datastructures.annotation.EntityAnnotation;
import pt.uminho.anote2.datastructures.annotation.EventAnnotation;
import pt.uminho.anote2.datastructures.documents.AnnotatedDocument;
import pt.uminho.anote2.datastructures.documents.DocumentSet;
import pt.uminho.anote2.datastructures.documents.Publication;
import pt.uminho.anote2.datastructures.textprocessing.NormalizationForm;
import pt.uminho.anote2.datastructures.utils.GenericPair;
import pt.uminho.anote2.datastructures.utils.GenericPairComparable;
import pt.uminho.anote2.datastructures.utils.GenericTriple;

public class GeniaEventCorpusLoader implements ICorpusEventAnnotationLoader{
	
	private File filepath;
	private int corpusSize;
	private int corpusLoaderPosition;
	private IDocumentSet documents;
	private IDatabase db;
	private Document gateDoc;
	private List<String> corruptFiles;
	private Set<String> validTags;
	private String fulltext;
	private List<IEntityAnnotation> entities;
	private List<IEventAnnotation> events;
	private Map<String,Integer> classesClassesID;

	public GeniaEventCorpusLoader(IDatabase db,File filepath)
	{
		this.db=db;
		this.filepath=filepath;
		this.corpusLoaderPosition = 1;
		this.documents = new DocumentSet();
		this.validTags = putValidTags();
		this.fulltext = new String();
		this.entities = new ArrayList<IEntityAnnotation>();
		this.events = new ArrayList<IEventAnnotation>();
		this.classesClassesID = new HashMap<String, Integer>();
		try {
			Gate.init();
		} catch (GateException e) {
			e.printStackTrace();
		}
	}
	
	private Set<String> putValidTags() {
		Set<String> validTags = new HashSet<String>();
		validTags.add("Annotation");
		validTags.add("PubmedArticleSet");
		validTags.add("PubmedArticle");
		validTags.add("MedlineCitation");
		validTags.add("PMID");
		validTags.add("Article");
		validTags.add("ArticleTitle");
		validTags.add("sentence");
		validTags.add("term");
		validTags.add("cons");
		validTags.add("event");
		validTags.add("type");
		validTags.add("theme");
		validTags.add("clue");
		validTags.add("cause");
		validTags.add("clueType");
		validTags.add("clueLoc");
		validTags.add("linkCause");
		validTags.add("linkTheme");
		validTags.add("comment");
		validTags.add("Abstract");
		validTags.add("frag");
		validTags.add("clueTime");
		validTags.add("AbstractText");
		validTags.add("site");
		validTags.add("corefTheme");
		validTags.add("clueExperiment");
		validTags.add("corefCause");
		validTags.add("product");
		validTags.add("corefSite");
		return validTags;
	}

	@Override
	public IDocumentSet processTextFile() {
		if(validateFile())
		{
			for(File file:filepath.listFiles())
			{
				if(!file.isDirectory())
				{
					try {
						IDocument doc = processFile(file);
						getDocuments().addDocument(corpusLoaderPosition, doc);
						corpusLoaderPosition++;
						this.entities = new ArrayList<IEntityAnnotation>();
						this.events = new ArrayList<IEventAnnotation>();
						this.fulltext = new String();
					} catch (ResourceInstantiationException e) {
						e.printStackTrace();
					} catch (InvalidOffsetException e) {
						e.printStackTrace();
					}
				}
			} 
			return 	getDocuments();
		}
		else
		{
			return null;
		}	
	}
	

	public List<IEntityAnnotation> getEntities() {
		return entities;
	}

	public List<IEventAnnotation> getEvents() {
		return events;
	}

	private IDocument processFile(File file) throws ResourceInstantiationException, InvalidOffsetException {
		gateDoc = GateLoader.createGateDocument(file);;
		AnnotationSet annotSetOriginal = gateDoc.getAnnotations("Original markups");
		String title = getDocumentTitle(gateDoc,annotSetOriginal);
		String abstractText = getDocumentAbstract(gateDoc,annotSetOriginal);
		String pmid = getDocumentPMID(gateDoc,annotSetOriginal);
		IPublication pub = new Publication(-1,pmid, title,"", "", "","", "", "", "","", abstractText, false);
		processeSentence(annotSetOriginal);
		IDocument docResult = new AnnotatedDocument(-1, pub,null, null,getEntityAnnotationsIN(),getEventAnnotaionIN(), -1);
		docResult.setFullTExt(getFulltext());
		gateDoc.cleanup();
		return docResult;
	}

	private void processeSentence(AnnotationSet annotSetOriginal) {

		long[][] sentencesLimits = GateLoader.getGenericOffsetLimits(annotSetOriginal,"sentence",true);
		long[][] sentencesWhitEventLimits = getSentenceLimits(annotSetOriginal);
		long sentenceIndex = sentencesLimits[0][0];
		long error = sentencesLimits[0][0];
		String sentenceText = new String();
		for(int i=0;i<sentencesWhitEventLimits.length;i++)
		{
			sentenceText = sentenceText + GateLoader.getPartGateDocument(gateDoc,sentencesLimits[i][0], sentencesLimits[i][1]);
			String[][] terms = processSentenceEntities(annotSetOriginal,sentencesLimits[i][0],sentencesLimits[i][1],sentenceIndex,error);
			processSentenceEvents(annotSetOriginal,sentencesWhitEventLimits[i][0],sentencesWhitEventLimits[i][1],sentenceIndex,error,terms);
			sentenceIndex = sentenceIndex+sentencesLimits[i][1]-sentencesLimits[i][0];
		}
		this.fulltext = sentenceText;
	}
	

	private void processSentenceEvents(AnnotationSet annotSetOriginal, long startsentenceEvent,long endsentenceEvent, long sentenceIndex, long error,String[][] terms) {

		AnnotationSet annotationSentence = annotSetOriginal.getContained(startsentenceEvent,endsentenceEvent);
		long[][] eventsOffset = GateLoader.getGenericOffsetLimits(annotationSentence, "event",true);
		long verbPositionEnd,verbPositionStart;
		IEventProperties eventProperties = null;
		IEventAnnotation ev=null;
		// eventID -> ArrayList<> lefrentities,ArrayList<> rigth entities
		Map<String, GenericPair<List<String>, List<String>>> hasheventwhitentities = new HashMap<String, GenericPair<List<String>,List<String>>>();
		// eventID -> ( Verb(clue),ontologyRelation,offsetStart)
		Map<String,GenericTriple<String,String,Long>> hashEventdetails = new HashMap<String, GenericTriple<String,String,Long>>();
		// ID Genia Event Entity ID-> EntityInfo
		Map<String, String[]> hashentities = getHashTermsIDentityID(terms);
		for(int i=eventsOffset.length-1;i>=0;i--)
		{
			getEventDetails(gateDoc, annotationSentence, eventsOffset,hasheventwhitentities, hashEventdetails,i);
		}
		Iterator<String> iteventID = hasheventwhitentities.keySet().iterator();
		while(iteventID.hasNext())
		{
			String eventID = iteventID.next();
			String verb = hashEventdetails.get(eventID).getX();
			verbPositionStart =  hashEventdetails.get(eventID).getZ();
			verbPositionEnd =  hashEventdetails.get(eventID).getZ()+verb.length();
			String ontologyRelationClass = hashEventdetails.get(eventID).getY();
			List<String> cause = hasheventwhitentities.get(eventID).getX();
			List<String> them = hasheventwhitentities.get(eventID).getY();
			List<IEntityAnnotation> causeEntities = getEntitiesList(cause,hasheventwhitentities,sentenceIndex,startsentenceEvent,hashentities,error);
			List<IEntityAnnotation> themEntities = getEntitiesList(them,hasheventwhitentities,sentenceIndex,startsentenceEvent,hashentities,error);
			GenericPair<List<IEntityAnnotation>,List<IEntityAnnotation>> entitiesResults = getEntitiesPosition(sentenceIndex+verbPositionStart-error, causeEntities, themEntities);
			ev = new EventAnnotation(-1, sentenceIndex+verbPositionStart-error,sentenceIndex+verbPositionEnd-error, "RE", entitiesResults.getX(), entitiesResults.getY(), verb, 0, ontologyRelationClass, eventProperties);
			this.events.add(ev);
		}		
	}
	
	private GenericPair<List<IEntityAnnotation>,List<IEntityAnnotation>> getEntitiesPosition(
			Long verbPosition,
			List<IEntityAnnotation> cause,
			List<IEntityAnnotation> them)
			{
		List<IEntityAnnotation> left = new ArrayList<IEntityAnnotation>();
		List<IEntityAnnotation> right = new ArrayList<IEntityAnnotation>();
		for(int i=0;i<cause.size();i++)
		{
			IEntityAnnotation pair = cause.get(i);
			if(pair.getStartOffset()<verbPosition)
				left.add(pair);
			else
				right.add(pair);
		}
		for(int i=0;i<them.size();i++)
		{
			IEntityAnnotation pair = them.get(i);
			if(pair.getStartOffset()<verbPosition)
				left.add(pair);
			else
				right.add(pair);
		}	
		return new GenericPair<List<IEntityAnnotation>,List<IEntityAnnotation>>(left,right);
	}
	
	private List<IEntityAnnotation> getEntitiesList(List<String> entitiesID, Map<String, GenericPair<List<String>, List<String>>> hasheventwhitentities,long sentenceIndex, long sentenceStaerEvent, Map<String, String[]> hashentities, long error) {
		List<IEntityAnnotation> list = new ArrayList<IEntityAnnotation>();
		Stack<String> stack = new Stack<String>();
		Set<String> haveProcessed = new HashSet<String>();
		IEntityAnnotation e;
		int classID;
		String[] info;
		String term;
		long offsetDiff;
		for(String id:entitiesID)
		{
			if(hashentities.get(id) == null)
			{
				stack.push(id);
			}
			else
			{
				info = hashentities.get(id);
				classID = classesClassesID.get(info[3]);
				term = info[1];
				offsetDiff = Long.valueOf(info[0])-sentenceStaerEvent;
				e = new EntityAnnotation(-1,sentenceIndex+offsetDiff-error, sentenceIndex+offsetDiff+term.length()-error, classID, 0, term, NormalizationForm.getNormalizationForm(term));
				list.add(e);
			}
		}
		while(stack.size() > 0){
			String currentId = stack.pop();
			if(hasheventwhitentities.get(currentId)!=null&&haveProcessed.contains(currentId)==false) // porque a sentence pode ser de outra frase e não esta na hash
			{
				haveProcessed.add(currentId);
				List<String> cause = hasheventwhitentities.get(currentId).getX();
				List<String> them = hasheventwhitentities.get(currentId).getY();
				for(String id2:cause)
					if(hashentities.get(id2) == null)
						stack.push(id2);
					else
					{
						info = hashentities.get(id2);
						classID = classesClassesID.get(info[3]);
						term = info[1];
						offsetDiff = Long.valueOf(info[0])-sentenceStaerEvent;
						e = new EntityAnnotation(-1,sentenceIndex+offsetDiff-error, sentenceIndex+offsetDiff+term.length()-error, classID, 0, term, NormalizationForm.getNormalizationForm(term));
						list.add(e);
					}
				for(String id3:them)
					if(hashentities.get(id3) == null)
						stack.push(id3);
					else
					{
						info = hashentities.get(id3);
						classID = classesClassesID.get(info[3]);
						term = info[1];
						offsetDiff = Long.valueOf(info[0])-sentenceStaerEvent;
						e = new EntityAnnotation(-1,sentenceIndex+offsetDiff-error, sentenceIndex+offsetDiff+term.length()-error, classID, 0, term, NormalizationForm.getNormalizationForm(term));
						list.add(e);
					}
			}
		}
		return list;
	}


	/**
	 * Method that receive ner annotations
	 * 
	 * @param annotations
	 * @return
	 */
	private Map<String,String[]> getHashTermsIDentityID(String[][] annotations)
	{
		HashMap<String,String[]> hashentities = new HashMap<String,String[]>();
		for(int i=0;i<annotations.length;i++)
		{
			hashentities.put(annotations[i][2],annotations[i]);
		}
		return hashentities;
	}

	/**
	 * Method that give event entities and event details (verb(clue) an ontological classification)
	 * 
	 * 
	 * @param gateDoc
	 * @param annotSentenceEvent
	 * @param eventsOffset
	 * @param hasheventwhitentities
	 * @param hashEventdetails
	 * @param i
	 */
	private void getEventDetails( Document gateDoc, AnnotationSet annotSentenceEvent, long[][] eventsOffset,
			Map<String, GenericPair<List<String>, List<String>>> hasheventwhitentities,
			Map<String, GenericTriple<String, String, Long>> hashEventdetails,
			int i) {
		AnnotationSet annotSentenceEvents;
		annotSentenceEvents = annotSentenceEvent.getContained(eventsOffset[i][0],eventsOffset[i][1]);
		AnnotationSet annotEvent = annotSentenceEvents.get("event");
		if(annotEvent.size()>0)
		{
			Iterator<Annotation> itEvent = annotEvent.iterator();
			String eventID = (String) itEvent.next().getFeatures().get("id");
			annotEvent = annotSentenceEvents.get("clueType");
			if(annotEvent.size()>0)
			{
				Iterator<Annotation> itVerb = annotEvent.iterator();
				Annotation annot = itVerb.next();
				Long statVerb = annot.getStartNode().getOffset();
				String verb = GateLoader.getPartGateDocument(gateDoc,statVerb, annot.getEndNode().getOffset());
				annotEvent = annotSentenceEvents.get("type");
				Iterator<Annotation> itontologyRelationClass = annotEvent.iterator();
				String ontologyRelationClass = (String) itontologyRelationClass.next().getFeatures().get("class");
				processEvent(hasheventwhitentities,annotSentenceEvents,eventID);
				hashEventdetails.put(eventID, new GenericTriple<String, String,Long>(verb,ontologyRelationClass,statVerb-eventsOffset[i][0]));
			}
		}
	}
	
	/**
	 * 
	 * Method that add to Hash a new event entities at rigth and left
	 * 
	 * 
	 * @param hasheventwhitentities
	 * @param annotEvent
	 * @param eventID
	 */
	private void processEvent(Map<String,GenericPair<List<String>,List<String>>> hasheventwhitentities,AnnotationSet annotEvent,String eventID)
	{
		List<String> leftEntities = new ArrayList<String>();
		List<String> rightEntities = new ArrayList<String>();
		
		AnnotationSet cause =  annotEvent.get("cause");	
		if(cause.size()>0)
		{
			Iterator<Annotation> itAnnot = cause.iterator();
			Annotation annot = itAnnot.next();
			String entLeft1 = (String) annot.getFeatures().get("idref");
			String entLeft2 = (String) annot.getFeatures().get("idref1");
			if(entLeft1!=null)
			{
				leftEntities.add(entLeft1);
			}
			if(entLeft2!=null)
			{
				leftEntities.add(entLeft2);
			}		
		}
		AnnotationSet theme =  annotEvent.get("theme");
		if(theme.size()>0)
		{
			Iterator<Annotation> itAnnot = theme.iterator();
			Annotation annot = itAnnot.next();
			String entRight1 = (String) annot.getFeatures().get("idref");
			String entRight2 = (String) annot.getFeatures().get("idref1");
			if(entRight1!=null)
			{
				rightEntities.add(entRight1);
			}
			if(entRight2!=null)
			{
				rightEntities.add(entRight2);
			}
		}	
		hasheventwhitentities.put(eventID, new GenericPair<List<String>, List<String>>(leftEntities,rightEntities));
	}


	/**
	 * Method that extract all events from sentence (start,end offsets)
	 * 
	 * @param pmid
	 * @param gateDoc
	 * @param annotSetOriginal
	 * @param start
	 * @param end
	 */
	private String[][] processSentenceEntities(AnnotationSet annotSetOriginal, long start, long end,long sentenceIndex,long error)
	{

		AnnotationSet annotSentenceEvent = annotSetOriginal.getContained(start,end);
		long startEntity,endEntity,offsetDif,offsetFinalDif;
		String entity,classe;
		int classID;
		String[][] terms = processTermSentence(annotSentenceEvent);
		IEntityAnnotation e;
		for(int i=0;i<terms.length;i++)
		{
			startEntity = Long.valueOf(terms[i][0]);
			endEntity = Long.valueOf(terms[i][1]);
			entity = GateLoader.getPartGateDocument(gateDoc, startEntity, endEntity);
			terms[i][1]=entity; /// start,entity,id,sem
			offsetDif = startEntity-start;
			offsetFinalDif = endEntity-start;
			classe = terms[i][3];
			if(classesClassesID.containsKey(classe))
			{
				classID = classesClassesID.get(classe);
			}
			else
			{
				classID = CorporaManagement.addElementClass(db,classe);
				classesClassesID.put(classe, classID);
			}	
			e = new EntityAnnotation(-1, sentenceIndex+offsetDif-error, sentenceIndex+offsetFinalDif-error, classID,0, terms[i][1], NormalizationForm.getNormalizationForm(terms[i][1]));
			this.entities.add(e);
		}
		return terms;
	}
	

	
	/**
	 * Method that find all entities on sentence
	 * 
	 * @param annotSentencewhitinsentence
	 * @return
	 */
	private String[][] processTermSentence(AnnotationSet annotSentencewhitinsentence) 
	{
		Annotation annot;
		annotSentencewhitinsentence = annotSentencewhitinsentence.get("term");
		String[][] terms = new String[annotSentencewhitinsentence.size()][4]; // startoffset,end,id,sem
		Iterator<Annotation> itAnnotTerms =  annotSentencewhitinsentence.iterator();
		int i=0;
		while(itAnnotTerms.hasNext())
		{
			annot=itAnnotTerms.next();
			long start = annot.getStartNode().getOffset();
			long end = annot.getEndNode().getOffset();
			String id = (String) annot.getFeatures().get("id");
			String sem = (String) annot.getFeatures().get("sem");
			terms[i][0]= String.valueOf(new Long(start));
			terms[i][1]= String.valueOf(new Long(end));
			terms[i][2]= id;
			if(sem==null)
			{
				sem="unknown";
			}
			terms[i][3]= sem;
			i++;
		}
		return terms;
	}
	
	/** 
	 * Method that return a sentenceLimits ( sentence + events ) for order offset
	 * 
	 * @param annotSetOriginal
	 * @return
	 */
	private long[][] getSentenceLimits(AnnotationSet annotSetOriginal)
	{
		long[][] sentenceLimits = GateLoader.getGenericOffsetLimits(annotSetOriginal,"sentence",true);
		long[][] articlesLimits = GateLoader.getGenericOffsetLimits(annotSetOriginal,"Article",false);
		for(int i=0;i<sentenceLimits.length-1;i++)
		{
			sentenceLimits[i][1]=sentenceLimits[i+1][0];
		}		
		sentenceLimits[sentenceLimits.length-1][1]=articlesLimits[0][1];		
		return sentenceLimits;
	}

	private String getDocumentPMID(Document gateDoc,AnnotationSet annotSetOriginal) throws InvalidOffsetException {
		return GateLoader.getGeneralArticleInfo(gateDoc, annotSetOriginal,"PMID");
	}

	private String getDocumentAbstract(Document gateDoc,AnnotationSet annotSetOriginal) throws InvalidOffsetException {	
		String abstractText = new String(),sentence = new String();
		long[][] articleAbstractLimits = GateLoader.getGenericOffsetLimits(annotSetOriginal, "AbstractText",false);
		AnnotationSet annotationAbtsract = annotSetOriginal.getContained(articleAbstractLimits[0][0],articleAbstractLimits[0][1]);
		SortedMap<GenericPairComparable<Long, Long>, Annotation> annotations = GateLoader.getGenericOrderAnnotations(annotationAbtsract, "sentence");
		Iterator<GenericPairComparable<Long, Long>> it = annotations.keySet().iterator();
		GenericPairComparable<Long, Long> pos;
		while(it.hasNext())
		{
			pos = it.next();
			sentence= GateLoader.getPartGateDocument(gateDoc,pos.getX(),pos.getY());
			abstractText = abstractText.concat(sentence);
		}
		return abstractText;
	}

	private String getDocumentTitle(Document gateDoc,AnnotationSet annotSetOriginal) throws InvalidOffsetException {
		String title = new String(),sentence = new String();
		long[][] articleTitleLimits = GateLoader.getGenericOffsetLimits(annotSetOriginal, "ArticleTitle",false);
		AnnotationSet annotationAbtsract = annotSetOriginal.getContained(articleTitleLimits[0][0],articleTitleLimits[0][1]);
		SortedMap<GenericPairComparable<Long, Long>, Annotation> annotations = GateLoader.getGenericOrderAnnotations(annotationAbtsract, "sentence");
		Iterator<GenericPairComparable<Long, Long>> it = annotations.keySet().iterator();
		GenericPairComparable<Long, Long> pos;
		while(it.hasNext())
		{
			pos = it.next();
			sentence= GateLoader.getPartGateDocument(gateDoc,pos.getX(),pos.getY());
			title = title.concat(sentence);
		}
		return title;
	}

	public boolean validateFile() {
		if(filepath.isDirectory())
		{
			corruptFiles = new ArrayList<String>();
			for(File file:filepath.listFiles())
			{
				validateOneFile(file);
			}
			if(corruptFiles.size()>0){
				return false;
			}
			else{
				return true;
			}
		}
		else{
			return false;
		}
	}

	private void validateOneFile(File file) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		org.w3c.dom.Document doc;
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("*");
			for (int temp = 0; temp < nList.getLength(); temp++) {	 
				   Node nNode = nList.item(temp);
				   if(!validTags.contains(nNode.getNodeName()))
				   {
					   corruptFiles.add(file.getAbsolutePath());
					   return;
				   }
			}			
		} catch (SAXException e) {
			corruptFiles.add(file.getAbsolutePath());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			corruptFiles.add(file.getAbsolutePath());
			e.printStackTrace();
		}	
	}

	public List<String> getCorruptFiles() {
		return corruptFiles;
	}

	public File getFileorDirectory() {
		return filepath;
	}

	public int corpusSize() {
		return corpusSize;
	}

	
	public int corpusLoadPosition() {
		return corpusLoaderPosition;
	}
	
	public static void main(String[] args) {
		GeniaEventCorpusLoader anoteLoader = new GeniaEventCorpusLoader(null,new File("C:/Users/Hugo Costa/Desktop/GENIA_event_annotation/GENIAcorpus_event/"));
		anoteLoader.processTextFile();
		
	}

	public IDocumentSet getDocuments() {
		return documents;
	}

	public void setDocuments(IDocumentSet documents) {
		this.documents = documents;
	}

	@Override
	public List<IAnnotatedDocument> getEntityAnnotaions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IAnnotatedDocument> getEventAnnotaions(File filepath) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<IEntityAnnotation> getEntityAnnotationsIN()
	{
		return this.entities;
	}
	
	public List<IEventAnnotation> getEventAnnotaionIN()
	{
		return this.events;
	}
	
	public String getFulltext() {
		return fulltext;
	}

	public Map<String, Integer> getClassesClassesID() {
		return classesClassesID;
	}


}
