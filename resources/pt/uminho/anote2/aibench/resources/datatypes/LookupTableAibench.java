package pt.uminho.anote2.aibench.resources.datatypes;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pt.uminho.anote2.aibench.utils.operations.TimeLeftProgress;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.resources.lookuptable.LookupTable;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;
import pt.uminho.anote2.resource.lookuptables.ILookupTable;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;

@Datatype(structure = Structure.SIMPLE,namingMethod="getNameAibench",removable=true)
public class LookupTableAibench extends LookupTable{

	public LookupTableAibench(IDatabase db, int id, String name, String info) {
		super(db, id, name, info);
	}
	
	public String getNameAibench()
	{
		return getName();
	}
	
	public void notifyViewObservers(){
		this.setChanged();
		this.notifyObservers();
	}
	
	public boolean merge(ILookupTable dicSnd,TimeLeftProgress progress,boolean cancel) 
	{
		Connection connection = getDb().getConnection();	
		if(connection==null)
		{
			return false;
		}
		else
		{
			progress.setTimeString("Load Terms Destinary Dictionary");
			loadAllTerms();
			progress.setTimeString("Load Terms Source Dictionary");
			IResourceElementSet<IResourceElement> terms = dicSnd.getResourceElements();
			Set<Integer> classesSnd = dicSnd.getClassContent();
			Set<Integer> classes1st = getClassContent();
			Map<Integer, String> classesDB = new HashMap<Integer, String>();
			try {
				classesDB = getClassIDClassOnDatabase(getDb());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(Integer classID:classesSnd)
			{
				if(!classes1st.contains(classID))
				{
					this.addResourceContent(classesDB.get(classID));
				}
			}
			int total = terms.size();
			int step = 0;
			long start = GregorianCalendar.getInstance().getTimeInMillis();
			long end;
			long totalTime;
			for(IResourceElement elem:terms.getElements())
			{
				this.addElement(elem);
				if(step%500==0)
				{
					end = GregorianCalendar.getInstance().getTimeInMillis();
					totalTime = end-start;
					progress.setTime(totalTime, step, total);
					progress.setProgress((float) step / (float) total);
				}
				step++;
			}
			deleteTerms();
			return true;
		}
	}
	

}
