package pt.uminho.anote2.aibench.publicationmanager.datatypes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;
import pt.uminho.anote2.process.ir.pubmed.QueryInfornationRetrieval;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
/**
 * Aibench Datatype that represent a Query Search in IIRSearch
 * 
 * @author Hugo Costa
 *
 */
@Datatype(structure = Structure.SIMPLE,namingMethod="getName",viewable=true,removable=true)
public class QueryInformationRetrievalExtension extends QueryInfornationRetrieval{


	private static final long serialVersionUID = 1L;
	private PublicationManager pubManager;
		
	public QueryInformationRetrievalExtension(
			int idqueries, Date date,
			String keywords,
			String organism,		
			int matching_publications,
			int available_abstracts,
			int downloaded_publications,
			Properties properties,
			PublicationManager pubManager) {
		super(idqueries, date, keywords,organism,
				matching_publications, available_abstracts,downloaded_publications,properties);
		this.pubManager=pubManager;
		
	}
	
	/**
	 * Method that search in database for all pmids present in this query
	 * 
	 * @return List  of Pmids
	 */
	public Set<String> getPmids()
	{
		Set<String> pmids = new HashSet<String>();
		Connection connection = this.pubManager.getDb().getConnection();
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(QueriesPublication.selectIDAndPmidsForQuery);
			statement.setInt(1,getID());
			ResultSet rs = statement.executeQuery();
			while(rs.next())
			{
				String pmid = rs.getString(2);
				pmids.add(pmid);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pmids;
	}
	
	
	public PublicationManager getPubManager() {
		return pubManager;
	}


	public void setPubManager(PublicationManager pubManager) {
		this.pubManager = pubManager;
	}


	public String getName()
	{
		return "Query :"+getID();
	}
	
}
