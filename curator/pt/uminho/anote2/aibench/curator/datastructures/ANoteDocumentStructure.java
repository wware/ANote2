package pt.uminho.anote2.aibench.curator.datastructures;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import pt.uminho.anote2.aibench.curator.utils.ParsingUtils;

public class ANoteDocumentStructure implements Serializable {

	private static final long serialVersionUID = -402834557520192646L;
	private Map<String,String> pieces = new HashMap<String,String>(); //Hash with pairs of (complex tag,clear text)
	private String piecesNames[] = {"JOURNAL", "ABSTRACT", "TITLE", "REFERENCES", "AUTHORS", "SECTION"};
		
	public ANoteDocumentStructure(){
		this.pieces = new HashMap<String, String>();
	}
	
	public ANoteDocumentStructure(Map<String, String> pieces, String[] piecesNames) {
		this.pieces = pieces;
		this.piecesNames = piecesNames;
	}
	
	/**
	 * PAra as partes do texto
	 * 
	 * @param htmlText
	 * @return
	 */
	public String complexTagToHtml(String htmlText){
		String resultext = htmlText, tokenHtml;
		int index1, index2, i=0;
		
		while(i<piecesNames.length-1)
		{
			index1 = resultext.indexOf("<" + piecesNames[i] + ">");
		
			if(index1 != -1)
			{
				index1 += 2 + piecesNames[i].length();
				index2 = resultext.indexOf("</" + piecesNames[i] + ">");
				
				tokenHtml = resultext.substring(index1, index2);
				String tokenclean = ParsingUtils.clearText(tokenHtml);
				
				this.pieces.put(piecesNames[i], tokenclean);
				
				if(piecesNames[i] == "ABSTRACT")
					resultext = resultext.replace("<" + piecesNames[i] + ">" + tokenHtml + "</" + piecesNames[i] + ">", "<DIV STYLE =\"margin-left: 40px; margin-right: 30px; \" align=\"justify\"><br/><b>Abstract:</b><br/>" + tokenHtml + "<br/> <br/></DIV>");
				if(piecesNames[i] == "REFERENCES")
					resultext = resultext.replace("<" + piecesNames[i] + ">" + tokenHtml + "</" + piecesNames[i] + ">", "<br/><b>References:</b><br/>" + tokenHtml + "<br/> <br/>");
				resultext = resultext.replace("<" + piecesNames[i] + ">" + tokenHtml + "</" + piecesNames[i] + ">", "<br/><b>" + tokenHtml + "</b><br/> <br/>");
				
			}
			i++;
		}
		
		index1 = resultext.indexOf("<SECTION>");
		while(index1 != -1)
		{
			index1 += 9;
			index2 = resultext.indexOf("</SECTION>", index1);
			tokenHtml = resultext.substring(index1, index2);
			String tokenclean = ParsingUtils.clearText(tokenHtml);
			
			this.pieces.put("SECTION-"+tokenclean, tokenclean);
			
			resultext = resultext.replace("<SECTION>" + tokenHtml + "</SECTION>", "<br/><b>" + tokenHtml + "</b><br/> <br/>");
			index1 = resultext.indexOf("<SECTION>", index2);
		}
		
		return resultext;
	}


	public Map<String, String> getPieces() {return pieces;}
	public void setPieces(Map<String, String> pieces) {this.pieces = pieces;}
	public String[] getPiecesNames() {return piecesNames;}
	public void setPiecesNames(String[] piecesNames) {this.piecesNames = piecesNames;}
}
