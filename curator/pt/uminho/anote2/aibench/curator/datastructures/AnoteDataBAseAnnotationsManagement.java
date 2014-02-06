package pt.uminho.anote2.aibench.curator.datastructures;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.NERDocumentAnnotation;
import pt.uminho.anote2.aibench.corpus.utils.AnnotationPosition;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesAnnotatedDocument;
import pt.uminho.anote2.datastructures.textprocessing.NormalizationForm;

public class AnoteDataBAseAnnotationsManagement {
	
	private IDatabase database;
	private int corpusID;
	private int processID;
	private int publicationID;
		
	private PreparedStatement removeAnnotationPS;
	private PreparedStatement insertEntityAnnotationPS;
	private PreparedStatement updateClassAnnotationPS;
	
	public AnoteDataBAseAnnotationsManagement(NERDocumentAnnotation ner) throws SQLException
	{
		this.database=((Corpus)ner.getCorpus()).getCorpora().getDb();
		this.corpusID=ner.getCorpus().getID();
		this.processID=ner.getProcess().getID();
		this.publicationID=ner.getID();
		initPS();
	}
	
	private void initPS() throws SQLException {
		removeAnnotationPS = database.getConnection().prepareStatement(QueriesAnnotatedDocument.removeAnnotation);
		removeAnnotationPS.setInt(3, getCorpusID());
		removeAnnotationPS.setInt(4, getProcessID());
		removeAnnotationPS.setInt(5, getPublicationID());
		insertEntityAnnotationPS = database.getConnection().prepareStatement(QueriesAnnotatedDocument.insertEntityAnnotation);
		insertEntityAnnotationPS.setInt(1,processID);
		insertEntityAnnotationPS.setInt(2,corpusID);
		insertEntityAnnotationPS.setInt(3,publicationID);
		updateClassAnnotationPS = database.getConnection().prepareStatement(QueriesAnnotatedDocument.updateClassAnnotation);
		updateClassAnnotationPS.setInt(4,corpusID);
		updateClassAnnotationPS.setInt(5,processID);
		updateClassAnnotationPS.setInt(6,publicationID);
	}

	public void removeAnnotation(AnnotationPosition pos) throws SQLException
	{
		removeAnnotationPS.setInt(1, pos.getStart());
		removeAnnotationPS.setInt(2, pos.getEnd());
		removeAnnotationPS.execute();
	}
	
	public void addAnnotation(AnnotationPosition pos,IEntityAnnotation elem) throws SQLException
	{
			insertAnnotaion(pos,elem.getResourceElementID(),elem.getAnnotationValue(),elem.getClassAnnotationID());	
	}
	
	public void updateAnnotations(List<AnnotationPosition> list, int classID) throws SQLException {
		
		updateClassAnnotationPS.setInt(1, classID);
		for(AnnotationPosition pos:list)
		{
			updateClassAnnotationPS.setInt(2,pos.getStart());
			updateClassAnnotationPS.setInt(3,pos.getEnd());
			updateClassAnnotationPS.execute();
		}
	}	
	
	
	private void insertAnnotaion(AnnotationPosition pos, int termID, String term,int classeID) throws SQLException {	
		insertEntityAnnotationPS.setInt(4,pos.getStart());
		insertEntityAnnotationPS.setInt(5,pos.getEnd());
		insertEntityAnnotationPS.setString(6,term);
		if(termID==0)
		{
			insertEntityAnnotationPS.setNull(7,0);
		}
		else
		{
			insertEntityAnnotationPS.setInt(7,termID);
		}
		insertEntityAnnotationPS.setString(8,NormalizationForm.getNormalizationForm(term));
		insertEntityAnnotationPS.setInt(9,classeID);
		insertEntityAnnotationPS.execute();
	}

	public IDatabase getDatabase() {
		return database;
	}

	public int getCorpusID() {
		return corpusID;
	}

	public int getProcessID() {
		return processID;
	}

	public int getPublicationID() {
		return publicationID;
	}


}
