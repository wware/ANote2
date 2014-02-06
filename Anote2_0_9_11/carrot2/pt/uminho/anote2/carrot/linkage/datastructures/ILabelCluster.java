package pt.uminho.anote2.carrot.linkage.datastructures;

import java.util.List;

public interface ILabelCluster {
	public int getID();
	public String getLabelName();
	public Double getScore();
	public List<Integer> getLabelDocumentsID();
}
