package pt.uminho.anote2.aibench.curator.view.panes;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import pt.uminho.anote2.aibench.corpus.datatypes.NERDocumentAnnotation;
import pt.uminho.anote2.aibench.curator.view.panes.tree.renderer.CuratorDocumentDetailsTreeCellRenderer;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;


public class DocumentPropertiesPanel extends JTabbedPane{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private NERDocumentAnnotation doc;
	private JTree treeStructure;
	private JPanel jpanelDocumentLAbels;
	private JPanel jpanelLinkOut;
	private JButton jButtonPMID;

	public DocumentPropertiesPanel(NERDocumentAnnotation doc) throws SQLException, DatabaseLoadDriverException
	{
		super();
		this.doc = doc;
		initGUI();
	}
	

	private void initGUI() throws SQLException, DatabaseLoadDriverException {
		{
			this.add("Document Link-out", getLinkOutPanel());
			this.add("Document Info",getDocumentInfo());
			this.add("Document Labels",getDocumentLabelPanel());
		}
		
	}

	private Component getDocumentLabelPanel() {
		if(jpanelDocumentLAbels == null)
		{
			jpanelDocumentLAbels = new JPanel();

		}
		return jpanelDocumentLAbels;
	}


	private DefaultTreeModel getTreeStructureModel() throws SQLException, DatabaseLoadDriverException{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("Document Info");
		DefaultMutableTreeNode subnode;
		if(doc.getType()!=null && doc.getType().length()>0)
		{
			subnode = new DefaultMutableTreeNode("Document Type:");
			node.add(subnode);
			subnode = new DefaultMutableTreeNode(doc.getType());
			node.add(subnode);
		}
		DefaultTreeModel model = new DefaultTreeModel(node);
		return model;
	}

	private Component getDocumentInfo() throws SQLException, DatabaseLoadDriverException {
		if(treeStructure==null)
		{
			treeStructure = new JTree(getTreeStructureModel());
			treeStructure.setCellRenderer(new CuratorDocumentDetailsTreeCellRenderer());
		}
		return treeStructure;
	}


	private Component getLinkOutPanel() throws SQLException, DatabaseLoadDriverException {
		if(jpanelLinkOut==null)
		{
			jpanelLinkOut = new JPanel();
			GridBagLayout jpanelLinkOutLayout = new GridBagLayout();
			jpanelLinkOutLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jpanelLinkOutLayout.rowHeights = new int[] {7, 7, 7};
			jpanelLinkOutLayout.columnWeights = new double[] {0.1, 0.1, 0.1};
			jpanelLinkOutLayout.columnWidths = new int[] {7, 7, 7};
			jpanelLinkOut.setLayout(jpanelLinkOutLayout);
			jpanelLinkOut.add(getJButtonOtherID(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jpanelLinkOut;
	}

	private JButton getJButtonOtherID() throws SQLException, DatabaseLoadDriverException {
		if(jButtonPMID == null) {
			jButtonPMID = new JButton();
			jButtonPMID.setBorder(BorderFactory.createTitledBorder(doc.getType()));
			if(doc.getExternalLink()!=null && doc.getExternalLink().length() > 0 )
			{
				jButtonPMID.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						try {
							Help.internetAccess(doc.getExternalLink());
						} catch (IOException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						}
					}
				});
			}
			if(doc.getOtherID()==null || doc.getOtherID().length() < 0)
				jButtonPMID.setText("Without");
			else
				jButtonPMID.setText(doc.getOtherID());
		}
		return jButtonPMID;
	}

}
