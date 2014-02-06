package pt.uminho.anote2.datastructures.database.queries;

public class QueriesGeneral {
	
	public static final String getTableInformation = "SHOW TABLE STATUS LIKE ? ";
	
	public static final String createDatabse = "CREATE DATABASE IF NOT EXISTS ";
	
	public static final String showDatabaseListOfTable = "SELECT table_name "+
													     "FROM information_schema.tables " +
													     "WHERE table_type = 'BASE TABLE' AND table_schema=?  "+
													     "ORDER BY table_name ASC ";

	
	public static final String selectClassColor = "SELECT color FROM classes_colors "+
												  "WHERE classes_idclasses=? ";

	public static final String insertClassColor = "INSERT INTO classes_colors VALUES (?,?) ";
	
	public static final String updateColor = "UPDATE classes_colors SET color=? "+
			 								 "WHERE classes_idclasses=? ";
	
	
	public static final String getAllSources = "SELECT idsources,source_name FROM sources ";

	public static final String getDatabaseVersion = "SELECT MAX(version) FROM version ";

	public static final String insertNewVersion = "INSERT INTO version (version,date,comments) VALUES (?,?,?)";

}
