package pt.uminho.anote2.datastructures.documents.publication;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;
import pt.uminho.anote2.datastructures.database.schema.DatabaseTablesName;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;

/**
 * Class that have some Document Database Management Methods
 * 
 * @author Hugo Costa
 *
 */
public class DocumentManager {
	
	/** Get the publications that already existing in the database with typeID
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 * 
	 */
	public static Map<String,Integer> getAllPublicationsOtherIDs(int typeID) throws SQLException, DatabaseLoadDriverException{
		
		Map<String,Integer> otherIDPublicationID = new HashMap<String, Integer>();
		PreparedStatement statement = Configuration.getDatabase().getConnection().prepareStatement(QueriesPublication.otherIDPublication);
		statement.setInt(1, typeID);
		ResultSet res = statement.executeQuery();	
		while(res.next())
		{
			otherIDPublicationID.put(res.getString(1),res.getInt(2));
		}
		return otherIDPublicationID;
	}
	
	/**
	 * Add A {@link List} of {@link IPublication} in database with publicationType
	 * 
	 * @param documents
	 * @param publicationType
	 * @throws DatabaseLoadDriverException
	 * @throws SQLException
	 */
	public static void addDocuments(List<IPublication> documents,String publicationType) throws DatabaseLoadDriverException, SQLException
	{
		int nextElement = HelpDatabase.getNextInsertTableID(DatabaseTablesName.publications);
		int idType = HelpDatabase.initArticleTypeID(publicationType);
		PreparedStatement addPublication = Configuration.getDatabase().getConnection().prepareStatement(QueriesPublication.insertPublication);
		for(IPublication pub : documents)
		{
			validateOherID(addPublication, pub);
			validateTitle(addPublication, pub);
			validateAuthors(addPublication, pub);
			validateDate(addPublication, pub);
			addPublication.setString(5, pub.getYearDate());
			addPublication.setString(6, "");
			addPublication.setString(7, pub.getStatus());
			addPublication.setString(8, pub.getJournal());
			addPublication.setString(9, pub.getVolume());
			addPublication.setString(10, pub.getIssue());
			addPublication.setString(11, pub.getPages());
			addPublication.setString(12, pub.getAbstractSection());
			addPublication.setInt(13, idType);
			addPublication.setString(14, pub.getExternalLink());
			addPublication.execute();
			pub.setID(nextElement++);
		}
		addPublication.close();
	}

	private static void validateAuthors(PreparedStatement addPublication,
			IPublication pub) throws SQLException {
		if(pub.getAuthors().length()>400)
		{
			addPublication.setString(3, pub.getAuthors().substring(0,399));
		}
		else
		{
			addPublication.setString(3, pub.getAuthors());
		}
	}

	private static void validateDate(PreparedStatement addPublication,
			IPublication pub) throws SQLException {
		if(pub.getYearDate().length() > 4)
		{
			addPublication.setString(4, pub.getYearDate().substring(0, 4));
		}
		else if(!pub.getYearDate().equals(""))
		{
			addPublication.setString(4, pub.getYearDate());	
		}
		else
		{
			addPublication.setNull(4,1);
		}
	}

	private static void validateTitle(PreparedStatement addPublication,
			IPublication pub) throws SQLException {
		if(pub.getTitle().length()>400)
		{
			addPublication.setString(2, pub.getTitle().substring(0, 399));
		}
		else
		{
			addPublication.setString(2, pub.getTitle());
		}
	}

	private static void validateOherID(PreparedStatement addPublication,
			IPublication pub) throws SQLException {
		if(pub.getOtherID().length()>200)
		{
			addPublication.setString(1, pub.getOtherID().substring(0,199));
		}
		else
		{
			addPublication.setString(1, pub.getOtherID());
		}
	}

	/**
	 * Add a {@link IPublication} in database with publicationType
	 * 
	 * @param pub
	 * @param publicationType
	 * @throws DatabaseLoadDriverException
	 * @throws SQLException
	 */
	public static void addDocument(IPublication pub,String publicationType) throws DatabaseLoadDriverException, SQLException
	{
		int nextElement = HelpDatabase.getNextInsertTableID(DatabaseTablesName.publications);
		int idType = HelpDatabase.initArticleTypeID(publicationType);
		PreparedStatement addPublication = Configuration.getDatabase().getConnection().prepareStatement(QueriesPublication.insertPublication);
		addPublication.setString(1, pub.getOtherID());
		addPublication.setString(2, pub.getTitle());
		addPublication.setString(3, pub.getAuthors());
		validateDate(addPublication, pub);
		addPublication.setString(5, pub.getYearDate());
		addPublication.setString(6, "");
		addPublication.setString(7, pub.getStatus());
		addPublication.setString(8, pub.getJournal());
		addPublication.setString(9, pub.getVolume());
		addPublication.setString(10, pub.getIssue());
		addPublication.setString(11, pub.getPages());
		addPublication.setString(12, pub.getAbstractSection());
		addPublication.setInt(13, idType);
		addPublication.setString(14, pub.getExternalLink());
		addPublication.execute();
		pub.setID(nextElement++);
		addPublication.close();
	}



}
