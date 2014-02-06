package pt.uminho.anote2.aibench.curator.view.panes;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import pt.uminho.anote2.aibench.curator.view.panes.tree.log.AnnotationLogNodeNotes;
import pt.uminho.anote2.aibench.curator.view.panes.tree.renderer.AnnotationsLogTreeCellRenderer;
import pt.uminho.anote2.datastructures.annotation.log.AnnotationLog;


public class AnnotationLogPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private JTree jTreeAnnotations;
	private JScrollPane jScrollPaneJtreeAnnotationView;

	public AnnotationLogPanel(List<AnnotationLog> logs)
	{
		initGUI();
		builtLogTreeNodes(logs);
	}

	private void builtLogTreeNodes(List<AnnotationLog> logs) {
		for(AnnotationLog log:logs)
		{
			addAnnotationLogNode(log);
		}
	}

	public void addAnnotationLogNode(AnnotationLog log) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(log);
		if(!log.getNotes().isEmpty())
		{
			DefaultMutableTreeNode subnnode = new DefaultMutableTreeNode(new AnnotationLogNodeNotes(log.getNotes()));
			node.add(subnnode);
		}
		((DefaultMutableTreeNode)jTreeAnnotations.getModel().getRoot()).add(node);
		jTreeAnnotations.updateUI();
	}

	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			this.setPreferredSize(new java.awt.Dimension(603, 512));
			thisLayout.rowWeights = new double[] {0.1};
			thisLayout.rowHeights = new int[] {7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			{
				jScrollPaneJtreeAnnotationView = new JScrollPane();
				this.add(jScrollPaneJtreeAnnotationView, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					DefaultMutableTreeNode root = new DefaultMutableTreeNode("log");
					jTreeAnnotations = new JTree(root);
					jScrollPaneJtreeAnnotationView.setViewportView(jTreeAnnotations);
				}
			}
			jTreeAnnotations.setCellRenderer(new AnnotationsLogTreeCellRenderer());
		}		
	}
	
	

}
