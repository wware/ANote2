package pt.uminho.anote2.carrot.linkage.views;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.TextAreaEditor;
import pt.uminho.anote2.aibench.utils.gui.TextAreaRenderer;
import pt.uminho.anote2.aibench.utils.gui.options.PreferencesSizeComponents;
import pt.uminho.anote2.carrot.linkage.datastructures.CarrotDatabaseLinkage;
import pt.uminho.anote2.carrot.linkage.datastructures.IClustering;
import pt.uminho.anote2.carrot.linkage.datastructures.ILabelCluster;
import pt.uminho.anote2.carrot.linkage.gui.CreateQueryFromClusterLabels;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.generic.genericpanel.buttons.ButtonTableCellEditor;
import pt.uminho.generic.genericpanel.buttons.ButtonTableRenderer;
import pt.uminho.generic.genericpanel.tablesearcher.TableSearchPanel;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;



public class ClustersQueryView extends JPanel implements Observer{

	private static final long serialVersionUID = 1L;
	private JSplitPane jSplitPaneClusters;
	private JPanel jPanelClustersResult;
	private JTree jTreeDocsLabels;
	private JTree jTreeLabelsDocs;
	private JScrollPane jScrollPaneClusters;
	private JScrollPane jScrollPaneDocsLabels;
	private JScrollPane jScrollPaneLabelsDocs;
	private TableSearchPanel jTableClusters;
	private JButton jButtonCreateClusters;
	private JPanel jPanelOperations;
	private JPanel jPanelDocsClusters;
	private JPanel jPanelClustersDocs;
	private JSplitPane jSplitPaneClustersInfo;
	private JPanel jPanelClusters;
	private List<IClustering> clusters;
	private Map<Integer,IPublication> docs;
	private QueryInformationRetrievalExtension query;

