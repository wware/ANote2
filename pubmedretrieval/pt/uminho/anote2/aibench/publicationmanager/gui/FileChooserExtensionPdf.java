package pt.uminho.anote2.aibench.publicationmanager.gui;

import java.io.File;

import javax.swing.JFileChooser;

public class FileChooserExtensionPdf extends JFileChooser{

	private static final long serialVersionUID = 4546357651671103607L;
	private final boolean cancel_message;
	
	
	public FileChooserExtensionPdf(boolean cancel_message){
		super();
		this.cancel_message = cancel_message;
		change();
		this.setApproveButtonText("ok");
	}
	
	private void change() {

		
	}

	public FileChooserExtensionPdf(boolean cancel_message, File defaultDirectory){
		super(defaultDirectory);
		this.cancel_message = cancel_message;
	}
	
    @Override
    public boolean accept(File file) {
        return (file.getName().toLowerCase().endsWith(".pdf") || file.isDirectory());
    }
	
	

	public void approveSelection(){
		return;
	}
	
	
	
	
	
	@Override
	public void cancelSelection(){
		return;
		
//		if(!cancel_message)
//		{
//			super.cancelSelection();
//			return;
//		}
//		
//		int result = JOptionPane.showConfirmDialog(
//				getTopLevelAncestor(),
//                "Are you sure that you want to quit without saving?",
//                "Quit Confirmation",
//                JOptionPane.YES_NO_OPTION,
//                JOptionPane.QUESTION_MESSAGE);
//       
//		switch(result)  {
//        case JOptionPane.YES_OPTION:
//            super.cancelSelection();
//            return;
//        case JOptionPane.NO_OPTION:
//            return;
//        }
	}

	public boolean isCancel_message() {
		return cancel_message;
	}
		
}
