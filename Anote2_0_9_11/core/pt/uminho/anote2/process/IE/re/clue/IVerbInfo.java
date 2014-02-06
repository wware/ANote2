package pt.uminho.anote2.process.IE.re.clue;

import pt.uminho.anote2.core.annotation.DirectionallyEnum;
import pt.uminho.anote2.core.annotation.PolarityEnum;


public interface IVerbInfo {
	
	public String getVerb();
	public String getLemma();
	public PolarityEnum getPolarity();
	public VerbVoiceEnum getVoice();
	public VerbTenseEnum getTense();
	public long  getStartOffset();
	public long getEndOffset();
	public DirectionallyEnum getDirectionality();
	
	public void setLemma(String lemma);
	/**
	 *  0 Negativa
	 *  1 Condicional
	 *  2 Positiva
	 *  3 Unknown
	 */
	public void setPolarity(PolarityEnum polarity);
	/**
	 * 0 - LeftToRight
	 * 1 - RightToLeft
	 * 2 - Unknown
	 * 3 - Both
	 */
	public void setDirectionality(DirectionallyEnum dir);

}
