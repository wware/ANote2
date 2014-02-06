package pt.uminho.anote2.process.ir.pubmed;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.core.configuration.IProxy;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.document.IDocumentSet;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesIRProcess;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesProcess;
import pt.uminho.anote2.datastructures.process.IRProcess;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.anote2.process.IR.IIRSearch;
import es.uvigo.ei.sing.BioSearch.connection.PubMexProgressHandler;
import es.uvigo.ei.sing.BioSearch.datatypes.CollectionNCBI;


public class PubMedSearch extends IRProcess implements IIRSearch{
	
	public static int MAX_RESULTS = 10000;
	
	/**
	 * For helping to divide pubmed search work
	 */
	
	private int nAbstracts = 0;
	private int nPublicacoes = 0;
	private int idQuery = -1;

	private String keywords;
	private String organism;
	private Properties properties;
	private int idTypePmid;
	
	public PubMedSearch(IProxy proxy,IDatabase database) {
		super(proxy,database);
		this.organism=new String();
		this.keywords=new String();
		this.properties=new Properties();
		setProxy();
		idTypePmid = HelpDatabase.initArticleTypeID(database,"pmid");
	}



	public int getIdTypePmid() {
		return idTypePmid;
	}

	public IDocumentSet getDocuments() { return null;}
	
	public List<IPublication> getPublicationDocuments(){ return null;}
	
	/**
	 * Method that return the number of publications for query
	 * 
	 * @return number of publications
	 */
	public int getQueryResults(String query) {
		PubMexAnote ex = new PubMexAnote();	
		
		return ex.getResultCount(query);
	}
	
	public boolean search(String keywords,String organism,Properties proterties)  
	{
		this.keywords=keywords;
		this.organism=organism.replace("\"","");
		this.properties=proterties;	
		
		String query=new String();
		this.getDatabase().openConnection();
		Connection connection = getDatabase().getConnection();
	
		if(connection == null)
		{
			this.getDatabase().closeConnection();
			return false;
		}		
		query = buildQuery();		
		int nPubs = getQueryResults(query);	
		if(nPubs==0)
		{
			this.getDatabase().closeConnection();
			return true;
		}		
		idQuery = insertQuery(connection);	
		if(idQuery==-1)
		{
			this.getDatabase().closeConnection();
			return false;
		}		
		HashMap<String,Integer>	tableMemberSize = new HashMap<String, Integer>();
		try {
			tableMemberSize = getDbColumnsInfo(connection);
		} catch (SQLException e1) {
			e1.printStackTrace();
			this.getDatabase().closeConnection();
			return false;
		}
        	
        if(nPubs > MAX_RESULTS)
		{
        	int year;
        	int maxYear;
        	if(!this.properties.containsKey("fromDate"))
        	{
        		year=PublicationManager.searchYearStarting;
        	}
        	else
        	{
        		year=Integer.valueOf((String) this.properties.get("fromDate"));
        	}
        	if(!this.properties.containsKey("toDate"))
        	{
        		Calendar cal = Calendar.getInstance();
        		maxYear = cal.get(Calendar.YEAR);	
        	}
        	else
        	{
        		if(!this.properties.get("toDate").equals("present"))
        		{
        			maxYear=Integer.valueOf((String) this.properties.get("toDate"));
        		}
        		else
        		{
            		Calendar cal = Calendar.getInstance();
            		maxYear = cal.get(Calendar.YEAR);	
        		}   			
        	}	    
		    while(year<=maxYear)
		    {			
				query = rebuildQueryByYear(year);
				searchPubmed(query,tableMemberSize);
				year++;
		    }
		}
		else
		{
			searchPubmed(query,tableMemberSize);
		}
        refreshQueryInfo();
		return true;
	}
	
