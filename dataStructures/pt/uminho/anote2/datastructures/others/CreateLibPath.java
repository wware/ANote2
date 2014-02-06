package pt.uminho.anote2.datastructures.others;

import java.io.File;

public class CreateLibPath {
	
		   
	    public static String getPath(String path)
	    {
	        String result = "SET CLASSPATH=\"";
	        File file = new File(path);
	        if(file.isDirectory())
	        {
	            for(String fil:file.list())
	            {
	                result = result + "./lib/"+fil+";";
	            }
	        }
	        result = result + "\"";
	        return result;
	    }
	   
	    public static void main(String[] args) {
	        String path = "C:/releases/ANote2_1_0_0_win/lib";
	        String res = CreateLibPath.getPath(path);
	    }

}
