package pt.uminho.anote2.datastructures.database.queries.documents;

public class QueriesPublication {
	
	/**
	 * Publication IndentifierTypeID
	 */
	public static final String selectPublicationIndetifierTypeID = "SELECT id_type FROM publications_id_type WHERE name=? ";	
	
	public static final String selectPublicationIDForPublicationOtherID = "SELECT id FROM publications WHERE other_id=? ";	
	
	public static final String insertIndetifierType = "INSERT INTO publications_id_type (name) VALUES (?)";
	
	public static final String selectPublicationsTypeForPublication = "SELECT name FROM publications_id_type AS q "+
																	  "JOIN publications ON (q.id_type=publications_id_type_id_type) "+
																	  "WHERE id=?";
	
	/**
	 * General
	 */
	
	public static final String insertPublications = "INSERT INTO publications (other_id,title,authors,date,fulldate,type,status,journal,volume,issue,pages,abstract,publications_id_type_id_type) " +
    	    										"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public static final String updatePublication = "UPDATE publications " +  
												   "SET title=?, authors=?, date=?, fulldate=?, type=?, status=?, journal=?, volume=?, issue=?, pages=?, abstract=? " +   
												   "WHERE other_id=?";
	
	public static final String selectPublicationsResume="SELECT title,authors,type,fulldate,status,journal,volume,issue,pages,abstract,external_links " +
														"FROM publications ";
	
	public static final String getIDOtherID = "SELECT id FROM publications WHERE other_id=?";
	
	public static final String updatePublicationPDFavailable = "UPDATE publications " +  
															   "SET available_pdf=? " +   
															   "WHERE other_id=?";

	public static final String otherIDPublications = "SELECT other_id " +
												     "FROM publications";
	
	public static final String selectPublicationsQueryExtra = "SELECT  publications.id , publications.other_id, " +
														"publications.title, publications.authors, publications.date, publications.status, " +
														"publications.journal, publications.volume, publications.issue, publications.pages, " +
														"publications.external_links, publications.abstract, publications.available_pdf " +
														"FROM queries_has_publications join publications ON publications_id=id " +
														"WHERE queries_idqueries=? ";
	
	public static final String selectPublication = "SELECT  publications.id , publications.other_id, " +
												   "publications.title, publications.authors, publications.date, publications.status, " +
												   "publications.journal, publications.volume, publications.issue, publications.pages, " +
												   "publications.external_links, publications.abstract, publications.available_pdf "+
												   "FROM publications "+
												   "WHERE publications.id=?";
	
	
	public static final String selectPublicationType = "SELECT name FROM publications_id_type AS q "+
													   "JOIN publications ON (q.id_type=publications_id_type_id_type) "+
													   "WHERE id=?";
	
	/**
	 * Queries
	 */
	public static final String insertPublicationOnQuery = "INSERT INTO queries_has_publications " + 
														  "SET queries_idqueries=? , publications_id=?";
	
	public static final String selectIDAndPmidsForQuery = "SELECT publications.id,publications.other_id " +
														  "FROM queries_has_publications join publications ON publications_id=id " +
														  "WHERE queries_idqueries=?";
	
	public static final String selectAllPublicationQueryInfo = "SELECT publications.id, publications.other_id, " +
															   "publications.title, publications.authors, publications.date, publications.status, " +
															   "publications.journal, publications.volume, publications.issue, publications.pages, " +
															   "publications.external_links, publications.abstract, publications.available_pdf, relevance " +
															   "FROM queries_has_publications AS q JOIN publications ON (q.publications_id=id) "+
															   "NATURAL LEFT JOIN document_relevance " +
															   "WHERE queries_idqueries=? ";
	


}
