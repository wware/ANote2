package pt.uminho.anote2.aibench.utils.operations;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation
public class ExitOperation {
	
	@Port(name="Exit",direction=Direction.OUTPUT,order=1)
	public String getReferenceManager(){
		
		if(stopQuestion())
		{
			System.exit(1);
		}
		return null;
	}
	
	private boolean stopQuestion(){
		Object[] options = new String[]{"Yes", "No"};
		int opt = showOptionPane("Exit", "Exit Program", options);
		switch (opt) {
		case 0:
			return true;
		default:
			return false;
		}
	}
	
	/** Presents a option pane with the given title, question and options */
	public static int showOptionPane(String title, String question, Object[] options){
		JOptionPane option_pane = new JOptionPane(question);

		option_pane.setOptions(options);
		
		JDialog dialog = option_pane.createDialog(Workbench.getInstance().getMainFrame(), title);
		dialog.setVisible(true);
		
		Object choice = option_pane.getValue();
					
		for(int i=0; i<options.length; i++)
			if(options[i].equals(choice))
				return i;
		
		return -1;		
	}

}
