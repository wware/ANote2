package pt.uminho.anote2.aibench.ner.operations;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.ner.datastructures.ElementToNer;
import pt.uminho.anote2.aibench.ner.datastructures.ResourcesToNerAnote;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.operations.TimeLeftProgress;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesAnnotatedDocument;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.textprocessing.NormalizationForm;
import pt.uminho.anote2.datastructures.textprocessing.Tokenizer;
import pt.uminho.anote2.ner.annotations.AnnotationPosition;
import pt.uminho.anote2.ner.ner.AnnotationPositions;
import pt.uminho.anote2.ner.ner.NER2;
import pt.uminho.anote2.ner.ner.rules.HandRules;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation()
public class OperationNerAnote {
	
	private TimeLeftProgress progress = new TimeLeftProgress();
	private Corpus corpus;
	private IEProcess ner;
	private List<Integer> classIDCaseSensative = new ArrayList<Integer>();	
	private int documents = 0;
	private int entitiesAnnotated = 0; 
	
	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}

	@Port(name="Corpus",direction=Direction.INPUT,order=1)
	public void setCorpus(Corpus corpus)
	{
		this.corpus=corpus;
	}
	
	@Port(name="resources",direction=Direction.INPUT,order=3)
	public void setResources(ResourcesToNerAnote resources)
	{
		progress.setTimeString("Load Terms From DB...");
		ElementToNer resource = new ElementToNer();
		for(int i=0;i<resources.getList().size();i++)
		{
			Set<Integer> selected = resources.getList().get(i).getZ();
			Set<Integer> all = resources.getList().get(i).getY();
			if(selected.equals(all))
			{
				resource.addResource(resources.getList().get(i).getX());
			}
			else
			{
				resource.addResource(resources.getList().get(i).getX(),new ArrayList<Integer>(selected));
			}
		}
		Properties prop = transformResourcesToOrderMapInProperties(resource);
		if(corpus.getProperties().get("textType").equals("abstract"))
		{
			prop.put("textType", "abstract");		
			ner = new IEProcess(corpus,"Anote NER","ner",prop,corpus.getCorpora().getDb());
			corpus.registerProcess(ner);
			abstractProcessing(resource);	
		}
		else if(corpus.getProperties().get("textType").equals("full text"))
		{
			prop.put("textType", "full text");
			ner = new IEProcess(corpus,"Anote NER","ner",prop,corpus.getCorpora().getDb());
			corpus.registerProcess(ner);
			fullTextProcessing(resource);
		}
		else if(corpus.getProperties().get("textType").equals("abstract OR full text"))
		{
			prop.put("textType", "abstract OR full text");
			ner = new IEProcess(corpus,"Anote NER","ner",prop,corpus.getCorpora().getDb());
			corpus.registerProcess(ner);
			fullOrAbstractProcessing(resource);
		}		
		corpus.notifyViewObserver();	
		if(loaderStats())
		{
			
		}
		new ShowMessagePopup("NER Anote Done!!!");
	}
	
	
	private Properties transformResourcesToOrderMapInProperties(ElementToNer resources) {
		Properties prop = new Properties();
		for(Integer resoruceID:resources.getResourceIdsOptions().keySet())
		{
			prop.put(String.valueOf(resoruceID),resources.getResourceIdsOptions().get(resoruceID));
		}	
		return prop;
	}
	
	private void abstractProcessing(ElementToNer resources) {
		int size = corpus.size();
		documents = size;
		int step = 0;
		long startTime = Calendar.getInstance().getTimeInMillis();
		long actualTime,differTime;
		Iterator<IDocument> itDocs = corpus.iterator();
		while(itDocs.hasNext())
		{
			List<Integer> classIdCaseSensative = new ArrayList<Integer>();
			AnnotationPositions annotationsPositions = new AnnotationPositions();
			IDocument doc = itDocs.next();
			String textAbstract = ((IPublication)doc).getAbstractSection();
			if(textAbstract==null)
			{
				Workbench.getInstance().warn("The article whit id: "+doc.getID()+"not contains abstract ");
			}
			else
			{
				textAbstract = Tokenizer.tokenizer(textAbstract);
				HandRules rules = new HandRules(resources.getRules());
				NER2 ner = new NER2(resources.getTerms(), rules);
				try {
					annotationsPositions = ner.executeNer(textAbstract,classIdCaseSensative);
					insertOnDatabaseAnnotations(doc,annotationsPositions);
					entitiesAnnotated = entitiesAnnotated+annotationsPositions.getAnnotations().size();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			actualTime = Calendar.getInstance().getTimeInMillis();
			differTime = actualTime - startTime;
			step++;
			progress.setTime(differTime, step, size);
			progress.setProgress((float) step/ (float) size);

		}	
	}

	private void insertOnDatabaseAnnotations(IDocument doc,AnnotationPositions annotationsPositions) {
		
		Connection conn = corpus.getCorpora().getDb().getConnection();
		IEntityAnnotation annot;
		try {
			PreparedStatement insertAnnot = conn.prepareStatement(QueriesAnnotatedDocument.insertEntityAnnotation);
			insertAnnot.setInt(1,ner.getID());
			insertAnnot.setInt(2,corpus.getId());
			insertAnnot.setInt(3,doc.getID());
			for(AnnotationPosition annotPos :annotationsPositions.getAnnotations().keySet())
			{
				insertAnnot.setInt(4,annotPos.getStart());
				insertAnnot.setInt(5,annotPos.getEnd());
				annot = annotationsPositions.getAnnotations().get(annotPos);
				insertAnnot.setString(6,annot.getAnnotationValue());
				insertAnnot.setInt(7,annot.getID());
				insertAnnot.setString(8,NormalizationForm.getNormalizationForm(annot.getAnnotationValue()));
				insertAnnot.setInt(9,annot.getClassAnnotationID());
				insertAnnot.execute();
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	private void fullTextProcessing(ElementToNer resources) {
		progress.setTimeString("Calculating ...");
		int size = corpus.size();
		documents = size;
		int step = 0;
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		long actualTime,differTime;
		Iterator<IDocument> itDocs = corpus.iterator();
		while(itDocs.hasNext())
		{
			IDocument doc = itDocs.next();
			IPublication pub = (IPublication) doc;
			String fullText = doc.getFullTextFromDatabase(corpus.getCorpora().getDb());
			AnnotationPositions annotationsPositions = new AnnotationPositions();
			if(fullText.equals(""))
			{
				fullText = findFullTextArticle(pub);
			}
			fullText = Tokenizer.tokenizer(fullText);
			HandRules rules = new HandRules(resources.getRules());
			NER2 ner = new NER2(resources.getTerms(), rules);
			try {
				annotationsPositions = ner.executeNer(fullText,classIDCaseSensative );
				insertOnDatabaseAnnotations(doc,annotationsPositions);
				entitiesAnnotated = entitiesAnnotated+annotationsPositions.getAnnotations().size();
			} catch (Exception e) {
				e.printStackTrace();
			}	
			actualTime = GregorianCalendar.getInstance().getTimeInMillis();
			differTime = actualTime - startTime;
			step++;
			progress.setTime(differTime, step, size);
			progress.setProgress((float) step/ (float) size);
		}
	}

	private String findFullTextArticle(IPublication pub) {
		String fullText=new String();;
		String id_path = Corpora.saveDocs+"id"+pub.getID()+".pdf";
		String id_path_otherID = Corpora.saveDocs+"id"+pub.getID()+"-"+pub.getOtherID()+".pdf";
		String pdf_path = Corpora.saveDocs+pub.getOtherID()+".pdf";
		if(new File(id_path).exists())
		{
			fullText = pub.getFullTextFromURL(id_path);
		}
		else if(new File(id_path_otherID).exists())
		{
			fullText = pub.getFullTextFromURL(id_path_otherID);
		}
		else if(new File(pdf_path).exists())
		{
			fullText = pub.getFullTextFromURL(pdf_path);
		}
		if(fullText==null||fullText.equals(""))
		{
			Workbench.getInstance().warn("The article whit id: "+pub.getID()+" not contains full text");
		}
		else
		{
			pub.setFullTExtOnDatabase(corpus.getCorpora().getDb(), fullText);
		}
		return fullText;
	}
	
	private void fullOrAbstractProcessing(ElementToNer resources) {
		progress.setTimeString("Calculating ...");
		int size = corpus.size();
		documents = size;
		int step = 0;
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		long actualTime,differTime;
		Iterator<IDocument> itDocs = corpus.iterator();
		while(itDocs.hasNext())
		{
			IDocument doc = itDocs.next();
			IPublication pub = (IPublication) doc;
			String fullText = doc.getFullTextFromDatabase(corpus.getCorpora().getDb());
			AnnotationPositions annotationsPositions = new AnnotationPositions();
			if(fullText.equals("")||fullText==null)
			{
				String id_path = Corpora.saveDocs+"id"+doc.getID()+".pdf";
				String id_path_otherID = Corpora.saveDocs+"id"+doc.getID()+"-"+pub.getOtherID()+".pdf";
				String pdf_path = Corpora.saveDocs+pub.getOtherID()+".pdf";
				if(new File(id_path).exists())
				{
					fullText = doc.getFullTextFromURL(id_path);
				}
				else if(new File(id_path_otherID).exists())
				{
					fullText = doc.getFullTextFromURL(id_path_otherID);
				}
				else if(new File(pdf_path).exists())
				{
					fullText = doc.getFullTextFromURL(pdf_path);
				}
				if(fullText==null||fullText.equals(""))
				{
					/**
					 * Processing abstarct
					 */
					String textAbstract = ((IPublication)doc).getAbstractSection();
					if(textAbstract==null)
					{
						Workbench.getInstance().warn("The article whit id:  "+doc.getID()+"not contains either abstract and full text");

					}
					else
					{
						textAbstract = Tokenizer.tokenizer(textAbstract);

						HandRules rules = new HandRules(resources.getRules());
						NER2 ner = new NER2(resources.getTerms(), rules);
						try {
							annotationsPositions = ner.executeNer(textAbstract,classIDCaseSensative);
							insertOnDatabaseAnnotations(doc,annotationsPositions);
							entitiesAnnotated = entitiesAnnotated+annotationsPositions.getAnnotations().size();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				else
				{
					doc.setFullTExtOnDatabase(corpus.getCorpora().getDb(), fullText);
				}
			}
			fullText = Tokenizer.tokenizer(fullText);
			HandRules rules = new HandRules(resources.getRules());
			NER2 ner = new NER2(resources.getTerms(), rules);
			try {
				annotationsPositions = ner.executeNer(fullText,classIDCaseSensative);
				insertOnDatabaseAnnotations(doc,annotationsPositions);
			} catch (Exception e) {
				e.printStackTrace();
			}	
			actualTime = GregorianCalendar.getInstance().getTimeInMillis();
			differTime = actualTime - startTime;
			progress.setTime(differTime, step, size);
			progress.setProgress((float) step/ (float) size);
		}	
	}
	
	private boolean loaderStats(){
		Object[] options = new String[]{"Continue"};
		int opt = showOptionPane("NER Finishing Stats","Annotations :"+entitiesAnnotated+"\n Documents :"+documents, options);
		switch (opt) {
		case 0:
			return true;
		default:
			return true;
		}
	}
	
	/** Presents a option pane with the given title, question and options */
	public static int showOptionPane(String title, String question, Object[] options){
		JOptionPane option_pane = new JOptionPane(question);

		option_pane.setOptions(options);
		JDialog dialog = option_pane.createDialog(Workbench.getInstance().getMainFrame(), title);
		dialog.setVisible(true);
		
		Object choice = option_pane.getValue();
					
		for(int i=0; i<options.length; i++)
			if(options[i].equals(choice))
				return i;
		
		return -1;		
	}
}
