package pt.uminho.anote2.aibench.curator.datastructures.document.parts;

import java.io.Serializable;

import pt.uminho.anote2.datastructures.annotation.AnnotationPositions;

public class CuratorDocumentPreviousState implements Serializable {

	private static final long serialVersionUID = 5340167800213807065L;
	private CuratorDocumentHTMLAndText documentText;
	private CuratorDocumentAnnotations annotations;	
	private AnnotationPositions annotNERAnnotations;
	
	public CuratorDocumentPreviousState(CuratorDocumentHTMLAndText documentText,
								     CuratorDocumentAnnotations annotations,
								     AnnotationPositions annotNERAnnotations){
		this.documentText = documentText.clone();
		this.annotations = annotations.clone();
		//this.annotNERAnnotations = annotNERAnnotations.clone();
	}
	
	public CuratorDocumentHTMLAndText getDocumentText() {return documentText;}
	public void setDocumentText(CuratorDocumentHTMLAndText documentText) {this.documentText = documentText.clone();}
	public CuratorDocumentAnnotations getAnnotations() {return annotations;}
	public void setAnnotations(CuratorDocumentAnnotations annotations) {this.annotations = annotations.clone();}

	public void setAnnotNERAnnotations(AnnotationPositions annotNERAnnotations) {
		this.annotNERAnnotations = annotNERAnnotations;
	}

	public AnnotationPositions getAnnotNERAnnotations() {
		return annotNERAnnotations;
	}
}
