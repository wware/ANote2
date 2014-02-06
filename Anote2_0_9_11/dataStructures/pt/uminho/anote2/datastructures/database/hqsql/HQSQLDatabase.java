package pt.uminho.anote2.datastructures.database.hqsql;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import pt.uminho.anote2.core.database.DataBaseTypeEnum;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.ADatabase;

public class HQSQLDatabase extends ADatabase implements IDatabase{

	public HQSQLDatabase(String host, String port, String schema, String user,
			String pass) {
		super(host, port, schema, user, pass,DataBaseTypeEnum.MYSQL);
		this.setDriverClassName("org.hsqldb.jdbcDriver");		

	}

	@Override
	public Connection getNeWConnection() throws DatabaseLoadDriverException {
		if(!isLoadDriver())
		{
			this.loadDriver();
			setLoadDriver(true);
		}
		String url_db_connection = "jdbc:hsqldb:file:" + getSchema();
		try {
			return DriverManager.getConnection(url_db_connection, this.getUser(), this.getPwd());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void openConnection() throws DatabaseLoadDriverException {
		if(!isLoadDriver())
		{
			this.loadDriver();
			setLoadDriver(true);
		}
		String url_db_connection = "jdbc:hsqldb:file:" + getSchema();
		try {
			this.setConnection(DriverManager.getConnection(url_db_connection, this.getUser(), this.getPwd()));
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean existDatabase() {
		File file = new File(getSchema());
		return file.exists();
	}
	
	public boolean createDataBase()
	{
		File file = new File(getSchema());
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
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
