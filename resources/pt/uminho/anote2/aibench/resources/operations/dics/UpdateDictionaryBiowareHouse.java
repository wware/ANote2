package pt.uminho.anote2.aibench.resources.operations.dics;

import java.text.DecimalFormat;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import pt.uminho.anote2.aibench.resources.datastructures.biowarehouse.BiowarehouseExtension;
import pt.uminho.anote2.aibench.resources.datatypes.DictionaryAibench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.operations.TimeLeftProgress;
import pt.uminho.anote2.core.database.IDatabase;
import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation()
public class UpdateDictionaryBiowareHouse {
	
	private DictionaryAibench dic;
	private IDatabase biowarehousedb;
	private Set<String> classLoader;
	
	private TimeLeftProgress progress = new TimeLeftProgress();
	private DecimalFormat round = new DecimalFormat("0.00");
	private int numberTerms = 0;
	private int numberSyn = 0;
	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}

	@Cancel
	public void cancel(){
		//cancelMethod();
		dic.notifyViewObservers();
		Workbench.getInstance().warn("Dictionary Update Cancel!");	
	}
	


	@Port(name="biowaredatabase",direction=Direction.INPUT,order=1)
	public void setBiowarehouse(IDatabase biowarehousedb)
	{
		this.biowarehousedb=biowarehousedb;
	}
	
	@Port(name="dictionary",direction=Direction.INPUT,order=2)
	public void setSource(DictionaryAibench dictionaryAibench)
	{
		this.dic=dictionaryAibench;
	}
	
	@Port(name="classLoader",direction=Direction.INPUT,order=3)
	public void setEntity(Set<String> classLoader)
	{
		this.classLoader=classLoader;
	}
	
	@Port(name="organism",direction=Direction.INPUT,order=4)
	public void setOrganism(Boolean syn)
	{
		BiowarehouseExtension bio = new BiowarehouseExtension(dic,biowarehousedb,false,progress);
		bio.loadTermsFromBiowareHouse(classLoader, syn);
		numberSyn = bio.getNumberSyn();
		numberTerms = bio.getNumberTerms();
		if(loaderStats())
		{
			
		}
		dic.notifyViewObservers();
		new ShowMessagePopup("Update Dictionary Complete !!!");
	}
	
	private boolean loaderStats(){
		Object[] options = new String[]{"Continue"};
		float averageSyn = (float) numberSyn/(float) numberTerms;
		
		
		int opt = showOptionPane("Dictionary Update Stats","New Entities :"+numberTerms+"\n Avarage Syn per Term :"+round.format(averageSyn), options);
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
