package pt.uminho.generic.operation;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import es.uvigo.ei.aibench.workbench.Workbench;

public class OperationOptionPane {

	/** Presents a option pane with the given title, question and options */
	protected static int showOptionPane(String title, String question, Object[] options){
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

	public static boolean loaderStats(String generalInfo,String info){
		Object[] options = new String[]{"Continue"};
		int opt = showOptionPane(generalInfo,info, options);
		switch (opt) {
		case 0:
			return true;
		default:
			return true;
		}

	}
}
