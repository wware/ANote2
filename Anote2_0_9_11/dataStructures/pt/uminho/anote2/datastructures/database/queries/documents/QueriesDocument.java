package pt.uminho.anote2.datastructures.database.queries.documents;

public class QueriesDocument {
	
	/**
	 * Document Full Text
	 * 
	 */
	
	public final static String getPubFullText = "SELECT publications.fulltext FROM publications "+
												"WHERE id=?";
	
	public final static String updatePubFullText = "UPDATE publications "+
			   									   "SET publications.fulltext=? "+
			   									   "WHERE id=? ";
	
	/**
	 * Document Relevance
	 * 
	 */
	
	public static final String insertRelevanceInDocInQuery = "INSERT INTO document_relevance (publications_id,queries_idqueries,relevance) " +
															 "VALUES (?,?,?) ";
	
	public static final String updateRelevanceDocInQuery = "UPDATE document_relevance " +
										   "SET relevance=? "+
										   "WHERE queries_idqueries=? AND publications_id=?";
	
	public static final String selectRelevancePublication =  "SELECT relevance " + 
															 "FROM document_relevance " +
															 "WHERE publications_id =?  AND queries_idqueries=? ";
	
	public static final String selectPublicationRelevanceForQuery = "SELECT publications_id, relevance " +
																	"FROM document_relevance " +
																	"WHERE queries_idqueries=?";
	
	public static final String removeRelevance = "DELETE FROM document_relevance WHERE publications_id=? AND queries_idqueries=? ";

	
	/**
	 *  Document Fields
	 *  
	 */
	
	public static final String insertDocumentField = "INSERT INTO publication_fields " +
													 "VALUES(?,?,?,?) ";  // publication_id,field_name,start_offset,end_offset
	
	public static final String removeDocumentField = "REMOVE FROM publication_fields " +
													 "WHERE publication_id=? AND field=? ";
	
	public static final String updateDocumentField = "UPDATE publication_fileds " +
													 "SET field=? AND start=? AND end=? "+
													 "WHERE publication_id=? AND field=? ";


	

}
