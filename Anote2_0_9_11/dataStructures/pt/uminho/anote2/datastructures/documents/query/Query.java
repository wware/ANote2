package pt.uminho.anote2.datastructures.documents.query;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Properties;
import java.util.Set;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.core.document.RelevanceType;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesDocument;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesIRProcess;
import pt.uminho.anote2.datastructures.documents.Publication;
import pt.uminho.anote2.datastructures.textprocessing.NormalizationForm;
import pt.uminho.anote2.datastructures.utils.HTMLCodes;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.process.IR.IIRSearch;



/**
 * That class saves information about parameters of one Query to {@link IIRSearch} process
 * 
 * @author Hugo Costa
 * 
 *
 */
public class Query extends Observable implements Serializable {

	private static final long serialVersionUID = 1L;
	
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
	
	/**
	 * Query Identification
	 */
	private String name;
	
	/**
	 * Query Properties
	 */
	private Properties properties;
	
	private int documentsMatching;
	private int available_abstracts;
	private int documentsRetrieved;
	private boolean fromPubmedSearch;

	private Map<Integer,RelevanceType> documentRelevance;	

	
	
	public Query(int idqueries,String name, Date date, String keywords, 
		String organism,int matching_publications, int available_abstracts, int downloaded_publications,
		boolean frompubmedsearch,Properties properties) {
		this.idqueries = idqueries;
		this.name = name;
		this.date = date;
		this.keywords = keywords;
		this.organism=organism;
		this.documentsMatching = matching_publications;
		this.available_abstracts = available_abstracts;
		this.documentsRetrieved = downloaded_publications;
		this.setFromPubmedSearch(frompubmedsearch);
		this.properties=properties;
		this.documentRelevance = null;
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
	
	/**
	 * Return number of publications retrieval
	 *  
	 * @return
	 */
	public int getDocumentsRetrived() {
		return documentsRetrieved;
	}
	public void setDownloaded_publications(int documentsRetrieved) {
		this.documentsRetrieved = documentsRetrieved;
	}
	
	public void setMatching_publications(int documentsMatching) {
		this.documentsMatching = documentsMatching;
	}

	public String getQueryResumeWithProperties() throws SQLException, DatabaseLoadDriverException
	{
		if(isFromPubmedSearch())
		{
			StringBuffer str = new StringBuffer();
			str.append("Query Name : "+getName() +  " \n");
			str.append("Keywords : "+this.keywords + "\n");
			str.append("Organism : "+this.organism + "\n");
			Enumeration<Object> itObj = this.properties.keys();
			while(itObj.hasMoreElements())
			{
				Object propertyNameObj = itObj.nextElement();
				String propertyName = (String) propertyNameObj ;
				Object propertyValueObj = this.properties.getProperty(propertyName);		
				str.append(propertyName+" : "+propertyValueObj.toString() + "\n");			
			}
			return str.toString();
		}
		else
		{
			StringBuffer str = new StringBuffer();
			str.append("Query Name : "+getName() +  " \n");
			if(getKeyWords()!=null && getKeyWords().length() > 0)
				str.append("Keywords : "+this.keywords + "\n");
			if(getOrganism()!=null && getOrganism().length() > 0)
				str.append("Organism : "+this.organism + "\n");
			Enumeration<Object> itObj = this.properties.keys();
			while(itObj.hasMoreElements())
			{
				Object propertyNameObj = itObj.nextElement();
				String propertyName = (String) propertyNameObj ;
				Object propertyValueObj = this.properties.getProperty(propertyName);		
				str.append(propertyName+" : "+propertyValueObj.toString() + "\n");			
			}
			return str.toString();
		}
	}
	
	public String toString(){
		StringBuffer str = new StringBuffer();
		if(this.name!=null && this.name.length() > 0)
		{
			str.append("Query Name : "+this.name + "  \n");
			return str.toString();
		}
		str.append("Query (ID) :"+ this.idqueries + "  \n");
		if(this.keywords!=null && this.keywords.length() > 0)
			str.append("Keywords : "+this.keywords + "   \n");
		if(this.organism!=null && this.organism.length() > 0)
			str.append("Organism : "+this.organism + "  \n");
		if(this.date!=null)
			str.append("Date : "+this.date.toString() + "  \n");
		return str.toString();
		
	}
	
	
	/**
	 * Method that search in database for all pmids present in this query
	 * 
	 * @return List  of Pmids
	 * @throws SQLException 
	 * @throws DatabaseLoadDriverException 
	 */
	public Set<String> getPmids() throws DatabaseLoadDriverException, SQLException
	{
		Set<String> pmids = new HashSet<String>();
		Connection connection = Configuration.getDatabase().getConnection();
		PreparedStatement statement;
		statement = connection.prepareStatement(QueriesPublication.selectIDAndOtherIDForQuery);
		statement.setInt(1,getID());
		ResultSet rs = statement.executeQuery();
		while(rs.next())
		{
			String pmid = rs.getString(2);
			pmids.add(pmid);
		}
		rs.close();
		statement.close();
		return pmids;
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
		return this.documentsMatching;
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
	
	/**
	 * Return all DatabaseID for a Query
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DatabaseLoadDriverException
	 */
	public Set<Integer> getIds() throws SQLException, DatabaseLoadDriverException
	{
		Set<Integer> ids = new HashSet<Integer>();
		PreparedStatement prep = Configuration.getDatabase().getConnection().prepareStatement(QueriesPublication.selectIDAndOtherIDForQuery);
		prep.setInt(1,getID());
		ResultSet rs = prep.executeQuery();

		while(rs.next())
		{
			int id = rs.getInt(1);
			ids.add(id);
		}
		rs.close();
		prep.close();
		return ids;
	}
	
	private Map<Integer, RelevanceType> getDocumentRelevance() throws SQLException, DatabaseLoadDriverException{
		if(documentRelevance == null)
		{
			Map<Integer, RelevanceType> documentRelevance = new HashMap<Integer, RelevanceType>();
			PreparedStatement add_pub_stat;
			add_pub_stat = Configuration.getDatabase().getConnection().prepareStatement(QueriesDocument.selectPublicationRelevanceForQuery);
			add_pub_stat.setInt(1,getID());
			ResultSet rs = add_pub_stat.executeQuery();	
			while(rs.next())
			{
				documentRelevance.put(rs.getInt(1), RelevanceType.convertString(rs.getString(2)));
			}
			rs.close();
			add_pub_stat.close();
			this.documentRelevance = documentRelevance;
		}
		return documentRelevance;
	}
	
	
	/**
	 * Return all {@link IPublication} for Query
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DatabaseLoadDriverException
	 */
	public List<IPublication> getPublications() throws SQLException, DatabaseLoadDriverException  {		
		List<IPublication> publications = new ArrayList<IPublication>();		
		String stat = QueriesPublication.selectPublicationsQueryExtra;		
		PreparedStatement ps;
		ps = (PreparedStatement) Configuration.getDatabase().getConnection().prepareStatement(stat);
		ps.setInt(1,getID());	
		ResultSet rs = ps.executeQuery();
		while(rs.next())
		{
			publications.add(new Publication(
					rs.getInt(1),
					rs.getString(2),//int pmid
					rs.getString(3),//String title
					rs.getString(4),//String authors
					rs.getString(5),//int date
					rs.getString(6),//String status
					rs.getString(7),//String journal
					rs.getString(8),//String volume
					rs.getString(9),//String issue
					rs.getString(10),//String pages
					rs.getString(11),  // String externalLink
					rs.getString(12),//String aBstract
					rs.getBoolean(13)
					));
		} 
		rs.close();
		ps.close();
		return publications;
	}


	/**
	 * Return if Query was taken in Pubmed
	 * 
	 * @return
	 */
	public boolean isFromPubmedSearch() {
		return fromPubmedSearch;
	}



	public void setFromPubmedSearch(boolean fromPubmedSearch) {
		this.fromPubmedSearch = fromPubmedSearch;
	}


	/**
	 * Add Outside Publication to a Query
	 * 
	 * @param publication
	 * @param publicationType
	 * @param file
	 * @param relevance
	 * @throws SQLException
	 * @throws DatabaseLoadDriverException
	 * @throws IOException 
	 */
	public void addOusidePublication(IPublication publication,String publicationType, File file,RelevanceType relevance) throws SQLException, DatabaseLoadDriverException, IOException {
		int publicationID = insertPublication(publication,publicationType);
		publication.setID(publicationID);
		addPublicationToCorpus(publicationID);
		updateRelevance(publication,relevance);
		if(file!=null)
			publication.addPDFFile(file);
		documentsMatching ++;
		if(publication.getAbstractSection()!=null && publication.getAbstractSection().length() > 0)
			available_abstracts ++;
		refreshQueryInfo();
	}
	
	/** Refresh query information 
	 * @param connection Database connection
	 * @param id_query Query id 
	 * @param n_pubs number of publications related to the query
	 * @param n_abs number of publications related to the query
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException */
	private void refreshQueryInfo() throws SQLException, DatabaseLoadDriverException{
		PreparedStatement statement;
		statement = GlobalOptions.database.getConnection().prepareStatement(QueriesIRProcess.updateQueryInfo);
		statement.setInt(1,getDocumentMathing());
		statement.setInt(2,getAvailable_abstracts());
		statement.setString(3, Utils.currentTime());
		statement.setInt(4,getID());
		statement.execute();
		statement.close();
	}


	/**
	 * Update {@link IPublication} {@link RelevanceType}
	 * 
	 * @param publication
	 * @param relevance
	 * @throws SQLException
	 * @throws DatabaseLoadDriverException
	 */
	public void updateRelevance(IPublication publication,RelevanceType relevance) throws SQLException, DatabaseLoadDriverException {
		
		if(relevance!=null)
		{
			if(getDocumentRelevance().containsKey(publication.getID()))
			{
				PreparedStatement ps;
					ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesDocument.updateRelevanceDocInQuery);
					ps.setNString(1,relevance.toString());
					ps.setInt(2,getID());
					ps.setInt(3,publication.getID());
					ps.execute();
					ps.close();

			}
			else
			{
				PreparedStatement ps;
					ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesDocument.insertRelevanceInDocInQuery);
					ps.setInt(1,publication.getID());
					ps.setInt(2,getID());
					ps.setNString(3,relevance.toString());
					ps.execute();
					ps.close();
			}
		}
		if(getDocumentRelevance()!=null)
			getDocumentRelevance().put(publication.getID(), relevance);
	}
	
