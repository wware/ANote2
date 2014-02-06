package pt.uminho.anote2.process.IE.re;

public interface IVerbInfo {
	
	public String getVerb();
	public String getLemma();
	public Integer getPolarity();
	public VerbVoice getVoice();
	public VerbTense getTense();
	public Long  getStartOffset();
	public Long getEndOffset();
	public Boolean getDirectionality();
	
	public void setLemma(String lemma);
	/**
	 * -1 Negativa
	 *  0 Condicional
	 *  1 Positiva
	 */
	public void setPolarity(int polarity);
	/**
	 * True -> Direita para a Esquerda e Ambos
	 * Falso -> Esquerda para a direita
	 */
	public void setDirectionality(boolean dir);

}
