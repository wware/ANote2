package pt.uminho.anote2.datastructures.database;

import pt.uminho.anote2.core.database.IDatabase;

public class DatabaseManager {
	private IDatabase database;
	
	public DatabaseManager(IDatabase database)
	{
		this.setDatabase(database);
	}

	public IDatabase getDatabase() {
		return database;
	}

	public void setDatabase(IDatabase database) {
		this.database = database;
	}
	
	public String toString()
	{
		return database.toString();
	}
}
