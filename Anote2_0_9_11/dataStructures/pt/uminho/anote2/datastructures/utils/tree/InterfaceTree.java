package pt.uminho.anote2.datastructures.utils.tree;

import java.util.Iterator;

public interface InterfaceTree<T>{
    InterfaceTreeNode<T> root();
    InterfaceTreeNode<T> parent(InterfaceTreeNode<T> node);
    Iterator<InterfaceNode<T>> children(InterfaceNode<T> node);
    int size();
    boolean isEmpty();
    Iterator<pt.uminho.anote2.datastructures.utils.tree.InterfaceNode<T>> nodeIterator();
    void swapNodes(InterfaceNode<T> node,InterfaceNode<T> node1);
    void addTree(InterfaceNode<T> nodeToReplace,InterfaceNode<T> tree);
    int depth();
    int height();
}
