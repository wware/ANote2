package pt.uminho.anote2.aibench.utils.settings.proxy;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

import es.uvigo.ei.aibench.workbench.Workbench;

public class PortInputVerifier extends InputVerifier{

	@Override
	public boolean verify(JComponent input) {
		if(input instanceof JTextComponent){
			String text = ((JTextComponent)input).getText();
			boolean valid = validatePort(text);
			if(!valid){
				((JTextComponent)input).setText("");
			}
			return valid; 
		}
		else{
			try {
				throw new Exception("PortVerifier can only be applied to JTextComponents");
			} catch (Exception e) {				
				e.printStackTrace();
			}
		}
		Workbench.getInstance().warn("Port not valid");
		return false;
	}
	
	public static final boolean validatePort(String portString){
		int port = -1;
		try{
			port = Integer.parseInt(portString);
		} catch(NumberFormatException e){
			return false;
		}
		
		return validatePortRange(port);
		
	}
	
	public static final boolean validatePortRange(int port){		
		return ((port >=0) && (port <= 65536)) ? true : false;
	}

}
