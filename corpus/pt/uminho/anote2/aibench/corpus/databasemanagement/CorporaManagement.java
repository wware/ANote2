package pt.uminho.anote2.aibench.corpus.databasemanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesAnnotatedDocument;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesProcess;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.utils.GenericPair;
import pt.uminho.anote2.datastructures.utils.Utils;

public class CorporaManagement {
	
	public static int insertPublication(IDatabase db,IPublication pub) throws SQLException
	{		
		int result = -1;
		
		HashMap<String, Integer> tablesSize;
			PreparedStatement add_pub_stat = db.getConnection().prepareStatement(QueriesPublication.insertPublications);
			tablesSize = getDbColumnsInfo(db.getConnection());
			result = addPubToDB(db,add_pub_stat,pub,true,tablesSize );	
		return result;
	}
	
	public static int insertPublicationLowMemory(IDatabase db,IPublication pub,PreparedStatement add_pub_stat,HashMap<String, Integer> tablesSize) throws SQLException
	{		
		int result = -1;
		result = addPubToDB(db,add_pub_stat,pub,true,tablesSize );	
		return result;
	}
	

	protected static int addPubToDB(IDatabase db,PreparedStatement p_statement,IPublication pub, boolean is_new,HashMap<String, Integer> tablesSize) throws NumberFormatException, SQLException{
		int idTypePmid = HelpDatabase.initArticleTypeID(db,"pmid");
		int pubID =  HelpDatabase.getNextInsertTableID(db,"publications");
		// Pmid
		String pmid = pub.getOtherID();
		p_statement.setString(1,pmid);
		addToStatement(pub.getTitle(),2, p_statement, "title", "",tablesSize);	
		addToStatement(pub.getAuthors(),3, p_statement, "authors", "",tablesSize);
		String aux = pub.getDate();
		if(aux!=null && !aux.equals("null") && !aux.equals(""))
		{
			String[] tokens = aux.split("[ ;,-]");
			aux = tokens[0];
			p_statement.setInt(4, Integer.decode(aux));
		}
		else
			p_statement.setString(4,null);
		
		if(aux!=null && !aux.equals(""))
		{
			String dte = Utils.formatDate(aux+";");
			if(dte.length()>=tablesSize.get("fulldate"))
				dte = dte.substring(0,tablesSize.get("fulldate"));
			p_statement.setString(5, dte);
		}
		else
			p_statement.setString(5,null);
		addToStatement("",6, p_statement, "type", null,tablesSize);
		addToStatement(pub.getStatus(),7, p_statement, "status", null,tablesSize);
		addToStatement(pub.getJournal(),8, p_statement, "journal", null,tablesSize);
		addToStatement(pub.getVolume(),9, p_statement, "volume", null,tablesSize);
		addToStatement(pub.getIssue(),10, p_statement, "issue", null,tablesSize);
		addToStatement(pub.getPages(),11, p_statement, "pages", null,tablesSize);
		addToStatement(pub.getAbstractSection(),12, p_statement, "abstract", null,tablesSize);
		// PMid represent the type of article not ID on database
		p_statement.setInt(13,idTypePmid);
		p_statement.execute();
		return pubID;
	}
	
	
	
	
	protected static void addToStatement(String value, int j, PreparedStatement p_statement, String column, String default_value,HashMap<String, Integer> tablesSize) throws SQLException{
		if(value!=null && !value.equals(""))
		{
			if(value.length()>=tablesSize.get(column))
				value = value.substring(0,tablesSize.get(column));
			p_statement.setString(j, value);
		}
		else
			p_statement.setString(j, default_value);
	}
	
    public static HashMap<String, Integer> getDbColumnsInfo(Connection c) throws SQLException{

		HashMap<String, Integer> db_columns = new HashMap<String, Integer>();
		Statement statement = c.createStatement();
		ResultSet res = statement.executeQuery(QueriesPublication.selectPublicationsResume);
		ResultSetMetaData rsmd = res.getMetaData();
		int numberOfColumns = rsmd.getColumnCount();
		for(int i=1;i<=numberOfColumns;i++){
			db_columns.put(rsmd.getColumnName(i),rsmd.getPrecision(i));
		}
		return db_columns;
    }
    
