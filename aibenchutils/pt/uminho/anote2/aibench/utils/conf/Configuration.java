package pt.uminho.anote2.aibench.utils.conf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class Configuration {
	
	public static void saveSettings(String host,String port,String schema,String user,String pwd,boolean enableProxy,String proxyHost,String proxyPort){
		File settingsfile = new File("conf/settings.conf");
		try {
			FileWriter fw = new FileWriter(settingsfile);
			
			BufferedWriter bw = new BufferedWriter(fw);
				
			bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			bw.write("<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">\n");
			bw.write("<properties>\n");
			bw.write("<comment>Settings file</comment>\n");
			
			bw.write("<entry key=\"DB-Host\">" + host + "</entry>\n");
			bw.write("<entry key=\"DB-Port\">" + port + "</entry>\n");
			bw.write("<entry key=\"DB-Schema\">" + schema + "</entry>\n");
			bw.write("<entry key=\"DB-User\">" + user + "</entry>\n");
			bw.write("<entry key=\"DB-Pwd\">" + pwd + "</entry>\n");
			
			bw.write("<entry key=\"HttpProxy-Enable\">" + enableProxy + "</entry>\n");
			bw.write("<entry key=\"HttpProxy-Host\">" + proxyHost + "</entry>\n");
			bw.write("<entry key=\"HttpProxy-Port\">" + proxyPort + "</entry>\n");
			
			bw.write("</properties>");
			
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Method that read a Settings File the keys and return List of conf*/
	public static ArrayList<String> getElementByXMLFile(String settingsFilePath,ArrayList<String> keys){
		
		ArrayList<String> settings = new ArrayList<String>();
		File file = new File(settingsFilePath);
		if(file.length()<=0)
			return null;
		
		Properties p = new Properties();
		try {
			p.loadFromXML(new FileInputStream(file));
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(String key:keys)
		{
			settings.add(p.getProperty(key));
		}
		return settings;
	}
	
	/** Method that change xml properties*/
	public static boolean setXmlProperties(String settingsFilePath,ArrayList<GenericPairC<String,String>> changes)
	{
		File file = new File(settingsFilePath);
		if(file.length()<=0)
			return false;
		
		Properties p = new Properties();
		try {
			p.loadFromXML(new FileInputStream(file));
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(GenericPairC<String,String> change: changes)
		{
			FileOutputStream out;
			try {
				out = new FileOutputStream(settingsFilePath);

				p.setProperty(change.getX(),change.getY());
				p.storeToXML(out,"");

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		return true;
	}
	
	public static void main(String[] args) throws IOException{
		ArrayList<GenericPairC<String,String>> changes = new ArrayList<GenericPairC<String,String>>();
		changes.add(new GenericPairC<String, String>("DB-Host","Hugo"));
		Configuration.setXmlProperties("settings.conf",changes);
	
	}
	
	

}
