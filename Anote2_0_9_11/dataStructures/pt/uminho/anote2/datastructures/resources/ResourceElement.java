package pt.uminho.anote2.datastructures.resources;



import java.util.ArrayList;
import java.util.List;

import pt.uminho.anote2.core.annotation.IExternalID;
import pt.uminho.anote2.resource.IResourceElement;

public class ResourceElement implements IResourceElement, Comparable<IResourceElement>{

	private int id;
	private String term;
	private int termClassID;
	private String termClass;
	private List<IExternalID> externalIDs;
	private List<String> synonyms;
	private int priorety;
	private boolean isActive;
	
	public ResourceElement(String term)
	{
		this.id=-1;
		this.term=term;
		this.termClassID=-1;
		this.externalIDs = null;
		this.termClass = null;
		this.priorety=-1;
		this.synonyms = new ArrayList<String>();
		this.isActive = true;
	}

	public ResourceElement(String term,int termClassID)
	{
		this.id=-1;
		this.term=term;
		this.termClassID=termClassID;
		this.externalIDs = null;
		this.termClass = null;
		this.synonyms = new ArrayList<String>();
		this.isActive = true;

	}

	public ResourceElement(int id,String term)
	{
		this.id=id;
		this.term=term;
		this.termClassID=-1;
		this.externalIDs = null;
		this.termClass = null;
		this.synonyms = new ArrayList<String>();
		this.isActive = true;

	}

	public ResourceElement(int id,String term,int termClassID,String termClass)
	{
		this.id=id;
		this.term=term;
		this.termClass=termClass;
		this.termClassID=termClassID;
		this.priorety=-1;
		this.externalIDs = null;
		this.synonyms = new ArrayList<String>();
		this.isActive = true;
	}
	
	public ResourceElement(int id, String term, int termClassID,String termClass, boolean active) {
		this.id=id;
		this.term=term;
		this.termClass=termClass;
		this.termClassID=termClassID;
		this.priorety=-1;
		this.externalIDs = null;
		this.synonyms = new ArrayList<String>();
		this.isActive = active;

	}
	
	public ResourceElement(int id,String term,int termClassID,String termClass,IExternalID externalIDs)
	{
		this.id=id;
		this.term=term;
		this.termClass=termClass;
		this.termClassID=termClassID;
		this.priorety=-1;
		this.synonyms = new ArrayList<String>();
		this.externalIDs = new ArrayList<IExternalID>();
		this.externalIDs.add(externalIDs);
		this.isActive = true;


	}
	
	public ResourceElement(int id,String term,int termClassID,String termClass,int priorety)
	{
		this.id=id;
		this.term=term;
		this.termClass=termClass;
		this.termClassID=termClassID;
		this.priorety=priorety;
		this.synonyms = new ArrayList<String>();
		this.externalIDs=null;
		this.isActive = true;

	}
	
	public ResourceElement(int id,String term,int termClassID,String termClass,List<IExternalID> externalIDs,List<String> synonyms)
	{
		this.id=id;
		this.term=term;
		this.termClass=termClass;
		this.termClassID=termClassID;
		this.externalIDs=externalIDs;
		this.priorety=-1;
		this.synonyms = synonyms;
		this.isActive = true;

		
	}
	
	public ResourceElement(int id,String term,int termClassID,String termClass,List<IExternalID> externalIDs,int priorety)
	{
		this.id=id;
		this.term=term;
		this.termClass=termClass;
		this.termClassID=termClassID;
		this.externalIDs=externalIDs;
		this.priorety=priorety;
		this.synonyms = new ArrayList<String>();
		this.isActive = true;
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

	public int getPriority() {
		return priorety;
	}
	
	public ResourceElement clone()
	{
		return new ResourceElement(this.id,this.term,this.termClassID,this.termClass,this.externalIDs,this.priorety);
	}

	public List<IExternalID> getExtenalIDs() {
		return externalIDs;
	}

	public List<String> getSynonyms() {
		return synonyms;
	}

	public boolean isActive() {
		return isActive;
	}

	@Override
	public void changeTermClassID(int newClasseID) {
		this.termClassID = newClasseID;
	}
}
