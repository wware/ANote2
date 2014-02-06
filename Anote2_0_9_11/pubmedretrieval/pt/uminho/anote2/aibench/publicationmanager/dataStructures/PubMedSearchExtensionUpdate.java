package pt.uminho.anote2.aibench.publicationmanager.dataStructures;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.processes.ir.IIRSearchProcessReport;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesIRProcess;
import pt.uminho.anote2.datastructures.documents.publication.DocumentManager;
import pt.uminho.anote2.datastructures.documents.query.Query;
import pt.uminho.anote2.datastructures.report.processes.IRSearchReport;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.datastructures.utils.conf.OtherConfigurations;
import pt.uminho.anote2.process.IR.exception.InternetConnectionProblemException;
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
	private long startTime;
	
	public PubMedSearchExtensionUpdate(boolean cancel, TimeLeftProgress progress) {
		super(cancel, progress);
		this.pmids= new HashSet<String>();
		this.abstractavailable=0;
		this.publicationAdded=0;		
	}

	
	public IRSearchReport updateQuery(Query queryInfo) throws DatabaseLoadDriverException, SQLException, InternetConnectionProblemException  
	{
		setIdQuery(queryInfo.getID());
		if(!OtherConfigurations.getFreeFullTextOnly())
		{	
			return normalUpdate(queryInfo);
		}
		else
		{
			if(!queryInfo.getProperties().containsKey("articleDetails"))
			{
				return restrictedUpdate(queryInfo);
			}
			else 
			{
				String prop = queryInfo.getProperties().getProperty("articleDetails");
				if(prop.equals("freefulltext"))
				{
					setIsfreefulltextquery(true);
					return normalUpdate(queryInfo);
				}
				else
				{
					return restrictedUpdate(queryInfo);
				}
			}
		}
	}
	
	
	
	private IRSearchReport restrictedUpdate(Query queryInfo) throws DatabaseLoadDriverException, SQLException, InternetConnectionProblemException {
		startTime = GregorianCalendar.getInstance().getTimeInMillis();
		IRSearchReport report = new IRSearchReport(queryInfo.getKeyWords(), queryInfo.getOrganism());
		pmids = queryInfo.getPmids();
		Properties prop = queryInfo.getProperties();
		setKeywords(queryInfo.getKeyWords());
		setOrganism(queryInfo.getOrganism());
		setProperties(queryInfo.getProperties());
		String query = buildQuery();
		int search1 = getQueryResults(query);
		prop.put("articleDetails","freefulltext");
		setProperties(prop);
		query = buildQuery();
		int search2 = getQueryResults(query);
		setNPubs(search1 + search2);
		prop.remove("articleDetails");
		query = buildQuery();
		searchMethod(queryInfo,report,query);
		searchFreeFullTextMethod(prop);
		return report;
	}


	private IRSearchReport normalUpdate(Query queryInfo) throws DatabaseLoadDriverException, SQLException, InternetConnectionProblemException {
		startTime = GregorianCalendar.getInstance().getTimeInMillis();
		IRSearchReport report = new IRSearchReport(queryInfo.getKeyWords(), queryInfo.getOrganism());
		pmids = queryInfo.getPmids();
		setKeywords(queryInfo.getKeyWords());
		setOrganism(queryInfo.getOrganism());
		setProperties(queryInfo.getProperties());		
		setIdQuery(queryInfo.getID());
		String query = buildQuery();		
		setNPubs(getQueryResults(query));
		searchMethod(queryInfo,report,query);
		return report;
	}
	
	protected IIRSearchProcessReport searchMethod(Query queryInfo,
			IIRSearchProcessReport report, String query) throws SQLException, DatabaseLoadDriverException {
		boolean correct = true;	
		HashMap<String,Integer>	tableMemberSize = new HashMap<String, Integer>();
		tableMemberSize = getDbColumnsInfo(Configuration.getDatabase().getConnection());
        if(getNPubs() > MAX_RESULTS)
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
        	refreshQueryInfo(queryInfo);
        }
        if(!correct)
        	report.setcancel();
		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		report.setTime(endTime-startTime);
		return report;
	}
	
	
	
	public boolean searchPubmed(String q,final HashMap<String,Integer> tableMemberSize,final IIRSearchProcessReport report){
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
						/** Linked list with the already existing pmids in database. */
						Map<String, Integer> pmidsAlreadyExistOnDB = DocumentManager.getAllPublicationsOtherIDs(getIdTypePmid());
						PreparedStatement add_pub_stat = Configuration.getDatabase().getConnection().prepareStatement(QueriesPublication.insertPublication);
						PreparedStatement add_rel_stat = Configuration.getDatabase().getConnection().prepareStatement(QueriesPublication.insertPublicationOnQuery);
						int pubs = 0;
						
						for (; pubs<results.getResultSize(); pubs++) //result row
						{	
							// Se ja tiver o pmid nao faz nd
							String[] values = results.getResultAt(pubs);
							if(pmids.contains(values[1]))
							{

							}
							else
							{	
								if(!pmidsAlreadyExistOnDB.containsKey(values[1]))
								{
									int pubID =  HelpDatabase.getNextInsertTableID(GlobalTablesName.publications);
									addPubToDB(add_pub_stat, results, pubs, true,tableMemberSize); //adds the publication to the database		
									addRelation(add_rel_stat,pubID);
									publicationAdded++;
									report.incrementDocumentRetrieval(1);
									int pos = getAttributeIndex("Abstract", results); // abstract
									if(values[pos]!=null)
									{
										abstractavailable++;
									}
								}
								else
								{
									int pubID =  pmidsAlreadyExistOnDB.get(values[1]);
									addRelation(add_rel_stat,pubID);
									publicationAdded++;
									report.incrementDocumentRetrieval(1);
									int pos = getAttributeIndex("Abstract", results); // abstract
									if(values[pos]!=null && values[pos].length()>0)
									{
										abstractavailable++;
									}
								}	
							}
						}
						add_pub_stat.close();
						add_rel_stat.close();
					} catch (SQLException e) {
					    e.printStackTrace();
					} catch (DatabaseLoadDriverException e) {
						e.printStackTrace();
					}
					setActuaPubs(getActuaPubs() + results.getResultSize() % 50);
					getProgress().setProgress((float)getActuaPubs()/(float) getNPubs());
					long nowTime2 = GregorianCalendar.getInstance().getTimeInMillis();
					long differTime2 = nowTime2-startTime;
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
				long differTime = nowTime-startTime;
				getProgress().setTime(differTime, getActuaPubs(), getNPubs());
			}
		});
		return true;
	}
	
	protected void refreshQueryInfo(Query queryInfo) throws DatabaseLoadDriverException, SQLException{

		if(queryInfo.getID()==-1)
			return;	    
		int morepubs = queryInfo.getDocumentMathing()+this.publicationAdded;
		int moreabst = queryInfo.getAvailable_abstracts()+this.abstractavailable;
		Connection connection = Configuration.getDatabase().getConnection();
		PreparedStatement statement;
		statement = connection.prepareStatement(QueriesIRProcess.updateQueryInfo);
		statement.setInt(1,morepubs);
		statement.setInt(2,moreabst);
		statement.setString(3,Utils.currentTime());
		statement.setInt(4,queryInfo.getID());
		statement.execute();
		statement.close();
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
				
						PreparedStatement updt_rel_stat = Configuration.getDatabase().getConnection().prepareStatement(QueriesPublication.updatePublicationPDFavailable);
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
								updt_rel_stat.setNString(2, pmid);
								updt_rel_stat.setInt(3, getIdTypePmid());
								updt_rel_stat.execute();
							}
						}
					} catch (SQLException e) {
					    e.printStackTrace();
					} catch (DatabaseLoadDriverException e) {
						e.printStackTrace();
					}
					setActuaPubs(getActuaPubs() + results.getResultSize() % 50);
					getProgress().setProgress((float)getActuaPubs()/(float) getNPubs());

					long nowTime2 = GregorianCalendar.getInstance().getTimeInMillis();
					long differTime2 = nowTime2-startTime;
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
				long differTime = nowTime-startTime;
				getProgress().setTime(differTime, getActuaPubs(), getNPubs());
			}
		});
		return true;
	}

}
