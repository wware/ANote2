package pt.uminho.anote2.core.document;

public enum RelevanceType {
	none,
	irrelevant,
	related,
	relevant;

	public static RelevanceType convertString(String string) {
		if(string== null)
		{
			return RelevanceType.none;
		}
		if(string.equals("irrelevant"))
		{
			return RelevanceType.irrelevant;
		}
		else if(string.equals("related"))
		{
			return RelevanceType.related;
		}
		else if(string.equals("relevant"))
		{
			return RelevanceType.relevant;
		}
		return RelevanceType.none;
	}
}
