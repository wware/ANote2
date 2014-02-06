package pt.uminho.anote2.aibench.corpus.structures;

import java.io.File;
import java.io.Serializable;

/**
 * Class that represent a pdf File 
 * 
 * @author Rafael Carreira
 *
 */
public class PDF implements Serializable{

	private static final long serialVersionUID = -3778870650705798906L;
	
	/**
	 * Pdf filepath
	 */
	private String filepath =null;
	
	public PDF(File file){
		filepath = file.getPath();
	}

	/**
	 * Method that a PDf File Path
	 * 
	 * @return PDf File Path
	 * 		   or null if PDf File Path don't exist
	 */
	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

}
