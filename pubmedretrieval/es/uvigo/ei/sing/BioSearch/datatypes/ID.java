package es.uvigo.ei.sing.BioSearch.datatypes;

public class ID {
	private String id;
	
	public ID(String id){
		this.id = id;
	}
	@Override
	public String toString(){
		return id;
	}
	
	@Override
	public boolean equals(Object o){
		return (o instanceof ID && ((ID) o).id.equals(id));
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
}
