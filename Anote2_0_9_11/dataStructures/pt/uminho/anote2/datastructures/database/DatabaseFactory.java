package pt.uminho.anote2.datastructures.database;

import pt.uminho.anote2.core.database.DataBaseTypeEnum;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.mysql.MySQLDatabase;

/**
 * Class that contains some static methods to build {@link IDatabase}
 * 
 * @author Hugo Costa
 *
 */
public class DatabaseFactory {
	
	public static IDatabase createDatabase(DataBaseTypeEnum databatype,String host,String port,String schema,String user,String pwd)
	{
		if(databatype == DataBaseTypeEnum.MYSQL)
		{
			return new MySQLDatabase(host, port, schema, user, pwd);
		}
//		else if(databatype == DataBaseTypeEnum.HQSQL)
//		{
//			return new HQSQLDatabase(host, port, schema, user, pwd);
//
//		}
//		else if(databatype == DataBaseTypeEnum.SQLITE)
//		{
//			return new SQLLiteDatabase(host, port, schema, user, pwd);
//		}
		return null;
	}

	public static IDatabase createDatabase(String database) {
		String[] databaseFields = database.split("\\|");
		if(databaseFields!=null && databaseFields.length >= 5)
		{
			
			DataBaseTypeEnum databatype = DataBaseTypeEnum.convertStringInDataBaseTypeEnum(databaseFields[0]);
			String host = databaseFields[1];
			String port = databaseFields[2];
			String schema = databaseFields[3];
			String user = databaseFields[4];
			String pwd = new String();
			if(databatype  == DataBaseTypeEnum.MYSQL)
			{
				if(databaseFields.length==6)
				{
					pwd = databaseFields[5];
				}
				else if(databaseFields.length==5)
				{
					pwd = "";
				}
				return new MySQLDatabase(host, port, schema, user, pwd);
			}
//			else if(databatype == DataBaseTypeEnum.HQSQL)
//			{
//				return new HQSQLDatabase(host, port, schema, user, pwd);
//
//			}
//			else if(databatype == DataBaseTypeEnum.SQLITE)
//			{
//				return new SQLLiteDatabase(host, port, schema, user, pwd);
//			}
		}
		return null;
	}
	
	public static DatabaseManager createDatabaseManager(String database) {
		String[] databaseFields = database.split("\\|");
		if(databaseFields!=null && databaseFields.length >= 5)
		{
			
			DataBaseTypeEnum databatype = DataBaseTypeEnum.convertStringInDataBaseTypeEnum(databaseFields[0]);
			String host = databaseFields[1];
			String port = databaseFields[2];
			String schema = databaseFields[3];
			String user = databaseFields[4];
			String pwd = new String();
			if(databatype  == DataBaseTypeEnum.MYSQL)
			{
				if(databaseFields.length==6)
				{
					pwd = databaseFields[5];
				}			else if(databaseFields.length==5)
				{
					pwd = "";
				}
				return new DatabaseManager(new MySQLDatabase(host, port, schema, user, pwd));
			}
//			else if(databatype == DataBaseTypeEnum.HQSQL)
//			{
//				return new HQSQLDatabase(host, port, schema, user, pwd);
//
//			}
//			else if(databatype == DataBaseTypeEnum.SQLITE)
//			{
//				return new SQLLiteDatabase(host, port, schema, user, pwd);
//			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		String createdatabase = "MYSQL|localhost|3306|teste_db_creation|root|";
		createDatabaseManager(createdatabase);
	}
	
}
