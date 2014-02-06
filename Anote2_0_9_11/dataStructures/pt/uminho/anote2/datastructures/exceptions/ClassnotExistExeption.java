package pt.uminho.anote2.datastructures.exceptions;

public class ClassnotExistExeption extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -554261703461997581L;
	
	public ClassnotExistExeption(String className)
	{
		super("Class "+className+" not Exist");
	}

}
