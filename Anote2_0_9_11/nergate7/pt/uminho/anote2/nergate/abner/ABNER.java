package pt.uminho.anote2.nergate.abner;

import es.uvigo.ei.aibench.workbench.Workbench;
import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.abner.AbnerTagger;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
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

public class ABNER extends IEProcess implements INERProcess{
	
	private TimeLeftProgress progress;
	private ABNERTrainingModel model;
	private Document gateDocument;
	protected static final int characteres = 500000;
	private File file = new File("conf/fileNERAbner.xml");
	private boolean stop = false;
	private AbnerTagger abTagger;
	
	public ABNER(ICorpus corpus, String description, String type,Properties properties,ABNERTrainingModel model,TimeLeftProgress progress) 
	{
		super(corpus, description, type, properties);
		this.model=model;
		this.progress=progress;
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
			GateInit.getInstance().creoleRegister("plugins/Tagger_Abner");
	}	
		
	protected void documetsRelationExtraction(INERProcessReport report, StringBuffer stringWhitManyDocuments) throws GateException, SQLException, IOException, DatabaseLoadDriverException {
		String fileText = "<Docs>"+stringWhitManyDocuments.toString()+"</Docs>";
		FileHandling.writeInformationOnFile(file,fileText);
		performeGateAbner();
		List<GenericTriple<Long, Long, Integer>> documentsLimit = getGateDocumentlimits(gateDocument);
		
		for(GenericTriple<Long, Long, Integer> docProcessing:documentsLimit)
		{
			AnnotationPositions annots =  getAnnotations(docProcessing.getX(),docProcessing.getY());
			if(!stop)
			{
				IEProcess.insertOnDatabaseEntityAnnotations(docProcessing.getZ(),getId(), getCorpus(), annots);
				report.incrementEntitiesAnnotated(annots.getAnnotations().size());
			}
			else
			{
				report.setcancel();
				break;
			}
		}
	}

	private AnnotationPositions getAnnotations(Long startDoc,Long endDoc) throws InvalidOffsetException, SQLException, DatabaseLoadDriverException {
		AnnotationPositions annotPos = new AnnotationPositions();
		AnnotationSet annotAbner = gateDocument.getAnnotations().get("Tagger").get(startDoc, endDoc);
		Iterator<Annotation> annotIterator = annotAbner.iterator();
		Annotation annot;
		String value;
		String annotationValueNormalization;
		IEntityAnnotation entity;
		AnnotationPosition position;
		int classID;
		String classe;
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
				classe = (String) annot.getFeatures().get("type");
				classID = ClassProperties.insertNewClass(classe);
				entity = new EntityAnnotation(-1, start-startDoc, end-startDoc,classID ,-1, value, annotationValueNormalization);
				position = new AnnotationPosition(Integer.parseInt(String.valueOf(start-startDoc)), Integer.parseInt(String.valueOf(end-startDoc)));
				annotPos.addAnnotationWhitConflicts(position, entity);
			}
			else
			{
				// To long entity
			}
		}
		return annotPos;
	}
	
	private void performeGateAbner() throws ResourceInstantiationException, ExecutionException, MalformedURLException {
		if(abTagger==null)
		{
			FeatureMap params = Factory.newFeatureMap();
			FeatureMap features = Factory.newFeatureMap();
			params.put("sourceUrl",file.toURI().toString());	
			gateDocument = (Document) Factory.createResource("gate.corpora.DocumentImpl", params, features, "GATE Homepage");
			params = Factory.newFeatureMap();
			features = Factory.newFeatureMap();
			features = Factory.newFeatureMap();
			params.put("abnerMode", model.toValue());
			abTagger = (AbnerTagger) Factory.createResource("gate.abner.AbnerTagger", params, features);
		}
		else
		{
			FeatureMap params = Factory.newFeatureMap();
			FeatureMap features = Factory.newFeatureMap();
			params.put("sourceUrl",file.toURI().toString());	
			gateDocument = (Document) Factory.createResource("gate.corpora.DocumentImpl", params, features, "GATE Homepage");
		}
		abTagger.setDocument(gateDocument);
		abTagger.execute();
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

	public INERProcessReport executeCorpusNER(ICorpus corpus) throws DatabaseLoadDriverException, SQLException,Exception {
		INERProcessReport report =  new NERProcessReport(GlobalNames.nerAbner,this);
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
			report.setTime(actualTime-startTime);
			file.delete();
		cleanAll();
		return report;
	}

	private void cleanAll() {
		if(abTagger!=null)
		{
			Factory.deleteResource(abTagger);
		}
		if(gateDocument!=null)
		{
			Factory.deleteResource(gateDocument);

		}
	}

	@Override
	public void stop() {
		stop = true;	
	}
	
}
