package pt.uminho.anote2.datastructures.annotation.ner;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import pt.uminho.anote2.core.annotation.IAnnotation;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.report.processes.INERMergeProcess;
import pt.uminho.anote2.datastructures.annotation.AnnotationPosition;
import pt.uminho.anote2.datastructures.annotation.AnnotationPositions;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesAnnotatedDocument;
import pt.uminho.anote2.datastructures.documents.AnnotatedDocument;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.report.processes.NERMergeSchemasReport;
import pt.uminho.anote2.datastructures.textprocessing.NormalizationForm;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextInfoSmall;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.process.IE.INERSchema;

/**
 * Class that contains methods to Merge NER Schemas
 * 
 * @author Hugo Costa
 *
 */
public class MergeNERSchemas {
	
	private ICorpus corpus;
	private IIEProcess baseProcess;
	private boolean stop = false;
	private PreparedStatement insertAnnot;
	
	public MergeNERSchemas(ICorpus corpus,IIEProcess baseProcess)
	{
		this.corpus = corpus;
		this.baseProcess = baseProcess;
	}
	
	private void initPrepareStatments() throws SQLException, DatabaseLoadDriverException {
		insertAnnot = Configuration.getDatabase().getConnection().prepareStatement(QueriesAnnotatedDocument.insertEntityAnnotation);
	}