	/**
	 * Get Document Relevance given Document database ID
	 * 
	 * @param documentID
	 * @return
	 * @throws SQLException
	 * @throws DatabaseLoadDriverException
	 */
	public RelevanceType getDocumentRelevanceType(int documentDatabaseID) throws SQLException, DatabaseLoadDriverException
	{

		if(getDocumentRelevance().containsKey(documentDatabaseID))
		{
			return getDocumentRelevance().get(documentDatabaseID);
		}
		else
		{
			return RelevanceType.none;
		}
	}
	
	private void addPublicationToCorpus(int id) throws SQLException, DatabaseLoadDriverException{
		PreparedStatement add_rel_stat;
		add_rel_stat = Configuration.getDatabase().getConnection().prepareStatement(QueriesPublication.insertPublicationOnQuery);
		add_rel_stat.setInt(1,getID());
		add_rel_stat.setInt(2, id);
		add_rel_stat.execute();
		add_rel_stat.close();
	}
	
	private int insertPublication(IPublication publication,String publicationType) throws SQLException, DatabaseLoadDriverException
	{		
		int result = -1;
		HashMap<String, Integer> tablesSize;
		PreparedStatement add_pub_stat = Configuration.getDatabase().getConnection().prepareStatement(QueriesPublication.insertPublication);
		tablesSize = getDbColumnsInfo();
		result = addPubToDB(add_pub_stat,publication,publicationType,tablesSize );
		return result;
	}
	
