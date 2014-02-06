package pt.uminho.anote2.datastructures.utils.tree;

public interface InterfaceNode<T> extends InterfaceTreeNode<T> {
	String getStringRepresentation();
	T getNodeReturnType();
	T getChildNodeReturnType(int childNumber);
    boolean typeCheck();
    InterfaceNode<T> newInstance();
}
