package pt.uminho.anote2.carrot.linkage.gui.help;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.tree.TreeSelectionModel;

import org.jdesktop.swingx.JXTreeTable;

import pt.uminho.anote2.carrot.linkage.datastructures.IClustering;
import pt.uminho.anote2.carrot.linkage.datastructures.ILabelCluster;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.utils.conf.GlobalGUIOptions;

public class ClusteringOperationPanel extends JScrollPane{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private IClustering cluster;
	private Map<Integer, IPublication> publications;
	
	private static final List<String> COLUMN_NAMES = new ArrayList<String>();
	private int columnEditable = 3;

	static {
		COLUMN_NAMES.add("Cluster Label Name");
		COLUMN_NAMES.add("Documents");
		COLUMN_NAMES.add("Score");
		COLUMN_NAMES.add("Sel.");
	}
	
	private final JXTreeTable table = new JXTreeTable();
	private ClusterSelectionTreeTableModel treeTableModel;
	
	public ClusteringOperationPanel(IClustering cluster, Map<Integer, IPublication> publications)
	{
		this.cluster=cluster;
		this.publications = publications;
		initGUI();
	}

	private void initGUI() {
		this.updateModel();
		this.setViewportView(table);
		this.table.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.table.getColumn(3).setMaxWidth(GlobalGUIOptions.tableSelectionSize);
		this.table.getColumn(2).setMaxWidth(60);
		this.table.getColumn(1).setMaxWidth(100);


	}

	private synchronized void updateModel() {
		InformationTreeTableNode root = new InformationTreeTableNode(cluster.getName(),COLUMN_NAMES.size());
		InformationTreeTableNode node, leaf;
		for (ILabelCluster label :cluster.getClustersLabels()) {
			node = new LabelClusterTreeTableNode(label,COLUMN_NAMES.size());
			for (Integer docID:label.getLabelDocumentsID()) {
				
				leaf = new PublicationTreeTableNode(publications.get(docID),2);
				node.add(leaf);
			}
			root.add(node);
		}
		this.treeTableModel = new ClusterSelectionTreeTableModel(root,columnEditable,COLUMN_NAMES);
		this.table.setTreeTableModel(this.treeTableModel);	
		this.table.packAll();
	}

	public JXTreeTable getTable() {
		return table;
	}

	
}
