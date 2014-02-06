package pt.uminho.anote2.core.document;

public enum CorpusTextType {
	Abstract,
	FullText,
	Hybrid;

	public static CorpusTextType convertStringToCorpusType(String propValue) {
		if(propValue.equals(CorpusTextType.Abstract.toString()))
		{
			return CorpusTextType.Abstract;
		}
		else if(propValue.equals(CorpusTextType.FullText.toString()))
		{
			return CorpusTextType.FullText;
		}
		else if(propValue.equals(CorpusTextType.Hybrid.toString()))
		{
			return CorpusTextType.Hybrid;
		}
		return null;
	}
}
