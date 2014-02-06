package pt.uminho.anote2.datastructures.database.queries.documents;

public class QueriesPublication {
	
	/**
	 * Publication IndentifierTypeID
	 */
	public static final String selectPublicationIndetifierTypeID = "SELECT id_type FROM publications_id_type WHERE name=? ";	
	
	public static final String selectPublicationIDForPublicationOtherID = "SELECT id FROM publications WHERE other_id=? AND publications_id_type_id_type=? ";	
	
	public static final String insertIndetifierType = "INSERT INTO publications_id_type (name) VALUES (?)";
	
	public static final String selectPublicationsTypeForPublication = "SELECT name FROM publications_id_type AS q "+
																	  "JOIN publications ON (q.id_type=publications_id_type_id_type) "+
																	  "WHERE id=?";
	
	public static final String selectAllDocumentTypes = "SELECT name FROM publications_id_type";
	
	/**
	 * General
	 */
	
	public static final String insertPublication = "INSERT INTO publications (other_id,title,authors,date,fulldate,type,status,journal,volume,issue,pages,abstract,publications_id_type_id_type,external_links) " +
    	    										"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public static final String updatePublicationExternalLink = "UPDATE publications " +  
			   												   "SET external_links=? " +   
			   												   "WHERE id=? ";;

	
	public static final String selectPublicationsResume="SELECT title,authors,type,fulldate,status,journal,volume,issue,pages,abstract,external_links " +
														"FROM publications ";
	
	public static final String selectgetIDDocumentFromOtherID = "SELECT id FROM publications WHERE other_id=?";
	
	public static final String updatePublicationPDFavailable = "UPDATE publications " +  
															   "SET available_pdf=? " +   
															   "WHERE other_id=? AND publications_id_type_id_type=? ";

	public static final String otherIDPublication = "SELECT other_id,id " +
												     "FROM publications "+
												     "WHERE publications_id_type_id_type=? ";
	
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
	public static final String insertPublicationOnQuery = "INSERT IGNORE INTO queries_has_publications " + 
														  "SET queries_idqueries=? , publications_id=?";
	
	public static final String selectIDAndOtherIDForQuery = "SELECT publications.id,publications.other_id " +
														  "FROM queries_has_publications join publications ON publications_id=id " +
														  "WHERE queries_idqueries=?";
	
	public static final String selectAllPublicationQueryInf = "SELECT publications.id, publications.other_id, " +
															   "publications.title, publications.authors, publications.date, publications.status, " +
															   "publications.journal, publications.volume, publications.issue, publications.pages, " +
															   "publications.external_links, publications.abstract, publications.available_pdf, relevance " +
															   "FROM queries_has_publications AS q JOIN publications ON (q.publications_id=id) "+
															   "NATURAL LEFT JOIN document_relevance " +
															   "WHERE queries_idqueries=? ";

	


}
