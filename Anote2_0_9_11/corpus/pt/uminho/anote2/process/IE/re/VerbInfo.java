package pt.uminho.anote2.process.IE.re;

import pt.uminho.anote2.core.annotation.DirectionallyEnum;
import pt.uminho.anote2.core.annotation.PolarityEnum;
import pt.uminho.anote2.process.IE.re.clue.IVerbInfo;
import pt.uminho.anote2.process.IE.re.clue.VerbTenseEnum;
import pt.uminho.anote2.process.IE.re.clue.VerbVoiceEnum;



/**
 * 
 * @author Hugo Costa
 * 
 *
 */

public class VerbInfo implements IVerbInfo{
	
	private String verb;
	private String lemma;
	/**
	 * 0 Negativo
	 * 1 Conditional
	 * 2 Positivo
	 * 3 Unknown
	 */
	private PolarityEnum polarity;
	/**
	 *  Verb Voice
	 */
	private VerbVoiceEnum voice;
	/**
	 * Verb Tense
	 */
	private VerbTenseEnum tense;
	private long startOffset;
	private long endOffset;
	/**
	 * Directionaly of verb
	 * true for L->R
	 * false for R->L
	 */
	private DirectionallyEnum directional;
	
	public VerbInfo(String verb,String lemma,PolarityEnum polarity,VerbVoiceEnum voice,VerbTenseEnum tense,DirectionallyEnum directional,
			long startOffset,long endOffset)
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
	
	public VerbInfo(long startOffset,long endOffset,String verb,String lemma, PolarityEnum polarity2)
	{
		this.startOffset=startOffset;
		this.endOffset=endOffset;
		this.verb=verb;
		this.lemma=lemma;
		this.polarity=polarity2;
		this.directional=DirectionallyEnum.Unknown;
	}
	

	public String toString()
	{
		String all = new String();
		all = all.concat("verb : "+this.verb+" lemma : "+this.lemma+" pos "+this.polarity+" tense :"+this.tense.toString()+"voice : "+
				this.voice.toString()+" offset : "+startOffset);
		return all;
	}

	public DirectionallyEnum getDirectionality() {
		return this.directional;
	}

	public long getEndOffset() {
		return this.endOffset;
	}

	public PolarityEnum getPolarity() {
		return this.polarity;
	}

	public long getStartOffset() {
		return this.startOffset;
	}

	public void setDirectionality(DirectionallyEnum dir) {
		this.directional=dir;
		
	}

	public void setPolarity(PolarityEnum polarity) {
		this.polarity=polarity;
	}

	public String getLemma() {
		return this.lemma;
	}

	public VerbTenseEnum getTense() {
		return this.tense;
	}

	public String getVerb() {
		return this.verb;
	}

	public VerbVoiceEnum getVoice() {
		return this.voice;
	}

	public void setLemma(String lemma) {
		this.lemma=lemma;
	}
	
	
	
	

}
