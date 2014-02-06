package pt.uminho.anote2.aibench.utils.session;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;


public class SessionSettings {
	
	
	static SessionSettings sessionSettings = null;
	private static Properties properties;
	
	synchronized public static SessionSettings getSessionSettings(){
		if(sessionSettings == null)
			try {
				sessionSettings = new SessionSettings();
			} catch (IOException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			}
		return sessionSettings;
	}
	
	private SessionSettings() throws IOException{
		File sessionFile = new File(GlobalOptions.sessionFile);
		if(!sessionFile.exists())
			sessionFile.createNewFile();
		properties = readPropertyFile();
	}
	
	private void savePropertyFile(){
						
		try {
			properties.store(new FileOutputStream(GlobalOptions.sessionFile),null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Loads the properties from a file.
	 * @param file
	 * @return properties from file
//	 */
	private Properties readPropertyFile(){
		
		Properties prop = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream(GlobalOptions.sessionFile);
			prop.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
	
	public String getSessionProperty(String sessionPropertyKey)
	{
		if(sessionPropertyKey.equals(SessionPropertykeys.DefaultSelectDirectory))
		{
			return getSearchDirectory();
		}
		return properties.getProperty(sessionPropertyKey);
	}
	
	public void setSessionProperty(String sessionPropertyKey,String sessionPropertyValue)
	{
		properties.setProperty(sessionPropertyKey, sessionPropertyValue);
		savePropertyFile();
	}
		
	public String getSearchDirectory()
	{
		if(properties.getProperty(SessionPropertykeys.DefaultSelectDirectory) == null)
			return null;
		File file = new File(properties.getProperty(SessionPropertykeys.DefaultSelectDirectory));
		if(file.isDirectory())
			return properties.getProperty(SessionPropertykeys.DefaultSelectDirectory);
		else
			return file.getParent();
	}
	
	public void setSearchDirectory(String newDirectory)
	{
		setSessionProperty(SessionPropertykeys.DefaultSelectDirectory,newDirectory);
	}

	
}
