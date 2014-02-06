package pt.uminho.anote2.carrot.linkage.report;

import pt.uminho.anote2.carrot.linkage.datastructures.CarrotClusterAlgorithmsEnum;
import pt.uminho.anote2.carrot.linkage.datastructures.IClustering;
import pt.uminho.anote2.core.report.IReport;
import pt.uminho.anote2.datastructures.documents.query.Query;

public interface ICLusteringReport extends IReport{
	public IClustering getClustering();
	public CarrotClusterAlgorithmsEnum getAlgorithm();
	public Query getQuery();

}
