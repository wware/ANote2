package pt.uminho.generic.components.jchooser;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import pt.uminho.anote2.aibench.utils.session.SessionSettings;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;

public class FileChooserOBOExtention extends FileChooserLoadExtention{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FileChooserOBOExtention() {
		super(getExtensions());
		setHomeDirectory();
	}

	private void setHomeDirectory() {
		String homeDirectory = SessionSettings.getSessionSettings().getSearchDirectory();
		if(homeDirectory!=null)
			this.setCurrentDirectory(new File(homeDirectory));		
	}

	private static Set<String> getExtensions() {
		Set<String> supported = new HashSet<String>();
		supported.add("obo");
		return supported;
	}

	

}