	public static void insertOnDatabaseAnnotations(IDatabase db,int ieprocessID,int corpusID,int docID, IDocument doc) {
		IAnnotatedDocument docAnnot = (IAnnotatedDocument) doc;
		Connection conn = db.getConnection();
		try {
			PreparedStatement insertAnnot = conn.prepareStatement(QueriesAnnotatedDocument.insertEntityAnnotation);
			insertAnnot.setInt(1,ieprocessID);
			insertAnnot.setInt(2,corpusID);
			insertAnnot.setInt(3,docID);
			for(IEntityAnnotation ent :docAnnot.getEntitiesAnnotations())
			{
				insertAnnot.setInt(4, (int) ent.getStartOffset());
				insertAnnot.setInt(5, (int) ent.getEndOffset());
				insertAnnot.setString(6,ent.getAnnotationValue());
				if(ent.getResourceElementID()<1)
				{
					insertAnnot.setNull(7,1);
				}
				else
				{
					insertAnnot.setInt(7,ent.getResourceElementID());
				}
				insertAnnot.setString(8,ent.getAnnotationValueNormalization());
				insertAnnot.setInt(9,ent.getClassAnnotationID());
				insertAnnot.execute();
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	public static int addElementClass(IDatabase db,String classe) {
		Connection connection = db.getConnection();
		if(connection == null){ return -1;}
		else
		{
			try {
				PreparedStatement getClassesIDForClassePS = connection.prepareStatement(QueriesResources.getClassesIDForClasseName);
				getClassesIDForClassePS.setString(1, classe);
				ResultSet rs = getClassesIDForClassePS.executeQuery();
				if(rs.next()){ return rs.getInt(1);}
				else
				{
					PreparedStatement insertClassPS = connection.prepareStatement(QueriesResources.insertClass);
					insertClassPS.setString(1,classe);
					insertClassPS.execute();
					return HelpDatabase.getNextInsertTableID(db,"classes")-1;
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	public static void insertEventOnDataBase(IDatabase db,int corpusID,int ieprocessID, int docID, IEventAnnotation event) {
		PreparedStatement insertAnnot;
		try {
			insertAnnot = db.getConnection().prepareStatement(QueriesAnnotatedDocument.insertEventAnnotation);
			insertAnnot.setInt(1,ieprocessID);
			insertAnnot.setInt(2,corpusID);
			insertAnnot.setInt(3,docID);
			insertAnnot.setInt(4, (int) event.getStartOffset());
			insertAnnot.setInt(5, (int) event.getEndOffset());
			insertAnnot.setNull(6,1);
			insertAnnot.setNull(7,1);
			insertAnnot.setNull(8,1);
			insertAnnot.setNull(9,1);
			insertAnnot.setString(10,event.getOntologycalClass());
			insertAnnot.setString(11,event.getEventClue());
			insertAnnot.execute();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public static void insertEvenEntitySide(PreparedStatement stat, int entID, int i) throws SQLException {
		stat.setInt(2,entID);
		stat.setInt(3,i);
		stat.execute();	
	
	}

	public static void insertEntityAnnotation(PreparedStatement insertAnnot, IEntityAnnotation ent) throws SQLException {	
			insertAnnot.setInt(4, (int) ent.getStartOffset());
			insertAnnot.setInt(5, (int) ent.getEndOffset());
			insertAnnot.setString(6,ent.getAnnotationValue());
			insertAnnot.setNull(7,1);
			insertAnnot.setString(8,ent.getAnnotationValueNormalization());
			insertAnnot.setInt(9,ent.getClassAnnotationID());
			insertAnnot.execute();	
	}

	public static String getNERProcessDesignation(IDatabase db, String intNer) {
		PreparedStatement insertAnnot;
		try {
			insertAnnot = db.getConnection().prepareStatement(QueriesProcess.getProcessDescription);
			insertAnnot.setString(1,intNer);
			ResultSet rs = insertAnnot.executeQuery();
			if(rs.next())
			{
				return rs.getString(1);
			}
			else
			{
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static GenericPair<Integer,Integer> getEventInformation(IDatabase db, Integer annotaionID) {
		PreparedStatement insertAnnot;
		try {
			insertAnnot = db.getConnection().prepareStatement(QueriesAnnotatedDocument.getEventInformation);
			insertAnnot.setInt(1,annotaionID);
			ResultSet rs = insertAnnot.executeQuery();
			if(rs.next())
			{
				return new GenericPair<Integer, Integer>(rs.getInt(1), rs.getInt(2));
			}
			else
			{
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
