package pt.uminho.generic.components.jchooser;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import pt.uminho.anote2.aibench.utils.session.SessionSettings;

public class FileChooserCSVExtention extends FileChooserLoadExtention{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FileChooserCSVExtention() {
		super(getExtentionsSupported());
		sethomeDirectory();
	}

	private void sethomeDirectory() {
		String homeDirectory = SessionSettings.getSessionSettings().getSearchDirectory();
		if(homeDirectory!=null)
			this.setCurrentDirectory(new File(homeDirectory));
	}

	private static Set<String> getExtentionsSupported() {
		Set<String> supported = new HashSet<String>();
		supported.add("txt");
		supported.add("csv");
		supported.add("tsv");
		return supported;
	}



}
