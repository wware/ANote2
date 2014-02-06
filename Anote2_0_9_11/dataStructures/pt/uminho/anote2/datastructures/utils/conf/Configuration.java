package pt.uminho.anote2.datastructures.utils.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.DatabaseManager;
import pt.uminho.anote2.datastructures.settings.database.DatabaseDefaultSettings;
import pt.uminho.anote2.datastructures.utils.GenericPairC;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;


public class Configuration {
	
//	public static void saveSettings(String host,String port,String schema,String user,String pwd,boolean enableProxy,String proxyHost,String proxyPort) throws IOException{
//		File settingsfile = new File(GlobalOptions.configurationFile);
//		FileWriter fw = new FileWriter(settingsfile);
//
//		BufferedWriter bw = new BufferedWriter(fw);
//
//		bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
//		bw.write("<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">\n");
//		bw.write("<properties>\n");
//		bw.write("<comment>Settings file</comment>\n");
//
//		bw.write("<entry key=\"DB-Host\">" + host + "</entry>\n");
//		bw.write("<entry key=\"DB-Port\">" + port + "</entry>\n");
//		bw.write("<entry key=\"DB-Schema\">" + schema + "</entry>\n");
//		bw.write("<entry key=\"DB-User\">" + user + "</entry>\n");
//		bw.write("<entry key=\"DB-Pwd\">" + pwd + "</entry>\n");
//
//		bw.write("<entry key=\"HttpProxy-Enable\">" + enableProxy + "</entry>\n");
//		bw.write("<entry key=\"HttpProxy-Host\">" + proxyHost + "</entry>\n");
//		bw.write("<entry key=\"HttpProxy-Port\">" + proxyPort + "</entry>\n");
//
//		bw.write("</properties>");
//
//		bw.close();
//		fw.close();
//	}
	
	/** Method that read a Settings File the keys and return List of conf*/
	public static List<String> getElementByXMLFile(String settingsFilePath,ArrayList<String> keys){
		
		List<String> settings = new ArrayList<String>();
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
	
	public static void enablePluginManagerStartupGUI() throws IOException
	{
		InputStream fis = new FileInputStream(GlobalOptions.pluginManagerFile);
		Properties properties = new Properties();
		properties.load(fis);
		fis.close();
		properties.setProperty("plugins.start.gui", "true");
		FileOutputStream os = new FileOutputStream(GlobalOptions.pluginManagerFile);
		properties.store(os, null);
		os.flush();
		os.close();
	}
	
		/** Method that change xml properties*/
		public static boolean setXmlProperties(String settingsFilePath,List<GenericPairC<String,String>> changes)
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
	
//	public static IDatabase getDatabase() throws DatabaseLoadDriverException, SQLException
//	{
//		if(GlobalOptions.database == null)
//		{
//			ArrayList<String> elements = new ArrayList<String>();
//			elements.add("DB-Host");
//			elements.add("DB-Port");
//			elements.add("DB-Schema");
//			elements.add("DB-User");
//			elements.add("DB-Pwd");
//			ArrayList<String> data = Configuration.getElementByXMLFile(GlobalOptions.configurationFile,elements);
//			if(data!=null && data.size()==5)
//			{
//				GlobalOptions.database = new MySQLDatabase(data.get(0),data.get(1),data.get(2),data.get(3),data.get(4));
//				GlobalOptions.database.openConnection();
//			}
//		}
//		return GlobalOptions.database;
//	}
	
	public static IDatabase getDatabase()
	{
		if(GlobalOptions.database == null)
		{
			GlobalOptions.database = ((DatabaseManager) PropertiesManager.getPManager().getProperty(DatabaseDefaultSettings.DATABASE)).getDatabase();
		}
		return GlobalOptions.database;
	}
}
