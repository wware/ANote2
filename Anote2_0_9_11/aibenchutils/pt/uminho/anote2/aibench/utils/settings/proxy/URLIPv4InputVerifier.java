package pt.uminho.anote2.aibench.utils.settings.proxy;



import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

import es.uvigo.ei.aibench.workbench.Workbench;


public class URLIPv4InputVerifier extends InputVerifier{

	@Override
	public boolean verify(JComponent input) {
		if(input instanceof JTextComponent){
			String text = ((JTextComponent)input).getText();
			boolean validURL = URLInputVerifier.verifyURL(text,input);
			if(!validURL){
				boolean validIP = IPv4InputVerifier.validateIPAddress(text);
				if(!validIP){
					((JTextComponent)input).setText("");
					return false;
				} else return true;
			}
			return true;					
		}
		Workbench.getInstance().warn("Host not valid");
		return false;
	
	}

}
