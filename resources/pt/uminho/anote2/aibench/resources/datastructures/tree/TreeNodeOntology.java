package pt.uminho.anote2.aibench.resources.datastructures.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pt.uminho.anote2.datastructures.utils.tree.InterfaceTreeNode;
import pt.uminho.anote2.resource.IResourceElement;


/**
 * Created by IntelliJ IDEA.
 * User: pedro
 * Date: Aug 8, 2008
 * Time: 11:07:10 AM
 * To change this template use File | Settings | File Templates.
 */

/*
 * Pedrao Warning
 * 
 * Ao defenir um nodo ( na minha opinião ) ao criar um nodo deveria-se colocar logo o pai
 * senão pode criar confusão mais tarde
 * 
 * 
 * 
 */
public class TreeNodeOntology<T> implements InterfaceTreeNode<T>,Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7693345115427824492L;
	protected List<InterfaceTreeNode<T>> childNodes;
    protected InterfaceTreeNode<T> parent;
    protected T value;

    public TreeNodeOntology(T value) {
        childNodes = new ArrayList<InterfaceTreeNode<T>>();
        parent = null;
        this.value = value;
    }

    public InterfaceTreeNode<T> getChild(int childNumber) {
        return childNodes.get(childNumber);
    }

    public void setChild(int childNumber, InterfaceTreeNode<T> node) {
        childNodes.add(childNumber, node);
    }

    public InterfaceTreeNode<T> getParent() {
        return parent;
    }

    public void setParent(InterfaceTreeNode<T> parent) {
        this.parent = parent;
    }

    public boolean isLeaf() {
        return childNodes.size() == 0;
    }

    public int getNumberOfChildren() {
        return childNodes.size();
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public List<InterfaceTreeNode<T>> getChildNodes() {
        return childNodes;  
    }
    
	public boolean compareNodes(InterfaceTreeNode<T> node2)
    {
		if(this.getNumberOfChildren()!=node2.getNumberOfChildren())
    	{
    		return false;
    	}
    	T value1 = this.getValue();
    	T value2 = node2.getValue();
    	return value1.equals(value2);
    	
    }
    
    public String toString()
    {
    	return ((IResourceElement) this.getValue()).getTerm();
    }
    
    // E um novo metodo ver se funciona
	public int getLevelNode() {
		int level=0;
		if(getParent()==null)
		{
			return 0;		
		}
		else
		{
			InterfaceTreeNode<T> node = this.getParent();
			level=1;
			while(node.getParent()!=null)
			{
				node = node.getParent();
				level++;
			}
			return level;
		}
		
	}

	public void removeNode() {
		InterfaceTreeNode<T> parent = this.getParent();
		for(int i=0;i<parent.getNumberOfChildren();i++)
		{
			// Nao sei se isto funciona
			if(parent.getChild(i).compareNodes(this))
			{
				deleteOnParentArray(parent,i);
				return;
			}
		}
	}
	
	private void deleteOnParentArray(InterfaceTreeNode<T> parent,int childPosition)
	{
		if(childPosition==parent.getNumberOfChildren()-1)
		{
			parent.getChildNodes().remove(childPosition);
		}		
		else
		{
			// ver isto muito bem
			// so remove se for do ultimo po primeiro
			int numberchilds = parent.getNumberOfChildren()-1;			
			parent.setChild(childPosition,parent.getChild(numberchilds));
			parent.getChildNodes().remove(numberchilds);
		}
	}

}
