package pt.uminho.anote2.aibench.publicationmanager.dataStructures;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.publicationmanager.exeptions.PubmedException;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.operations.TimeLeftProgress;
import pt.uminho.anote2.core.configuration.IProxy;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;
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
	private TimeLeftProgress progress;
	private int nPubs;
	private int actuaPubs;
	private boolean isfreefulltextquery = false;
	
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

	private long starttime;
	
	public PubMedSearchExtension(IProxy proxy, IDatabase database,boolean cancel,TimeLeftProgress progress) {
		super(proxy, database);
		this.cancel=cancel;
		this.progress=progress;
	}
	
	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	public boolean search(String keywords,String organism, Properties properties)  
	{
		boolean correct = true;
		String query;
		setKeywords(keywords);
		setOrganism(organism);
		setProperties(properties);
		
		this.getDatabase().openConnection();
		Connection connection = getDatabase().getConnection();
	
		if(connection == null)
		{
			this.getDatabase().closeConnection();
			return false;
		}
		query = buildQuery();			
		nPubs = getQueryResults(query);		
	    if(stopQuestion(nPubs))
	    {
	    	new ShowMessagePopup("Query Cancel");
            return false;
	    }
		setIdQuery(insertQuery(connection));	
		if(getIdQuery()==-1)
		{
			try {
				throw new PubmedException("No results for the query: \"" + query + "\"!");
			} catch (PubmedException e) {
				return false;
			}
		}	
		HashMap<String,Integer>	tableMemberSize = new HashMap<String, Integer>();
		try {
			tableMemberSize = getDbColumnsInfo(connection);
		} catch (SQLException e1) {
			e1.printStackTrace();
			return false;
		}
		starttime = GregorianCalendar.getInstance().getTimeInMillis();	
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
				correct = searchPubmed(query,tableMemberSize);
				year++;
		    }
		}
		else
		{
			correct = searchPubmed(query,tableMemberSize);
		}
        if(correct)
        {
        	refreshQueryInfo();
        }
		return correct;
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

	public TimeLeftProgress getProgress() {
		return progress;
	}

	public int getNPubs() {
		return nPubs;
	}

	public long getStarttime() {
		return starttime;
	}

	protected boolean searchPubmed(String q,final HashMap<String,Integer> tableMemberSize){
	    final PubMexAnote ex = new PubMexAnote();
		ex.doQuery(q, new PubMexProgressHandler(){

			public void cancelled() {
				ex.cancel(true);
				return;
			}
			public void count(int count) {
				if(cancel)
				{
					if(getID()!=-1)
					{
						ex.cancel(true);
						try {
							PublicationManager.removeQuery(getDatabase(),getID());
						} catch (SQLException e) {
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
					/** Linked list with the already existing pmids in database. */
					LinkedList<String> pmidsAlreadyExistOnDB = getPublications(getDatabase().getConnection());
			
					try {
						PreparedStatement add_pub_stat = getDatabase().getConnection().prepareStatement(QueriesPublication.insertPublications);
						PreparedStatement add_rel_stat = getDatabase().getConnection().prepareStatement(QueriesPublication.insertPublicationOnQuery);
						PreparedStatement updt_rel_stat = getDatabase().getConnection().prepareStatement(QueriesPublication.updatePublication);
						PreparedStatement getID = getDatabase().getConnection().prepareStatement(QueriesPublication.selectPublicationIDForPublicationOtherID);
						PreparedStatement update_pub_pdf = getDatabase().getConnection().prepareStatement(QueriesPublication.updatePublicationPDFavailable);
						int pubs = 0;
						for (; pubs<results.getResultSize()&&!cancel; pubs++) //result row
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
									else
									{
										
									}
								}
								if(isfreefulltextquery)
								{
									update_pub_pdf.setBoolean(1,true);
									update_pub_pdf.setString(2,values[1]);
									update_pub_pdf.executeUpdate();;
								}						
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					addToCounts(pubs, abs_count);
					} catch (SQLException e) {
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
						PublicationManager.removeQuery(getDatabase(),getID());
					} catch (SQLException e) {
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
		properties.put("articleDetails","freefulltext");
		String query;
		setKeywords(keywords);
		setOrganism(organism);
		setProperties(properties);
		
		this.getDatabase().openConnection();
		Connection connection = getDatabase().getConnection();
	
		if(connection == null)
		{
			this.getDatabase().closeConnection();
			return false;
		}
		query = buildQuery();
		actuaPubs=0;
		nPubs = getQueryResults(query);
		
		if(nPubs==0)
		{
			
		}
		
		starttime = GregorianCalendar.getInstance().getTimeInMillis();	
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
        this.getDatabase().closeConnection();
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
							PublicationManager.removeQuery(getDatabase(),getID());
						} catch (SQLException e) {
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
						PreparedStatement updt_rel_stat = getDatabase().getConnection().prepareStatement(QueriesPublication.updatePublicationPDFavailable);					
						int pubs = 0;
						for (; pubs<results.getResultSize(); pubs++) //result row
						{	
							String[] values = results.getResultAt(pubs);
							String pmid = values[1];				
							updt_rel_stat.setBoolean(1,true);
							updt_rel_stat.setString(2,pmid);
							updt_rel_stat.executeUpdate();
						}
					} catch (SQLException e) {
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
							PublicationManager.removeQuery(getDatabase(),getID());
						} catch (SQLException e) {
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
	
}
