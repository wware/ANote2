package pt.uminho.anote2.aibench.utils.gui;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.event.HyperlinkEvent;


public class Help{

	private static void hyperlinkUpdate(HyperlinkEvent event){
		if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
			try {
				Desktop.getDesktop().browse(event.getURL().toURI());
			} catch(URISyntaxException ioe) {
				ioe.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void internetAccess(String link) throws IOException
	{
		openBrowserLink(link);
	}
	
	private static void openBrowserLink(String link) throws IOException
	{
		URL url=null;
		url = new URL(link);

		String os = System.getProperty("os.name").toLowerCase();
		Runtime rt = Runtime.getRuntime();


		if (os.indexOf( "win" ) >= 0) {

			// this doesn't support showing urls in the form of "page.html#nameLink" 
			HyperlinkEvent hp = new HyperlinkEvent(new Object(),HyperlinkEvent.EventType.ACTIVATED,url);
			hyperlinkUpdate(hp);

		} else if (os.indexOf( "mac" ) >= 0) {

			rt.exec( "open " + url);

		} else if (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0) {

			// Do a best guess on unix until we get a platform independent way
			// Build a list of browsers to try, in this order.
			String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
					"netscape","opera","links","lynx"};

			// Build a command string which looks like "browser1 "url" || browser2 "url" ||..."
			StringBuffer cmd = new StringBuffer();
			for (int i=0; i<browsers.length; i++)
				cmd.append( (i==0  ? "" : " || " ) + browsers[i] +" \"" + url + "\" ");

			rt.exec(new String[] { "sh", "-c", cmd.toString() });

		} else {
			return;
		}
		return;		
	}

}
