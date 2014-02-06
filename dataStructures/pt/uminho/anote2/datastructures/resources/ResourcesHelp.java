package pt.uminho.anote2.datastructures.resources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;

public class ResourcesHelp extends Observable{
	


	private IDatabase db;
	private String name;
	private String info;
	private int id;
	protected PreparedStatement addElem;
	private PreparedStatement addSyn;
	private PreparedStatement addExternalID;
	private PreparedStatement existElem=null;
	private PreparedStatement existElemSyn=null;

	private Set<String> elems;
	
	/**
	 * Constructor that inicialise a prepared statement to insert a resource element
	 * 
	 * @param db
	 * @param id
	 * @param name
	 * @param info
	 */
	public ResourcesHelp(IDatabase db,int id,String name,String info)
	{
		this.elems = new HashSet<String>();
		this.db=db;
		this.id=id;
		if(name==null){this.name="";}
		else {this.name=name;}
		if(info==null) this.info="";
		else this.info=info;
		initDBPrepareStatment(db, id);	
	}

	private void initDBPrepareStatment(IDatabase db, int id) 
	{	
		try {
			initAddElem(db, id);
			initAddSyn(db);
			initAddExternalID(db);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void loadAllTerms()
	{
		try {
			findSynTextOnly();
			findTermsTextOnly();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteTerms()
	{
		this.elems=new HashSet<String>();
	}
	
	public void initExistElem()
	{

		try {
			existElem = db.getConnection().prepareStatement(QueriesResources.existElementInResource);
			existElem.setInt(1,this.id);
			 existElemSyn = db.getConnection().prepareStatement(QueriesResources.existElementSynonyms);
			 existElemSyn.setInt(1,this.id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteInitElem()
	{
		existElem=null;
		existElemSyn=null;
	}
	
	public IResourceElementSet<IResourceElement> getTermsByName(String name)
	{
//		IResourceElementSet<IResourceElement> elem = null;
//		boolean existactivated = true;
//		if(existElem==null)
//		{
//			existactivated=false;
//			initExistElem();
//		}
//		
//		existElem.setString(1, name);
//		ResultSet rs = existElem.executeQuery();
//		if(rs.next())
//		{
//			
//		}
//		existElemSyn.setString(1, name);
//		ResultSet rs2 = existElem.executeQuery();
//		if(rs2.next())
//		{
//			
//		}	
//		
//		
//		if(existactivated=false)
//		{
//			deleteInitElem();
//		}
		return null;
	}
	
	public IResourceElement getFirstTermByName(String name)
	{
		IResourceElement elem = null;
		boolean existactivated = true;
		if(existElem==null)
		{
			existactivated=false;
			initExistElem();
		}
		
		try {
			existElem.setString(2, name);
			ResultSet rs = existElem.executeQuery();
			if(rs.next())
			{
				return new ResourceElement(rs.getInt(1),name,rs.getInt(2),"");
			}
			existElemSyn.setString(2, name);
			ResultSet rs2 = existElem.executeQuery();
			if(rs2.next())
			{
				return new ResourceElement(rs2.getInt(1),name,rs2.getInt(2),"");
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(existactivated==false)
		{
			deleteInitElem();
		}
		return elem;
	}
	
	/**
	 * Method for load term for memory structure for not insert a duplication on database
	 * @throws SQLException 
	 */
	private void findTermsTextOnly() throws SQLException
	{
		
		Connection conn = getDb().getConnection();
		if(conn==null)
		{
			return;
		}
		PreparedStatement termInMemory = conn.prepareStatement(QueriesResources.selectAllResourceElement);
		termInMemory.setInt(1,getId());
		ResultSet rs = termInMemory.executeQuery();
		while(rs.next())
		{
			this.elems.add(rs.getString(4));
		}
		rs.close();
	}

	/**
	 * Method for load term synonyms for memory structure for not insert a duplication on database
	 */
	private void findSynTextOnly() throws SQLException
	{

		
		Connection conn = getDb().getConnection();
		if(conn==null)
		{
			return;
		}
		PreparedStatement termInMemory = conn.prepareStatement(QueriesResources.getElementsSynonyms );
		termInMemory.setInt(1,getId());
		ResultSet rs = termInMemory.executeQuery();
		while(rs.next())
		{
			this.elems.add(rs.getString(1));
		}
		rs.close();
	}

	private void initAddElem(IDatabase db, int id) throws SQLException
	{
		addElem = db.getConnection().prepareStatement(QueriesResources.insertElement);
		addElem.setInt(3,id);
	}
	
	private void initAddSyn(IDatabase db) throws SQLException 
	{
		addSyn = db.getConnection().prepareStatement(QueriesResources.insertElementSynonyms);
	}
	
	private void initAddExternalID(IDatabase db) throws SQLException {	
		addExternalID = db.getConnection().prepareStatement(QueriesResources.insertElementExternalID);
	}
	
	/**
	 * Method that add a Syn
	 * 
	 * @param dicElement
	 * @return true -
	 */
	public boolean addSynomns(IResourceElement dicElement)
	{	
		/**
		 * Impedir que sinonimos com uma letra sejam introduzidos;
		 */
		if(dicElement.getTerm()==null)
		{
			return false;
		}
		else if(dicElement.getTerm().length()<2)
		{
			return false;
		}
		if(existElem==null)
		{
			return addMultipleSyn(dicElement);
		}
		else
		{
			return addSimpleSyn(dicElement);
		}
	}

	private boolean addSimpleSyn(IResourceElement elem) 
	{
		Connection connection =db.getConnection();
		if(connection == null){ return false;}
		try {
			if(verifyIfTermExist(elem))
			{
				return false;
			}
			addSyn.setInt(1,elem.getID());
			addSyn.setString(2,elem.getTerm());
			addSyn.execute();

		} catch (SQLException e) {
			return false;
		}
		
		return true;
	}
	

	
	public void addSynonymsNoRestritions(IResourceElement elem) throws SQLException
	{
		if(elem.getTerm().length()>HelpDatabase.synonynSize)
			return;
		addSyn.setInt(1,elem.getID());
		addSyn.setString(2,elem.getTerm());
		addSyn.execute();
	}
	
	public void addTermNoRestritions(IResourceElement elem) throws SQLException
	{
		if(elem.getTerm().length()>300)
		{
			addElem.setString(1,elem.getTerm().substring(0, 300));
		}
		else
		{
			addElem.setString(1,elem.getTerm());
		}
		if(elem.getTermClassID()<1)
		{
			addElem.setNull(2, 1);
		}
		else 
		{
			addElem.setInt(2,elem.getTermClassID());
		}
		addElem.execute();
	}

	private boolean addMultipleSyn(IResourceElement dicElement) {
		Connection connection = getDb().getConnection();
		if(connection == null){ return false;}
		else
		{	
			try {
				if(this.elems.contains(dicElement.getTerm())){ return false;}
				addSyn.setInt(1,dicElement.getID());
				addSyn.setString(2,dicElement.getTerm());
				addSyn.execute();
				this.elems.add(dicElement.getTerm());
			} catch (SQLException e) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method that add a extenalId for a term
	 * 
	 * @param termID
	 * @param externalID
	 * @param sourceID
	 * @return
	 */
	public boolean addExternalID(int termID,String externalID,int sourceID) {
		try {
			addExternalID.setInt(1,termID);
			addExternalID.setInt(2,this.getId());
			addExternalID.setString(3,externalID);
			addExternalID.setInt(4,sourceID);
			addExternalID.execute();
		} catch (SQLException e) {
			return false;
		}
		return true;
	}	
	
	/**
	 * Method that add a Resource Element
	 * 
	 * @param elem - Resource Element
	 * @return true - if element had been correct inserted 
	 * false - otherwise
	 */
	public boolean addElement(IResourceElement elem)
	{
		if(elem.getTerm().length()<2)
		{
			return false;
		}
		if(existElem==null)
		{
			return addMultiElement(elem);
		}
		else
		{
			return addSimpleElement(elem);
		}
	}
	
	private boolean addMultiElement(IResourceElement elem)
	{
		Connection connection = this.db.getConnection();
		if(connection == null){ return false;}
		else
		{
			try {
				if(this.elems.contains(elem.getTerm())){ return false; }
				addElem.setString(1,elem.getTerm());
				addElem.setInt(2,elem.getTermClassID());
				addElem.execute();
				this.elems.add(elem.getTerm());
			} catch (SQLException e) {
				return false;
			}
		}
		return true;
	}
	
	protected boolean addSimpleElement(IResourceElement elem)
	{
		Connection connection = this.db.getConnection();
		if(connection == null){ 
			return false;
		}
		try {
			if(verifyIfTermExist(elem))
			{
				return false;
			}
			addElem.setString(1,elem.getTerm());
			if(elem.getTermClassID()<1)
			{
				addElem.setNull(2, 1);
			}
			else
			{
				addElem.setInt(2,elem.getTermClassID());
			}
			addElem.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean verifyIfTermExist(IResourceElement elem) throws SQLException
	{
		existElem.setString(2, elem.getTerm());
		ResultSet rs = existElem.executeQuery();
		if(rs.next())
		{
			return true;
		}
		existElemSyn.setString(1, elem.getTerm());
		ResultSet rs2 = existElem.executeQuery();
		if(rs2.next())
		{
			return true;
		}	
		return false;
	}
	
	/**
	 * Method that insert a Biological Class in DataBase or return your ID of it
	 * 
	 * @param classe - Biological Class
	 * @return Database Table ID of classe
	 * -1  
	 */
	public int addElementClass(String classe) {
		Connection connection = this.db.getConnection();
		if(connection == null){ return -1;}
		else
		{
			try {
				PreparedStatement getClassesIDForClassePS = connection.prepareStatement(QueriesResources.getClassesIDForClasseName);
				getClassesIDForClassePS.setString(1, classe);
				ResultSet rs = getClassesIDForClassePS.executeQuery();
				if(rs.next()){ return rs.getInt(1);}
				else
				{
					PreparedStatement insertClassPS = connection.prepareStatement(QueriesResources.insertClass);
					insertClassPS.setString(1,classe);
					insertClassPS.execute();
					return HelpDatabase.getNextInsertTableID(getDb(),"classes")-1;
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}
	
	/**
	 * Update Element
	 * 
	 * @param elem
	 * @return
	 */
	public boolean updateElement(IResourceElement elem) {
		Connection connection = this.db.getConnection();
		if(connection == null){ return false;}
		else
		{
			try {
				PreparedStatement udpdateElement = connection.prepareStatement(QueriesResources.updateElement);		
				udpdateElement.setString(1, elem.getTerm());
				udpdateElement.setInt(2, elem.getTermClassID());
				udpdateElement.setInt(3, elem.getID());
				udpdateElement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method that remove a Resource Element 
	 * 
	 * @param elem
	 * @return
	 */
	public boolean removeElement(IResourceElement elem) {
		Connection connection = this.db.getConnection();		
		if(connection == null){ return false;}
		else
		{
			try {
				PreparedStatement removeElementPS = connection.prepareStatement(QueriesResources.removeElement);
				removeElementPS.setInt(1,elem.getID());
				removeElementPS.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public IResourceElementSet<IResourceElement> getTermByClass(String termClass) {

		IResourceElementSet<IResourceElement> dicElems = new ResourceElementSet<IResourceElement>();
				
		String term=null;
		IResourceElement dicElem = null;
		int elementID;
		try {
			Connection con = db.getConnection();
			if(con==null)
			{
				return null;
			}
			int classID = findClassID(termClass);
			if(classID==-1)
			{
				return null;
			}

			PreparedStatement getTemsClass = con.prepareStatement(QueriesResources.selectElementClass);
			getTemsClass.setInt(1,this.id);
			getTemsClass.setInt(2,classID);
			ResultSet rs = getTemsClass.executeQuery();
			while(rs.next())
			{
				elementID = rs.getInt(1);
				term = rs.getString(2);
				dicElem = new ResourceElement(elementID,term,classID,termClass);
				dicElems.addElementResource(dicElem);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return dicElems;
	}
	
	
	protected int findClassID(String className)
	{
		int termClassID = -1;
		Connection conn = getDb().getConnection();
		if(conn==null)
		{
			return -1;
		}
		else
		{
			try {
				PreparedStatement findClassIDPS = conn.prepareStatement(QueriesResources.getClassIDForClass);
				findClassIDPS.setString(1,className);
				ResultSet rsTermClassID = findClassIDPS.executeQuery();
				if(rsTermClassID.next())
				{
					termClassID=rsTermClassID.getInt(1);
					rsTermClassID.close();
					return termClassID;
				}
				else
				{
					return -1;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return -1;
			}
		}
	
	}
	

	
	public IResourceElementSet<IResourceElement> getTermByClass(int termClassID) {
		IResourceElementSet<IResourceElement> dicElems = new ResourceElementSet<IResourceElement>();
		String term=null;
		IResourceElement dicElem = null;
		int elementID;
		try {
			
			Connection con = db.getConnection();
			if(con==null)
			{
				return null;
			}
			
			String classe = getClassIDClassOnDatabase(this.db).get(termClassID);
			
			if(termClassID==-1)
			{
				return null;
			}

			PreparedStatement getTemsClass = con.prepareStatement(QueriesResources.selectElementClass);
			getTemsClass.setInt(1,this.id);
			getTemsClass.setInt(2,termClassID);
			ResultSet rs = getTemsClass.executeQuery();
			while(rs.next())
			{
				elementID = rs.getInt(1);
				term = rs.getString(2);
				dicElem = new ResourceElement(elementID,term,termClassID,classe);
				dicElems.addElementResource(dicElem);
			}
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return dicElems;
	}
	
	/**
	 * Method that return a existence classes on database
	 * 
	 * @return Map<ClassID,Class>
	 * @throws SQLException
	 */
	public static Map<Integer,String> getClassIDClassOnDatabase(IDatabase db) throws SQLException
	{

		Map<Integer,String> classIDClass = new HashMap<Integer, String>();
		

		Connection connection = db.getConnection();
		if(connection == null)
		{
			return null;
		}
		else
		{
			PreparedStatement findClasses = connection.prepareStatement(QueriesResources.selectAllClasses);
			ResultSet rs = findClasses.executeQuery();
			while(rs.next())
			{
				classIDClass.put(rs.getInt(1),rs.getString(2));
			}
			rs.close();
		}
		return classIDClass;	
	}
	
	/**
	 * Method that return a List of resource elements of a resource
	 * 
	 * @return
	 */
	public IResourceElementSet<IResourceElement> getResourceElements() {
		IResourceElementSet<IResourceElement> resourceElems = new ResourceElementSet<IResourceElement>();
		IResourceElement elem = null;
		
		Connection connection = getDb().getConnection();
		String term=null;
		int idResourceElement,classID;	
		if(connection == null)
		{
			getDb().closeConnection();
			return null;
		}
		else
		{		
			try {		
				Map<Integer,String> classes = getClassIDClassOnDatabase(this.db);		
				PreparedStatement getElems = connection.prepareStatement(QueriesResources.selectAllResourceElement);
				getElems.setInt(1,getId());	
				ResultSet rs = getElems.executeQuery();
				while(rs.next())
				{			
					idResourceElement = rs.getInt(1);
					classID = rs.getInt(3);
					term = rs.getString(4);
					elem = new ResourceElement(idResourceElement,term,classID,classes.get(classID));
					resourceElems.addElementResource(elem);
				}
				rs.close();		
			} catch (SQLException e) {
			}
		}
		return resourceElems;
	}
	
	public boolean addResourceContent(String classe)
	{
		int classID = addElementClass(classe);
		if(classID==-1)
		{
			return false;
		}
		else
		{			
			Connection conn = getDb().getConnection();
			if(conn==null)
			{
				return false;
			}
			PreparedStatement addClassContentPS;
			try {
				addClassContentPS = conn.prepareStatement(QueriesResources.insertResourceClassContent);
				addClassContentPS.setInt(1,getId());
				addClassContentPS.setInt(2,classID);
				addClassContentPS.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return true;
		}
	}
	
	public boolean addResourceContent(int classID)
	{
		if(classID==-1)
		{
			return false;
		}
		else
		{			
			Connection conn = getDb().getConnection();
			if(conn==null)
			{
				return false;
			}
			PreparedStatement addClassContentPS;
			try {
				addClassContentPS = conn.prepareStatement(QueriesResources.insertResourceClassContent);
				addClassContentPS.setInt(1,getId());
				addClassContentPS.setInt(2,classID);
				addClassContentPS.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return true;
		}
	}
	
	public Set<Integer> getClassContent()
	{

		Connection conn = this.db.getConnection();
		Set<Integer> contentClassesID = new HashSet<Integer>();	
		PreparedStatement getResourceClassContentID;
		try {
			getResourceClassContentID = conn.prepareStatement(QueriesResources.selectResourceClassesContent);
			getResourceClassContentID.setInt(1,this.id);
			ResultSet rs = getResourceClassContentID.executeQuery();
			while(rs.next())
			{
				contentClassesID.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contentClassesID;
	}
	
	public IResourceElement getTerm(int termID)
	{
		Connection conn = this.db.getConnection();
		try {
			Map<Integer, String> classes = getClassIDClassOnDatabase(getDb());
			PreparedStatement prep = conn.prepareStatement(QueriesResources.selectElementByID);
			prep.setInt(1, termID);
			ResultSet rs = prep.executeQuery();
			if(rs.next())
			{
				int idResourceElement = rs.getInt(1);
				int classID = rs.getInt(3);
				String term = rs.getString(4);
				return new ResourceElement(idResourceElement,term,classID,classes.get(classID));
			}
			else
			{
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void notifyViewObservers(){
		setChanged();
		notifyObservers();
	}

	public IDatabase getDb() {
		return db;
	}

	public String getName() {
		return name;
	}

	public String getInfo() {
		return info;
	}

	public int getId() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
