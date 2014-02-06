package pt.uminho.anote2.datastructures.resources.ontology;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.resources.ResourcesHelp;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.resource.ontologies.IOntology;

public class Ontology extends ResourcesHelp implements IOntology{

	public Ontology(int id, String name, String info) {
		super(id, name, info);
	}
	
	public Ontology(int id, String name, String info,boolean active) {
		super(id, name, info,active);
	}
	
	public int compareTo(IOntology dic)
	{
		if(this.getID()==dic.getID())
		{
			return 0;
		}
		else if(this.getID()<=dic.getID())
		{
			return -1;
		}
		return 1;
	}
	
	public String toString()
	{
		String info = new String();
		info ="Ontology : " + getName() + " (ID :"+ getID() + " ) ";
		if(!getInfo().equals(""))
		{
			info = info + "Notes: "+getInfo();
		}
		if(!isActive())
		{
			info = info + " (Inactive) ";
		}
		return info;
	}

	public String getType() {
		return GlobalOptions.resourcesOntologyName;
	}

	public boolean isOntologyFill() throws SQLException, DatabaseLoadDriverException {
		PreparedStatement ps;
			ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.selectResourceElementCount);
			ps.setInt(1,getID());
			ResultSet rs = ps.executeQuery();
			rs.next();
			if(rs.getInt(1)>0)
			{
				rs.close();
				ps.close();
				return true;
			}
			else
			{
				rs.close();
				ps.close();
				return false;
			}
	}
	
}
