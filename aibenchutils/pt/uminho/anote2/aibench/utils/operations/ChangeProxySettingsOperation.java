package pt.uminho.anote2.aibench.utils.operations;

import java.util.ArrayList;

import pt.uminho.anote2.aibench.utils.conf.Configuration;
import pt.uminho.anote2.aibench.utils.conf.GenericPairC;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.configuration.IProxy;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

/**
 * ƒ necess‡rio rever esta situa?‹o
 * Tem de existir um tipo de dados oculto que guarde dados gerais sobre o projecto
 * nomeadamente a liga?‹o de proxy e a conex‹o a base de dados.
 * 
 * n‹o faz sentido o Utils necessitar de depedendencias de projectos ( exepto core e dataStructures)
 * 
 * Nota 28 Setembro de 2011
 * 
 * 
 * @author Hugo Costa
 *
 */
@Operation(name="Change Proxy Settings", description="")
public class ChangeProxySettingsOperation {
	
	
	@Port(direction=Direction.INPUT, name="proxy", order=1)
	public void setProxy(IProxy proxy) {
		ArrayList<GenericPairC<String,String>> changes = new ArrayList<GenericPairC<String,String>>();
		if(proxy.isEnable())
		{
			changes.add(new GenericPairC<String, String>("HttpProxy-Enable",String.valueOf(proxy.isEnable())));
			changes.add(new GenericPairC<String, String>("HttpProxy-Host",proxy.getProxyHost()));
			changes.add(new GenericPairC<String, String>("HttpProxy-Port",proxy.getProxyPort()));
		}
		else
		{
			changes.add(new GenericPairC<String, String>("HttpProxy-Enable","false"));
		}
		Configuration.setXmlProperties("conf/settings.conf", changes);
		
//		List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(PublicationManager.class);
//		if(cl.size()>0)
//		{
//			PublicationManager pubManager = (PublicationManager) cl.get(0).getUserData();
//			pubManager.setProxy(proxy);
//		}
		new ShowMessagePopup("Proxy Change");
	}

}