	/**
	 * Method that add {@link AnnotationPositions} to a {@link IDocument} in a {@link INERSchema} on {@link ICorpus}
	 * 
	 * @param docID
	 * @param processID
	 * @param corous
	 * @param annotationsPositions
	 * @throws SQLException
	 */
	protected void insertOnDatabaseEntityAnnotations(int docID,int processID,ICorpus corous, AnnotationPositions annotationsPositions) throws SQLException{

		IEntityAnnotation annot;
		insertAnnot.setInt(1,processID);
		insertAnnot.setInt(2,corous.getID());
		insertAnnot.setInt(3,docID);
		for(AnnotationPosition annotPos :annotationsPositions.getAnnotations().keySet())
		{
			if(stop)
				break;
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

	}
	
	/**
	 * Method that merges a List of {@link IIEProcess}
	 * 
	 * @param nerProcessesToMerge
	 * @return {@link INERMergeProcess}
	 * @throws SQLException
	 * @throws DatabaseLoadDriverException 
	 */
	public INERMergeProcess mergeNERProcessesMergeAnnotations(List<IIEProcess> nerProcessesToMerge) throws SQLException, DatabaseLoadDriverException{
		if(insertAnnot==null)
			initPrepareStatments();
		List<IIEProcess> nerProcessesToMergeValidated = validateNERPprocesses(nerProcessesToMerge);
		if(nerProcessesToMergeValidated.size()==0)
		{
			INERMergeProcess report = new NERMergeSchemasReport(GlobalTextInfoSmall.mergenerprocesses,null, baseProcess, nerProcessesToMergeValidated);
			report.setcancel();
			report.setNotes("No valid NERs Process to merge");
			return report;
		}
		else
		{
			Properties properties = getProperties(nerProcessesToMergeValidated);
			IEProcess newProcess = new IEProcess(corpus, GlobalNames.mergeNER, GlobalNames.ner, properties);
			INERMergeProcess report = new NERMergeSchemasReport(GlobalTextInfoSmall.mergenerprocesses,newProcess, baseProcess, nerProcessesToMergeValidated);
			AnnotationPositions docResultAnnotations;
			long startTime = Calendar.getInstance().getTimeInMillis();
			int i=0;
			Iterator<IDocument> itDocs = corpus.iterator();
			while(itDocs.hasNext())
			{
				if(!stop)
				{
					IDocument doc = itDocs.next();
					docResultAnnotations = processDocument(doc,nerProcessesToMergeValidated);
					if(!stop)
						insertOnDatabaseEntityAnnotations(doc.getID(),newProcess.getId(),corpus,docResultAnnotations);
					memoryAndProgressAndTime(i, corpus.size(), startTime);
				}
				else
				{
					report.setcancel();
					break;
				}
				i++;
			}
			corpus.registerProcess(newProcess);
			long endTime = GregorianCalendar.getInstance().getTimeInMillis();
			report.setTime(endTime-startTime);
			return report;
		}

	}
	
	private Properties getProperties(List<IIEProcess> nerProcessesToMergeValidated) {
		Properties prop = new Properties();
		prop.put(GlobalNames.mergeNERSchema, "true");
		prop.put(GlobalNames.NERSchema+1,baseProcess.toString());
		int i=2;
		for(IIEProcess proc : nerProcessesToMergeValidated)
		{
			prop.put(GlobalNames.NERSchema+i,proc.toString());
			i++;
		}
		if(baseProcess.getProperties().containsKey(GlobalNames.normalization) && Boolean.valueOf((String)baseProcess.getProperties().get(GlobalNames.normalization)))
			prop.put(GlobalNames.normalization, "true");
		return prop;
	}

	private AnnotationPositions processDocument(IDocument doc, List<IIEProcess> nerProcessesToMergeValidated) throws SQLException, DatabaseLoadDriverException {
		AnnotatedDocument annotatedDocument = new AnnotatedDocument((IEProcess) baseProcess, corpus, doc);
		AnnotationPositions annot = annotatedDocument.getEntitiesAnnotationsOrderByOffset();
		for(IIEProcess process:nerProcessesToMergeValidated)
		{
			if(stop)
				return new AnnotationPositions();
			annotatedDocument = new AnnotatedDocument(process, corpus, doc);
			for(AnnotationPosition pos:annotatedDocument.getEntitiesAnnotationsOrderByOffset().getAnnotations().keySet())
			{
				if(stop)
					return new AnnotationPositions();
				IAnnotation entityAnnotaion = annotatedDocument.getEntitiesAnnotationsOrderByOffset().getAnnotations().get(pos);
				annot.addAnnotationWhitConflitsAndReplaceIfRangeIsMore(pos, entityAnnotaion);
			}
		}
		return annot;
	}

	public void memoryAndProgressAndTime(int step, int total, long startTime) {
		System.out.println((GlobalOptions.decimalformat.format((double)step/ (double) total * 100)) + " %...");
		Runtime.getRuntime().gc();
		System.out.println((Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory())/(1024*1024) + " MB ");
	}

	private List<IIEProcess> validateNERPprocesses(List<IIEProcess> nerProcessesToMerge) {
		List<IIEProcess> nerProcessesOk = new ArrayList<IIEProcess>();
		for(IIEProcess process:nerProcessesToMerge)
		{

			if(valideProcess(process))
			{
				nerProcessesOk.add(process);
			}
		}
		return nerProcessesOk;
	}
	
	private boolean valideProcess(IIEProcess process) {

		if(!testSameCorpus(process))
		{
			return false;
		}
		if(!testSameTextNormalizationType(process))
		{
			return false;
		}
		return true;
	}
	
	private boolean testSameTextNormalizationType(IIEProcess baseProcess) {
		if(getNormalizationProcessOption(baseProcess) == getNormalizationProcessOption(this.baseProcess))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private boolean  getNormalizationProcessOption(IIEProcess baseProcess) {
		boolean nornalization = false;
		if(baseProcess.getProperties().containsKey(GlobalNames.normalization))
		{
			nornalization = Boolean.valueOf(baseProcess.getProperties().getProperty(GlobalNames.normalization));
		}
		return nornalization;
	}
	
	private boolean testSameCorpus(IIEProcess process) {
		int corpusID = this.corpus.getID();
		int processCorpusID = process.getCorpus().getID();
		if(corpusID==processCorpusID)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void stop()
	{
		stop = true;
	}

}
