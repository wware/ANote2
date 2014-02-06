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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import pt.uminho.anote2.aibench.corpus.databasemanagement.CorporaManagement;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IDocumentSet;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.corpusloaders.ICorpusEntityLoader;
import pt.uminho.anote2.corpusloaders.utils.GateLoader;
import pt.uminho.anote2.datastructures.annotation.EntityAnnotation;
import pt.uminho.anote2.datastructures.documents.AnnotatedDocument;
import pt.uminho.anote2.datastructures.documents.DocumentSet;
import pt.uminho.anote2.datastructures.documents.Publication;
import pt.uminho.anote2.datastructures.textprocessing.NormalizationForm;

public class ANoteCorpusLoader implements ICorpusEntityLoader{
	
	private File filepath;
	private int corpusSize;
	private int corpusLoaderPosition;
	private IDocumentSet documents;
	private IDatabase db;
	private Document gateDoc;
	private List<String> corruptFiles;
	private Set<String> validTags;
	
	public ANoteCorpusLoader(IDatabase db,File filepath)
	{
		this.db=db;
		this.filepath=filepath;
		this.corpusLoaderPosition = 1;
		this.setDocuments(new DocumentSet());
		this.validTags = putValidTags();
		try {
			Gate.init();
		} catch (GateException e) {
			e.printStackTrace();
		}
	}

	private Set<String> putValidTags() {
		Set<String> validTags = new HashSet<String>();
		validTags.add("ARTICLE");
		validTags.add("JOURNAL");
		validTags.add("TITLE");
		validTags.add("span");
		validTags.add("SECTION");
		validTags.add("PARAGRAPH");
		validTags.add("AUTHORS");
		validTags.add("ABSTRACT");
		validTags.add("KEYWORDS");
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

	private IDocument processFile(File file) throws ResourceInstantiationException, InvalidOffsetException {
		gateDoc = GateLoader.createGateDocument(file);
		String fullText = gateDoc.getContent().toString();
		AnnotationSet annotSetOriginal = gateDoc.getAnnotations("Original markups");
		String title = getDocumentTitle(gateDoc,annotSetOriginal);
		String abstractText = getDocumentAbstract(gateDoc,annotSetOriginal);
		String journal = getDocumentJournal(gateDoc,annotSetOriginal);
		String authors = getDocumentAuthors(gateDoc,annotSetOriginal);
		IPublication pub = new Publication(-1, "", title,authors, "", "",journal, "", "", "","", abstractText, false);
		List<IEntityAnnotation> entities = getEntities(gateDoc,annotSetOriginal);
		IDocument docResult = new AnnotatedDocument(-1, pub,null, null, entities, -1);
		docResult.setFullTExt(fullText);
		gateDoc.cleanup();
		return docResult;
	}

	private List<IEntityAnnotation> getEntities(Document gateDoc2, AnnotationSet annotSetOriginal) throws InvalidOffsetException {
		Map<String,Integer> classesClassesID = new HashMap<String, Integer>();
		List<IEntityAnnotation> entityList = new ArrayList<IEntityAnnotation>();
		AnnotationSet annotSetOriginalSpan = annotSetOriginal.get("span");
		Iterator<Annotation> annotIterator = annotSetOriginalSpan.iterator();
		Annotation annot;
		long start,end;
		String value,annotationValueNormalization,classe;
		IEntityAnnotation entity;
		int classID;
		while(annotIterator.hasNext())
		{
			annot = annotIterator.next();
			start = annot.getStartNode().getOffset();
			end = annot.getEndNode().getOffset();
			value=gateDoc.getContent().getContent(start, end).toString();
			annotationValueNormalization = NormalizationForm.getNormalizationForm(value);
			classe = annot.getFeatures().get("class").toString();
			if(classesClassesID.containsKey(classe))
			{
				classID = classesClassesID.get(classe);
			}
			else
			{
				classID = CorporaManagement.addElementClass(db,classe);
				classesClassesID.put(classe, classID);
			}
			entity = new EntityAnnotation(-1, start, end,classID,-1, value, annotationValueNormalization);
			entityList.add(entity);
		}
		return entityList;
	}

	private String getDocumentAuthors(Document gateDoc2,AnnotationSet annotSetOriginal) throws InvalidOffsetException {
		return GateLoader.getGeneralArticleInfo(gateDoc, annotSetOriginal,"AUTHORS");
	}

	private String getDocumentJournal(Document gateDoc2,AnnotationSet annotSetOriginal) throws InvalidOffsetException {
		return GateLoader.getGeneralArticleInfo(gateDoc, annotSetOriginal,"JOURNAL");
	}

	private String getDocumentAbstract(Document gateDoc2,AnnotationSet annotSetOriginal) throws InvalidOffsetException {
		return GateLoader.getGeneralArticleInfo(gateDoc, annotSetOriginal,"ABSTRACT");
	}

	private String getDocumentTitle(Document gateDoc2,AnnotationSet annotSetOriginal) throws InvalidOffsetException {
		return GateLoader.getGeneralArticleInfo(gateDoc, annotSetOriginal,"TITLE");
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

	@Override
	public List<IAnnotatedDocument> getEntityAnnotaions() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) {
		ANoteCorpusLoader anoteLoader = new ANoteCorpusLoader(null,new File("C:/Users/Hugo Costa/Desktop/Ecoli"));
		anoteLoader.validateFile();
		anoteLoader.processTextFile();
	}

	public IDocumentSet getDocuments() {
		return documents;
	}

	public void setDocuments(IDocumentSet documents) {
		this.documents = documents;
	}
		
	

}
