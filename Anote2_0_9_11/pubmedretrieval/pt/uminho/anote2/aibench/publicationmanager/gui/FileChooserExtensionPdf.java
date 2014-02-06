package pt.uminho.anote2.aibench.publicationmanager.gui;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import pt.uminho.anote2.aibench.utils.session.SessionSettings;

public class FileChooserExtensionPdf extends JFileChooser{

	private static final long serialVersionUID = 4546357651671103607L;
	private String pdf = ".pdf";

	public FileChooserExtensionPdf(){
		super();
		this.setApproveButtonText("Select");
		addFileFilter();
		disableTextField(this.getComponents());
		setFileHome();
	}


	private void setFileHome() {
		String file = SessionSettings.getSessionSettings().getSearchDirectory();
		if(file!=null)
		{
			this.setCurrentDirectory(new File(file));
		}
	}


	@Override
	public boolean accept(File file) {
		return (file.isFile() && file.getName().toLowerCase().endsWith(pdf) || file.isDirectory());
	}

	private void addFileFilter()
	{
		FileFilter filter = new FileFilter(){

			public boolean accept(File file) {
				return (file.isFile() && file.getName().toLowerCase().endsWith(pdf));
			}

			public String getDescription() {
				return pdf;
			}

		};
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
