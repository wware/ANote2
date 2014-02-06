package pt.uminho.anote2.aibench.curator.exeption;

import es.uvigo.ei.aibench.workbench.Workbench;

public class ANoteFileNotExistsException extends Exception{
	
	private static final long serialVersionUID = 1840558902680646019L;

	public ANoteFileNotExistsException(){
		super();
	}
		
	public ANoteFileNotExistsException(String e){
		super(e);
		Workbench.getInstance().warn(e);
	}

}
