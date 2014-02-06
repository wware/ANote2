package pt.uminho.anote2.datastructures.resources;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import org.apache.log4j.Logger;

import pt.uminho.anote2.core.annotation.IExternalID;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.annotation.ExternalID;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.database.queries.resources.dics.QueriesDics;
import pt.uminho.anote2.datastructures.database.schema.TableResourcesElements;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.datastructures.resources.lookuptable.LookupTable;
import pt.uminho.anote2.datastructures.resources.ontology.Ontology;
import pt.uminho.anote2.datastructures.resources.rule.RulesSet;
import pt.uminho.anote2.datastructures.utils.GenericPair;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;

public class ResourcesHelp extends Observable{
	
	/**
	 * Logger
	 */
	static Logger logger = Logger.getLogger(ResourcesHelp.class.getName());
	
	private String name;
	private String info;
	private int id;
	protected PreparedStatement addElem;
	private PreparedStatement addSyn;
	private PreparedStatement addExternalID;
	private PreparedStatement existElem=null;
	private PreparedStatement existElemSyn=null;
	private PreparedStatement existExternalIDTerm=null;

	private boolean active;
	private Map<String,GenericPair<Integer,Integer>> terms; // Term -> (classID,DatabaseID)
	private Map<String,GenericPair<Integer,Integer>> synonyms; // TermSynonym -> (classID,DatabaseID)
	private Map<String,GenericPair<Integer,Integer>> externalIDs; // ExternalID -> (sourceID,DatabaseID)

	/**
	 * Constructor that start a prepared statement to insert a resource element
	 * 
	 * @param db
	 * @param id
	 * @param name
	 * @param info
	 */
	public ResourcesHelp(int id,String name,String info)
	{
		this.id=id;
		if(name==null){this.name="";}
		else {this.name=name;}
		if(info==null) this.info="";
		else this.info=info;
		this.active = true;
		this.terms= new HashMap<String, GenericPair<Integer,Integer>>();
		this.synonyms = new HashMap<String, GenericPair<Integer,Integer>>();
		this.externalIDs = new HashMap<String, GenericPair<Integer,Integer>>();
		try {
			initDBPrepareStatment(id);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DatabaseLoadDriverException e) {
			e.printStackTrace();
		}	
	}
	
	public ResourcesHelp(int id,String name,String info,boolean active)
	{
		this.id=id;
		if(name==null){this.name="";}
		else {this.name=name;}
		if(info==null) this.info="";
		else this.info=info;
		this.setActive(active);
		this.terms= new HashMap<String, GenericPair<Integer,Integer>>();
		this.synonyms = new HashMap<String, GenericPair<Integer,Integer>>();
		this.externalIDs = new HashMap<String, GenericPair<Integer,Integer>>();
		try {
			initDBPrepareStatment(id);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DatabaseLoadDriverException e) {
			e.printStackTrace();
		}	
	}

	private void initDBPrepareStatment(int id) throws SQLException, DatabaseLoadDriverException 
	{	
			initAddElem(id);
			initAddSyn();
			initAddExternalID();
			initExistElem();
	}
	
	public void loadAllTermsFromDatabaseToMemory() throws SQLException, DatabaseLoadDriverException
	{
		findSynTextOnly();
		findTermsTextOnly();
		findExternalIDs();
	}

	public void deleteTermsInMemory()
	{
		this.terms= new HashMap<String, GenericPair<Integer,Integer>>();
		this.synonyms = new HashMap<String, GenericPair<Integer,Integer>>();
		this.externalIDs = new HashMap<String, GenericPair<Integer,Integer>>();
	}
	
