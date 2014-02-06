package pt.uminho.anote2.aibench.curator.datastructures.document.parts;


public class CuratorDocumentState {

	private CuratorDocumentAnnotations annotations;
	private CuratorDocumentHTMLAndText documentText;
	

	public CuratorDocumentState() {
		this.annotations = new CuratorDocumentAnnotations();
	}

	public CuratorDocumentState(CuratorDocumentAnnotations annotations) {
		this.annotations = annotations;
	}

	
	public CuratorDocumentAnnotations getAnnotations() {return annotations;}
	public void setAnnotations(CuratorDocumentAnnotations annotations) {this.annotations = annotations;}
	public CuratorDocumentHTMLAndText getDocumentText() {return documentText;}
	public void setDocumentText(CuratorDocumentHTMLAndText documentText) {this.documentText = documentText;}
}
