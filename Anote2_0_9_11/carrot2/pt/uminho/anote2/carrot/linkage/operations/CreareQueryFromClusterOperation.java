package pt.uminho.anote2.carrot.linkage.operations;

import java.sql.SQLException;
import java.util.List;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.carrot.linkage.datastructures.CarrotDatabaseLinkage;
import pt.uminho.anote2.carrot.linkage.datastructures.IClustering;
import pt.uminho.anote2.carrot.linkage.datastructures.ILabelCluster;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation
public class CreareQueryFromClusterOperation {
	
	private QueryInformationRetrievalExtension query;
	private IClustering clusters;

	@Port(name="query",direction=Direction.INPUT,order=1)
	public void setQuery(QueryInformationRetrievalExtension query){
		this.query=query;	
	}
	
	@Port(name="clusterInfo",direction=Direction.INPUT,order=2)
	public void setQuery(IClustering cluster){
		this.clusters=cluster;	
	}
	
	@Port(name="clusters",direction=Direction.INPUT,order=3)
	public void setClusters(List<ILabelCluster> labels){
		System.gc();
		if(labels.size()>0)
		{
			try {
				CarrotDatabaseLinkage.createQuery(query,clusters,labels);
				List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(PublicationManager.class);
				PublicationManager pm = (PublicationManager) cl.get(0).getUserData();
				pm.addQueryInformationRetrievalExtension(PublicationManager.getLastQuery(pm));
			} catch (SQLException e) {
				new ShowMessagePopup("Query Creation Fail.");
				TreatExceptionForAIbench.treatExcepion(e);
				return;
			} catch (DatabaseLoadDriverException e) {
				new ShowMessagePopup("Query Creation Fail.");
				TreatExceptionForAIbench.treatExcepion(e);
				return;
			}
			query.getPubManager().notifyViewObserver();
			new ShowMessagePopup("Query Created.");
		}
		else
		{
			new ShowMessagePopup("Query Creation Fail.");
		}
	}

}
