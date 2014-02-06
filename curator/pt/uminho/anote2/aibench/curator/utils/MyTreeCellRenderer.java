package pt.uminho.anote2.aibench.curator.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import pt.uminho.anote2.datastructures.annotation.AnnotationColorStyleProperty;

public class MyTreeCellRenderer extends DefaultTreeCellRenderer{
	
	private static final long serialVersionUID = 1L;
	

	
	public MyTreeCellRenderer()
	{
		super();
	}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		Component component = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		DefaultMutableTreeNode mut = (DefaultMutableTreeNode) value;
			
		if(mut.getUserObject().getClass().equals(AnnotationColorStyleProperty.class))
		{	
			this.setText(((AnnotationColorStyleProperty) mut.getUserObject()).getName());

			this.setFont(new Font("Arial", Font.BOLD, 13));
			
			this.setForeground(Color.decode(((AnnotationColorStyleProperty) mut.getUserObject()).getColor()));
		}
		else
			this.setFont(new Font("Arial", Font.PLAIN, 12));
		
		this.setIcon(null);
		
		UIManager.put("Tree.Icon", null);	
		
		return component;
	}

}
