package pt.uminho.anote2.datastructures.resources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;

public class ResourcesManagement {
	
	
	
	public static int newResource(IDatabase db,String name,String info,String resourceType) 
	{	

		Connection connection = db.getConnection();
		if(connection == null)
		{
			return -1;			
		}
		else
		{
			try {
			PreparedStatement stat = connection.prepareStatement(QueriesResources.insertResource);
			int resourceTypeID = addResourceType(db,resourceType);
			stat.setString(1,name);
			stat.setInt(2,resourceTypeID);
			stat.setString(3,info);
			stat.execute();
			return HelpDatabase.getNextInsertTableID(db, "resources")-1;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return -2;
	}
	
	
	protected static int addResourceType(IDatabase db, String resourceType)
	{
		Connection connection = db.getConnection();
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
				return HelpDatabase.getNextInsertTableID(db, "resources_type")-1;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return -1;	
	}
	
	public static int addSource(IDatabase db,String source) {
		Connection connection = db.getConnection();
		if(connection == null)
		{
			return -1;
		}
		else
		{
			try {
			PreparedStatement idsourcePS = connection.prepareStatement(QueriesResources.selectSourcesIDByName);
			idsourcePS.setString(1,source);
			ResultSet rs = idsourcePS.executeQuery();
			if(rs.next()){ return rs.getInt(1);}
			PreparedStatement insertSourcePS = connection.prepareStatement(QueriesResources.insertSource);
			insertSourcePS.setString(1,source);
			insertSourcePS.execute();
			return HelpDatabase.getNextInsertTableID(db,"sources")-1;				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}


	public static int addRelationType(IDatabase db, String string) {
		Connection connection = db.getConnection();
		if(connection == null)
		{
			return -1;
		}
		else
		{
			try {
				PreparedStatement relationTypePS = connection.prepareStatement(QueriesResources.insertRelationType);
				relationTypePS.setString(1,string);
				relationTypePS.execute();
			} catch (SQLException e) {			
				e.printStackTrace();
				return -1;
			}
			return HelpDatabase.getNextInsertTableID(db, "relation_type")-1;
		}
	}

}
