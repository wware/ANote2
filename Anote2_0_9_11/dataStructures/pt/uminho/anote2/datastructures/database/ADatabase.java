package pt.uminho.anote2.datastructures.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import pt.uminho.anote2.core.database.DataBaseTypeEnum;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.QueriesGeneral;
import pt.uminho.anote2.datastructures.database.schema.DatabaseTablesName;


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
	private DataBaseTypeEnum databaseType;
	public static String databaseSplitFields = "|";
	public static int numberOfTables = 47;
	
	private boolean loadDriver = false;

	
	public ADatabase(String host, String port, String schema, String user, String pass,DataBaseTypeEnum databseType) {
		this.host = host;
		this.port = port;
		this.schema = schema;
		this.user = user;
		this.pwd = pass;
		this.databaseType = databseType;
		this.connection=null;
	}

	public abstract void openConnection() throws DatabaseLoadDriverException, SQLException;
	
	protected void loadDriver() throws DatabaseLoadDriverException
	{	
		try {
			Class.forName(this.getDriverClassName()).newInstance();
		} catch (Exception e) {
			throw new DatabaseLoadDriverException(e);
		}				
	}
	
	public boolean equals(Object database)
	{
		if(database == null)
			return false;
		if(database instanceof IDatabase)
		{
			IDatabase db = (IDatabase) database;
			if(!databaseType.equals(db.getDataBaseType()))
			{
				return false;
			}
			else if(!host.equals(db.getHost()))
			{
				return false;
			}
			else if(!port.equals(db.getPort()))
			{
				return false;
			}
			else if(!schema.equals(db.getSchema()))
			{
				return false;
			}
			else if(!user.equals(db.getUser()))
			{
				return false;
			}
			else if(!pwd.equals(db.getPwd()))
			{
				return false;
			}
			return true;
		}
		else
			return false;
	}

	public void closeConnection() {

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
		str.append(this.databaseType.toString() + databaseSplitFields);
		str.append(this.host + databaseSplitFields);
		str.append(this.port + databaseSplitFields);
		str.append(this.schema + databaseSplitFields);
		str.append(this.user + databaseSplitFields);
		str.append(this.pwd);
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

	public Connection getConnection() throws DatabaseLoadDriverException, SQLException {
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
		if(timenow-starttime>timeout)
		{
			starttime = GregorianCalendar.getInstance().getTimeInMillis();
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
	
	public DataBaseTypeEnum getDataBaseType()
	{
		return this.databaseType;
	}
	
	@Override
	public boolean isfill() throws DatabaseLoadDriverException, SQLException {
			PreparedStatement fillDatabase = getConnection().prepareStatement(QueriesGeneral.showDatabaseListOfTable);
			fillDatabase.setString(1, getSchema());
			ResultSet rs = fillDatabase.executeQuery();
			Set<String> listOfTables = new HashSet<String>();
			while(rs.next())
			{
				listOfTables.add(rs.getString(1));
			}
			if(listOfTables.size()<ADatabase.numberOfTables && listOfTables.contains(DatabaseTablesName.resources) && listOfTables.contains(DatabaseTablesName.version) && listOfTables.contains(DatabaseTablesName.publications))
			{
				fillDatabase.close();
				rs.close();
				return true;
			}
			else
			{
				fillDatabase.close();
				rs.close();
				return false;
			}
	}

	public boolean isLoadDriver() {
		return loadDriver;
	}

	public void setLoadDriver(boolean loadDriver) {
		this.loadDriver = loadDriver;
	}
}