package pt.uminho.anote2.aibench.curator.datastructures;

import java.io.Serializable;

import pt.uminho.anote2.aibench.corpus.utils.AnnotationPositions;

public class ANoteDocumentPreviousData implements Serializable {

	private static final long serialVersionUID = 5340167800213807065L;
	private ANoteDocumentText documentText;
	private ANoteDocumentAnnotations annotations;	
	private AnnotationPositions annotNERAnnotations;
	
	public ANoteDocumentPreviousData(ANoteDocumentText documentText,
								     ANoteDocumentAnnotations annotations,
								     AnnotationPositions annotNERAnnotations){
		this.documentText = documentText.clone();
		this.annotations = annotations.clone();
		//this.annotNERAnnotations = annotNERAnnotations.clone();
	}
	
	public ANoteDocumentText getDocumentText() {return documentText;}
	public void setDocumentText(ANoteDocumentText documentText) {this.documentText = documentText.clone();}
	public ANoteDocumentAnnotations getAnnotations() {return annotations;}
	public void setAnnotations(ANoteDocumentAnnotations annotations) {this.annotations = annotations.clone();}

	public void setAnnotNERAnnotations(AnnotationPositions annotNERAnnotations) {
		this.annotNERAnnotations = annotNERAnnotations;
	}

	public AnnotationPositions getAnnotNERAnnotations() {
		return annotNERAnnotations;
	}
}
