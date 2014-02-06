package pt.uminho.anote2.aibench.utils.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;

import pt.uminho.anote2.datastructures.utils.tree.InterfaceTreeNode;

public class JTreeCellRenderer extends DefaultTreeCellRenderer{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4882871693369675818L;

	public JTreeCellRenderer()
	{
		super();
	}
	
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		Component component = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		this.setFont(new Font("Arial", Font.BOLD, 13));		
		{	
			if(row==0)
				this.setFont(new Font("Arial", Font.BOLD, 11));
			else
				this.setFont(new Font("Arial", Font.PLAIN, 12));
		}	
		this.setIcon(null);	
		UIManager.put("Tree.Icon", null);		
		return component;
	}


	
	public static class MyTreeCellRendererOntology extends DefaultTreeCellRenderer{
		private static final long serialVersionUID = -4777650803406195524L;
		public MyTreeCellRendererOntology()
		{
			super();			
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
		{
			Component component = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

			InterfaceTreeNode<String> mut = (InterfaceTreeNode<String>) value;
			if(mut.getParent()==null)
			{
					this.setFont(new Font("Arial", Font.BOLD, 14));
					this.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/bynalinha.png")));					

			}
			else if(mut.isLeaf())
			{
				this.setForeground(Color.BLACK);
				this.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/bynalinha1.png")));

			}
			else
			{
				this.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/bynalinha.png")));
			}
			return component;
		}
	}
}
