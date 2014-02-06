package pt.uminho.anote2.aibench.publicationmanager.datatypes;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.listeners.IChangeRelevancePublicationsListener;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.listeners.IChangeSelectionPublicationsListener;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.core.document.RelevanceType;
import pt.uminho.anote2.datastructures.documents.query.Query;
import pt.uminho.anote2.datastructures.documents.query.QueryManager;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import es.uvigo.ei.aibench.workbench.Workbench;
/**
 * Aibench Datatype that represent a Query Search in IIRSearch
 * 
 * @author Hugo Costa
 *
 */
@Datatype(structure = Structure.SIMPLE,namingMethod="getQueryName",setNameMethod="setQueryName",viewable=true,removable=true,renamed=true,autoOpen=true)
public class QueryInformationRetrievalExtension extends Query{


	private static final long serialVersionUID = 1L;
	private PublicationManager pubManager;
	private Set<Integer> selectedPublications;
	private boolean first = true;
	private Map<Integer,IChangeSelectionPublicationsListener> selectPublicationsListener;
	private Map<Integer,IChangeRelevancePublicationsListener> selectRelevanceListener;

	public QueryInformationRetrievalExtension(
			int idqueries,String name,Date date,String keywords,String organism,		
			int matching_publications, int available_abstracts,int downloaded_publications,
			boolean fromPubmedSearch,Properties properties,
			PublicationManager pubManager) {
		super(idqueries,name, date, keywords,organism,
				matching_publications, available_abstracts,downloaded_publications,fromPubmedSearch,properties);
		this.pubManager=pubManager;
		this.selectPublicationsListener = new HashMap<Integer, IChangeSelectionPublicationsListener>();
		this.selectRelevanceListener = new HashMap<Integer, IChangeRelevancePublicationsListener>();
		this.selectedPublications = new HashSet<Integer>();
		
	}
	
	public void registerIChangeSelectionPublicationsListener(int listerID,IChangeSelectionPublicationsListener lister)
	{
		this.selectPublicationsListener.put(listerID, lister);
	}
	
	public void registerIChangeRelevancePublicationsListener(int listerID,IChangeRelevancePublicationsListener lister)
	{
		this.selectRelevanceListener.put(listerID, lister);
	}
	
	public PublicationManager getPubManager() {
		return pubManager;
	}


	public void setPubManager(PublicationManager pubManager) {
		this.pubManager = pubManager;
	}
	
	public String getQueryName() throws SQLException, DatabaseLoadDriverException
	{
		return getName();
	}


	
	public void setQueryName(String name) throws SQLException, DatabaseLoadDriverException
	{
		if(name.equals(getName()))
		{
			
		}
		else if(name==null  || name.length() == 0)
		{
			this.setChanged();
			this.notifyObservers();
			Workbench.getInstance().warn("Query Name can not be empty");
		}
		else
		{
			super.setName(name);
			super.setNameINDatabase(name);
			getPubManager().notifyViewObserver();
		}
	}
	
	@Override
	public List<IPublication> getPublications() throws SQLException, DatabaseLoadDriverException  {
		List<IPublication> publications = super.getPublications();	
		if(first)
		{
			selectedPublications = new HashSet<Integer>();
			for(IPublication pub: publications)
			{
				selectedPublications.add(pub.getID());
			}
			first = false;
		}
		return publications;
	}

	public void addPublicationToSelctedPublications(int publicationID)
	{
		this.selectedPublications.add(publicationID);
	}
	
	public void updateAllSelectionListeners(int viewID) {
		for(int listenerID : this.selectPublicationsListener.keySet())
		{
			IChangeSelectionPublicationsListener listener = this.selectPublicationsListener.get(listenerID);
			listener.updateSelectionPublication(viewID);
		}
	}
	
	public void updateAllRelevanceListeners() {
		for(int listenerID : this.selectRelevanceListener.keySet())
		{
			IChangeRelevancePublicationsListener listener = this.selectRelevanceListener.get(listenerID);
			listener.changeRelevanceListner();
		}
	}

	public void removePublicationToSelctedPublications(int publicationID)
	{
		this.selectedPublications.remove(publicationID);
	}
	
	public boolean publicationINSelectingPublication(int publicationID)
	{
		if(this.selectedPublications.contains(publicationID))
			return true;
		return false;
	}

	public void setSelectingPublications(Set<Integer> selectedPublications) {
		if(selectedPublications!=null)
		{
			this.selectedPublications = selectedPublications;
		}
	}

	public void addProperty(String propertyName,String propertyValue) throws SQLException, DatabaseLoadDriverException
	{
		if(!getProperties().containsKey(propertyName))
		{
			QueryManager.addQueryProperty(getID(), propertyName, propertyValue);
			this.getProperties().put(propertyName, propertyValue);
		}
	}

	public void updateRelevance(IPublication publication, RelevanceType newRelevance) throws SQLException, DatabaseLoadDriverException
	{
		super.updateRelevance(publication, newRelevance);
		updateAllRelevanceListeners();
	}
	

	public void setFirst() {
		this.first = true;
	}


	
}
