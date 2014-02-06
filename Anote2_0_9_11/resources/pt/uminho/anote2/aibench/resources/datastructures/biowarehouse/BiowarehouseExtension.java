package pt.uminho.anote2.aibench.resources.datastructures.biowarehouse;

import java.sql.SQLException;
import java.util.Set;

import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.Biowarehouse.BiowarehouseLoader;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class BiowarehouseExtension extends BiowarehouseLoader{
	
	private boolean cancel;
	private TimeLeftProgress progress;	
	
	public BiowarehouseExtension(IDictionary dictionary,IDatabase source,boolean cancel,TimeLeftProgress progress) {
		super(dictionary,source);
		this.cancel=cancel;
		this.progress=progress;	
	}
	
	public IResourceUpdateReport loadTermsFromBiowareHouse(Set<String> classForLoading,boolean synonyms) throws DatabaseLoadDriverException, SQLException {
	
		loadClasses(classForLoading);
		getDictionary().loadAllTermsFromDatabaseToMemory();
		setSourceID(getDictionary().addSource("BiowareHouse"));
		progress.setTimeString("Loading Terms (metabolic gene)");
		if(classForLoading.contains("metabolic gene")&&!cancel)
			metabolicInformation(metabolic_gene_query, ClassProperties.getClassClassID().get(GlobalNames.metabolicGenes));
		progress.setTimeString("Loading Terms (enzymes)");
		progress.setProgress((float) 1 / (float) 9);
		if(classForLoading.contains(GlobalNames.enzymes)&&!cancel)
			metabolicInformation(enzymes_query, ClassProperties.getClassClassID().get(GlobalNames.enzymes));
		progress.setTimeString("Loading Terms (gene)");
		progress.setProgress((float) 2 / (float) 9);
		if(classForLoading.contains(GlobalNames.gene)&&!cancel)
			selectInsert(gene_query,GlobalNames.gene);
		progress.setTimeString("Loading Terms (protein)");
		progress.setProgress((float) 3 / (float) 9);
		if(classForLoading.contains(GlobalNames.protein)&&!cancel)
			selectInsert(protein_query,GlobalNames.protein);
		progress.setTimeString("Loading Terms (pathway)");
		progress.setProgress((float) 4 / (float) 9);
		if(classForLoading.contains(GlobalNames.pathways)&&!cancel)
			selectInsert(pathways_query,GlobalNames.pathways);
		progress.setTimeString("Loading Terms (organism)");
		progress.setProgress((float) 5 / (float) 9);
		if(classForLoading.contains(GlobalNames.organism)&&!cancel)
			selectInsert(organism_query,GlobalNames.organism);
		progress.setTimeString("Loading Terms (compound)");
		progress.setProgress((float) 6 / (float) 9);
		if(classForLoading.contains(GlobalNames.compounds)&&!cancel)
			selectInsert(compound_query,GlobalNames.compounds);	
		progress.setTimeString("Loading Terms (reaction)");
		progress.setProgress((float) 7 / (float) 9);
		//There are no reaction names.!!
		if(classForLoading.contains(GlobalNames.reactions)&&!cancel)
			selectInsert(reaction_query,GlobalNames.reactions);
		progress.setTimeString("Loading Terms (Synonyms)");
		progress.setProgress((float) 8 / (float) 9);
		if(synonyms)
		{
			synonyms();	
		}
		getDictionary().deleteTermsInMemory();
		progress.setProgress((float) 1);
		return getReport();
	}

}
