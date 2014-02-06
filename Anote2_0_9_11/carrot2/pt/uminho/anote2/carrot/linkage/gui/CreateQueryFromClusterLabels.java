package pt.uminho.anote2.carrot.linkage.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdesktop.swingx.JXTreeTable;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericView;
import pt.uminho.anote2.carrot.linkage.datastructures.IClustering;
import pt.uminho.anote2.carrot.linkage.datastructures.ILabelCluster;
import pt.uminho.anote2.carrot.linkage.gui.help.ClusteringOperationPanel;
import pt.uminho.anote2.carrot.linkage.gui.help.LabelClusterTreeTableNode;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class CreateQueryFromClusterLabels extends DialogGenericView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IClustering cluster;
	private QueryInformationRetrievalExtension query;
	private ClusteringOperationPanel jScrollPane1;
	private Map<Integer, IPublication> publications;
	
	public CreateQueryFromClusterLabels(QueryInformationRetrievalExtension query,IClustering cluster,Map<Integer, IPublication> publications)
	{
		super("Create Query From Clustering Results");
		this.cluster = cluster;
		this.query = query;
		this.publications = publications;
		try {
			initDocs();
			initGUI();
			this.setSize(GlobalOptions.superWidth, GlobalOptions.superHeight);
			Utilities.centerOnOwner(this);
			this.setModal(true);
			this.setVisible(true);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
		
	}

	private void initGUI() {	
		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.1, 0.0};
		thisLayout.rowHeights = new int[] {7, 7};
		thisLayout.columnWeights = new double[] {0.1};
		thisLayout.columnWidths = new int[] {7};
		getContentPane().setLayout(thisLayout);
		{
			jScrollPane1 = new ClusteringOperationPanel(cluster,publications);
			getContentPane().add(jScrollPane1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		{
			getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
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

	public void okButtonAction() {
		List<ILabelCluster> lables = new ArrayList<ILabelCluster>();
		JXTreeTable table = jScrollPane1.getTable();
		Object root = table.getTreeTableModel().getRoot();	
		for(int i=0;i<table.getTreeTableModel().getChildCount(root);i++)
		{
			Object sun = table.getTreeTableModel().getChild(root, i);
			LabelClusterTreeTableNode lcluster = (LabelClusterTreeTableNode) sun;
			if(lcluster.isSelected())
			{
				lables.add(lcluster.getLabel());
			}
		}
		if(lables.size()==0)
		{
			Workbench.getInstance().warn("Select at least one label for the new query");
		}
		else
		{
			ParamSpec[] paramsSpec = new ParamSpec[]{
					new ParamSpec("query", QueryInformationRetrievalExtension.class,query, null),
					new ParamSpec("clusterInfo", IClustering.class,cluster, null),
					new ParamSpec("clusters", List.class,lables, null)
			};
			
			for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
				if (def.getID().equals("operations.createquerybycluster")){			
					Workbench.getInstance().executeOperation(def, paramsSpec);
					finish();
					return;
				}
			}
		}

	}

	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Create_Query_from_Cluster_results#Configuration";
	}	

}
