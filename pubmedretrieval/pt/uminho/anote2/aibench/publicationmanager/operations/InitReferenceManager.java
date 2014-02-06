package pt.uminho.anote2.aibench.publicationmanager.operations;

import java.util.ArrayList;
import java.util.List;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.utils.conf.Configuration;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.datastructures.configuration.Proxy;
import pt.uminho.anote2.datastructures.database.MySQLDatabase;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

/**
 * Aibench Operation -- for Init Reference Manager
 * 
 * @author Hugo Costa
 *
 */
@Operation()
public class InitReferenceManager{
	
	@Port(name="Reference Manager",direction=Direction.OUTPUT,order=1)
	public PublicationManager getReferenceManager(){
		
		List<ClipboardItem> lisRm = Core.getInstance().getClipboard().getItemsByClass(PublicationManager.class);

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
		ArrayList<String> data = Configuration.getElementByXMLFile("conf/settings.conf",elements);
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
		PublicationManager pubManager = new PublicationManager(proxy,mysql);
		new ShowMessagePopup("Publication Manager Ready");
		return pubManager;	
	}
}

