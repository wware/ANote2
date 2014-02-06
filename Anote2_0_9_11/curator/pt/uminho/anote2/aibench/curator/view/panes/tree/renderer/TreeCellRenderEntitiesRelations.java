package pt.uminho.anote2.aibench.curator.view.panes.tree.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.SQLException;

import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import pt.uminho.anote2.aibench.corpus.structures.CorporaProperties;
import pt.uminho.anote2.aibench.curator.view.panes.tree.EventNode;
import pt.uminho.anote2.aibench.curator.view.panes.tree.TreeNodeTextBold;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;

public class TreeCellRenderEntitiesRelations extends DefaultTreeCellRenderer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	
	public TreeCellRenderEntitiesRelations()
	{
		super();
	}
	
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		Component component = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		DefaultMutableTreeNode mut = (DefaultMutableTreeNode) value;			
		if(mut.getUserObject() instanceof IEntityAnnotation)
		{	
			IEntityAnnotation ent = (IEntityAnnotation) mut.getUserObject();
			this.setFont(new Font("Arial", Font.BOLD, 11));	
			{
				String color = Color.blue.toString();
				try {
					color = CorporaProperties.getCorporaClassColor(ent.getClassAnnotationID()).getColor();
				} catch (SQLException e) {
					TreatExceptionForAIbench.treatExcepion(e);
				} catch (DatabaseLoadDriverException e) {
					TreatExceptionForAIbench.treatExcepion(e);
				}
				this.setForeground(Color.decode(color));
			}
		}
		else if(mut.getUserObject() instanceof EventNode)
		{
			this.setFont(new Font("Arial", Font.BOLD, 11));
		}
		else if(mut.getUserObject() instanceof TreeNodeTextBold)
		{
			this.setFont(new Font("Arial", Font.BOLD, 11));
		}
		else
			this.setFont(new Font("Arial", Font.PLAIN, 12));	
		this.setIcon(null);	
		UIManager.put("Tree.Icon", null);		
		return component;
	}
}
