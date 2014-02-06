package pt.uminho.anote2.aibench.resources.operations.dics;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import pt.uminho.anote2.aibench.resources.datatypes.DictionaryAibench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.operations.TimeLeftProgress;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation()
public class MergeDictionary {
	

	
	private TimeLeftProgress progress = new TimeLeftProgress();
	private DictionaryAibench dic1;

	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}

//	@Cancel
//	public void cancel(){
//		cancelMethod();
//	}

	@Port(name="dictionary dest",direction=Direction.INPUT,order=1)
	public void setDicionariesDest(DictionaryAibench dic1)
	{
		this.dic1=dic1;
	}
	
	@Port(name="dictionary source",direction=Direction.INPUT,order=2)
	public void setDicionariesSource(DictionaryAibench dic2)
	{
		if(dic1.compareTo(dic2)==0)
		{
			Workbench.getInstance().warn("Insert the same dictionaries");
		}
		else
		{
			dic1.merge(dic2, progress, true);
			dic1.notifyViewObservers();
			if(loaderStats())
			{
				
			}
			new ShowMessagePopup("Merge Dictionaries Done!!!");
		}
	}
	
	private boolean loaderStats(){
		Object[] options = new String[]{"Continue"};		
		int opt = showOptionPane("Dictionary Merge Stats","Terms :"+dic1.getTermsMergeAdding()+"\nSynonyms :"+dic1.getTermsMergeSynonymsAdding()+"\nClasses :"+dic1.getClasssesMergeAdding(), options);
		switch (opt) {
		case 0:
			return true;
		default:
			return true;
		}
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
