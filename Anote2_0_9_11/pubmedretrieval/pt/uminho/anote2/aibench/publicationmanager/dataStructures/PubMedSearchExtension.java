package pt.uminho.anote2.aibench.publicationmanager.dataStructures;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.utils.timeleft.ISimpleTimeLeft;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.processes.ir.IIRSearchProcessReport;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;
import pt.uminho.anote2.datastructures.documents.publication.DocumentManager;
import pt.uminho.anote2.datastructures.documents.query.QueryManager;
import pt.uminho.anote2.datastructures.report.processes.IRSearchReport;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.datastructures.utils.conf.OtherConfigurations;
import pt.uminho.anote2.process.IR.exception.InternetConnectionProblemException;
import pt.uminho.anote2.process.ir.pubmed.PubMedSearch;
import pt.uminho.anote2.process.ir.pubmed.PubMexAnote;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.sing.BioSearch.connection.PubMexProgressHandler;
import es.uvigo.ei.sing.BioSearch.datatypes.CollectionNCBI;

/**
 * Aibench Extension of PubMedSearch.
 * This class serves to implements in PubMedSearch some visual details for PubMed Search
 * 
 * @author Hugo Costa
 *
 */
public class PubMedSearchExtension extends PubMedSearch{

	private boolean cancel;
	private ISimpleTimeLeft progress;
	private int nPubs;
	private int actuaPubs;
	private boolean isfreefulltextquery = false;
	private boolean viewPrelimiraryInfo = true;
	private long starttime;
	
