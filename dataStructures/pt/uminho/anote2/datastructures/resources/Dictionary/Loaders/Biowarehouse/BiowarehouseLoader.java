package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.Biowarehouse;

/**
 * @author Analia Lourenço
 * @author Hugo Costa
 * 
**/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.MySQLDatabase;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.resources.ResourcesHelp;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.dictionary.IBiowareHouseLoader;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class BiowarehouseLoader extends Loader implements IBiowareHouseLoader{
	
	private int numberSyn;
	private int numberTerms;
	private boolean cancel;
	private int sourceID;
	
	public BiowarehouseLoader(IDictionary dictionary,IDatabase source)
	{
		super(dictionary,source);
		numberSyn = 0;
		numberTerms = 0;
		cancel = false;
	}
	
	protected void loadClasses(Set<String> classLoader) {
		for(String classL:classLoader)
		{
			getDictionary().addResourceContent(classL);
		}		
		try {
			setClassID_Class(ResourcesHelp.getClassIDClassOnDatabase(getDictionary().getDb()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for(Integer classID:getClassID_Class().keySet())
		{
			getTerm_idclasses().put(getClassID_Class().get(classID), classID);
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
	
	
	public void loadTermsFromBiowareHouse(Set<String> classForLoading,boolean synonyms) {
	
		loadClasses(classForLoading);
		getDictionary().loadAllTerms();
		sourceID = getDictionary().addSource("BiowareHouse");
		if(classForLoading.contains("metabolic gene")&&!cancel)
			metabolicInformation(metabolic_gene_query, getTerm_idclasses().get("metabolic gene"));
		if(classForLoading.contains("enzyme")&&!cancel)
			metabolicInformation(enzymes_query,getTerm_idclasses().get("enzyme"));
		if(classForLoading.contains("gene")&&!cancel)
			selectInsert(gene_query,"gene");
		if(classForLoading.contains("protein")&&!cancel)
			selectInsert(protein_query,"protein");
		if(classForLoading.contains("pathway")&&!cancel)
			selectInsert(pathways_query,"pathway");
		if(classForLoading.contains("organism")&&!cancel)
			selectInsert(organism_query,"organism");
		if(classForLoading.contains("compound")&&!cancel)
		selectInsert(compound_query,"compound");	
		//There are no reaction names!!!!!
		if(classForLoading.contains("reaction")&&!cancel)
		selectInsert(reaction_query,"reaction");
		if(synonyms)
			synonyms();	
		getDictionary().deleteTerms();
	}
	
	
	protected void metabolicInformation(String query, Integer idclass){
		
		try {
			Statement stmt = (Statement) getSource().getConnection().createStatement();	
			ResultSet res=stmt.executeQuery(query);
			while (res.next()) {
				String term=res.getString(2);
				int WID=res.getInt(1);
				IResourceElement elem = new ResourceElement(-1,term,idclass,"");
				if(getDictionary().addElement(elem))
				{
					int elementID = HelpDatabase.getNextInsertTableID(getDictionary().getDb(), "resource_elements")-1;
					getDictionary().addExternalID(elementID, String.valueOf(WID), sourceID);
					numberTerms++;
				}
			}
			res.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void selectInsert(String query,String term_class){
		int idclass=getTerm_idclasses().get(term_class).intValue();
			
		try {
		
			Statement stmt = (Statement) getSource().getConnection().createStatement();		
			ResultSet res=stmt.executeQuery(query);
			while (res.next()) {
				String term=res.getString(2);
				IResourceElement elem = new ResourceElement(-1,term,idclass,"");
				if(getDictionary().addElement(elem))
				{
					int elementID = HelpDatabase.getNextInsertTableID(getDictionary().getDb(), "resource_elements")-1;
					getDictionary().addExternalID(elementID, res.getString(1), sourceID);
					//Check whether the systematic name or the ECnumber exist
					if(term_class.equals("compound") || term_class.equals("reaction")){ 
						String synonym=res.getString(3);
						if(getDictionary().addSynomns(new ResourceElement(elementID,synonym,idclass,term_class)))
						{
							numberSyn++;
						}
					}
					numberTerms++;
				}
			}	
			res.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}			
	}	

	protected void synonyms() 
	{
		Map<String,Integer> externalID_IDTerm;
		PreparedStatement wid_synonyms=null;
		//Gets SYNONYMs attached to particular WID		
		try {
			wid_synonyms = (PreparedStatement) getSource().getConnection().prepareStatement("SELECT OtherWID,Syn "+ 
																			"FROM SynonymTable ");												
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		try {
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
					if(getDictionary().addSynomns(elem))
					{
						numberSyn++;
					}
				}
				else
				{
					
				}
			}
			syns.close();
			wid_synonyms.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
	private Map<String, Integer> getExternalIDTErmID() throws SQLException {
		Connection con = getDictionary().getDb().getConnection();
		if(con==null)
		{
			return null;
		}
		Map<String, Integer> externalIDtermID = new HashMap<String, Integer>();
		
		int sourceID = getSourceBiowarehouseID();
		
		String query = "SELECT resource_elements_idresource_elements,external_id FROM resource_elements_extenal_id "
					   + "WHERE resource_elements_resources_idresources=? AND sources_idsources=? ";
		
		PreparedStatement queryPS = con.prepareStatement(query);
		queryPS.setInt(1,getDictionary().getId());
		queryPS.setInt(2, sourceID);
		ResultSet rs = queryPS.executeQuery();
		while(rs.next())
		{
			externalIDtermID.put(rs.getString(2), rs.getInt(1));
		}
		return externalIDtermID;
	}


	private int getSourceBiowarehouseID() {
		Connection con = getDictionary().getDb().getConnection();
		if(con==null)
		{
			return -1;
		}
		String getSourceID = "SELECT idsources FROM sources "+
							 "WHERE source_name='BiowareHouse' ";
		ResultSet rs;
		try {
			rs = con.prepareStatement(getSourceID).executeQuery();
			if(rs.next())
			{
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public IDatabase getBiowareHouseDB() {
		return getSource();
	}

	public int getNumberSyn() {
		return numberSyn;
	}

	public int getNumberTerms() {
		return numberTerms;
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
	
	
}
