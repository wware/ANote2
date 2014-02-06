package pt.uminho.anote2.core.document;


/**
 * Interfeca that define one class who convert pdf to text
 * 
 * @author Hugo Costa
 *
 * @version 1.0 (16 junho 2009)
 */
public interface IPDFtoTXT {
	
	/**
	 * Method that convert a pdf file in a text file
	 * 
	 * @param url - Pdf file url
	 * @return text file
	 */
	public String convertPDFDocument(String url);
}