	public ClustersQueryView(QueryInformationRetrievalExtension query )
	{
		super();
		this.query=query;
		this.query.addObserver(this);
		try {
			this.docs = initDocs();
			initGUI();	
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}

	private Map<Integer, IPublication> initDocs() throws SQLException, DatabaseLoadDriverException {
		Map<Integer, IPublication> docs = new HashMap<Integer, IPublication>();
		List<IPublication> pubs = query.getPublications();
		for(IPublication pub:pubs)
		{
			docs.put(pub.getID(), pub);
		}
		return docs;
	}

	private void initGUI() throws SQLException, DatabaseLoadDriverException {
		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.1, 0.0};
		thisLayout.rowHeights = new int[] {7, 7};
		thisLayout.columnWeights = new double[] {0.1};
		thisLayout.columnWidths = new int[] {7};
		this.setLayout(thisLayout);
		this.setSize(1000, 800);
		{
			jSplitPaneClusters = new JSplitPane();
			this.add(jSplitPaneClusters, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			{
				jPanelClusters = new JPanel();
				GridLayout jPanelClustersLayout = new GridLayout(1, 1);
				jPanelClustersLayout.setColumns(1);
				jPanelClustersLayout.setHgap(5);
				jPanelClustersLayout.setVgap(5);
				jPanelClusters.setLayout(jPanelClustersLayout);
				jSplitPaneClusters.add(jPanelClusters, JSplitPane.LEFT);
				jPanelClusters.setBorder(BorderFactory.createTitledBorder("Clusters"));
				{
					jScrollPaneClusters = new JScrollPane();
					jPanelClusters.add(jScrollPaneClusters);
					{

						jScrollPaneClusters.setViewportView(getJTableClusters());
					}
				}
				jSplitPaneClusters.setResizeWeight(0.5);

			}
			{
				jPanelClustersResult = new JPanel();
				GridLayout jPanelClustersResultLayout = new GridLayout(1, 1);
				jPanelClustersResultLayout.setColumns(1);
				jPanelClustersResultLayout.setHgap(5);
				jPanelClustersResultLayout.setVgap(5);
				jPanelClustersResult.setLayout(jPanelClustersResultLayout);
				jSplitPaneClusters.add(jPanelClustersResult, JSplitPane.RIGHT);
				jPanelClustersResult.setEnabled(false);
				jPanelClustersResult.setBorder(BorderFactory.createTitledBorder(null, "Cluster Details", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
				{
					jSplitPaneClustersInfo = new JSplitPane();
					jSplitPaneClustersInfo.setResizeWeight(0.5);

					jPanelClustersResult.add(jSplitPaneClustersInfo);
					jSplitPaneClustersInfo.setOrientation(JSplitPane.VERTICAL_SPLIT);
					{
						jPanelClustersDocs = new JPanel();
						GridLayout jPanelClustersDocsLayout = new GridLayout(1, 1);
						jPanelClustersDocsLayout.setColumns(1);
						jPanelClustersDocsLayout.setHgap(5);
						jPanelClustersDocsLayout.setVgap(5);
						jPanelClustersDocs.setLayout(jPanelClustersDocsLayout);
						jSplitPaneClustersInfo.add(jPanelClustersDocs, JSplitPane.RIGHT);
						jPanelClustersDocs.setBorder(BorderFactory.createTitledBorder("Documents (Labels)"));
						{
							jScrollPaneDocsLabels = new JScrollPane();
							jPanelClustersDocs.add(jScrollPaneDocsLabels);
							{
								jTreeDocsLabels = new JTree();
								jTreeDocsLabels.setModel(new DefaultTreeModel(null));
								jScrollPaneDocsLabels.setViewportView(jTreeDocsLabels);
							}
						}
					}
					{
						jPanelDocsClusters = new JPanel();
						GridLayout jPanelDocsClustersLayout = new GridLayout(1, 1);
						jPanelDocsClustersLayout.setColumns(1);
						jPanelDocsClustersLayout.setHgap(5);
						jPanelDocsClustersLayout.setVgap(5);
						jPanelDocsClusters.setLayout(jPanelDocsClustersLayout);
						jSplitPaneClustersInfo.add(jPanelDocsClusters, JSplitPane.LEFT);
						jPanelDocsClusters.setBorder(BorderFactory.createTitledBorder("Labels (Documents)"));
						{
							jScrollPaneLabelsDocs = new JScrollPane();
							jPanelDocsClusters.add(jScrollPaneLabelsDocs);
							{
								jTreeLabelsDocs = new JTree();
								jTreeLabelsDocs.setModel(new DefaultTreeModel(null));
								jScrollPaneLabelsDocs.setViewportView(jTreeLabelsDocs);
							}
						}
					}
				}
			}
		}
		{
			jPanelOperations = new JPanel();
			GridBagLayout jPanelOperationsLayout = new GridBagLayout();
			this.add(jPanelOperations, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelOperationsLayout.rowWeights = new double[] {0.1};
			jPanelOperationsLayout.rowHeights = new int[] {7};
			jPanelOperationsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelOperationsLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelOperations.setLayout(jPanelOperationsLayout);
			{
				jButtonCreateClusters = new JButton();
				jPanelOperations.add(jButtonCreateClusters, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jButtonCreateClusters.setText("Create Cluster");
				jButtonCreateClusters.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Clustering_icon.png")));
				jButtonCreateClusters.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent arg0) {
						for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
							if (def.getID().equals("operations.querycluster")){			
								Workbench.getInstance().executeOperation(def);
								return;
							}
						}	
					}
				});
			}
		}
	}

	public void update(Observable arg0, Object arg1) {
		try {
			jTableClusters.setModel(getQueryClusters());
			completeTable(jTableClusters.getMainTable());
			jTreeDocsLabels.setModel(new DefaultTreeModel(null));
			jTreeLabelsDocs.setModel(new DefaultTreeModel(null));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DatabaseLoadDriverException e) {
			e.printStackTrace();
		}

	}
	
	public TableSearchPanel getJTableClusters() throws SQLException, DatabaseLoadDriverException {
		if(jTableClusters==null)
		{
			jTableClusters = new TableSearchPanel();
			jTableClusters.setModel(getQueryClusters());
			jTableClusters.addMouseListener(new MouseListener() {			
				public void mouseReleased(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {updateTrees();}
				public void mouseExited(MouseEvent e) {}
				public void mouseEntered(MouseEvent e) {}
				public void mouseClicked(MouseEvent e) {}
			});
			jTableClusters.addKeyListener(new KeyListener() {		
				public void keyTyped(KeyEvent arg0) {}
				public void keyReleased(KeyEvent arg0) {}
				public void keyPressed(KeyEvent arg0) {updateTrees();}
			});
			completeTable(jTableClusters.getMainTable());
		}
		return jTableClusters;
	}

	protected void updateTrees() {
		if(jTableClusters.getMainTable().getSelectedRows()[0]>-1)
		{	
			jTreeLabelsDocs.setModel(getJtreeLabelsModel());
			jTreeDocsLabels.setModel(getJtreeDocumentsLabelModel());
		}
	}

	private void completeTable(JTable jtable) {
		jtable.setRowHeight(PreferencesSizeComponents.generalRowHeight);
		jtable.getColumnModel().getColumn(0).setMaxWidth(PreferencesSizeComponents.idfieldmaxWith);
		jtable.getColumnModel().getColumn(0).setMinWidth(PreferencesSizeComponents.idfieldminWidth);
		jtable.getColumnModel().getColumn(0).setPreferredWidth(PreferencesSizeComponents.idfieldpreferredWidth);
		jtable.getColumnModel().getColumn(1).setMinWidth(100);
		jtable.getColumnModel().getColumn(2).setMinWidth(100);
		jtable.getColumnModel().getColumn(3).setMaxWidth(PreferencesSizeComponents.removefieldmaxWith);
		jtable.getColumnModel().getColumn(3).setMinWidth(PreferencesSizeComponents.removefieldminWith);
		jtable.getColumnModel().getColumn(3).setPreferredWidth(PreferencesSizeComponents.removefieldpreferredWith);
		jtable.getColumnModel().getColumn(4).setMaxWidth(75);
		jtable.getColumnModel().getColumn(4).setMinWidth(60);
		jtable.getColumnModel().getColumn(4).setPreferredWidth(60);
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/Remove_from_database.png"));
		jtable.getColumn("DB").setCellRenderer(new ButtonTableDBRenderer(icon));
		jtable.getColumn("DB").setCellEditor(new ButtonTableDBCellEditor());
		ImageIcon icon2 = new ImageIcon(getClass().getClassLoader().getResource("icons/query.png"));
		jtable.getColumn("Create").setCellRenderer(new ButtonLoadClusterToQueryRenderer(icon2));
		jtable.getColumn("Create").setCellEditor(new ButtonLoadClusterToQueryCellEditor());
		TextAreaRenderer renderer = new TextAreaRenderer();
		TextAreaEditor editor = new TextAreaEditor();	
		for (int j = 0; j < 3; j++) {
			jtable.getColumnModel().getColumn(j).setCellRenderer(renderer);
			jtable.getColumnModel().getColumn(j).setCellEditor(editor);
		}		
	}

	private TableModel getQueryClusters() throws SQLException, DatabaseLoadDriverException {
		clusters = databaseFindQueryClusters();
		String[] columns = new String[] {"ID","Descripion", "Properties", "DB","Create"};
		DefaultTableModel tableModel = new DefaultTableModel();		
		tableModel.setColumnIdentifiers(columns);	
		Object[][] data;
		int i = 0;	
		data = new Object[clusters.size()][5];	
		for(IClustering cluster: clusters)
		{
			data[i][0] = cluster.getID();
			data[i][1] = cluster.getName();
			String prop = new String();
			for(Object props:cluster.getPropeties().keySet())
			{
				prop = prop + ""+props+" : "+cluster.getPropeties().get(props).toString() + "\n";
			}	
			data[i][2] = prop;
			data[i][3] = "";
			data[i][4] = "";
			tableModel.addRow(data[i]);			
			i++;
		}
		return tableModel;
	}

	private List<IClustering> databaseFindQueryClusters() throws SQLException, DatabaseLoadDriverException {
		List<IClustering> clustersProcesses = CarrotDatabaseLinkage.getClustersForQuery(this.query);
		return clustersProcesses;
	}
	
	class ButtonTableDBRenderer extends ButtonTableRenderer{

		private static final long serialVersionUID = 6770443805813243771L;

		public ButtonTableDBRenderer(ImageIcon icon) {
			super(icon);
		}

		@Override
		public void whenClick() {
			try {
				removeFromDB();
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			}
			
		}

	}
	
	class ButtonTableDBCellEditor extends ButtonTableCellEditor
	{
		private static final long serialVersionUID = 1L;

		public void whenClick() {
			try {
				removeFromDB();
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);		
			}
		}
		
	}
	
	class ButtonLoadClusterToQueryRenderer extends ButtonTableRenderer {

		private static final long serialVersionUID = 1L;

		public ButtonLoadClusterToQueryRenderer(ImageIcon icon)
		{
			super(icon);
		}
		
		public void whenClick() {
			loadToQuery();
		}

	}

	class ButtonLoadClusterToQueryCellEditor extends ButtonTableCellEditor {

		private static final long serialVersionUID = 1L;

		public void whenClick() {
			loadToQuery();
		}

	}

	public void removeFromDB() throws SQLException, DatabaseLoadDriverException {
		if(jTableClusters.getMainTable().getSelectedColumns()[0]>-1)
		{
			int select = jTableClusters.getMainTable().getSelectedRows()[0];
			IClustering cluster = clusters.get(select);
			if(removeCluter(cluster))
			{
				CarrotDatabaseLinkage.removeClusterProcess(cluster);
				clusters.remove(select);
				jTableClusters.setModel(getQueryClusters());
				completeTable(jTableClusters.getMainTable());
				jTreeDocsLabels.setModel(new DefaultTreeModel(null));
				jTreeLabelsDocs.setModel(new DefaultTreeModel(null));
			}
		}
	}
	
	private boolean removeCluter(IClustering cluster){
		Object[] options = new String[]{"Yes", "No"};
		int opt = showOptionPane("Cluster Remove", "Remove Cluster Process "+cluster.getName() +" ( "+cluster.getID()+") whit "+cluster.getClustersLabels().size()+ " clusters ", options);
		switch (opt) {
		case 0:
			return true;
		default:
			return false;
		}
	}
	
	/** Presents a option pane with the given title, question and options */
	public static int showOptionPane(String title, String question, Object[] options){
		JOptionPane option_pane = new JOptionPane(question);
		//option_pane.setIcon(new ImageIcon(getClass().getClassLoader().getResource("messagebox_question.png")));
		option_pane.setOptions(options);
		
		JDialog dialog = option_pane.createDialog(Workbench.getInstance().getMainFrame(), title);
		dialog.setVisible(true);
		
		Object choice = option_pane.getValue();
					
		for(int i=0; i<options.length; i++)
			if(options[i].equals(choice))
				return i;
		
		return -1;		
	}

	public void loadToQuery() {
		IClustering clus = clusters.get(jTableClusters.getMainTable().getSelectedRows()[0]);
		new CreateQueryFromClusterLabels(query, clus,docs);
	}
	
	private DefaultTreeModel getJtreeLabelsModel(){
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("Labels");	
		IClustering clu = clusters.get(jTableClusters.getMainTable().getSelectedRows()[0]);
		DefaultMutableTreeNode subchild;
		for(ILabelCluster label: clu.getClustersLabels())
		{
			
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(label);
			for(Integer docID:label.getLabelDocumentsID())
			{
				IPublication doc = docs.get(docID);
				String title = doc.getTitle();
				String abstracttext = doc.getAbstractSection();
				subchild = new DefaultMutableTreeNode("TITLE: "+title+"\n"+"ABSTRACT: "+abstracttext + " ID: "+docID);
				child.add(subchild);
			}		
			node.add(child);
		}
		DefaultTreeModel model = new DefaultTreeModel(node);
		return model;
	}
	
	private DefaultTreeModel getJtreeDocumentsLabelModel(){
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("Documents");	
		IClustering clu = clusters.get(jTableClusters.getMainTable().getSelectedRows()[0]);
		Map<Integer,List<ILabelCluster>> documentIDLabels = getDocumentsLabels(clu);
		
		DefaultMutableTreeNode subchild;
		for(Integer docID: documentIDLabels.keySet())
		{
			IPublication doc = docs.get(docID);
			String title = doc.getTitle();
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(" ID: "+docID+" TITLE: "+title);
			for(ILabelCluster cl:documentIDLabels.get(docID))
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
			for(Integer id:cl.getLabelDocumentsID())
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
