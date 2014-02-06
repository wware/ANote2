package pt.uminho.anote2.carrot.linkage.datastructures;

import java.util.List;
import java.util.Properties;

public class ClusterProcess implements IClustering{

	private int id;
	private Properties properties;
	private String name;
	private List<ILabelCluster> clusterLabels;
	
	public ClusterProcess(int id, Properties properties, String name,List<ILabelCluster> clusterLabels) {
		super();
		this.id = id;
		this.properties = properties;
		this.name = name;
		this.clusterLabels = clusterLabels;
	}

	public int getID() {
		return id;
	}

	public Properties getPropeties() {
		return properties;
	}

	public String getName() {
		return name;
	}

	public List<ILabelCluster> getClustersLabels() {
		return clusterLabels;
	}
	
	public String toString()
	{
		return "ID :"+getID()+" "+getName(); 
	}


}
