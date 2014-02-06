package pt.uminho.anote2.datastructures.textprocessing;

public class TextProcessor {
	
	public static int getNumberOFwords(String text)
	{
		String textToCompare = text;
		textToCompare = textToCompare.trim();
		textToCompare = textToCompare.replace("(", "");
		textToCompare = textToCompare.replace(")", "");
		textToCompare = textToCompare.replace(":", "");
		textToCompare = textToCompare.replace(".", "");
		textToCompare = textToCompare.replace(",", "");
		textToCompare = textToCompare.replace("?", "");
		textToCompare = textToCompare.replace("-", "");
		return textToCompare.split("\\s+").length + 1 ;
	}

}
