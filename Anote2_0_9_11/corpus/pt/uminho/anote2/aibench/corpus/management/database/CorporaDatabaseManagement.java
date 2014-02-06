package pt.uminho.anote2.aibench.corpus.management.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.datastructures.database.queries.corpora.QueriesCorpora;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesAnnotatedDocument;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesProcess;
import pt.uminho.anote2.datastructures.utils.GenericPair;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;

public class CorporaDatabaseManagement {
	

		
    
	public static void insertOnDatabaseAnnotations(int ieprocessID,int corpusID,int docID, IDocument doc) throws SQLException, DatabaseLoadDriverException {
		IAnnotatedDocument docAnnot = (IAnnotatedDocument) doc;
			PreparedStatement insertAnnot = Configuration.getDatabase().getConnection().prepareStatement(QueriesAnnotatedDocument.insertEntityAnnotation);
			insertAnnot.setInt(1,ieprocessID);
			insertAnnot.setInt(2,corpusID);
			insertAnnot.setInt(3,docID);
			for(IEntityAnnotation ent :docAnnot.getEntitiesAnnotations())
			{
				insertAnnot.setInt(4, (int) ent.getStartOffset());
				insertAnnot.setInt(5, (int) ent.getEndOffset());
				insertAnnot.setNString(6,ent.getAnnotationValue());
				if(ent.getResourceElementID()<1)
				{
					insertAnnot.setNull(7,1);
				}
				else
				{
					insertAnnot.setInt(7,ent.getResourceElementID());
				}
				insertAnnot.setNString(8,ent.getAnnotationValueNormalization());
				insertAnnot.setInt(9,ent.getClassAnnotationID());
				insertAnnot.execute();
			}
			insertAnnot.close();	
	}

	public static void insertEventOnDataBase(int corpusID,int ieprocessID, int docID, IEventAnnotation event) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement insertAnnot;
			insertAnnot = Configuration.getDatabase().getConnection().prepareStatement(QueriesAnnotatedDocument.insertEventAnnotation);
			insertAnnot.setInt(1,ieprocessID);
			insertAnnot.setInt(2,corpusID);
			insertAnnot.setInt(3,docID);
			insertAnnot.setInt(4, (int) event.getStartOffset());
			insertAnnot.setInt(5, (int) event.getEndOffset());
			insertAnnot.setNull(6,1);
			insertAnnot.setNull(7,1);
			insertAnnot.setNull(8,1);
			insertAnnot.setNull(9,1);
			insertAnnot.setNString(10,event.getOntologycalClass());
			insertAnnot.setNString(11,event.getEventClue());
			insertAnnot.execute();
			insertAnnot.close();
		
	}

	public static void insertEvenEntitySide(PreparedStatement stat, int entID, int i) throws SQLException {
		stat.setInt(2,entID);
		stat.setInt(3,i);
		stat.execute();	
	
	}

	public static void insertEntityAnnotation(PreparedStatement insertAnnot, IEntityAnnotation ent) throws SQLException {	
			insertAnnot.setInt(4, (int) ent.getStartOffset());
			insertAnnot.setInt(5, (int) ent.getEndOffset());
			insertAnnot.setNString(6,ent.getAnnotationValue());
			insertAnnot.setNull(7,1);
			insertAnnot.setNString(8,ent.getAnnotationValueNormalization());
			insertAnnot.setInt(9,ent.getClassAnnotationID());
			insertAnnot.execute();	
	}

	public static String getNERProcessDesignation(String intNer) throws SQLException, DatabaseLoadDriverException {
		String result = null;
		PreparedStatement insertAnnot;
		insertAnnot = Configuration.getDatabase().getConnection().prepareStatement(QueriesProcess.getProcessDescription);
		insertAnnot.setNString(1,intNer);
		ResultSet rs = insertAnnot.executeQuery();
		if(rs.next())
		{
			result =  rs.getString(1);
		}
		rs.close();
		insertAnnot.close();
		return result;
	}
	
	public static GenericPair<Integer,Integer> getEventInformation(Integer annotaionID) throws SQLException, DatabaseLoadDriverException {
		GenericPair<Integer,Integer> pair = null;
		PreparedStatement insertAnnot;
		insertAnnot = Configuration.getDatabase().getConnection().prepareStatement(QueriesAnnotatedDocument.getEventInformation);
		insertAnnot.setInt(1,annotaionID);
		ResultSet rs = insertAnnot.executeQuery();
		if(rs.next())
		{
			pair = new GenericPair<Integer, Integer>(rs.getInt(1), rs.getInt(2));
		}
		rs.close();
		insertAnnot.close();
		return pair;
	}
	
	public static void removeIEProcess(int processID) throws SQLException, DatabaseLoadDriverException
	{
		removeAllAnnotationsProperties(processID);
		removeAllAnnotationsSide(processID);
		removeAllAnnotations(processID);
		removeProcessProperties(processID);
		removeCorpusHasProcess(processID);
		removeProcess(processID);
	}

	private static void removeCorpusHasProcess(int processID) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesProcess.removeProcessFromCorpus);
		ps.setInt(1,processID);
		ps.execute();
		ps.close();
	}

	private static void removeProcessProperties(int processID) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesProcess.removeProcessProperties);
		ps.setInt(1,processID);
		ps.execute();
		ps.close();

	}

	private static void removeAllAnnotationsSide(int processID) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesProcess.removeAllAnnotationSides);
		ps.setInt(1,processID);
		ps.execute();
		ps.close();

	}

	private static void removeAllAnnotationsProperties(int processID) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesProcess.removeAllTermsProperties);
		ps.setInt(1,processID);
		ps.execute();
		ps.close();

	}

	private static void removeProcess(int processID) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesProcess.removeProcess);
		ps.setInt(1,processID);
		ps.execute();
		ps.close();

		
	}

	private static void removeAllAnnotations(int processID) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesProcess.removeAllTerms);
		ps.setInt(1,processID);
		ps.execute();
		ps.close();

	}
	
	public static int getCorpusSize(int corpusID) throws SQLException, DatabaseLoadDriverException
	{
		int result = -1;
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesCorpora.corpusSize);
		ps.setInt(1, corpusID);
		ResultSet rs = ps.executeQuery();
		if(rs.next())
		{
			result = rs.getInt(1);
		}
		rs.close();
		ps.close();
		return result;
	}

	public static void updateCorpusName(int corpusID, String name) throws SQLException, DatabaseLoadDriverException {
			PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesCorpora.updateCorpusName);
			ps.setInt(2, corpusID);
			ps.setString(1, name);
			ps.execute();
			ps.close();
	}

}
