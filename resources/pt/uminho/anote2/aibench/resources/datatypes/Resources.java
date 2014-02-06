package pt.uminho.anote2.aibench.resources.datatypes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pt.uminho.anote2.core.configuration.IProxy;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import es.uvigo.ei.aibench.core.datatypes.annotation.Clipboard;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;

@Datatype(structure = Structure.COMPLEX,viewable=false,namingMethod="getName",removable=true)
public class Resources{
	
	private IDatabase db;
	private IProxy proxy;
	
	private Dictionaries dictionaries;
	private LookupTables lookuptables;
	private RulesSet rules;
	private Ontologies ontologies;

	public Resources(IDatabase db,IProxy proxy)
	{
		this.db=db;
		this.proxy=proxy;
		this.dictionaries=new Dictionaries(this);
		this.lookuptables=new LookupTables(this);
		this.rules=new RulesSet(this);
		this.setOntologies(new Ontologies(this));
	}

	@Clipboard(name="Dictionaries",order=1)
	public Dictionaries getDictionaries() {
		return dictionaries;
	}

	@Clipboard(name="Lookup Tables",order=2)
	public LookupTables getLookuptables() {
		return lookuptables;
	}
	
	@Clipboard(name="Rules",order=3)
	public RulesSet getRules() {
		return rules;
	}
	
	@Clipboard(name="Ontologies",order=4)
	public Ontologies getOntologies() {
		return ontologies;
	}
	
	public String getName()
	{
		return "Resources";
	}
	
	public ResultSet getResourceFielsByType(String type)
	{
		PreparedStatement stat;
		try {
			stat = getDb().getConnection().prepareStatement(QueriesResources.selectResourceFilterByType);
			stat.setString(1,type);
			ResultSet rs = stat.executeQuery();
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public boolean newResource(String name,String info,String resourceType) 
	{	

		Connection connection = getDb().getConnection();
		if(connection == null)
		{
			return false;			
		}
		else
		{
			try {
			PreparedStatement stat = connection.prepareStatement(QueriesResources.insertResource);
			int resourceTypeID = addResourceType(resourceType);
			stat.setString(1,name);
			stat.setInt(2,resourceTypeID);
			stat.setString(3,info);
			stat.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	
	public boolean removeResource(int resourceID) 
	{	
		return false;
	}
	
	private int addResourceType(String resourceType)
	{
		
		Connection connection = this.db.getConnection();
		if(connection == null)
		{		
			return -1;			
		}
		else
		{
			try {
				
				PreparedStatement stat = connection.prepareStatement(QueriesResources.selectResourceTypes);
				stat.setString(1,resourceType);
				ResultSet rs = stat.executeQuery();
				if(rs.next())
				{
					return rs.getInt(1);
				}
				PreparedStatement stat2 = connection.prepareStatement(QueriesResources.insertResourceType);
				stat2.setString(1,resourceType);
				stat2.execute();
				return HelpDatabase.getNextInsertTableID(getDb(), "resources_type")-1;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return -1;	
	}
	
	public IDatabase getDb() {
		return db;
	}

	public IProxy getProxy() {
		return proxy;
	}
	
	public void setDb(IDatabase db) {
		this.db = db;
	}

	public void setProxy(IProxy proxy) {
		this.proxy = proxy;
	}

	public void setOntologies(Ontologies ontologies) {
		this.ontologies = ontologies;
	}

}
