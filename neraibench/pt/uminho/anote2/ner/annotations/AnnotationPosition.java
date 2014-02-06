package pt.uminho.anote2.ner.annotations;


public class AnnotationPosition {
	private int start;
	private int end;
	
	public AnnotationPosition(int start, int end){
		this.start = start;
		this.end = end;
	}
	
	public boolean contain(AnnotationPosition a){
		return ( a.getStart()>=start && a.getEnd()<=end ) || ( a.getStart()>=start && a.getStart()<=end );
	}
	
	public int lenght()
	{
		return getEnd()-getStart();
	}

	public int getStart() {return start;}
	public void setStart(int start) {this.start = start;}
	public int getEnd() {return end;}
	public void setEnd(int end) {this.end = end;}
}
