package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.Biowarehouse;

/**
 * @author Hugo Costa
 * 
**/

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.ConflitsType;
import pt.uminho.anote2.core.report.resources.IInsertConflits;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import pt.uminho.anote2.datastructures.annotation.ExternalID;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.report.resources.ResourceMergeConflits;
import pt.uminho.anote2.datastructures.report.resources.ResourceUpdateReport;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.dictionary.IBiowareHouseLoader;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class BiowarehouseLoader extends Loader implements IBiowareHouseLoader{
	

	private boolean cancel;
	private int sourceID;
	private IResourceUpdateReport report;
	
	public BiowarehouseLoader(IDictionary dictionary,IDatabase source)
	{
		super(dictionary,source);
		report = new ResourceUpdateReport(GlobalNames.updateDictionariesReportTitle, dictionary, new File(""), "Bioware House Database");
		cancel = false;
	}
	
	protected void loadClasses(Set<String> classLoader) throws DatabaseLoadDriverException, SQLException {
		for(String classL:classLoader)
		{
			getDictionary().addResourceContent(classL);
		}		
	}
	
	
	protected static String  metabolic_gene_query = "SELECT GeneWID,Name " +
												   "FROM GeneWIDProteinWID JOIN Gene ON GeneWID=Gene.WID " +
												   "WHERE ProteinWID IN " +
												   "(SELECT ProteinWID " +
												   "FROM EnzymaticReaction) AND Name IS NOT NULL ";
	
	protected static String enzymes_query = "SELECT DISTINCT Protein.WID,Name " +
										  "FROM EnzymaticReaction JOIN Protein " +
										  "ON ProteinWID=Protein.WID " +
										  "WHERE Name IS NOT NULL ";
	
	protected static String gene_query = "SELECT WID,Name " +
									   "FROM Gene " +
									   "WHERE Name IS NOT NULL ";

	protected static String protein_query = "SELECT WID,Name " +
										  "FROM Protein " +
										  "WHERE Name IS NOT NULL";

	protected static String pathways_query = "SELECT WID,Name " +
										   "FROM Pathway " +
										   "WHERE Name IS NOT NULL ";
	
	protected static String organism_query = "SELECT WID,Name " +
										   "FROM Taxon " +
										   "WHERE Name IS NOT NULL ";
	
	protected static String compound_query = "SELECT WID,Name,SystematicName " +
										   "FROM Chemical " +
										   "WHERE Name IS NOT NULL";
	
	protected static String reaction_query = "SELECT WID,Name,ECNumber " +
										   "FROM Reaction " +
										   "WHERE Name IS NOT NULL" ;
	
	
	public IResourceUpdateReport loadTermsFromBiowareHouse(Set<String> classForLoading,boolean synonyms) throws SQLException, DatabaseLoadDriverException {
	
		loadClasses(classForLoading);
		getDictionary().loadAllTermsFromDatabaseToMemory();
		sourceID = getDictionary().addSource(GlobalNames.metabolicGenes);
		if(classForLoading.contains("metabolic gene")&&!cancel)
			metabolicInformation(metabolic_gene_query, ClassProperties.getClassClassID().get(GlobalNames.metabolicGenes));
		if(classForLoading.contains(GlobalNames.enzymes)&&!cancel)
			metabolicInformation(enzymes_query, ClassProperties.getClassClassID().get(GlobalNames.enzymes));
		if(classForLoading.contains(GlobalNames.gene)&&!cancel)
			selectInsert(gene_query,GlobalNames.gene);
		if(classForLoading.contains(GlobalNames.protein)&&!cancel)
			selectInsert(protein_query,GlobalNames.protein);
		if(classForLoading.contains(GlobalNames.pathways)&&!cancel)
			selectInsert(pathways_query,GlobalNames.pathways);
		if(classForLoading.contains(GlobalNames.organism)&&!cancel)
			selectInsert(organism_query,GlobalNames.organism);
		if(classForLoading.contains(GlobalNames.compounds)&&!cancel)
			selectInsert(compound_query,GlobalNames.compounds);	
		//There are no reaction names.!!
		if(classForLoading.contains(GlobalNames.reactions)&&!cancel)
			selectInsert(reaction_query,GlobalNames.reactions);
		if(synonyms)
			synonyms();	
		getDictionary().deleteTermsInMemory();
		return getReport();
	}
	
	
	protected void metabolicInformation(String query, Integer idclass) throws SQLException, DatabaseLoadDriverException{
		Statement stmt = (Statement) getSource().getConnection().createStatement();	
		ResultSet res=stmt.executeQuery(query);
		while (res.next()) {
			String term=res.getString(2);
			int WID=res.getInt(1);
			IResourceElement elem = new ResourceElement(-1,term,idclass,"");
			if(getDictionary().addElement(elem))
			{
				report.addTermAdding(1);
				int elementID = HelpDatabase.getNextInsertTableID(GlobalTablesName.resource_elements)-1;
				if(getDictionary().addExternalID(elementID, String.valueOf(WID), sourceID))
				{
					report.addExternalIDs(1);
				}
				else
				{
					IResourceElement newElem = new ResourceElement(-1, "", 1, "", new ExternalID(String.valueOf(WID), "", sourceID));
					IInsertConflits conflit = new ResourceMergeConflits(ConflitsType.AlteradyHaveExternalID, elem,newElem );
					report.addConflit(conflit);
				}
			}
			else
			{
				IInsertConflits conflit;
				IResourceElement elemValue = getDictionary().getFirstTermByName(elem.getTerm());
				if(elem.getTermClassID()==elemValue.getTermClassID())
				{
					conflit = new ResourceMergeConflits(ConflitsType.AlreadyHaveTerm, elemValue, elem);
				}
				else
				{
					conflit = new ResourceMergeConflits(ConflitsType.TermInDiferentClasses, elemValue, elem) ;
				}
				report.addConflit(conflit);
			}
		}
		res.close();
		stmt.close();
	}

	protected void selectInsert(String query,String term_class) throws SQLException, DatabaseLoadDriverException{
		int idclass= ClassProperties.getClassClassID().get(term_class);
		Statement stmt = (Statement) getSource().getConnection().createStatement();		
		ResultSet res=stmt.executeQuery(query);
		while (res.next()) {
			String term=res.getString(2);
			IResourceElement elem = new ResourceElement(-1,term,idclass,"");
			if(getDictionary().addElement(elem))
			{
				report.addTermAdding(1);
				int elementID = HelpDatabase.getNextInsertTableID(GlobalTablesName.resource_elements)-1;
				getDictionary().addExternalID(elementID, res.getString(1), sourceID);
				//Check whether the systematic name or the ECnumber exist
				if(term_class.equals(GlobalNames.compounds) || term_class.equals(GlobalNames.reactions)){ 
					String synonym=res.getString(3);
					IResourceElement newelem = new ResourceElement(elementID,synonym,idclass,term_class);
					if(getDictionary().addSynomyn(newelem))
					{
						report.addSynonymsAdding(1);
					}
					else
					{
						IInsertConflits conflit = new ResourceMergeConflits(ConflitsType.AlreadyHaveSynonyms, elem, newelem);
						report.addConflit(conflit);
					}
				}

			}
			else
			{
				IInsertConflits conflit;
				IResourceElement elemValue = getDictionary().getFirstTermByName(elem.getTerm());
				if(elem.getTermClassID()==elemValue.getTermClassID())
				{
					conflit = new ResourceMergeConflits(ConflitsType.AlreadyHaveTerm, elemValue, elem);
				}
				else
				{
					conflit = new ResourceMergeConflits(ConflitsType.TermInDiferentClasses, elemValue, elem) ;
				}
				report.addConflit(conflit);
			}
		}	
		res.close();
		stmt.close();		
	}	

	protected void synonyms() throws SQLException, DatabaseLoadDriverException 
	{
		Map<String,Integer> externalID_IDTerm;
		PreparedStatement wid_synonyms=null;
		//Gets SYNONYMs attached to particular WID		
		wid_synonyms = (PreparedStatement) getSource().getConnection().prepareStatement("SELECT OtherWID,Syn "+ 
				"FROM SynonymTable ");													
		externalID_IDTerm = getExternalIDTErmID();
		ResultSet syns = wid_synonyms.executeQuery();
		while(syns.next())
		{
			String externalID = syns.getString(1);
			if(externalID_IDTerm.containsKey(externalID))
			{
				int term_id = externalID_IDTerm.get(externalID);
				String synonym=syns.getString(2);

				IResourceElement elem = new ResourceElement(term_id,synonym,-1,"");
				if(getDictionary().addSynomyn(elem))
				{
					report.addSynonymsAdding(1);
				}
				else
				{
					IInsertConflits conflit = new ResourceMergeConflits(ConflitsType.AlreadyHaveSynonyms, elem, elem);
					report.addConflit(conflit);
				}
			}
		}
		syns.close();
		wid_synonyms.close();
	}	
	
	private Map<String, Integer> getExternalIDTErmID() throws SQLException, DatabaseLoadDriverException {
		Map<String, Integer> externalIDtermID = new HashMap<String, Integer>();	
		int sourceID = getSourceBiowarehouseID();
		
		String query = "SELECT resource_elements_idresource_elements,external_id FROM resource_elements_extenal_id "
					   + "WHERE resource_elements_resources_idresources=? AND sources_idsources=? ";
		
		PreparedStatement queryPS = Configuration.getDatabase().getConnection().prepareStatement(query);
		queryPS.setInt(1,getDictionary().getID());
		queryPS.setInt(2, sourceID);
		ResultSet rs = queryPS.executeQuery();
		while(rs.next())
		{
			externalIDtermID.put(rs.getString(2), rs.getInt(1));
		}
		return externalIDtermID;
	}


	private int getSourceBiowarehouseID() throws SQLException, DatabaseLoadDriverException {
		String getSourceID = "SELECT idsources FROM sources "+
				"WHERE source_name='BiowareHouse' ";
		ResultSet rs = Configuration.getDatabase().getConnection().prepareStatement(getSourceID).executeQuery();
		if(rs.next())
		{
			return rs.getInt(1);
		}
		return 0;
	}
	
	public IDatabase getBiowareHouseDB() {
		return getSource();
	}


	public void setcancel(boolean newCancel) {
		cancel=newCancel;	
	}
	
	public boolean isCancel() {
		return cancel;
	}


	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}


	public int getSourceID() {
		return sourceID;
	}


	public static String getMetabolic_gene_query() {
		return metabolic_gene_query;
	}


	public static String getEnzymes_query() {
		return enzymes_query;
	}


	public static String getGene_query() {
		return gene_query;
	}


	public static String getProtein_query() {
		return protein_query;
	}


	public static String getPathways_query() {
		return pathways_query;
	}


	public static String getOrganism_query() {
		return organism_query;
	}


	public static String getCompound_query() {
		return compound_query;
	}


	public static String getReaction_query() {
		return reaction_query;
	}


	public void setSourceID(int sourceID) {
		this.sourceID = sourceID;
	}


	public static void setReaction_query(String reaction_query) {
		BiowarehouseLoader.reaction_query = reaction_query;
	}

	public IResourceUpdateReport getReport() {
		return report;
	}
	
	
}
