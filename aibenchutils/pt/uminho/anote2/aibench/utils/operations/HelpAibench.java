package pt.uminho.anote2.aibench.utils.operations;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.inputgui.ParamsWindow;

public class HelpAibench {
	
	public static Object getSelectedItem(Class<?> classe)
	{
		JTree tree = Workbench.getInstance().getTreeManager().getAIBenchClipboardTree();
		TreePath selPath = tree.getSelectionPath();
		if (selPath != null) {
			final DefaultMutableTreeNode node = (DefaultMutableTreeNode)
			selPath.getLastPathComponent();
			if (node.getUserObject() instanceof ClipboardItem){
				ClipboardItem item = (ClipboardItem)node.getUserObject();
				ParamsWindow.preferredClipboardItem = item;
				Object data = item.getUserData(); //O ITEM SELECCIONADO!!!!!!!!!!
				if(classe.isAssignableFrom(data.getClass()))
				{
					return data;
				}
				else
				{
					return null;
				}
			}
		}
		return null;
	}
	
	/** Presents a option pane with the given title, question and options */
	public static int showOptionPane(String title, String question, Object[] options){
		JOptionPane option_pane = new JOptionPane(question);

		option_pane.setIcon(new ImageIcon("plugins_src/pt.uminho.di.anote/icons/messagebox_question.png"));
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
