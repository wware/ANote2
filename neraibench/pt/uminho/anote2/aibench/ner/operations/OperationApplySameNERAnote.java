package pt.uminho.anote2.aibench.ner.operations;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.NERProcess;
import pt.uminho.anote2.aibench.ner.datastructures.ElementToNer;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.operations.TimeLeftProgress;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesAnnotatedDocument;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.datastructures.resources.lookuptable.LookupTable;
import pt.uminho.anote2.datastructures.resources.ontology.Ontology;
import pt.uminho.anote2.datastructures.resources.rule.RulesSet;
import pt.uminho.anote2.datastructures.textprocessing.NormalizationForm;
import pt.uminho.anote2.datastructures.textprocessing.Tokenizer;
import pt.uminho.anote2.ner.annotations.AnnotationPosition;
import pt.uminho.anote2.ner.ner.AnnotationPositions;
import pt.uminho.anote2.ner.ner.NER2;
import pt.uminho.anote2.ner.ner.rules.HandRules;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation()
public class OperationApplySameNERAnote {
	
	private TimeLeftProgress progress = new TimeLeftProgress();
	private Corpus corpus;
	private IIEProcess process;
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
	
	@Port(name="Process",direction=Direction.INPUT,order=2)
	public void setIEProcess(IIEProcess process)
	{
		this.process= new NERProcess(process.getCorpus(), process.getDescription(), process.getType(), process.getProperties(), process.getDB());
		Properties prop = process.getProperties();
		String textType = prop.getProperty("textType");
		prop.remove("textType");
		ElementToNer resources = new ElementToNer();
		try {
			propToResourcesToOrderMap(prop, resources);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(textType.equals("full text"))
		{		
			fullTextProcessing(resources);
		}
		else if(textType.equals("abstract"))
		{
			abstractProcessing(resources);
		}
		else
		{
			fullOrAbstractProcessing(resources);
		}
		corpus.registerProcess(this.process);
		corpus.notifyViewObserver();	
		if(loaderStats())
		{
			
		}
		new ShowMessagePopup("NER Anote Done!!!");
	}

	private void propToResourcesToOrderMap(Properties prop,ElementToNer resource) throws SQLException {
		for(String prop_name:prop.stringPropertyNames())
		{
			String value = prop.getProperty(prop_name);
			if(value.equals("all"))
			{
				resource.addResource(fillModelQuerysTable(Integer.valueOf(prop_name)));
			}
			else
			{
				List<Integer> listClassContent = getClassContent(value);
				resource.addResource(fillModelQuerysTable(Integer.valueOf(prop_name)),listClassContent);
			}
		}
	}
	
	private List<Integer> getClassContent(String value) {
		String[] split = value.split("\\,");
		List<Integer> classContentID = new ArrayList<Integer>();
		for(String contentID:split)
		{
			if(contentID.length()>0)
			{
				classContentID.add(Integer.valueOf(contentID));
			}
		}
		return classContentID;
	}

	private IResource<IResourceElement> fillModelQuerysTable(int resourceID) throws SQLException{						 
		PreparedStatement statement = corpus.getCorpora().getDb().getConnection().prepareStatement(QueriesResources.getResourcesInfo);
		IResource<IResourceElement> resource = null;
		statement.setInt(1, resourceID);
		ResultSet rs = statement.executeQuery();
		int id;
		String type,name,note;
		if(rs.next())
		{
			id = rs.getInt(1);
			type = rs.getString(2);
			name = rs.getString(3);
			note = rs.getString(4);
			resource = makeResource(id,type,name,note);
		}
		rs.close();
		return resource;
	}
	
	private IResource<IResourceElement> makeResource(int id, String type,String name, String note) {
		if(type.equals("dictionary"))
		{
			return new Dictionary(corpus.getCorpora().getDb(), id, name, note);
		}
		else if(type.equals("lookuptable"))
		{
			return new LookupTable(corpus.getCorpora().getDb(), id, name, note);
		}
		else if(type.equals("ontology"))
		{
			return new Ontology(corpus.getCorpora().getDb(), id, name, note);
		}
		else if(type.equals("rules"))
		{
			return new RulesSet(corpus.getCorpora().getDb(), id, name, note);
		}
		return null;
	}
	
	private void abstractProcessing(ElementToNer resources) {
		progress.setTimeString("Calculating ...");
		int size = corpus.size();
		documents = size;
		int step = 0;
		long startTime = Calendar.getInstance().getTimeInMillis();
		long actualTime,differTime;
		Iterator<IDocument> itDocs = corpus.iterator();
		while(itDocs.hasNext())
		{
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
					annotationsPositions = ner.executeNer(textAbstract,classIDCaseSensative);
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
			insertAnnot.setInt(1,process.getID());
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
		System.out.println("Corpus Size :"+corpus.size());
		Iterator<IDocument> itDocs = corpus.iterator();
		System.out.println("Terms :"+resources.getTerms().size());
		System.out.println("Rules :"+resources.getRules().size());
		while(itDocs.hasNext())
		{
			long sartTimeDoc = GregorianCalendar.getInstance().getTimeInMillis();
			IDocument doc = itDocs.next();
			System.out.println("Doc "+doc.getID());
			IPublication pub = (IPublication) doc;
			String fullText = doc.getFullTextFromDatabase(corpus.getCorpora().getDb());
			AnnotationPositions annotationsPositions = new AnnotationPositions();
			if(fullText.equals(""))
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
					Workbench.getInstance().warn("The article whit id: "+doc.getID()+" not contains full text");
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
				entitiesAnnotated = entitiesAnnotated+annotationsPositions.getAnnotations().size();
			} catch (Exception e) {
				e.printStackTrace();
			}	
			actualTime = GregorianCalendar.getInstance().getTimeInMillis();
			differTime = actualTime - startTime;
			long endTimeDoc = GregorianCalendar.getInstance().getTimeInMillis();
			long timeDoc = endTimeDoc-sartTimeDoc;
			System.out.println("Time "+timeDoc/1000);
			step++;
			progress.setTime(differTime, step, size);
			progress.setProgress((float) step/ (float) size);
		}
	}
		
		private void fullOrAbstractProcessing(ElementToNer resources) {
			progress.setTimeString("Calculating ...");
			int size = corpus.size();
			documents = size;
			int step = 0;
			long startTime = GregorianCalendar.getInstance().getTimeInMillis();
			long actualTime,differTime;
			System.out.println("Corpus Size :"+corpus.size());
			Iterator<IDocument> itDocs = corpus.iterator();
			System.out.println("Terms :"+resources.getTerms().size());
			System.out.println("Rules :"+resources.getRules().size());
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
					entitiesAnnotated = entitiesAnnotated+annotationsPositions.getAnnotations().size();
					insertOnDatabaseAnnotations(doc,annotationsPositions);
				} catch (Exception e) {
					e.printStackTrace();
				}	
				actualTime = GregorianCalendar.getInstance().getTimeInMillis();
				differTime = actualTime - startTime;
				step++;
				progress.setTime(differTime, step, size);
				progress.setProgress((float) step/ (float) size);			}	
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
