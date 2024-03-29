package pt.uminho.anote2.core.document;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;


/**
 * Interface that implements a scientific document.
 * 
 * @author Hugo Costa
 *
 * @version 1.1 (14 Novembro 2009)
 */
public interface IPublication extends IDocument{
	
	/**
	 * Method That return pmid of article
	 * 
	 * @return article otherID ( example pmid)
	 * 		-- null if don't exist
	 */
	public String getOtherID();
	
	/**
	 * Method That return Type of article : PMID or Other
	 * 
	 * @return article otherID ( example pmid)
	 * 		-- null if don't exist
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 */
	public String getType() throws SQLException, DatabaseLoadDriverException;
	
	/**
	 * Method that return article title
	 * 
	 * @return article title
	 * 	  -- null if don�t exit 
	 */
	public String getTitle();
	
	/**
	 * Method that return article authors
	 * 
	 * @return article authors
	 * 	  -- null if don�t exit 
	 */	
	public String getAuthors();
	
	/**
	 * Method that return article date
	 * 
	 * @return article date
	 * 	  -- null if don�t exit 
	 */
	public String getYearDate();
	
	/**
	 * Method that return article journal
	 * 
	 * @return article journal
	 * 	  -- null if don�t exit 
	 */
	public String getJournal();
	
	/**
	 * Method that return article publication status
	 * 
	 * @return article article publication status
	 * 	  -- null if don�t exit 
	 */
	public String getStatus();
	
	/**
	 * Method that return article publication volume
	 * 
	 * @return article article publication volume
	 * 	  -- null if don�t exit 
	 */
	public String getVolume();
	
	
	public String getIssue();
	
	/**
	 * Method that return article publication volume pages
	 * 
	 * @return article article publication volume pages
	 * 	  -- null if don�t exit 
	 */
	public String getPages(); 
	
	/**
	 * Method that return article publication external Link
	 * 
	 * @return article article publication external link
	 * 	  -- null if don�t exit 
	 */
	public String getExternalLink();
	
	/**
	 * Method that set article publication external Link
	 * 
	 * @return article article publication external link
	 * 	  -- null if don�t exit 
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 */
	public void setExternalLink(String externalLink) throws SQLException, DatabaseLoadDriverException;
	
	/**
	 * Method that return abstract 
	 * 
	 * @return article abstract
	 * 	  -- null if don�t exit 
	 */
	public String getAbstractSection();
	
	/**
	 * Method that return if publication contains availble free full text
	 * 
	 * @return true - if Free full text
	 * 		   false - otherwise
	 */
	public boolean getAvailableFreeFullTExt();
	
	/**
	 * Method for find if publication have PDF
	 * 
	 * @return
	 */
	public boolean isPDFAvailable();
	
	
	/**
	 * Method to Add PDF File to Document
	 * 
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	public void addPDFFile(File file) throws IOException;
	
	/**
	 * Method to Set PDF File to Document
	 * 
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	public void setPDFFile(File file) throws IOException;
	

}
