package pt.uminho.anote2.datastructures.database;

import java.util.ArrayList;
import java.util.List;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.startdatabase.StartDatabase;

/**
 * Class that represent a Databse Archive
 * 
 * @author Hugo Costa
 *
 */
public class DatabaseArchive {
	
	private List<IDatabase> databaseArchive;
	private static String separator = "-";
	
	public DatabaseArchive()
	{
		databaseArchive = new ArrayList<IDatabase>();
	}
	
	public DatabaseArchive(String databaseArchiveString)
	{
		this();
		String[] databseString =  databaseArchiveString.split(separator);
		for(String database :databseString)
		{
			addDatabaseToArchive(DatabaseFactory.createDatabase(database));
		}
	}
	
	/**
	 * Add {@link IDatabase} to Archive
	 * 
	 * @param database
	 */
	public void addDatabaseToArchive(IDatabase database)
	{
		if(notExistDatabaseInArchive(database))
			this.databaseArchive.add(database);
	}
	
	private boolean notExistDatabaseInArchive(IDatabase database) {
		if(database == null)
		{
			return false;
		}
		if(database instanceof StartDatabase)
		{
			return false;
		}
		for(Object databaseInArchive : databaseArchive)
		{
			if(database.equals(databaseInArchive))
			{
				return false;
			}
		}
		return true;
	}

	public String toString()
	{
		StringBuffer str = new StringBuffer();
		for(IDatabase db : databaseArchive)
		{
			str.append(db.toString());
			str.append(separator);
		}
		return str.toString().substring(0,str.length()-1);
	}

	/**
	 * Return A {@link List} of {@link IDatabase}
	 * 
	 * @return
	 */
	public List<IDatabase> getDatabaseArchive() {
		return databaseArchive;
	}
	
	public boolean equals(Object obj)
	{
		if(obj instanceof List)
		{
			List objList = (List) obj;
			if(databaseArchive.size() != objList.size())
				return false;
			for(int i=0;i<objList.size();i++)
			{
				Object objInside = objList.get(i);
				if(objInside instanceof IDatabase)
				{
					IDatabase database = (IDatabase) objInside;
					if(!database.equals(databaseArchive.get(i)))
					{
						return false;
					}
				}
				else
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Remove {@link IDatabase} from Archive given index
	 * 
	 * @param databaseListIndex
	 */
	public void removedatabase(int databaseListIndex) {
		databaseArchive.remove(databaseListIndex);
	}
	
	
}
