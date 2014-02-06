package pt.uminho.anote2.datastructures.database.queries.resources;

public class QueriesResources {
	
	public static final String existElementInResource = "SELECT idresource_elements,classes_idclasses " +
														"FROM resource_elements " +
														"WHERE resources_idresources=? AND element=? "; 
	
	public static final String existElementSynonyms = "SELECT  idresource_elements,classes_idclasses "+
			 										"FROM resource_elements t JOIN synonyms s ON t.idresource_elements = s.resource_elements_idresource_elements "+
			 										"WHERE resources_idresources=? AND synonym=?"; 
	
	public static final String getElementsSynonyms = "SELECT s.synonym "+
			   									"FROM resource_elements t JOIN synonyms s ON t.idresource_elements = s.resource_elements_idresource_elements "+
			   									"WHERE resources_idresources=?";
	
	public static final String insertElement = "INSERT INTO resource_elements (element,classes_idclasses,resources_idresources) "+
											   "VALUES (?,?,?)";
	
	public final static String insertElementSynonyms = "INSERT INTO synonyms (resource_elements_idresource_elements,synonym) "+
			  									 	   "VALUES (?,?)";
	
	public final static String insertElementExternalID = "INSERT INTO resource_elements_extenal_id (resource_elements_idresource_elements,resource_elements_resources_idresources,external_id,sources_idsources) "+
														 "VALUES (?,?,?,?)";
	
	public static final String getClassesIDForClasseName = "SELECT idclasses FROM classes "+
			   											   "WHERE class=?";

	public static final String insertClass = "INSERT INTO classes (class) VALUES (?)";
	
	public static final String updateElement = "UPDATE resource_elements "+
			   								   "SET element=? , classes_idclasses=? "+
			   								   "WHERE idresource_elements=? ";
	
	public static final String removeElement = "DELETE FROM resource_elements "+
			   								   "WHERE idresource_elements=?";
	
	public static final String selectElementClass = "SELECT idresource_elements,element " +
												    "FROM resource_elements " +
												    "WHERE resources_idresources=? AND classes_idclasses=?";
	
	public static final String getClassIDForClass = "SELECT idclasses FROM classes WHERE class=?";
	
	public static final String selectAllClasses = "SELECT * FROM classes";
	
	public static final String selectAllResourceElement = "SELECT * FROM resource_elements "+
														  "WHERE resources_idresources=?";
	
	public static final String insertResourceClassContent = "INSERT IGNORE resources_content (resources_idresources,classes_idclasses) "+
															"VALUES (?,?)";
	
	public static final String selectResourceClassesContent = "SELECT classes_idclasses FROM resources_content "+
			 												  "WHERE resources_idresources=? ";	
	
	public static final String selectElementByID = "SELECT * FROM resource_elements "+
												   "WHERE idresource_elements=?";
	
	public static final String selectResourceElementAndClass="SELECT element,classes.class "+
			 												 "FROM resource_elements AS q JOIN classes ON (q.classes_idclasses=classes.idclasses) "+
			 												 "WHERE resources_idresources=?";
	
	public static final String selectElementExternalID = "SELECT * FROM resource_elements "+
			 							 				 "JOIN resource_elements_extenal_id ON resource_elements_extenal_id.resource_elements_idresource_elements=resource_elements.idresource_elements "+
			 							 				 "WHERE resources_idresources=?";
	
	public static final String selectElementSynByID = "SELECT resource_elements_idresource_elements,synonym " +
												"FROM synonyms " +
												"WHERE resource_elements_idresource_elements=?";
	
	public static final String selectSourcesIDByName = "SELECT idsources FROM sources "+
													   "WHERE source_name=?";
	
	public static final String selectResourceTypes = "SELECT idresources_type FROM resources_type "+
													 "WHERE type=? ";
	
	public static final String insertSource = "INSERT INTO sources (source_name) " +
											  "VALUES(?) ";
	
	public static final String insertResourceType = "INSERT INTO resources_type (type) VALUES(?) ";
	
	public static final String insertResource = "INSERT INTO resources (name,resources_type_idresources_type,note) "+
												"VALUES (?,?,?)";
	

	public static final String selectAllresources = "SELECT idresources,type,name,note "+
													"FROM resources AS q JOIN resources_type ON (q.resources_type_idresources_type=resources_type.idresources_type); ";
	
	public static final String getResourcesInfo =	"SELECT idresources,type,name,note "+
													"FROM resources AS q JOIN resources_type ON (q.resources_type_idresources_type=resources_type.idresources_type) " +
													"WHERE idresources=? ";
	
	public static final String selectResourceFilterByType = "SELECT idresources,name,note "+
			   												"FROM resources AS q JOIN resources_type ON (q.resources_type_idresources_type=resources_type.idresources_type) "+
			   												"WHERE resources_type.type=? ";
	
	public static final String insertRelationType = "INSERT INTO relation_type (type)" +
													" VALUES(?)";
	
	public static final String insertResourceElementRelation = "INSERT IGNORE resource_elements_relation (idresource_elements_a,idresource_elements_b,relation_type_idrelation_type) " +
															   " VALUES(?,?,?)";

	public static final String selectResourceElementCount = "SELECT COUNT(*) FROM resource_elements " +
															"WHERE resources_idresources=? ";

	public static final String selectRelationsOntology = "SELECT t.idresource_elements_a, t.idresource_elements_b "+
														 "FROM resource_elements_relation t JOIN resource_elements s ON t.idresource_elements_a = s.idresource_elements "+
														 "WHERE s.resources_idresources=? ";
	
	public static final String  selectResourceclassContent = "SELECT classes_idclasses,classes.class "+
														     "FROM resources_content AS q JOIN classes ON (q.classes_idclasses=classes.idclasses) "+
														     "WHERE resources_idresources=?";
	
	public static String totalResourceTerms = "SELECT COUNT(*) FROM resource_elements "+
									          "WHERE resources_idresources=?";
	
	public static String totalResourceSyn = "SELECT COUNT(*) "+
			  						"FROM resource_elements t JOIN synonyms s ON t.idresource_elements = s.resource_elements_idresource_elements "+
			  						"WHERE resources_idresources=?";
	
	public static String totalResourceClassTerms = "SELECT COUNT(*) FROM resource_elements "+
												   "WHERE resources_idresources=? AND classes_idclasses=?";
	
	public static String totalResourceClassSyn = "SELECT COUNT(*) "+
												 "FROM resource_elements t JOIN synonyms s ON t.idresource_elements = s.resource_elements_idresource_elements "+
												 "WHERE resources_idresources=? AND classes_idclasses=?";

}
