package pt.uminho.anote2.relation.datastructures;

import pt.uminho.anote2.core.annotation.DirectionallyEnum;
import pt.uminho.anote2.datastructures.utils.GenericTriple;
import pt.uminho.anote2.process.IE.re.IDirectionality;
import pt.uminho.anote2.process.IE.re.ISentenceSintaxRepresentation;
import pt.uminho.anote2.process.IE.re.clue.IVerbInfo;

public class Directionality implements IDirectionality{

	public Directionality()
	{
		
	}
	

	public DirectionallyEnum getDirectionality(IVerbInfo verbInfo, ISentenceSintaxRepresentation sentenceSintax) {
	
		return byRule(verbInfo, sentenceSintax);
	}


	private DirectionallyEnum byRule(IVerbInfo verbInfo,ISentenceSintaxRepresentation sentenceSintax) {
		Long endVerbPos = verbInfo.getEndOffset();
		GenericTriple<String,String,String> nextPos = sentenceSintax.getNextElement(endVerbPos);
		if(nextPos!=null && nextPos.getZ().equals("by"))
			return DirectionallyEnum.RightToLeft;
		else
			return DirectionallyEnum.LeftToRight;
	}

}
