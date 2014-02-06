package pt.uminho.anote2.aibench.corpus.datatypes;

import java.util.List;
import java.util.Properties;

import pt.uminho.anote2.aibench.corpus.utils.AnnotationPosition;
import pt.uminho.anote2.aibench.corpus.utils.AnnotationPositions;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.documents.AnnotatedDocument;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.textprocessing.Tokenizer;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;

@Datatype(structure = Structure.SIMPLE,namingMethod="getName")
public class NERDocumentAnnotation extends AnnotatedDocument{
	
	private AnnotationPositions annotPositions;
	
	public NERDocumentAnnotation(IPublication pub) {
		super(pub.getID());
	}
	
	public NERDocumentAnnotation(IEProcess reProcess, ICorpus corpus,IDocument document)
	{
		super(reProcess,corpus,document);
	}

	public String getName()
	{
		return "ID :"+getID()+" Corpus :"+getCorpus().getID()+" Process "+getProcess().getID();
	}
	
	public AnnotationPositions getAnnotationPositions()
	{
		if(annotPositions==null)
		{
			AnnotationPositions annotations = new AnnotationPositions();
			List<IEntityAnnotation> ent = getEntitiesAnnotations();
			for(int i=0;i<ent.size();i++)
			{
				IEntityAnnotation entity = ent.get(i);
				AnnotationPosition pos = new AnnotationPosition((int)entity.getStartOffset(),(int)entity.getEndOffset());
				annotations.addAnnotationWhitoutConflicts(pos, entity);
			}
			setAnnotPositions(annotations);
		}
		return annotPositions;
	}
	
	public AnnotationPositions getAnnotPositions() {
		return annotPositions;
	}

	public void setAnnotPositions(AnnotationPositions annotPositions) {
		this.annotPositions = annotPositions;
	}

	public String getTextWhitoutAnnotations()
	{
		String text = new String();
		Properties corpusProperties = ((Corpus) getCorpus()).getProperties();
		if(corpusProperties.containsKey("textType"))
		{
			String propertyValue = corpusProperties.getProperty("textType");
			boolean typeAnote = ((IEProcess)getProcess()).getDescription().equals("Anote NER")||((IEProcess)getProcess()).getDescription().equals("Manual Curation");
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
	
	public void notifyViewObservers()
	{
		this.setChanged();
		this.notifyObservers();
	}
}
