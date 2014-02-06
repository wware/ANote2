package pt.uminho.anote2.carrot.linkage.datastructures;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.carrot2.core.Cluster;
import org.carrot2.core.Document;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.clusters.QueriesClusters;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;
import pt.uminho.anote2.datastructures.documents.query.Query;
import pt.uminho.anote2.datastructures.documents.query.QueryManager;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;

public class CarrotDatabaseLinkage {
	
	

	public static int clusterProcessRegistry(String string) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement insertCluster = Configuration.getDatabase().getConnection().prepareStatement(QueriesClusters.insertCluster);
		insertCluster.setString(1, string);
		insertCluster.execute();
		insertCluster.close();
		return HelpDatabase.getNextInsertTableID(GlobalTablesName.cluster)-1;
	}

	public static void clusterProcessPropertiesRegistry(int clusterID,Properties properties) throws SQLException, DatabaseLoadDriverException {
		if(properties!=null)
		{
			PreparedStatement insertClusterProperties = Configuration.getDatabase().getConnection().prepareStatement(QueriesClusters.insertClusterProperties);
			insertClusterProperties.setInt(1, clusterID);
			for(Object prop:properties.keySet())
			{
				insertClusterProperties.setString(2, prop.toString());
				insertClusterProperties.setString(3, properties.get(prop).toString());
				insertClusterProperties.execute();
			}
			insertClusterProperties.close();
		}
	}

	public static void clusterProcessLabelsRegistry(int queryID, int clusterID,List<Cluster> clusters) throws SQLException, DatabaseLoadDriverException {
		int labelID = HelpDatabase.getNextInsertTableID( GlobalTablesName.clusterLabels);
		PreparedStatement insertLabel = Configuration.getDatabase().getConnection().prepareStatement(QueriesClusters.insertClusterLabel);
		PreparedStatement insertLabelDocument = Configuration.getDatabase().getConnection().prepareStatement(QueriesClusters.insertClusterLabelDocument);
		PreparedStatement insertLabelProcess = Configuration.getDatabase().getConnection().prepareStatement(QueriesClusters.insertClusterLabelProcess);

		insertLabelDocument.setInt(3, queryID);
		for(Cluster cl:clusters)
		{
			Double score = cl.getScore();
			String label = cl.getLabel();
			clusterLabelRegistry(insertLabel,label,score);
			clusterLabelProcessRegistry(insertLabelProcess,labelID,clusterID);
			clusterLabelDocumentsRegistry(insertLabelDocument,labelID,cl);
			labelID++;
		}
		insertLabel.close();
		insertLabelDocument.close();
		insertLabelProcess.close();
	}

	private static void clusterLabelProcessRegistry(PreparedStatement insertLabelProcess, int labelID, int clusterID) throws SQLException {
		insertLabelProcess.setInt(1, labelID);
		insertLabelProcess.setInt(2, clusterID);
		insertLabelProcess.execute();
	}

	private static void clusterLabelDocumentsRegistry(PreparedStatement insertLabelDocument, int labelID, Cluster cl) throws SQLException {
		for(Document doc : cl.getDocuments())
		{
			int docID = Integer.parseInt(doc.getContentUrl());
			insertLabelDocument.setInt(1, labelID);
			insertLabelDocument.setInt(2, docID);
			insertLabelDocument.execute();
		}
		
	}

	private static void clusterLabelRegistry(PreparedStatement insertLabel,String label, Double score) throws SQLException {
		insertLabel.setString(1,label);
		if(score==null)
			insertLabel.setDouble(2,0);
		else
			insertLabel.setDouble(2,score);
		insertLabel.execute();
		
	}

	public static void clusterProcessQueryRegistry(int queryID, int clusterID) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement insertClusterRegistryInQuery = Configuration.getDatabase().getConnection().prepareStatement(QueriesClusters.insertQueryCluster);
		insertClusterRegistryInQuery.setInt(1, clusterID);
		insertClusterRegistryInQuery.setInt(2, queryID);
		insertClusterRegistryInQuery.execute();
		insertClusterRegistryInQuery.close();
	}
	
	public static List<IClustering> getClustersForQuery(Query query) throws DatabaseLoadDriverException, SQLException
	{
		List<IClustering> listClusters = new ArrayList<IClustering>();
		PreparedStatement getQueryClusters;
		getQueryClusters = Configuration.getDatabase().getConnection().prepareStatement(QueriesClusters.selectQueryClusters);
		PreparedStatement getClusterProperties =  Configuration.getDatabase().getConnection().prepareStatement(QueriesClusters.selectClusterProperties);
		PreparedStatement getClusterLabels =  Configuration.getDatabase().getConnection().prepareStatement(QueriesClusters.selectClusterLabels);
		PreparedStatement getClusterLabelsDocuments =  Configuration.getDatabase().getConnection().prepareStatement(QueriesClusters.selectClusterLabelsDocuments);
		getQueryClusters.setInt(1, query.getID());
		ResultSet rs = getQueryClusters.executeQuery();
		while(rs.next())
		{
			int clusterProcessID = rs.getInt(1);
			String description = rs.getString(2);
			Properties prop = getClusterproperties(getClusterProperties,clusterProcessID);
			List<ILabelCluster> clusterLabels = getClusterLabels(clusterProcessID,getClusterLabels,getClusterLabelsDocuments);
			IClustering cluster = new ClusterProcess(clusterProcessID, prop, description, clusterLabels);
			listClusters.add(cluster);
		}
		rs.close();
		getQueryClusters.close();
		getClusterLabels.close();
		getClusterLabelsDocuments.close();
		return listClusters;
	}
	
	public static IClustering getClusteringByID(int clusteringID) throws SQLException, DatabaseLoadDriverException
	{
		PreparedStatement getQueryClusterID =  Configuration.getDatabase().getConnection().prepareStatement(QueriesClusters.selectQueryClustersID);
		PreparedStatement getClusterProperties =  Configuration.getDatabase().getConnection().prepareStatement(QueriesClusters.selectClusterProperties);
		PreparedStatement getClusterLabels =  Configuration.getDatabase().getConnection().prepareStatement(QueriesClusters.selectClusterLabels);
		PreparedStatement getClusterLabelsDocuments =  Configuration.getDatabase().getConnection().prepareStatement(QueriesClusters.selectClusterLabelsDocuments);
		getQueryClusterID.setInt(1, clusteringID);
		IClustering cluster = null;
		ResultSet rs = getQueryClusterID.executeQuery();
		if(rs.next())
		{
			String description = rs.getString(2);
			Properties prop = getClusterproperties(getClusterProperties,clusteringID);
			List<ILabelCluster> clusterLabels = getClusterLabels(clusteringID,getClusterLabels,getClusterLabelsDocuments);
			cluster = new ClusterProcess(clusteringID, prop, description, clusterLabels);
		}
		rs.close();
		getQueryClusterID.close();
		getClusterProperties.close();
		getClusterLabels.close();
		getClusterLabelsDocuments.close();
		return cluster;
	}

	private static List<ILabelCluster> getClusterLabels(int clusterID,PreparedStatement getClusterLabels,PreparedStatement getClusterLabelsDocuments) throws SQLException {
		List<ILabelCluster> clusterLabels = new ArrayList<ILabelCluster>();
		getClusterLabels.setInt(1, clusterID);
		ResultSet rs = getClusterLabels.executeQuery();
		while(rs.next())
		{
			int labelID = rs.getInt(1);
			String labelName = rs.getString(2);
			Double score = rs.getDouble(3);
			List<Integer> documentsIDs = getLabelDocuments(getClusterLabelsDocuments,labelID);
			ILabelCluster label = new LabelCluster(labelID, labelName, score, documentsIDs);
			clusterLabels.add(label);
		}
		rs.close();
		return clusterLabels;
	}

	private static List<Integer> getLabelDocuments(PreparedStatement getClusterLabelsDocuments, int labelID) throws SQLException {
		List<Integer> documentsIDs = new ArrayList<Integer>();
		getClusterLabelsDocuments.setInt(1, labelID);
		ResultSet rs = getClusterLabelsDocuments.executeQuery();
		while(rs.next())
		{
			int docID = rs.getInt(1);
			documentsIDs.add(docID);
		}
		rs.close();
		return documentsIDs;
	}

	private static Properties getClusterproperties(PreparedStatement getClusterProperties, int clusterID) throws SQLException {
		Properties properties = new Properties();
		getClusterProperties.setInt(1, clusterID);
		ResultSet rs = getClusterProperties.executeQuery();
		while(rs.next())
		{
			String key = rs.getString(1);
			String value = rs.getString(2);
			properties.put(key, value);
		}
		rs.close();
		return properties;
	}

	public static void removeClusterProcess(IClustering cluster) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement insertCluster = Configuration.getDatabase().getConnection().prepareStatement(QueriesClusters.inativeClusterProcess);
		insertCluster.setInt(1, cluster.getID());
		insertCluster.execute();
		insertCluster.close();
	}

	public static void createQuery(Query query,IClustering clusters, List<ILabelCluster> labels) throws SQLException, DatabaseLoadDriverException {
		Set<IPublication> pubs = new HashSet<IPublication>();
		Map<Integer,IPublication> pubsMap = new HashMap<Integer, IPublication>();
		for(IPublication pub :query.getPublications())
		{
			pubsMap.put(pub.getID(), pub);
		}
		for(ILabelCluster label:labels)
		{
			for(int docID : label.getLabelDocumentsID())
			{
				pubs.add(pubsMap.get(docID));
			}
		}
		int queryID = QueryManager.insertQuery("Query Generates by Clustering results in Query "+query.getID()+" and Clustering "+clusters.getID()
				, "", pubs.size(), pubs.size(), new Properties(), false);
		insertQueryPublications(queryID,pubs);
	}

	private static void insertQueryPublications(int queryID, Set<IPublication> pubs) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement insertDocs = Configuration.getDatabase().getConnection().prepareStatement(QueriesPublication.insertPublicationOnQuery);
		insertDocs.setInt(1, queryID);
		for(IPublication pubID:pubs)
		{
			insertDocs.setInt(2, pubID.getID());
			insertDocs.execute();
		}
		insertDocs.close();
	}
	
	

}
