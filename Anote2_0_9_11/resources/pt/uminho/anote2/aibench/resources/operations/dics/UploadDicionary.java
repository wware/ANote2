package pt.uminho.anote2.aibench.resources.operations.dics;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import pt.uminho.anote2.aibench.resources.datastructures.ChebiExtension;
import pt.uminho.anote2.aibench.resources.datastructures.NcbiTaxonomyExtension;
import pt.uminho.anote2.aibench.resources.datastructures.UniprotExtension;
import pt.uminho.anote2.aibench.resources.datastructures.biocyc.BioCycCoumpoundExtension;
import pt.uminho.anote2.aibench.resources.datastructures.biocyc.BioCycEnzymeExtension;
import pt.uminho.anote2.aibench.resources.datastructures.biocyc.BioCycGeneExtension;
import pt.uminho.anote2.aibench.resources.datastructures.biocyc.BioCycPathwaysExtension;
import pt.uminho.anote2.aibench.resources.datastructures.biocyc.BioCycProteinExtension;
import pt.uminho.anote2.aibench.resources.datastructures.biocyc.BioCycReactionExtension;
import pt.uminho.anote2.aibench.resources.datastructures.brenda.BrendaExtension;
import pt.uminho.anote2.aibench.resources.datastructures.entrezgene.EntrezGeneExtension;
import pt.uminho.anote2.aibench.resources.datastructures.kegg.KeggCompoundExtension;
import pt.uminho.anote2.aibench.resources.datastructures.kegg.KeggEnzymesExtension;
import pt.uminho.anote2.aibench.resources.datastructures.kegg.KeggGenesExtension;
import pt.uminho.anote2.aibench.resources.datastructures.kegg.KeggReactionsExtension;
import pt.uminho.anote2.aibench.resources.datatypes.DictionaryAibench;
import pt.uminho.anote2.aibench.resources.gui.report.UpdateReportGUI;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import pt.uminho.anote2.resource.dictionary.IDicionaryFlatFilesLoader;
import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation(enabled=false)
public class UploadDicionary {
	
	private DictionaryAibench dic;
	private String source;
	private String entity;
	private Properties properties;
	private boolean cancel=false;
	private TimeLeftProgress progress = new TimeLeftProgress("Update Dictionary");
	private IDicionaryFlatFilesLoader dicLoader;
	
	@Progress
	public TimeLeftProgress getProgress(){
		return progress;
	}

