package pt.uminho.anote2.datastructures.database.mysql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import pt.uminho.anote2.core.database.DataBaseTypeEnum;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.ADatabase;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.UpdateDatabaseHelp;
import pt.uminho.anote2.datastructures.database.queries.QueriesGeneral;
import pt.uminho.anote2.datastructures.utils.FileHandling;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;

/**
 * Class that represent a Mysql Database Server 
 * 
 * @author Hugo Costa
 *
 */
public class MySQLDatabase extends ADatabase{
	
	
	public MySQLDatabase(String host, String port, String schema, String user, String pwd)
	{
		super(host,port,schema,user,pwd,DataBaseTypeEnum.MYSQL);
		this.setDriverClassName("com.mysql.jdbc.Driver");		
	}

	public void openConnection() throws DatabaseLoadDriverException, SQLException
	{
		if(!isLoadDriver())
		{
			this.loadDriver();
			setLoadDriver(true);
		}
		String url_db_connection = "jdbc:mysql://" + this.getHost() + ":" + this.getPort() + "/" + this.getSchema();
		this.setConnection(DriverManager.getConnection(url_db_connection, this.getUser(), this.getPwd()));
	}

	public Connection getNeWConnection() throws DatabaseLoadDriverException, SQLException{
		if(!isLoadDriver())
		{
			this.loadDriver();
			setLoadDriver(true);
		}
		String url_db_connection = "jdbc:mysql://" + this.getHost() + ":" + this.getPort() + "/" + this.getSchema();
		return DriverManager.getConnection(url_db_connection, this.getUser(), this.getPwd());
	}

	@Override
	public boolean existDatabase() throws DatabaseLoadDriverException, SQLException {
		IDatabase dbtest = new MySQLDatabase(this.getHost(), this.getPort(),"INFORMATION_SCHEMA",getUser(),getPwd());
		dbtest.openConnection();
		ResultSet result = null;
		Connection connect = dbtest.getConnection();		
		result = connect.createStatement().executeQuery("SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '"+getSchema()+"'");
		while(result.next())
		{
			if(result.getString(1).equals(getSchema()))
			{
				dbtest.closeConnection();
				return true;	
			}
			else
			{
				return false;
			}
		}
		dbtest.closeConnection();
		return false;
	}

	public boolean createDataBase() throws DatabaseLoadDriverException, SQLException 
	{
		IDatabase dbtest = new MySQLDatabase(this.getHost(), this.getPort(),"mysql",getUser(),getPwd());
		PreparedStatement createDatbase;
		createDatbase = dbtest.getConnection().prepareStatement(QueriesGeneral.createDatabse + getSchema() + " DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ");
		createDatbase.execute();
		return true;
		
	}

	public boolean createDataBase(String user, String password) throws DatabaseLoadDriverException, SQLException
	{
		IDatabase dbtest = new MySQLDatabase(this.getHost(), this.getPort(),"mysql",user,password);
		PreparedStatement createDatbase;
		createDatbase = dbtest.getConnection().prepareStatement(QueriesGeneral.createDatabse + getSchema() + " DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ");
		createDatbase.execute();
		return true;
	}
	
	public void fillDataBaseTables() throws DatabaseLoadDriverException, SQLException, IOException{		 
		FileReader fr = new FileReader(new File( GlobalOptions.mysqlDatabaseFile));
		BufferedReader br = new BufferedReader(fr);				
		StringBuffer stat = new StringBuffer();			 
		String str = br.readLine();
		Statement statement  = getConnection().createStatement();;		 
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
					statement.execute(stat.toString());
					stat=new StringBuffer();
				}
			}
			str = br.readLine();
		}		 
	}

	private void updateDatabaseStepFrom(String filePath) throws DatabaseLoadDriverException, SQLException, IOException{		 
		
		FileReader fr;
		fr = new FileReader(new File(filePath));
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
					statement = getConnection().createStatement();
					statement.execute(stat.toString());
					stat=new StringBuffer();
				}
			}
			str = br.readLine();
		}

	}

	@Override
	public void updateDatabase() throws DatabaseLoadDriverException, SQLException {
		int databaseVersion = HelpDatabase.getMaxDatabaseVersion();
		int newVersionID = UpdateDatabaseHelp.readDatabaseFileDataBase();
		for(int i=databaseVersion+1;i<=newVersionID;i++)
		{
			String path = GlobalOptions.mysqlDatbaseUpdateFolder + "/" + GlobalOptions.mysqlDatbaseUpdateStartNameFile + i + GlobalOptions.mysqlDatbaseUpdateEndNameFile; 
			String filePathComments =  GlobalOptions.mysqlDatbaseUpdateFolder + "/" + GlobalOptions.mysqlDatbaseUpdateStartNameFile + i + GlobalOptions.mysqlDatbaseUpdateEndNameInfoFile;
			String comments = "";
			try {
				comments = FileHandling.getFileContent(new File(filePathComments) );
				updateDatabaseStepFrom(path);
				HelpDatabase.insertNewVersionRegestry(i,comments);
				GlobalOptions.anote2DatabaseVersion = i;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean isDatabaseOutOfDate() {
		return UpdateDatabaseHelp.isDatabaseOutOfDate();
	}

}
