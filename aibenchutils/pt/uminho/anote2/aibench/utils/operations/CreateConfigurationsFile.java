package pt.uminho.anote2.aibench.utils.operations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import pt.uminho.anote2.aibench.utils.conf.Configuration;
import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.configuration.IProxy;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.MySQLDatabase;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation(name="Create Configuration File", description="")
public class CreateConfigurationsFile {
	
	private IProxy proxy;
	
	@Port(direction=Direction.INPUT, name="proxy", order=1)
	public void setProxy(IProxy proxy) {
		this.proxy=proxy;
	}
	
	@Port(direction=Direction.INPUT, name="database", order=2)
	public void setDatabase(IDatabase database) {
		try {
			createConnection(database.getUser(),database.getPwd());			
		} catch (Exception e) {
			e.printStackTrace();
			Workbench.getInstance().warn("Error Creating @Note2 Database");
			return;
		}
				
		Configuration.saveSettings("localhost","3306","anote_db",
				database.getUser(),database.getPwd(),
				proxy.isEnable(),proxy.getProxyHost(),proxy.getProxyPort());
		
		new ShowMessagePopup("Configuration Complete !!!");
	}
	
	public void createConnection(String user,String pwd) throws Exception{
					
		IDatabase db = new MySQLDatabase("localhost","3306","mysql",user,pwd);
		db.openConnection();
		if(existDataBase("anote_db",user,pwd))
		{
	        if(overwriteDatabase())
	        {
	        	db.createDataBase(GlobalOptions.mysqlDatabaseFile);
	        }
		}
		else
		{
			db.createDataBase(GlobalOptions.mysqlDatabaseFile);
		}
	}
	
	private boolean existDataBase(String dataBase,String user,String pwd) throws SQLException
	{
		MySQLDatabase db = new MySQLDatabase("localhost","3306","mysql",user,pwd);
		db.openConnection();
		Statement statement = db.getConnection().createStatement();
		ResultSet result = statement.executeQuery("SHOW DATABASES");
		while(result.next())
		{
			if(result.getString(1).equals(dataBase))
			{
				db.closeConnection();
				return true;
				
			}
		}
		db.closeConnection();
		return false;
	}
	
	private boolean overwriteDatabase(){
		Object[] options = new String[]{"Overwrite", "Continue"};
		int opt = HelpAibench.showOptionPane("Database Warning", "Database allready exist", options);
		switch (opt) {
		case 0:
			return true;
		default:
			return false;
		}
	}			 
}
