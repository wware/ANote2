package pt.uminho.anote2.aibench.resources.operations.ontologies;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import pt.uminho.anote2.aibench.resources.datastructures.ontologies.OBOOntologyExtensionv1p2;
import pt.uminho.anote2.aibench.resources.datatypes.OntologyAibench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.operations.TimeLeftProgress;
import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation()
public class LoadOBOOntologyv1p2 {
	
	private OntologyAibench ontology;	
	private TimeLeftProgress progress = new TimeLeftProgress();
	private OBOOntologyExtensionv1p2 onto;
	
	@Port(name="Ontology",direction=Direction.INPUT,order=1)
	public void setOntology(OntologyAibench ontology)
	{
		if(ontology.isOntologyFill())
		{
			Workbench.getInstance().warn("Ontology already have elements.. can not updated..!!!");
			return;
		}
		this.ontology=ontology;
	}
		

	@Port(name="File",direction=Direction.INPUT,order=2)
	public void setFile(File file)
	{

		onto = new OBOOntologyExtensionv1p2(file, ontology.getName(),ontology.getInfo(), ontology.getDb(), progress);
		if(!onto.validateFile(file))
		{
			Workbench.getInstance().warn("File is not supported..!!!");
			return;
		}
		onto.getOntology(ontology);
		ontology.notifyViewObservers();
		loaderStats(onto.getNumberTerms(),onto.getNumberSyn());
		new ShowMessagePopup("Ontology Updated !!!");
	}
	
	
	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}

	@Cancel
	public void cancel(){
		onto.cancel();
		Workbench.getInstance().warn("Ontology Update Cancel!");
		return;
	}
	
	protected boolean loaderStats(int terms,int syn){
		Object[] options = new String[]{"Continue"};	
		int opt = showOptionPane("Ontology Update Stats","New Entities :"+terms+"\n Synonyms :"+syn, options);
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

		option_pane.setIcon(new ImageIcon("plugins_src/pt.uminho.di.anote2/icons/messagebox_question.png"));
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
