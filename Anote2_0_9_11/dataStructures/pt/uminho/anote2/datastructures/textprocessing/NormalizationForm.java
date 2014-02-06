package pt.uminho.anote2.datastructures.textprocessing;

/**
 * Class for generate the normalization form of a word
 * 
 * 
 * @author Hugo Costa
 *
 */
public class NormalizationForm {
	
	public static String getNormalizationForm(String term)
	{
		return term;
	}
	
	public static String removeOffsetProblemSituation(String text) {
		text = text.replaceAll("\\r", "").trim();
		text = text.replaceAll("\\n\\n", "\\n");
		text = text.replaceAll("\\n", ". ");
		text = text.replaceAll("\\s", " ");
		text = text.replaceAll("\\t", " ");
		text = text.replaceAll("\\f", "");			
		text = text.replaceAll("\\e", "");
		text = text.replaceAll("\\c[", "");
		text = text.replaceAll("<", "«");
		text = text.replaceAll(">", "»");
		text = text.replaceAll("\\Q", "");	
		text = text.replaceAll("\\s+", " ");
		return text;
	}

}
