package pt.uminho.anote2.aibench.corpus.datatypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.process.IE.INERProcess;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.ListElements;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;

@Datatype(structure = Structure.LIST,namingMethod="getNERName",viewable=false)
public class NERProcess extends IEProcess implements INERProcess{

	private List<IAnnotatedDocument> annotatedDocs;
	private Map<Integer,IAnnotatedDocument> allProcessDocs;
	private Map<Integer,IEntityAnnotation> entityAnnotations;
	
	public NERProcess(int id,ICorpus corpus, String description, String type,Properties properties, IDatabase db) {
		super(id,corpus, description, type, properties, db);
		this.annotatedDocs = new ArrayList<IAnnotatedDocument>();
		this.allProcessDocs = null;
		this.entityAnnotations=null;
	}
	
	public NERProcess(ICorpus corpus, String description, String type,Properties properties, IDatabase db) {
		super(corpus, description, type, properties, db);
		this.annotatedDocs = new ArrayList<IAnnotatedDocument>();
		this.allProcessDocs = null;
		this.entityAnnotations=null;
	}

	@ListElements(modifiable=true)
	public List<IAnnotatedDocument> getAnnotatedDocs() {
		return annotatedDocs;
	}
	
	public void addAnnotatedDocument(IAnnotatedDocument doc)
	{
		if(!alreadyExist(doc))
		{
			annotatedDocs.add(doc);
			new ShowMessagePopup("Annotated Document Added !!!");
			notifyViewObservers();
		}	
	}
	
	public boolean alreadyExist(IAnnotatedDocument doc)
	{
		for(IAnnotatedDocument docEx:annotatedDocs)
		{
			if(docEx.getID()==doc.getID())
			{
				return true;
			}
		}
		return false;
	}
	
	public void notifyViewObservers()
	{
		this.setChanged();
		this.notifyObservers();
	}
	
	public String getNERName()
	{
		return getDescription()+" ("+getType()+") "+getId();
	}

	@Override
	public void executeNER() {
		
	}

	public Map<Integer,IAnnotatedDocument> getAllProcessDocs() {
		if(allProcessDocs==null)
		{
			allProcessDocs = getAllAnnotatedDocs();
		}
		return allProcessDocs;
	}

	public Map<Integer,IEntityAnnotation> getEntityAnnotations() {
		if(entityAnnotations==null)
		{
			entityAnnotations = getAllEntities();
		}
		return entityAnnotations;
	}
	
	private Map<Integer, IAnnotatedDocument> getAllAnnotatedDocs() {
		Map<Integer, IAnnotatedDocument> docAnnotation = new HashMap<Integer, IAnnotatedDocument>();
		IAnnotatedDocument annotDoc;
		int docID;
		for(IDocument doc:getCorpus().getArticlesCorpus())
		{
			docID = doc.getID();
			annotDoc = new NERDocumentAnnotation(this,getCorpus(),getCorpus().getArticlesCorpus().getDocument(docID));
			docAnnotation.put(docID, annotDoc);
		}
		return docAnnotation;
	}

}
