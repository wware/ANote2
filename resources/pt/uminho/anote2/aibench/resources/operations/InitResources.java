package pt.uminho.anote2.aibench.resources.operations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import pt.uminho.anote2.aibench.resources.datatypes.Resources;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.datastructures.configuration.Proxy;
import pt.uminho.anote2.datastructures.database.MySQLDatabase;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation(enabled=true)
public class InitResources{
	
	@Port(name="Resources",direction=Direction.OUTPUT,order=1)
	public Resources getReferenceManager()
	{
		List<ClipboardItem> lisRm = Core.getInstance().getClipboard().getItemsByClass(Resources.class);
		if(lisRm.size()!=0)
		{
			return null;
		}
		ArrayList<String> elements = new ArrayList<String>();
		elements.add("DB-Host");
		elements.add("DB-Port");
		elements.add("DB-Schema");
		elements.add("DB-User");
		elements.add("DB-Pwd");
		elements.add("HttpProxy-Enable");
		elements.add("HttpProxy-Host");
		elements.add("HttpProxy-Port");
		ArrayList<String> data = getElementByXMLFile("conf/settings.conf", elements);
		
		MySQLDatabase mysql = new MySQLDatabase(data.get(0),data.get(1),data.get(2),data.get(3),data.get(4));
		Proxy proxy = null;
		if(data.get(5).equals("true"))
		{
			proxy = new Proxy(data.get(6),data.get(7));
		}
		else
		{
			proxy = new Proxy();
		}
		Resources resources = new Resources(mysql,proxy);
		new ShowMessagePopup("Resources Ready!!!");
		return resources;

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
	
	

}
