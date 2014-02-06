package pt.uminho.anote2.process.IE.re;

import java.util.Set;

import pt.uminho.anote2.core.annotation.PolarityEnum;
import pt.uminho.anote2.process.IE.re.clue.IVerbInfo;


public interface IPolarity {
	
	
	/**
	 * For IVerbInfo determinate polarity
	 * 
	 * -1 Negative
	 * 0 Conditional
	 * 1 Positive
	 * 
	 */
	public PolarityEnum getPolarity(IVerbInfo verb);
	
	/**
	 * For add a negatively Verbs 
	 * 
	 * Ex. lack fail
	 */
	public Set<String> getNegativilyVerbs();
	public void setNegativilyVerbs(Set<String> negVerbs);
	
	/**
	 * For Conditional Verbs 
	 * 
	 * Ex. May,Can,Should
	 * 
	 * @return
	 */
	public Set<String> getConditionalVerbs();
	public void setConditionalVerbsSet(Set<String> coditionalVerbs);

}
