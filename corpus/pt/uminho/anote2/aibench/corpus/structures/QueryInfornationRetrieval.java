package pt.uminho.anote2.aibench.corpus.structures;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Observable;
import java.util.Properties;
import java.util.Set;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;



/**
 * That class saves information about parameters of one Query to IIRSerach process
 * 
 * @author Hugo Costa
 * 
 * @version 1.1 (15 Setembro 2009)
 *
 */

public class QueryInfornationRetrieval extends Observable implements Serializable {

	private static final long serialVersionUID = 6783755838883528975L;
	
	/**
	 * QueryID on DataBase
	 */
	private int idqueries;
	
	/**
	 * Date of query
	 */
	private Date date;
	
	/**
	 * Keywords  Query
	 */
	private String keywords;
	
	/**
	 * Organism
	 */
	private String organism;
	
	private Properties properties;
	private int matching_publications;
	private int available_abstracts;
	private int downloaded_publications;
	
	
	public QueryInfornationRetrieval(int idqueries, Date date, String keywords, 
		String organism,int matching_publications, int available_abstracts, int downloaded_publications,
		Properties properties) {
		this.idqueries = idqueries;
		this.date = date;
		this.keywords = keywords;
		this.organism=organism;
		this.matching_publications = matching_publications;
		this.available_abstracts = available_abstracts;
		this.downloaded_publications = downloaded_publications;
		this.properties=properties;
	}
	

	
	/**
	 * Method that return date of query
	 * 
	 * @return Date
	 */
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public int getDownloaded_publications() {
		return downloaded_publications;
	}
	public void setDownloaded_publications(int downloaded_publications) {
		this.downloaded_publications = downloaded_publications;
	}
	
	/**
	 * Method that return a number publication MActhing
	 * 
	 * @return number of number publication Macthing
	 */
	public int getMatching_publications() {
		return matching_publications;
	}
	public void setMatching_publications(int matching_publications) {
		this.matching_publications = matching_publications;
	}
	
	public String toString(){
		StringBuffer str = new StringBuffer();
		str.append("Keywords : "+this.keywords + "\n");
		str.append("Organism : "+this.organism + "\n");
		return str.toString();
	}
	
	/**
	 * Method that return a Query Properties
	 * 
	 * @return query properties
	 */
	public Properties getProperties() {
		return properties;
	}
	
	public int getDocumentMathing() {
		return this.matching_publications;
	}
	
	/**
	 * Method that return a queryID in database
	 * 
	 * @return queryID
	 */
	public int getID() {
		return this.idqueries;
	}
	
	/**
	 * Method that return keyword of Query
	 * 
	 * @return keywords
	 * 		   "" -- if keywords don't exist
	 */
	public String getKeyWords() {
		return this.keywords;
	}
	
	/**
	 * Method that return the organism in searching
	 * 
	 * @return organism
	 * 		   "" -- if organism don't exist
	 */
	public String getOrganism() {
		return organism;
	}
	
	/**
	 * Method that return a number of available abstracts in the Query
	 * 
	 * @return number of available abstract
	 */
	public int getAvailable_abstracts() {
		return available_abstracts;
	}
	public void setAvailable_abstracts(int available_abstracts) {
		this.available_abstracts = available_abstracts;
	}

	public void notifyViewObserver()
	{
		this.setChanged();
		notifyObservers();
	}
	
	public Set<Integer> getIds(IDatabase db)
	{
		Set<Integer> ids = new HashSet<Integer>();
		
		Connection conn = db.getConnection();

		try {
			PreparedStatement prep = conn.prepareStatement(QueriesPublication.selectIDAndPmidsForQuery);
			prep.setInt(1,getID());
			ResultSet rs = prep.executeQuery();
		
			while(rs.next())
			{
				int id = rs.getInt(1);
				ids.add(id);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		return ids;
	}
	


	
	
		
}
