package pt.uminho.anote2.aibench.curator.text;

public class XmlConversions {

	public static String replaceXmlInit(String text){
		String res = text;
		
		/*
		<?xml version="1 . 0" encoding="ISO - 8859 - 1"?>
		<?xml - stylesheet type="text/css" href=" . . \ . . \ default . css"?>
		*/
		res = res.replaceAll("<\\?xml version.+?\\?>", "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		res = res.replaceAll("<\\?xml - stylesheet.+?\\?>", "<?xml-stylesheet type=\"text/css\" href=\"..\\\\..\\\\default.css\"?>");
		
		return res;
	}
}
