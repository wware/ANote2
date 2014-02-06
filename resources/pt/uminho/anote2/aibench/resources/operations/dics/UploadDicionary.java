package pt.uminho.anote2.aibench.resources.operations.dics;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import pt.uminho.anote2.aibench.resources.datastructures.BrendaExtension;
import pt.uminho.anote2.aibench.resources.datastructures.ChebiExtension;
import pt.uminho.anote2.aibench.resources.datastructures.EntrezGeneExtension;
import pt.uminho.anote2.aibench.resources.datastructures.NcbiTaxonomyExtension;
import pt.uminho.anote2.aibench.resources.datastructures.UniprotExtension;
import pt.uminho.anote2.aibench.resources.datastructures.biocyc.BioCycCoumpoundExtension;
import pt.uminho.anote2.aibench.resources.datastructures.biocyc.BioCycEnzymeExtension;
import pt.uminho.anote2.aibench.resources.datastructures.biocyc.BioCycGeneExtension;
import pt.uminho.anote2.aibench.resources.datastructures.biocyc.BioCycPathwaysExtension;
import pt.uminho.anote2.aibench.resources.datastructures.biocyc.BioCycProteinExtension;
import pt.uminho.anote2.aibench.resources.datastructures.biocyc.BioCycReactionExtension;
import pt.uminho.anote2.aibench.resources.datastructures.kegg.KeggCompoundExtension;
import pt.uminho.anote2.aibench.resources.datastructures.kegg.KeggEnzymesExtension;
import pt.uminho.anote2.aibench.resources.datastructures.kegg.KeggGenesExtension;
import pt.uminho.anote2.aibench.resources.datastructures.kegg.KeggReactionsExtension;
import pt.uminho.anote2.aibench.resources.datatypes.DictionaryAibench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.operations.TimeLeftProgress;
import pt.uminho.anote2.resource.dictionary.IDicionaryFlatFilesLoader;
import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation
public class UploadDicionary {
	
	private DictionaryAibench dic;
	private String source;
	private String entity;
	private Properties properties;
	private boolean cancel=false;
	private TimeLeftProgress progress = new TimeLeftProgress();
	private IDicionaryFlatFilesLoader dicLoader;
	
