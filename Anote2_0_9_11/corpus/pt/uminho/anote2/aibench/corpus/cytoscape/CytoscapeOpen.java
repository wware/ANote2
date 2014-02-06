package pt.uminho.anote2.aibench.corpus.cytoscape;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import pt.uminho.anote2.datastructures.exceptions.InvalidDirectoryException;
import pt.uminho.anote2.datastructures.exceptions.UnknownOperationSystemException;

public class CytoscapeOpen {

	private static String cytoscapepath = "C:/Program Files/Cytoscape_v3.0.2/";
	private static String cytoscapepathLinux = "/home/hcosta/Cytoscape_v3.0.2/";
	private static String cytoscapepathMacOS = "/Applications/Cytoscape/";

	private static String networkFileTest = "C:/Users/Hugo Costa/Desktop/network_reprocess_22.xgmml";
	private static String networkFileTestLinux = "/home/hcosta/network_reprocess_22.xgmml";
	private static String networkFileTestMacOS = "/Users/hugo/Desktop/network_reprocess_22.xgmml";

	public static final String fileProperty = "networkfile";
	
	public static void launch(String cytoscapePath,Properties properties) throws UnknownOperationSystemException, InvalidDirectoryException, IOException
	{
		File cytoScapeDir = new File(cytoscapePath);
		if(cytoScapeDir.isDirectory())
		{
			String os = System.getProperty("os.name").toLowerCase();
			if (os.indexOf( "win" ) >= 0) {
				File filetoRun = new File(cytoscapePath+"/cytoscape.bat");
				if(filetoRun.exists())
				{
					Runtime rt = Runtime.getRuntime();
					String command = createCommandOnWindows(properties);
					rt.exec(command, null, cytoScapeDir);					
				}
				else
					throw new InvalidDirectoryException("Cytoscape path is not invalid");

			} else if (os.indexOf( "mac" ) >= 0) {
				File filetoRun = new File(cytoscapePath+"/cytoscape.app/");
				if(filetoRun.exists())
				{
					Runtime rt = Runtime.getRuntime();
					String command = createCommandOnMacOS(properties);
					rt.exec(command, null, cytoScapeDir);					
				}
				else
					throw new InvalidDirectoryException("Cytoscape path is not invalid");

			} else if (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0) {
				Runtime rt = Runtime.getRuntime();
				String command = createCommandOnLinux(properties);
				rt.exec(command, null, cytoScapeDir);
			} else {
				throw new UnknownOperationSystemException();
			}
		}
		else
			throw new InvalidDirectoryException("Cytoscape path can not be a file");
	}
	
	private static String createCommandOnMacOS(Properties properties) {
		String baseCommand = "open Cytoscape.app";
//		if(properties.getProperty(CytoscapeOpen.fileProperty)!=null)
//		{
//			baseCommand = baseCommand + " -N \""+properties.getProperty(CytoscapeOpen.fileProperty)+"\"";
//		}
		return baseCommand;
	}

	private static String createCommandOnLinux(Properties properties) {
		String baseCommand = "./Cytoscape";
		if(properties.getProperty(CytoscapeOpen.fileProperty)!=null)
		{
			baseCommand = baseCommand + " -N "+properties.getProperty(CytoscapeOpen.fileProperty);
		}
		return baseCommand;
	}

	private static String createCommandOnWindows(Properties properties) {
		String baseCommand = "cmd /c start Cytoscape.exe";
		if(properties.getProperty(CytoscapeOpen.fileProperty)!=null)
		{
			String builtPAth = properties.getProperty(CytoscapeOpen.fileProperty);
			builtPAth = builtPAth.replace("\\", "/");
			baseCommand = baseCommand + " -N \""+builtPAth+"\"";
		}
		return baseCommand;
	}

	public static boolean validateCytoscapeDirectory(String path) throws InvalidDirectoryException, UnknownOperationSystemException
	{
		File cytoScapeDir = new File(path);
		if(cytoScapeDir.isDirectory())
		{
			String os = System.getProperty("os.name").toLowerCase();
			if (os.indexOf( "win" ) >= 0) {
				File filetoRun = new File(path+"/cytoscape.bat");
				if(filetoRun.exists())
				{
					return true;
				}
				else
					throw new InvalidDirectoryException("Cytoscape path is not invalid");

			} else if (os.indexOf( "mac" ) >= 0) {
				File filetoRun = new File(path+"/cytoscape.app/");
				if(filetoRun.exists())
				{
					return true;
				}
				else
					throw new InvalidDirectoryException("Cytoscape path is not invalid");

			} else if (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0) {

				File filetoRun = new File(path+"/Cytoscape");
				if(filetoRun.exists())
				{
					return true;
				}
				else
					throw new InvalidDirectoryException("Cytoscape path is invalid");

			} else {
				throw new UnknownOperationSystemException();
			}
		}
		else
			throw new InvalidDirectoryException("Cytoscape path can not be a file");
	}
	
	public static void main(String[] args) throws UnknownOperationSystemException, InvalidDirectoryException, IOException {
		Properties pro = new Properties();
		pro.put("networkfile", networkFileTestMacOS);
		CytoscapeOpen.launch(cytoscapepathMacOS, pro);
	}

	//	Runtime.getRuntime().exec("cmd /c start build.bat");
}
