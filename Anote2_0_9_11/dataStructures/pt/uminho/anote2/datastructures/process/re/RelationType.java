package pt.uminho.anote2.datastructures.process.re;

import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.process.IE.re.IRelationsType;

/**
 * Class That represent a relation type. Relation type represnt witch class are present in left and right side of relation
 * 
 * @author Hugo Costa
 *
 */
public class RelationType implements  IRelationsType{

	private int leftClassID;
	private int rightClassID;


	public RelationType(int leftClassID, int rightClassID) {
		super();
		this.leftClassID = leftClassID;
		this.rightClassID = rightClassID;
	}

	public int getRightClassID() {
		return rightClassID;
	}

	public int getLeftClassID() {
		return leftClassID;
	}
	
	public int compareTo(IRelationsType o) {
		if(this.leftClassID==o.getLeftClassID())
		{
			return this.rightClassID-o.getRightClassID();
		}
		return this.leftClassID-o.getLeftClassID();
	}
	
	public String toString()
	{
		return ClassProperties.getClassIDClass().get(leftClassID)+"--"+ClassProperties.getClassIDClass().get(rightClassID);
	}
	
}
