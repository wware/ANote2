package pt.uminho.anote2.aibench.loadsave.operations;

import java.io.File;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation(name="Load Corpus status")
public class LoadCorpus {
	

	@Port(name="file",direction=Direction.BOTH,order=1)
	public Corpora getCorpora(File file) throws Exception{
		List<ClipboardItem> lisCorpora = Core.getInstance().getClipboard().getItemsByClass(Corpora.class);
		List<ClipboardItem> lisRm = Core.getInstance().getClipboard().getItemsByClass(Corpus.class);		
		Corpora pm;
		if(lisRm.size()!=0)
		{
			if(overwriteGUI())
			{
				Core.getInstance().getClipboard().removeClipboardItem(lisCorpora.get(0));
				lisCorpora.remove(0);
				pm = new Corpora();
				pm.loadFile(file);
				new ShowMessagePopup("Corpora Overwrited .");
				return pm;
			}
			else
			{
				return null;
			}
		}
		else
		{
			if(lisCorpora.size()>0)
				Core.getInstance().getClipboard().removeClipboardItem(lisCorpora.get(0));
			pm = new Corpora();
			pm.loadFile(file);
		}
		new ShowMessagePopup("Corpora Load.");
		return pm;	
	}
	
	private boolean overwriteGUI(){
		Object[] options = new String[]{"Overwrite","Cancel"};
		int opt = showOptionPane("@Note2 Notification","@Note2 already contain Corpus Information Data-type \n Do you wish overwrite it ?", options);
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
