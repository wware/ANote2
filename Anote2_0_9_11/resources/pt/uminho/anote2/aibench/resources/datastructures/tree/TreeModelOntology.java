package pt.uminho.anote2.aibench.resources.datastructures.tree;

import java.io.Serializable;
import java.util.Vector;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import pt.uminho.anote2.datastructures.utils.tree.InterfaceTreeNode;
import pt.uminho.anote2.resource.IResourceElement;

public class TreeModelOntology implements TreeModel,Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 3658449583290008114L;

	protected TreeOntology<IResourceElement> treeOntology;
	private Vector<TreeModelListener> treeModelListeners = new Vector<TreeModelListener>();
	
	public TreeModelOntology(TreeOntology<IResourceElement> treeOntology2)
	{
		treeOntology=treeOntology2;
	}
	
	
	public void addTreeModelListener(TreeModelListener l) {
		treeModelListeners.addElement(l);	
	}


	@SuppressWarnings("unchecked")
	public Object getChild(Object parent, int index) 
	{
		InterfaceTreeNode<IResourceElement> node = (InterfaceTreeNode<IResourceElement>) parent;
		return node.getChild(index);
	}

	@SuppressWarnings("unchecked")
	public int getChildCount(Object parent) {
		InterfaceTreeNode<IResourceElement> node = (InterfaceTreeNode<IResourceElement>) parent;	
		return node.getNumberOfChildren();
	}
	

	@SuppressWarnings("unchecked")
	public int getIndexOfChild(Object parent, Object child) {
		if(parent==null||child==null)
		{
			return -1;
		}
		InterfaceTreeNode<IResourceElement> node = (InterfaceTreeNode<IResourceElement>) parent;
		InterfaceTreeNode<IResourceElement> node2 = (InterfaceTreeNode<IResourceElement>) child;
		
		for(int i=0;i<node.getNumberOfChildren();i++)
		{
			InterfaceTreeNode<IResourceElement> nodeAux = node.getChild(i);
			if(nodeAux.compareNodes(node2))
			{
				return i;
			}
			
		}
		return -1;
	}

	@SuppressWarnings("unchecked")
	public boolean isLeaf(Object node) {
		InterfaceTreeNode<IResourceElement> nodeAux = (InterfaceTreeNode<IResourceElement>) node;	
		return nodeAux.getNumberOfChildren()==0;
	}

	public void removeTreeModelListener(TreeModelListener l) {
		treeModelListeners.removeElement(l);	
	}

	public void valueForPathChanged(TreePath path, Object newValue) {

		
	}
	
	@SuppressWarnings("unchecked")
	public Object getParent(Object child)
	{
		InterfaceTreeNode<IResourceElement> childObj = (InterfaceTreeNode<IResourceElement>) child;
		return childObj.getParent();
	}


	public void insertNodeInto(TreeNodeOntology<IResourceElement> node,TreeNodeOntology<IResourceElement> parent)
	{
		parent.setChild(parent.getNumberOfChildren(), node);	
	}


	public Object getRoot() {
		return treeOntology.getRootNode();
	}

	
}
