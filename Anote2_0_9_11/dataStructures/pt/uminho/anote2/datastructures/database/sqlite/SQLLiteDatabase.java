package pt.uminho.anote2.datastructures.database.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import pt.uminho.anote2.core.database.DataBaseTypeEnum;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.ADatabase;

public class SQLLiteDatabase extends ADatabase implements IDatabase{

	
	
	public SQLLiteDatabase(String host, String port, String schema, String user, String pwd)
	{
		super(host,port,schema,user,pwd,DataBaseTypeEnum.MYSQL);
		this.setDriverClassName("org.sqlite.JDBC");		
	}
	
	
	public void openConnection() throws DatabaseLoadDriverException
	{
		if(!isLoadDriver())
		{
			this.loadDriver();
			setLoadDriver(true);
		}
		try {
				String url_db_connection = "jdbc:sqlite:" + this.getSchema();
				this.setConnection(DriverManager.getConnection(url_db_connection));
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}


	public Connection getNeWConnection() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean createDataBase() {
		return false;
		
	}


	@Override
	public boolean existDatabase() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void fillDataBaseTables() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void updateDatabase() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean isDatabaseOutOfDate() {
		// TODO Auto-generated method stub
		return false;
	}


}
