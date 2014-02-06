package pt.uminho.anote2.datastructures.utils;


/**
 * Class that saves some math functions 
 *
 */
public class MathUtils {
	
	public static int getAverage(int[] values){
		int average=0, total=0;

		for(int i=0;i<values.length;i++)
		{
			if(values[i]!=0)
			{
				average+=values[i];
				total++;
			}
		}
				
		double factor = (double)total / 2;
		int mod = average % total; 
		average = average / total;
		
		if( mod >= factor)
			return average+1;

		return average;
	}

	public static Object round(Double ret, Integer doublePrecision) {
		return null;
	}

}
