package pt.uminho.anote2.datastructures.process.network;

import java.util.List;

import pt.uminho.anote2.process.IE.network.IAtributes;

public class Atributes implements IAtributes{
	

	private String label;
	private String name;
	private String value;
	private String valueType;
	private boolean isListAtribute;
	private List<IAtributes> listAtributes;	
	
	public Atributes(String label, String name, String value, String valueType,
			boolean isListAtribute, List<IAtributes> listAtributes) {
		super();
		this.label = label;
		this.name = name;
		this.value = value;
		this.valueType = valueType;
		this.isListAtribute = isListAtribute;
		this.listAtributes = listAtributes;
	}
	
	public String getLabel() {
		return label;
	}
	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
	public String getValueType() {
		return valueType;
	}
	public boolean isListAtribute() {
		return isListAtribute;
	}
	public List<IAtributes> getListAtributes() {
		return listAtributes;
	}

}
