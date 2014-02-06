package pt.uminho.anote2.process.IE.re;

import java.util.Set;


public interface IPolarity {
	
	
	/**
	 * For IVerbInfo determinate polarity
	 * 
	 * -1 Negative
	 * 0 Conditional
	 * 1 Positive
	 * 
	 */
	public int getPolarity(IVerbInfo verb);
	
	/**
	 * For add a negatively Verbs 
	 * 
	 * Ex. lack fail
	 */
	public Set<String> getNegativilyVerbs();
	public void setNegativilyVerbs(Set<String> negVerbs);
	
	/**
	 * For Condicional Verbs 
	 * 
	 * Ex. May,Can,Should
	 * 
	 * @return
	 */
	public Set<String> getConditionalVerbs();
	public void setConditionalVerbsSet(Set<String> coditionalVerbs);

}
