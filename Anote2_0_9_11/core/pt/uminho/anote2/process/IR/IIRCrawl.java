package pt.uminho.anote2.process.IR;

import java.util.List;

import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.core.report.processes.ir.IIRCrawlingProcessReport;

/**
 * Interface that implements a journal retrieval process
 * 
 * 
 * @author Hugo Costa
 *
 * @version 1.0 (17 Junho 2009)
 */
public interface IIRCrawl extends IIRProcess{
	
	/**
	 * Method that get pdf document on internet
	 * 
	 * @param pmid Article pmid
	 * @param path Path where pdf are saved
	 * @return Pdf File
	 * 		  -- null if journal retrieval is not possible 
	 */
	public IIRCrawlingProcessReport getFullText(List<IPublication> pubs);
}
