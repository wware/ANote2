package pt.uminho.anote2.aibench.publicationmanager.dataStructures;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.utils.operations.TimeLeftProgress;
import pt.uminho.anote2.core.configuration.IProxy;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesIRProcess;
import pt.uminho.anote2.process.ir.pubmed.PubMexAnote;
import es.uvigo.ei.sing.BioSearch.connection.PubMexProgressHandler;
import es.uvigo.ei.sing.BioSearch.datatypes.CollectionNCBI;

/**
 * @Aibench Extension of PubMedSearch.
 * This class serves to implements in PubMedSearch to update a query
 * 
 * @author Hugo Costa
 *
 */
public class PubMedSearchExtensionUpdate extends PubMedSearchExtension{

	private Set<String> pmids= new HashSet<String>();
	private int abstractavailable;
	private int publicationAdded;
	
	public PubMedSearchExtensionUpdate(IProxy proxy, IDatabase database,
			boolean cancel, TimeLeftProgress progress) {
		super(proxy, database, cancel, progress);
		this.pmids= new HashSet<String>();
		this.abstractavailable=0;
		this.publicationAdded=0;		
	}
	
	public int getPublicationAdded() {
		return publicationAdded;
	}
	
	/**
	 * This method receive a query that has already been made and try to update it in Database
	 * 
	 * @param queryInfo
	 * @return true -- If update have success
	 * 	       false -- otherwise
	 */
	public boolean search(QueryInformationRetrievalExtension queryInfo)  
	{
		pmids = queryInfo.getPmids();;
		setKeywords(queryInfo.getKeyWords());
		setOrganism(queryInfo.getOrganism());
		setProperties(queryInfo.getProperties());
		
		setIdQuery(queryInfo.getID());
		
		this.getDatabase().openConnection();
		Connection connection = getDatabase().getConnection();
	
		if(connection == null)
		{
			return false;
		}
		
		String query = buildQuery();		
		setNPubs(getQueryResults(query));
		
		if(getNPubs()==0)
		{
			this.publicationAdded=0;
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
		setStarttime(GregorianCalendar.getInstance().getTimeInMillis());	
        if(getNPubs() > MAX_RESULTS)
		{
        	setNPubs(totalPubs());
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
				searchPubmed(query,tableMemberSize);
				year++;
		    }
		}
		else
		{
			searchPubmed(query,tableMemberSize);
		}
        refreshQueryInfo(queryInfo);
        this.getDatabase().closeConnection();
		return true;
	}
	
	protected boolean searchPubmed(String q,final HashMap<String,Integer> tableMemberSize){
	    final PubMexAnote ex = new PubMexAnote();
		ex.doQuery(q, new PubMexProgressHandler(){

			
			public void cancelled() {
				return;
			}
			public void count(int count) {
				if(isCancel()==true)
				{
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
					/** Linked list with the already existing pmids in database. */
					LinkedList<String> pmidsAlreadyExistOnDB = getPublications(getDatabase().getConnection());					
					try {
						PreparedStatement add_pub_stat = getDatabase().getConnection().prepareStatement(QueriesPublication.insertPublications);
						PreparedStatement add_rel_stat = getDatabase().getConnection().prepareStatement(QueriesPublication.insertPublicationOnQuery);
						PreparedStatement updt_rel_stat = getDatabase().getConnection().prepareStatement(QueriesPublication.updatePublication);
						PreparedStatement getID = getDatabase().getConnection().prepareStatement(QueriesPublication.selectPublicationIDForPublicationOtherID);

						int pubs = 0;
						
						for (; pubs<results.getResultSize(); pubs++) //result row
						{	
							// Se ja tiver o pmid nao faz nd
							String[] values = results.getResultAt(pubs);
							if(pmids.contains(values[1]))
							{
								addPubToDB(updt_rel_stat, results, pubs, false,tableMemberSize); 
							}
							else
							{	
								if(!pmidsAlreadyExistOnDB.contains(values[1]))
								{
									int pubID =  HelpDatabase.getNextInsertTableID(getDatabase(),"publications");
									addPubToDB(add_pub_stat, results, pubs, true,tableMemberSize); //adds the publication to the database		
									addRelation(add_rel_stat,pubID);
									publicationAdded++;
								}
								else
								{
									addPubToDB(updt_rel_stat, results, pubs, false,tableMemberSize); //updates the publication on the database
									getID.setString(1, values[1]);
									ResultSet rs = getID.executeQuery();
									if(rs.next())
									{
										int pubID =  rs.getInt(1);
										addRelation(add_rel_stat,pubID);
									}
									int pos = getAttributeIndex("Abstract", results); // abstract
									if(values[pos]!=null)
									{
										abstractavailable++;
									}
								}	
					
							}
						}
					} catch (SQLException e) {
					    e.printStackTrace();
					}
					setActuaPubs(getActuaPubs() + results.getResultSize() % 50);
					getProgress().setProgress((float)getActuaPubs()/(float) getNPubs());
					long nowTime2 = GregorianCalendar.getInstance().getTimeInMillis();
					long differTime2 = nowTime2-getStarttime();
					getProgress().setTime(differTime2, getActuaPubs(), getNPubs());
				 
				}
			}
			public void getted(int getted) {
				if(isCancel()==true)
				{
					ex.cancel(true);
					return;
				}
				setActuaPubs(getActuaPubs()+50);
				getProgress().setProgress((float)getActuaPubs()/(float) getNPubs());
				long nowTime = GregorianCalendar.getInstance().getTimeInMillis();
				long differTime = nowTime-getStarttime();
				getProgress().setTime(differTime, getActuaPubs(), getNPubs());
			}
		});
		return true;
	}
	
