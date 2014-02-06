package pt.uminho.anote2.aibench.resources.datatypes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.queries.resources.rules.QueriesRules;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.resources.rule.RulesSet;
import pt.uminho.anote2.resource.IResourceElement;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;

@Datatype(structure = Structure.SIMPLE,namingMethod="getNameRules",removable=true)
public class RulesAibench extends RulesSet{
	

	
	public RulesAibench(IDatabase db, int id, String name, String info) {
		super(db, id, name, info);
	}
	
	public String getNameRules()
	{
		return getName();
	}
	
	public void notifyViewObservers(){
		this.setChanged();
		this.notifyObservers();
	}
	
	public void changePriorety(IResourceElement fst,IResourceElement snd)
	{
		int priorety = fst.getPriority();
		Connection con = getDb().getConnection();
		if(!(con==null))
		{
			try {
				PreparedStatement changePrioretyPS = con.prepareStatement(QueriesRules.changePriorety);
				changePrioretyPS.setInt(1, priorety);
				changePrioretyPS.setInt(2, snd.getID());
				changePrioretyPS.execute();
				changePrioretyPS.setInt(1, priorety-1);
				changePrioretyPS.setInt(2, fst.getID());
				changePrioretyPS.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}		
		}	
	}
	
	public List<IResourceElement> getRules() {
		List<IResourceElement> resourceElems = new ArrayList<IResourceElement>();
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
					resourceElems.add(elem);
				}
				rs.close();		
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		return resourceElems;
	}
}