	private void initExistElem() throws SQLException, DatabaseLoadDriverException
	{
			existElem = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.existElementInResource);
			existElem.setInt(1,this.id);
			existElemSyn = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.existElementSynonyms);
			existElemSyn.setInt(1,this.id);
			existExternalIDTerm = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.existExternalIDForTem);
			
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
//		existElem.setNString(1, name);
//		ResultSet rs = existElem.executeQuery();
//		if(rs.next())
//		{
//			
//		}
//		existElemSyn.setNString(1, name);
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
	
	public IResourceElement getFirstTermByName(String name) throws SQLException
	{
		IResourceElement elem = null;
		existElem.setNString(2, name);
		ResultSet rs = existElem.executeQuery();
		if(rs.next())
		{
			return new ResourceElement(rs.getInt(1),name,rs.getInt(2),"");
		}
		existElemSyn.setNString(2, name);
		ResultSet rs2 = existElem.executeQuery();
		if(rs2.next())
		{
			return new ResourceElement(rs2.getInt(1),name,rs2.getInt(2),"");
		}	
		return elem;
	}
	
	/**
	 * Method for load term for memory structure for not insert a duplication on database
	 * @throws SQLException 
	 * @throws DatabaseLoadDriverException 
	 */
	private void findTermsTextOnly() throws SQLException, DatabaseLoadDriverException
	{
		this.terms= new HashMap<String, GenericPair<Integer,Integer>>();
		PreparedStatement termInMemory = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.selectAllResourceElement);
		termInMemory.setInt(1,getID());
		ResultSet rs = termInMemory.executeQuery();
		while(rs.next())
		{
			this.terms.put(rs.getString(4),new GenericPair<Integer, Integer>(rs.getInt(3),rs.getInt(1)));
		}
		rs.close();
		termInMemory.close();
	}

	/**
	 * Method for load term synonyms for memory structure for not insert a duplication on database
	 * @throws DatabaseLoadDriverException 
	 */
	private void findSynTextOnly() throws SQLException, DatabaseLoadDriverException
	{
		this.synonyms = new HashMap<String, GenericPair<Integer,Integer>>();
		PreparedStatement termInMemory = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.getElementsSynonyms);
		termInMemory.setInt(1,getID());
		ResultSet rs = termInMemory.executeQuery();
		while(rs.next())
		{
			this.synonyms.put(rs.getString(1),new GenericPair<Integer, Integer>(rs.getInt(2),rs.getInt(3)));
		}
		rs.close();
		termInMemory.close();
	}
	
	private void findExternalIDs() throws SQLException, DatabaseLoadDriverException {
		this.externalIDs = new HashMap<String, GenericPair<Integer,Integer>>();
		PreparedStatement extIDMemory = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.selectElementExternalID);
		extIDMemory.setInt(1,getID());
		ResultSet rs = extIDMemory.executeQuery();
		while(rs.next())
		{
			this.externalIDs.put(rs.getString(8),new GenericPair<Integer, Integer>(rs.getInt(9),rs.getInt(1)));
		}
		rs.close();	
		extIDMemory.close();
	}

	private void initAddElem(int id) throws SQLException, DatabaseLoadDriverException
	{
		addElem = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.insertElement);
		addElem.setInt(3,id);
	}
	
	private void initAddSyn() throws SQLException, DatabaseLoadDriverException 
	{
		addSyn = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.insertElementSynonyms);
	}
	
	private void initAddExternalID() throws SQLException, DatabaseLoadDriverException {	
		addExternalID = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.insertElementExternalID);
	}
	
	/**
	 * Method that add a Syn
	 * 
	 * @param dicElement
	 * @return true -
	 */
	public boolean addSynomyn(IResourceElement dicElement)
	{	
		if(dicElement.getTerm()==null)
		{
			return false;
		}
		else if(dicElement.getTerm().length()< TableResourcesElements.mimimumSynonymSize || dicElement.getTerm().length()>TableResourcesElements.synonymSize)
		{
			return false;
		}
		else if(this.terms!=null && this.terms.containsKey(dicElement.getTerm()))
		{
			return false;
		}
		else if(this.synonyms!=null && this.synonyms.containsKey(dicElement.getTerm()))
		{
			return false;
		}
		if(addSynonymsNoRestritions(dicElement))
		{
			if(this.synonyms!=null)
			{
				this.synonyms.put(dicElement.getTerm(),new GenericPair<Integer, Integer>(dicElement.getTermClassID(), dicElement.getID()) );
			}
			return true;
		}
		else
		{
			return false;
		}

	}
	
	public boolean addSynonymsNoRestritions(IResourceElement elem)
	{
		if( elem.getTerm().length()>TableResourcesElements.synonymSize)
		{
			return false;
		}
		try {
			if(elem.getTerm().length()>TableResourcesElements.elementSize)
			{
				addSyn.setNString(1,elem.getTerm().substring(0, TableResourcesElements.elementSize));
			}
			else
			{
				addSyn.setInt(1,elem.getID());
			}
			addSyn.setNString(2,elem.getTerm());
			addSyn.execute();
		} catch (SQLException e) {
			logger.error(e.getMessage() + addSyn.toString());
			return false;
		}
		return true;
	}
	
	public boolean addTermNoRestritions(IResourceElement elem)
	{
		if(elem.getTerm().length()>TableResourcesElements.elementSize)
		{
			return false;
		}
		else
		{
			try {
				if(elem.getTerm().length()>TableResourcesElements.elementSize)
				{
					addElem.setNString(1,elem.getTerm().substring(0, TableResourcesElements.elementSize));
				}
				else
				{
					addElem.setNString(1,elem.getTerm());
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
			} catch (SQLException e) {
				logger.error(e.getMessage() + addElem.toString());
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

		if(externalIDs.containsKey(externalID) && externalIDs.get(externalID).getX()==sourceID)
		{
			return false;
		}
		try {
			addExternalID.setInt(1,termID);
			addExternalID.setInt(2,this.getID());
			addExternalID.setNString(3,externalID);
			addExternalID.setInt(4,sourceID);			
			addExternalID.execute();
			externalIDs.put(externalID, new GenericPair<Integer, Integer>(sourceID, termID));
		} catch (SQLException e) {
			logger.error(e.getMessage() + addExternalID.toString());
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
	 * @throws SQLException 
	 * @throws DatabaseLoadDriverException 
	 */
	public boolean addElement(IResourceElement elem)
	{
		if(elem.getTerm().length()<TableResourcesElements.mimimumElementSize && elem.getTerm().length()>TableResourcesElements.elementSize)
		{
			return false;
		}
//		if(existElem!=null)
		{
			return addMultiElement(elem);
		}
//		else
//		{
//			return addSimpleElement(elem);
//		}
	}
	

	private boolean addMultiElement(IResourceElement elem)
	{
		try {
			if(this.terms.containsKey(elem.getTerm()) || this.synonyms.containsKey(elem.getTerm()))
			{
				return false;
			}
			addElem.setNString(1,elem.getTerm());
			if(elem.getTermClassID()==-1)
			{
				addElem.setNull(2,1);
			}
			else
			{
				addElem.setInt(2,elem.getTermClassID());
			}
			addElem.execute();
			this.terms.put(elem.getTerm(),new GenericPair<Integer, Integer>(elem.getTermClassID(),elem.getID()));
		} catch (SQLException e) {
			logger.error(e.getMessage() + addElem.toString());
			return false;
		}
		return true;
	}

	public boolean addSimpleElement(IResourceElement elem)
	{
		try {
			if(verifyIfTermExist(elem))
			{
				return false;
			}
			addElem.setNString(1,elem.getTerm());
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
			logger.error(e.getMessage() + addElem.toString());
			return false;
		}
		return true;
	}
	
	public boolean verifyIfTermExist(IResourceElement elem)
	{
		try {
			existElem.setNString(2, elem.getTerm());
			ResultSet rs = existElem.executeQuery();
			if(rs.next())
			{
				existElem.clearBatch();
				rs.close();
				return true;
			}
			existElem.clearBatch();
			existElemSyn.setNString(2, elem.getTerm());
			ResultSet rs2 = existElemSyn.executeQuery();
			if(rs2.next())
			{
				existElemSyn.clearBatch();
				rs2.close();
				return true;
			}
			existElemSyn.clearBatch();
			return false;
		} catch (SQLException e) {
			logger.error(e.getMessage() + existElemSyn.toString() + existElem.toString());
			return true;
		}
	}
	
	/**
	 * Method that insert a Biological Class in DataBase or return your ID of it
	 * 
	 * @param classe - Biological Class
	 * @return Database Table ID of classe
	 * -1  
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 */
	public int addElementClass(String classe) throws SQLException, DatabaseLoadDriverException {
		if(ClassProperties.getClassClassID().containsKey(classe))
		{
			return ClassProperties.getClassClassID().get(classe);
		}
		else
		{
			return ClassProperties.insertNewClass(classe);
		}		
	}

	/**
	 * Update Element
	 * 
	 * @param elem
	 * @return
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 */
	public void updateElement(IResourceElement elem) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement udpdateElement = GlobalOptions.database.getConnection().prepareStatement(QueriesResources.updateElement);		
		udpdateElement.setNString(1, elem.getTerm());
		if(elem.getTermClassID()==-1)
		{
			udpdateElement.setNull(2, 2);
		}
		else
		{
			udpdateElement.setInt(2, elem.getTermClassID());
		}
		udpdateElement.setInt(3, elem.getID());
		udpdateElement.execute();
		udpdateElement.close();
	}
	
	public void inactiveElement(IResourceElement element) throws SQLException, DatabaseLoadDriverException
	{
		PreparedStatement inactivateElem = GlobalOptions.database.getConnection().prepareStatement(QueriesResources.inactiveElement);		
		inactivateElem.setInt(1,element.getID());
		inactivateElem.execute();
		inactivateElem.close();
	}

	public void inactiveElementsByClassID(int classID) throws SQLException, DatabaseLoadDriverException {
		int step = 0;
		IResourceElementSet<IResourceElement> elemns = getAllTermByClass(classID);
		int total = elemns.size();
		PreparedStatement inactivateElem = GlobalOptions.database.getConnection().prepareStatement(QueriesResources.inactiveElement);		
		for(IResourceElement element:elemns.getElements())
		{
			inactivateElem.setInt(1,element.getID());
			inactivateElem.execute();
			if(step%100==0)
			{
				memoryAndProgress(step, total);
			}
			step ++;
		}
		inactivateElem.close();
	}
	
	/**
	 * Method that remove a Resource Element 
	 * 
	 * @param elem
	 * @return
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 */
	public void removeElement(IResourceElement elem) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement removeElementPS = GlobalOptions.database.getConnection().prepareStatement(QueriesResources.removeElement);
		removeElementPS.setInt(1,elem.getID());
		removeElementPS.execute();
		removeElementPS.close();
	}
	
	public IResourceElementSet<IResourceElement> getTermByClass(String termClass) throws SQLException, DatabaseLoadDriverException {
		IResourceElementSet<IResourceElement> dicElems = new ResourceElementSet<IResourceElement>();			
		String term=null;
		IResourceElement dicElem = null;
		int elementID;
		int classID = findClassID(termClass);
		if(classID==-1)
		{
			return null;
		}
		PreparedStatement getTemsClass = GlobalOptions.database.getConnection().prepareStatement(QueriesResources.selectElementClass);
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
		getTemsClass.close();
		return dicElems;
	}

	
	protected int findClassID(String className) throws SQLException, DatabaseLoadDriverException
	{
		int termClassID = -1;
		{
			PreparedStatement findClassIDPS = GlobalOptions.database.getConnection().prepareStatement(QueriesResources.getClassIDForClass);
			findClassIDPS.setNString(1,className);
			ResultSet rsTermClassID = findClassIDPS.executeQuery();
			if(rsTermClassID.next())
			{
				termClassID=rsTermClassID.getInt(1);

			}
			rsTermClassID.close();
			findClassIDPS.close();
		}
		return termClassID;
	}
	


	public IResourceElementSet<IResourceElement> getTermByClass(int termClassID) throws SQLException, DatabaseLoadDriverException {
		IResourceElementSet<IResourceElement> dicElems = new ResourceElementSet<IResourceElement>();
		String term=null;
		IResourceElement dicElem = null;
		int elementID;
		String classe = ClassProperties.getClassIDClass().get(termClassID);
		if(termClassID==-1)
		{
			return null;
		}
		PreparedStatement getTemsClass = Configuration.getDatabase().getNeWConnection().prepareStatement(QueriesResources.selectElementClass);
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
		getTemsClass.close();
		return dicElems;
	}
	
	/**
	 * Method that return a List of resource elements of a resource
	 * 
	 * @return
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 */
	public IResourceElementSet<IResourceElement> getResourceElements() throws SQLException, DatabaseLoadDriverException {
		IResourceElementSet<IResourceElement> resourceElems = new ResourceElementSet<IResourceElement>();
		IResourceElement elem = null;
		String term=null;
		int idResourceElement,classID;	
		{		
			PreparedStatement getElems = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.selectAllResourceElement);
			getElems.setInt(1,getID());	
			ResultSet rs = getElems.executeQuery();
			while(rs.next())
			{			
				idResourceElement = rs.getInt(1);
				classID = rs.getInt(3);
				term = rs.getString(4);
				elem = new ResourceElement(idResourceElement,term,classID,ClassProperties.getClassIDClass().get(classID));
				resourceElems.addElementResource(elem);
			}
			rs.close();	
			getElems.close();
		}
		return resourceElems;
	}
	
	public IResourceElementSet<IResourceElement> getAllTermByClass(int classID) throws SQLException, DatabaseLoadDriverException {
		IResourceElementSet<IResourceElement> dicElems = new ResourceElementSet<IResourceElement>();		
		String term=null;
		IResourceElement dicElem = null;
		int elementID;

		String termClasse = ClassProperties.getClassIDClass().get(classID);
		if(termClasse==null)
		{
			return null;
		}
		PreparedStatement getTemsClass = Configuration.getDatabase().getConnection().prepareStatement(QueriesDics.selectTermsClass);
		getTemsClass.setInt(1,getID());
		getTemsClass.setInt(2,classID);
		ResultSet rs = getTemsClass.executeQuery();
		while(rs.next())
		{
			elementID = rs.getInt(1);
			term = rs.getString(2);
			dicElem = new ResourceElement(elementID,term,classID,termClasse);
			dicElems.addElementResource(dicElem);
		}
		rs.close();
		getTemsClass.close();
		PreparedStatement getTemsSynClass = Configuration.getDatabase().getConnection().prepareStatement(QueriesDics.selectSynTermsClass);
		getTemsSynClass.setInt(1,getID());
		getTemsSynClass.setInt(2,classID);
		ResultSet rsSyn = getTemsSynClass.executeQuery();
		while(rsSyn.next())
		{
			elementID = rsSyn.getInt(1);
			term = rsSyn.getString(2);
			dicElem = new ResourceElement(elementID,term,classID,termClasse);
			dicElems.addElementResource(dicElem);
		}
		rsSyn.close();
		getTemsSynClass.close();
		return dicElems;
	}
	
	public boolean addResourceContent(String classe) throws SQLException, DatabaseLoadDriverException
	{
		int classID = addElementClass(classe);
		if(classID==-1)
		{
			return false;
		}
		else
		{			
			PreparedStatement addClassContentPS;
			addClassContentPS = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.insertResourceClassContent);
			addClassContentPS.setInt(1,getID());
			addClassContentPS.setInt(2,classID);
			addClassContentPS.execute();
			addClassContentPS.close();
			return true;
		}
	}
	
	public boolean addResourceContent(int classID) throws SQLException, DatabaseLoadDriverException
	{
		if(classID==-1)
		{
			return false;
		}
		else
		{			
			PreparedStatement addClassContentPS;
			addClassContentPS = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.insertResourceClassContent);
			addClassContentPS.setInt(1,getID());
			addClassContentPS.setInt(2,classID);
			addClassContentPS.execute();
			addClassContentPS.close();
			return true;
		}
	}
	
	public Set<Integer> getClassContent() throws SQLException, DatabaseLoadDriverException
	{
		Set<Integer> contentClassesID = new HashSet<Integer>();	
		PreparedStatement getResourceClassContentID;
		getResourceClassContentID = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.selectResourceClassesContent);
		getResourceClassContentID.setInt(1,this.id);
		ResultSet rs = getResourceClassContentID.executeQuery();
		while(rs.next())
		{
			contentClassesID.add(rs.getInt(1));
		}
		rs.close();
		getResourceClassContentID.close();
		return contentClassesID;
	}
	
	public IResourceElement getTerm(int termID) throws SQLException, DatabaseLoadDriverException
	{
		PreparedStatement prep =  Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.selectElementByIDWihoutActiveFilter);
		prep.setInt(1, termID);
		ResultSet rs = prep.executeQuery();
		if(rs.next())
		{
			int idResourceElement = rs.getInt(1);
			int classID = rs.getInt(3);
			String term = rs.getString(4);
			boolean active = rs.getBoolean(5);
			rs.close();
			prep.close();
			return new ResourceElement(idResourceElement,term,classID,ClassProperties.getClassIDClass().get(classID),active);
		}
		rs.close();
		prep.close();
		return null;
	}

	public IResourceElementSet<IResourceElement> getAllTermsAndSynonyms() throws SQLException, DatabaseLoadDriverException {		
		IResourceElementSet<IResourceElement> terms = new ResourceElementSet<IResourceElement>();		
		PreparedStatement getSyn = Configuration.getDatabase().getConnection().prepareStatement(QueriesDics.termSyn);
		getSyn.setInt(1,getID());
		ResultSet rs = getSyn.executeQuery();
		int termID,classID;
		String termName;
		while(rs.next())
		{
			termID = rs.getInt(1);
			termName = rs.getString(2);
			classID = rs.getInt(3);
			IResourceElement elem = new ResourceElement(termID,termName,classID,ClassProperties.getClassIDClass().get(classID));
			terms.addElementResource(elem);
		}
		rs.close();
		getSyn.close();
		PreparedStatement getTems = Configuration.getDatabase().getConnection().prepareStatement(QueriesDics.getTerms);
		getTems.setInt(1,getID());
		ResultSet rs2 = getTems.executeQuery();
		while(rs2.next())
		{
			termID = rs2.getInt(1);
			termName = rs2.getString(2);
			classID = rs2.getInt(3);
			IResourceElement elem = new ResourceElement(termID,termName,classID,ClassProperties.getClassIDClass().get(classID));
			terms.addElementResource(elem);
		}
		rs2.close();
		getTems.close();
		return terms;
	}
	

	/**
	 * Method that filter resources elements by class
	 * 
	 * @param termClass
	 * @return
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 */
	public IResourceElementSet<IResourceElement> getAllTermByClass(String termClass) throws SQLException, DatabaseLoadDriverException {

		IResourceElementSet<IResourceElement> dicElems = new ResourceElementSet<IResourceElement>();	
		String term=null;
		IResourceElement dicElem = null;
		int elementID;
		int classID = findClassID(termClass);
		if(classID==-1)
		{
			return null;
		}
		PreparedStatement getTemsClass = Configuration.getDatabase().getConnection().prepareStatement(QueriesDics.selectTermsClass);
		getTemsClass.setInt(1,getID());
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
		getTemsClass.close();
		PreparedStatement getTemsSynClass = Configuration.getDatabase().getConnection().prepareStatement(QueriesDics.selectSynTermsClass);
		getTemsSynClass.setInt(1,getID());
		getTemsSynClass.setInt(2,classID);
		ResultSet rsSyn = getTemsSynClass.executeQuery();
		while(rsSyn.next())
		{
			elementID = rsSyn.getInt(1);
			term = rsSyn.getString(2);
			dicElem = new ResourceElement(elementID,term,classID,termClass);
			dicElems.addElementResource(dicElem);
		}
		rsSyn.close();
		getTemsSynClass.close();
		return dicElems;
	}
	
	public void removeElementAllSynonyms(int termID) throws SQLException, DatabaseLoadDriverException{
		PreparedStatement removeElementPS =  Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.removeElementSynonyms);
		removeElementPS.setInt(1,termID);
		removeElementPS.execute();
		removeElementPS.close();
	}
	
	
	public void removeElementSynonyms(int termID,String elem) throws SQLException, DatabaseLoadDriverException
	{
		PreparedStatement removeElementPS = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.removeElementSynomym);
		removeElementPS.setInt(1,termID);
		removeElementPS.setNString(2,elem);
		removeElementPS.execute();
		removeElementPS.close();
	}
	
	public int addSource(String source) {
		{
			try {
				PreparedStatement idsourcePS = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.selectSourcesIDByName);
				idsourcePS.setNString(1,source);
				ResultSet rs = idsourcePS.executeQuery();
				if(rs.next()){ return rs.getInt(1);}
				rs.close();
				idsourcePS.close();
				PreparedStatement insertSourcePS = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.insertSource);
				insertSourcePS.setNString(1,source);
				insertSourcePS.execute();
				insertSourcePS.close();
				return HelpDatabase.getNextInsertTableID(GlobalTablesName.sources)-1;				
			} catch (Exception e) {
				logger.error(e.getMessage());
				return -1;
			}
		}
	}
	
	public IResourceElementSet<IResourceElement> getTermSynomns(int termID) throws SQLException, DatabaseLoadDriverException {	
		IResourceElementSet<IResourceElement> res = new ResourceElementSet<IResourceElement>(); 
		PreparedStatement getTermSynPS = GlobalOptions.database.getConnection().prepareStatement(QueriesResources.selectElementSynByID);
		getTermSynPS.setInt(1,termID);
		ResultSet rs = getTermSynPS.executeQuery();
		while(rs.next())
		{
			IResourceElement elem = new ResourceElement(rs.getInt(1),rs.getString(2),-1,"");
			res.addElementResource(elem);
		}
		rs.close();
		getTermSynPS.close();
		return res;
	}
	
	public List<IExternalID> getexternalIDandSorceIDandSource(int termID) throws SQLException, DatabaseLoadDriverException {
		List<IExternalID> list = new ArrayList<IExternalID>();	
		PreparedStatement getExternalIDandSource = GlobalOptions.database.getConnection().prepareStatement(QueriesDics.selectExternalIDandSource);
		getExternalIDandSource.setInt(1,termID);
		ResultSet rs = getExternalIDandSource.executeQuery();
		while(rs.next())
		{
			list.add(new ExternalID(rs.getString(1), rs.getString(3),rs.getInt(2)));
		}
		rs.close();
		getExternalIDandSource.close();
		return list;
	}
	
	public static IResource<IResourceElement> getTermResource(int termID) throws SQLException, DatabaseLoadDriverException
	{
		IResource<IResourceElement> resource = null;	
		PreparedStatement getExternalIDandSource =  Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.getTermResources);
		getExternalIDandSource.setInt(1,termID);
		ResultSet rs = getExternalIDandSource.executeQuery();
		if(rs.next())
		{
			String type = rs.getString(4);
			if(type.equals(GlobalOptions.resourcesDictionaryName))
			{
				resource = new Dictionary(rs.getInt(1), rs.getString(2), rs.getString(3));
			}
			else if(type.equals(GlobalOptions.resourcesLookupTableName))
			{
				resource = new LookupTable(rs.getInt(1), rs.getString(2), rs.getString(3));
			}
			else if(type.equals(GlobalOptions.resourcesRuleSetName))
			{
				resource = new RulesSet(rs.getInt(1), rs.getString(2), rs.getString(3));
			}
			else if(type.equals(GlobalOptions.resourcesOntologyName))
			{
				resource = new Ontology(rs.getInt(1), rs.getString(2), rs.getString(3));
			}
		}
		rs.close();
		getExternalIDandSource.close();

		return resource;
	}
	
	
	protected void memoryAndProgress(int step, int total) {
		System.out.println((GlobalOptions.decimalformat.format((double)step/ (double) total * 100)) + " %...");
		Runtime.getRuntime().gc();
		System.out.println((Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory())/(1024*1024) + " MB ");
	}

	
	public Map<String, GenericPair<Integer, Integer>> getSynonyms() {
		return synonyms;
	}
	
	
	public void notifyViewObservers(){
		setChanged();
		notifyObservers();
	}


	public String getName() {
		return name;
	}

	public String getInfo() {
		return info;
	}

	public int getID() {
		return id;
	}
	
	public void setName(String name) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement ps = GlobalOptions.database.getConnection().prepareStatement(QueriesResources.renameResource);
		ps.setInt(2, getID());
		ps.setString(1,name);
		ps.execute();
		ps.close();
		this.name = name;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public Map<String, GenericPair<Integer, Integer>> getShortElems() {
		return terms;
	}
	
	public boolean existExternalID(int termID,IExternalID ext) throws SQLException {
		existExternalIDTerm.setInt(1, termID);
		existExternalIDTerm.setString(2,ext.getExternalID());
		existExternalIDTerm.setInt(3, ext.getSourceID());
		ResultSet rs = existExternalIDTerm.executeQuery();

		if(rs.next())
		{
			rs.close();
			return true;
		}
		else
		{
			rs.close();
			return false;
		}
	}

	public static String convertClassesToResourceProperties(Set<Integer> classes) {
		String classesStr = new String();
		for(Integer classID:classes)
		{
			classesStr = classesStr.concat(classID+",");
		}
		if(classesStr.length() > 0)
			classesStr = classesStr.substring(0,classesStr.length()-1);
		return classesStr;
	}

}
