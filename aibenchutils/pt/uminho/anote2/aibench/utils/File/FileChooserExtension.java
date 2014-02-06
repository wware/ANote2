package pt.uminho.anote2.aibench.utils.File;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FileChooserExtension extends JFileChooser{

	private static final long serialVersionUID = 4546357651671103607L;
	private final boolean cancel_message;
	
	
	public FileChooserExtension(boolean cancel_message){
		super();
		this.cancel_message = cancel_message;
		change();
	}
	
	private void change() {
		// TODO Auto-generated method stub
		
	}

	public FileChooserExtension(boolean cancel_message, File defaultDirectory){
		super(defaultDirectory);
		this.cancel_message = cancel_message;
	}
	
	
	@Override
	public void approveSelection(){
		File file = getSelectedFile();
		
		if(file.getName().contains("."))
			if(!file.getName().toLowerCase().endsWith(".pdf"))
			{
				 JOptionPane.showInternalMessageDialog(
						 this.getParent(), 
						 "The extension must be \".pdf\"",
						 "information",
						 JOptionPane.INFORMATION_MESSAGE
						 );
				return;
			}
		
//		if(file.length()>0)
//		{
//			int result = JOptionPane.showConfirmDialog(
//					getTopLevelAncestor(),
//                    "The file already exists. Do you want to overwrite it?",
//                    "The file already exists",
//                    JOptionPane.YES_NO_CANCEL_OPTION,
//                    JOptionPane.QUESTION_MESSAGE);
//            
//			switch(result)  {
//            case JOptionPane.YES_OPTION:
//                super.approveSelection();
//                return;
//            case JOptionPane.NO_OPTION:
//                return;
//            case JOptionPane.CANCEL_OPTION:
//                cancelSelection();
//                return;
//            }
//		}
//		// else
		super.approveSelection();
	}

	public boolean isCancel_message() {
		return cancel_message;
	}
	
	
	
	
	
//	@Override
//	public void cancelSelection(){
//		
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
//	}
		
}
