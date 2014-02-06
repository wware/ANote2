package pt.uminho.anote2.aibench.curator.view.panes.tree.clues;

import pt.uminho.anote2.datastructures.annotation.AnnotationPosition;

public class ClueNode {
	
	private AnnotationPosition position;
	private String clue;
	
	public ClueNode(AnnotationPosition position,String clue)
	{
		this.position=position;
		this.clue=clue;
	}
	
	public String toString()
	{
		return clue + " ("+position.getStart()+"-"+position.getEnd()+" )";
	}

	public AnnotationPosition getPosition() {
		return position;
	}

}
