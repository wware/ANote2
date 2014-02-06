package pt.uminho.anote2.core.database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;

/**
 * This interface implements a general database credentials 
 * 
 * @author Hugo Costa
 * 
 * @version 1.0 (16 Junho 2009)
 *
 */
public interface IDatabase {
	

	/**
	 * Method that open a database Connection
	 * 
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 * @throws DatabaseCredentialsException 
	 */
	public void openConnection() throws DatabaseLoadDriverException, SQLException;
	
	/**
	 * Method that close a database connection
	 */
	public void closeConnection();
	
	
	/**  
	 * Method that change driver database
	 */
	public void setDriverClassName(String driverName);
	
	/**
	 * Method that return a Connection at database
	 * 
	 * @return
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 * @throws DatabaseCredentialsException 
	 */
	public Connection getConnection() throws DatabaseLoadDriverException, SQLException;
	
	/**
	 * Method that return a Schema database connection
	 * 
	 * @return Schema
	 */
	
	public String getSchema();
	/**
	 * Method that return Host of database connection
	 * 
	 * @return Host
	 */
	public String getHost();
	
	/**
	 * Method that return Port of database connection
	 * 
	 * @return Port
	 */
	public String getPort();
	
	/**
	 * Method that return User of database connection
	 * 
	 * @return User
	 */
	public String getUser();
	
	/**
	 * Method that return Password of database connection
	 * 
	 * @return Password
	 */
	public String getPwd();
	
	/**
	 * Method for create a new connection
	 * 
	 * @return {@link Connection}
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 * @throws DatabaseCredentialsException 
	 */
	public Connection getNeWConnection() throws DatabaseLoadDriverException, SQLException;
	
	
	/**
	 * 
	 * Fill database tables
	 * 
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 * @throws DatabaseCredentialsException 
	 */
	public void fillDataBaseTables() throws DatabaseLoadDriverException, SQLException, FileNotFoundException, IOException;
	
	/**
	 * 
	 * Create a Database
	 * 
	 * @param user
	 * @param pwd
	 * @return
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 * @throws DatabaseCredentialsException 
	 */
	public boolean createDataBase() throws DatabaseLoadDriverException, SQLException ;
	
	
	/**
	 * Update database
	 * 
	 * @throws DatabaseLoadDriverException 
	 * @throws DatabaseCredentialsException 
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public void updateDatabase() throws DatabaseLoadDriverException, SQLException;
	
	
	/**
	 * Test if database is updated
	 * 
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public boolean isDatabaseOutOfDate();
	
	/**
	 * Test if database exists
	 * @return
	 * @throws DatabaseLoadDriverException 
	 * @throws DatabaseCredentialsException 
	 * @throws SQLException 
	 */
	public boolean existDatabase() throws DatabaseLoadDriverException, SQLException;
	
	/**
	 * GetDatabaseType
	 * 
	 * @return {@link DataBaseTypeEnum}
	 */
	public DataBaseTypeEnum getDataBaseType();
	
	/**
	 * Return if databse tables has fill in database
	 * 
	 * @return
	 * @throws DatabaseLoadDriverException 
	 * @throws DatabaseCredentialsException 
	 * @throws SQLException 
	 */
	public boolean isfill() throws DatabaseLoadDriverException, SQLException;
}
