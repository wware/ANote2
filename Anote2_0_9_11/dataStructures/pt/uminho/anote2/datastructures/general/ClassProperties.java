package pt.uminho.anote2.datastructures.general;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;

@SuppressWarnings("unchecked")
public class ClassProperties {
	
	private static Map<Integer,String> classIDClass;
	private static Map<String,Integer> classClassID;
	
	static{
		try {
			classIDClass = getClasses();
			classClassID = (Map<String, Integer>) Utils.swapHashElements(classIDClass);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DatabaseLoadDriverException e) {
			e.printStackTrace();
		}
	}

	
	public static int insertNewClass(String className) throws SQLException, DatabaseLoadDriverException
	{
		if(classClassID.containsKey(className.toLowerCase()))
		{
			return classClassID.get(className.toLowerCase());
		}
		Connection connection = Configuration.getDatabase().getConnection();
		PreparedStatement insertClassPS = connection.prepareStatement(QueriesResources.insertClass);
		insertClassPS.setNString(1,className.toLowerCase());
		insertClassPS.execute();
		insertClassPS.close();
		int id = HelpDatabase.getNextInsertTableID(GlobalTablesName.classes)-1;
		classClassID.put(className.toLowerCase(), id);
		classIDClass.put(id, className.toLowerCase());
		return id;
	}
	
	public static int getClassIDOrinsertIfNotExist(String classeName) throws SQLException, DatabaseLoadDriverException
	{
		if(classClassID.containsKey(classeName.toLowerCase()))
		{
			return classClassID.get(classeName.toLowerCase());
		}
		else
		{
			return insertNewClass(classeName.toLowerCase());
		}
	}
	
	
	public static Map<Integer, String> getClassIDClass() {
		return classIDClass;
	}



	public static Map<String, Integer> getClassClassID() {
		return classClassID;
	}



	private static Map<Integer,String> getClasses() throws SQLException, DatabaseLoadDriverException
	{
		Map<Integer,String> classIDClass = new HashMap<Integer, String>();		
		Connection connection = Configuration.getDatabase().getConnection();
		ResultSet rs = connection.prepareStatement(QueriesResources.selectAllClasses).executeQuery();
		while(rs.next())
		{
			classIDClass.put(rs.getInt(1),rs.getString(2).toLowerCase());
		}
		return classIDClass;
	}

}
