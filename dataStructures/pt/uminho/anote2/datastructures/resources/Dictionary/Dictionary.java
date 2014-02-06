package pt.uminho.anote2.datastructures.resources.Dictionary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.database.queries.resources.dics.QueriesDics;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.resources.ResourceElementSet;
import pt.uminho.anote2.datastructures.resources.ResourcesHelp;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;
import pt.uminho.anote2.resource.dictionary.IDictionary;

/**
 * 
 * @author Hugo Costa
 *
 */
public class Dictionary extends ResourcesHelp implements IDictionary{

	public Dictionary(IDatabase db,int id,String name,String info)
	{
		super(db,id,name,info);
	}

	public boolean merge(IDictionary dicSnd) 
	{
		Connection connection = getDb().getConnection();	
		if(connection==null)
		{
			return false;
		}
		else
		{
			loadAllTerms();
			
			Set<Integer> classesSnd = dicSnd.getClassContent();
			Set<Integer> classes1st = getClassContent();
			Map<Integer, String> classesDB = new HashMap<Integer, String>();
			try {
				classesDB = getClassIDClassOnDatabase(getDb());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			for(Integer classID:classesSnd)
			{
				if(!classes1st.contains(classID))
				{
					this.addResourceContent(classesDB.get(classID));
				}
			}
			int nextResourceElementID = HelpDatabase.getNextInsertTableID(this.getDb(),"resource_elements");	
			IResourceElementSet<IResourceElement> terms = dicSnd.getResourceElements();;	
			for(IResourceElement elem:terms.getElements())
			{
				if(this.addElement(elem))
				{
					IResourceElementSet<IResourceElement> syns = getTermSynomns(elem.getID());
					for(IResourceElement elem2:syns.getElements())
					{
						addSynomns(new ResourceElement(nextResourceElementID,elem2.getTerm(),-1,""));
					}
					this.addExternalID(nextResourceElementID, elem.getExtenalID(),elem.getExternalIDSourceID());
					nextResourceElementID++;
				}
				else
				{
					this.addExternalID(nextResourceElementID, elem.getExtenalID(),elem.getExternalIDSourceID());
				}
			}
			deleteTerms();
			return true;
		}
	}
	
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
				Map<Integer,String> classes = getClassIDClassOnDatabase(getDb());		
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
	
	
	
	public IResourceElementSet<IResourceElement> getTermSynomns(int termID) {
		Connection connection = getDb().getConnection();
		if(connection==null){return null;}		
		IResourceElementSet<IResourceElement> res = new ResourceElementSet<IResourceElement>(); 
		try {
			
			PreparedStatement getTermSynPS = connection.prepareStatement(QueriesResources.selectElementSynByID);
			getTermSynPS.setInt(1,termID);
			ResultSet rs = getTermSynPS.executeQuery();
			while(rs.next())
			{
				IResourceElement elem = new ResourceElement(rs.getInt(1),rs.getString(2),-1,"");
				res.addElementResource(elem);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	

	public int addSource(String source) {
		Connection connection = getDb().getConnection();
		if(connection == null)
		{
			return -1;
		}
		else
		{
			try {
			PreparedStatement idsourcePS = connection.prepareStatement(QueriesResources.selectSourcesIDByName);
			idsourcePS.setString(1,source);
			ResultSet rs = idsourcePS.executeQuery();
				if(rs.next()){ return rs.getInt(1);}
				PreparedStatement insertSourcePS = connection.prepareStatement(QueriesResources.insertSource);
				insertSourcePS.setString(1,source);
				insertSourcePS.execute();
				return HelpDatabase.getNextInsertTableID(getDb(),"sources")-1;				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		getDb().closeConnection();
		return -1;
	}
	
	public int compareTo(IDictionary dic)
	{
		if(this.getId()==dic.getId())
		{
			return 0;
		}
		else if(this.getId()<=dic.getId())
		{
			return -1;
		}
		return 1;
	}
	
	public String toString()
	{
		String info = new String();
		info = info + getId() + "-" + getName() + " (" + getType() + ") " + "Note : " + getInfo();
		return info;
	}

	public String getType() {
		return "Dictionary";
	}

	public IResourceElementSet<IResourceElement> getAllTermsAndSynonyms() {
		
		Map<Integer, String> classes;
		IResourceElementSet<IResourceElement> terms = new ResourceElementSet<IResourceElement>();		
		Connection connection = getDb().getConnection();
		if(connection==null)
		{
			return null;
		}
		try {
			classes = getClassIDClassOnDatabase(getDb());
			PreparedStatement getSyn = getDb().getConnection().prepareStatement(QueriesDics.termSyn);
			getSyn.setInt(1,getId());

			ResultSet rs = getSyn.executeQuery();
			int termID,classID;
			String termName,classe;
			while(rs.next())
			{
				termID = rs.getInt(1);
				termName = rs.getString(2);
				classID = rs.getInt(3);
				classe = classes.get(classID);
				IResourceElement elem = new ResourceElement(termID,termName,classID,classe);
				terms.addElementResource(elem);
			}
			rs.close();
			PreparedStatement getTems = getDb().getConnection().prepareStatement(QueriesDics.getTerms);
			getTems.setInt(1,getId());
			ResultSet rs2 = getTems.executeQuery();
			while(rs2.next())
			{
				termID = rs2.getInt(1);
				termName = rs2.getString(2);
				classID = rs2.getInt(3);
				classe = classes.get(classID);
				IResourceElement elem = new ResourceElement(termID,termName,classID,classe);
				terms.addElementResource(elem);
			}
			rs2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return terms;
	}

	/**
	 * Method that filter resources elements by class
	 * 
	 * @param termClass
	 * @return
	 */
	public IResourceElementSet<IResourceElement> getAllTermByClass(String termClass) {

		IResourceElementSet<IResourceElement> dicElems = new ResourceElementSet<IResourceElement>();


		
		String term=null;
		IResourceElement dicElem = null;
		int elementID;
		try {
			Connection con = getDb().getConnection();
			if(con==null)
			{
				return null;
			}
			int classID = findClassID(termClass);
			if(classID==-1)
			{
				return null;
			}

			PreparedStatement getTemsClass = con.prepareStatement(QueriesDics.selectTermsClass);
			getTemsClass.setInt(1,getId());
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
			PreparedStatement getTemsSynClass = con.prepareStatement(QueriesDics.selectSynTermsClass);
			getTemsSynClass.setInt(1,getId());
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
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return dicElems;
	}
	

	@Override
	public IResourceElementSet<IResourceElement> getAllTermByClass(int classID) {
	IResourceElementSet<IResourceElement> dicElems = new ResourceElementSet<IResourceElement>();
		

		
		String term=null;
		IResourceElement dicElem = null;
		int elementID;
		try {
			
			Connection con = getDb().getConnection();
			if(con==null)
			{
				return null;
			}
			
			String termClasse = getClassIDClassOnDatabase(getDb()).get(classID);
			if(termClasse==null)
			{
				return null;
			}
			PreparedStatement getTemsClass = con.prepareStatement(QueriesDics.selectTermsClass);
			getTemsClass.setInt(1,getId());
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
			PreparedStatement getTemsSynClass = con.prepareStatement(QueriesDics.selectSynTermsClass);
			getTemsSynClass.setInt(1,getId());
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
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return dicElems;
	}

}