	/**
	 * Method that return the number of publications for query
	 * 
	 * @return number of publications
	 */
	public int totalPubs()
	{
		PubMexAnote pubmed = new PubMexAnote();
		int pubs=0;
		int year;
    	int maxYear;
    	
    	if(!this.properties.containsKey("fromDate"))
    	{
    		year=PublicationManager.searchYearStarting;
    	}
    	else
    	{
    		year=Integer.valueOf((String) this.properties.get("fromDate"));
    	}
    	if(!this.properties.containsKey("toDate"))
    	{
    		Calendar cal = Calendar.getInstance();
    		maxYear = cal.get(Calendar.YEAR);	
    	}
    	else
    	{
    		if(!this.properties.get("toDate").equals("present"))
    		{
    			maxYear=Integer.valueOf((String) this.properties.get("toDate"));
    		}
    		else
    		{
        		Calendar cal = Calendar.getInstance();
        		maxYear = cal.get(Calendar.YEAR);	
    		}   			
    	}	    
	    while(year<=maxYear)
	    {
			String query2 = rebuildQueryByYear(year);
			int pubsInt = pubmed.getResultCount(query2);
			pubs = pubs+pubsInt;
			year++;
	    }
	    return pubs;
	}
	
	protected boolean searchPubmed(String q,final HashMap<String,Integer> tableMemberSize){
	    final PubMexAnote ex = new PubMexAnote();
	   
		ex.doQuery(q, new PubMexProgressHandler(){

			public void cancelled() {}
			public void count(int count) {}
			public void error(Throwable error) {}

			public void finished(CollectionNCBI results) {
				
				if(results==null) // No results for the keywors
				{
					
				}
				else
				{
					int abs_count = 0;
					/** Linked list with the already existing pmids in database. */
					LinkedList<String> pmidsAlreadyExistOnDB = getPublications(getDatabase().getConnection());
					try {
						PreparedStatement add_pub_stat = getDatabase().getConnection().prepareStatement(QueriesPublication.insertPublications);
						PreparedStatement add_rel_stat = getDatabase().getConnection().prepareStatement(QueriesPublication.insertPublicationOnQuery);
						PreparedStatement updt_rel_stat = getDatabase().getConnection().prepareStatement(QueriesPublication.updatePublication);
						PreparedStatement getID = getDatabase().getConnection().prepareStatement(QueriesPublication.getIDOtherID);					
						int pubs = 0;	
						for (; pubs<results.getResultSize(); pubs++) //result row
						{	
							String[] values = results.getResultAt(pubs);
							
							try {
								if(!pmidsAlreadyExistOnDB.contains(values[1]))
								{
									int pubID =  HelpDatabase.getNextInsertTableID(getDatabase(),"publications");
									abs_count += addPubToDB(add_pub_stat, results, pubs, true,tableMemberSize); //adds the publication to the database
									addRelation(add_rel_stat,pubID);
								}
								else
								{
									abs_count += addPubToDB(updt_rel_stat, results, pubs, false,tableMemberSize); //updates the publication on the database
									getID.setString(1, values[1]);
									ResultSet rs = getID.executeQuery();
									if(rs.next())
									{
										int pubID =  rs.getInt(1);
										addRelation(add_rel_stat,pubID);
									}			
								}							
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					addToCounts(pubs, abs_count);
					} catch (SQLException e) {
					    e.printStackTrace();
					}
				 
				}
			}
			public void getted(int getted) {}
		});
		return true;
	}
	
	protected void addToCounts(int pubs, int absCount){
	    	this.nPublicacoes += pubs;
	    	this.nAbstracts += absCount;
	}
	
	/**
	 * Method for building query and return it
	 * 
	 * @return Query (Pubmed Format)
	 */
    public String buildQuery(){
    	String query = "";
    	
       	if(!this.keywords.equals(""))
    	    query = " (" + keywords + ") ";

    	if(!this.organism.equals(""))
    	    query = query + getOrganismoName(organism);

    	if(properties.getProperty("authors")!=null)
    	    query = " (" + properties.get("authors") + "[Author]) AND" + query;
    	
    	if(properties.getProperty("journal")!=null)
    	    query = " (\"" + properties.get("journal") + "\"[Journal]) AND" + query;
    	if(properties.getProperty("fromDate")==null)
    	{
    		query = " (\"" + PublicationManager.searchYearStarting + "\"[Publication date]:\" "+ properties.get("toDate") +"\"[Publication Date]) AND " + query;
    	}
    	else if(properties.getProperty("toDate")==null)
    	{
        	query = " (\"" + properties.get("fromDate") + "\"[Publication date]:\"3000\"[Publication Date]) AND " + query;
    	}	
    	else
    	{
    	    query = " (\"" + properties.get("fromDate") + "\"[Publication date]:\""+properties.get("toDate")+"\"[Publication Date]) AND " + query;
    	}
       	if(properties.getProperty("articleDetails")!=null)
    	{
       		String articleDetails = properties.getProperty("articleDetails");
       		
       		if(articleDetails.equals("abstract"))
       		{
       			query = " (hasabstract[text]) AND"+ query;
       		}
       		else if(articleDetails.equals("freefulltext"))
       		{
       			query = " (free full text[sb]) AND"+ query;
       		}
       		else
       		{
       			query = " (full text[sb]) AND"+query;
       		}
    	}
    	if(properties.getProperty("ArticleSource")!=null)
    	{
    		String medpmc = properties.getProperty("ArticleSource");
    		if(medpmc.equals("medpmc"))
    		{
    			query = " ((medline[sb] OR pubmed pmc local[sb])) AND"+ query;
    		}
    		else if(medpmc.equals("med"))
    		{
    			query = " ((medline[sb])) AND"+ query;
    		}
    		else
    		{
    			query = " ((pubmed pmc local[sb])) AND"+ query;
    		}
    	}
    	if(properties.getProperty("articletype")!=null)
    	{
    		query = " (("+properties.get("articletype")+"[ptyp])) AND"+query;
    	}
       	if(query.endsWith("AND"))
    	{
    		query = query.substring(0,query.length()-3);
    	}
    	return query;
    }
    
	protected String getOrganismoName(String orgaSTream) {
		
		String[] organisms = orgaSTream.split("AND|OR");
		String changeOrganism;
		for(String organism:organisms)
		{
			changeOrganism = parse(organism);
			orgaSTream = orgaSTream.replace(organism, changeOrganism);
		}
		orgaSTream = orgaSTream.replace("AND"," AND ");
		orgaSTream = orgaSTream.replace("OR"," OR ");
		return 	" AND  (" + orgaSTream +")";		
	}
	
	private String parse(String organism){

		Pattern p = Pattern.compile("\\s*\\w\\.\\s\\w+\\s*");
		Matcher m;
		String organism2 = organism;
		organism = organism.replace("\"","");

		m = p.matcher(organism);
		
		if(m.find())
			return "\"" + organism + "\"";
		else
		{
			p = Pattern.compile("\\s*(\\w+)\\s(\\w+)\\s*");
			m = p.matcher(organism);
			if(m.find())
			{
				String newOrgan =  "(\"" + m.group(1).charAt(0) + ". " + m.group(2) + "\"OR\"" + m.group(1)+" "+m.group(2) + "\")";	

				String ret = organism2.replace(organism, newOrgan);

				return ret;
			}
			else
				return "\"" + organism + "\"";
		}
	}
        
	/**
	 * 
	 * Method for building query for year and return it
	 * 
	 * @param year
	 * @return Query (Pubmed Format)
	 */
	protected String rebuildQueryByYear(int year){
    	String query = "";
    	
    	if(!this.keywords.equals(""))
    	    query = " (" + keywords + ") ";

    	if(!this.organism.equals(""))
    	    query = query + getOrganismoName(organism);

    	if(properties.get("authors")!=null)
    	    query = " (" + properties.get("authors") + "[Author]) AND" + query;
    	
    	if(properties.get("journal")!=null)
    	    query = " (\"" + properties.get("journal") + "\"[Journal]) AND" + query;

    	query = " (\"" + year + "\"[Publication date]:\"" + year + "[Publication Date]) AND" + query;
    	   
       	if(properties.getProperty("articleDetails")!=null)
    	{
       		String articleDetails = properties.getProperty("articleDetails");
       		
       		if(articleDetails.equals("abstract"))
       		{
       			query = " (hasabstract[text]) AND"+ query;
       		}
       		else if(articleDetails.equals("freefulltext"))
       		{
       			query = " (free full text[sb]) AND"+ query;
       		}
       		else
       		{
       			query = " (full text[sb]) AND"+query;
       		}
    	}
    	if(properties.getProperty("ArticleSource")!=null)
    	{
    		String medpmc = properties.getProperty("ArticleSource");
    		if(medpmc.equals("medpmc"))
    		{
    			query = " ((medline[sb] OR pubmed pmc local[sb])) AND"+ query;
    		}
    		else if(medpmc.equals("med"))
    		{
    			query = " ((medline[sb])) AND"+ query;
    		}
    		else
    		{
    			query = " ((pubmed pmc local[sb])) AND"+ query;
    		}
    	}
    	if(properties.getProperty("articletype")!=null)
    	{
    		query = " (("+properties.get("articletype")+"[ptyp])) AND"+query;
    	}
    	if(query.endsWith("AND"))
    	{
    		query = query.substring(0,query.length()-3);
    	}
    	return query;
	}
	
	
    
	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
		
	/** Get the publications that already existing in the database */
	protected LinkedList<String> getPublications(Connection connection){
		
		LinkedList<String> pmids_db = new LinkedList<String>();
		try{
			Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery(QueriesPublication.otherIDPublications);
			
			while(res.next())
				pmids_db.add(res.getString(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pmids_db;
	}
	
	/** Adds or updates a publication in the database
	 * @param is_new if true, adds the publication else, updates the publication information
	 * @return 1 if the abstract is different from zero or 0 in the other case
	 * @throws SQLException 
	 * @throws NumberFormatException */
	protected int addPubToDB(PreparedStatement p_statement,CollectionNCBI publications_result, int i, boolean is_new,HashMap<String, Integer> tablesSize) throws NumberFormatException, SQLException{
		
		/** 
		 * [1] [7] [5] [15] [26] [20] [16] [24] [8] [9] [10] [A] 
 		 *              ^    ^    ^    ^                      ^ 
		 * */
		
		String[] values = publications_result.getResultAt(i);
		
		int j=1;
		
		// Pmid
		String pmid = values[1];
		if(is_new)
			p_statement.setString(j++,pmid);
		
		int pos = getAttributeIndex("Title", publications_result); // title
		addToStatement(values[pos], j++, p_statement, "title", "",tablesSize);
		
		pos = getAttributeIndex("AuthorList", publications_result); // authors
		addToStatement(values[pos], j++, p_statement, "authors", "",tablesSize);
		
		pos = getAttributeIndex("Publication Date", publications_result); // date	
		String aux = values[pos];
		if(aux!=null && !aux.equals("null") && !aux.equals(""))
		{
			String[] tokens = aux.split("[ ;,-]");
			aux = tokens[0];
			p_statement.setInt(j++, Integer.decode(aux));
		}
		else
			p_statement.setString(j++,null);
		
		pos = getAttributeIndex("Publication Date", publications_result); // fulldate
		if(values[pos]!=null && !values[pos].equals(""))
		{
			String dte = Utils.formatDate(values[pos]+";");
			if(dte.length()>=tablesSize.get("fulldate"))
				dte = dte.substring(0,tablesSize.get("fulldate"));
			p_statement.setString(j++, dte);
		}
		else
			p_statement.setString(j++, null);
	
		pos = getAttributeIndex("Publication Type", publications_result); // type
		addToStatement(values[pos], j++, p_statement, "type", null,tablesSize);
		
		pos = getAttributeIndex("Publication Status", publications_result); // status
		addToStatement(values[pos], j++, p_statement, "status", null,tablesSize);
		
		pos = getAttributeIndex("Journal Title", publications_result); // journal
		addToStatement(values[pos], j++, p_statement, "journal", null,tablesSize);
		
		pos = getAttributeIndex("Volume", publications_result); // volume
		addToStatement(values[pos], j++, p_statement, "volume", null,tablesSize);
	
		pos = getAttributeIndex("Issue", publications_result); // issue
		addToStatement(values[pos], j++, p_statement, "issue", null,tablesSize);
		
		pos = getAttributeIndex("Pagination", publications_result); // pages
		addToStatement(values[pos], j++, p_statement, "pages", null,tablesSize);
	
		pos = getAttributeIndex("Abstract", publications_result); // abstract
		addToStatement(values[pos], j++, p_statement, "abstract", null,tablesSize);
		
				
		if(is_new)
		{
			p_statement.setInt(j++,getIdTypePmid());
			p_statement.execute();
		}
		else
		{
			p_statement.setString(j++, pmid);
			p_statement.executeUpdate();
		}

		if(values[pos] == null) // if abstract is null
			return 0;
		return 1;
	}
	
	protected void addToStatement(String value, int j, PreparedStatement p_statement, String column, String default_value,HashMap<String, Integer> tablesSize) throws SQLException{
		if(value!=null && !value.equals(""))
		{
			if(value.length()>=tablesSize.get(column))
				value = value.substring(0,tablesSize.get(column));
			p_statement.setString(j, value);
		}
		else
			p_statement.setString(j, default_value);
	}
		
	/** Insert the query on database and returns its id. If return value is equals to -1, an error was occurred */
	protected int insertQuery(Connection connection){
		int id_query=-1;
		try {
			
			/** Add query to database */
			
			PreparedStatement statement = connection.prepareStatement(QueriesProcess.insertQuery);
			statement.setString(1, Utils.currentTime());
			statement.setString(2, keywords);
			statement.setString(3, organism);			
			statement.execute();
			ResultSet r = statement.executeQuery(QueriesIRProcess.idQueryMax);
			if(r.next())
				id_query = r.getInt(1);	
			insertproperties(id_query);		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id_query;
	}
	
	public void insertproperties(int id_query) {
		try {		
			Connection connection = getDatabase().getConnection();
			PreparedStatement statement = connection.prepareStatement(QueriesIRProcess.insertQueryProperties);	
			Enumeration<Object> itObj = this.properties.keys();
			while(itObj.hasMoreElements())
			{
				Object propertyNameObj = itObj.nextElement();
				String propertyName = (String) propertyNameObj ;
				Object propertyValueObj = this.properties.getProperty(propertyName);
				statement.setInt(1,id_query);
				statement.setString(2, propertyName);
				statement.setString(3,(String) propertyValueObj);
				statement.execute();
					
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/** Add the relation: (id_query,pmid) to database 
	 * @throws SQLException */
	protected void addRelation(PreparedStatement p_stat, int id) throws SQLException{
		p_stat.setInt(1, idQuery);
		p_stat.setInt(2, id);
		p_stat.execute();
	}
	
	/** Refresh query information 
	 * @param connection Database connection
	 * @param id_query Query id 
	 * @param n_pubs number of publications related to the query
	 * @param n_abs number of publications related to the query*/
	protected void refreshQueryInfo(){
		
	    if(idQuery==-1)
	    	return;
		Connection connection = getDatabase().getConnection();
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(QueriesIRProcess.updateQueryInfo);
			statement.setInt(1,nPublicacoes);
			statement.setInt(2,nAbstracts);
			statement.setInt(3,idQuery);
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	//gets the column position of a given attribute name in a given result set
    protected int getAttributeIndex(String attributeName,CollectionNCBI result){
        return result.getResultAttributes().indexOf(attributeName);
    } 
    
    protected static HashMap<String, Integer> getDbColumnsInfo(Connection c) throws SQLException{

		HashMap<String, Integer> db_columns = new HashMap<String, Integer>();
		Statement statement = c.createStatement();
		ResultSet res = statement.executeQuery(QueriesPublication.selectPublicationsResume);
		ResultSetMetaData rsmd = res.getMetaData();
		int numberOfColumns = rsmd.getColumnCount();
		for(int i=1;i<=numberOfColumns;i++){
			db_columns.put(rsmd.getColumnName(i),rsmd.getPrecision(i));
		}
		return db_columns;
    }
    

	public String getOrganism() {
		return organism;
	}
	public void setOrganism(String organism) {
		this.organism = organism.replace("\"","");;
	}

	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	public int getIdQuery() {
		return idQuery;
	}
	public void setIdQuery(int idQuery) {
		this.idQuery = idQuery;
	}

	public String getQuery() {
		return buildQuery();
	}

	public String getType() {
		return "IRSearch";
	}
	
	public IDatabase getDB() {
		return getDatabase();
	}

	public int getID() {
		return idQuery;
	}
			
	
}

