package pt.uminho.anote2.datastructures.database;

import java.sql.DriverManager;
import java.sql.SQLException;


public class MySQLDatabase extends ADatabase{

	public MySQLDatabase(String host, String port, String schema, String user, String pwd)
	{
		super(host,port,schema,user,pwd);
		this.setDriverClassName("com.mysql.jdbc.Driver");		
	}

	public void openConnection()
	{
		try {
				this.loadDriver();
				String url_db_connection = "jdbc:mysql://" + this.getHost() + ":" + this.getPort() + "/" + this.getSchema();
				this.setConnection(DriverManager.getConnection(url_db_connection, this.getUser(), this.getPwd()));
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
}
