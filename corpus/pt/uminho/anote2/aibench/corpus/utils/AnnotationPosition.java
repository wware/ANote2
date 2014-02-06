package pt.uminho.anote2.aibench.corpus.utils;


public class AnnotationPosition implements Cloneable{
	private int start;
	private int end;
	
	public AnnotationPosition(int start, int end){
		this.start = start;
		this.end = end;
	}
	
	public boolean contain(AnnotationPosition a){
		return ( a.getStart()>=start && a.getEnd()<=end ) || ( a.getStart()>=start && a.getStart()<=end );
	}
	
	public boolean equals(AnnotationPosition pos)
	{
		return ((this.start == pos.getStart()) && (this.end == pos.getEnd()));		
	}
	
	public int compareTo(AnnotationPosition pos)
	{
		if(this.equals(pos))
		{
			return 0;
		}
		else
		{
			if(this.start>pos.start)
			{
				return 1;
			}
			else
			{
				return -1;
			}
		}
	}
	
	public AnnotationPosition clone()
	{
		return new AnnotationPosition(this.start,this.end);
	}

	public int getStart() {return start;}
	public void setStart(int start) {this.start = start;}
	public int getEnd() {return end;}
	public void setEnd(int end) {this.end = end;}
}
