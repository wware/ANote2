package pt.uminho.anote2.aibench.curator.datastructures;

public class ANoteDocumentData {

	private ANoteDocumentAnnotations annotations;
	private ANoteDocumentStructure documentStructure;
	private ANoteDocumentText documentText;
	

	public ANoteDocumentData() {
		this.annotations = new ANoteDocumentAnnotations();
		this.documentStructure = new ANoteDocumentStructure();
	}

	public ANoteDocumentData(ANoteDocumentAnnotations annotations, ANoteDocumentStructure documentStructure) {
		this.annotations = annotations;
		this.documentStructure = documentStructure;
	}

	
	public ANoteDocumentAnnotations getAnnotations() {return annotations;}
	public void setAnnotations(ANoteDocumentAnnotations annotations) {this.annotations = annotations;}
	public ANoteDocumentStructure getDocumentStructure() {return documentStructure;}
	public void setDocumentStructure(ANoteDocumentStructure documentStructure) {this.documentStructure = documentStructure;}
	public ANoteDocumentText getDocumentText() {return documentText;}
	public void setDocumentText(ANoteDocumentText documentText) {this.documentText = documentText;}
}
