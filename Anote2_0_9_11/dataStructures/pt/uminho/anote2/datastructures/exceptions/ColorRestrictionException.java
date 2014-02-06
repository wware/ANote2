package pt.uminho.anote2.datastructures.exceptions;

public class ColorRestrictionException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ColorRestrictionException(String color)
	{
		super("Class color can not be "+color);
	}

}
