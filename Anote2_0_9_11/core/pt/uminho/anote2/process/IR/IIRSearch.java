package pt.uminho.anote2.process.IR;

import java.sql.SQLException;
import java.util.Properties;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.processes.ir.IIRSearchProcessReport;
import pt.uminho.anote2.process.IR.exception.InternetConnectionProblemException;


/**
 * This interface implements a Information retrieval process that search in online library archives 
 * detailed information for scientific articles
 * 
 * @author Hugo Costa
 *
 * @version 1.0 ( 17 junho 2009)
 */
public interface IIRSearch extends IIRProcess{
	
	/**
	 * Method that return a query for library archives search
	 * 
	 * @return - Query
	 */
	public String getQuery();
	
	/**
	 * Method that return the number of query result publication
	 * 
	 * @param query
	 * @return Number of publications mathing for query
	 * @throws InternetConnectionProblemException 
	 */
	public int getQueryResults(String query) throws InternetConnectionProblemException;
	
	/**
	 * Search publication online library archives and save details on database
	 * 
	 * @param keywords - The keywords of search
	 * @param organism - Search direct to an Organism 
	 * @param properties - List of Query properties that can be used to specify search
	 * 
	 * @return IRSearch Report - if process success
	 * 		   -- false othewise
	 * @throws PubmedException 
	 * @throws SQLException 
	 * @throws DatabaseLoadDriverException 
	 * @throws InternetConnectionProblemException 
	 */
	public IIRSearchProcessReport search(String keywords,String organism,Properties properties) throws DatabaseLoadDriverException, SQLException, InternetConnectionProblemException;
}


