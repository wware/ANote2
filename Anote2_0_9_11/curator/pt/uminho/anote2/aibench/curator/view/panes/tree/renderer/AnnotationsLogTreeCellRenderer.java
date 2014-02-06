package pt.uminho.anote2.aibench.curator.view.panes.tree.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import pt.uminho.anote2.datastructures.annotation.log.AnnotationLog;
import pt.uminho.anote2.datastructures.annotation.log.AnnotationLogTypeEnum;

public class AnnotationsLogTreeCellRenderer extends DefaultTreeCellRenderer{
	
	private static final long serialVersionUID = 1L;
	

	
	public AnnotationsLogTreeCellRenderer()
	{
		super();
	}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		Component component = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		DefaultMutableTreeNode mut = (DefaultMutableTreeNode) value;
		Object object = mut.getUserObject();
		if(object instanceof AnnotationLog)
		{
			AnnotationLog log = (AnnotationLog) mut.getUserObject();
			if(log.getType()==AnnotationLogTypeEnum.ENTITYADD)
			{
				component.setForeground(Color.GREEN);
			}
			else if(log.getType()==AnnotationLogTypeEnum.ENTITYUPDATE)
			{
				component.setForeground(Color.MAGENTA);
			}
			else if(log.getType()==AnnotationLogTypeEnum.ENTITYREMOVE)
			{
				component.setForeground(Color.RED);
			}
			else if(log.getType()==AnnotationLogTypeEnum.RELATIONREMOVE)
			{
				component.setForeground(Color.RED);
			}
			else if(log.getType()==AnnotationLogTypeEnum.RELATIONUPDATE)
			{
				component.setForeground(Color.MAGENTA);
			}
			else if(log.getType()==AnnotationLogTypeEnum.RELATIONADD)
			{
				component.setForeground(Color.GREEN);
			}

		}
		this.setFont(new Font("Arial", Font.PLAIN, 12));
		this.setIcon(null);	
		UIManager.put("Tree.Icon", null);		
		return component;
	}

}
