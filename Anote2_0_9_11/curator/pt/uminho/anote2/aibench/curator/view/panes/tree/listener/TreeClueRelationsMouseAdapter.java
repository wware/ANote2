package pt.uminho.anote2.aibench.curator.view.panes.tree.listener;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import pt.uminho.anote2.aibench.curator.gui.RelationAddGUI;
import pt.uminho.anote2.aibench.curator.gui.RelationEditGUI;
import pt.uminho.anote2.aibench.curator.view.panes.RECuratorPane;
import pt.uminho.anote2.aibench.curator.view.panes.tree.EventNode;
import pt.uminho.anote2.aibench.curator.view.panes.tree.clues.ClueNode;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.exceptions.process.manualcuration.ManualCurationException;

public class TreeClueRelationsMouseAdapter extends MouseAdapter{
	
	private JTree tree;
	private RECuratorPane reCuratorPane;

	public TreeClueRelationsMouseAdapter(JTree tree, RECuratorPane reCuratorPane)
	{
		this.tree=tree;
		this.reCuratorPane=reCuratorPane;
	}
	
	public void mousePressed(MouseEvent e)
    {
        if(SwingUtilities.isRightMouseButton(e))
        {
            TreePath path = tree.getPathForLocation ( e.getX (), e.getY () );
            if (path == null)
				return;
			tree.setSelectionPath(path);
            Rectangle pathBounds = tree.getUI().getPathBounds(tree,path);
            final DefaultMutableTreeNode obj = (DefaultMutableTreeNode) path.getLastPathComponent();
    		if(obj.getUserObject() instanceof EventNode)
    		{		
    			JPopupMenu menu = new JPopupMenu ();
    			JMenuItem edit = new JMenuItem("Edit");
    			edit.addActionListener(new ActionListener(){
    				public void actionPerformed(ActionEvent evt) {
    					EventNode eventNode = ((EventNode)obj.getUserObject());
						new RelationEditGUI(reCuratorPane.getCuratorDocument(), eventNode.getEvent());
    				}
    			});
                menu.add(edit);
                menu.addSeparator();
    			JMenuItem remove = new JMenuItem("Remove");
    			remove.addActionListener(new ActionListener(){
    				public void actionPerformed(ActionEvent evt) {
    					try {
    						EventNode eventNode = ((EventNode)obj.getUserObject());
    						removeRelationEvent(eventNode.getEvent());
    						removeremoveRelationEventFromTree(obj);
    					} catch (SQLException e) {
    						TreatExceptionForAIbench.treatExcepion(e);
    					} catch (DatabaseLoadDriverException e) {
    						TreatExceptionForAIbench.treatExcepion(e);
    					} catch (ManualCurationException e) {
    						TreatExceptionForAIbench.treatExcepion(e);
						}
    				}
    			});
                menu.add(remove);
                menu.show( tree, pathBounds.x, pathBounds.y + pathBounds.height );
    		}
    		else if(obj.getUserObject() instanceof ClueNode)
    		{
				final ClueNode clueNodeNode = ((ClueNode)obj.getUserObject());
    			JPopupMenu menu = new JPopupMenu ();
                JMenuItem addRelation = new JMenuItem("Add Relation");
                addRelation.addActionListener(new ActionListener(){
    				public void actionPerformed(ActionEvent evt) {
    					new RelationAddGUI(reCuratorPane.getCuratorDocument(), clueNodeNode.getPosition(), true);
    				}
    			});
                menu.add(addRelation);
                menu.addSeparator();
    			JMenuItem removeAll = new JMenuItem("Remove Clue");
    			removeAll.addActionListener(new ActionListener(){
    				public void actionPerformed(ActionEvent evt) {
    					try {
    						removeRelationEvent(clueNodeNode);
    						removeClueFromTree(obj);
    					} catch (SQLException e) {
    						TreatExceptionForAIbench.treatExcepion(e);
    					} catch (DatabaseLoadDriverException e) {
    						TreatExceptionForAIbench.treatExcepion(e);
    					} catch (ManualCurationException e) {
    						TreatExceptionForAIbench.treatExcepion(e);
						}
    				}
    			});
                menu.add(removeAll);
                menu.show( tree, pathBounds.x, pathBounds.y + pathBounds.height );
    		}
        }
    }

	private void removeClueFromTree(DefaultMutableTreeNode obj) throws SQLException,DatabaseLoadDriverException{
	    DefaultTreeModel model = (DefaultTreeModel) (tree.getModel());
	    model.removeNodeFromParent(obj);
	}

	private void removeRelationEvent(ClueNode clueNodeNode) throws SQLException,DatabaseLoadDriverException,ManualCurationException{
		reCuratorPane.removeAllEventsFromClue(clueNodeNode.getPosition());

	}

	private void removeremoveRelationEventFromTree(DefaultMutableTreeNode obj) {
	    DefaultTreeModel model = (DefaultTreeModel) (tree.getModel());
	    if(obj.getParent().getChildCount() == 1)
	    {
		    model.removeNodeFromParent((DefaultMutableTreeNode) obj.getParent());  	
	    }
	    else
	    {
		    model.removeNodeFromParent(obj);
	    }
	}

	private void removeRelationEvent(IEventAnnotation event) throws SQLException,DatabaseLoadDriverException,ManualCurationException {
		reCuratorPane.removeEvent(event);
	}

}
