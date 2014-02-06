package es.uvigo.ei.sing.BioSearch.datatypes;

import java.util.Vector;

//import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;

//@Datatype(viewable=false, namingMethod="getName",setNameMethod="setName")
public class CollectionResult {

	private Class<?> identifiersClass;
	private String searchParams;
	private Vector<String> ids = new Vector<String>();
	private Vector<String[]> busq = new Vector<String[]>();
	private Vector<String> resultAttributes=new Vector<String>();
	private String bdName = "";
	public String name;
	
	
	public int getSearchIDsSize(){
		if (ids == null) return 0;
		return ids.size();
	}
	
	public String getIDAt(int pos){
		return ids.elementAt(pos);
	}
	public void addID(String id){
		ids.addElement(id);
	}
	public int getResultSize(){
		return busq.size();
	}
	public String[] getResultAt(int pos){
		return busq.elementAt(pos);
	}
	
	
	public void addResult(String[] row){
		this.busq.add(row);
	}
	
	public String getSearchParams() {
		return searchParams;
	}
	public void setSearchParams(String searchParams) {
		this.searchParams = searchParams;
	}

	public Vector<String> getResultAttributes() {
		return resultAttributes;
	}

	public void setResultAttributes(Vector<String> resultAttributes) {
		this.resultAttributes = resultAttributes;
	}
	
	public void addResultAttribute(String attribute){
		this.resultAttributes.addElement(attribute);
	}

	public String getBdName() {
		return bdName;
	}

	public void setBdName(String bdName) {
		this.bdName = bdName;
	}

	public Class<?> getIdentifiersClass() {
		return identifiersClass;
	}

	public void setIdentifiersClass(Class<?> identifiersClass) {
		this.identifiersClass = identifiersClass;
	}

	/**
	 * @return the name of Collection
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to Collection
	 */
	public void setName(String name) {
		this.name = name;
	}

	public Vector<String> getIds() {
		return ids;
	}

	public Vector<String[]> getBusq() {
		return busq;
	}
	
}
