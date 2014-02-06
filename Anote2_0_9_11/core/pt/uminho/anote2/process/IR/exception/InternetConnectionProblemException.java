package pt.uminho.anote2.process.IR.exception;


public class InternetConnectionProblemException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InternetConnectionProblemException(Exception e)
	{
		super(e);
	}

}
