package pt.uminho.anote2.datastructures.database.startdatabase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import pt.uminho.anote2.core.database.DataBaseTypeEnum;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;

public class StartDatabase implements IDatabase{

	public StartDatabase() {
		super();
	}

	@Override
	public Connection getNeWConnection() throws DatabaseLoadDriverException,
			SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fillDataBaseTables() throws DatabaseLoadDriverException,
			SQLException, FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean createDataBase() throws DatabaseLoadDriverException,
			SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateDatabase() throws DatabaseLoadDriverException,
			SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDatabaseOutOfDate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean existDatabase() throws DatabaseLoadDriverException,
			SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openConnection() throws DatabaseLoadDriverException,
			SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeConnection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDriverClassName(String driverName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Connection getConnection() throws DatabaseLoadDriverException,
			SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSchema() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPort() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPwd() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataBaseTypeEnum getDataBaseType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isfill() throws DatabaseLoadDriverException, SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	
	public String toString()
	{
		return new String();
	}

}
