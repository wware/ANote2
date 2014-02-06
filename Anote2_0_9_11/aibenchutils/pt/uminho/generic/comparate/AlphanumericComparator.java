package pt.uminho.generic.comparate;

import java.util.Comparator;

public class AlphanumericComparator implements Comparator<Object>{

	
	
	@Override
	public int compare(Object o1, Object o2) {
		
		if(o1 instanceof Integer && o2 instanceof Integer)
		{
			return ((Integer) o1).compareTo((Integer)o2);
		}
		else if(o1 instanceof String && o2 instanceof String)
		{
			return ((String) o1).compareTo((String)o2);
		}
		else if(o1 instanceof String && o2 instanceof Integer || o1 instanceof Integer && o2 instanceof String)
		{
			String obj1 = String.valueOf(o1);
			String obj2 = String.valueOf(o2);
			if(obj1.length()==0 && obj2.length() == 0)
			{
				return 0;
			}
			else if(obj1.length() == 0)
			{
				return 1;
			}
			else if(obj2.length() == 0)
			{
				return -1;
			}
			for(int i=0;i<obj1.length() && i< obj2.length();i++)
			{
				String tes1 = obj1.substring(i, i+1).toLowerCase();
				String tes2 = obj2.substring(i, i+1).toLowerCase();
				int compareCharacterResult = tes1.compareTo(tes2);
				
				if(compareCharacterResult!= 0)
				{
					return compareCharacterResult;
				}
			}

			return 0;
		}
		return Integer.valueOf(String.valueOf(o1.equals(o2)));
	}
	
	

}
