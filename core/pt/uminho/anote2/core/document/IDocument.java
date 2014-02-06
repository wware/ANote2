package pt.uminho.anote2.core.document;

import pt.uminho.anote2.core.database.IDatabase;

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
	
	public String getFullTextFromDatabase(IDatabase db);
	
	public String getFullTextFromURL(String url);
	
	public void setFullTExtOnDatabase(IDatabase db,String fullText);
	
	public void setFullTExt(String fullText);
	
	public String getContent();
	
}
