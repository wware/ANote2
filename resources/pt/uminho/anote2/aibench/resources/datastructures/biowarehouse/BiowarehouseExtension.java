package pt.uminho.anote2.aibench.resources.datastructures.biowarehouse;

import java.util.Set;

import pt.uminho.anote2.aibench.utils.operations.TimeLeftProgress;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.Biowarehouse.BiowarehouseLoader;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class BiowarehouseExtension extends BiowarehouseLoader{
	
	private boolean cancel;
	private TimeLeftProgress progress;	
	
	public BiowarehouseExtension(IDictionary dictionary,IDatabase source,boolean cancel,TimeLeftProgress progress) {
		super(dictionary,source);
		this.cancel=cancel;
		this.progress=progress;	
	}
	
	public void loadTermsFromBiowareHouse(Set<String> classForLoading,boolean synonyms) {
	
		loadClasses(classForLoading);
		getDictionary().loadAllTerms();
		setSourceID(getDictionary().addSource("BiowareHouse"));
		progress.setTimeString("Loading Terms (metabolic gene)");
		if(classForLoading.contains("metabolic gene")&&!cancel)
		{
			metabolicInformation(metabolic_gene_query, getTerm_idclasses().get("metabolic gene"));
		}
		progress.setTimeString("Loading Terms (enzymes)");
		progress.setProgress((float) 1 / (float) 9);
		if(classForLoading.contains("enzyme")&&!cancel)
		{
			metabolicInformation(enzymes_query,getTerm_idclasses().get("enzyme"));
		}
		progress.setTimeString("Loading Terms (gene)");
		progress.setProgress((float) 2 / (float) 9);
		if(classForLoading.contains("gene")&&!cancel)
		{
			selectInsert(gene_query,"gene");
		}
		progress.setTimeString("Loading Terms (protein)");
		progress.setProgress((float) 3 / (float) 9);
		if(classForLoading.contains("protein")&&!cancel)
		{
			selectInsert(protein_query,"protein");
		}
		progress.setTimeString("Loading Terms (pathway)");
		progress.setProgress((float) 4 / (float) 9);
		if(classForLoading.contains("pathway")&&!cancel)
		{
			selectInsert(pathways_query,"pathway");
		}
		progress.setTimeString("Loading Terms (organism)");
		progress.setProgress((float) 5 / (float) 9);
		if(classForLoading.contains("organism")&&!cancel)
		{
			selectInsert(organism_query,"organism");
		}
		progress.setTimeString("Loading Terms (compound)");
		progress.setProgress((float) 6 / (float) 9);
		if(classForLoading.contains("compound")&&!cancel)
		{
			selectInsert(compound_query,"compound");
		}
		progress.setTimeString("Loading Terms (reaction)");
		progress.setProgress((float) 7 / (float) 9);
		//There are no reaction names!!!!!
		if(classForLoading.contains("reaction")&&!cancel)
		{
			selectInsert(reaction_query,"reaction");
		}
		progress.setTimeString("Loading Terms (Synonyms)");
		progress.setProgress((float) 8 / (float) 9);
		if(synonyms)
		{
			synonyms();	
		}
		getDictionary().deleteTerms();
		progress.setProgress((float) 1);
	}

}
