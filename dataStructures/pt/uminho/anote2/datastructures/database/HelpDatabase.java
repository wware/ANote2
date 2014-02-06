package pt.uminho.anote2.datastructures.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.queries.QueriesGeneral;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;

public class HelpDatabase {
	
	public final static int synonynSize = 250;
	
	/**
	 * 
	 * @param db
	 * @param tableName
	 * @return
	 */
	public static int getNextInsertTableID(IDatabase db,String tableName)
	{
				
		Connection connection = db.getConnection();
		if(connection == null)
		{
			db.openConnection();
			if(db.getConnection()==null)
			{
				return -1;
			}
		}
		try {
			java.sql.PreparedStatement showTableInfoPS = connection.prepareStatement(QueriesGeneral.getTableInformation);
			showTableInfoPS.setString(1,tableName);
			ResultSet rs = showTableInfoPS.executeQuery();		
			if(rs.next())
			{
				int id = rs.getInt(11);
				rs.close();
				//db.closeConnection();
				return id;
			}
			else
			{
				rs.close();
				//db.closeConnection();
				return -2;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			//db.closeConnection();
			return -1;
		}

	}
	
	public static  int initArticleTypeID(IDatabase db,String type) {
		Connection connection = db.getConnection();
		if(connection == null)
		{
			db.openConnection();
			if(db.getConnection()==null)
			{
				return -1;
			}
		}

		try{
			java.sql.PreparedStatement getPmidTypeIDPS = connection.prepareStatement(QueriesPublication.selectPublicationIndetifierTypeID);
			getPmidTypeIDPS.setString(1, type);
			ResultSet res = getPmidTypeIDPS.executeQuery();
			if(res.next())
			{
				return res.getInt(1);
			}
			else
			{
				java.sql.PreparedStatement insertPmidTypePS = connection.prepareStatement(QueriesPublication.insertIndetifierType);
				insertPmidTypePS.setString(1,type);
				int pmidIDtype = HelpDatabase.getNextInsertTableID(db,"publications_id_type");
				insertPmidTypePS.execute();
				return pmidIDtype;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	
	}	

}
