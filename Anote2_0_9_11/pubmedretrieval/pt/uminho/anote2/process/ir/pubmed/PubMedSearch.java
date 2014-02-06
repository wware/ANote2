package pt.uminho.anote2.process.ir.pubmed;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IDocumentSet;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.core.report.processes.ir.IIRSearchProcessReport;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;
import pt.uminho.anote2.datastructures.documents.publication.DocumentManager;
import pt.uminho.anote2.datastructures.documents.query.QueryManager;
import pt.uminho.anote2.datastructures.process.IRProcess;
import pt.uminho.anote2.datastructures.report.processes.IRSearchReport;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.datastructures.utils.conf.OtherConfigurations;
import pt.uminho.anote2.process.IR.IIRSearch;
import pt.uminho.anote2.process.IR.exception.InternetConnectionProblemException;
import es.uvigo.ei.sing.BioSearch.connection.PubMexProgressHandler;
import es.uvigo.ei.sing.BioSearch.datatypes.CollectionNCBI;


public class PubMedSearch extends IRProcess implements IIRSearch{
	
	/**
	 * Logger
	 */
	static Logger logger = Logger.getLogger(PubMedSearch.class.getName());
	
	public static int MAX_RESULTS = 10000;
	public static final String pubmedLink = "http://www.ncbi.nlm.nih.gov/pubmed/";

	
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
	private boolean stop;
	private boolean isfreefulltextquery = false;
	private int nPubs;
	private int actuaPubs;
	private boolean cancel = false;
	
