package pt.uminho.anote2.datastructures.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import pt.uminho.anote2.core.database.IDatabase;

public class SQLLiteDatabase extends ADatabase implements IDatabase{

	
	
	public SQLLiteDatabase(String host, String port, String schema, String user, String pwd)
	{
		super(host,port,schema,user,pwd);
		this.setDriverClassName("org.sqlite.JDBC");		
	}
	
	
	public void openConnection()
	{
		try {
//			if(super.getConnection().isClosed())
//			{
				this.loadDriver();
				String url_db_connection = "jdbc:sqlite:" + this.getSchema();
				this.setConnection(DriverManager.getConnection(url_db_connection));
//			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	public static void main(String[] args) throws SQLException {
		 SQLLiteDatabase sql = new SQLLiteDatabase("", "", "./conf/anote2.db", "","");
		 Connection conn = sql.getConnection();
		 sql.createDataBase("./conf/sqlite1.sql");	 
	}

}
