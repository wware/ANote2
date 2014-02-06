package pt.uminho.anote2.corpusloaders.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class FileHandeling {
	
	public static Set<String> getFileLines(File filePath) throws FileNotFoundException, IOException {
		Set<String> files = new HashSet<String>();
		FileInputStream fstream = new FileInputStream(filePath);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		while ((strLine = br.readLine()) != null)   {
			
			files.add(strLine);
		}
		return files;
	}
	
	public static String getFileContent(File filePath) throws FileNotFoundException, IOException
	{
		Set<String> lines = getFileLines(filePath);
		String content = new String();
		for(String line:lines)
		{
			content = content + line;
		}
		return content;
	}
	


}
