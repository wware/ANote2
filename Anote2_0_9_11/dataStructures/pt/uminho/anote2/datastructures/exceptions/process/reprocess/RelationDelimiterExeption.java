package pt.uminho.anote2.datastructures.exceptions.process.reprocess;

import pt.uminho.anote2.datastructures.utils.conf.GlobalTextInfoSmall;


public class RelationDelimiterExeption extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public RelationDelimiterExeption()
	{
		super(GlobalTextInfoSmall.relationDelimiterExeption);
	}
	public RelationDelimiterExeption(String e){
		super(e);
	}
	

}
