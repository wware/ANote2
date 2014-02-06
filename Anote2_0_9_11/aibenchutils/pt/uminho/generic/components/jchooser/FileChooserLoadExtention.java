package pt.uminho.generic.components.jchooser;

import java.awt.Component;
import java.io.File;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FileChooserLoadExtention extends JFileChooser{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Set<String> extentionsSupportes;
	
	public FileChooserLoadExtention(Set<String> extentionsSupportes){
		super();
		this.extentionsSupportes = extentionsSupportes;
		this.setApproveButtonText("Select");
		addFileFilter();
		disableTextField(this.getComponents());
	}
	
    @Override
    public boolean accept(File file) {
    	if(file.isDirectory())
    		return true;
    	for(String extention:extentionsSupportes)
    	{
    		if(file.getName().toLowerCase().endsWith("."+extention))
    			return true;
    	}
        return false;
    }
    
	private void addFileFilter()
	{
		FileFilterCSVExtention filter = new FileFilterCSVExtention(extentionsSupportes);
		super.setFileFilter(filter);
	}

	public void disableTextField(Component[] comp)  
	{  
		for(int x = 0; x < comp.length; x++)  
		{  
			if(comp[x] instanceof JPanel) disableTextField(((JPanel)comp[x]).getComponents());  
			else if(comp[x] instanceof JTextField)  
			{  
				((JTextField)comp[x]).setEditable(false);  
				return;  
			}  
		}  
	}  

}
