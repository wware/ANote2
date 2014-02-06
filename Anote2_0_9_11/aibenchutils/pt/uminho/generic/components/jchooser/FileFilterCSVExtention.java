package pt.uminho.generic.components.jchooser;

import java.io.File;
import java.util.Set;

import javax.swing.filechooser.FileFilter;

public class FileFilterCSVExtention extends FileFilter{

	private Set<String> extentions;
	
	public FileFilterCSVExtention(Set<String> extentions)
	{
		this.extentions = extentions;
	}
	
	
	@Override
	public boolean accept(File file) {
		for(String ext:extentions)
		{
			if(file.isFile() && file.getName().toLowerCase().endsWith(ext))
				return true;	
		}
		return false;
	}

	@Override
	public String getDescription() {
		String result = new String();
		for(String ext:extentions)
		{
			result = result +"\""+ext+"\" ";
		}
		return result;
	}

}
