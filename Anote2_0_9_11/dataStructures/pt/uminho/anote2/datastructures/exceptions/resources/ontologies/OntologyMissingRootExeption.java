package pt.uminho.anote2.datastructures.exceptions.resources.ontologies;

import pt.uminho.anote2.datastructures.utils.conf.GlobalTextInfoSmall;

public class OntologyMissingRootExeption extends Exception {
	
	private static final long serialVersionUID = -4574335219818517049L;

	public OntologyMissingRootExeption(){
		super(GlobalTextInfoSmall.ontologyrootFault);
	}

}
