package pt.uminho.anote2.carrot.linkage.datastructures;

import java.util.List;
import java.util.Properties;

import org.carrot2.clustering.kmeans.BisectingKMeansClusteringAlgorithm;
import org.carrot2.clustering.lingo.LingoClusteringAlgorithm;
import org.carrot2.clustering.stc.STCClusteringAlgorithm;
import org.carrot2.core.Cluster;
import org.carrot2.core.Document;


public enum CarrotClusterAlgorithmsEnum {
	STC{
		
		public List<Cluster> getClusters(List<Document> documents,Properties properties,String[] keywords)
		{
			STCClusteringAlgorithm stc = new STCClusteringAlgorithm();
			stc.documents = documents;
			if(properties.containsKey(ClusterAlgorithmsPropertiesNames.queryInformation) && ((Boolean)properties.get(ClusterAlgorithmsPropertiesNames.queryInformation)))
			{
				if(keywords!=null)
				{
					String query = new String();
					if(keywords[0]!=null)
					{
						query = keywords[0];
						if(keywords[1]!=null)
						{
							query = query + " " + keywords[1];
						}
						stc.query = query;
					}
				}
			}
			if(properties.containsKey(ClusterAlgorithmsPropertiesNames.documentCountBoost))
			{
				stc.documentCountBoost = (Double) properties.get(ClusterAlgorithmsPropertiesNames.documentCountBoost);
			}
			if(properties.containsKey(ClusterAlgorithmsPropertiesNames.maxBaseClusters))
			{
				stc.maxBaseClusters = (Integer) properties.get(ClusterAlgorithmsPropertiesNames.maxBaseClusters);
			}
			if(properties.containsKey(ClusterAlgorithmsPropertiesNames.minBaseClusterScore))
			{
				stc.minBaseClusterScore = (Double) properties.get(ClusterAlgorithmsPropertiesNames.minBaseClusterScore);
			}
			if(properties.containsKey(ClusterAlgorithmsPropertiesNames.minBaseClusterSize))
			{
				stc.minBaseClusterSize = (Integer) properties.get(ClusterAlgorithmsPropertiesNames.minBaseClusterSize);
			}
			if(properties.containsKey(ClusterAlgorithmsPropertiesNames.optimalPhraseLength))
			{
				stc.optimalPhraseLength = (Integer) properties.get(ClusterAlgorithmsPropertiesNames.optimalPhraseLength);
			}
			if(properties.containsKey(ClusterAlgorithmsPropertiesNames.singleTermBoost))
			{
				stc.singleTermBoost = (Double) properties.get(ClusterAlgorithmsPropertiesNames.singleTermBoost);
			}
			stc.process();
			return stc.clusters;
		}
		
	},
	Lingo
	{
		
		public List<Cluster> getClusters(List<Document> documents,Properties properties,String[] keywords)
		{
			LingoClusteringAlgorithm lingo = new LingoClusteringAlgorithm();
			lingo.documents = documents;
			if(properties.containsKey(ClusterAlgorithmsPropertiesNames.desiredClusterCountBase))
			{
				lingo.desiredClusterCountBase = (Integer) properties.get(ClusterAlgorithmsPropertiesNames.desiredClusterCountBase);
			}
			if(properties.containsKey(ClusterAlgorithmsPropertiesNames.queryInformation) && ((Boolean)properties.get(ClusterAlgorithmsPropertiesNames.queryInformation)))
			{
				if(keywords!=null)
				{
					String query = new String();
					if(keywords[0]!=null)
					{
						query = keywords[0];
						if(keywords[1]!=null)
						{
							query = query + " " + keywords[1];
						}
						lingo.query = query;
					}
				}
			}
			if(properties.containsKey(ClusterAlgorithmsPropertiesNames.scoreWeight))
			{
				lingo.scoreWeight = (Double) properties.get(ClusterAlgorithmsPropertiesNames.scoreWeight);
			}
			lingo.process();
			return lingo.clusters;
		}
	},
	Kmeans
	{
		
		public List<Cluster> getClusters(List<Document> documents,Properties properties,String[] keywords)
		{
			BisectingKMeansClusteringAlgorithm kmeans = new BisectingKMeansClusteringAlgorithm();
			kmeans.documents = documents;
			if(properties.containsKey(ClusterAlgorithmsPropertiesNames.clusterCount))
			{
				kmeans.clusterCount = (Integer) properties.get(ClusterAlgorithmsPropertiesNames.clusterCount);
			}
			if(properties.containsKey(ClusterAlgorithmsPropertiesNames.labelCount))
			{
				kmeans.labelCount = (Integer) properties.get(ClusterAlgorithmsPropertiesNames.labelCount);
			}
			if(properties.containsKey(ClusterAlgorithmsPropertiesNames.maxIterations))
			{
				kmeans.maxIterations = (Integer) properties.get(ClusterAlgorithmsPropertiesNames.maxIterations);
			}
			if(properties.containsKey(ClusterAlgorithmsPropertiesNames.partitionCount))
			{
				kmeans.partitionCount = (Integer) properties.get(ClusterAlgorithmsPropertiesNames.partitionCount);
			}
			if(properties.containsKey(ClusterAlgorithmsPropertiesNames.useDimensionalityReduction))
			{
				kmeans.useDimensionalityReduction = (Boolean) properties.get(ClusterAlgorithmsPropertiesNames.useDimensionalityReduction);
			}
			kmeans.process();
			return kmeans.clusters;
		}
	};
	
	
	public List<Cluster> getClusters(List<Document> documents,Properties properties,String[] keywords)
	{
		return this.getClusters(documents,properties,keywords);
	}
}

