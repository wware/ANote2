package pt.uminho.anote2.core.document;

import java.sql.SQLException;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;


/**
 * This interface represent a document
 * 
 * 
 * @author Hugo Costa
 *
 * @version 1.1 (13 November 2009)
 */
public interface IDocument{
	
	/**
	 * This method return a ID for a document
	 * 
	 * @return
	 */
	public int getID();
	
	public void setID(int id);
	/**
	 * Method that return the url of file
	 * 
	 * @return url
	 * 		 -- null if file exist in memory or database only
	 */
	public String getSourceURL();
	
	/**
	 * Method that change url Document
	 * 
	 * @param url
	 */
	public void setSourceURL(String url);
	
	/**
	 * Method that return all full text in databse
	 * 
	 * @return
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 */
	public String getFullTextFromDatabase() throws SQLException, DatabaseLoadDriverException;
	
	/**
	 * Method that return fullt text form file ( PDF)
	 * 
	 * @param url
	 * @return
	 */
	public String getFullTextFromURL(String url);
	
	/**
	 * Method that set full TExt in Databse
	 * @param fullText
	 * @throws SQLException 
	 * @throws DatabaseLoadDriverException 
	 */
	public void setFullTExtOnDatabase(String fullText) throws SQLException, DatabaseLoadDriverException;
	
	public void setFullTExt(String fullText);
	
	public String getContent();	
	
}
