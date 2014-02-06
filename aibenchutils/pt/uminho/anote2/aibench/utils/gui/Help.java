package pt.uminho.anote2.aibench.utils.gui;
import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.event.HyperlinkEvent;


public class Help{

	private static void hyperlinkUpdate(HyperlinkEvent event){
		if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
			try {
				//jEditorPaneHelp.setPage(event.getURL());
				Desktop.getDesktop().browse(event.getURL().toURI());
			} catch(URISyntaxException ioe) {
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void internetAcess(String link)
	{
		URL url=null;
		try {
			url = new URL(link);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		HyperlinkEvent hp = new HyperlinkEvent(new Object(),HyperlinkEvent.EventType.ACTIVATED,url);
		hyperlinkUpdate(hp);
	}
	
}
