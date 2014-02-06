package pt.uminho.anote2.datastructures.database.queries.process;

public class QueriesProcess {
	
	/**
	 * General
	 */
	public final static String existProcessType = "SELECT * " +
			   									  "FROM processes_type " +
			   									  "WHERE type=? ";
	
	public final static String insertProcessType = "INSERT INTO processes_type (type) VALUES(?) ";
	
	public final static String insertProcess = "INSERT INTO processes (processes_type_idprocesses_type,name) " +
			   								   "VALUES (?,?)";
	
	/**
	 * Process Properties
	 */
	
	public final static String insertProcessProperties = "INSERT INTO process_properties (processes_idprocesses,property_name,property_value) " +
			   											 "VALUES (?,?,?)";
	
	
	
	public static final String insertQuery = "INSERT INTO queries (date,keywords,organism) " +
											 "VALUES(?,?,?)";
	
	public static final String removeQuery = "DELETE FROM queries " +
											 "WHERE idqueries=? ";
	
	public static final String removeQueryProperties = "DELETE FROM queries_properties " +
													    "WHERE queries_idqueries=? ";	
	
	public static final String removeQueryPublicationLinking = "DELETE FROM queries_has_publications " +
															   "WHERE queries_idqueries=? ";
	
	
	public static final String selectQueriesPubmed = "SELECT idqueries,date,keywords,organism,matching_publications," +
													 "available_abstracts " +
													 "FROM queries "; 
	
	/**
	 * Specific Queries 
	 * 
	 */
	
	public static final String getProcessDescription = "SELECT name FROM processes "+
													   "WHERE idprocesses=?";
	
}