	public PubMedSearch() {
		super();
		this.organism=new String();
		this.keywords=new String();
		this.properties=new Properties();
		this.stop = false;
		try {
			idTypePmid = HelpDatabase.initArticleTypeID(GlobalNames.pmid);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
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
	public int getQueryResults(String query) throws InternetConnectionProblemException{
		PubMexAnote ex = new PubMexAnote();		
		return ex.getResultCount(query);
	}
	
	public IIRSearchProcessReport search(String keywords,String organism,Properties properties) throws DatabaseLoadDriverException, SQLException , InternetConnectionProblemException
	{
		if(!OtherConfigurations.getFreeFullTextOnly())
		{	
			return normalSearch(keywords, organism, properties);
		}
		else
		{
			if(!properties.containsKey("articleDetails"))
			{
				return restrictedversionSearch(keywords, organism, properties);
			}
			else 
			{
				String prop = properties.getProperty("articleDetails");
				if(prop.equals("freefulltext"))
				{
					setIsfreefulltextquery(true);
					return normalSearch(keywords, organism, properties);
				}
				else
				{
					return restrictedversionSearch(keywords, organism, properties);
				}
			}
		}
	}
	
	private IIRSearchProcessReport normalSearch(String keywords,String organism, Properties properties) throws DatabaseLoadDriverException, SQLException, InternetConnectionProblemException {
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		IIRSearchProcessReport report = new IRSearchReport(keywords, organism);
		String query;
		setKeywords(keywords);
		setOrganism(organism);
		setProperties(properties);	
		query = buildQuery();
		nPubs = getQueryResults(query);		
		return searchMethod(startTime, report, query);
	}
	
	private IIRSearchProcessReport restrictedversionSearch(String keywords, String organism, Properties properties) throws DatabaseLoadDriverException, SQLException, InternetConnectionProblemException {
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		IIRSearchProcessReport report = new IRSearchReport(keywords, organism);
		String query;
		setKeywords(keywords);
		setOrganism(organism);
		setProperties(properties);	
		query = buildQuery();
		int search1 = getQueryResults(query);		
		properties.put("articleDetails","freefulltext");
		setProperties(properties);
		query = buildQuery();
		int search2 = getQueryResults(query);
		nPubs = search1 + search2;
		properties.remove("articleDetails");
		query = buildQuery();
		searchMethod(startTime,report,query);
		searchFreeFullTextMethod(properties);
		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		report.setTime(endTime-startTime);
		return report;
	} 
	
	private IIRSearchProcessReport searchMethod(long startTime,
			IIRSearchProcessReport report, String query) throws DatabaseLoadDriverException, SQLException {
		boolean correct = true;
		int queryID = QueryManager.insertQuery(getKeywords(), getOrganism(), 0,0,properties,true);
		setIdQuery(queryID);		
		HashMap<String,Integer>	tableMemberSize = new HashMap<String, Integer>();
		tableMemberSize = getDbColumnsInfo(Configuration.getDatabase().getConnection());
        if(nPubs > MAX_RESULTS)
		{        	
        	int year;
        	int maxYear;
        	if(!getProperties().containsKey("fromDate"))
        	{
        		year=PublicationManager.searchYearStarting;
        	}
        	else
        	{
        		year=Integer.valueOf((String) getProperties().get("fromDate"));
        	}
        	if(!getProperties().containsKey("toDate"))
        	{
        		maxYear = new GregorianCalendar().get(Calendar.YEAR);
        	}
        	else
        	{
        		if(!getProperties().get("toDate").equals("present"))
        		{
        			maxYear=Integer.valueOf((String) getProperties().get("toDate"));
        		}
        		else
        		{
            		maxYear = new GregorianCalendar().get(Calendar.YEAR);
        		}   			
        	}	    
		    while(year<=maxYear)
		    {			
				query = rebuildQueryByYear(year);
				correct = searchPubmed(query,tableMemberSize,report);
				year++;
		    }
		}
		else
		{
			correct = searchPubmed(query,tableMemberSize,report);
		}
        if(correct)
        {
        	QueryManager.refreshQueryInfo(getID(), getnPublicacoes(), getnAbstracts());
        }
        if(!correct)
        	report.setcancel();
		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		report.setTime(endTime-startTime);
		return report;
	}
	
	private boolean searchFreeFullTextMethod(Properties properties) {
		String query;
		properties.put("articleDetails","freefulltext");
		query = buildQuery();
        if(nPubs > MAX_RESULTS)
		{
        	nPubs=totalPubs();
        	
        	int year;
        	int maxYear;
        	if(!getProperties().containsKey("fromDate"))
        	{
        		year=PublicationManager.searchYearStarting;
        	}
        	else
        	{
        		year=Integer.valueOf((String) getProperties().get("fromDate"));
        	}
        	if(!getProperties().containsKey("toDate"))
        	{
        		Calendar cal = Calendar.getInstance();
        		maxYear = cal.get(Calendar.YEAR);	
        	}
        	else
        	{
        		if(!getProperties().get("toDate").equals("present"))
        		{
        			maxYear=Integer.valueOf((String) getProperties().get("toDate"));
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
				searchPubmed2(query);
				year++;
		    }
		}
		else
		{
			searchPubmed2(query);
		}
		return true;
	}
	
	protected boolean searchPubmed2(String query){
	    final PubMexAnote ex = new PubMexAnote();
		ex.doQuery(query, new PubMexProgressHandler(){

			public void cancelled() {
				return;
			}
			public void count(int count) {
				if(cancel)
				{
					ex.cancel();
					if(getID()!=-1)
					{
						try {
							PublicationManager.removeQuery(getID());
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (DatabaseLoadDriverException e) {
							e.printStackTrace();
						}
					}
					return;
				}
			}
			public void error(Throwable error) {}

			public void finished(CollectionNCBI results) {
				
				if(results==null) // No results for the keywors
				{
					
				}
				else
				{
					try {
						PreparedStatement updt_rel_stat = Configuration.getDatabase().getConnection().prepareStatement(QueriesPublication.updatePublicationPDFavailable);					
						int pubs = 0;
						for (; pubs<results.getResultSize(); pubs++) //result row
						{	
							String[] values = results.getResultAt(pubs);
							String pmid = values[1];				
							updt_rel_stat.setBoolean(1,true);
							updt_rel_stat.setNString(2,pmid);
							updt_rel_stat.setInt(3,idTypePmid);
							updt_rel_stat.executeUpdate();
						}
						updt_rel_stat.close();
					} catch (SQLException e) {
						logger.error(e.getMessage());
					} catch (DatabaseLoadDriverException e) {
						logger.error(e.getMessage());
					}
					actuaPubs = actuaPubs + results.getResultSize() % 50;
				}
			}
			public void getted(int getted) {
				if(cancel)
				{
					ex.cancel();
					if(getID()!=-1)
					{
						try {
							PublicationManager.removeQuery(getID());
						} catch (SQLException e) {
							logger.error(e.getMessage());
						} catch (DatabaseLoadDriverException e) {
							e.printStackTrace();
						}
					}
					return;
				}
				actuaPubs=actuaPubs+50;
			}
		});
		return !cancel;
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
	
	public boolean searchPubmed(String q,final HashMap<String,Integer> tableMemberSize, final IIRSearchProcessReport report){
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
					
					try {
						/** Linked list with the already existing pmids in database. */
						Map<String, Integer> pmidsAlreadyExistOnDB = DocumentManager.getAllPublicationsOtherIDs(idTypePmid);

						PreparedStatement add_pub_stat = Configuration.getDatabase().getConnection().prepareStatement(QueriesPublication.insertPublication);
						PreparedStatement add_rel_stat = Configuration.getDatabase().getConnection().prepareStatement(QueriesPublication.insertPublicationOnQuery);
						int pubs=0;
						report.incrementDocumentRetrieval(results.getResultSize());
						for (; pubs<results.getResultSize(); pubs++) //result row
						{	
							String[] values = results.getResultAt(pubs);
							if(stop)		
							{
								report.setcancel();
								break;
							}
							if(!pmidsAlreadyExistOnDB.containsKey(values[1]))
							{
								int pubID =  HelpDatabase.getNextInsertTableID(GlobalTablesName.publications);
								abs_count += addPubToDB(add_pub_stat, results, pubs, true,tableMemberSize); //adds the publication to the database
								addRelation(add_rel_stat,pubID);
							}
							else
							{	
								int pos = getAttributeIndex("Abstract", results); // abstract
								if(values[pos].length() > 0)
									abs_count++;
								int pubID =  pmidsAlreadyExistOnDB.get(values[1]);
								addRelation(add_rel_stat,pubID);
							}							

						}
						addToCounts(pubs, abs_count);
						add_pub_stat.close();
						add_rel_stat.close();
					} catch (SQLException e) {
						logger.error(e.getMessage());
					} catch (DatabaseLoadDriverException e) {
						logger.error(e.getMessage());
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
    	if(properties.getProperty("fromDate")==null && properties.getProperty("toDate") != null)
    	{
    		query = " (\"" + PublicationManager.searchYearStarting + "\"[Publication date]:\" "+ properties.get("toDate") +"\"[Publication Date]) AND " + query;
    	}
    	else if(properties.getProperty("fromDate")==null && properties.getProperty("toDate") == null)
    	{
    		query = " (\"" + PublicationManager.searchYearStarting + "\"[Publication date]:\"3000\"[Publication Date]) AND " + query;
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
				return organism;
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
			p_statement.setNString(j++,pmid);
		
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
			p_statement.setNString(j++,null);
		
		pos = getAttributeIndex("Publication Date", publications_result); // fulldate
		if(values[pos]!=null && !values[pos].equals(""))
		{
			String dte = Utils.formatDate(values[pos]+";");
			if(dte.length()>=tablesSize.get("fulldate"))
				dte = dte.substring(0,tablesSize.get("fulldate"));
			p_statement.setNString(j++, dte);
		}
		else
			p_statement.setNString(j++, null);
	
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
		
		// External Link
					
		if(is_new)
		{
			p_statement.setInt(j++,getIdTypePmid());
			p_statement.setNString(j++, PubMedSearch.pubmedLink + pmid);
			p_statement.execute();
		}
		else
		{
			p_statement.setNString(j++, PubMedSearch.pubmedLink + pmid);
			p_statement.setNString(j++, pmid);
			p_statement.executeUpdate();
		}
		
		if(values[pos] == null || values[pos].length() == 0) // if abstract is null
			return 0;
		return 1;
	}
	
	protected void addToStatement(String value, int j, PreparedStatement p_statement, String column, String default_value,HashMap<String, Integer> tablesSize) throws SQLException{
		if(value!=null && !value.equals(""))
		{
			if(value.length()>=tablesSize.get(column))
				value = value.substring(0,tablesSize.get(column));
			p_statement.setNString(j, value);
		}
		else
			p_statement.setNString(j, default_value);
	}

	/** Add the relation: (id_query,pmid) to database 
	 * @throws SQLException */
	protected void addRelation(PreparedStatement p_stat, int id) throws SQLException{
		p_stat.setInt(1, idQuery);
		p_stat.setInt(2, id);
		p_stat.execute();
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
	
	public int getID() {
		return idQuery;
	}

	@Override
	public void stop() {
		stop = true;		
	}


	public static Properties defaultPropertiesSettings() {
		Properties prop = new Properties();
		return prop;
	}
	
	public boolean isIsfreefulltextquery() {
		return isfreefulltextquery;
	}

	public void setIsfreefulltextquery(boolean isfreefulltextquery) {
		this.isfreefulltextquery = isfreefulltextquery;
	}

	public int getActuaPubs() {
		return actuaPubs;
	}

	public void setActuaPubs(int actuaPubs) {
		this.actuaPubs = actuaPubs;
	}
	
	public int getnAbstracts() {
		return nAbstracts;
	}

	public int getnPublicacoes() {
		return nPublicacoes;
	}
			
}

