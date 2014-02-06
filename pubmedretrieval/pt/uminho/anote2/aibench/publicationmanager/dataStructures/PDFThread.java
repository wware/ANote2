package pt.uminho.anote2.aibench.publicationmanager.dataStructures;

import java.io.File;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.process.ir.pubmed.PubMedCrawl;

public class PDFThread extends Thread{
	private File file=null;
	String pmid;
	String docs;
	PubMedCrawl crawl;
	
	public PDFThread(PubMedCrawl crawl,String pmid,String docs)
	{
		this.crawl=crawl;
		this.pmid=pmid;
		this.docs=docs;
	
	}
	
	public void run()
	{
		file = crawl.getFullText(pmid,docs);
		crawl.getDatabase();
			if(file!=null)
			{
				
				if(!file.exists())
				{
					updatePmidURL(crawl.getDatabase(),pmid,"na");
				
				}
				else
				{
					updatePmidURL(crawl.getDatabase(),pmid,"available");
				}
			}
			else
			{
			}
	}
	
	private void updatePmidURL(IDatabase db, String pmid, String string) {
		db.executeUpdate("UPDATE publications "+
						 "SET external_links='"+string+"' "+
						 "WHERE pmid='"+pmid+"'");		
	}
	
}
