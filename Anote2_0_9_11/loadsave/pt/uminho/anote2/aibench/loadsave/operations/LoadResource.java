package pt.uminho.anote2.aibench.loadsave.operations;

import java.io.File;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import pt.uminho.anote2.aibench.resources.datatypes.DictionaryAibench;
import pt.uminho.anote2.aibench.resources.datatypes.LexicalWordsAibench;
import pt.uminho.anote2.aibench.resources.datatypes.LookupTableAibench;
import pt.uminho.anote2.aibench.resources.datatypes.OntologyAibench;
import pt.uminho.anote2.aibench.resources.datatypes.Resources;
import pt.uminho.anote2.aibench.resources.datatypes.RulesAibench;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation(name="Load Resources Status By File")
public class LoadResource {
	
	
	@Port(name="file",direction=Direction.BOTH,order=1)
	public Resources getResources(File file) throws Exception{

		List<ClipboardItem> lisRm = Core.getInstance().getClipboard().getItemsByClass(Resources.class);
		List<ClipboardItem> dics = Core.getInstance().getClipboard().getItemsByClass(DictionaryAibench.class);
		List<ClipboardItem> looks = Core.getInstance().getClipboard().getItemsByClass(LookupTableAibench.class);
		List<ClipboardItem> rules = Core.getInstance().getClipboard().getItemsByClass(RulesAibench.class);
		List<ClipboardItem> onto = Core.getInstance().getClipboard().getItemsByClass(OntologyAibench.class);
		List<ClipboardItem> words = Core.getInstance().getClipboard().getItemsByClass(LexicalWordsAibench.class);

		Resources rs;
		if(dics.size()+looks.size()+rules.size()+onto.size()+words.size()>0)
		{
			if(overwriteGUI())
			{
				Core.getInstance().getClipboard().removeClipboardItem(lisRm.get(0));
				rs = new Resources();
				rs.loadFile(file);
				return rs;
			}
			else
			{
				return null;
			}
		}
		else
		{
			if(lisRm.size()>0)
				Core.getInstance().getClipboard().removeClipboardItem(lisRm.get(0));
			rs = new Resources();
			rs.loadFile(file);
		}
		return rs;	
	}
	
	private boolean overwriteGUI(){
		Object[] options = new String[]{"Overwrite","Cancel"};
		int opt = showOptionPane("@Note2 Notification","@Note2 already contain Resources Data-type \n Do you wish overwrite it ?", options);
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
