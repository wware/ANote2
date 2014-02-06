package pt.uminho.anote2.relation.operations;

import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import process.IGatePosTagger;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.NERProcess;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.operations.TimeLeftProgress;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.process.IE.INERProcess;
import pt.uminho.anote2.process.IE.re.IRelationModel;
import pt.uminho.anote2.relation.core.RelationExtraxtionExtension;
import pt.uminho.anote2.relation.core.relationModels.RelationModelSimple;
import pt.uminho.anote2.relation.core.relationModels.RelationModelVerbLimitation;
import pt.uminho.anote2.relation.datastructures.Directionality;
import pt.uminho.anote2.relation.datastructures.Polarity;
import pt.uminho.anote2.relation.datastructures.PosTaggerEnem;
import pt.uminho.anote2.relation.datastructures.RelationsModelEnem;
import pt.uminho.anote2.relation.gate.Gate51PosTagger;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;
import gate.util.GateException;

@Operation(description="Extract Relations")
public class OperationRelationExtraction {
	
	private PosTaggerEnem tagger;
	private RelationsModelEnem model;
	private ICorpus corpus;
	private INERProcess nerProcess;
	private TimeLeftProgress progress = new TimeLeftProgress();
	private int relationsAnnotated = 0;
	private int entitiesAssociation = 0;
	
	
	@Port(name="Corpus",description="Corpus to Relation Extraction Process",direction=Direction.INPUT,order=1)
	public void getCorpus(Corpus corpus)
	{
		this.corpus=corpus;
	}
	
	@Port(name="NER Process",description="Process that contains Entity information",direction=Direction.INPUT,order=2)
	public void getCorpus(NERProcess nerProcess)
	{
		this.nerProcess=nerProcess;
	}
	
	@Port(name="Pos-Tagger",description="Pos-Tagger",direction=Direction.INPUT,order=3)
	public void getTAgger(PosTaggerEnem tagger)
	{
		this.tagger=tagger;
	}
	
	@Port(name="Relation Model",description="Model",direction=Direction.INPUT,order=4)
	public void getTAgger(RelationsModelEnem model) throws GateException
	{
		this.model=model;
		findRelations();
	}
	
	public void findRelations() throws GateException
	{
		IGatePosTagger postagger = null;
		IRelationModel relationModel;

		switch (tagger) {
		case Gate_POS:
			postagger = new Gate51PosTagger(new Directionality(),new Polarity());
			break;
		}
		switch (model) {
		case Verb_Limitation:
			relationModel = new RelationModelVerbLimitation(corpus,nerProcess,postagger);
			break;

		default:
			relationModel = new RelationModelSimple(corpus,nerProcess,postagger);
			break;
		}
		
		Properties prop = new Properties();
		prop.put("nerprocess",String.valueOf(nerProcess.getID()));
		prop.put("process","Rel@tion Process");
		prop.put("tagger",String.valueOf(tagger.name()));
		prop.put("relationModel",String.valueOf(relationModel));
		RelationExtraxtionExtension relExtraction = new RelationExtraxtionExtension(corpus,nerProcess,postagger,relationModel,prop,progress);
		corpus.registerProcess(relExtraction);
		relExtraction.executeRE();
		relationsAnnotated = relExtraction.getRelationsAnnotated();
		entitiesAssociation = relExtraction.getEntitiesAssociation();
		((Corpus) corpus).notifyViewObserver();
		if(loaderStats())
		{
			
		}
		new ShowMessagePopup("RE @Note Process Done !!");
	}
		
	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}
	
	private boolean loaderStats(){
		Object[] options = new String[]{"Continue"};
		int opt = showOptionPane("RE Finishing Stats","Events Annotated :"+relationsAnnotated+"\nEntities Associated whit relations  :"+entitiesAssociation+"\nDocuments :"+corpus.getArticlesCorpus().size(),options);
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
