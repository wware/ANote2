package pt.uminho.anote2.datastructures.resources.rule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.resources.rules.QueriesRules;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.resources.ResourceElementSet;
import pt.uminho.anote2.datastructures.resources.ResourcesHelp;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;
import pt.uminho.anote2.resource.rules.IRule;

public class RulesSet extends ResourcesHelp implements IRule{
	
	public RulesSet(IDatabase db, int id, String name, String info) {
		super(db, id, name, info);
	}

	public boolean removeElement(IResourceElement ruleElement) {
		return super.removeElement(ruleElement);
	}

	public boolean updateElement(IResourceElement ruleElement) {
		return super.updateElement(ruleElement);
	}
	
	public int compareTo(IRule dic)
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

	@Override
	public String getType() {
		return "Rules";
	}
	
	public boolean addElement(IResourceElement elem)
	{
		initExistElem();
		if(addSimpleElement(elem))
		{
			int elementID = HelpDatabase.getNextInsertTableID(getDb(),"resource_elements");
			int findPriorety = findMAxPriorety();
			insertPrioretyInElement(elementID-1,findPriorety+1);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	protected boolean addSimpleElement(IResourceElement elem)
	{
		Connection connection = getDb().getConnection();
		if(connection == null){ return false;}
		try {
			addElem.setString(1,elem.getTerm());
			addElem.setInt(2,elem.getTermClassID());
			addElem.execute();

		} catch (SQLException e) {
			return false;
		}
		
		return true;
	}
	
	
	protected void insertPrioretyInElement(int elementID,int priorety)
	{
		Connection con = getDb().getConnection();
		if(con==null){}
		else
		{
			try {
				PreparedStatement findMAxPrioretyPS = con.prepareStatement(QueriesRules.insertPrioretyOnRule);
				findMAxPrioretyPS.setInt(1,elementID);
				findMAxPrioretyPS.setInt(2,getId());
				findMAxPrioretyPS.setInt(3,priorety);
				findMAxPrioretyPS.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}		
		}
	}
	
	protected int findMAxPriorety()
	{
		int priorety = 0;	
		Connection con = getDb().getConnection();
		if(con==null){}
		else
		{
			try {
				PreparedStatement findMAxPrioretyPS = con.prepareStatement(QueriesRules.findMAxPriorety);
				findMAxPrioretyPS.setInt(1,getId());
				ResultSet rs = findMAxPrioretyPS.executeQuery();
				if(rs.next())
				{
					if(rs.getString(1)==null)
					{
						
					}
					else
					{
						return rs.getInt(1);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}		
		}	
		return priorety;
	}
	
	public IResourceElementSet<IResourceElement> getResourceElements() {
		IResourceElementSet<IResourceElement> resourceElems = new ResourceElementSet<IResourceElement>();
		IResourceElement elem = null;
		
		Connection connection = getDb().getConnection();
		
		String term=null;
		int idResourceElement,classID;	
		if(connection == null)
		{
			return null;
		}
		else
		{	
			int priorety;
			try {		
				Map<Integer,String> classes = getClassIDClassOnDatabase(getDb());		
				PreparedStatement getElems = connection.prepareStatement(QueriesRules.getAllElementsWhitPriorety);
				getElems.setInt(1,getId());	
				ResultSet rs = getElems.executeQuery();
				while(rs.next())
				{
					idResourceElement = rs.getInt(1);
					classID = rs.getInt(3);
					term = rs.getString(4);
					priorety = rs.getInt(7);
					elem = new ResourceElement(idResourceElement,term,classID,classes.get(classID),priorety);
					resourceElems.addElementResource(elem);
				}
				rs.close();		
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		return resourceElems;
	}
	
	public boolean merge(IRule ruleSnd) 
	{
		Connection connection = getDb().getConnection();	
		if(connection==null)
		{
			return false;
		}
		else
		{
			loadAllTerms();
			
			Set<Integer> classesSnd = ruleSnd.getClassContent();
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
			IResourceElementSet<IResourceElement> terms = ruleSnd.getResourceElements();;	
			for(IResourceElement elem:terms.getElements())
			{
				this.addElement(elem);
			}
			deleteTerms();
			return true;
		}
	}




}