	protected void refreshQueryInfo(QueryInformationRetrievalExtension query){
		
	    if(query.getID()==-1)
	    	return;
	    
	    int morepubs = query.getDocumentMathing()+this.publicationAdded;
	    int moreabst = query.getAvailable_abstracts()+this.abstractavailable;
	    
	    
		Connection connection = getDatabase().getConnection();
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(QueriesIRProcess.updateQueryInfo);
			statement.setInt(1,morepubs);
			statement.setInt(2,moreabst);
			statement.setInt(3,query.getID());
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	public boolean searchUpdate(QueryInformationRetrievalExtension queryInfo)  
	{
		pmids = queryInfo.getPmids();
		queryInfo.getProperties().put("articleDetails","freefulltext");
		setKeywords(queryInfo.getKeyWords());
		setOrganism(queryInfo.getOrganism());
		setProperties(queryInfo.getProperties());
		
		this.getDatabase().openConnection();
		Connection connection = getDatabase().getConnection();
	
		if(connection == null)
		{
			this.getDatabase().closeConnection();
			return false;
		}
		
		String query = buildQuery();
		
		this.setActuaPubs(0);
		setNPubs(getQueryResults(query));
		
		if(getNPubs()==0)
		{
			this.publicationAdded=0;
			return false;
		}
		
		setStarttime(GregorianCalendar.getInstance().getTimeInMillis());	
        if(getNPubs() > MAX_RESULTS)
		{
        	setNPubs(totalPubs());
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
        this.getDatabase().closeConnection();
		return true;
	}
	
	protected boolean searchPubmed2(String q){
	    final PubMexAnote ex = new PubMexAnote();
		ex.doQuery(q, new PubMexProgressHandler(){

			
			public void cancelled() {
				return;
			}
			public void count(int count) {
				if(isCancel()==true)
				{
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
				
						PreparedStatement updt_rel_stat = getDatabase().getConnection().prepareStatement(QueriesPublication.updatePublicationPDFavailable);
						int pubs = 0;					
						for (; pubs<results.getResultSize(); pubs++) //result row
						{	
							String[] values = results.getResultAt(pubs);
							String pmid = values[1];
							if(pmids.contains(pmid))
							{				
							}
							else
							{	
								updt_rel_stat.setBoolean(1,true);
								updt_rel_stat.setString(2, pmid);
								updt_rel_stat.execute();
							}
						}
					} catch (SQLException e) {
					    e.printStackTrace();
					}
					setActuaPubs(getActuaPubs() + results.getResultSize() % 50);
					getProgress().setProgress((float)getActuaPubs()/(float) getNPubs());

					long nowTime2 = GregorianCalendar.getInstance().getTimeInMillis();
					long differTime2 = nowTime2-getStarttime();
					getProgress().setTime(differTime2, getActuaPubs(), getNPubs());
				 
				}
			}
			public void getted(int getted) {
				if(isCancel()==true)
				{
					ex.cancel(true);
					return;
				}
				setActuaPubs(getActuaPubs()+50);
				getProgress().setProgress((float)getActuaPubs()/(float) getNPubs());
				long nowTime = GregorianCalendar.getInstance().getTimeInMillis();
				long differTime = nowTime-getStarttime();
				getProgress().setTime(differTime, getActuaPubs(), getNPubs());
			}
		});
		return true;
	}

}
