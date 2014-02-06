package pt.uminho.anote2.datastructures.annotation.ner;

/**
 * 
 * Class that represent a Simple Entity .
 * 
 * @author Hugo Costa
 *
 */
public class SimpleEntity implements Comparable<SimpleEntity>{
	
	private String name;
	private int classID;
	private int resourceElementID;
	
	/**
	 * 
	 * @param name
	 * @param classID
	 * @param resourceElementID
	 */
	public SimpleEntity(String name, int classID, int resourceElementID) {
		super();
		this.name = name;
		this.classID = classID;
		this.resourceElementID = resourceElementID;
	}

	public SimpleEntity clone(){
		return new SimpleEntity(getName(), getClassID(), getResourceElementID());
	}

	public String toString() {
		return "SimpleEntity [name=" + name + ", classID=" + classID
				+ ", resourceElementID=" + resourceElementID + "]";
	}


	public int getClassID() {
		return classID;
	}

	public String getName() {
		return name;
	}
	
	public int compareTo(SimpleEntity o) {
		if(this.getResourceElementID()!=0 && this.getResourceElementID()==o.getResourceElementID())
		{
			return 0;
		}
		else if(this.getName().equals(o.getName()))
		{
			return this.getClassID() - o.getClassID();
		}
		else
		{
			return this.getName().compareTo(o.getName());
		}
	}
	
	public boolean equals(SimpleEntity o)
	{
		return compareTo(o) == 0;
	}


	public int getResourceElementID() {
		return resourceElementID;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
