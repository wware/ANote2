package pt.uminho.anote2.aibench.corpus.datatypes;

import java.util.List;
import java.util.Properties;

import pt.uminho.anote2.aibench.corpus.databasemanagement.CorporaManagement;
import pt.uminho.anote2.aibench.corpus.utils.AnnotationPosition;
import pt.uminho.anote2.aibench.corpus.utils.AnnotationPositions;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.textprocessing.Tokenizer;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;

@Datatype(structure = Structure.SIMPLE,namingMethod="getName")
public class REDocumentAnnotation extends NERDocumentAnnotation{
	
	private AnnotationPositions annotEntityPositions;
	private AnnotationPositions annotEventPositions;
	
	public REDocumentAnnotation(IPublication pub) {
		super(pub);
	}

	
	public REDocumentAnnotation(IEProcess reProcess, ICorpus corpus,IDocument doc) {
		super(reProcess,corpus,doc);
	}


	public String getName()
	{
		return "ID :"+getID()+" Corpus :"+getCorpus().getID()+" Process "+getProcess().getID();
	}
	
	public AnnotationPositions getE()
	{
		if(annotEventPositions==null)
		{
			AnnotationPositions annotations = new AnnotationPositions();
			List<IEventAnnotation> ent = getEventAnnotations();
			for(int i=0;i<ent.size();i++)
			{
				IEventAnnotation entity = ent.get(i);
				AnnotationPosition pos = new AnnotationPosition((int)entity.getStartOffset(),(int)entity.getEndOffset());
				annotations.addAnnotationWhitoutConflicts(pos, entity);
			}
			setAnnotEntityPositions(annotations);
		}
		return annotEventPositions;
	}
	

//	public AnnotationPositions getEventAnnotaionsPositions()
//	{
//		if(annotEventPositions==null)
//		{
//			AnnotationPositions annotations = new AnnotationPositions();
//			List<IEventAnnotation> events = getEventAnnotations();
//			for(int i=0;i<events.size();i++)
//			{
//				IEventAnnotation entity = events.get(i);
//				AnnotationPosition pos = new AnnotationPosition((int)entity.getStartOffset(),(int)entity.getEndOffset());
//				annotations.addAnnotationWhitoutConflicts(pos, entity);
//			}
//			setAnnotEntityPositions(annotations);
//		}
//		return annotEventPositions;
//	}
	

	public String getTextWhitoutAnnotations()
	{
		String text = new String();
		Properties corpusProperties = ((Corpus) getCorpus()).getProperties();
		if(corpusProperties.containsKey("textType"))
		{
			String propertyValue = corpusProperties.getProperty("textType");
			String intNer = (String) ((IEProcess)getProcess()).getProperties().get("nerprocess");
			String name;
			if(intNer!=null)
			{
				name =  CorporaManagement.getNERProcessDesignation(((Corpus)getCorpus()).getCorpora().getDb(),intNer);
			}
			else
			{
				name = "";
			}
			boolean typeAnote = ((IEProcess)getProcess()).getDescription().equals("Anote NER")||((IEProcess)getProcess()).getDescription().equals("Manual Curation") || name.equals("Anote NER");
			if(propertyValue.equals("full text"))
			{
				text = getFullTextFromDatabase(((Corpus)getCorpus()).getCorpora().getDb());
				if(typeAnote)
				{
					text = Tokenizer.tokenizer(text);
				}
				return text;
			}
			else if(propertyValue.equals("abstract"))
			{
				text = getAbstractSection();
				if(typeAnote)
				{
					text = Tokenizer.tokenizer(text);
				}
				return text;
			}
			else
			{
				String fulltext = getFullTextFromDatabase(((Corpus)getCorpus()).getCorpora().getDb());
				if(fulltext!=null)
				{
					if(typeAnote)
					{
						text = Tokenizer.tokenizer(text);
					}
					return fulltext;
				}
				else
				{
					text = getAbstractSection();
					if(typeAnote)
					{
						text = Tokenizer.tokenizer(text);
					}
					return text;
				}
			}
		}
		return text;
	}
	
	public void setAnnotEntityPositions(AnnotationPositions annotEntityPositions) {
		this.annotEntityPositions = annotEntityPositions;
	}

	public AnnotationPositions getAnnotEventPositions() {
		return annotEventPositions;
	}

	public void setAnnotEventPositions(AnnotationPositions annotEventPositions) {
		this.annotEventPositions = annotEventPositions;
	}
	
	public AnnotationPositions getAnnotEntityPositions() {
		return annotEntityPositions;
	}

	
	public void notifyViewObservers()
	{
		this.setChanged();
		this.notifyObservers();
	}

}
