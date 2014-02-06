package pt.uminho.anote2.aibench.ner.configuration;



public enum NERLexicalResourcesPreProssecingEnum {
	No,
	StopWords,
	POSTagging,
	Hybrid;
	
	
	public static NERLexicalResourcesPreProssecingEnum convertStringToNERLexicalResourcesPreProssecingEnum(String toconvert)
	{
		if(toconvert.equals("No"))
		{
			return NERLexicalResourcesPreProssecingEnum.No;
		}
		else if(toconvert.equals("StopWords"))
		{
			return NERLexicalResourcesPreProssecingEnum.StopWords;
		}
		else if(toconvert.equals("POSTagging"))
		{
			return NERLexicalResourcesPreProssecingEnum.POSTagging;
		}
		else if(toconvert.equals("Hybrid"))
		{
			return NERLexicalResourcesPreProssecingEnum.Hybrid;
		}
		return null;
	}
	
}
