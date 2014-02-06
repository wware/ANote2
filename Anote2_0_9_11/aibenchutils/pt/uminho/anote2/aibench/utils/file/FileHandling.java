package pt.uminho.anote2.aibench.utils.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;

public class FileHandling {
	
	public static boolean createDirectory(String dir){
		
		return (new File(dir)).mkdir();
	
	}
	
	public static boolean existDirectory(String dir){
	
		return (new File(dir)).exists();
		
	}
	
	public static boolean removeDirectory(File dir){
		//If the directory is not empty, it is necessary to first recursively delete all files and subdirectories in the directory. Here is a method that will delete a non-empty directory.
		
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for(String child: children)
			{
				boolean success = removeDirectory(new File(dir, child));
				if (!success) {
					// Deletion failed
					System.exit(0);
					return false;					
				}
			}
		}			

		return removeEmptyDirectory(dir);
	
	}
	
	public static boolean removeEmptyDirectory(File dir){		
		return dir.delete();
	}
	
	public static boolean copyFile(File in, File out){
		try {
			FileReader fr = new FileReader(in);
			FileWriter fw = new FileWriter(out);
			BufferedReader br = new BufferedReader(fr);
			BufferedWriter bw = new BufferedWriter(fw);
			
			String line = br.readLine();
			while(line != null)
			{
				bw.write(line + "\n");
				line = br.readLine();
			}
			
			br.close();
			bw.close();
			fr.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/** Binary */
	public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
    
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
	
	public static boolean appendToFile(String text, File in, File out){
		try {
			FileReader fr = new FileReader(in);
			FileWriter fw = new FileWriter(out);
			BufferedReader br = new BufferedReader(fr);
			BufferedWriter bw = new BufferedWriter(fw);
			
			String line = text;
			while(line != null)
			{
				bw.write(line + "\n");
				line = br.readLine();
			}
			
			br.close();
			bw.close();
			fr.close();
			fw.close();
 		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// Return a list with names of the files is a given directory
	public static ArrayList<String> listFileNames(File directory){
		ArrayList<String> fileList = new ArrayList<String>();
		
		if(!directory.isDirectory())
			return fileList;
		
		for(File file: directory.listFiles())
			fileList.add(file.getName());
		
		return fileList;
	}
	
	// Test if the given file contains a given String
	public static boolean fileContains(File file, String token) throws IOException{
		
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		
		String line = br.readLine();
		while(line!=null)
		{
			if(line.compareTo(token)==0)
				return true;
			line = br.readLine();
		}
		br.close();
		fr.close();
		return false;
	}
	
	//Remove a given String from a given file
	public static void removeFromFile(File file, String token) throws IOException{
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		
		ArrayList<String> lines = new ArrayList<String>();
		
		String line = br.readLine();
				
		while(line!=null)
		{
			if(line.compareTo(token)!=0)
				lines.add(line);
			line = br.readLine();
		}
		br.close();
		fr.close();
		
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		
		for(String str:lines)
			bw.write(str+"\n");
		
		bw.close();
		fw.close();
	}
	
	//	Remove a given String from a given file
	public static void appendToFile(File file, String token) throws IOException{
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		
		ArrayList<String> lines = new ArrayList<String>();
		
		String line = token;
				
		while(line!=null)
		{
			lines.add(line);
			line = br.readLine();
		}
		br.close();
		fr.close();
		
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		
		for(String str:lines)
			bw.write(str+"\n");
		
		bw.close();
		fw.close();
	}
	
	/**
	 * SAve and Load Files Section
	 */
	
	public static boolean testSaveFile(File file)
	{
		SAXBuilder builder = new SAXBuilder();
		try {
			Document doc = (Document) builder.build(file);
			Element rootNode = doc.getRootElement();
			if(rootNode.getValue().equals(GlobalOptions.rootName))
			{
				return true;
			}
			else
			{
				return false;
			}
		} catch (JDOMException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}
	
	public static boolean putSaveFileTags(File file)
	{
		String content = "<?xml version=\"1.0\"?>" +
						 "<"+GlobalOptions.rootName+" version=\""+GlobalOptions.anote2Version+"\">\n" +
						 "</"+GlobalOptions.rootName+">";
		try {
			appendToFile(file,content);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean testOlderInformation(File file, String string) {
		SAXBuilder builder = new SAXBuilder();
		Document doc;
		try {
			doc = (Document) builder.build(file);
		} catch (JDOMException e) {
			e.printStackTrace();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return true;
		}
		Element rootNode = doc.getRootElement();
		Element pm =  rootNode.getChild(string);
		if(pm!=null)
			return true;
		return false;
	}

	
}
