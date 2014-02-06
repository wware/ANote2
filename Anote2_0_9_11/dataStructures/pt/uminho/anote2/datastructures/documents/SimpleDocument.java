package pt.uminho.anote2.datastructures.documents;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesDocument;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;

public class SimpleDocument extends Observable implements IDocument{
	
	private String sourceURL; 
	private String title;
	private String authors;
	private String abstractSection;
	private String content; //plain text
	private int id;
	
	public SimpleDocument(int id){
		this.id=id;
	}
	
	public SimpleDocument(int id,String title,String aBstract,String authors){
		this.title=title;
		this.abstractSection=aBstract;
		this.authors=authors;
		this.sourceURL=null;
		this.content=null;
		this.id=id;
	}
	
	public String getSourceURL() {
		return this.sourceURL;
	}

	public void setSourceURL(String url) {
		this.sourceURL=url;
	}
	
	public String getContent() {
		return this.content;
	}	

	public String toXml() {
		return null;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getID() {
		return this.id;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthors() {
		return authors;
	}
	public void setAuthors(String authors) {
		this.authors = authors;
	}
	public String getAbstractSection() {
		return abstractSection;
	}
	public void setAbstractSection(String abstractSection) {
		this.abstractSection = abstractSection;
	}

	public void setID(int id) {
		this.id = id;
	}

	public String getFullTextFromDatabase() throws SQLException, DatabaseLoadDriverException {	
		String fulltext = new String();	
		PreparedStatement getFullTExtPub = Configuration.getDatabase().getConnection().prepareStatement(QueriesDocument.getPubFullText);
		getFullTExtPub.setInt(1,getID());
		ResultSet rs = getFullTExtPub.executeQuery();
		if(rs.next())
		{
			fulltext = rs.getString(1);
		}
		rs.close();
		getFullTExtPub.close();
		if(fulltext==null)
		{
			fulltext = new String();
		}
		return fulltext;
	}

	public void setFullTExtOnDatabase(String fulltext) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement getFullTExtPub = Configuration.getDatabase().getConnection().prepareStatement(QueriesDocument.updatePubFullText);
		getFullTExtPub.setNString(1,fulltext);
		getFullTExtPub.setInt(2,getID());
		getFullTExtPub.execute();
		getFullTExtPub.close();
		setContent(fulltext);
	}

	public String getFullTextFromURL(String url) {
		PDFtoText pdfText = new PDFtoText();
		return pdfText.convertPDFDocument(url);
	}

	public void setFullTExt(String fullText) {
		this.content=fullText;		
	}



}
