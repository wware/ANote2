package pt.uminho.anote2.relation.datastructures;

import java.util.HashSet;
import java.util.Set;

import pt.uminho.anote2.process.IE.re.IPolarity;
import pt.uminho.anote2.process.IE.re.IVerbInfo;

public class Polarity implements IPolarity{
	
	/**
	 * List of verb who have negativety
	 */
	private Set<String> negVerbs;
	
	/**
	 * List of Condicional Verbs
	 */
	private Set<String> coditionalVerbs;
	
	public Polarity()
	{
		this.negVerbs=getNegativilyVerbs();
		this.coditionalVerbs=getConditionalVerbs();
	}
	
	
	public int getPolarity(IVerbInfo verbInfo)
	{
		for(String codVerbs:coditionalVerbs)
		{
			if(verbInfo.getLemma().contains(codVerbs))
				return 0;
		}
		if(this.negVerbs.contains(verbInfo.getVerb()))
		{
			return -1;
		}
		else
		{
			return verbInfo.getPolarity();
		}
	}
	
	public Set<String> getNegativilyVerbs()
	{
		Set<String> negVerbs = new HashSet<String>();
		negVerbs.add("lack");
		negVerbs.add("fail");
		negVerbs.add("inactivates");
		negVerbs.add("inactivates");
		negVerbs.add("inactivated");
		negVerbs.add("inactivation");
		negVerbs.add("inhibits");
		negVerbs.add("inhibition");
		return negVerbs;	
	}
	
	public void setNegativilyVerbs(Set<String> negVerbs)
	{
		this.negVerbs=negVerbs;
	}
	
	public Set<String> getConditionalVerbs()
	{
		Set<String> codVerbs = new HashSet<String>();
		codVerbs.add("can");
		codVerbs.add("may");
		codVerbs.add("should");
		codVerbs.add("could");		
		return codVerbs;	
	}

	public void setConditionalVerbsSet(Set<String> coditionalVerbs) {
		this.coditionalVerbs=coditionalVerbs;
		
	}
	

}
