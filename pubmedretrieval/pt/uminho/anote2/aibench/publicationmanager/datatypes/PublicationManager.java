package pt.uminho.anote2.aibench.publicationmanager.datatypes;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.configuration.IProxy;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesProcess;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.ListElements;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
/**
 * Aibench Datatype
 * 
 * This class serves to manage publication groping by queries.
 * 
 * @author Hugo Costa
 *
 */
@Datatype(structure = Structure.LIST,namingMethod="getName")
public class PublicationManager extends Observable implements Serializable {
	
	private static final long serialVersionUID = -6847950783302249246L;
	/**
	 * Database credentials
	 */
	private IDatabase db;
	/**
	 * Proxy Settings
	 */
	private IProxy proxy;
	
	/**
	 * List Of Queries on Clipboard
	 */
	private List<QueryInformationRetrievalExtension> queries;
	
	public static String saveDocs = "Docs/";
	public static boolean fullVersion = true; // true for us
											  // false for other
	
	public static int searchYearStarting = 1900;
	
	public PublicationManager(IProxy proxy,IDatabase db) {
		this.db = db;
		this.proxy = proxy;
		queries=new ArrayList<QueryInformationRetrievalExtension>();
	}

	public void notifyViewObserver(){
		
		setChanged();
		notifyObservers();
	}
	
	/**
	 * This MEthod return a list of Queries present on Clipboard ( Aibench )
	 * 
	 * @return
	 */
	@ListElements(modifiable=true)
	public List<QueryInformationRetrievalExtension> getQueries()
	{
		return this.queries;
	}
	
	/**
	 * This MEthod that add an Query to Clipboard ( Aibench )
	 * 
	 * @return
	 */
	public void addQueryInformationRetrievalExtension(QueryInformationRetrievalExtension query)
	{
		if(compare(query))
		{
			new ShowMessagePopup("Query Already Exist on Clipboard");
		}
		else
		{
			this.queries.add(query);
			new ShowMessagePopup("Query Added");
			this.notifyViewObserver();
		}
	}
	
	public boolean compare(QueryInformationRetrievalExtension query)
	{
		for(QueryInformationRetrievalExtension q:queries)
		{
			if(q.getID()==query.getID())
			{
				return true;
			}
				
		}
		return false;
	}
	
	public static boolean removeQuery(IDatabase db,int queryID) throws SQLException
	{
		PreparedStatement removeQuery = db.getConnection().prepareStatement(QueriesProcess.removeQuery);
		PreparedStatement removePublicationsQuery = db.getConnection().prepareStatement(QueriesProcess.removeQueryPublicationLinking);
		PreparedStatement removeQueryProperties = db.getConnection().prepareStatement(QueriesProcess.removeQueryProperties);
		removeQuery.setInt(1,queryID);
		removePublicationsQuery.setInt(1,queryID);
		removeQueryProperties.setInt(1,queryID);
		removePublicationsQuery.execute();
		removeQueryProperties.execute();
		removeQuery.execute();
		return true;
	}

	public String getName()
	{
		return "Publication Manager";
	}

	public IDatabase getDb() {
		return db;
	}

	public void setDb(IDatabase db) {
		this.db = db;
	}

	public IProxy getProxy() {
		return proxy;
	}

	public void setProxy(IProxy proxy) {
		this.proxy = proxy;
	}

}
