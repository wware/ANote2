package pt.uminho.anote2.datastructures.resources.ontology;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.resources.ResourceElementSet;
import pt.uminho.anote2.datastructures.resources.ResourcesHelp;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;
import pt.uminho.anote2.resource.ontologies.IOntology;

public class Ontology extends ResourcesHelp implements IOntology{

	public Ontology(IDatabase db, int id, String name, String info) {
		super(db, id, name, info);
	}
	
	public int compareTo(IOntology dic)
	{
		if(this.getId()==dic.getId())
		{
			return 0;
		}
		else if(this.getId()<=dic.getId())
		{
			return -1;
		}
		return 1;
	}
	
	public String toString()
	{
		String info = new String();
		info = info + getId() + "-" + getName() + " (" + getType() + ") " + "Note : " + getInfo();
		return info;
	}

	public String getType() {
		return "Ontology";
	}

	public boolean isOntologyFill() {
		PreparedStatement ps;
		try {
			ps = getDb().getConnection().prepareStatement(QueriesResources.selectResourceElementCount);
			ps.setInt(1,getId());
			ResultSet rs = ps.executeQuery();
			rs.next();
			if(rs.getInt(1)>0)
			{
				return true;
			}
			else
			{
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public IResourceElementSet<IResourceElement> getTermSynomns(int termID) {
		Connection connection = getDb().getConnection();
		if(connection==null){return null;}		
		IResourceElementSet<IResourceElement> res = new ResourceElementSet<IResourceElement>(); 
		try {
			
			PreparedStatement getTermSynPS = connection.prepareStatement(QueriesResources.selectElementSynByID);
			getTermSynPS.setInt(1,termID);
			ResultSet rs = getTermSynPS.executeQuery();
			while(rs.next())
			{
				IResourceElement elem = new ResourceElement(rs.getInt(1),rs.getString(2),-1,"");
				res.addElementResource(elem);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

}
