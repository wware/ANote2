package pt.uminho.anote2.aibench.utils.exceptions;

import es.uvigo.ei.aibench.workbench.Workbench;

public class MissingDatatypesException extends Exception{
	
	private static final long serialVersionUID = 7513661256220093063L;

	public MissingDatatypesException(){
		super();
	}
		
	public MissingDatatypesException(String e){
		super(e);
		Workbench.getInstance().warn(e);
	}
	
}
