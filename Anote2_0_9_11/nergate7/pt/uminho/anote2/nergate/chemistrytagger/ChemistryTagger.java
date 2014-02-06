package pt.uminho.anote2.nergate.chemistrytagger;

import es.uvigo.ei.aibench.workbench.Workbench;
import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.splitter.RegexSentenceSplitter;
import gate.creole.tokeniser.SimpleTokeniser;
import gate.util.GateException;
import gate.util.InvalidOffsetException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import mark.chemistry.Tagger;

import org.apache.log4j.Logger;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.core.report.processes.INERProcessReport;
import pt.uminho.anote2.datastructures.annotation.AnnotationPosition;
import pt.uminho.anote2.datastructures.annotation.AnnotationPositions;
import pt.uminho.anote2.datastructures.annotation.ner.EntityAnnotation;
import pt.uminho.anote2.datastructures.database.schema.TableAnnotation;
import pt.uminho.anote2.datastructures.documents.AnnotatedDocument;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.report.processes.NERProcessReport;
import pt.uminho.anote2.datastructures.textprocessing.NormalizationForm;
import pt.uminho.anote2.datastructures.utils.FileHandling;
import pt.uminho.anote2.datastructures.utils.GenericTriple;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.process.IE.INERProcess;
import pt.uminho.gate.GateInit;

public class ChemistryTagger  extends IEProcess implements INERProcess{
	
	private Document gateDocument;
	private File file = new File("conf/fileNERchemistryTagger.xml");
	private boolean chemistrylon ;
	private boolean chemistryElements;
	private boolean chemistryCompounds;
	private TimeLeftProgress progress;
	private int classID;
	private boolean stop = false;
	protected static final int characteres = 500000;
	private Tagger tagger;
	private SimpleTokeniser tokeniser;
	private RegexSentenceSplitter regExpSentenceSplitter;



