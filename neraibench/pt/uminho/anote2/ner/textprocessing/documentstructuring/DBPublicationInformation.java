package pt.uminho.anote2.ner.textprocessing.documentstructuring;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBPublicationInformation {

	private PreparedStatement pStatement;
		
	public DBPublicationInformation(String dbHost, String dbPort, String dbSchema, String dbUser, String dbPwd) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		String driver_class_name = "com.mysql.jdbc.Driver";
		String url_db_connection = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbSchema;
		
		Class.forName(driver_class_name).newInstance();
		Connection connection = (Connection) DriverManager.getConnection(url_db_connection,	dbUser, dbPwd);
		
		String query = "SELECT ? FROM publications WHERE pmid=?";
		pStatement = connection.prepareStatement(query);
	}

	public Map<String,String> getPieces(String pmid, List<String> fields) throws SQLException{
		Map<String,String> pieces = new HashMap<String, String>();
		for(String field: fields)
		{
			String piece = getField(field, pmid);
			if(piece!=null)
				pieces.put(field, piece);
		}
		return pieces;
	}

	public String getField(String field, String pmid) throws SQLException{
		
		pStatement.setString(1, field);
		pStatement.setString(2, pmid);
		
		ResultSet rs = pStatement.executeQuery();
		String result = null;
		if(rs.next())
			result = rs.getString(1);
		return result;
	}
	
	
}
