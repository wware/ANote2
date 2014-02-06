package pt.uminho.anote2.datastructures.database.queries.documents;

/**
 * 
 * 
 * 
 * 
 * @author Hugo Costa
 * 
 * 
 *
 */

public class QueriesAnnotatedDocument {
	
	
	/**
	 * Entities Annotations
	 * 
	 */
	public final static String selectEntityAnnotations = "SELECT idannotations,start,end,element,resource_elements_id,normalization_form,classes_idclasses " +
													     "FROM annotations "+ 
													     "WHERE annotations.processes_idprocesses=? AND annotations.corpus_idcorpus=? AND annotations.publications_id=? AND type='ner' ";
	
	public final static String insertEntityAnnotation = "INSERT INTO annotations (processes_idprocesses,corpus_idcorpus, publications_id,start,end,element,resource_elements_id,normalization_form,classes_idclasses,type) " +
			  											"VALUES (?,?,?,?,?,?,?,?,?,1)";
	
	
	/**
	 * Event Annotations
	 * 
	 */
	public final static String insertEventAnnotation = "INSERT INTO annotations (processes_idprocesses,corpus_idcorpus, publications_id,start,end,element,resource_elements_id,normalization_form,classes_idclasses,type,classification_re,clue) " +
													   "VALUES (?,?,?,?,?,?,?,?,?,2,?,?)";
	
	public final static String selectEventAnnotations = "SELECT idannotations,start,end,clue,classification_re FROM annotations "+
											  "WHERE processes_idprocesses=? AND corpus_idcorpus=? AND publications_id=? AND type='re' ";
	
	public final static String selectEventEntitiesAnnotations = "SELECT q.idannotations,q.idannotations_sub,q.type "+
																"FROM annotations_side AS q "+
																"JOIN annotations AS p ON p.idannotations=q.idannotations "+
																"WHERE p.processes_idprocesses=? AND p.corpus_idcorpus=? AND p.publications_id=? AND p.type='re' ";
	
	public final static String selectEventProperties = "SELECT q.idannotations,q.property_name,q.property_value "+
													   "FROM annotations_properties AS q "+
													   "JOIN annotations AS p ON p.idannotations=q.idannotations "+
													   "WHERE p.processes_idprocesses=? AND p.corpus_idcorpus=? AND p.publications_id=? AND p.type='re' ";
	
	public static String getEventInformation = "SELECT corpus_idcorpus, publications_id "+
											   "FROM annotations "+
											   "WHERE idannotations=? ";
	
	/**
	 *  General Annotations
	 */
	public final static String removeAnnotation = "DELETE FROM annotations "+
			 									   "WHERE start=? AND end=? AND corpus_idcorpus=? AND processes_idprocesses=? AND publications_id=? ";


	public final static String updateClassAnnotation = "UPDATE annotations " +
													   "SET classes_idclasses=? , resource_elements_id=NULL "+
													   "WHERE start=? AND end=? AND corpus_idcorpus=? AND processes_idprocesses=? AND publications_id=? ";
	
	/**
	 * Annotation properties
	 */
	
	public final static String insertAnnotationProperties = "INSERT INTO annotations_properties" +
														    " VALUES(?,?,?) ";
	
	public final static String removeAnnotaionProperties = "DELETE FROM annotation_properties" +
														   "WHERE idannotations=? AND property_name=? ";
	
	public final static String updateAnnotationProperties = "UPDATE annotation_properties "+
															"SET property_value=? " +
															"WHERE idannotations=? AND property_name=? ";
	
	/**
	 * Annotations Side
	 */
	
	public final static String insertAnnotationSide = "INSERT IGNORE INTO annotations_side " +
													  "VALUES(?,?,?)"; // 1 - left 2 - Right
	
	public final static String removeAnnotationSide = "DELETE FROM annotations_side" +
													  "WHERE idannotation=? AND idannotation_sub=?";






	
	


}
