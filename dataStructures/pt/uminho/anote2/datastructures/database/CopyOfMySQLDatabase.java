package pt.uminho.anote2.datastructures.database;

import java.sql.DriverManager;
import java.sql.SQLException;


public class CopyOfMySQLDatabase extends ADatabase{

	public CopyOfMySQLDatabase(String host, String port, String schema, String user, String pwd)
	{
		super(host,port,schema,user,pwd);
		this.setDriverClassName("com.mysql.jdbc.Driver");		
	}

	public void openConnection()
	{
		try {
//			if(super.getConnection().isClosed())
//			{
				this.loadDriver();
				String url_db_connection = "jdbc:mysql://" + this.getHost() + ":" + this.getPort() + "/" + this.getSchema();
				this.setConnection(DriverManager.getConnection(url_db_connection, this.getUser(), this.getPwd()));
//			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
}
