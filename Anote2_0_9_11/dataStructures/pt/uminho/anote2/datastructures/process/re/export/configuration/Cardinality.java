package pt.uminho.anote2.datastructures.process.re.export.configuration;

import pt.uminho.anote2.process.IE.re.export.configuration.ICardinality;

/**
 * 
 * Class for represent a relation cardinality 
 * Representation 
 * 0 - Zero
 * 1 - One
 * -1 - To Many
 * 
 * @author Hugo Costa
 *
 */
public class Cardinality implements ICardinality{

	private int leftEntities;
	private int righEntities;
	
	public Cardinality(int leftEntities, int righEntities) {
		super();
		this.leftEntities = leftEntities;
		this.righEntities = righEntities;
	}

	public int getLeftEntities() {
		return leftEntities;
	}

	public int getRightEntities() {
		return righEntities;
	}

	public int compareTo(ICardinality o) {
		if(this.leftEntities==o.getLeftEntities())
		{
			return this.righEntities - o.getRightEntities();
		}
		return this.leftEntities - o.getLeftEntities();
	}
	
	public boolean equals(ICardinality o)
	{
		return compareTo(o) == 0;
	}

	public String toString() {
		return getString(leftEntities) + " x " + getString(righEntities);
	}

	private String getString(int number) {
		if(number == 0)
			return "Zero";
		else if(number == 1)
			return "One";
		return "Many";
	}
	

}
