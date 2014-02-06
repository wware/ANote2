package pt.uminho.anote2.aibench.utils.settings.proxy;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

public class IPv4InputVerifier extends InputVerifier{

	@Override
	public boolean verify(JComponent input) {
		if(input instanceof JTextComponent){			
			String text = ((JTextComponent)input).getText();
			boolean valid = validateIPAddress(text);
			if(!valid)
				((JTextComponent)input).setText("");
			return valid;
		}else{
			try {
				throw new Exception("IPv4Verifier can only be applied to JTextComponents.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public final static boolean validateIPAddress(String  ipAddress){

		String[] parts = ipAddress.split("\\.");

		if (parts.length != 4) return false;

		for (String s : parts){
			int i = Integer.parseInt(s);

			if ((i < 0) || (i > 255)) return false;

		}
		return true;
	}

}
