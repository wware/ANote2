package pt.uminho.anote2.datastructures.exceptions.process.manualcuration;

public class ManualCurationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ManualCurationException(String notes)
	{
		super(notes);
	}

}
