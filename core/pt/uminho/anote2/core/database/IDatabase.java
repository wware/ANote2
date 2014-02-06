package pt.uminho.anote2.core.database;

import java.sql.Connection;
import java.sql.ResultSet;

import pt.uminho.anote2.core.configuration.IConfiguration;

/**
 * This interface implements a general database credentials 
 * 
 * @author Hugo Costa
 * 
 * @version 1.0 (16 Junho 2009)
 *
 */
public interface IDatabase extends IConfiguration{
	

	/**
	 * Method that open a database Connection
	 */
	public void openConnection();
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
	 */
	public Connection getConnection();
	
	/**
	 * Method that test if statement exist on database
	 * 
	 * @param statement - Query 
	 * @return true if query exist
	 * 	   -- false otherwise
	 */
	public boolean exists(String statement);
	
	/**
	 * Method that execute a query and return a ResultSet of them
	 * 
	 * @param stmt - Query
	 * @return ResultSet - Mathing query results
	 * 		   null - if don't have results
	 */
	public ResultSet executeQuery(String stmt);
	
	
	/**
	 * Method that execute a insert or update on database
	 * 
	 * @param stmt - Query
	 */
	public void executeUpdate(String stmt);
	
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
	 * 
	 * Create a Database
	 * 
	 * @param user
	 * @param pwd
	 * @return
	 */
	public void createDataBase(String pathFile);
}
