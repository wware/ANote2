package pt.uminho.anote2.datastructures.annotation;

import pt.uminho.anote2.core.annotation.IExternalID;

public class ExternalID implements IExternalID{

	private String externalID;
	private String source;
	private int sourceID;
	
	public ExternalID(String externalID,String source,int sourceID)
	{
		this.externalID=externalID;
		this.source=source;
		this.sourceID=sourceID;
	}
	
	public String getExternalID() {
		return externalID;
	}

	public int getSourceID() {
		return sourceID;
	}

	public String getSource() {
		return source;
	}
	
	public String toString()
	{
		String result = getExternalID();
		if(getSource() != null)
			result = result + " ( "+getSource() + " )";
		return result;
	}


	public boolean equals(Object arg0) {
		if(arg0 instanceof ExternalID)
			return getExternalID().equals(((ExternalID) arg0).getExternalID()) && getSourceID() == ((ExternalID) arg0).getSourceID();
		else
			return false;
		
	}
	
	

	
}
