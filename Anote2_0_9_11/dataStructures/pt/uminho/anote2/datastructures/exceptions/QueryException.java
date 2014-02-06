package pt.uminho.anote2.datastructures.exceptions;

/**
 * Classe that implements a Query Exception
 * 
 * @author Hugo Costa
 * 
 * @version 1.0 (16 Junho 2009)
 *
 */
public class QueryException extends Exception{
	
	private static final long serialVersionUID = -4574335219818517049L;

	public QueryException(){
		super();
	}
	
	public QueryException(String e){
		super(e);
	}

}
