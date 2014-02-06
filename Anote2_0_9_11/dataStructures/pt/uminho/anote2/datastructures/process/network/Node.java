package pt.uminho.anote2.datastructures.process.network;

import java.util.List;

import pt.uminho.anote2.process.IE.network.IAtributes;
import pt.uminho.anote2.process.IE.network.IGraphicsAtribute;
import pt.uminho.anote2.process.IE.network.INode;

public class Node implements INode{
	private int id;
	private String label;
	private String name;
	private List<IAtributes> atributesList;
	private IGraphicsAtribute graphicsAtribute;
	private boolean nodeAlone;
	

	public Node(int id, String label, String name,
			List<IAtributes> atributesList, IGraphicsAtribute graphicsAtribute) {
		super();
		this.id = id;
		this.label = label;
		this.name = name;
		this.atributesList = atributesList;
		this.graphicsAtribute = graphicsAtribute;
		this.nodeAlone = true;
	}
	
	
	public int getID() {
		return id;
	}
	public String getLabel() {
		return label;
	}
	public String getName() {
		return name;
	}
	public List<IAtributes> getAtributesList() {
		return atributesList;
	}
	public IGraphicsAtribute getGraphicsAtribute() {
		return graphicsAtribute;
	}


	public void increaseWeight() {
		graphicsAtribute.increseWith();
	}

	public boolean isNodeAlone() {
		return nodeAlone;
	}

	public void setNodeAlone(boolean newStatus) {
		this.nodeAlone = newStatus;
	}
	
	
}
