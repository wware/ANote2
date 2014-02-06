package pt.uminho.anote2.carrot.linkage.datastructures;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import org.carrot2.core.Cluster;
import org.carrot2.core.Document;

import pt.uminho.anote2.carrot.linkage.report.ClusteringReport;
import pt.uminho.anote2.carrot.linkage.report.ICLusteringReport;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.documents.query.Query;

public class CarrotRunClusterAlgorithms {

	public static ICLusteringReport execute(Query query,CarrotClusterAlgorithmsEnum algorithm, Properties properties) throws SQLException, DatabaseLoadDriverException {
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		List<Document> documents = new ArrayList<Document>();
		List<IPublication> pubs = null;
		pubs = query.getPublications();
		IClustering clustering;
		for (IPublication pub : pubs)
		{
			Document doc = new Document(pub.getTitle(),pub.getAbstractSection(),String.valueOf(pub.getID()));
			documents.add(doc);
		}
		String[] quer = new String[2];
		quer[0] = query.getKeyWords();
		quer[1] = query.getOrganism();
		List<Cluster> clustersByTopic = algorithm.getClusters(documents, properties, quer);
		int clusterID;
		clusterID = CarrotDatabaseLinkage.clusterProcessRegistry("Cluster "+algorithm.toString()+" Algorithm");
		CarrotDatabaseLinkage.clusterProcessPropertiesRegistry(clusterID,properties);
		CarrotDatabaseLinkage.clusterProcessLabelsRegistry(query.getID(),clusterID,clustersByTopic);
		CarrotDatabaseLinkage.clusterProcessQueryRegistry(query.getID(),clusterID);
		clustering = CarrotDatabaseLinkage.getClusteringByID(clusterID);
		ICLusteringReport report = new ClusteringReport(clustering,algorithm,query);
		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		report.setTime(endTime-startTime);
		return report;
	}

	
	
}
