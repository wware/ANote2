package pt.uminho.anote2.process.IR;

import java.util.Properties;


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
	 */
	public int getQueryResults(String query);
	
	/**
	 * Search publication online library archives and save details on database
	 * 
	 * @param keywords - The keywords of search
	 * @param organism - Search direct to an Organism 
	 * @param properties - List of Query properties that can be used to specify search
	 * 
	 * @return true - if process success
	 * 		   -- false othewise
	 */
	public boolean search(String keywords,String organism,Properties properties);
}


