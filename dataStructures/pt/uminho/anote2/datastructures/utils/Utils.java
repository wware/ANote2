package pt.uminho.anote2.datastructures.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that contains some util methods 
 * 
 *
 */
public class Utils {
	
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	

	
	/**Returns the current time of the system */
	public static String currentTime() {
	     
	  	Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
	    return sdf.format(cal.getTime());

	}
	
	public static String formatDate(String date){

		int i = date.indexOf(";");
		
		String[] tokens = date.substring(0,i).split("[ -/]");
		
		if(tokens.length==1)
			return tokens[0];
		
		if(tokens.length==2)
			return tokens[0] + "-" + tokens[1];
		
		return tokens[0] + "-" + tokens[1] + "-" + tokens[2];

	}
	
	/** Returns the current time of the system */
	public static int currentYear() {
	     
	    Calendar cal = Calendar.getInstance();
	    int year = cal.get(Calendar.YEAR);
	    return year;

	}
	
	/** Swap element of a Hash*/
	public static Map<? extends Object, ? extends Object> swapHashElements(Map<? extends Object,? extends Object> hash)
	{
		Map<Object,Object> result = new HashMap<Object,Object>();
		for(Object key:hash.keySet())
		{
			result.put(hash.get(key),key);
		}
		return result;
	}
	
	public static String treatSentenceForXMLProblems(String sentence)
	{
		sentence=sentence.replaceAll("&","&amp;");
		sentence=sentence.replaceAll("\"","&quot;");
		sentence=sentence.replaceAll("<","&lt;");
		sentence=sentence.replaceAll(">","&gt;");
		return sentence;
	}

}
