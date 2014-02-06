package pt.uminho.anote2.datastructures.documents.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesIRProcess;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesProcess;
import pt.uminho.anote2.datastructures.database.schema.DatabaseTablesName;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;

/**
 * Class for Manager Query in Database
 * 
 * @author Hugo Costa
 *
 */
public class QueryManager {
	
	/** Insert the query on database and returns its id. If return value is equals to -1, an error was occurred 
	 * @throws SQLException 
	 * @throws DatabaseLoadDriverException */
	public static int insertQuery(String keywords,String organism,int publications,int abstracts,Properties properties,boolean ispubmed) throws DatabaseLoadDriverException, SQLException{
		int id_query=-1;
		id_query = HelpDatabase.getNextInsertTableID(DatabaseTablesName.queries);
		PreparedStatement statement = Configuration.getDatabase().getConnection().prepareStatement(QueriesProcess.insertQuery);
		statement.setNString(1, Utils.currentTime());
		statement.setNString(2, keywords);
		statement.setNString(3, organism);	
		statement.setInt(4, publications);	
		statement.setInt(5, abstracts);	
		statement.setBoolean(6,ispubmed);	
		statement.execute();
		statement.close();
		insertQueryproperties(id_query,properties);		
		return id_query;
	}
	
	public static void addQueryProperty(int queryID,String propertyName,String propertyValue) throws SQLException, DatabaseLoadDriverException
	{
		PreparedStatement statement = Configuration.getDatabase().getConnection().prepareStatement(QueriesIRProcess.insertQueryProperties);	
		statement.setInt(1,queryID);
		statement.setNString(2, propertyName);
		statement.setNString(3,propertyValue);
		statement.execute();	
		statement.close();
	}

	private static void insertQueryproperties(int id_query,Properties properties) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement statement = Configuration.getDatabase().getConnection().prepareStatement(QueriesIRProcess.insertQueryProperties);	
		Enumeration<Object> itObj = properties.keys();
		while(itObj.hasMoreElements())
		{
			Object propertyNameObj = itObj.nextElement();
			String propertyName = (String) propertyNameObj ;
			Object propertyValueObj = properties.getProperty(propertyName);
			statement.setInt(1,id_query);
			statement.setNString(2, propertyName);
			statement.setNString(3,(String) propertyValueObj);
			statement.execute();	
		}
		statement.close();
	}
	
	/**
	 * Add Publication To A Query
	 * 
	 * @param queryID
	 * @param publictionID
	 * @throws SQLException
	 * @throws DatabaseLoadDriverException
	 */
	public static  void addPublicationToQuery(int queryID,IPublication publictionID) throws SQLException, DatabaseLoadDriverException{
		PreparedStatement p_stat;
		p_stat = Configuration.getDatabase().getConnection().prepareStatement(QueriesPublication.insertPublicationOnQuery);
		p_stat.setInt(1, queryID);
		p_stat.setInt(2, publictionID.getID());
		p_stat.execute();
		p_stat.close();

	}
	
	/**
	 * Add A {@link List} {@link IPublication} to a Query
	 * 
	 * @param queryID
	 * @param publictionIDs
	 * @throws SQLException
	 * @throws DatabaseLoadDriverException
	 */
	public static  void addPublicationsToQuery(int queryID,List<IPublication> publictionIDs) throws SQLException, DatabaseLoadDriverException{
		PreparedStatement p_stat;
		p_stat = Configuration.getDatabase().getConnection().prepareStatement(QueriesPublication.insertPublicationOnQuery);
		p_stat.setInt(1, queryID);
		for(IPublication pub : publictionIDs)
		{
			p_stat.setInt(2, pub.getID());
			p_stat.execute();
		}
		p_stat.close();
	}
	
	/**
	 * Refresh Query information
	 * 
	 * @param queryID
	 * @param nPublicacoes
	 * @param nAbstracts
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 */
	public static void refreshQueryInfo(int queryID,int nPublicacoes,int nAbstracts) throws SQLException, DatabaseLoadDriverException{

		PreparedStatement statement;
		statement = Configuration.getDatabase().getConnection().prepareStatement(QueriesIRProcess.updateQueryInfo);
		statement.setInt(1,nPublicacoes);
		statement.setInt(2,nAbstracts);
		statement.setString(3,Utils.currentTime());
		statement.setInt(4,queryID);
		statement.execute();
		statement.close();
	}
	
	/**
	 * Inactivate Query in Database
	 * 
	 * @param queryID
	 * @throws SQLException
	 * @throws DatabaseLoadDriverException
	 */
	public static void removeQueryFromDatabase(int queryID) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement ps;
		ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesIRProcess.inactiveQuery);
		ps.setInt(1,queryID);
		ps.execute();
		ps.close();

	}

	/**
	 * Update Query Name in Database
	 * 
	 * @param queryID
	 * @param name
	 * @throws SQLException
	 * @throws DatabaseLoadDriverException
	 */
	public static void updateQueryName(int queryID ,String name) throws SQLException, DatabaseLoadDriverException {
			PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesIRProcess.renameQuery);
			ps.setInt(2,queryID);
			ps.setString(1,name);
			ps.execute();
			ps.close();		
	}

}
