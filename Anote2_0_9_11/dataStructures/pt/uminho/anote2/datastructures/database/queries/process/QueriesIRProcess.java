package pt.uminho.anote2.datastructures.database.queries.process;

public class QueriesIRProcess {
	
	/**
	 * Utils
	 */	
	
	public static final String insertQueryProperties = "INSERT INTO queries_properties (queries_idqueries, property_name, property_value) " +
													   "VALUES(?,?,?)";

	public static final String updateQueryInfo = "UPDATE queries " +  
			 									 "SET matching_publications=? , available_abstracts=? ,  date=? " +   
			 									 "WHERE idqueries=? AND active=1";
	
	public static final String inactiveQuery = "UPDATE queries " +  
												 "SET active=0 " +   
												 "WHERE idqueries=? ";
	
	public static final String selectQueryByID = "SELECT q.idqueries, q.keywords,q.organism " +
												 "FROM queries q JOIN queries_has_publications p ON q.idqueries = p.queries_idqueries " +
												 "WHERE publications_id =? AND active=1";
	
	public static final String selectQueryProperties = "SELECT property_name,property_value " +
													   "FROM queries_properties " +
													   "WHERE queries_idqueries=? ";
	
	public static final String selectQueriesInformationByID = "SELECT idqueries,name,date,keywords,organism,matching_publications,available_abstracts,frompubmed " +
													          "FROM queries "+
													          "WHERE idqueries=? AND active=1";

	public static final String renameQuery = "UPDATE queries "+
											 "SET name = ? "+
											 "WHERE idqueries=? "; 

}
