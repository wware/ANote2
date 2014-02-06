package pt.uminho.anote2.aibench.curator.datastructures.document.parts;

import java.io.Serializable;

public class CuratorDocumentHTMLAndText implements Serializable {

	private static final long serialVersionUID = 1908339149347314619L;
	
	private String clearText;
	private String htmlTextForEntities;
	private String htmlTextForEntitiesAndClues;
	
	public CuratorDocumentHTMLAndText(String cleaText,String htmlTextForEntities)
	{
		this.clearText=cleaText;
		this.htmlTextForEntities=htmlTextForEntities;
	}
	
	public CuratorDocumentHTMLAndText(String cleaText,String htmlTextForEntities,String htmlTextEntitiesAndClues)
	{
		this.clearText=cleaText;
		this.htmlTextForEntities=htmlTextForEntities;
		this.setHtmlTextForEntitiesAndClues(htmlTextEntitiesAndClues);
	}
			
	public CuratorDocumentHTMLAndText clone(){
		CuratorDocumentHTMLAndText clone = new CuratorDocumentHTMLAndText(clearText,htmlTextForEntities);
		return clone;
	}
	
	public String getClearText() {return clearText;}
	public void setClearText(String clearText) {this.clearText = clearText;}
	public String getHtmlTextForEntities() {return htmlTextForEntities;}
	public void setHtmlTextForEntities(String htmlText) {this.htmlTextForEntities = htmlText;}

	public String getHtmlTextForEntitiesAndClues() {
		return htmlTextForEntitiesAndClues;
	}

	public void setHtmlTextForEntitiesAndClues(
			String htmlTextForEntitiesAndClues) {
		this.htmlTextForEntitiesAndClues = htmlTextForEntitiesAndClues;
	}
}
