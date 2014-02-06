package pt.uminho.anote2.aibench.resources.operations;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import pt.uminho.anote2.aibench.resources.datatypes.Resources;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation
public class ExitResources {
	
	@Port(name="Exit",direction=Direction.OUTPUT,order=1)
	public String getReferenceManager(){
		
		List<ClipboardItem> lisRm = Core.getInstance().getClipboard().getItemsByClass(Resources.class);
		if(lisRm.size()==1)
		{
			if(stopQuestion())
			{
				Workbench.getInstance().elementRemoved(lisRm.get(0));
				Core.getInstance().getClipboard().removeClipboardItem(lisRm.get(0));
				new ShowMessagePopup("Plugin Resources Disable");
			}
		}
		return null;
	}
	
	private boolean stopQuestion(){
		Object[] options = new String[]{"Yes", "No"};
		int opt = showOptionPane("Close Resources Plugin", "", options);
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

		//option_pane.setIcon(new ImageIcon("messagebox_question.png"));
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
