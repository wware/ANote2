package pt.uminho.anote2.aibench.corpus.datatypes;

import java.sql.SQLException;
import java.util.List;

import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.annotation.AnnotationPosition;
import pt.uminho.anote2.datastructures.annotation.AnnotationPositions;
import pt.uminho.anote2.datastructures.documents.AnnotatedDocument;
import pt.uminho.anote2.datastructures.process.IEProcess;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;

@Datatype(structure = Structure.SIMPLE,namingMethod="getName",removable=true,renamed=false,autoOpen=true)
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
	
	public AnnotationPositions getEntityAnnotationPositions() throws SQLException, DatabaseLoadDriverException
	{
		if(annotPositions==null)
		{
			annotPositions = new AnnotationPositions();
			List<IEntityAnnotation> ent = getEntitiesAnnotations();
			for(int i=0;i<ent.size();i++)
			{
				IEntityAnnotation entity = ent.get(i);
				AnnotationPosition pos = new AnnotationPosition((int)entity.getStartOffset(),(int)entity.getEndOffset());
				annotPositions.addAnnotationWhitConflicts(pos, entity);
			}
		}
		return annotPositions;
	}

	
	public void notifyViewObservers()
	{
		this.setChanged();
		this.notifyObservers();
	}
}
