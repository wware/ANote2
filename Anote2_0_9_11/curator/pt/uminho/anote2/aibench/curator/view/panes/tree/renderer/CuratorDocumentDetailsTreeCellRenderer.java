package pt.uminho.anote2.aibench.curator.view.panes.tree.renderer;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class CuratorDocumentDetailsTreeCellRenderer extends DefaultTreeCellRenderer{
	
	private static final long serialVersionUID = 1L;
	

	
	public CuratorDocumentDetailsTreeCellRenderer()
	{
		super();
	}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		Component component = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		DefaultMutableTreeNode mut = (DefaultMutableTreeNode) value;			
		if(mut.getUserObject().toString().contains(":"))
		{
			this.setFont(new Font("Arial", Font.BOLD, 12));
		}
		else
		{
			this.setFont(new Font("Arial", Font.PLAIN, 12));	
		}
		this.setIcon(null);	
		UIManager.put("Tree.Icon", null);		
		return component;
	}

}
