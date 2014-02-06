package pt.uminho.anote2.aibench.utils.gui;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * 
 * @author Rafael Carreira 
 * 		   Hugo Costa
 * 
 * Objectivo : Introduzir beleza nas Jtree
 *
 */
public class MyTreeCellRenderer extends DefaultTreeCellRenderer{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4882871693369675818L;

	public MyTreeCellRenderer()
	{
		super();
	}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		Component component = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		@SuppressWarnings("unused")
		DefaultMutableTreeNode mut = (DefaultMutableTreeNode) value;
			
		//if(mut.getUserObject().getClass().equals(ANoteProperty.class))
		{	
			//this.setText(((ANoteProperty) mut.getUserObject()).getName());

			this.setFont(new Font("Arial", Font.BOLD, 13));
			
			//this.setForeground(Color.decode(((ANoteProperty) mut.getUserObject()).getColor()));
		}
		//else
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

}
