package pt.uminho.anote2.datastructures.exceptions;

import es.uvigo.ei.aibench.workbench.Workbench;

/**
 * Class that implements a Pubmed Search Exception
 */
public class PubmedException extends Exception{

	private static final long serialVersionUID = 6508039901961085938L;

	public PubmedException(){
		super();
	}
		
	public PubmedException(String e){
		super(e);
		Workbench.getInstance().error(e);
	}
	
}
