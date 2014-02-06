package pt.uminho.anote2.process.ir.pubmed;

import java.io.File;

import pt.uminho.anote2.core.configuration.IProxy;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.document.IDocumentSet;
import pt.uminho.anote2.datastructures.process.IRProcess;
import pt.uminho.anote2.process.IR.IIRCrawl;
import es.uvigo.ei.pdfDownloader.PDFDownloadHandler;

public class PubMedCrawl extends IRProcess implements IIRCrawl{
	
	public PubMedCrawl(IProxy proxy,IDatabase database)
	{
		super(proxy, database);
		setProxy();
	}
	
	public File getFullText(final String pmid,String path){

	
		PubMexAnote ex = new PubMexAnote();
		
		PDFDownloadHandler handler = new PDFDownloadHandler() {
			public void error(Exception exception) {
			}
			
			public void error(String message) {
			}

			public void error(String message, Exception e) {
			}

			public void finished(File file) {

			}

			public void info(String info) {
			}
		};
		
		File file = ex.downloadPDF(pmid,path, handler);

		return file;
	}

	public IDocumentSet getDocuments() { return null;}

	public void setDocuments(IDocumentSet arg0) {}

	public String getType() {
		return "IRCrawl";
	}

	public IDatabase getDB() {
		return getDatabase();
	}

	public int getID() {
		return 0;
	}
}
