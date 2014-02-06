package pt.uminho.anote2.carrot.linkage.operations;

import java.sql.SQLException;
import java.util.Properties;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.carrot.linkage.datastructures.CarrotClusterAlgorithmsEnum;
import pt.uminho.anote2.carrot.linkage.datastructures.CarrotRunClusterAlgorithms;
import pt.uminho.anote2.carrot.linkage.gui.ClusteringReportGUI;
import pt.uminho.anote2.carrot.linkage.report.ICLusteringReport;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation(enabled=false)
public class QueryGerateCarrotClusterOperation {
	
	private QueryInformationRetrievalExtension query;
	private CarrotClusterAlgorithmsEnum algorithm;

	@Port(name="query",direction=Direction.INPUT,order=1)
	public void setQuery(QueryInformationRetrievalExtension query){
		this.query=query;	
	}
	
	
	@Port(name="algorithm",direction=Direction.INPUT,order=2)
	public void setAlgoritm(CarrotClusterAlgorithmsEnum algorithm){
		this.algorithm = algorithm;
	}
	
	
	@Port(name="properties",direction=Direction.INPUT,order=3)
	public void setProperties(Properties properties){
		System.gc();
		ICLusteringReport report;
		try {
			report = CarrotRunClusterAlgorithms.execute(query,algorithm,properties);
		} catch (SQLException e) {
			new ShowMessagePopup("Cluster Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Cluster Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		}
		if(report.isFinishing())
		{
			this.query.notifyViewObserver();
			new ClusteringReportGUI(report);
			new ShowMessagePopup("Cluster Done.");
		}
	}
	
}
