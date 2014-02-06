package pt.uminho.anote2.process.IE.re;

import pt.uminho.anote2.core.annotation.DirectionallyEnum;
import pt.uminho.anote2.process.IE.re.clue.IVerbInfo;



public interface IDirectionality {
	
	public DirectionallyEnum getDirectionality(IVerbInfo verbInfo,ISentenceSintaxRepresentation sentenceSintax);

}
