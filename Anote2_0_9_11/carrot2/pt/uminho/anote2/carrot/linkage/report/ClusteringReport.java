package pt.uminho.anote2.carrot.linkage.report;

import pt.uminho.anote2.carrot.linkage.datastructures.CarrotClusterAlgorithmsEnum;
import pt.uminho.anote2.carrot.linkage.datastructures.IClustering;
import pt.uminho.anote2.datastructures.documents.query.Query;
import pt.uminho.anote2.datastructures.report.Report;

public class ClusteringReport extends Report implements ICLusteringReport{

	
	private IClustering clustering;
	private CarrotClusterAlgorithmsEnum algorithm;
	private Query query;
	
	public ClusteringReport(IClustering clustering, CarrotClusterAlgorithmsEnum algorithm, Query query2) {
		super("Clustering Report");
		this.clustering = clustering;
		this.algorithm = algorithm;
		this.query = query2;
	}

	@Override
	public IClustering getClustering() {
		return clustering;
	}

	@Override
	public CarrotClusterAlgorithmsEnum getAlgorithm() {
		return algorithm;
	}


	@Override
	public Query getQuery() {
		return query;
	}

}
