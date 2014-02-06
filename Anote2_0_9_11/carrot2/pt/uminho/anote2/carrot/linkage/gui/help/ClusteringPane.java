package pt.uminho.anote2.carrot.linkage.gui.help;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import pt.uminho.anote2.carrot.linkage.datastructures.IClustering;
import pt.uminho.anote2.carrot.linkage.datastructures.ILabelCluster;
import pt.uminho.anote2.carrot.linkage.report.ICLusteringReport;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.utils.Utils;


public class ClusteringPane extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ICLusteringReport report;
	private JLabel jLabelClustering;
	private JLabel jLabelDuration;
	private JTextField jTextFieldLabels;
	private JTree jTreePublications;
	private JTree jTreeLabels;
	private JTextField jTextFieldAlgorithm;
	private JTextField jTextFieldClustering;
	private JLabel jLabelAlgorithm;
	private JTextField jTextFieldDuration;
	private JLabel jLabelLabels;
	private JTabbedPane jTabbedPaneClusteringDetails;
	private JPanel jPanelSummary;
	private Map<Integer,IPublication> pub;
	private JScrollPane jScrollPaneTreeLabels;
	private JScrollPane jScrollPaneTreePublications;

	public ClusteringPane(ICLusteringReport report) throws SQLException, DatabaseLoadDriverException
	{
		this.report = report;
		this.pub = getMapPublications();
		initGUI();
		completeGUI();
	}
	
	
	private Map<Integer, IPublication> getMapPublications() throws SQLException, DatabaseLoadDriverException {
		Map<Integer, IPublication> pubs = new HashMap<Integer, IPublication>();
		for(IPublication pub:report.getQuery().getPublications())
		{
			pubs.put(pub.getID(), pub);
		}
		return pubs;
	}


	private void completeGUI() {
		jTextFieldAlgorithm.setText(report.getAlgorithm().toString());
		jTextFieldClustering.setText(report.getClustering().toString());
		jTextFieldDuration.setText(Utils.convertTimeToString(report.getTime()));
		jTextFieldLabels.setText(String.valueOf(report.getClustering().getClustersLabels().size()));
	}

	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			this.setPreferredSize(new java.awt.Dimension(761, 547));
			thisLayout.rowWeights = new double[] {0.0, 0.1};
			thisLayout.rowHeights = new int[] {7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			{
				jPanelSummary = new JPanel();
				GridBagLayout jPanelSummaryLayout = new GridBagLayout();
				this.add(jPanelSummary, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelSummary.setBorder(BorderFactory.createTitledBorder("Summary"));
				jPanelSummaryLayout.rowWeights = new double[] {0.1, 0.1};
				jPanelSummaryLayout.rowHeights = new int[] {7, 7};
				jPanelSummaryLayout.columnWeights = new double[] {0.0, 0.1, 0.1, 0.1};
				jPanelSummaryLayout.columnWidths = new int[] {7, 7, 7, 7};
				jPanelSummary.setLayout(jPanelSummaryLayout);
				{
					jLabelClustering = new JLabel();
					jPanelSummary.add(jLabelClustering, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
					jLabelClustering.setText("Clustering :");
				}
				{
					jLabelLabels = new JLabel();
					jPanelSummary.add(jLabelLabels, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
					jLabelLabels.setText("Labels :");
				}
				{
					jLabelDuration = new JLabel();
					jPanelSummary.add(jLabelDuration, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
					jLabelDuration.setText("Processing Time :");
				}
				{
					jTextFieldDuration = new JTextField();
					jTextFieldDuration.setEditable(false);
					jPanelSummary.add(jTextFieldDuration, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
				}
				{
					jTextFieldLabels = new JTextField();
					jTextFieldLabels.setEditable(false);
					jPanelSummary.add(jTextFieldLabels, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
				}
				{
					jLabelAlgorithm = new JLabel();
					jPanelSummary.add(jLabelAlgorithm, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
					jLabelAlgorithm.setText("Algorithm :");
				}
				{
					jTextFieldClustering = new JTextField();
					jTextFieldClustering.setEditable(false);
					jPanelSummary.add(jTextFieldClustering, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
				}
				{
					jTextFieldAlgorithm = new JTextField();
					jTextFieldAlgorithm.setEditable(false);
					jPanelSummary.add(jTextFieldAlgorithm, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
				}
			}
			{
				jTabbedPaneClusteringDetails = new JTabbedPane();
				this.add(jTabbedPaneClusteringDetails, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jTabbedPaneClusteringDetails.setBorder(BorderFactory.createTitledBorder("Clustering Details"));
				{
					jTreeLabels = new JTree();
					jTreeLabels.setModel(getJtreeLabelsModel());
					jScrollPaneTreeLabels = new JScrollPane();
					jScrollPaneTreeLabels.setViewportView(jTreeLabels);
					jTabbedPaneClusteringDetails.addTab("Organized by Label", null, jScrollPaneTreeLabels, null);
				}
				{
					jTreePublications = new JTree();
					jTreePublications.setModel(getJtreeDocumentsLabelModel());
					jScrollPaneTreePublications = new JScrollPane();
					jScrollPaneTreePublications.setViewportView(jTreePublications);
					jTabbedPaneClusteringDetails.addTab("Organized by Document", null, jScrollPaneTreePublications, null);
				}
			}
		}
		
	}
	
	private DefaultTreeModel getJtreeLabelsModel(){
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("Labels");	
		DefaultMutableTreeNode subchild;
		for(ILabelCluster label: report.getClustering().getClustersLabels())
		{
			
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(label);
			for(int doc:label.getLabelDocumentsID())
			{
				subchild = new DefaultMutableTreeNode(pub.get(doc));
				child.add(subchild);
			}		
			node.add(child);
		}
		DefaultTreeModel model = new DefaultTreeModel(node);
		return model;
	}
	
	private DefaultTreeModel getJtreeDocumentsLabelModel(){
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("Documents");	
		Map<Integer,List<ILabelCluster>> documentIDLabels = getDocumentsLabels(report.getClustering());
		DefaultMutableTreeNode subchild;
		for(Integer doc: documentIDLabels.keySet())
		{
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(pub.get(doc));
			for(ILabelCluster cl:documentIDLabels.get(doc))
			{
				subchild = new DefaultMutableTreeNode(cl);
				child.add(subchild);
			}		
			node.add(child);
		}
		DefaultTreeModel model = new DefaultTreeModel(node);
		return model;
	}
	
	private Map<Integer, List<ILabelCluster>> getDocumentsLabels(IClustering clu) {
		Map<Integer, List<ILabelCluster>> result = new HashMap<Integer, List<ILabelCluster>>();
		for(ILabelCluster cl :clu.getClustersLabels())
		{
			for(int id:cl.getLabelDocumentsID())
			{
				if(!result.containsKey(id))
				{
					result.put(id,new ArrayList<ILabelCluster>());
				}
				result.get(id).add(cl);
			}
		}
		return result;
	}
}