	@Cancel(cancelInBackground=true)
	public void cancel(){
		Workbench.getInstance().warn("Dictionary Update Cancel!");	
		cancelMethod();
		dic.notifyViewObservers();
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
		System.gc();
		try {
		File file = new File(filepath);	
		if(source.equals("Uniprot"))
		{
			dicLoader = new UniprotExtension(dic,cancel,progress);

			dicLoader.getDictionary().loadAllTermsFromDatabaseToMemory();
			dicLoader.loadTermsFromFile(file,properties);
			dicLoader.getDictionary().deleteTermsInMemory();
		}
		else if(source.equals("EntrezGene"))
		{
			dicLoader = new EntrezGeneExtension(dic,cancel,progress);
			dicLoader.getDictionary().loadAllTermsFromDatabaseToMemory();
			dicLoader.loadTermsFromFile(file,properties);
			dicLoader.getDictionary().deleteTermsInMemory();
		}
		else if(source.equals("NCBI Taxonomy"))
		{
			dicLoader = new NcbiTaxonomyExtension(dic,cancel,progress);
			dicLoader.getDictionary().loadAllTermsFromDatabaseToMemory();
			dicLoader.loadTermsFromFile(file,properties);
			dicLoader.getDictionary().deleteTermsInMemory();
		}
		else if(source.equals("Chebi"))
		{
			dicLoader = new ChebiExtension(dic,cancel,progress);
			dicLoader.getDictionary().loadAllTermsFromDatabaseToMemory();
			dicLoader.loadTermsFromFile(file,properties);
			dicLoader.getDictionary().deleteTermsInMemory();
		}
		else if(source.equals("Brenda"))
		{
			dicLoader = new BrendaExtension(dic,cancel,progress);
			dicLoader.getDictionary().loadAllTermsFromDatabaseToMemory();
			dicLoader.loadTermsFromFile(file,properties);
			dicLoader.getDictionary().deleteTermsInMemory();

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
				List<IResourceUpdateReport> reports = new ArrayList<IResourceUpdateReport>();
				reports = getAllBioCyc(file);
				long totalTime = 0;
				for(IResourceUpdateReport report:reports)
				{	
					dicLoader.getReport().addClassesAdding(1);
					dicLoader.getReport().addTermAdding(report.getTermsAdding());	
					dicLoader.getReport().addSynonymsAdding(report.getSynonymsAdding());
					dicLoader.getReport().addExternalIDs(report.getExternalIDs());
					dicLoader.getReport().addAllConflit(report.getConflicts());
					totalTime += report.getTime();
				}
				dicLoader.getReport().setTime(totalTime);
			}

		}
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Dictionary Update Fail .");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		} catch (SQLException e) {
			new ShowMessagePopup("Dictionary Update Fail .");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		}	catch (IOException e) {
			new ShowMessagePopup("Dictionary Update Fail .");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		}catch (Exception e) {
			new ShowMessagePopup("Dictionary Update Fail .");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		}
		IResourceUpdateReport report = dicLoader.getReport();
		if(report.isFinishing())
		{
			dic.notifyViewObservers();
			new UpdateReportGUI(report);
			new ShowMessagePopup("Dictionary Update Complete .");
		}
		else
		{
			new ShowMessagePopup("Dictionary Update Cancel .");
		}
	}

	private List<IResourceUpdateReport>  getAllBioCyc(
			File file) throws DatabaseLoadDriverException, SQLException,
			IOException, Exception {
		List<IResourceUpdateReport>  reports = new ArrayList<IResourceUpdateReport>();;
		for(File file_folder:file.listFiles())
		{
			if(file_folder.isFile())
			{
				if(bioCompound(file_folder,true))
				{
					IResourceUpdateReport report1 = dicLoader.getReport().clone();
					reports.add(report1);
				}
				if(bioEnzyme(file_folder,true))
				{
					IResourceUpdateReport report2 = dicLoader.getReport().clone();
					reports.add(report2);
				}
				if(bioGene(file_folder,true))
				{
					IResourceUpdateReport report3 = dicLoader.getReport().clone();
					reports.add(report3);
				}
				if(bioPathways(file_folder,true))
				{
					IResourceUpdateReport report4 = dicLoader.getReport().clone();
					reports.add(report4);
				}
				if(bioProtein(file_folder,true))
				{
					IResourceUpdateReport report5 = dicLoader.getReport().clone();
					reports.add(report5);
				}
				if(bioReaction(file_folder,true))
				{
					IResourceUpdateReport report6 = dicLoader.getReport().clone();
					reports.add(report6);
				}
			}
		}
		return reports;
	}

	private boolean bioReaction(File file,boolean ckeckFile) throws DatabaseLoadDriverException, SQLException, IOException,Exception {
		dicLoader = new BioCycReactionExtension(dic,cancel,progress);
		if(ckeckFile)
		{
			if(!dicLoader.checkFile(file))
			{
				return false;
			}
		}
		dicLoader.getDictionary().loadAllTermsFromDatabaseToMemory();
		dicLoader.loadTermsFromFile(file,properties);
		dicLoader.getDictionary().deleteTermsInMemory();
		return true;
	}

	private boolean bioProtein(File file,boolean ckeckFile) throws DatabaseLoadDriverException, SQLException, IOException,Exception {
		dicLoader = new BioCycProteinExtension(dic,cancel,progress);
		if(ckeckFile)
		{
			if(!dicLoader.checkFile(file))
			{
				return false;
			}
		}
		dicLoader.getDictionary().loadAllTermsFromDatabaseToMemory();
		dicLoader.loadTermsFromFile(file,properties);
		dicLoader.getDictionary().deleteTermsInMemory();
		return true;
	}

	private boolean bioPathways(File file,boolean ckeckFile) throws DatabaseLoadDriverException, SQLException, IOException,Exception {
		dicLoader = new BioCycPathwaysExtension(dic,cancel,progress);
		if(ckeckFile)
		{
			if(!dicLoader.checkFile(file))
			{
				return false;
			}
		}
		dicLoader.getDictionary().loadAllTermsFromDatabaseToMemory();
		dicLoader.loadTermsFromFile(file,properties);
		dicLoader.getDictionary().deleteTermsInMemory();
		return true;
	}

	private boolean bioGene(File file,boolean ckeckFile) throws DatabaseLoadDriverException, SQLException, IOException,Exception {
		dicLoader = new BioCycGeneExtension(dic,cancel,progress);
		if(ckeckFile)
		{
			if(!dicLoader.checkFile(file))
			{
				return false;
			}
		}
		dicLoader.getDictionary().loadAllTermsFromDatabaseToMemory();
		dicLoader.loadTermsFromFile(file,properties);
		dicLoader.getDictionary().deleteTermsInMemory();
		return true;
	}

	private boolean bioEnzyme(File file,boolean ckeckFile) throws DatabaseLoadDriverException, SQLException, IOException,Exception {
		dicLoader = new BioCycEnzymeExtension(dic,cancel,progress);
		if(ckeckFile)
		{
			if(!dicLoader.checkFile(file))
			{
				return false;
			}
		}
		dicLoader.getDictionary().loadAllTermsFromDatabaseToMemory();
		dicLoader.loadTermsFromFile(file,properties);
		dicLoader.getDictionary().deleteTermsInMemory();
		return true;
	}

	private boolean bioCompound(File file,boolean ckeckFile) throws DatabaseLoadDriverException, SQLException, IOException,Exception {
		dicLoader = new BioCycCoumpoundExtension(dic,cancel,progress);
		if(ckeckFile)
		{
			if(!dicLoader.checkFile(file))
			{
				return false;
			}
		}
		dicLoader.getDictionary().loadAllTermsFromDatabaseToMemory();
		dicLoader.loadTermsFromFile(file,properties);
		dicLoader.getDictionary().deleteTermsInMemory();
		return true;
	}

	private void keggGene(File file,boolean ckeckFile) throws  DatabaseLoadDriverException, SQLException, IOException,Exception {
		dicLoader = new KeggGenesExtension(dic,cancel,progress);
		if(ckeckFile)
		{
			if(!dicLoader.checkFile(file))
			{
				return;
			}
		}
		dicLoader.getDictionary().loadAllTermsFromDatabaseToMemory();
		dicLoader.loadTermsFromFile(file,properties);
		dicLoader.getDictionary().deleteTermsInMemory();
	}

	private void keggReaction(File file,boolean ckeckFile) throws DatabaseLoadDriverException, SQLException, IOException,Exception {
		dicLoader = new KeggReactionsExtension(dic,cancel,progress);
		if(ckeckFile)
		{
			if(!dicLoader.checkFile(file))
			{
				return;
			}
		}
		dicLoader.getDictionary().loadAllTermsFromDatabaseToMemory();
		dicLoader.loadTermsFromFile(file,properties);
		dicLoader.getDictionary().deleteTermsInMemory();

	}

	private void keggCompound(File file,boolean ckeckFile) throws DatabaseLoadDriverException, SQLException, IOException,Exception {
		dicLoader = new KeggCompoundExtension(dic,cancel,progress);
		if(ckeckFile)
		{
			if(!dicLoader.checkFile(file))
			{
				return;
			}
		}
		dicLoader.getDictionary().loadAllTermsFromDatabaseToMemory();
		dicLoader.loadTermsFromFile(file,properties);
		dicLoader.getDictionary().deleteTermsInMemory();
	}

	private void keggEnzymes(File file,boolean ckeckFile) throws DatabaseLoadDriverException, SQLException, IOException,Exception {
		dicLoader = new KeggEnzymesExtension(dic,cancel,progress);
		if(ckeckFile)
		{
			if(!dicLoader.checkFile(file))
			{
				return;
			}
		}
		dicLoader.getDictionary().loadAllTermsFromDatabaseToMemory();
		dicLoader.loadTermsFromFile(file,properties);
		dicLoader.getDictionary().deleteTermsInMemory();
	}
		

}
