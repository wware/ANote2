package pt.uminho.anote2.aibench.publicationmanager.dataStructures;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.utils.exceptions.NonExistingConnection;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesDocument;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;

/**
 * Class that save a Relevance Data from one publication in a specific Query
 * 
 * 
 * @author Rafael Carreira
 *
 */
public class RelevanceData implements Serializable{

	private static final long serialVersionUID = 8115717205178066788L;
	
	/**
	 * Publication
	 */
	private IPublication pubResume;
	
	/**
	 * QueryID in Database
	 */
	private int idQuery;
	
	private String keywords;
	
	/**
	 * Relevance
	 */
	private String relevance;
	
	private String previousRelevance;
	
	public RelevanceData(IPublication pubResume, int idQuery, String keywords) throws NonExistingConnection{
		this.pubResume = pubResume;
		this.idQuery = idQuery;
		this.keywords = keywords;		
		this.relevance = getCurrentRelevance(idQuery);
		this.previousRelevance=null;
	}
	
	
	public String getPreviousRelevance() {
		return previousRelevance;
	}


	public void setPreviousRelevance(String previousRelevance) {
		this.previousRelevance = previousRelevance;
	}


	public int getIdQuery() {
		return idQuery;
	}

	public void setIdQuery(int idQuery) {
		this.idQuery = idQuery;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	public String getRelevance() {
		return relevance;
	}


	public void setRelevance(String relevance) {
		this.relevance = relevance;
	}
	
	@Override
	public String toString(){
		StringBuffer str = new StringBuffer();
		str.append(idQuery + " - ");
		str.append(keywords);
		return str.toString();
	}
	
	/**
	 * Method that find a Relevance for publication in spefic Query
	 * 
	 * @param idQuery
	 * @return
	 * @throws NonExistingConnection
	 */
	private String getCurrentRelevance(int idQuery) throws NonExistingConnection{
		
		List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(PublicationManager.class);
		
		PublicationManager ref = null;
		for(ClipboardItem session: cl)
		{
			if(session.getUserData().getClass().isAssignableFrom(PublicationManager.class))
			{
				ref = (PublicationManager) session.getUserData();
				break;
			}
		}
		if(ref  == null)
			throw new NonExistingConnection("There is no connection on Clipboard");
		
	
		try {
			Connection connection = ref.getDb().getConnection();
			PreparedStatement statement;
				statement = connection.prepareStatement(QueriesDocument.selectRelevancePublication);
				statement.setInt(1,pubResume.getID());
				statement.setInt(2,idQuery);
				ResultSet rs = statement.executeQuery();
	
			if(rs.next())
				return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;	
	}
	
}
