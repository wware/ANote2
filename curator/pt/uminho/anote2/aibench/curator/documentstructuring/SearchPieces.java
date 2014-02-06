package pt.uminho.anote2.aibench.curator.documentstructuring;



public class SearchPieces {
	
	private String documentText;
	
	public SearchPieces(){}

	public void setDocumentText(String documentText) {
		this.documentText = documentText;
	}

	public String getDocumentText() {
		return documentText;
	}
	
	
//	public AnnotationPosition searchPiece(String text, PieceToSearch pieceToSearch)
//	{
//		int i = 0;
//		String lines[] = text.split("\n");
//		
//		if(pieceToSearch.getNParagraphs()>lines.length)
//			return null;
//		
//		String firstWords = pieceToSearch.firstTextWords();
//		
//		
//		while(i<pieceToSearch.getNParagraphs())
//		{
//			if(lines[i].startsWith(firstWords))
//			{
//				
//			}
//		}
//	}
	
}
