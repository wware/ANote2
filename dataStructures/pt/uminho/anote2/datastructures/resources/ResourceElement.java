package pt.uminho.anote2.datastructures.resources;



import pt.uminho.anote2.resource.IResourceElement;

public class ResourceElement implements IResourceElement, Comparable<IResourceElement>{

	private int id;
	private String term;
	private int termClassID;
	private String termClass;
	private String externalID;
	private int sourceID;
	private int priorety;
	
	public ResourceElement(String term)
	{
		this.id=-1;
		this.term=term;
		this.termClassID=-1;
	}
	
	public ResourceElement(int id,String term)
	{
		this.id=id;
		this.term=term;
		this.termClassID=-1;
	}
	
	public ResourceElement(int id,String term,int termClassID,String termClass)
	{
		this.id=id;
		this.term=term;
		this.termClass=termClass;
		this.termClassID=termClassID;
		this.externalID=null;
		this.sourceID=-1;
		this.priorety=-1;
	}
	
	public ResourceElement(int id,String term,int termClassID,String termClass,String externalID,int sourceExternalID)
	{
		this.id=id;
		this.term=term;
		this.termClass=termClass;
		this.termClassID=termClassID;
		this.externalID=externalID;
		this.sourceID=sourceExternalID;
		this.priorety=-1;
	}
	
	public ResourceElement(int id,String term,int termClassID,String termClass,int priorety)
	{
		this.id=id;
		this.term=term;
		this.termClass=termClass;
		this.termClassID=termClassID;
		this.priorety=priorety;
		this.externalID=null;
		this.sourceID=-1;
	}
	
	public ResourceElement(int id,String term,int termClassID,String termClass,String externalID,int sourceExternalID,int priorety)
	{
		this.id=id;
		this.term=term;
		this.termClass=termClass;
		this.termClassID=termClassID;
		this.externalID=externalID;
		this.sourceID=sourceExternalID;
		this.priorety=priorety;
	}
	
	public int getID() {
		return this.id;
	}


	public String getTerm() {
		return this.term;
	}


	public String getTermClass() {
		return this.termClass;
	}


	public String getElementType() {
		return "";
	}


	public int compareTo(IResourceElement elem) {
		if(this.id==elem.getID())
		{
			int term = this.term.compareTo(elem.getTerm());
			if(term==0)
			{
				if(elem.getTermClass()==null)
				{
					return -2;
				}
				return this.termClass.compareTo(elem.getTermClass());
			}
			else
				return term;
		}
		else if(id<elem.getID())
		{
			return -1;
		}
		else
		{
			return 1;
		}
	}

	public int getTermClassID() {
		return this.termClassID;
	}

	@Override
	public String getExtenalID() {
		return externalID;
	}

	@Override
	public int getExternalIDSourceID() {
		return sourceID;
	}

	@Override
	public int getPriority() {
		return priorety;
	}
	
	public ResourceElement clone()
	{
		return new ResourceElement(this.id,this.term,this.termClassID,this.termClass,this.externalID,this.sourceID,this.priorety);
	}
}
