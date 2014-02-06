package pt.uminho.anote2.corpusloaders.loaders;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Gate;
import gate.util.GateException;
import gate.util.InvalidOffsetException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import pt.uminho.anote2.aibench.corpus.databasemanagement.CorporaManagement;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IDocumentSet;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.corpusloaders.ICorpusEntityLoader;
import pt.uminho.anote2.corpusloaders.utils.FileHandeling;
import pt.uminho.anote2.corpusloaders.utils.GateLoader;
import pt.uminho.anote2.datastructures.annotation.EntityAnnotation;
import pt.uminho.anote2.datastructures.documents.AnnotatedDocument;
import pt.uminho.anote2.datastructures.documents.DocumentSet;
import pt.uminho.anote2.datastructures.documents.Publication;
import pt.uminho.anote2.datastructures.textprocessing.NormalizationForm;
import pt.uminho.anote2.datastructures.utils.FileHandling;

public class AIMEDProteinCorpusLoader implements ICorpusEntityLoader{

	private File filepath;
	private int corpusSize;
	private int corpusLoaderPosition;
	private IDocumentSet documents;
	private IDatabase db;
	private Document gateDoc;

	private static final String fileCorpusCheck = "/corpus-list";
	private static final String fileCorpusTags = "/tag-list";
	

	public AIMEDProteinCorpusLoader(IDatabase db,File filepath)
	{
		this.db=db;
		this.filepath=filepath;
		this.corpusLoaderPosition = 1;
		this.documents = new DocumentSet();	
		try {
			Gate.init();
		} catch (GateException e) {
			e.printStackTrace();
		}
	}


	public IDocumentSet processTextFile() {
		if(validateFile())
		{
			try {
				Set<String> listFiles = getFilesList();
				setCorpusSize(listFiles.size());
				for(File file:filepath.listFiles())
				{
					if(!file.isDirectory())
					{
						if(listFiles.contains(file.getName()))
						{
							IDocument doc = processFile(file);
							getDocuments().addDocument(corpusLoaderPosition, doc);
							corpusLoaderPosition++;
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (XMLStreamException e) {
				e.printStackTrace();
			} catch (GateException e) {
				e.printStackTrace();
			} 
			return 	getDocuments();
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * @throws XMLStreamException 
	 * @throws GateException 
	*/
	private IDocument processFile(File file) throws IOException, XMLStreamException, GateException {
		String fileContent = FileHandeling.getFileContent(file);
		fileContent=fileContent.replace("<?>", "");
		fileContent=fileContent.replace("</?>", "");
		fileContent = "<Doc>" + fileContent + "</Doc>";
		File fileTmp = new File("fileTmp.xml");			
		FileHandling.writeInformationOnFile(fileTmp, fileContent);		
		gateDoc = GateLoader.createGateDocument(fileTmp);;
		AnnotationSet annotSetOriginal = gateDoc.getAnnotations("Original markups");	
		String title = getDocumentTitle(gateDoc,annotSetOriginal);
		String abstractText = getDocumentAbstract(gateDoc,annotSetOriginal);
		IPublication pub = new Publication(-1, "", title, "AIMED Team", "", "", "", "", "", "","", abstractText, false);
		List<IEntityAnnotation> entities = getEntities(gateDoc,annotSetOriginal);
		IDocument docResult = new AnnotatedDocument(-1, pub,null, null, entities, -1);
		gateDoc.cleanup();
		return docResult;
	}
	
	private List<IEntityAnnotation> getEntities(Document gateDoc, AnnotationSet annotSetOriginal) throws InvalidOffsetException {
		IEntityAnnotation entity;
		long start = 0,end = 0;
		String value = "",annotationValueNormalization = "";
		Annotation annot;
		List<IEntityAnnotation> entities = new ArrayList<IEntityAnnotation>();
		AnnotationSet annotSetOriginalProt = annotSetOriginal.get("prot");
		Iterator<Annotation> annotIterator = annotSetOriginalProt.iterator();
		int classID = CorporaManagement.addElementClass(db,"protein");
		while(annotIterator.hasNext())
		{
			annot = annotIterator.next();
			start = annot.getStartNode().getOffset();
			end = annot.getEndNode().getOffset();
			value=gateDoc.getContent().getContent(start, end).toString();
			annotationValueNormalization = NormalizationForm.getNormalizationForm(value);
			entity = new EntityAnnotation(-1, start, end,classID,-1, value, annotationValueNormalization);
			entities.add(entity);
		}
		return entities;
	}


	private String getDocumentAbstract(Document gateDoc,AnnotationSet annotSetOriginal) throws InvalidOffsetException {
		return gateDoc.getContent().toString();
	}


	private String getDocumentTitle(Document gateDoc,AnnotationSet annotSetOriginal) throws InvalidOffsetException {
		return GateLoader.getGeneralArticleInfo(gateDoc, annotSetOriginal,"ArticleTitle");
	}

	private Set<String> getFilesList() throws IOException {
		Set<String> files = new HashSet<String>();
		File filePath = new File(getFilepath().getAbsolutePath()+fileCorpusCheck);
		files = FileHandeling.getFileLines(filePath);
		return files;		
	}

	public File getFilepath() {
		return filepath;
	}


	public boolean validateFile() {
		if(getFilepath().isDirectory())
		{
			File test1 = new File(getFilepath().getAbsolutePath()+fileCorpusCheck);
			File test2 = new File(getFilepath().getAbsolutePath()+fileCorpusTags);
			return test1.exists() && test2.exists();
		}
		else
		{
			return false;
		}
	}

	public int corpusSize() {
		return this.corpusSize;
	}

	public int corpusLoadPosition() {
		return this.corpusLoaderPosition;
	}

	@Override
	public List<IAnnotatedDocument> getEntityAnnotaions() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public IDocumentSet getDocuments() {
		return documents;
	}
	
	public void setCorpusSize(int corpusSize) {
		this.corpusSize = corpusSize;
	}

	public File getFileorDirectory() {
		return filepath;
	}
	

}
