package pt.uminho.anote2.carrot.linkage.datastructures;

import java.util.List;

import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;

public class LabelCluster implements ILabelCluster{

	private int id;
	private String lableName;
	private Double score;
	private List<Integer> documentsIDs;

	public LabelCluster(int id, String lableName, Double score,List<Integer> documentsIDs) {
		this.id = id;
		this.lableName = lableName;
		this.score = score;
		this.documentsIDs = documentsIDs;
	}

	public int getID() {
		return id;
	}

	public String getLabelName() {
		return lableName;
	}

	public Double getScore() {
		return score;
	}

	public List<Integer> getLabelDocumentsID() {
		return documentsIDs;
	}
	
	public String toString()
	{
		String result = getLabelName() + " ("+documentsIDs.size()+")";
		if(score!=null || score==0)
			result = result + " Score: "+GlobalOptions.decimalformat.format(getScore());
		return result;
	}

}
