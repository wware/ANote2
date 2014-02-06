package pt.uminho.anote2.aibench.utils.settings.proxy;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

public class URLInputVerifier extends InputVerifier{

	@Override
	public boolean verify(JComponent input) {
		if(input instanceof JTextComponent){
			String text = ((JTextComponent)input).getText();

			boolean valid = verifyURL(text,input);
			if(!valid){
				((JTextComponent)input).setText("");
				return false;
			}
			else
			{
				((JTextComponent)input).setText(text);
				return true;	
			}

					
		}else{
			try {
				throw new Exception("URLVerifier can only be applied to JTextComponents.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static final boolean verifyURL(String urlString,JComponent input){		
		
		boolean isvalid1 = true;
//		boolean isvalid2 = true;
		if(urlString.startsWith("http://"))
		{
			String newURL = urlString.replace("http://","");
			((JTextComponent)input).setText(newURL);
		}
		else if(urlString.startsWith("Http://"))
		{
			String newURL = urlString.replace("Http://","");
			((JTextComponent)input).setText(newURL);
		}
		else
		{
			urlString = "http://"+urlString;
		}
		try{
			@SuppressWarnings("unused")
			URL url = new URL(urlString);
		} catch(MalformedURLException e){
			isvalid1 = false;
		}
//		try{
//			@SuppressWarnings("unused")
//			URL url = new URL("http:\\/\\/"+urlString);
//		} catch(MalformedURLException e){
//			isvalid2 = false;
//		}	
		return (isvalid1);
	}

}
