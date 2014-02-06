package pt.uminho.anote2.datastructures.documents;

import pt.uminho.anote2.core.document.IPublication;


public class Publication extends SimpleDocument implements IPublication{
	
	private String pmid;
	private String date;
	private String journal;
	private String status;
	private String volume;
	private String issue;
	private String pages;
	private String url; //journal url
	private boolean availableFreePdf;
	
	public Publication(int id)
	{
		super(id);
		this.pmid=null;
		this.date=null;
		this.journal=null;
		this.status=null;
		this.volume=null;
		this.issue=null;
		this.pages=null;		
	}
	
	public Publication(int id,IPublication pub)
	{
		super(id,pub.getTitle(),pub.getAbstractSection(),pub.getAuthors());
		this.pmid=pub.getOtherID();
		this.date=pub.getDate();
		this.journal=pub.getJournal();
		this.status=pub.getStatus();
		this.volume=pub.getVolume();
		this.issue=pub.getIssue();
		this.pages=pub.getPages();
	}
	
	public Publication(int id,String otherIDpmid,String title,String authors,String date,String status,String journal,
			String volume,String issue,String pages,String url,String abstractSection,boolean freefulltextAvailable)
	{
		super(id,title,abstractSection,authors);
		this.pmid=otherIDpmid;
		this.date=date;
		this.status=status;
		this.journal=journal;
		this.volume=volume;
		this.issue=issue;
		this.pages=pages;
		this.url=url; 
		this.availableFreePdf=freefulltextAvailable;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getJournal() {
		return journal;
	}

	public void setJournal(String journal) {
		this.journal = journal;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getPages() {
		return pages;
	}

	public void setPages(String pages) {
		this.pages = pages;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean getAvailableFreeFullTExt() {
		return availableFreePdf;
	}

	public String getOtherID() {
		return pmid;
	}	

}