	public PubMedSearchExtension(boolean cancel,ISimpleTimeLeft progress) {
		super();
		this.cancel=cancel;
		this.progress=progress;
	}
	
	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}
	

	@Override
	public IIRSearchProcessReport search(String keywords,String organism, Properties properties) throws DatabaseLoadDriverException, SQLException, InternetConnectionProblemException  
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
	
	private IIRSearchProcessReport restrictedversionSearch(String keywords, String organism, Properties properties) throws DatabaseLoadDriverException, SQLException, InternetConnectionProblemException {
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		IIRSearchProcessReport report = new IRSearchReport(keywords, organism);
		String query;
		setKeywords(keywords);
		setOrganism(organism);
		setProperties(properties);	
		query = buildQuery();
		int search1 = getQueryResults(query);		
		if(viewPrelimiraryInfo)
		{
			if(stopQuestion(search1))
			{
				report.setcancel();
				return report;
			}
		}
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



	private IIRSearchProcessReport normalSearch(String keywords,String organism, Properties properties) throws DatabaseLoadDriverException, SQLException, InternetConnectionProblemException {
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		IIRSearchProcessReport report = new IRSearchReport(keywords, organism);
		String query;
		setKeywords(keywords);
		setOrganism(organism);
		setProperties(properties);	
		query = buildQuery();
		nPubs = getQueryResults(query);		
		if(viewPrelimiraryInfo)
		{
			if(stopQuestion(nPubs))
			{
				report.setcancel();
				return report;
			}
		}
		return searchMethod(startTime, report, query);
	}



	protected IIRSearchProcessReport searchMethod(long startTime,
			IIRSearchProcessReport report, String query) throws DatabaseLoadDriverException, SQLException {
		boolean correct = true;
		int queryID = QueryManager.insertQuery(getKeywords(), getOrganism(), 0, 0, getProperties(), true);
		setIdQuery(queryID);		
		HashMap<String,Integer>	tableMemberSize = new HashMap<String, Integer>();
		tableMemberSize = getDbColumnsInfo(Configuration.getDatabase().getConnection());
		starttime = GregorianCalendar.getInstance().getTimeInMillis();	
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
	
	public void setProgress(TimeLeftProgress progress) {
		this.progress = progress;
	}

	public void setNPubs(int pubs) {
		nPubs = pubs;
	}

	public void setStarttime(long starttime) {
		this.starttime = starttime;
	}

	public ISimpleTimeLeft getProgress() {
		return progress;
	}

	public int getNPubs() {
		return nPubs;
	}

	public long getStarttime() {
		return starttime;
	}

	public boolean searchPubmed(String q,final HashMap<String,Integer> tableMemberSize,final IIRSearchProcessReport report){
	    final PubMexAnote ex = new PubMexAnote();
		ex.doQuery(q, new PubMexProgressHandler(){

			public void cancelled() {
				report.setcancel();
				ex.cancel(true);
				return;
			}
			public void count(int count) {
				if(cancel)
				{
					if(getID()!=-1)
					{
						report.setcancel();
						ex.cancel(true);
						try {
							PublicationManager.removeQuery(getID());
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (DatabaseLoadDriverException e) {
							e.printStackTrace();
						}
					}
					return ;
				}
			}
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
						Map<String, Integer> pmidsAlreadyExistOnDB = DocumentManager.getAllPublicationsOtherIDs(getIdTypePmid());

						PreparedStatement add_pub_stat = Configuration.getDatabase().getConnection().prepareStatement(QueriesPublication.insertPublication);
						PreparedStatement add_rel_stat = Configuration.getDatabase().getConnection().prepareStatement(QueriesPublication.insertPublicationOnQuery);
						PreparedStatement update_pub_pdf = Configuration.getDatabase().getConnection().prepareStatement(QueriesPublication.updatePublicationPDFavailable);
						int pubs = 0;
						report.incrementDocumentRetrieval(results.getResultSize());
						for (; pubs<results.getResultSize()&&!cancel; pubs++) //result row
						{	
							String[] values = results.getResultAt(pubs);

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
							if(isfreefulltextquery)
							{
								update_pub_pdf.setBoolean(1,true);
								update_pub_pdf.setNString(2,values[1]);
								update_pub_pdf.setInt(3, getIdTypePmid());
								update_pub_pdf.executeUpdate();;
							}						
						}
						addToCounts(pubs, abs_count);
						add_pub_stat.close();
						add_rel_stat.close();
						update_pub_pdf.close();
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (DatabaseLoadDriverException e) {
						e.printStackTrace();
					}
					actuaPubs = actuaPubs + results.getResultSize() % 50;
					progress.setProgress((float)actuaPubs/(float) nPubs);

					long nowTime2 = GregorianCalendar.getInstance().getTimeInMillis();
					long differTime2 = nowTime2-starttime;
					progress.setTime(differTime2, actuaPubs, nPubs);
				 
				}
			}
			public void getted(int getted) {
				if(cancel)
				{
					ex.cancel(true);
					try {
						PublicationManager.removeQuery(getID());
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (DatabaseLoadDriverException e) {
						e.printStackTrace();
					}
					return;
				}
				actuaPubs=actuaPubs+50;
				progress.setProgress((float)actuaPubs/(float) nPubs);
				long nowTime = GregorianCalendar.getInstance().getTimeInMillis();
				long differTime = nowTime-starttime;
				progress.setTime(differTime, actuaPubs, nPubs);
			}
		});
		return !cancel;
	}
	
	private boolean stopQuestion(int nPubs){
		Object[] options = new String[]{"Stop", "Continue"};
		int opt = showOptionPane("PubMed Results", "The search for:\n" + buildQuery() + "\n contains " + nPubs + " results.", options);
		switch (opt) {
		case 0:
			return true;
		default:
			return false;
		}
	}
	
	/** Presents a option pane with the given title, question and options */
	public static int showOptionPane(String title, String question, Object[] options){
		JOptionPane option_pane = new JOptionPane(question);
		//option_pane.setIcon(new ImageIcon(getClass().getClassLoader().getResource("messagebox_question.png")));
		option_pane.setOptions(options);
		
		JDialog dialog = option_pane.createDialog(Workbench.getInstance().getMainFrame(), title);
		dialog.setVisible(true);
		
		Object choice = option_pane.getValue();
					
		for(int i=0; i<options.length; i++)
			if(options[i].equals(choice))
				return i;
		
		return -1;		
	}
	
	public boolean searchAddProperty(String keywords,String organism, Properties properties)  
	{
		setKeywords(keywords);
		setOrganism(organism);
		setProperties(properties);
		return searchFreeFullTextMethod(properties);
	}



	protected boolean searchFreeFullTextMethod(Properties properties) {
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
							updt_rel_stat.setInt(3,getIdTypePmid());
							updt_rel_stat.executeUpdate();
						}
						updt_rel_stat.close();
					} catch (SQLException e) {
					    e.printStackTrace();
					} catch (DatabaseLoadDriverException e) {
						e.printStackTrace();
					}
					actuaPubs = actuaPubs + results.getResultSize() % 50;
					progress.setProgress((float)actuaPubs/(float) nPubs);
					long nowTime2 = GregorianCalendar.getInstance().getTimeInMillis();
					long differTime2 = nowTime2-starttime;
					progress.setTime(differTime2, actuaPubs, nPubs);
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
							e.printStackTrace();
						} catch (DatabaseLoadDriverException e) {
							e.printStackTrace();
						}
					}
					return;
				}
				actuaPubs=actuaPubs+50;
				progress.setProgress((float)actuaPubs/(float) nPubs);
				long nowTime = GregorianCalendar.getInstance().getTimeInMillis();
				long differTime = nowTime-starttime;
				progress.setTime(differTime, actuaPubs, nPubs);
			}
		});
		return !cancel;
	}
	
	public boolean isIsfreefulltextquery() {
		return isfreefulltextquery;
	}

	public void setIsfreefulltextquery(boolean isfreefulltextquery) {
		this.isfreefulltextquery = isfreefulltextquery;
	}

	public void setViewPrelimiraryInfo(boolean viewPrelimiraryInfo) {
		this.viewPrelimiraryInfo = viewPrelimiraryInfo;
	}

	public int getActuaPubs() {
		return actuaPubs;
	}

	public void setActuaPubs(int actuaPubs) {
		this.actuaPubs = actuaPubs;
	}

	
}
