package pt.uminho.anote2.carrot.linkage.datastructures;

import java.util.List;
import java.util.Properties;

public interface IClustering {
	public int getID();
	public Properties getPropeties();
	public String getName();
	public List<ILabelCluster> getClustersLabels();
}