	public ChemistryTagger(ICorpus corpus, String description, String type,Properties properties,
			TimeLeftProgress progress,
			boolean chemistrylon,boolean chemistryElements,boolean chemistryCompounds) {
		super(corpus, description, type, properties);
		this.progress=progress;
		this.chemistrylon=chemistrylon;
		this.chemistryElements=chemistryElements;
		this.chemistryCompounds=chemistryCompounds;
		try {
			GateInit();
		} catch (MalformedURLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (GateException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}

	public void GateInit() throws GateException, MalformedURLException
	{
			GateInit.getInstance().init();
			GateInit.getInstance().creoleRegister("plugins/ANNIE");
			GateInit.getInstance().creoleRegister("plugins/Tagger_Chemistry");
	}	
	
	private void performeGateChemistryTagger() throws ResourceInstantiationException, ExecutionException {
		if(tagger==null)
		{
			FeatureMap params = Factory.newFeatureMap();
			FeatureMap features = Factory.newFeatureMap();
			params.put("sourceUrl",file.toURI().toString());	
			gateDocument = (Document) Factory.createResource("gate.corpora.DocumentImpl", params, features, "GATE Homepage");
			params = Factory.newFeatureMap();
			features = Factory.newFeatureMap();
			features = Factory.newFeatureMap();
			tokeniser = (SimpleTokeniser) Factory.createResource("gate.creole.tokeniser.SimpleTokeniser", params, features);
			features = Factory.newFeatureMap();	
			regExpSentenceSplitter = (RegexSentenceSplitter) Factory.createResource("gate.creole.splitter.RegexSentenceSplitter", params, features);
			params = Factory.newFeatureMap();
			params.put("removeElements","false");
			tagger = (Tagger) Factory.createResource("mark.chemistry.Tagger", params, features);
		}
		tokeniser.setDocument(gateDocument);
		tokeniser.execute();
		regExpSentenceSplitter.setDocument(gateDocument);
		regExpSentenceSplitter.execute();
		tagger.setDocument(gateDocument);
		tagger.execute();
	}
	
	protected void documetsRelationExtraction(INERProcessReport report, StringBuffer stringWhitManyDocuments) throws GateException, SQLException, IOException, DatabaseLoadDriverException {
		String fileText = "<Docs>"+stringWhitManyDocuments.toString()+"</Docs>";
		FileHandling.writeInformationOnFile(file,fileText);
		performeGateChemistryTagger();
		List<GenericTriple<Long, Long, Integer>> documentsLimit = getGateDocumentlimits(gateDocument);	
		for(GenericTriple<Long, Long, Integer> docProcessing:documentsLimit)
		{
			AnnotationPositions annotPos = new AnnotationPositions();
			if(chemistryCompounds & !stop)
				getAnnotations(annotPos,docProcessing.getX(),docProcessing.getY(),GlobalNames.nerChemistryTaggerChemistryCompounds);
			if(chemistryElements & !stop)
				getAnnotations(annotPos,docProcessing.getX(),docProcessing.getY(),GlobalNames.nerChemistryTaggerChemistryElements);
			if(chemistrylon & !stop)	
				getAnnotations(annotPos,docProcessing.getX(),docProcessing.getY(),GlobalNames.nerChemistryTaggerChemistrylon);
			if(!stop)
			{
				IEProcess.insertOnDatabaseEntityAnnotations(docProcessing.getZ(),getId(), getCorpus(), annotPos);
				report.incrementEntitiesAnnotated(annotPos.getAnnotations().size());
			}
			else
			{
				report.setcancel();
				break;
			}
		}
		System.gc();
	}

	private void getAnnotations(AnnotationPositions annotPos,Long startDoc,Long endDoc,String type) throws InvalidOffsetException {
		AnnotationSet annotAbner = gateDocument.getAnnotations().get(type).get(startDoc, endDoc);
		Iterator<Annotation> annotIterator = annotAbner.iterator();
		Annotation annot;
		String value;
		String annotationValueNormalization;
		IEntityAnnotation entity;
		AnnotationPosition position;
		Long start,end;
		while(annotIterator.hasNext())
		{
			annot = annotIterator.next();
			start = annot.getStartNode().getOffset();
			end = annot.getEndNode().getOffset();
			if(end-start<TableAnnotation.maxAnnotaionElementSize && start>=startDoc && end <= endDoc)
			{
				value= gateDocument.getContent().getContent(start, end).toString();
				annotationValueNormalization = NormalizationForm.getNormalizationForm(value);
				entity = new EntityAnnotation(-1, start-startDoc, end-startDoc,this.classID ,-1, value, annotationValueNormalization);
				position = new AnnotationPosition(Integer.parseInt(String.valueOf(start-startDoc)), Integer.parseInt(String.valueOf(end-startDoc)));
				annotPos.addAnnotationWhitConflicts(position, entity);
			}
		}
	}
	
	public static List<GenericTriple<Long, Long,Integer>> getGateDocumentlimits(Document doc) {
		List<GenericTriple<Long,Long,Integer>> documentLimits = new ArrayList<GenericTriple<Long,Long,Integer>>();
		AnnotationSet annotSetSentences = doc.getAnnotations("Original markups");
		AnnotationSet annotSetSentences2 = annotSetSentences.get("Doc");
		for(Annotation annot:annotSetSentences2)
		{
			String idS = (String) annot.getFeatures().get("id");
			int id = Integer.valueOf(idS);
			documentLimits.add(new GenericTriple<Long, Long,Integer>(annot.getStartNode().getOffset(),annot.getEndNode().getOffset(),id));
		}
		return documentLimits;
	}

	@Override
	public INERProcessReport executeCorpusNER(ICorpus corpus) throws SQLException, DatabaseLoadDriverException,Exception {
		classID = ClassProperties.insertNewClass(GlobalNames.compounds);
		INERProcessReport report = new NERProcessReport(GlobalNames.nerChemistryTagger,this);
		progress.setTimeString("Calculating ...");
		int step = 0;
		int size = getCorpus().size();
		int textSize = 0;
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		StringBuffer stringWhitManyDocuments = new StringBuffer();
		long actualTime,differTime;
		Iterator<IDocument> itDocs = getCorpus().iterator();
		while(itDocs.hasNext())
		{
			if(stop)
			{
				report.setcancel();
				break;
			}
			IDocument doc = itDocs.next();
			IPublication pub = (IPublication) doc;
			IAnnotatedDocument annotDoc = new AnnotatedDocument(pub.getID(),pub, this, getCorpus());
			String text = annotDoc.getDocumetAnnotationText();
			if(text == null || text.length() == 0)
			{
				Logger logger = Logger.getLogger(Workbench.class.getName());
				logger.warn("No available text for publication whit id = "+doc.getID()+" for NER Process");
			}
			else
			{
				int lenthText = text.length();
				textSize = textSize+lenthText;
				if(textSize>characteres && !stop)
				{
					stringWhitManyDocuments.append("<Doc id=\""+doc.getID()+"\">"+Utils.treatSentenceForXMLProblems(text)+"</Doc>");
					documetsRelationExtraction(report,stringWhitManyDocuments);
					stringWhitManyDocuments = new StringBuffer();
					textSize=0;
					actualTime = GregorianCalendar.getInstance().getTimeInMillis();
					differTime = actualTime - startTime;
					progress.setTime(differTime, step, size);
					progress.setProgress((float) step/ (float) size);
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
			}
			step++;
			report.incrementDocument();
		}
		if(stringWhitManyDocuments.length()>0 && !stop)
		{
			documetsRelationExtraction(report,stringWhitManyDocuments);
		}
		actualTime = GregorianCalendar.getInstance().getTimeInMillis();
		report.setTime(actualTime - startTime);
		file.delete();
		cleanAll();
		return report;
	}

	private void cleanAll() {
		if(tokeniser!=null)
		{
			Factory.deleteResource(tokeniser);
		}
		if(regExpSentenceSplitter!=null)
		{
			Factory.deleteResource(regExpSentenceSplitter);
		}
		if(tagger!=null)
		{
			Factory.deleteResource(tagger);
		}
		if(gateDocument!=null)
		{
			Factory.deleteResource(gateDocument);
		}
	}

public void stop() {
		this.stop = true;
		
	}

}
