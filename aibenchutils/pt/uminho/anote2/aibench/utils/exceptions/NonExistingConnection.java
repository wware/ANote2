package pt.uminho.anote2.aibench.utils.exceptions;

import es.uvigo.ei.aibench.workbench.Workbench;

public class NonExistingConnection extends Exception {
	
	private static final long serialVersionUID = 1840558902680646019L;

	public NonExistingConnection(){
		super();
	}
		
	public NonExistingConnection(String e){
		super(e);
		Workbench.getInstance().warn(e);
	}

}
