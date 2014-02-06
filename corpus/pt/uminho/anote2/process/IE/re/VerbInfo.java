package pt.uminho.anote2.process.IE.re;



/**
 * 
 * @author Hugo Costa
 * 
 * Objectivo : Guardar informação sobre os verbos
 * 
 *
 */

public class VerbInfo implements IVerbInfo{
	
	private String verb;
	private String lemma;
	/**
	 * -1 Negativo
	 * 0 - Conditional
	 * 1 Positivo
	 */
	private Integer polarity;
	/**
	 *  Verb Voice
	 */
	private VerbVoice voice;
	/**
	 * Verb Tense
	 */
	private VerbTense tense;
	private Long startOffset;
	private Long endOffset;
	/**
	 * Directionaly of verb
	 * true for L->R
	 * false for R->L
	 */
	private Boolean directional;
	
	public VerbInfo(String verb,String lemma,Integer polarity,VerbVoice voice,VerbTense tense,boolean directional,
			Long startOffset,Long endOffset)
	{
		this.verb=verb;
		this.lemma=lemma;
		this.polarity=polarity;
		this.voice=voice;
		this.tense=tense;
		this.startOffset=startOffset;
		this.endOffset=endOffset;
		this.directional=directional;
	}
	
	public VerbInfo(Long startOffset,Long endOffset,String verb,String lemma, int polarity2)
	{
		this.startOffset=startOffset;
		this.endOffset=endOffset;
		this.verb=verb;
		this.lemma=lemma;
		this.polarity=polarity2;
		this.directional=null;
	}
	
	public boolean isDirectional() {
		return directional;
	}

	public String toString()
	{
		String all = new String();
		all = all.concat("verb : "+this.verb+" lemma : "+this.lemma+" pos "+this.polarity+" tense :"+this.tense.toString()+"voice : "+
				this.voice.toString()+" offset : "+startOffset);
		return all;
	}

	public Boolean getDirectionality() {
		return this.directional;
	}

	public Long getEndOffset() {
		return this.endOffset;
	}

	public Integer getPolarity() {
		return this.polarity;
	}

	public Long getStartOffset() {
		return this.startOffset;
	}

	public void setDirectionality(boolean dir) {
		this.directional=dir;
		
	}

	public void setPolarity(int polarity) {
		this.polarity=polarity;
	}

	public String getLemma() {
		return this.lemma;
	}

	public VerbTense getTense() {
		return this.tense;
	}

	public String getVerb() {
		return this.verb;
	}

	public VerbVoice getVoice() {
		return this.voice;
	}

	public void setLemma(String lemma) {
		this.lemma=lemma;
	}
	
	
	
	

}
