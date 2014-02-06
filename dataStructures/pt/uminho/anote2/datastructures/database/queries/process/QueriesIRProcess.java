package pt.uminho.anote2.datastructures.database.queries.process;

public class QueriesIRProcess {
	
	/**
	 * Utils
	 */
	public static final String idQueryMax = "SELECT max(idqueries) " +
											"FROM queries";
	
	
	public static final String insertQueryProperties = "INSERT INTO queries_properties (queries_idqueries, property_name, property_value) " +
													   "VALUES(?,?,?)";

	public static final String updateQueryInfo = "UPDATE queries " +  
			 									 "SET matching_publications=? , available_abstracts=? " +   
			 									 "WHERE idqueries=? ";
	
	public static final String selectQueryByID = "SELECT q.idqueries, q.keywords,q.organism " +
												 "FROM queries q JOIN queries_has_publications p ON q.idqueries = p.queries_idqueries " +
												 "WHERE publications_id =? ";
	
	public static final String selectQueryProperties = "SELECT property_name,property_value " +
													   "FROM queries_properties " +
													   "WHERE queries_idqueries=? ";

}
