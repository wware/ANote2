package pt.uminho.anote2.aibench.resources.datastructures.tree;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import pt.uminho.anote2.datastructures.utils.tree.InterfaceTreeNode;
import pt.uminho.anote2.resource.ontologies.ITermBDID;

public class TreeOntology<T extends ITermBDID> implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3435885203331418367L;
	protected InterfaceTreeNode<T> rootNode;
    protected int size;

    public TreeOntology() {
        this.rootNode = null;
        size = 0;
    }

    public void setRootNode(InterfaceTreeNode<T> node) {
        rootNode = node;
        size = subTreeSize(node);
        size=1;
    }

    public void setRootNode(T value) {
        rootNode = new TreeNodeOntology<T>(value);
        rootNode.setParent(null);
        size = 1;
    }

    public InterfaceTreeNode<T> getRootNode() {
        return rootNode;
    }

    public InterfaceTreeNode<T> parent(InterfaceTreeNode<T> node) {
        return node.getParent();
    }

    public int getNumberOfChidren(InterfaceTreeNode<T> node) {
        return node.getNumberOfChildren();
    }

    public InterfaceTreeNode<T> getChildren(InterfaceTreeNode<T> node, int position) {
        return node.getChild(position);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return rootNode == null;
    }

    public void setChildValue(T value, InterfaceTreeNode<T> node, int childPosition) throws Exception {
        InterfaceTreeNode<T> newNode = new TreeNodeOntology<T>(value);
        InterfaceTreeNode<T> childNode = node.getChild(childPosition);
        size -= subTreeSize(childNode);
        newNode.setParent(node);
        node.setChild(childPosition, newNode);
        size++;
    }

    public void setChildValue(InterfaceTreeNode<T> newNode, InterfaceTreeNode<T> node, int childPosition) {
        int subTreeSize = subTreeSize(node);
        size -= subTreeSize;
        newNode.setParent(node);
        node.setChild(childPosition, newNode);
        size++;
    }


	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static int subTreeSize(InterfaceTreeNode node) {
        if (node == null)
            return 0;

        int size = 0;
        Queue<InterfaceTreeNode> nodeQueue = new LinkedList();
        Set<Integer> list = new HashSet<Integer>();
        nodeQueue.add(node);
        list.add(node.getID());
        InterfaceTreeNode currentNode;
        while (nodeQueue.size() > 0) {
        	currentNode = nodeQueue.remove();
            int numberOfChildren = currentNode.getNumberOfChildren();
            for (int i = 0; i < numberOfChildren; i++) {
                InterfaceTreeNode childNode = currentNode.getChild(i);
                if (childNode != null && !list.contains(childNode.getID()))
                {
                    nodeQueue.add(childNode);
                    list.add(node.getID());
                }
            }
            size++;
        }
        return size;
    }

    public void setChildValue(TreeOntology<T> tree, InterfaceTreeNode<T> node, int childPosition) {
        int subTreeSize = subTreeSize(node);
        size -= subTreeSize;
        InterfaceTreeNode<T> newNode = tree.getRootNode();
        newNode.setParent(node);
        node.setChild(childPosition, newNode);
        size += tree.size();
    }

    public int depth() {
        return depth(rootNode);
    }


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static int depth(InterfaceTreeNode node) {
        if (node.getParent() == null)
            return 0;

        int depth = 0;
        Queue<InterfaceTreeNode> nodeQueue = new LinkedList();
        nodeQueue.add(node.getParent());

        while (nodeQueue.size() > 0) {
            InterfaceTreeNode parentNode = nodeQueue.remove();
            InterfaceTreeNode grandParentNode = parentNode.getParent();
            if (grandParentNode != null)
                nodeQueue.add(grandParentNode);
            depth++;
        }

        return depth;
    }

    public int height() {
        return height(rootNode);
    }

	@SuppressWarnings("rawtypes")
	public static int height(InterfaceTreeNode node) {
        int height = 0;
        return height;
    }

}
