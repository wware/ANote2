package pt.uminho.anote2.datastructures.process.network;

import java.util.ArrayList;
import java.util.List;

import pt.uminho.anote2.process.IE.network.IAtributes;
import pt.uminho.anote2.process.IE.network.IEdge;
import pt.uminho.anote2.process.IE.network.IGraphicsAtribute;
import pt.uminho.anote2.process.IE.network.INode;

public class Edge implements IEdge {
	
	private String id;
	private String label;
	private INode sourceNode;
	private INode targetNode;
	private List<IAtributes> atributesList;
	private IGraphicsAtribute graphicsAtribute;
	private boolean isDirected;
	
	public Edge(String id, String label, INode sourceNode, INode targetNode, boolean isDirected,
			List<IAtributes> atributesList, IGraphicsAtribute graphicsAtribute
	)
	{
		super();
		this.id = id;
		this.label = label;
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
		this.atributesList = atributesList;
		this.graphicsAtribute = graphicsAtribute;
		this.isDirected = isDirected;
	}
	
	@Override
	public String toString() {
		return "Edge [id=" + id + ", label=" + label + ", sourceNode="
				+ sourceNode + ", targetNode=" + targetNode
				+ ", atributesList=" + atributesList + ", graphicsAtribute="
				+ graphicsAtribute + "]";
	}

	public String getID() {
		return id;
	}
	public String getLabel() {
		return label;
	}

	public List<IAtributes> getAtributesList() {
		return atributesList;
	}
	public IGraphicsAtribute getGraphicsAtribute() {
		return graphicsAtribute;
	}

	public INode getSourceNode() {
		return sourceNode;
	}

	public INode getTargetNode() {
		return targetNode;
	}

	public void increaseWeight() {
		if(graphicsAtribute==null)
		{
			graphicsAtribute = new GraphicsAtribute(1, "#00000", "#FFFFFF",null,true, null, null);
		}
		else
		{
			graphicsAtribute.increseWith();
		}
	}

	@Override
	public boolean isDirected() {
		return isDirected;
	}

	@Override
	public void addEdgeAAtributes(IAtributes atribute) {
		if(atributesList==null)
		{
			atributesList = new ArrayList<IAtributes>();
		}
		atributesList.add(atribute);
	}
	
	public int compareTo(IEdge o) {
		return getID().compareTo(o.getID());
	}
	
}
