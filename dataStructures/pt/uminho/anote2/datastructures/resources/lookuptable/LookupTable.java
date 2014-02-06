package pt.uminho.anote2.datastructures.resources.lookuptable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import es.uvigo.ei.aibench.workbench.Workbench;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.resources.ResourcesHelp;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;
import pt.uminho.anote2.resource.lookuptables.ILookupTable;

public class LookupTable extends ResourcesHelp implements ILookupTable{

	public LookupTable(IDatabase db, int id, String name, String info) {
		super(db, id, name, info);

	}

	public boolean exportCSVFile(String pathFile) {		
		String entity,classe;
		Connection connection = getDb().getConnection();
		if(connection == null)
		{
			return false;
		}
		else
		{
			try {
				File file = new File(pathFile);
				if(!file.exists())
					file.createNewFile();
				PreparedStatement termInMemory = connection.prepareStatement(QueriesResources.selectResourceElementAndClass);
				termInMemory.setInt(1,getId());
				ResultSet rs = termInMemory.executeQuery();
				PrintWriter pw = new PrintWriter(file);				
				while(rs.next())
				{
					entity=rs.getString(1);
					classe=rs.getString(2);
					String line = entity+";"+classe;
					pw.write(line);
					pw.println();
				}
				pw.close();
				rs.close();			
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}
		return true;
	}

	public boolean importCVSFile(String pathFile) {	
		String line;
		File file = new File(pathFile);
		if(file==null || !file.exists())
		{
			return false;
		}
		else
		{
			FileReader fr;
			BufferedReader br;

			try {
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				
				while((line = br.readLine())!=null)
				{	
					String[] lin = line.split(";");
					int classID = addElementClass(lin[1]);
					addResourceContent(lin[1]);
					IResourceElement elem = new ResourceElement(-1,lin[0],classID,lin[1]);
					addElement(elem);
				}		
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}	
			return true;
		}
	}
	
	public int compareTo(ILookupTable dic)
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
		return "LookUpTable";
	}
	
	public boolean merge(ILookupTable looSnd) 
	{
		Connection connection = getDb().getConnection();	
		if(connection==null)
		{
			return false;
		}
		else
		{
			loadAllTerms();
			
			Set<Integer> classesSnd = looSnd.getClassContent();
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
			IResourceElementSet<IResourceElement> terms = looSnd.getResourceElements();;	
			for(IResourceElement elem:terms.getElements())
			{
				this.addElement(elem);
			}
			deleteTerms();
			return true;
		}
	}

}



