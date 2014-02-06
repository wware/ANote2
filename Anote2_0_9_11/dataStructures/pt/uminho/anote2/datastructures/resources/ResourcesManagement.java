package pt.uminho.anote2.datastructures.resources;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;

public class ResourcesManagement {
	
	
	
	public static int newResource(String name,String info,String resourceType) throws SQLException, DatabaseLoadDriverException 
	{	
			PreparedStatement stat = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.insertResource);
			int resourceTypeID = addResourceType(resourceType);
			stat.setNString(1,name);
			stat.setInt(2,resourceTypeID);
			stat.setNString(3,info);
			stat.execute();
			stat.close();
			return HelpDatabase.getNextInsertTableID(GlobalTablesName.resources)-1;
	}
	
	
	protected static int addResourceType(String resourceType) throws SQLException, DatabaseLoadDriverException
	{
		int result = -1;
		PreparedStatement stat =  Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.selectResourceTypes);
		stat.setNString(1,resourceType);
		ResultSet rs = stat.executeQuery();
		if(rs.next())
		{
			result = rs.getInt(1);
		}
		else
		{
			PreparedStatement stat2 =  Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.insertResourceType);
			stat2.setNString(1,resourceType);
			stat2.execute();
			stat2.close();
			result =  HelpDatabase.getNextInsertTableID(GlobalTablesName.resourcesType)-1;
		}
		rs.close();
		stat.close();
		return result;	
	}
	
	public static int addSource(String source) throws SQLException, DatabaseLoadDriverException {
		int result = -1;
		PreparedStatement idsourcePS = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.selectSourcesIDByName);
		idsourcePS.setNString(1,source);
		ResultSet rs = idsourcePS.executeQuery();
		if(rs.next()){ 
			result = rs.getInt(1);
		}
		else
		{
			PreparedStatement insertSourcePS = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.insertSource);
			insertSourcePS.setNString(1,source);
			insertSourcePS.execute();
			insertSourcePS.close();
			result = HelpDatabase.getNextInsertTableID(GlobalTablesName.sources)-1;	
		}
		rs.close();
		idsourcePS.close();
		return result;
	}


	public static int addRelationType(String string) throws SQLException, DatabaseLoadDriverException {
		int result = -1;
		PreparedStatement idsourcePS = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.selectRelationTypeID);
		idsourcePS.setNString(1,string);
		ResultSet rs = idsourcePS.executeQuery();
		if(rs.next()){
			result = rs.getInt(1);
		}
		else
		{
			PreparedStatement relationTypePS = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.insertRelationType);
			relationTypePS.setNString(1,string);
			relationTypePS.execute();
			relationTypePS.close();
			result = HelpDatabase.getNextInsertTableID(GlobalTablesName.relationtype)-1;
		}
		rs.close();
		idsourcePS.close();
		return result;
	}

}
