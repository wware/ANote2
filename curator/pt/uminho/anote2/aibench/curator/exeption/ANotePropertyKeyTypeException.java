package pt.uminho.anote2.aibench.curator.exeption;

import es.uvigo.ei.aibench.workbench.Workbench;

/**
 * 
 * @author paulo maia, Sep 27, 2007
 *
 */
public class ANotePropertyKeyTypeException extends Exception {

	private static final long serialVersionUID = 1L;

	public ANotePropertyKeyTypeException(){
		super();
	}
	
	public ANotePropertyKeyTypeException(String e){
		super(e);
		Workbench.getInstance().warn(e);
	}
}
