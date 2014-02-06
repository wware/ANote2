package pt.uminho.anote2.datastructures.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import pt.uminho.anote2.core.database.IDatabase;

import com.ibm.icu.util.GregorianCalendar;

public abstract class ADatabase implements IDatabase {

	private String host;
	private String port;
	private String schema;
	private String user;
	private String pwd;
	private Connection connection;
	private String driverClassName;
	public static final long timeout = 10000000;
	public static long starttime = 0;
	
	public ADatabase(String host, String port, String schema, String user, String pass) {
		this.host = host;
		this.port = port;
		this.schema = schema;
		this.user = user;
		this.pwd = pass;
		this.connection=null;
	}
	
	public ADatabase(List<String> elements)	{
		this.host = elements.get(0);
		this.port = elements.get(1);
		this.schema = elements.get(2);
		this.user = elements.get(3);
		this.pwd = elements.get(4);
		this.connection=null;
	}
	
	public abstract void openConnection();
	
	protected void loadDriver()
	{	
		try {
			Class.forName(this.getDriverClassName()).newInstance();				
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
	}
	

	public void closeConnection() {

	}
	
	
	public boolean exists(String statement)
	{
		boolean exist=false;
		try {
			Statement stmt = this.getConnection().createStatement();
			ResultSet resultSet = stmt.executeQuery(statement);
			
			exist=resultSet.first();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.closeConnection();
		return exist;
	}
	
	public ResultSet executeQuery(String statement)
	{
		ResultSet resultSet = null;
		try {
			Statement stmt = this.getConnection().createStatement();
			 resultSet = stmt.executeQuery(statement);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	
	public void executeUpdate(String statement)
	{
		try {
			Statement stmt = this.getConnection().createStatement();
			stmt.execute(statement);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void createDataBase(String filePath){		 
		 try {	 
			 FileReader fr = new FileReader(new File(filePath));
			 BufferedReader br = new BufferedReader(fr);				
			 StringBuffer stat = new StringBuffer();			 
			 String str = br.readLine();
			 Statement statement;		 
			 while(str!=null)
			 {
				 if(str.startsWith("--"))
				 {
					 
				 }	 
				 else if(str.compareTo("")!=0 )
				 {
					 stat.append(" "+str);
					 if(str.contains(";"))
					 {
						 statement = connection.createStatement();
						 statement.execute(stat.toString());
						 stat=new StringBuffer();
					 }
				 }
				 str = br.readLine();
			 }
			 			 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}			 
	}
	
	public boolean equals(ADatabase session){
		if(this.host.compareTo(session.getHost())==0
				&& this.port.compareTo(session.getPort())==0
				&& this.schema.compareTo(session.getSchema())==0
				&& this.user.compareTo(session.getUser())==0
				&& this.pwd.compareTo(session.getPwd())==0
			)
		{
			return true;
		}
		return false;
	}

	public String toString(){
		StringBuffer str = new StringBuffer();
		
		str.append(this.host + " | ");
		str.append(this.port + " | ");
		str.append(this.schema + " | ");
		str.append(this.user + " | ");
		str.append(this.pwd + "\n");
		
		return str.toString();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Connection getConnection() {
		if(connection==null)
		{
			this.openConnection();
			starttime = GregorianCalendar.getInstance().getTimeInMillis();
		} else{
			try {
				if(connection.prepareStatement("")==null)
				{
					this.openConnection();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

	}
	long timenow = GregorianCalendar.getInstance().getTimeInMillis();
	if(timenow-starttime>timenow)
	{
		this.openConnection();
	}
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
}
