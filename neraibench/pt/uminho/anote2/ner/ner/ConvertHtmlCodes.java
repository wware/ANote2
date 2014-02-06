package pt.uminho.anote2.ner.ner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;


public class ConvertHtmlCodes {

	private Map<String, String> htmlCodes;
	
	public ConvertHtmlCodes(File codesFile) throws Exception{
		FileReader fr = new FileReader(codesFile);
		BufferedReader br = new BufferedReader(fr);
		String line;
		htmlCodes = new HashMap<String, String>();
		while((line=br.readLine())!=null)
		{
			/**
			 * estranho
			 */
			String t = line.replace(";", " ;");
			htmlCodes.put(t,line);
		}
		br.close();
		fr.close();
	}
	
	public String convertToHtmlCode(String text){
		for(String code : htmlCodes.keySet())
			text = text.replace(code, htmlCodes.get(code));
		return text;
	}
	
}