	private DecimalFormat round = new DecimalFormat("0.00");
	private int numberTerms = 0;
	private int numberSyn = 0;
	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}

	@Cancel
	public void cancel(){
		cancelMethod();
		dic.notifyViewObservers();
		Workbench.getInstance().warn("Dictionary Update Cancel!");	
	}
	
	private void cancelMethod() {

		dicLoader.setcancel(true);
	}

	@Port(name="dictionary",direction=Direction.INPUT,order=1)
	public void setDicionaries(DictionaryAibench dic)
	{
		this.dic=dic;
	}
	
	@Port(name="source",direction=Direction.INPUT,order=2)
	public void setSource(String source)
	{
		this.source=source;
	}
	
	@Port(name="entity",direction=Direction.INPUT,order=3)
	public void setEntity(String entity)
	{
		this.entity=entity;
	}
	
	@Port(name="organism",direction=Direction.INPUT,order=4)
	public void setOrganism(Properties properties)
	{
		this.properties=properties;
	}
	
	@Port(name="filepath",direction=Direction.INPUT,order=5)
	public void getFilePath(String filepath)
	{
		File file = new File(filepath);
		
		if(source.equals("Uniprot"))
		{
			dicLoader = new UniprotExtension(dic,cancel,progress);
			dicLoader.getDictionary().loadAllTerms();
			dicLoader.loadTermsFromFile(file,properties);
			dicLoader.getDictionary().deleteTerms();
			numberTerms = numberTerms+dicLoader.getNumberTerms();
			numberSyn = numberSyn+dicLoader.getNumberSyn();
		}
		else if(source.equals("EntrezGene"))
		{
			dicLoader = new EntrezGeneExtension(dic,cancel,progress);
			dicLoader.getDictionary().loadAllTerms();
			dicLoader.loadTermsFromFile(file,properties);
			dicLoader.getDictionary().deleteTerms();
			numberTerms = numberTerms+dicLoader.getNumberTerms();
			numberSyn = numberSyn+dicLoader.getNumberSyn();
		}
		else if(source.equals("NCBI Taxonomy"))
		{
			dicLoader = new NcbiTaxonomyExtension(dic,cancel,progress);
			dicLoader.getDictionary().loadAllTerms();
			dicLoader.loadTermsFromFile(file,properties);
			dicLoader.getDictionary().deleteTerms();
			numberTerms = numberTerms+dicLoader.getNumberTerms();
			numberSyn = numberSyn+dicLoader.getNumberSyn();
		}
		else if(source.equals("Chebi"))
		{
			dicLoader = new ChebiExtension(dic,cancel,progress);
			dicLoader.getDictionary().loadAllTerms();
			dicLoader.loadTermsFromFile(file,properties);
			dicLoader.getDictionary().deleteTerms();
			numberTerms = numberTerms+dicLoader.getNumberTerms();
			numberSyn = numberSyn+dicLoader.getNumberSyn();
		}
		else if(source.equals("Brenda"))
		{
			dicLoader = new BrendaExtension(dic,cancel,progress);
			dicLoader.getDictionary().loadAllTerms();
			dicLoader.loadTermsFromFile(file,properties);
			dicLoader.getDictionary().deleteTerms();
			numberTerms = numberTerms+dicLoader.getNumberTerms();
			numberSyn = numberSyn+dicLoader.getNumberSyn();
		}
		else if(source.equals("Kegg"))
		{
			if(entity.equals("enzyme"))
			{
				keggEnzymes(file,false);
			}
			else if(entity.equals("compound"))
			{
				keggCompound(file,false);
			}
			else if(entity.equals("reaction"))
			{
				keggReaction(file,false);
			}
			else if(entity.equals("gene"))
			{
				keggGene(file,false);
			}
			else
			{
				for(File file_folder:file.listFiles())
				{
					if(file_folder.isFile())
					{
						keggEnzymes(file_folder,true);
						keggCompound(file_folder,true);
						keggReaction(file_folder,true);
						keggGene(file_folder,true);
					}
				
				}
			}		
		}
		else
		{
			if(entity.equals("compound"))
			{
				bioCompound(file,false);
			}
			else if(entity.equals("enzyme"))
			{
				bioEnzyme(file,false);
			}
			else if(entity.equals("gene"))
			{
				bioGene(file,false);
			}
			else if(entity.equals("pathways"))
			{
				bioPathways(file,false);
			}
			else if(entity.equals("protein"))
			{
				bioProtein(file,false);
			}
			else if(entity.equals("enzyme (reactions file)"))
			{
				bioReaction(file,false);
			}
			else
			{
				for(File file_folder:file.listFiles())
				{
					if(file_folder.isFile())
					{
						bioCompound(file_folder,true);
						bioEnzyme(file_folder,true);
						bioGene(file_folder,true);
						bioPathways(file_folder,true);
						bioProtein(file_folder,true);
						bioReaction(file_folder,true);
					}
				}
			}
			
		}	
		if(loaderStats())
		{
			
		}
		dic.notifyViewObservers();
		new ShowMessagePopup("Dictionary Update Complete !!!");
	}

	private void bioReaction(File file,boolean ckeckFile) {
		dicLoader = new BioCycReactionExtension(dic,cancel,progress);
		if(ckeckFile)
		{
			if(!dicLoader.checkFile(file))
			{
				return;
			}
		}
		dicLoader.getDictionary().loadAllTerms();
		dicLoader.loadTermsFromFile(file,properties);
		dicLoader.getDictionary().deleteTerms();
		numberTerms = numberTerms+dicLoader.getNumberTerms();
		numberSyn = numberSyn+dicLoader.getNumberSyn();
	}

	private void bioProtein(File file,boolean ckeckFile) {
		dicLoader = new BioCycProteinExtension(dic,cancel,progress);
		if(ckeckFile)
		{
			if(!dicLoader.checkFile(file))
			{
				return;
			}
		}
		dicLoader.getDictionary().loadAllTerms();
		dicLoader.loadTermsFromFile(file,properties);
		dicLoader.getDictionary().deleteTerms();
		numberTerms = numberTerms+dicLoader.getNumberTerms();
		numberSyn = numberSyn+dicLoader.getNumberSyn();
	}

	private void bioPathways(File file,boolean ckeckFile) {
		dicLoader = new BioCycPathwaysExtension(dic,cancel,progress);
		if(ckeckFile)
		{
			if(!dicLoader.checkFile(file))
			{
				return;
			}
		}
		dicLoader.getDictionary().loadAllTerms();
		dicLoader.loadTermsFromFile(file,properties);
		dicLoader.getDictionary().deleteTerms();
		numberTerms = numberTerms+dicLoader.getNumberTerms();
		numberSyn = numberSyn+dicLoader.getNumberSyn();
	}

	private void bioGene(File file,boolean ckeckFile) {
		dicLoader = new BioCycGeneExtension(dic,cancel,progress);
		if(ckeckFile)
		{
			if(!dicLoader.checkFile(file))
			{
				return;
			}
		}
		dicLoader.getDictionary().loadAllTerms();
		dicLoader.loadTermsFromFile(file,properties);
		dicLoader.getDictionary().deleteTerms();
		numberTerms = numberTerms+dicLoader.getNumberTerms();
		numberSyn = numberSyn+dicLoader.getNumberSyn();
	}

	private void bioEnzyme(File file,boolean ckeckFile) {
		dicLoader = new BioCycEnzymeExtension(dic,cancel,progress);
		if(ckeckFile)
		{
			if(!dicLoader.checkFile(file))
			{
				return;
			}
		}
		dicLoader.getDictionary().loadAllTerms();
		dicLoader.loadTermsFromFile(file,properties);
		dicLoader.getDictionary().deleteTerms();
		numberTerms = numberTerms+dicLoader.getNumberTerms();
		numberSyn = numberSyn+dicLoader.getNumberSyn();
	}

	private void bioCompound(File file,boolean ckeckFile) {
		dicLoader = new BioCycCoumpoundExtension(dic,cancel,progress);
		if(ckeckFile)
		{
			if(!dicLoader.checkFile(file))
			{
				return;
			}
		}
		dicLoader.getDictionary().loadAllTerms();
		dicLoader.loadTermsFromFile(file,properties);
		dicLoader.getDictionary().deleteTerms();
		numberTerms = numberTerms+dicLoader.getNumberTerms();
		numberSyn = numberSyn+dicLoader.getNumberSyn();
	}

	private void keggGene(File file,boolean ckeckFile) {
		dicLoader = new KeggGenesExtension(dic,cancel,progress);
		if(ckeckFile)
		{
			if(!dicLoader.checkFile(file))
			{
				return;
			}
		}
		dicLoader.getDictionary().loadAllTerms();
		dicLoader.loadTermsFromFile(file,properties);
		dicLoader.getDictionary().deleteTerms();
		numberTerms = numberTerms+dicLoader.getNumberTerms();
		numberSyn = numberSyn+dicLoader.getNumberSyn();
	}

	private void keggReaction(File file,boolean ckeckFile) {
		dicLoader = new KeggReactionsExtension(dic,cancel,progress);
		if(ckeckFile)
		{
			if(!dicLoader.checkFile(file))
			{
				return;
			}
		}
		dicLoader.getDictionary().loadAllTerms();
		dicLoader.loadTermsFromFile(file,properties);
		dicLoader.getDictionary().deleteTerms();
		numberTerms = numberTerms+dicLoader.getNumberTerms();
		numberSyn = numberSyn+dicLoader.getNumberSyn();
	}

	private void keggCompound(File file,boolean ckeckFile) {
		dicLoader = new KeggCompoundExtension(dic,cancel,progress);
		if(ckeckFile)
		{
			if(!dicLoader.checkFile(file))
			{
				return;
			}
		}
		dicLoader.getDictionary().loadAllTerms();
		dicLoader.loadTermsFromFile(file,properties);
		dicLoader.getDictionary().deleteTerms();
		numberTerms = numberTerms+dicLoader.getNumberTerms();
		numberSyn = numberSyn+dicLoader.getNumberSyn();
	}

	private void keggEnzymes(File file,boolean ckeckFile) {
		dicLoader = new KeggEnzymesExtension(dic,cancel,progress);
		if(ckeckFile)
		{
			if(!dicLoader.checkFile(file))
			{
				return;
			}
		}
		dicLoader.getDictionary().loadAllTerms();
		dicLoader.loadTermsFromFile(file,properties);
		dicLoader.getDictionary().deleteTerms();
		numberTerms = numberTerms+dicLoader.getNumberTerms();
		numberSyn = numberSyn+dicLoader.getNumberSyn();
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