	private int addPubToDB(PreparedStatement p_statement,IPublication pub,String publicationType, HashMap<String, Integer> tablesSize) throws NumberFormatException, SQLException, DatabaseLoadDriverException{
		
		int pubID =  HelpDatabase.getNextInsertTableID(GlobalTablesName.publications);
		int idType = HelpDatabase.initArticleTypeID(publicationType);
		// Pmid
		String pmid = pub.getOtherID();
		p_statement.setNString(1,pmid);
		addToStatement(pub.getTitle(),2, p_statement, "title", "",tablesSize);	
		addToStatement(pub.getAuthors(),3, p_statement, "authors", "",tablesSize);
		String aux = pub.getYearDate();
		if(aux!=null && !aux.equals("null") && !aux.equals(""))
		{
			String[] tokens = aux.split("[ ;,-]");
			aux = tokens[0];
			p_statement.setInt(4, Integer.decode(aux));
		}
		else
			p_statement.setNString(4,null);
		
		if(aux!=null && !aux.equals(""))
		{
			String dte = Utils.formatDate(aux+";");
			if(dte.length()>=tablesSize.get("fulldate"))
				dte = dte.substring(0,tablesSize.get("fulldate"));
			p_statement.setNString(5, dte);
		}
		else
			p_statement.setNString(5,null);
		addToStatement("",6, p_statement, "type", null,tablesSize);
		addToStatement(pub.getStatus(),7, p_statement, "status", null,tablesSize);
		addToStatement(pub.getJournal(),8, p_statement, "journal", null,tablesSize);
		addToStatement(pub.getVolume(),9, p_statement, "volume", null,tablesSize);
		addToStatement(pub.getIssue(),10, p_statement, "issue", null,tablesSize);
		addToStatement(pub.getPages(),11, p_statement, "pages", null,tablesSize);
		String abstractTxt = pub.getAbstractSection();
		HTMLCodes codes = new HTMLCodes();
		abstractTxt = codes.cleanString(abstractTxt);
		abstractTxt = NormalizationForm.removeOffsetProblemSituation(abstractTxt);
		addToStatement(abstractTxt,12, p_statement, "abstract", null,tablesSize);
		// PMid represent the type of article not ID on database
		p_statement.setInt(13,idType);
		if(pub.getExternalLink()==null)
		{
			p_statement.setNString(14,null);
		}
		else
			p_statement.setNString(14, pub.getExternalLink());
		p_statement.execute();
		return pubID;

	}
	
	
	private void addToStatement(String value, int j, PreparedStatement p_statement, String column, String default_value,HashMap<String, Integer> tablesSize) throws SQLException{
		if(value!=null && !value.equals(""))
		{
			if(value.length()>=tablesSize.get(column))
				value = value.substring(0,tablesSize.get(column));
			p_statement.setNString(j, value);
		}
		else
			p_statement.setNString(j, default_value);
	}
	
	
    private static HashMap<String, Integer> getDbColumnsInfo() throws SQLException, DatabaseLoadDriverException{

		HashMap<String, Integer> db_columns = new HashMap<String, Integer>();
		Statement statement = Configuration.getDatabase().getConnection().createStatement();
		ResultSet res = statement.executeQuery(QueriesPublication.selectPublicationsResume);
		ResultSetMetaData rsmd = res.getMetaData();
		int numberOfColumns = rsmd.getColumnCount();
		for(int i=1;i<=numberOfColumns;i++){
			db_columns.put(rsmd.getColumnName(i),rsmd.getPrecision(i));
		}
		return db_columns;
    }


    /**
     * Return Query Name
     * 
     * @return
     * @throws DatabaseLoadDriverException 
     * @throws SQLException 
     */
	public String getName() throws SQLException, DatabaseLoadDriverException {
		if(name==null || name.length() == 0)
		{
			String newName = new String();
			if(getKeyWords()!=null && getKeyWords().length() > 0)
				newName = newName + getKeyWords() + ":";
			if(getOrganism()!=null && getOrganism().length() > 0)
				newName = newName + getOrganism() + ":";
			if(getDate()!=null)
				newName = newName + getDate();
			setNameINDatabase(newName);
			setName(newName);
		}
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Set Query Name in Database
	 * 
	 * @param name
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 */
	public void setNameINDatabase(String name) throws SQLException, DatabaseLoadDriverException {
		QueryManager.updateQueryName(getID(),name);
	}
	
		
}
