package pt.uminho.anote2.carrot.linkage.datastructures;

import java.text.DecimalFormat;
import java.util.Hashtable;

import javax.swing.JLabel;

public class ClusterAlgorithmsFields {

	public final static int kmeansClusterCountMin = 2;
	public final static int kmeansClusterCountMax = 10000;
	public final static int kmeansClusterCountStep = 1;
	public final static int kmeansClusterCountDefaultValue = 25;
	
	public final static int kmeansClusterMaxIterationsMin= 1;
	public final static int kmeansClusterMaxIterationsMax= 10000;
	public final static int kmeansClusterMaxIterationsStep= 1;
	public final static int kmeansClusterMaxIterationsDefaultValue= 15;

	public final static boolean kmeansClusterMaxIterationsDimensionalityReduction = true;
	
	public final static int kmeansClusterPartitionCountMin = 2;
	public final static int kmeansClusterPartitionCountMax = 10;
	public final static int kmeansClusterPartitionCountStep = 1;
	public final static int kmeansClusterPartitionCountDefaultValue = 2;
	
	public final static int kmeansClusterLabelCountMin = 1;
	public final static int kmeansClusterLabelCountMax = 10;
	public final static int kmeansClusterLabelCountStep = 1;
	public final static int kmeansClusterLabelCountDefaultValue = 3;
	
	/**
	 * LINGO
	 * 
	 * The real values are divided by 10
	 */
	public final static int LingoScoreWeightMin = 0;
	public final static int LingoScoreWeightMax = 10;
	public final static int LingoScoreWeightStep = 1;
	public final static int LingoScoreWeightDefaultValue = 0;
	public final static int LingoScoreWeightDivisionNumber = 10;
	public static Hashtable<Integer,JLabel> LingoScoreWeightLabels = getLingoScoreHashTable();
	
	private static Hashtable<Integer, JLabel> getLingoScoreHashTable() {
		 Hashtable<Integer, JLabel> result = new Hashtable<Integer, JLabel>();
		 DecimalFormat decimalformat = new DecimalFormat("0.0");
		 for(int i=LingoScoreWeightMin;i<=LingoScoreWeightMax;i=i+LingoScoreWeightStep)
		 {
			 result.put(i, new JLabel(decimalformat.format((double) i / (double) LingoScoreWeightDivisionNumber)));
		 }
		return result;
	}
	
	public final static int LingoDesiredClusterCountBaseMin = 2;
	public final static int LingoDesiredClusterCountBaseMax = 100;
	public final static int LingoDesiredClusterCountBaseStep = 1;
	public final static int LingoDesiredClusterCountBaseDefaultValue = 30;
	
	public final static boolean LingoUSeQueryInformation = true;
	
	/**
	 * STC
	 */	
	
	public final static int STCScoreWeightMin = 0;
	public final static int STCScoreWeightMax = 10;
	public final static int STCScoreWeightStep = 1;
	public final static int STCScoreWeightDefaultValue = 0;
	public final static int STCScoreWeightDivisionNumber = 10;
	public static Hashtable<Integer,JLabel> STCScoreWeightLabels = getLingoScoreHashTable();
	
	public final static int STCMinBaseClusterScoreMin = 0;
	public final static int STCMinBaseClusterScoreMax = 10;
	public final static int STCMinBaseClusterScoreStep = 2;
	public final static int STCMinBaseClusterScoreDefaultValue = 2;

	public final static int STCMinBaseClusterSizeMin = 2;
	public final static int STCMinBaseClusterSizeMax = 20;
	public final static int STCMinBaseClusterSizeStep = 1;
	public final static int STCMinBaseClusterSizeDefaultValue = 2;
	
	public final static boolean STCUseQueryInformation = true;

	public final static int STCMaxBaseClustersMin = 2;
	public final static int STCMaxBaseClustersMax = 10000;
	public final static int STCMaxBaseClustersStep = 1;
	public final static int STCMaxBaseClustersDefaultValue = 300;
	
	public final static double STCSingleTermBoostMin = 0.0;
	public final static double STCSingleTermBoostMax = 10000.0;
	public final static double STCSingleTermBoostStep = 0.5;
	public final static double STCSingleTermBoostDefaultValue = 0.5;
	
	public final static int STCOptimalPhraseLengthMin = 1;
	public final static int STCOptimalPhraseLengthMax = 10000;
	public final static int STCOptimalPhraseLengthStep = 1;
	public final static int STCOptimalPhraseLengthDefaultValue = 3;
	
	public final static int STCDocumentCountBoostMin = 0;
	public final static int STCDocumentCountBoostMax = 10000;
	public final static int STCDocumentCountBoostStep = 1;
	public final static int STCDocumentCountBoostDefaultValue = 1;
}
