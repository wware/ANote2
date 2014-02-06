package pt.uminho.anote2.aibench.corpus.gui.help.treerenders;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.ClassTreeNode;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.DocumentTreeNode;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.EntityTreeNode;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.MainTermTreeNode;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.ResourceInfoDatabaseIDTreeNode;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.ResourceInfoExternalIDsLabel;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.ResourceInfoExternalIDsTreeNode;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.ResourceInfoPrimaryTermTreeNode;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.ResourceInfoStatusTreeNode;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.ResourceInfoSynonymsTreeNode;

public class TreeClassTermsRenderer extends DefaultTreeCellRenderer{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TreeClassTermsRenderer()
	{
		super();
	}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		Component component = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		DefaultMutableTreeNode mut = (DefaultMutableTreeNode) value;			
		if(mut.getUserObject().getClass().equals(ClassTreeNode.class))
		{	
			this.setFont(new Font("Arial", Font.BOLD, 13));	
			this.setIcon(null);	
		}
		else if(mut.getUserObject().getClass().equals(EntityTreeNode.class)){
			EntityTreeNode node = ((EntityTreeNode) mut.getUserObject());
			if(node.isHaveSynonyms())
			{
				this.setFont(new Font("Arial", Font.BOLD, 12));
			}
			else
			{
				this.setFont(new Font("Arial", Font.PLAIN, 12));	
			}
			this.setIcon(null);	
		}
		else if(mut.getUserObject().getClass().equals(ResourceInfoDatabaseIDTreeNode.class)
				|| mut.getUserObject().getClass().equals(ResourceInfoExternalIDsTreeNode.class)
				|| mut.getUserObject().getClass().equals(ResourceInfoStatusTreeNode.class)
				|| mut.getUserObject().getClass().equals(ResourceInfoSynonymsTreeNode.class)
				|| mut.getUserObject().getClass().equals(ResourceInfoPrimaryTermTreeNode.class))
		{
			this.setFont(new Font("Arial", Font.BOLD, 12));
			this.setIcon(null);	
		}
		else if(mut.getUserObject().getClass().equals(DocumentTreeNode.class))
		{
			this.setFont(new Font("Arial", Font.PLAIN, 12));
		}
		else if(mut.getUserObject().getClass().equals(ResourceInfoExternalIDsLabel.class))
		{
			this.setFont(new Font("Arial", Font.ITALIC, 12));
			this.setForeground(Color.decode("#330000"));
			this.setIcon(null);	
		}
		else if(mut.getUserObject().getClass().equals(MainTermTreeNode.class))
		{
			this.setFont(new Font("Arial", Font.ITALIC, 12));
			this.setForeground(Color.decode("#330000"));
			this.setIcon(null);	
		}
		else
		{
			this.setFont(new Font("Arial", Font.PLAIN, 12));
			this.setIcon(null);	
		}
		
		UIManager.put("Tree.Icon", null);		
		return component;
	}
}
