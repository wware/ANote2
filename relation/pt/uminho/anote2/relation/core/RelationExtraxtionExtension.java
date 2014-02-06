package pt.uminho.anote2.relation.core;

import gate.util.GateException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Properties;

import process.IGatePosTagger;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.utils.operations.TimeLeftProgress;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IDocumentSet;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.textprocessing.Tokenizer;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.anote2.process.IE.INERProcess;
import pt.uminho.anote2.process.IE.re.IRelationModel;

public class RelationExtraxtionExtension extends RelationsExtraction{
	
	private TimeLeftProgress progress;
	int position = 0;
	int max = 0;

	public RelationExtraxtionExtension(ICorpus corpus,INERProcess nerProcess, IGatePosTagger postagger,
			IRelationModel relationModel2, Properties properties,TimeLeftProgress progress) {
		super(corpus, nerProcess, postagger, relationModel2, properties);
		this.progress=progress;
		this.progress.setProgress(0);
		this.progress.setTimeString("Calculating...");
	}
	
	protected void fullOrAbstractProcessing() throws IOException, GateException, SQLException {
		IDocumentSet docs = getCorpus().getArticlesCorpus();
		Iterator<IDocument> itDocs =docs.iterator();
		max = docs.size();
		int docInDocumentGate = 0;
		StringBuffer stringWhitManyDocuments = new StringBuffer();
		int size = 0;
		long starttime = GregorianCalendar.getInstance().getTimeInMillis();
		while(itDocs.hasNext())
		{
			docInDocumentGate++;
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
			if(getNerProcess().getDescription().equals("Anote NER"))
			{
				fullText = Tokenizer.tokenizer(fullText);
			}
			size = size+fullText.length();
			if(size>characteres)
			{
				stringWhitManyDocuments.append("<Doc id=\""+doc.getID()+"\"> "+Utils.treatSentenceForXMLProblems(fullText)+"</Doc>");
				documetsRelationExtraction(stringWhitManyDocuments);
				stringWhitManyDocuments = new StringBuffer();
				size=0;
				position = position +docInDocumentGate;
				long actualTime = GregorianCalendar.getInstance().getTimeInMillis();
				progress.setTime(actualTime-starttime, position, max);
				progress.setProgress((float) position/(float) max);
				docInDocumentGate=0;
			}
			else
			{
				stringWhitManyDocuments.append("<Doc id=\""+doc.getID()+"\"> "+Utils.treatSentenceForXMLProblems(fullText)+"</Doc>");
			}
		}
	}

	protected void fullTextProcessing() throws IOException, GateException, SQLException {
		IDocumentSet docs = getCorpus().getArticlesCorpus();
		Iterator<IDocument> itDocs =docs.iterator();
		max = docs.size();
		StringBuffer stringWhitManyDocuments = new StringBuffer();
		int size = 0;
		int docInDocumentGate = 0;
		long starttime = GregorianCalendar.getInstance().getTimeInMillis();
		while(itDocs.hasNext())
		{
			docInDocumentGate++;
			IDocument doc = itDocs.next();
			String fulltext = doc.getFullTextFromDatabase(((Corpus) getCorpus()).getCorpora().getDb());
			if(getNerProcess().getDescription().equals("Anote NER"))
			{
				fulltext = Tokenizer.tokenizer(fulltext);
			}
			size = size+fulltext.length();
			if(size>characteres)
			{
				documetsRelationExtraction(stringWhitManyDocuments);
				stringWhitManyDocuments = new StringBuffer();
				size=0;
				position = position +docInDocumentGate;
				long actualTime = GregorianCalendar.getInstance().getTimeInMillis();
				progress.setTime(actualTime-starttime, position, max);
				progress.setProgress((float) position/(float) max);
				docInDocumentGate=0;
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
		IDocumentSet docs = getCorpus().getArticlesCorpus();
		Iterator<IDocument> itDocs =docs.iterator();
		max = docs.size();
		StringBuffer stringWhitManyDocuments = new StringBuffer();
		int size = 0;
		int docInDocumentGate = 0;
		long starttime = GregorianCalendar.getInstance().getTimeInMillis();
		while(itDocs.hasNext())
		{
			docInDocumentGate++;
			IDocument doc = itDocs.next();
			String abstractText = ((IPublication) doc).getAbstractSection();
			if(getNerProcess().getDescription().equals("Anote NER"))
			{
				abstractText = Tokenizer.tokenizer(abstractText);
			}
			size = size+abstractText.length();
			if(size>characteres)
			{
				stringWhitManyDocuments.append("<Doc id=\""+doc.getID()+"\"> "+Utils.treatSentenceForXMLProblems(abstractText)+"</Doc>");
				documetsRelationExtraction(stringWhitManyDocuments);
				stringWhitManyDocuments = new StringBuffer();
				size=0;
				position = position +docInDocumentGate;
				long actualTime = GregorianCalendar.getInstance().getTimeInMillis();
				progress.setTime(actualTime-starttime, position, max);
				progress.setProgress((float) position/(float) max);
				docInDocumentGate=0;
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
	
	
	
	

}
