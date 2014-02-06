package pt.uminho.anote2.datastructures.documents;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;
import pt.uminho.anote2.datastructures.utils.FileHandling;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;


public class Publication extends SimpleDocument implements IPublication{
	
	private String pmid;
	private String date;
	private String journal;
	private String status;
	private String volume;
	private String issue;
	private String pages;
	private String extenalLink; //publication extenal Link
	private boolean availableFreePdf;
	private String type;
	
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
		this.extenalLink=null;
		this.type = null;
	}
	
	public Publication(int id,IPublication pub)
	{
		super(id,pub.getTitle(),pub.getAbstractSection(),pub.getAuthors());
		this.pmid=pub.getOtherID();
		this.date=pub.getYearDate();
		this.journal=pub.getJournal();
		this.status=pub.getStatus();
		this.volume=pub.getVolume();
		this.issue=pub.getIssue();
		this.pages=pub.getPages();
		this.extenalLink=pub.getExternalLink();
		try {
			this.type = pub.getType();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DatabaseLoadDriverException e) {
			e.printStackTrace();
		}
	}
	
	public Publication(int id,String otherIDpmid,String title,String authors,String date,String status,String journal,
			String volume,String issue,String pages,String extenalLink,String abstractSection,boolean freefulltextAvailable)
	{
		super(id,title,abstractSection,authors);
		this.pmid=otherIDpmid;
		this.date=date;
		this.status=status;
		this.journal=journal;
		this.volume=volume;
		this.issue=issue;
		this.pages=pages;
		this.extenalLink=extenalLink; 
		this.availableFreePdf=freefulltextAvailable;
		this.type=null;
	}

	public String getYearDate() {
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

	public String getExternalLink() {
		return extenalLink;
	}

	public boolean getAvailableFreeFullTExt() {
		return availableFreePdf;
	}

	public String getOtherID() {
		return pmid;
	}	
	
	public String findFullTextArticle() throws SQLException, DatabaseLoadDriverException {
		String fullText=new String();;
		String id_path = GlobalOptions.saveDocDirectoty+"id"+getID()+".pdf";
		String id_path_otherID = GlobalOptions.saveDocDirectoty+"id"+getID()+"-"+getOtherID()+".pdf";
		String pdf_path = GlobalOptions.saveDocDirectoty+getOtherID()+".pdf";
		fullText = getFullTextFromDatabase();
		if(fullText !=null && !fullText.equals(""))
		{
			return fullText;
		}
		if(new File(id_path).exists())
		{
			fullText = getFullTextFromURL(id_path);
		}
		else if(new File(id_path_otherID).exists())
		{
			fullText = getFullTextFromURL(id_path_otherID);
		}
		else if(new File(pdf_path).exists())
		{
			fullText = getFullTextFromURL(pdf_path);
		}
		if(fullText==null||fullText.equals(""))
		{
		}
		else
		{
			setFullTExtOnDatabase(fullText);
		}
		return fullText;
	}
	
	public boolean isPDFAvailable() {
		String id_path = GlobalOptions.saveDocDirectoty+"id"+String.valueOf(getID())+".pdf";
		String id_path_otherID = GlobalOptions.saveDocDirectoty+"id"+getID()+"-"+pmid+".pdf";
		String pdf_path = GlobalOptions.saveDocDirectoty+pmid+".pdf";
		if(new File(pdf_path).exists()||new File(id_path).exists()||new File(id_path_otherID).exists())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public String toString()
	{
		String result = new String();
		if(getID() > 0)
			result = result + "("+getID()+") ";
		if(getOtherID()!=null && getOtherID().length() > 0)
			result = result + "PMID : "+getOtherID()+" ";
		if(getAbstractSection()!=null && getAbstractSection().length() > 0)
			result = result + "Abstract : "+getAbstractSection()+") ";
		return result;
	}

	@Override
	public void addPDFFile(File file) throws IOException {
		File fileDest;
		if(getOtherID().equals(""))
		{
			fileDest = new File(GlobalOptions.saveDocDirectoty+"id"+getID()+".pdf");
		}
		else
		{
			fileDest = new File(GlobalOptions.saveDocDirectoty+"id"+getID()+"-"+getOtherID()+".pdf");
		};
		fileDest.createNewFile();
		FileHandling.copy(file, fileDest);
	}

	@Override
	public void setPDFFile(File file) throws IOException {
		String id_path = GlobalOptions.saveDocDirectoty+"id"+String.valueOf(getID())+".pdf";
		String id_path_otherID = GlobalOptions.saveDocDirectoty+"id"+getID()+"-"+pmid+".pdf";
		String pdf_path = GlobalOptions.saveDocDirectoty+pmid+".pdf";
		File fileExist = new File(id_path);
		if(fileExist.exists())
		{
			fileExist.delete();
		}
		fileExist = new File(id_path_otherID);
		if(fileExist.exists())
		{
			fileExist.delete();
		}
		fileExist = new File(pdf_path);
		if(fileExist.exists())
		{
			fileExist.delete();
		}
		addPDFFile(file);
	}

	@Override
	public String getType() throws SQLException, DatabaseLoadDriverException {
		if(this.type==null)
		{
			PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesPublication.selectPublicationType);    
			ps.setInt(1,getID());	
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				this.type = rs.getString(1);
			}
			rs.close();
			ps.close();
		}
		return type;
	}

	@Override
	public void setExternalLink(String externalLink) throws SQLException, DatabaseLoadDriverException {
		if(getID()!=-1)
		{
			PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesPublication.updatePublicationExternalLink);    
			ps.setInt(2,getID());	
			ps.setString(1,externalLink);	
			ps.execute();
			extenalLink = externalLink;
		}
	}


}
