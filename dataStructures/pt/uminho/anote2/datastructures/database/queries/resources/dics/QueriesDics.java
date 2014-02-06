package pt.uminho.anote2.datastructures.database.queries.resources.dics;

public class QueriesDics {
	
//	public static final String termSyn = "SELECT t.idresource_elements,s.synonym,t.classes_idclasses,external_id,sources_idsources "+
//			 							 "FROM resource_elements t "+
//			 							 "JOIN synonyms s ON t.idresource_elements = s.resource_elements_idresource_elements "+
//			 							 "JOIN resource_elements_extenal_id ex ON ex.resource_elements_idresource_elements=t.idresource_elements "+
//			 							 "WHERE resources_idresources=?";
	
	public static final String termSyn = "SELECT  idresource_elements,s.synonym,t.classes_idclasses "+
										 "FROM resource_elements t JOIN synonyms s ON t.idresource_elements = s.resource_elements_idresource_elements "+
										 "WHERE resources_idresources=?	";

	public static final String getTerms = "SELECT idresource_elements,element,classes_idclasses FROM resource_elements WHERE resources_idresources=?";
	
	public static final String selectTermsClass = "SELECT idresource_elements,element " +
												  "FROM resource_elements " +
												  "WHERE resources_idresources=? AND classes_idclasses=?";

	public static final String selectSynTermsClass = "SELECT  idresource_elements,s.synonym "+
													 "FROM resource_elements t JOIN synonyms s ON t.idresource_elements = s.resource_elements_idresource_elements "+
													 "WHERE resources_idresources=? AND classes_idclasses=?";
	



}
